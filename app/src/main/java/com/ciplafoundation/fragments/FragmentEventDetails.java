package com.ciplafoundation.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciplafoundation.R;
import com.ciplafoundation.application.AppController;
import com.ciplafoundation.model.EventDetails;
import com.ciplafoundation.utility.Util;

/**
 * Created by User-66-pc on 4/4/2017.
 */

public class FragmentEventDetails extends Fragment {


    private static Activity mActivity;
    private static LinearLayout ll_pending_approved;
    private LinearLayout ll_general_details,ll_image,ll_image_distribution;
    private static TextView tv_heading;
    private static EventDetails arrEventDetails;

    public static FragmentEventDetails newInstance(TextView _tv_heading, LinearLayout _ll_pending_approved, EventDetails _arrEventDetails) {
        FragmentEventDetails f = new FragmentEventDetails();
        Bundle b = new Bundle();
        f.setArguments(b);
        arrEventDetails=_arrEventDetails;
        tv_heading=_tv_heading;
        ll_pending_approved=_ll_pending_approved;
        return f;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        initComponent(rootView);
        return rootView;
    }


    private void initComponent(View v) {
        mActivity = getActivity();
        ll_pending_approved.setVisibility(View.GONE);

        ll_general_details=(LinearLayout) v.findViewById(R.id.ll_general_details);
        ll_image=(LinearLayout) v.findViewById(R.id.ll_image);
        ll_image_distribution=(LinearLayout)v.findViewById(R.id.ll_image_distribution);
        setData();
        setImage();
    }

    private void setData() {

        if(arrEventDetails!=null) {

            inflateViewData("Project",arrEventDetails.getProject(),"",ll_general_details);
            inflateViewData("Involved Person",arrEventDetails.getInvolved_person(),"",ll_general_details);
            inflateViewData("District Name",arrEventDetails.getDistrict_name(),"",ll_general_details);
            inflateViewData("Block Name",arrEventDetails.getBlock_name(),"",ll_general_details);
            inflateViewData("Village Name",arrEventDetails.getVillage_name(),"",ll_general_details);
            inflateViewData("Organized On",arrEventDetails.getOrganized_on(),"",ll_general_details);
            inflateViewData("Venue",arrEventDetails.getVenu(),"",ll_general_details);
            inflateViewData("Title",arrEventDetails.getTitle(),"",ll_general_details);
            inflateViewData("Organized By",arrEventDetails.getOrganized_by(),"",ll_general_details);
            inflateViewData("Objective",arrEventDetails.getObjective(),"",ll_general_details);
            inflateViewData("Description",arrEventDetails.getDescription(),"",ll_general_details);


        }

    }

    private void setImage() {
        ll_image_distribution.setVisibility(View.VISIBLE);
        if(arrEventDetails!=null) {

            if(arrEventDetails.getImageUrls()!=null && arrEventDetails.getImageUrls().size()>0)
            {
                for (int i=0;i<arrEventDetails.getImageUrls().size();i++) {
                    View view=LayoutInflater.from(mActivity).inflate(R.layout.view_image,null);

                    final ImageView imageView = (ImageView) view.findViewById(R.id.iv_imageSlide);
                    imageView.setTag(arrEventDetails.getImageUrls().get(i));
                    AppController.getInstance().displayUniversalImg(arrEventDetails.getImageUrls().get(i),imageView,R.drawable.no_image);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Dialog dialogFullImage=new Dialog(mActivity);

                            dialogFullImage.setContentView(R.layout.dialog_image_fullimage);
                            ImageView imageViewFull=(ImageView)dialogFullImage.findViewById(R.id.imageViewFull) ;
                            AppController.getInstance().displayUniversalImg(imageView.getTag().toString(),imageViewFull,R.drawable.no_image);
                            dialogFullImage.show();
                        }
                    });
                    ll_image_distribution.addView(view);
                }
            }


        }


    }


    private void inflateViewData(String title, String value, String subValue, LinearLayout parent)
    {
        View view=LayoutInflater.from(mActivity).inflate(R.layout.view_inflate_data,null);
        TextView tv_data=(TextView)view.findViewById(R.id.tv_data);

        if(!Util.textIfNull(subValue).equalsIgnoreCase(""))
            tv_data.setText(Html.fromHtml("<big><b>" + Util.textIfNull(title) + "</b></big> <br>" + Util.textIfNull(value) + "<br><small>+ "+subValue+"</small>"),TextView.BufferType.SPANNABLE);
        else
            tv_data.setText(Html.fromHtml("<big><b>" + Util.textIfNull(title) + "</b></big> <br>" + Util.textIfNull(value)),TextView.BufferType.SPANNABLE);

        parent.addView(view);
    }




}
