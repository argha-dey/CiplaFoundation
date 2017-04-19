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
import com.ciplafoundation.model.PendingProposal;
import com.ciplafoundation.model.ProposalDetails;
import com.ciplafoundation.utility.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by User-66-pc on 4/4/2017.
 */

public class FragmentProposalDetails extends Fragment {

    private Activity activity;
    private static LinearLayout ll_pending_approved;
    private static TextView tv_heading,tv_schedule_data_view,tv_type_data_view,tv_ngodata_view,tv_fund_duration_data_view,
            tv_proposal_title_data_view,tv_proposal_det_brief_data_view,tv_timeline_data_view,tv_prop_duration_data_view,
            tv_fund_req_data_view,tv_district_data_view,tv_block_data_view,tv_vill_data_view,tv_loc_data_view,tv_distance_data_view,
            tv_benificiary_data_view,tv_rationale_data_view;
    private static ArrayList<ProposalDetails> arrProposalDetails=new ArrayList<>();

    public static FragmentProposalDetails newInstance(TextView _tv_heading,LinearLayout _ll_pending_approved,ArrayList<ProposalDetails> _arrProposalDetails) {
        FragmentProposalDetails f = new FragmentProposalDetails();
        Bundle b = new Bundle();
        f.setArguments(b);
        arrProposalDetails=_arrProposalDetails;
        tv_heading=_tv_heading;
        ll_pending_approved=_ll_pending_approved;
        return f;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_project_details, container, false);
        initComponent(rootView);
        return rootView;
    }


    private void initComponent(View v) {
        activity = getActivity();
        ll_pending_approved.setVisibility(View.GONE);
        tv_schedule_data_view=(TextView)v.findViewById(R.id.tv_schedule_data_view);
        tv_type_data_view=(TextView)v.findViewById(R.id.tv_type_data_view);
        tv_ngodata_view=(TextView)v.findViewById(R.id.tv_ngodata_view);
        tv_fund_duration_data_view=(TextView)v.findViewById(R.id.tv_fund_duration_data_view);
        tv_proposal_title_data_view=(TextView)v.findViewById(R.id.tv_proposal_title_data_view);
        tv_proposal_det_brief_data_view=(TextView)v.findViewById(R.id.tv_proposal_det_brief_data_view);
        tv_timeline_data_view=(TextView)v.findViewById(R.id.tv_timeline_data_view);
        tv_prop_duration_data_view=(TextView)v.findViewById(R.id.tv_prop_duration_data_view);
        tv_fund_req_data_view=(TextView)v.findViewById(R.id.tv_fund_req_data_view);
        tv_district_data_view=(TextView)v.findViewById(R.id.tv_district_data_view);
        tv_block_data_view=(TextView)v.findViewById(R.id.tv_block_data_view);
        tv_vill_data_view=(TextView)v.findViewById(R.id.tv_vill_data_view);
        tv_loc_data_view=(TextView)v.findViewById(R.id.tv_loc_data_view);
        tv_benificiary_data_view=(TextView)v.findViewById(R.id.tv_benificiary_data_view);
        tv_rationale_data_view=(TextView)v.findViewById(R.id.tv_rationale_data_view);

        setdata();

    }

    private void setdata() {

        if(arrProposalDetails!=null && arrProposalDetails.size()>0) {
            tv_schedule_data_view.setText(arrProposalDetails.get(0).getSchedule_vii());
            tv_type_data_view.setText(arrProposalDetails.get(0).getNgo_type());
            tv_ngodata_view.setText(arrProposalDetails.get(0).getNgo_vendor());
            tv_fund_duration_data_view.setText(arrProposalDetails.get(0).getDuration_funding());
            tv_proposal_title_data_view.setText(arrProposalDetails.get(0).getTitle());
            tv_proposal_det_brief_data_view.setText(arrProposalDetails.get(0).getDetails());
            tv_timeline_data_view.setText(arrProposalDetails.get(0).getTimeline());
            tv_prop_duration_data_view.setText(arrProposalDetails.get(0).getDuration());
            if(arrProposalDetails.get(0).getFund_requested()!=null && arrProposalDetails.get(0).getFund_requested()!="")
            tv_fund_req_data_view.setText(new DecimalFormat("##,##,##0").format(Integer.parseInt(arrProposalDetails.get(0).getFund_requested())));
            tv_district_data_view.setText(arrProposalDetails.get(0).getDistrict());
            tv_block_data_view.setText(arrProposalDetails.get(0).getBlock());
            tv_vill_data_view.setText(arrProposalDetails.get(0).getVillage());
            tv_loc_data_view.setText(arrProposalDetails.get(0).getLocation());
            tv_benificiary_data_view.setText(arrProposalDetails.get(0).getProfile_benificiary());
            tv_rationale_data_view.setText(arrProposalDetails.get(0).getRationale_project());
            if(arrProposalDetails.get(0).getProposal_no().isEmpty())
                //tv_heading.setText("");
            tv_heading.setVisibility(View.GONE);
            else {
                tv_heading.setVisibility(View.VISIBLE);
                tv_heading.setText("Proposal No.: " + arrProposalDetails.get(0).getProposal_no());
            }

        }


    }
}
