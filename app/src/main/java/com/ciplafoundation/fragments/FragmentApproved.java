package com.ciplafoundation.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciplafoundation.R;
import com.ciplafoundation.activities.FragmentBaseActivity;
import com.ciplafoundation.adapter.AdapterApprovedProposalList;
import com.ciplafoundation.model.AcceptedProposal;
import com.ciplafoundation.model.DataProposalList;
import com.ciplafoundation.utility.Util;

import java.util.ArrayList;

/**
 * Created by User-66-pc on 3/28/2017.
 */

public class FragmentApproved extends Fragment
        //implements SwipeRefreshLayout.OnRefreshListener
{

    private Activity activity;
    private static TextView tv_heading;
    private static LinearLayout ll_pending_approved;
    private TextView tv_noRecord;
    private RecyclerView rv_approved_proposal_list;
    private FragmentTransaction fragmentTransaction;
    private LinearLayoutManager mApproveLayoutManager;
    private AdapterApprovedProposalList adapterApprovedProposalList;
    //private SwipeRefreshLayout swipeRefreshLayout;

    public static FragmentApproved newInstance(TextView _tv_heading,LinearLayout _ll_pending_approved) {
        FragmentApproved f = new FragmentApproved();
        Bundle b = new Bundle();
        f.setArguments(b);
        tv_heading=_tv_heading;
        ll_pending_approved=_ll_pending_approved;
        return f;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_approved, container, false);

        initComponent(rootView);

        return rootView;
    }


    private void initComponent(View view) {
        activity = getActivity();
        ll_pending_approved.setVisibility(View.VISIBLE);
        rv_approved_proposal_list = (RecyclerView) view.findViewById(R.id.rv_approved_proposal_list);
        //swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        tv_noRecord = (TextView)view.findViewById(R.id.tv_noRecord);
        //swipeRefreshLayout.setOnRefreshListener(this);
        /*swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        //fetchMovies();
                                    }
                                }
        );*/

        tv_heading.setVisibility(View.VISIBLE);
        mApproveLayoutManager = new LinearLayoutManager(activity);
        rv_approved_proposal_list.setLayoutManager(mApproveLayoutManager);
        adapterApprovedProposalList = new AdapterApprovedProposalList(activity);
        ArrayList<AcceptedProposal> arrApprovedProposal= Util.fetchAcceptedProposal(activity);
        if(arrApprovedProposal!=null && arrApprovedProposal.size()>0)
        {
            tv_heading.setText("Approved Proposal List");
            tv_heading.setTextColor(getContext().getResources().getColor(R.color.approved));
            tv_noRecord.setVisibility(View.GONE);
            adapterApprovedProposalList.AddArray(arrApprovedProposal);
            rv_approved_proposal_list.setAdapter(adapterApprovedProposalList);
        }
        else
        {
            tv_heading.setText("Approved Proposal List");
            tv_heading.setTextColor(getContext().getResources().getColor(R.color.approved));
            tv_noRecord.setVisibility(View.VISIBLE);
            rv_approved_proposal_list.setVisibility(View.GONE);
        }


    }

   /* @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        if(Util.checkConnectivity(activity)) {
            ((FragmentBaseActivity) activity).proposalListWebServiceCalling();
            swipeRefreshLayout.setRefreshing(false);
        }
        else
        Util.showMessageWithOk(activity,getString(R.string.no_internet));
    }*/
}
