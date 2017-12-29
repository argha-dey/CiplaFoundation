package com.ciplafoundation.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciplafoundation.R;
import com.ciplafoundation.activities.FragmentBaseActivity;
import com.ciplafoundation.adapter.AdapterEvent;
import com.ciplafoundation.model.EventDetails;

import java.util.ArrayList;

/**
 * Created by User-66-pc on 3/28/2017.
 */

public class FragmentEvent extends Fragment implements View.OnClickListener,AdapterEvent.OnEditButtonClickListener
        //implements SwipeRefreshLayout.OnRefreshListener
{

    private static FragmentBaseActivity mActivity;
    private static TextView tv_heading;
    private static LinearLayout ll_pending_approved;
    private RelativeLayout rl_addEvent;
    private TextView tv_noRecord;
    private RecyclerView rv_event_list;
    private FragmentTransaction fragmentTransaction;
    private LinearLayoutManager mApproveLayoutManager;
    private AdapterEvent adapterEvent;
    private static ArrayList<EventDetails> mArrEvent;
    private String headerText="Event List";
    //private SwipeRefreshLayout swipeRefreshLayout;

    public static FragmentEvent newInstance(Activity activity,TextView _tv_heading, LinearLayout _ll_pending_approved,ArrayList<EventDetails> arrEvent) {
        FragmentEvent f = new FragmentEvent();
        Bundle b = new Bundle();
        f.setArguments(b);
        tv_heading=_tv_heading;
        ll_pending_approved=_ll_pending_approved;
        mArrEvent=arrEvent;
        mActivity =(FragmentBaseActivity)activity;
        return f;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        //Log.d("test","Fragment Event");
        initComponent(rootView);

        return rootView;
    }


    private void initComponent(View view) {
        rv_event_list = (RecyclerView) view.findViewById(R.id.rv_event_list);
        rl_addEvent   = (RelativeLayout) view.findViewById(R.id.rl_addEvent);
        //swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        tv_noRecord = (TextView)view.findViewById(R.id.tv_noRecord);
        ll_pending_approved.setVisibility(View.GONE);
        tv_heading.setVisibility(View.GONE);
        mApproveLayoutManager = new LinearLayoutManager(mActivity);
        rv_event_list.setLayoutManager(mApproveLayoutManager);
        adapterEvent = new AdapterEvent(mActivity,mArrEvent);
        adapterEvent.setClickListener(this);

        if(mArrEvent!=null && mArrEvent.size()>0)
        {
            tv_heading.setText(headerText);
            tv_heading.setTextColor(getContext().getResources().getColor(R.color.approved));
            tv_noRecord.setVisibility(View.GONE);
            rv_event_list.setAdapter(adapterEvent);
        }
        else
        {
            tv_heading.setText(headerText);
            tv_heading.setTextColor(getContext().getResources().getColor(R.color.approved));
            tv_noRecord.setVisibility(View.VISIBLE);
            rv_event_list.setVisibility(View.GONE);
        }

        rl_addEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.rl_addEvent)
        {
            FragmentAddEvent fragmentAddEvent=FragmentAddEvent.newInstance(mActivity,tv_heading, ll_pending_approved);
            mActivity. fragmentManager =mActivity.getSupportFragmentManager();
            fragmentTransaction = mActivity.fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.ll_main_container, fragmentAddEvent);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onEditClick(int position) {
        FragmentAddEvent fragmentAddEvent=FragmentAddEvent.newInstance(mActivity,tv_heading, ll_pending_approved);
        mActivity. fragmentManager =mActivity.getSupportFragmentManager();
        fragmentTransaction = mActivity.fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("eventId", mArrEvent.get(position).getId());
        fragmentAddEvent.setArguments(bundle);
        fragmentTransaction.replace(R.id.ll_main_container, fragmentAddEvent);
        fragmentTransaction.commit();
    }
}
