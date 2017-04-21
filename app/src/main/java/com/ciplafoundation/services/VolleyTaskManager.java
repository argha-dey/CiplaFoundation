package com.ciplafoundation.services;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ciplafoundation.application.AppController;
import com.ciplafoundation.R;
import com.ciplafoundation.utility.ServerResponseCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import android.util.Log;

@SuppressLint("ShowToast")
public class VolleyTaskManager extends ServiceConnector {
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private String TAG = "";
    private String tag_json_obj = "jobj_req";
    private boolean isToShowDialog = true, isToHideDialog = true;

    public VolleyTaskManager(Context context) {
        mContext = context;

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading...");

        TAG = mContext.getClass().getSimpleName();
        //Log.d("tag", TAG);
    }

    public void showProgressDialog() {
        if (!mProgressDialog.isShowing())
            try {
                mProgressDialog.show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    public void hideProgressDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    /**
     * Making json object request
     */
    private void makeJsonObjReq(int method, String url, final Map<String, String> paramsMap) {
        if (isToShowDialog) {
            showProgressDialog();
        }

        //Log.v("JSONObject", new JSONObject(paramsMap).toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(method, url, new JSONObject(paramsMap),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, response.toString());
                        // msgResponse.setText(response.toString());
                        if (isToHideDialog) {
                            hideProgressDialog();
                        }
                        // TODO On getting successful result:
                        ((ServerResponseCallback) mContext).onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //Log.d("error ocurred","TimeoutError");
                    Toast.makeText(mContext, mContext.getString(R.string.response_timeout), Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //Log.d("error ocurred","AuthFailureError");
                    Toast.makeText(mContext, mContext.getString(R.string.auth_failure), Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Log.d("error ocurred","ServerError");
                    Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Log.d("error ocurred","NetworkError");
                    Toast.makeText(mContext, mContext.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    //Log.d("error ocurred","ParseError");
                    Toast.makeText(mContext, mContext.getString(R.string.parse_error), Toast.LENGTH_LONG).show();
                }

                ((ServerResponseCallback) mContext).onError();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                /*Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Androidhive");
				params.put("email", "abc@androidhive.info");
				params.put("pass", "password123");*/
                return paramsMap;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


    /**
     * Service method calling for Login -->
     **/

    public void doLogin(HashMap<String, String> paramsMap, boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/logins/post";
        int method = Method.POST;

        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, paramsMap);
    }


    /**
     * Service method calling for Role -->
     **/

    public void doGetUserRole(String params,boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/userrole/get?"+params;
        int method = Method.GET;

        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    /**
     * Service method calling for User Division -->
     **/

    public void doGetUserDivision(String params,boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "login/userdivision/get?"+params;
        int method = Method.GET;

        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doGetProposalList(String params,boolean isToHideDialog) {
        //this.isToShowDialog = isToHideDialog;
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "proposal/lists/get?"+params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

    public void doGetProposalDetails(String params,boolean isToHideDialog) {
        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "proposal/view/get?"+params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }
    public void doGetTreeList(String params,boolean isToHideDialog) {

        this.isToHideDialog = isToHideDialog;
        String url = getBaseURL() + "tree/lists/get?"+params;
        int method = Method.GET;
        Log.i("url", url);
        //System.out.println(paramsMap);
        makeJsonObjReq(method, url, new HashMap<String, String>());
    }

}
