package com.ciplafoundation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ciplafoundation.R;
import com.ciplafoundation.application.AppController;
import com.ciplafoundation.model.EventDetails;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.services.VolleyTaskManager;
import com.ciplafoundation.utility.CustomTextView;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.Util;

import java.util.ArrayList;


public class AdapterEvent extends RecyclerView.Adapter<AdapterEvent.MyViewHolder> {
    private Context mContext;
    private ArrayList<EventDetails> eventList=new ArrayList<>();
    private UserClass user=new UserClass();
    private VolleyTaskManager volleyTaskManager;
    private Prefs prefs;
    private OnEditButtonClickListener onEditButtonClickListener;
    public AdapterEvent(Context _mContext,ArrayList<EventDetails> eventList) {
        this.mContext = _mContext;
        this.eventList=eventList;
        volleyTaskManager=new VolleyTaskManager(_mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
       /* return new MyViewHolder(itemView);*/
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_event_title.setText(Html.fromHtml("<u>"+eventList.get(position).getTitle()+"</u>"));
        holder.tv_details.setText(eventList.get(position).getDescription());
        holder.tv_postedBy.setText("By: "+eventList.get(position).getCreated_by());
        holder.tv_composite.setText(eventList.get(position).getDivision()+" | "+eventList.get(position).getDate());
        holder.tv_upcoming.setText(eventList.get(position).getEventSchedule());
        AppController.getInstance().displayUniversalImg(eventList.get(position).getImage_url(),holder.iv_event_image,R.drawable.no_image);
        holder.tv_event_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user= Util.fetchUserClass(mContext);
                String id=eventList.get(position).getId();
                String user_id=user.getUserId();
                String params="USR_USER_ID="+user_id+"&EVENT_ID="+id;
                prefs=new Prefs(mContext);
                volleyTaskManager.doGetEventDetails(params, true);
                prefs.setIsEventDetails(true);

            }
        });


        if(eventList.get(position).getEdit_enable().equalsIgnoreCase("true"))
            holder.btn_edit.setVisibility(View.VISIBLE);
        else
            holder.btn_edit.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView tv_event_title,tv_composite,tv_details,
                tv_postedBy,tv_upcoming;
        public ImageView iv_event_image;
        public Button btn_edit;


        public MyViewHolder(View itemView) {
            super(itemView);
            tv_event_title = (CustomTextView) itemView.findViewById(R.id.tv_event_title);
            tv_composite = (CustomTextView) itemView.findViewById(R.id.tv_composite);
            tv_details = (CustomTextView) itemView.findViewById(R.id.tv_details);
           // tv_clickToRead = (CustomTextView) itemView.findViewById(R.id.tv_clickToRead);
            tv_postedBy = (CustomTextView) itemView.findViewById(R.id.tv_postedBy);
            tv_upcoming = (CustomTextView) itemView.findViewById(R.id.tv_upcoming);
            iv_event_image = (ImageView) itemView.findViewById(R.id.iv_event_image);
            btn_edit = (Button) itemView.findViewById(R.id.btn_edit);

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEditButtonClickListener != null)
                        onEditButtonClickListener.onEditClick(getAdapterPosition());
                }
            });
        }



    }
    // allows clicks events to be caught
    public void setClickListener(OnEditButtonClickListener OnEditButtonClickListener) {
        this.onEditButtonClickListener = OnEditButtonClickListener;
    }
     public  interface  OnEditButtonClickListener
     {
          public void onEditClick(int position);
      }
}
