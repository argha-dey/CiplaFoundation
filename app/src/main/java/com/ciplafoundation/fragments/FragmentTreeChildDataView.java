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
import android.widget.TextView;

import com.ciplafoundation.R;
import com.ciplafoundation.adapter.AdapterPendingProposalList;

public class FragmentTreeChildDataView extends Fragment

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

    public static FragmentTreeChildDataView newInstance(TextView _tv_heading, LinearLayout _ll_pending_approved) {
        FragmentTreeChildDataView f = new FragmentTreeChildDataView();
        Bundle b = new Bundle();
        f.setArguments(b);
        tv_heading = _tv_heading;
        ll_pending_approved = _ll_pending_approved;
        return f;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Log.d("test","Fragment Tree");
        View rootView = inflater.inflate(R.layout.fragment_fragment_tree_child_data_view, container, false);
        initComponent(rootView);
        return rootView;
    }


    private void initComponent(View view) {
        activity = getActivity();


        tv_norecord = (TextView) view.findViewById(R.id.tv_header);
        tv_norecord.setTextColor(getContext().getResources().getColor(R.color.red));

    }
}













  /*  public void setRecycleView()
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
    }*/



