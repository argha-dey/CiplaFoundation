package com.ciplafoundation.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciplafoundation.R;
import com.ciplafoundation.activities.FragmentBaseActivity;
import com.ciplafoundation.model.DataProposalList;
import com.ciplafoundation.adapter.AdapterPendingProposalList;
import com.ciplafoundation.model.PendingProposal;
import com.ciplafoundation.utility.Util;

import java.util.ArrayList;

/**
 * Created by User-66-pc on 3/28/2017.
 */

public class FragmentPending extends Fragment
        //implements SwipeRefreshLayout.OnRefreshListener
{

    private Activity activity;
    private static TextView tv_heading;
    private static LinearLayout ll_pending_approved;
    private RecyclerView rv_pending_proposal_list;
    private TextView tv_norecord;
    private FragmentTransaction fragmentTransaction;
    private LinearLayoutManager mPendingLayoutManager;
    private AdapterPendingProposalList adapterPendingProposalList;
   // private SwipeRefreshLayout swipeRefreshLayout;

    public static FragmentPending newInstance(TextView _tv_heading,LinearLayout _ll_pending_approved) {
        FragmentPending f = new FragmentPending();
        Bundle b = new Bundle();
        f.setArguments(b);
        tv_heading=_tv_heading;
        ll_pending_approved=_ll_pending_approved;
        return f;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pending, container, false);
        initComponent(rootView);
        return rootView;
    }


    private void initComponent(View view) {
        activity = getActivity();
        ll_pending_approved.setVisibility(View.VISIBLE);
        rv_pending_proposal_list = (RecyclerView) view.findViewById(R.id.rv_pending_proposal_list);
        tv_norecord = (TextView) view.findViewById(R.id.tv_norecord);
        tv_heading.setVisibility(View.VISIBLE);
        //swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        //swipeRefreshLayout.setOnRefreshListener(this);
        setRecycleView();

    }

    public void setRecycleView()
    {
        mPendingLayoutManager = new LinearLayoutManager(activity);
        rv_pending_proposal_list.setLayoutManager(mPendingLayoutManager);
        adapterPendingProposalList = new AdapterPendingProposalList(activity);
        ArrayList<PendingProposal> arrPendingProposal= Util.fetchPendingProposal(activity);
        if(arrPendingProposal!=null && arrPendingProposal.size()>0)
        {
            tv_heading.setText("Pending Proposal List");
            tv_heading.setTextColor(getContext().getResources().getColor(R.color.pending));
            tv_norecord.setVisibility(View.GONE);
            adapterPendingProposalList.AddArray(arrPendingProposal);
            rv_pending_proposal_list.setAdapter(adapterPendingProposalList);
        }
        else{
            tv_heading.setText("Pending Proposal List");
            tv_heading.setTextColor(getContext().getResources().getColor(R.color.pending));
            tv_norecord.setVisibility(View.VISIBLE);
            rv_pending_proposal_list.setVisibility(View.GONE);
        }
    }

   /* @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        if(Util.checkConnectivity(activity)) {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((FragmentBaseActivity) activity).proposalListWebServiceCalling();
                    swipeRefreshLayout.setRefreshing(false);
                    setRecycleView();
                }
            },4000);

        }
        else {
            swipeRefreshLayout.setRefreshing(false);
            Util.showMessageWithOk(activity, getString(R.string.no_internet));
        }
    }*/
}
