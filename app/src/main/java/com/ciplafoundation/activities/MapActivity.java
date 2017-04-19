package com.ciplafoundation.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ciplafoundation.R;
import com.ciplafoundation.model.UserDivision;
import com.ciplafoundation.services.VolleyTaskManager;
import com.ciplafoundation.utility.AlertDialogCallBack;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.ServerResponseCallback;
import com.ciplafoundation.utility.Util;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapActivity extends BaseActivity implements
        OnMapReadyCallback,
        ServerResponseCallback,
        GoogleMap.OnMarkerClickListener

{

    private GoogleMap googleMap;
    private Context mContext;
    private VolleyTaskManager volleyTaskManager;
    private Prefs prefs;
    private boolean isUserDivision=false;
    private String user_id="";
    private ArrayList<UserDivision> arrlistUserDivision=new ArrayList<>();
    private ArrayList<Marker> mapmarker;
    private Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_map);
        init();
        initMap();
    }

    private void init()
    {
        mContext=MapActivity.this;
        volleyTaskManager=new VolleyTaskManager(mContext);
        prefs = new Prefs(mContext);
        user_id=Util.fetchUserClass(mContext).getUserId();
        setViewShowHide(false,false,true);
        if (Util.checkConnectivity(mContext))
        UserDivisionWebServiceCalling(user_id);
        else
        {
            //Util.showMessageWithOk(mContext,getString(R.string.no_internet));
            Util.showCallBackMessageWithOk(mContext,getString(R.string.no_internet),new AlertDialogCallBack() {
                        @Override
                        public void onSubmit() {
                            finish();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }

    }

    private void initMap() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap=map;
        //googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.setMinZoomPreference(4.0f);
        /*googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LatLng latLng = new LatLng(20.5937,78.9629);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                return true;
            }
        });*/

        LatLng latLng = new LatLng(20.5937,78.9629);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        //googleMap.addMarker(new MarkerOptions().position(latLng).title("Chiranjit"));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                View v=getLayoutInflater().inflate(R.layout.info_window,null);
                TextView tv_info_division_name=(TextView)v.findViewById(R.id.tv_info_division_name);
                tv_info_division_name.setText(marker.getTitle());
                return v;
            }
        });
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(marker.isInfoWindowShown())
                    marker.hideInfoWindow();
                String division_id=marker.getSnippet();
                prefs.setDivisionId(division_id);
                Intent i=new Intent(mContext, TaskActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
            }
        });


    }

    private void UserDivisionWebServiceCalling(String user_id)
    {
        isUserDivision=true;
        String params="USR_USER_ID="+user_id;
        volleyTaskManager.doGetUserDivision(params,true);
    }

    @Override
    public void onSuccess(JSONObject resultJsonObject) {

        Log.v("resultJsonObject",""+resultJsonObject);
        if(isUserDivision)
        {
            isUserDivision=false;
            if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {

                JSONArray detailsJsonArray=resultJsonObject.optJSONArray("details");
                if (detailsJsonArray != null && detailsJsonArray.length() > 0) {
                    arrlistUserDivision.clear();
                    for (int i = 0; i < detailsJsonArray.length(); i++) {
                        JSONObject detailsJsonObject = detailsJsonArray.optJSONObject(i);

                        UserDivision userDivision=new UserDivision();
                        userDivision.setDivisionId(detailsJsonObject.optString("id"));
                        userDivision.setName(detailsJsonObject.optString("name"));
                        userDivision.setLat(detailsJsonObject.optString("lat"));
                        userDivision.setLng(detailsJsonObject.optString("lng"));
                        //userDivision.setLatLng(new LatLng(detailsJsonObject.optDouble("lat"),detailsJsonObject.optDouble("lng")));
                        userDivision.setState(detailsJsonObject.optString("state"));
                        userDivision.setCount(detailsJsonObject.optString("count"));
                        arrlistUserDivision.add(userDivision);
                    }
                    Util.saveUserDivision(MapActivity.this, arrlistUserDivision);
                }
                addMarker();
                setDivisionList();

            } else {
                Util.showMessageWithOk(mContext, "User Division failed !!!");
                arrlistUserDivision.clear();
                //populateUserDivisionList();
            }

        }
    }

    @Override
    public void onError() {

    }

    /****** Add marker on map *********/
    void addMarker() {
        googleMap.clear();
        mapmarker = new ArrayList<Marker>();
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        TextView tv_divisionName=(TextView)v.findViewById(R.id.tv_divisionName);
        Log.v("arrlist Size: ",""+arrlistUserDivision.size());

        for (int j = 0; j < arrlistUserDivision.size(); j++) {
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(8), 2000, null);
            tv_divisionName.setText(arrlistUserDivision.get(j).getName());
           /* marker = googleMap.addMarker(new MarkerOptions().position(arrlistUserDivision.get(j).getLatLng()).snippet(String.valueOf(j))*/
            marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(arrlistUserDivision.get(j).getLat()),Double.parseDouble(arrlistUserDivision.get(j).getLng())))
                        .snippet(String.valueOf(j))
                        //.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, v)))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue))
                        .snippet(arrlistUserDivision.get(j).getDivisionId())
                        .title(arrlistUserDivision.get(j).getName()));

            mapmarker.add(marker);
        }

    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //Toast.makeText(mContext,""+marker.getSnippet(),Toast.LENGTH_SHORT).show();
        return false;
    }
}
