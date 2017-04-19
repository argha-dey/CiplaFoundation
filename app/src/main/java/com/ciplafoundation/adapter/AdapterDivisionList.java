package com.ciplafoundation.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ciplafoundation.R;
import com.ciplafoundation.activities.FragmentBaseActivity;
import com.ciplafoundation.activities.TaskActivity;
import com.ciplafoundation.model.UserDivision;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.Util;

import java.util.ArrayList;


/**
 * Created by User-66-pc on 3/28/2017.
 */

public class AdapterDivisionList extends RecyclerView.Adapter<AdapterDivisionList.MyViewHolder>
{
    private Context mContext;
    //private Activity activity;
    public PopupWindow popupWindow;
    public RelativeLayout rl_divisionList;
    private ArrayList<UserDivision> arrUserDivision=new ArrayList<>();
    private Prefs prefs;


    public AdapterDivisionList(Context mContext, PopupWindow popupWindow, RelativeLayout rl_divisionList)
    {
        this.mContext=mContext;
        this.popupWindow=popupWindow;
        this.rl_divisionList=rl_divisionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.divisionlist_single_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_divisionName.setText(arrUserDivision.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrUserDivision.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_divisionName;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_divisionName = (TextView) itemView.findViewById(R.id.tv_list_divisionName);
        }

        @Override
        public void onClick(View v) {

            rl_divisionList.setBackgroundResource(R.color.header_bg);
            popupWindow.dismiss();
            String division_id=arrUserDivision.get(getAdapterPosition()).getDivisionId();
            prefs = new Prefs(mContext);
            prefs.setDivisionId(division_id);
            Intent i=new Intent(mContext, TaskActivity.class);
            Activity activity = (Activity) mContext;
            mContext.startActivity(i);
            activity.overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);


        }
    }

    public void AddArray(ArrayList<UserDivision> _arrDataFormListItems) {
        arrUserDivision.clear();
        if (_arrDataFormListItems != null
                && _arrDataFormListItems.size() > 0) {
            arrUserDivision.addAll(_arrDataFormListItems);
        }
        notifyDataSetChanged();

    }
}
