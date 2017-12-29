package com.ciplafoundation.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciplafoundation.R;
import com.ciplafoundation.interfaces.CallBackListener;
import com.ciplafoundation.interfaces.InterfaceDialogCallback;
import com.ciplafoundation.model.ProposalDetails;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.services.VolleyTaskManager;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.ServerResponseCallback;
import com.ciplafoundation.utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by User-66-pc on 4/4/2017.
 */

public class FragmentProposalDetails extends Fragment {

    private static Activity mActivity;
    private static LinearLayout ll_pending_approved, ll_search_initiated_by,ll_notification;
    private LayoutInflater layoutInflater;
    private CallBackListener callBackListener;


    private static VolleyTaskManager volleyTaskManager;
    private static RelativeLayout rl_headerTree, rl_notification, rl_approval_review_details;
    private static ImageView iv_refresh_icon, iv_search_icon, iv_notification_close,ib_reject;
    private static TextView search_text, tv_notification;
    private static EditText et_approval_review;
    private static String isPushType="";

    private LinearLayout ll_accept, ll_resend, ll_approval_reject, ll_fund_distribution, ll_deliverable_distribution, ll_deliverable, ll_fund, ll_approve_download, ll_approve_track_main, ll_approval_download_view;
    private static TextView tv_heading, tv_schedule_data_view, tv_type_data_view, tv_ngodata_view, tv_fund_duration_data_view,
            tv_proposal_title_data_view, tv_proposal_det_brief_data_view, tv_timeline_data_view, tv_prop_duration_data_view,
            tv_fund_req_data_view, tv_district_data_view, tv_block_data_view, tv_vill_data_view, tv_loc_data_view, tv_distance_data_view,
            tv_benificiary_data_view, tv_rationale_data_view;
    private static ArrayList<ProposalDetails> arrProposalDetails = new ArrayList<>();
    private Prefs prefs;

    public static FragmentProposalDetails newInstance(String _isPushtype ,LinearLayout _ll_search_initiated_by, LinearLayout _ll_notification, ImageView _iv_notification_close, TextView _tv_notification, ImageView _iv_refresh_icon, ImageView _iv_search_icon, TextView _tv_heading, LinearLayout _ll_pending_approved, ArrayList<ProposalDetails> _arrProposalDetails) {
        FragmentProposalDetails f = new FragmentProposalDetails();
        Bundle b = new Bundle();
        f.setArguments(b);
        arrProposalDetails = _arrProposalDetails;
        tv_heading = _tv_heading;
        iv_refresh_icon = _iv_refresh_icon;
        iv_search_icon = _iv_search_icon;
        //search_text = _search_text;
        ll_search_initiated_by = _ll_search_initiated_by;
        ll_notification = _ll_notification;
        isPushType=_isPushtype;
        iv_notification_close = _iv_notification_close;
        ll_pending_approved = _ll_pending_approved;
        tv_notification = _tv_notification;
        return f;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_proposal_details, container, false);
        initComponent(rootView);
        return rootView;
    }

    private void initComponent(View v) {
        mActivity = getActivity();
        prefs=new Prefs(mActivity);
        ll_pending_approved.setVisibility(View.GONE);
        iv_refresh_icon.setVisibility(View.GONE);
        iv_search_icon.setVisibility(View.GONE);
        //search_text.setVisibility(View.GONE);
        ll_search_initiated_by.setVisibility(View.GONE);

        tv_schedule_data_view = (TextView) v.findViewById(R.id.tv_schedule_data_view);
        tv_type_data_view = (TextView) v.findViewById(R.id.tv_type_data_view);
        tv_ngodata_view = (TextView) v.findViewById(R.id.tv_ngodata_view);
        tv_fund_duration_data_view = (TextView) v.findViewById(R.id.tv_fund_duration_data_view);
        tv_proposal_title_data_view = (TextView) v.findViewById(R.id.tv_proposal_title_data_view);
        tv_proposal_det_brief_data_view = (TextView) v.findViewById(R.id.tv_proposal_det_brief_data_view);
        tv_timeline_data_view = (TextView) v.findViewById(R.id.tv_timeline_data_view);
        tv_prop_duration_data_view = (TextView) v.findViewById(R.id.tv_prop_duration_data_view);
        tv_fund_req_data_view = (TextView) v.findViewById(R.id.tv_fund_req_data_view);
        tv_district_data_view = (TextView) v.findViewById(R.id.tv_district_data_view);
        tv_block_data_view = (TextView) v.findViewById(R.id.tv_block_data_view);
        tv_vill_data_view = (TextView) v.findViewById(R.id.tv_vill_data_view);
        tv_loc_data_view = (TextView) v.findViewById(R.id.tv_loc_data_view);
        tv_benificiary_data_view = (TextView) v.findViewById(R.id.tv_benificiary_data_view);
        tv_rationale_data_view = (TextView) v.findViewById(R.id.tv_rationale_data_view);
        ib_reject=(ImageView) v.findViewById(R.id.ib_reject);
        rl_approval_review_details = (RelativeLayout) v.findViewById(R.id.rl_approval_review_details);
        et_approval_review = (EditText) v.findViewById(R.id.et_approval_review);
        ll_approval_reject = (LinearLayout) v.findViewById(R.id.ll_approval_reject);
        ll_resend = (LinearLayout) v.findViewById(R.id.ll_resend);
        ll_accept = (LinearLayout) v.findViewById(R.id.ll_accept);

        ll_fund = (LinearLayout) v.findViewById(R.id.ll_fund);
        ll_fund_distribution = (LinearLayout) v.findViewById(R.id.ll_fund_distribution);
        ll_deliverable = (LinearLayout) v.findViewById(R.id.ll_deliverable);
        ll_deliverable_distribution = (LinearLayout) v.findViewById(R.id.ll_deliverable_distribution);
        ll_approve_download = (LinearLayout) v.findViewById(R.id.ll_approve_download);
        ll_approval_download_view = (LinearLayout) v.findViewById(R.id.ll_approval_download_view);
        ll_approve_track_main = (LinearLayout) v.findViewById(R.id.ll_approve_track_main);

        setdata();

    }


    private void setdata() {

        if (arrProposalDetails != null && arrProposalDetails.size() > 0) {
            tv_schedule_data_view.setText(arrProposalDetails.get(0).getSchedule_vii());
            tv_type_data_view.setText(arrProposalDetails.get(0).getNgo_type());
            tv_ngodata_view.setText(arrProposalDetails.get(0).getNgo_vendor());
            tv_fund_duration_data_view.setText(arrProposalDetails.get(0).getDuration_funding());
            tv_proposal_title_data_view.setText(arrProposalDetails.get(0).getTitle());
            tv_proposal_det_brief_data_view.setText(arrProposalDetails.get(0).getDetails());
            tv_timeline_data_view.setText(arrProposalDetails.get(0).getTimeline());
            tv_prop_duration_data_view.setText(arrProposalDetails.get(0).getDuration());
            if (arrProposalDetails.get(0).getFund_requested() != null && arrProposalDetails.get(0).getFund_requested() != "")
                tv_fund_req_data_view.setText(new DecimalFormat("##,##,##0").format(Integer.parseInt(arrProposalDetails.get(0).getFund_requested())));
            tv_district_data_view.setText(arrProposalDetails.get(0).getDistrict());
            tv_block_data_view.setText(arrProposalDetails.get(0).getBlock());
            tv_vill_data_view.setText(arrProposalDetails.get(0).getVillage());
            tv_loc_data_view.setText(arrProposalDetails.get(0).getLocation());
            tv_benificiary_data_view.setText(arrProposalDetails.get(0).getProfile_benificiary());
            tv_rationale_data_view.setText(arrProposalDetails.get(0).getRationale_project());


            if (arrProposalDetails.get(0).getApprovalMessage().isEmpty()) {
                ll_notification.setVisibility(View.GONE);
            } else {
                ll_notification.setVisibility(View.VISIBLE);
                tv_notification.setText(arrProposalDetails.get(0).getApprovalMessage().toString());

            }
            if (arrProposalDetails.get(0).getProposal_no().isEmpty())
                //tv_heading.setText("");
                tv_heading.setVisibility(View.GONE);
            else {
                tv_heading.setVisibility(View.VISIBLE);
                tv_heading.setText("Proposal No.: " + arrProposalDetails.get(0).getProposal_no());
            }


            iv_notification_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_notification.setVisibility(View.GONE);

                }
            });
            setQuarterWiseDistribution();
            setDeliverableWiseDistribution();
            setDownloadView();
            setApprovalTrack();
            setApprovalReview();
        }


    }

    private void setApprovalReview() {

        if (arrProposalDetails.get(0).getApproval_status()) {
            rl_approval_review_details.setVisibility(View.VISIBLE);

            ll_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    volleyTaskManager=new VolleyTaskManager(mActivity);

                    if (et_approval_review.getText().toString().length() > 0) {

                        UserClass user = Util.fetchUserClass(mActivity);

                        HashMap<String, String> paramsMap = new HashMap<String, String>();
                        paramsMap.put("proposal_remarks",et_approval_review.getText().toString());
                        paramsMap.put("proposal_userId",user.getUserId());
                        paramsMap.put("proposal_roleId",user.getRoleId());
                        paramsMap.put("proposal_divisionId",prefs.getDivisionId());
                        paramsMap.put("proposal_id",arrProposalDetails.get(0).getProposal_id());
                        paramsMap.put("proposal_approveaction","accept");
                        volleyTaskManager.doApprovealProposalReview(paramsMap, true, new ServerResponseCallback() {
                            @Override
                            public void onSuccess(JSONObject resultJsonObject) {
                                Log.d("Responce:",resultJsonObject.toString());
                               if(resultJsonObject.optString("status").toString().equals("true")){
                                   Util.showDialog(mActivity, "Cipla", "Successful", new InterfaceDialogCallback() {
                                       @Override
                                       public void onClickAlertPositiveButton() {
                                                                                               // TODO Approval layout will be gone after successful
                                           rl_approval_review_details.setVisibility(View.GONE);


                                       }

                                       @Override
                                       public void onClickAlertNegativeButton() {

                                       }
                                   });
                               }

                            }

                            @Override
                            public void onError() {
                                Util.showMessageWithOk(mActivity,"Error! UnSuccessful");

                            }
                        });


                    }
                    else {
                        Util.showMessageWithOk(mActivity,"Please enter your review!");

                    }

                }


            });
            ll_resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    volleyTaskManager=new VolleyTaskManager(mActivity);
                    if (et_approval_review.getText().toString().length() > 0) {

                        UserClass user = Util.fetchUserClass(mActivity);

                        HashMap<String, String> paramsMap = new HashMap<String, String>();
                        paramsMap.put("proposal_remarks",et_approval_review.getText().toString());
                        paramsMap.put("proposal_userId",user.getUserId());
                        paramsMap.put("proposal_roleId",user.getRoleId());
                        paramsMap.put("proposal_divisionId",prefs.getDivisionId());
                        paramsMap.put("proposal_id",arrProposalDetails.get(0).getProposal_id());
                        paramsMap.put("proposal_approveaction","modify");
                        volleyTaskManager.doApprovealProposalReview(paramsMap, true, new ServerResponseCallback() {

                            @Override
                            public void onSuccess(JSONObject resultJsonObject) {
                                Log.d("Responce:",resultJsonObject.toString());
                                if(resultJsonObject.optString("status").toString().equals("true")){
                                    Util.showDialog(mActivity, "Cipla", "Successful", new InterfaceDialogCallback() {
                                        @Override
                                        public void onClickAlertPositiveButton() {
                                            // TODO Approval layout will be gone after successful
                                            rl_approval_review_details.setVisibility(View.GONE);


                                        }

                                        @Override
                                        public void onClickAlertNegativeButton() {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onError() {
                                Util.showMessageWithOk(mActivity,"Error! UnSuccessful");




                            }


                        });


                    }
                    else {
                        Util.showMessageWithOk(mActivity,"Please enter your review!");

                    }


                }
            });
            ll_approval_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    volleyTaskManager=new VolleyTaskManager(mActivity);
                    if (et_approval_review.getText().toString().length() > 0) {
                        UserClass user = Util.fetchUserClass(mActivity);

                        HashMap<String, String> paramsMap = new HashMap<String, String>();
                        paramsMap.put("proposal_remarks",et_approval_review.getText().toString());
                        paramsMap.put("proposal_userId",user.getUserId());
                        paramsMap.put("proposal_roleId",user.getRoleId());
                        paramsMap.put("proposal_divisionId",prefs.getDivisionId());
                        paramsMap.put("proposal_id",arrProposalDetails.get(0).getProposal_id());
                        paramsMap.put("proposal_approveaction","reject");
                        volleyTaskManager.doApprovealProposalReview(paramsMap, true, new ServerResponseCallback() {
                            @Override
                            public void onSuccess(JSONObject resultJsonObject) {
                                Log.d("Responce:",resultJsonObject.toString());
                                if(resultJsonObject.optString("status").toString().equals("true")){
                                    Util.showDialog(mActivity, "Cipla", "Successful", new InterfaceDialogCallback() {
                                        @Override
                                        public void onClickAlertPositiveButton() {
                                            // TODO Approval layout will be gone after successful
                                            rl_approval_review_details.setVisibility(View.GONE);


                                        }

                                        @Override
                                        public void onClickAlertNegativeButton() {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onError() {
                                Util.showMessageWithOk(mActivity,"Error! UnSuccessful");

                            }
                        });


                    }
                    else {
                        Util.showMessageWithOk(mActivity,"Please enter your review!");

                    }

                }
            });


        } else {
            rl_approval_review_details.setVisibility(View.GONE);
        }

    }


    //Method to set Data for fund distribution
    private void setQuarterWiseDistribution() {
        try {
            JSONArray jsonArrayQuarter = new JSONArray(arrProposalDetails.get(0).getQuarter_qise_distribution());
            if (jsonArrayQuarter != null && jsonArrayQuarter.length() > 0) {

                for (int i = 0; i < jsonArrayQuarter.length(); i++) {

                    View viewBaseDistribution = LayoutInflater.from(mActivity).inflate(R.layout.view_fund_data, null);

                    TextView tv_financial_year = (TextView) viewBaseDistribution.findViewById(R.id.tv_financial_year);
                    LinearLayout ll_fund_distribution_data = (LinearLayout) viewBaseDistribution.findViewById(R.id.ll_fund_distribution_data);

                    JSONObject jsonObjectQuarterDetails = jsonArrayQuarter.optJSONObject(i);

                    tv_financial_year.setText(Html.fromHtml(Util.textIfNull("<b> Financial Year (" + jsonObjectQuarterDetails.optString("session") + " )</b>")));

                    JSONArray jsonArrayQuarterDetails = jsonObjectQuarterDetails.optJSONArray("data");

                    if (jsonArrayQuarterDetails != null && jsonArrayQuarterDetails.length() > 0) {
                        for (int j = 0; j < jsonArrayQuarterDetails.length(); j++) {

                            JSONObject jObjectQuarterDetails = jsonArrayQuarterDetails.optJSONObject(j);

                            View viewDetailsDistribution = LayoutInflater.from(mActivity).inflate(R.layout.view_fund_details_data, null);
                            TextView tv_timeFrame = (TextView) viewDetailsDistribution.findViewById(R.id.tv_timeFrame);
                            TextView tv_fundCost = (TextView) viewDetailsDistribution.findViewById(R.id.tv_fundCost);
                            TextView tv_task = (TextView) viewDetailsDistribution.findViewById(R.id.tv_task);

                            if (arrProposalDetails.get(0).getView_fund_distribution().equalsIgnoreCase("old")) {
                                tv_timeFrame.setText(Util.textIfNull(jObjectQuarterDetails.optString("quater")));
                                tv_fundCost.setText(Util.textIfNull(jObjectQuarterDetails.optString("cost")));
                                if (!Util.textIfNull(jObjectQuarterDetails.optString("task").trim()).equalsIgnoreCase(""))
                                    tv_task.setText(Html.fromHtml(jObjectQuarterDetails.optString("task").replace("|", "\n")));
                            } else {
                                tv_timeFrame.setText(Util.textIfNull(jObjectQuarterDetails.optString("DATE_RANGE")));
                                tv_fundCost.setText(Util.textIfNull(jObjectQuarterDetails.optString("PFD_COST")));
                                if (!Util.textIfNull(jObjectQuarterDetails.optString("PFD_ACTIVITY").trim()).equalsIgnoreCase(""))
                                    tv_task.setText(Html.fromHtml(jObjectQuarterDetails.optString("PFD_ACTIVITY").replace("|", "\n")));
                            }
                            ll_fund_distribution_data.addView(viewDetailsDistribution);

                        }
                    }

                    ll_fund_distribution.addView(viewBaseDistribution);

                    //adding divider
                    View v = new View(mActivity);
                    v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    v.setBackgroundColor(Color.parseColor("#000000"));
                    if (i != jsonArrayQuarter.length() - 1)
                        ll_fund_distribution.addView(v);
                }
            } else
                ll_fund.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Method to set Data for fund distribution
    private void setDeliverableWiseDistribution() {
        try {
            JSONArray jsonArrayDeliverable = new JSONArray(arrProposalDetails.get(0).getDeliverable_wise_distribution());
            if (jsonArrayDeliverable != null && jsonArrayDeliverable.length() > 0) {

                for (int i = 0; i < jsonArrayDeliverable.length(); i++) {

                    View viewBaseDeliverable = LayoutInflater.from(mActivity).inflate(R.layout.view_deliverable_distribution, null);
                    TextView tv_deliverable = (TextView) viewBaseDeliverable.findViewById(R.id.tv_deliverable);
                    TextView tv_deliverable_duration = (TextView) viewBaseDeliverable.findViewById(R.id.tv_deliverable_duration);
                    TextView tv_deliverable_cost = (TextView) viewBaseDeliverable.findViewById(R.id.tv_deliverable_cost);
                    TextView tv_deliverable_comment = (TextView) viewBaseDeliverable.findViewById(R.id.tv_deliverable_comment);

                    tv_deliverable.setText(Html.fromHtml("<big><b> Deliverable</b></big> <br>" + Util.textIfNull(jsonArrayDeliverable.optJSONObject(i).optString("PRA_ACTIVITY_NAME"))));
                    tv_deliverable_duration.setText(Html.fromHtml("<big><b> Duration</b> </big> <br>" + Util.textIfNull(jsonArrayDeliverable.optJSONObject(i).optString("PRA_DURATION"))));
                    tv_deliverable_cost.setText(Html.fromHtml("<big><b>Cost (INR)</b> </big> <br>" + Util.textIfNull(jsonArrayDeliverable.optJSONObject(i).optString("PRA_ACTIVITY_COST"))));
                    tv_deliverable_comment.setText(Html.fromHtml("<big><b> Comment</b> </big> <br>" + Util.textIfNull(jsonArrayDeliverable.optJSONObject(i).optString("PRA_ACTIVITY_COMMENT"))));


                    ll_deliverable_distribution.addView(viewBaseDeliverable);

                    //adding divider
                    View v = new View(mActivity);
                    v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    v.setBackgroundColor(Color.parseColor("#000000"));
                    if (i != jsonArrayDeliverable.length() - 1)
                        ll_deliverable_distribution.addView(v);
                }
            } else
                ll_deliverable.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Method to set Data for Download document.....
    private void setDownloadView() {
        try {
            JSONArray jsonArrayDownload = new JSONArray(arrProposalDetails.get(0).getDownload_track());
            if (jsonArrayDownload != null && jsonArrayDownload.length() > 0) {

                for (int i = 0; i < jsonArrayDownload.length(); i++) {

                    layoutInflater = (LayoutInflater) mActivity
                            .getSystemService(LAYOUT_INFLATER_SERVICE);
                    View viewlayout = layoutInflater.inflate(R.layout.download_layout, null);


                    // View viewBaseDownloadable = LayoutInflater.from(mActivity).inflate(R.layout.download_layout, null);
                    final TextView tv_file_name = (TextView) viewlayout.findViewById(R.id.tv_file_name);
                    final ImageView iv_set_download = (ImageView) viewlayout.findViewById(R.id.iv_set_download);
                    iv_set_download.setTag(i);
                    tv_file_name.setText(jsonArrayDownload.optJSONObject(i).optString("filename"));
                    final String[] arrayFilePath = new String[jsonArrayDownload.length()];
                    final String[] arrayFileName = new String[jsonArrayDownload.length()];
                    arrayFilePath[i] = jsonArrayDownload.optJSONObject(i).optString("downloadpath");
                    arrayFileName[i] = jsonArrayDownload.optJSONObject(i).optString("filename");
                    iv_set_download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = Integer.valueOf((Integer) iv_set_download.getTag());
                            System.out.println("Image tag==" + arrayFilePath[position]);
                            String fileURL = arrayFilePath[position];
                            String fileName = arrayFileName[position];
                            String url = fileURL;
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Uri u = Uri.parse(url);
                            intent.setData(u);
                            startActivity(intent);

                            // new DownloadFileAsync(mActivity).execute(fileURL,fileName);

                        }
                    });

                    ll_approval_download_view.addView(viewlayout);

                }
            } else

                ll_approve_download.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Method to set Data for approval tracking .....
    private void setApprovalTrack() {
        try {
            JSONArray jsonArrayApprovalTrack = new JSONArray(arrProposalDetails.get(0).getApproved_track());
            if (jsonArrayApprovalTrack != null && jsonArrayApprovalTrack.length() > 0) {

                for (int i = 0; i < jsonArrayApprovalTrack.length(); i++) {

                    View viewBaseApprovalTrack = LayoutInflater.from(mActivity).inflate(R.layout.approval_track_proposal_view_layout, null);
                    TextView tv_approval_status = (TextView) viewBaseApprovalTrack.findViewById(R.id.tv_approval_status);
                    TextView tv_user = (TextView) viewBaseApprovalTrack.findViewById(R.id.tv_user);
                    TextView tv_date_time = (TextView) viewBaseApprovalTrack.findViewById(R.id.tv_date_time);
                    TextView tv_remarks = (TextView) viewBaseApprovalTrack.findViewById(R.id.tv_remarks);


                    tv_approval_status.setText(jsonArrayApprovalTrack.optJSONObject(i).optString("text"));
                    if (jsonArrayApprovalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PREP"))
                        tv_approval_status.setBackgroundColor(Color.parseColor("#abbac3"));
                    else if (jsonArrayApprovalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PEND"))
                        tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                    else if (jsonArrayApprovalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTH"))
                        tv_approval_status.setBackgroundColor(Color.parseColor("#82af6f"));
                    else if (jsonArrayApprovalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTL"))
                        tv_approval_status.setBackgroundColor(Color.parseColor("#428bca"));
                    else if (jsonArrayApprovalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("REJCT"))
                        tv_approval_status.setBackgroundColor(Color.parseColor("#de1028"));
                    else if (jsonArrayApprovalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFY"))
                        tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                    else if (jsonArrayApprovalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFYED")) {
                        tv_approval_status.setBackgroundColor(Color.parseColor("#fee188"));
                        tv_approval_status.setTextColor(Color.parseColor("#000000"));
                    }
                    tv_user.setText(jsonArrayApprovalTrack.optJSONObject(i).optString("user"));
                    tv_date_time.setText(jsonArrayApprovalTrack.optJSONObject(i).optString("datetime"));
                    tv_remarks.setText(jsonArrayApprovalTrack.optJSONObject(i).optString("remarks"));


                    ll_approve_track_main.addView(viewBaseApprovalTrack);

                    //adding divider
                    View v = new View(mActivity);
                    v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    v.setBackgroundColor(Color.parseColor("#000000"));
                    if (i != jsonArrayApprovalTrack.length() - 1)
                        ll_approve_track_main.addView(v);
                }
            } else
                ll_approve_track_main.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //getActivity() is fully created in onActivityCreated and instanceOf differentiate it between different Activities
        if (getActivity() instanceof CallBackListener)
            callBackListener = (CallBackListener) getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        iv_refresh_icon.setVisibility(View.VISIBLE);
        iv_search_icon.setVisibility(View.VISIBLE);
        //search_text.setVisibility(View.VISIBLE);
        ll_notification.setVisibility(View.GONE);
        if(isPushType.equalsIgnoreCase("proposal-view")){
            if(callBackListener != null){
                callBackListener.onCallBack("isProposalPush");
            }

        }

        }


    }

