package com.ciplafoundation.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciplafoundation.R;
import com.ciplafoundation.interfaces.CallBackListener;
import com.ciplafoundation.interfaces.InterfaceDialogCallback;
import com.ciplafoundation.model.ProjectDetails;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.services.VolleyTaskManager;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.ServerResponseCallback;
import com.ciplafoundation.utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by User-66-pc on 4/4/2017.
 */

public class FragmentProjectDetails extends Fragment {

    private static Activity mActivity;
    private LayoutInflater layoutInflater;
    private CallBackListener callBackListener;
    private static String isPushType = "", deviationId = "", projectPanId = "";
    private static VolleyTaskManager volleyTaskManager;
    private static EditText et_approval_review;
    private static RelativeLayout rl_notification, rl_approval_review_details, rl_single_row_monitoring_layout;
    private static LinearLayout ll_pending_approved, ll_search_initiated_by, ll_notification;
    private LinearLayout ll_accept, ll_resend, ll_approval_reject, ll_general_details, ll_track_view, ll_track, ll_project_details, ll_fund, ll_deliverable, ll_closure,
            ll_mou, ll_mou_distribution, ll_monitering, ll_monitering_distribution, ll_pan, ll_pan_distribution, ll_deviation, ll_deviation_distribution,
            ll_closure_distribution, ll_devition_track_view, ll_fund_distribution, ll_deliverable_distribution, ll_closure_track_view, ll_closure_track, ll_proposal_track, ll_proposal_track_view, ll_project_track, ll_project_track_view, ll_devition_track,
            ll_documents_track, ll_project_file_list, ll_pan_file_list, ll_mou_file_list, ll_monitoring_file_list, ll_deviation_file_list, ll_closure_file_list;
    private static TextView tv_heading, tv_notification, search_text, tv_project_file, tv_pan_file, tv_mou_file, tv_montitoring_file, tv_devition_file, closure_file;
    private static ImageView iv_refresh_icon, iv_search_icon, iv_notification_close;
    private static ProjectDetails _projectDetails;
    private Prefs prefs;

    public static FragmentProjectDetails newInstance(String _isPushType, String _projectPanId, String _deviationId, LinearLayout _ll_search_initiated_by, LinearLayout _ll_notification, ImageView _iv_notification_close, TextView _tv_notification, ImageView _iv_refresh_icon, ImageView _iv_search_icon, TextView _tv_heading, LinearLayout _ll_pending_approved, ProjectDetails projectDetails) {
        FragmentProjectDetails f = new FragmentProjectDetails();
        Bundle b = new Bundle();
        f.setArguments(b);
        _projectDetails = projectDetails;
        tv_heading = _tv_heading;
        iv_refresh_icon = _iv_refresh_icon;
        iv_search_icon = _iv_search_icon;
        //search_text = _search_text;
        isPushType = _isPushType;
        deviationId = _deviationId;
        projectPanId = _projectPanId;
        ll_search_initiated_by = _ll_search_initiated_by;
        ll_notification = _ll_notification;
        iv_notification_close = _iv_notification_close;
        ll_pending_approved = _ll_pending_approved;
        tv_notification = _tv_notification;
        return f;
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_project_details, container, false);
        initComponent(rootView);
        return rootView;
    }


    private void initComponent(View v) {
        mActivity = getActivity();
        prefs = new Prefs(mActivity);
        ll_pending_approved.setVisibility(View.GONE);
        iv_refresh_icon.setVisibility(View.GONE);
        iv_search_icon.setVisibility(View.GONE);
        //search_text.setVisibility(View.GONE);
        ll_search_initiated_by.setVisibility(View.GONE);

        ll_fund = (LinearLayout) v.findViewById(R.id.ll_fund);
        ll_fund_distribution = (LinearLayout) v.findViewById(R.id.ll_fund_distribution);

        ll_deliverable = (LinearLayout) v.findViewById(R.id.ll_deliverable);
        ll_deliverable_distribution = (LinearLayout) v.findViewById(R.id.ll_deliverable_distribution);

        ll_general_details = (LinearLayout) v.findViewById(R.id.ll_general_details);
        ll_project_details = (LinearLayout) v.findViewById(R.id.ll_project_details);

        ll_closure = (LinearLayout) v.findViewById(R.id.ll_closure);
        ll_closure_distribution = (LinearLayout) v.findViewById(R.id.ll_closure_distribution);

        ll_mou = (LinearLayout) v.findViewById(R.id.ll_mou);
        ll_mou_distribution = (LinearLayout) v.findViewById(R.id.ll_mou_distribution);

        ll_monitering = (LinearLayout) v.findViewById(R.id.ll_monitering);
        ll_monitering_distribution = (LinearLayout) v.findViewById(R.id.ll_monitering_distribution);

        ll_pan = (LinearLayout) v.findViewById(R.id.ll_pan);
        ll_pan_distribution = (LinearLayout) v.findViewById(R.id.ll_pan_distribution);

        ll_deviation = (LinearLayout) v.findViewById(R.id.ll_deviation);
        ll_deviation_distribution = (LinearLayout) v.findViewById(R.id.ll_deviation_distribution);


        ll_proposal_track = (LinearLayout) v.findViewById(R.id.ll_proposal_track);
        ll_proposal_track_view = (LinearLayout) v.findViewById(R.id.ll_proposal_track_view);
        ll_project_track = (LinearLayout) v.findViewById(R.id.ll_project_track);
        ll_project_track_view = (LinearLayout) v.findViewById(R.id.ll_project_track_view);
        ll_devition_track = (LinearLayout) v.findViewById(R.id.ll_devition_track);
        ll_devition_track_view = (LinearLayout) v.findViewById(R.id.ll_devition_track_view);
        ll_closure_track_view = (LinearLayout) v.findViewById(R.id.ll_closure_track_view);
        ll_closure_track = (LinearLayout) v.findViewById(R.id.ll_closure_track);
        ll_documents_track = (LinearLayout) v.findViewById(R.id.ll_documents_track);


        et_approval_review = (EditText) v.findViewById(R.id.et_approval_review);
        ll_approval_reject = (LinearLayout) v.findViewById(R.id.ll_approval_reject);
        ll_resend = (LinearLayout) v.findViewById(R.id.ll_resend);
        ll_accept = (LinearLayout) v.findViewById(R.id.ll_accept);

        rl_approval_review_details = (RelativeLayout) v.findViewById(R.id.rl_approval_review_details);

        ll_project_file_list = (LinearLayout) v.findViewById(R.id.ll_project_file_list);
        ll_pan_file_list = (LinearLayout) v.findViewById(R.id.ll_pan_file_list);
        ll_mou_file_list = (LinearLayout) v.findViewById(R.id.ll_mou_file_list);
        ll_monitoring_file_list = (LinearLayout) v.findViewById(R.id.ll_monitoring_file_list);
        ll_deviation_file_list = (LinearLayout) v.findViewById(R.id.ll_deviation_file_list);
        ll_closure_file_list = (LinearLayout) v.findViewById(R.id.ll_closure_file_list);

        tv_project_file = (TextView) v.findViewById(R.id.tv_project_file);
        tv_pan_file = (TextView) v.findViewById(R.id.tv_pan_file);
        tv_mou_file = (TextView) v.findViewById(R.id.tv_mou_file);
        tv_montitoring_file = (TextView) v.findViewById(R.id.tv_montitoring_file);
        tv_devition_file = (TextView) v.findViewById(R.id.tv_devition_file);
        closure_file = (TextView) v.findViewById(R.id.closure_file);
        rl_single_row_monitoring_layout = (RelativeLayout) v.findViewById(R.id.rl_single_row_monitoring_layout);
        setdata();

    }

    private void setdata() {

        if (_projectDetails != null) {

            if (_projectDetails.getProject_no().isEmpty() || _projectDetails.getProject_no().toString().equalsIgnoreCase("null"))
                //tv_heading.setText("");
                tv_heading.setVisibility(View.GONE);
            else {
                tv_heading.setVisibility(View.VISIBLE);
                tv_heading.setText("Project No.: " + _projectDetails.getProject_no());
            }
            if (_projectDetails.getApprovalMessage().toString().length() == 0) {
                ll_notification.setVisibility(View.GONE);
            } else {
                ll_notification.setVisibility(View.VISIBLE);
                tv_notification.setText(_projectDetails.getApprovalMessage().toString());
            }
            iv_notification_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll_notification.setVisibility(View.GONE);

                }
            });


            setProjectDetails();
            setDeliverableWiseDistribution();
            setClosure();
            setMou();
            setPan();
            setDeviation();
            setMonitoring();
            setQuarterWiseDistribution();
            setTrack();
            setDocuments();
            setApprovalReview();
        }


    }

    private void setApprovalReview() {

        if (_projectDetails.isApprovalMessageVisibility()) {
            rl_approval_review_details.setVisibility(View.VISIBLE);

            ll_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    volleyTaskManager = new VolleyTaskManager(mActivity);

                    if (et_approval_review.getText().toString().length() > 0) {

                        UserClass user = Util.fetchUserClass(mActivity);

                        HashMap<String, String> paramsMap = new HashMap<String, String>();
                        paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                        paramsMap.put("USER_ID", user.getUserId());
                        paramsMap.put("DIVISION_ID", prefs.getDivisionId());
                        paramsMap.put("PROJECT_ID", _projectDetails.getId());
                        paramsMap.put("approval_action", "accept");
                        volleyTaskManager.doApprovealProjectReview(paramsMap, true, new ServerResponseCallback() {
                            @Override
                            public void onSuccess(JSONObject resultJsonObject) {
                                Log.d("Responce:", resultJsonObject.toString());
                                if (resultJsonObject.optString("status").toString().equals("true")) {
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
                                Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                            }
                        });


                    } else {
                        Util.showMessageWithOk(mActivity, "Please enter your review!");

                    }

                }


            });

            ll_resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    volleyTaskManager = new VolleyTaskManager(mActivity);

                    if (et_approval_review.getText().toString().length() > 0) {

                        UserClass user = Util.fetchUserClass(mActivity);

                        HashMap<String, String> paramsMap = new HashMap<String, String>();
                        paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                        paramsMap.put("USER_ID", user.getUserId());
                        paramsMap.put("DIVISION_ID", prefs.getDivisionId());
                        paramsMap.put("PROJECT_ID", _projectDetails.getId());
                        paramsMap.put("approval_action", "modify");
                        volleyTaskManager.doApprovealProjectReview(paramsMap, true, new ServerResponseCallback() {
                            @Override
                            public void onSuccess(JSONObject resultJsonObject) {
                                Log.d("Responce:", resultJsonObject.toString());
                                if (resultJsonObject.optString("status").toString().equals("true")) {
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
                                Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                            }
                        });


                    } else {
                        Util.showMessageWithOk(mActivity, "Please enter your review!");

                    }

                }


            });


            ll_approval_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    volleyTaskManager = new VolleyTaskManager(mActivity);

                    if (et_approval_review.getText().toString().length() > 0) {

                        UserClass user = Util.fetchUserClass(mActivity);

                        HashMap<String, String> paramsMap = new HashMap<String, String>();
                        paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                        paramsMap.put("USER_ID", user.getUserId());
                        paramsMap.put("DIVISION_ID", prefs.getDivisionId());
                        paramsMap.put("PROJECT_ID", _projectDetails.getId());
                        paramsMap.put("approval_action", "reject");
                        volleyTaskManager.doApprovealProjectReview(paramsMap, true, new ServerResponseCallback() {
                            @Override
                            public void onSuccess(JSONObject resultJsonObject) {
                                Log.d("Responce:", resultJsonObject.toString());
                                if (resultJsonObject.optString("status").toString().equals("true")) {
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
                                Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                            }
                        });


                    } else {
                        Util.showMessageWithOk(mActivity, "Please enter your review!");

                    }

                }


            });


        } else {
            rl_approval_review_details.setVisibility(View.GONE);
        }
    }

    //Method to set  for document download
    private void setDocuments() {
        try {
            JSONObject allDocumentJsonObject = new JSONObject(_projectDetails.getDocuments());
            if (allDocumentJsonObject.toString().length() != 0) {

                JSONArray closurefileArray = allDocumentJsonObject.optJSONArray("closurefile");
                if (closurefileArray != null && closurefileArray.length() > 0) {

                    for (int i = 0; i < closurefileArray.length(); i++) {
                        layoutInflater = (LayoutInflater) mActivity
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                        View closurefilelayout = layoutInflater.inflate(R.layout.download_layout, null);
                        final TextView tv_file_name = (TextView) closurefilelayout.findViewById(R.id.tv_file_name);
                        final ImageView iv_set_download = (ImageView) closurefilelayout.findViewById(R.id.iv_set_download);
                        String url = closurefileArray.get(i).toString();
                        String fileName = url.substring(url.lastIndexOf('/') + 1);
                        tv_file_name.setText(fileName);

                        iv_set_download.setTag(i);
                        final String[] arrayFilePath = new String[closurefileArray.length()];
                        arrayFilePath[i] = url;
                        iv_set_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                String fileURL = arrayFilePath[position];
                                String url = fileURL;
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri u = Uri.parse(url);
                                intent.setData(u);
                                startActivity(intent);
                            }
                        });
                        ll_closure_file_list.addView(closurefilelayout);
                    }
                } else {
                    closure_file.setVisibility(View.GONE);

                }
                JSONArray deviationfileArray = allDocumentJsonObject.optJSONArray("deviationfile");
                if (deviationfileArray != null && deviationfileArray.length() > 0) {

                    for (int i = 0; i < deviationfileArray.length(); i++) {
                        layoutInflater = (LayoutInflater) mActivity
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                        View deviationfilelayout = layoutInflater.inflate(R.layout.download_layout, null);
                        final TextView tv_file_name = (TextView) deviationfilelayout.findViewById(R.id.tv_file_name);
                        final ImageView iv_set_download = (ImageView) deviationfilelayout.findViewById(R.id.iv_set_download);
                        String url = deviationfileArray.get(i).toString();
                        String fileName = url.substring(url.lastIndexOf('/') + 1);
                        tv_file_name.setText(fileName);

                        iv_set_download.setTag(i);
                        final String[] arrayFilePath = new String[deviationfileArray.length()];
                        arrayFilePath[i] = url;
                        iv_set_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                String fileURL = arrayFilePath[position];
                                String url = fileURL;
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri u = Uri.parse(url);
                                intent.setData(u);
                                startActivity(intent);
                            }
                        });
                        ll_deviation_file_list.addView(deviationfilelayout);
                    }
                } else {
                    tv_devition_file.setVisibility(View.GONE);

                }


                JSONArray monitoringfileArray = allDocumentJsonObject.optJSONArray("monitoringfile");
                if (monitoringfileArray != null && monitoringfileArray.length() > 0) {

                    for (int i = 0; i < monitoringfileArray.length(); i++) {
                        layoutInflater = (LayoutInflater) mActivity
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                        View monitoringfilelayout = layoutInflater.inflate(R.layout.download_layout, null);
                        final TextView tv_file_name = (TextView) monitoringfilelayout.findViewById(R.id.tv_file_name);
                        final ImageView iv_set_download = (ImageView) monitoringfilelayout.findViewById(R.id.iv_set_download);
                        String url = monitoringfileArray.get(i).toString();
                        String fileName = url.substring(url.lastIndexOf('/') + 1);
                        tv_file_name.setText(fileName);

                        iv_set_download.setTag(i);
                        final String[] arrayFilePath = new String[monitoringfileArray.length()];
                        arrayFilePath[i] = url;
                        iv_set_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                String fileURL = arrayFilePath[position];
                                String url = fileURL;
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri u = Uri.parse(url);
                                intent.setData(u);
                                startActivity(intent);
                            }
                        });

                        ll_monitoring_file_list.addView(monitoringfilelayout);
                    }
                } else {
                    tv_montitoring_file.setVisibility(View.GONE);

                }

                JSONArray projectfileArray = allDocumentJsonObject.optJSONArray("projectfile");
                if (projectfileArray != null && projectfileArray.length() > 0) {

                    for (int i = 0; i < projectfileArray.length(); i++) {
                        layoutInflater = (LayoutInflater) mActivity
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                        View projectfilelayout = layoutInflater.inflate(R.layout.download_layout, null);
                        final TextView tv_file_name = (TextView) projectfilelayout.findViewById(R.id.tv_file_name);
                        final ImageView iv_set_download = (ImageView) projectfilelayout.findViewById(R.id.iv_set_download);
                        String url = projectfileArray.get(i).toString();
                        String fileName = url.substring(url.lastIndexOf('/') + 1);
                        tv_file_name.setText(fileName);

                        iv_set_download.setTag(i);
                        final String[] arrayFilePath = new String[projectfileArray.length()];
                        arrayFilePath[i] = url;
                        iv_set_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                String fileURL = arrayFilePath[position];
                                String url = fileURL;
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri u = Uri.parse(url);
                                intent.setData(u);
                                startActivity(intent);
                            }
                        });

                        ll_project_file_list.addView(projectfilelayout);
                    }
                } else {
                    tv_project_file.setVisibility(View.GONE);
                }

                JSONArray panfileArray = allDocumentJsonObject.optJSONArray("panfile");
                if (panfileArray != null && panfileArray.length() > 0) {

                    for (int i = 0; i < panfileArray.length(); i++) {
                        layoutInflater = (LayoutInflater) mActivity
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                        View panfilelayout = layoutInflater.inflate(R.layout.download_layout, null);
                        final TextView tv_file_name = (TextView) panfilelayout.findViewById(R.id.tv_file_name);
                        final ImageView iv_set_download = (ImageView) panfilelayout.findViewById(R.id.iv_set_download);
                        String url = panfileArray.get(i).toString();
                        String fileName = url.substring(url.lastIndexOf('/') + 1);
                        tv_file_name.setText(fileName);

                        iv_set_download.setTag(i);
                        final String[] arrayFilePath = new String[panfileArray.length()];
                        arrayFilePath[i] = url;
                        iv_set_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                String fileURL = arrayFilePath[position];
                                String url = fileURL;
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri u = Uri.parse(url);
                                intent.setData(u);
                                startActivity(intent);
                            }
                        });

                        ll_pan_file_list.addView(panfilelayout);
                    }
                } else {
                    tv_pan_file.setVisibility(View.GONE);
                }

                JSONArray moufileArray = allDocumentJsonObject.optJSONArray("moufile");
                if (moufileArray != null && moufileArray.length() > 0) {

                    for (int i = 0; i < moufileArray.length(); i++) {
                        layoutInflater = (LayoutInflater) mActivity
                                .getSystemService(LAYOUT_INFLATER_SERVICE);
                        View moufilelayout = layoutInflater.inflate(R.layout.download_layout, null);
                        final TextView tv_file_name = (TextView) moufilelayout.findViewById(R.id.tv_file_name);
                        final ImageView iv_set_download = (ImageView) moufilelayout.findViewById(R.id.iv_set_download);
                        String url = moufileArray.get(i).toString();
                        String fileName = url.substring(url.lastIndexOf('/') + 1);
                        tv_file_name.setText(fileName);

                        iv_set_download.setTag(i);
                        final String[] arrayFilePath = new String[moufileArray.length()];
                        arrayFilePath[i] = url;
                        iv_set_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                String fileURL = arrayFilePath[position];
                                String url = fileURL;
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Uri u = Uri.parse(url);
                                intent.setData(u);
                                startActivity(intent);
                            }
                        });
                        ll_mou_file_list.addView(moufilelayout);
                    }
                } else {
                    tv_mou_file.setVisibility(View.GONE);
                }
// TODO If  All Array empty ....

                if (closurefileArray.length() == 0 && deviationfileArray.length() == 0 && monitoringfileArray.length() == 0
                        && projectfileArray.length() == 0 && panfileArray.length() == 0 && moufileArray.length() == 0) {

                    ll_documents_track.setVisibility(View.GONE);

                }

            } else
                ll_documents_track.setVisibility(View.GONE);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    //Method to set Data for Project details
    private void setProjectDetails() {

        inflateViewData("Project Title", _projectDetails.getProject_title(), "", ll_project_details);

        if (!_projectDetails.getDeviation_cost().equalsIgnoreCase("0"))
            inflateViewData("Project Budget", _projectDetails.getBudget(), "increased by: " + _projectDetails.getDeviation_cost(), ll_project_details);
        else
            inflateViewData("Project Budget", _projectDetails.getBudget(), "", ll_project_details);

        inflateViewData("Report submission Interval", _projectDetails.getReport_submission_interval(), "", ll_project_details);

        inflateViewData("Project Duration", _projectDetails.getDuration(), "", ll_project_details);

        if (!_projectDetails.getDeviation_time().equalsIgnoreCase("0"))
            inflateViewData("Project Duration", _projectDetails.getTimeline() + "<br>" + _projectDetails.getDuration() + "(" + _projectDetails.getDuration_word() + ") </br>", "extended by " + _projectDetails.getDeviation_time() + " days", ll_project_details);
        else
            inflateViewData("Project Duration", _projectDetails.getTimeline() + "<br>" + _projectDetails.getDuration() + "(" + _projectDetails.getDuration_word() + ") </br>", "", ll_project_details);

        inflateViewData("Project details in Brief", _projectDetails.getDetails_brief(), "", ll_project_details);
        inflateViewData("Project Expenditure", _projectDetails.getExpenditure(), "", ll_project_details);
        inflateViewData("Project No", _projectDetails.getProject_no(), "", ll_project_details);

    }

    //Method to set Data for Closure
    private void setClosure() {
        try {
            JSONArray jsonArrayClosure = new JSONArray(_projectDetails.getClosure());
            if (jsonArrayClosure != null && jsonArrayClosure.length() > 0) {
                final String[] clouserArrayId = new String[jsonArrayClosure.length()];
                for (int i = 0; i < jsonArrayClosure.length(); i++) {
                    layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View clouserViewLayout = layoutInflater.inflate(R.layout.single_row_clouser, null);
                    TextView tv_clouser_deliverable = (TextView) clouserViewLayout.findViewById(R.id.tv_clouser_deliverable);
                    TextView tv_clouser_suggesion = (TextView) clouserViewLayout.findViewById(R.id.tv_clouser_suggesion);
                    TextView tv_clouser_benificiary_amount = (TextView) clouserViewLayout.findViewById(R.id.tv_clouser_benificiary_amount);
                    TextView tv_clouser_date_status = (TextView) clouserViewLayout.findViewById(R.id.tv_clouser_date_status);
                    final ImageView iv_details = (ImageView) clouserViewLayout.findViewById(R.id.iv_details);
                    iv_details.setTag(i);


                    tv_clouser_deliverable.setText(jsonArrayClosure.optJSONObject(i).optString("PRC_DELIVERABLE_ACHIVE"));
                    tv_clouser_suggesion.setText(jsonArrayClosure.optJSONObject(i).optString("PRC_SUGGESTION"));
                    tv_clouser_benificiary_amount.setText(jsonArrayClosure.optJSONObject(i).optString("PRC_BENIFICIARY"));
                    tv_clouser_date_status.setText(jsonArrayClosure.optJSONObject(i).optString("PRC_CLOSURE_DATE"));
                    clouserArrayId[i] = jsonArrayClosure.optJSONObject(i).optString("PRC_ID");

                    iv_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            
                            int position = Integer.valueOf((Integer) iv_details.getTag());
                            String clouserid = clouserArrayId[position];
                            showClouserDialog(clouserid);
                         
                        }
                    });


                    
                    if (isPushType.equalsIgnoreCase("project-closure-view")) {
                        if (projectPanId.equals(clouserArrayId[i])) {
                            Log.i("projectId==== ", projectPanId);
                            showClouserDialog(projectPanId);
                        }
                    }

                    ll_closure_distribution.addView(clouserViewLayout);
                }
            } else
                ll_closure.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Method to set Data for Monitoring
    private void setMonitoring() {
        try {
            JSONArray jsonArrayMonitoring = new JSONArray(_projectDetails.getMonitering());
            if (jsonArrayMonitoring != null && jsonArrayMonitoring.length() > 0) {
                final String[] monitoringId = new String[jsonArrayMonitoring.length()];
                for (int i = 0; i < jsonArrayMonitoring.length(); i++) {
                    layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View monitoringLayout = layoutInflater.inflate(R.layout.single_row_monitoring, null);

                    final TextView tv_title_monitoring = (TextView) monitoringLayout.findViewById(R.id.tv_title_monitoring);
                    final TextView tv_monitoring_id = (TextView) monitoringLayout.findViewById(R.id.tv_monitoring_id);
                    final ImageView iv_details = (ImageView) monitoringLayout.findViewById(R.id.iv_details);
                    tv_title_monitoring.setText(jsonArrayMonitoring.optJSONObject(i).optString("LDT_TITLE"));
                    tv_monitoring_id.setText(jsonArrayMonitoring.optJSONObject(i).optString("submited_date"));
                    iv_details.setTag(i);
                    monitoringId[i] = jsonArrayMonitoring.optJSONObject(i).optString("LDT_LEVEL_DATA_ID");

                    iv_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            volleyTaskManager = new VolleyTaskManager(mActivity);
                            int position = Integer.valueOf((Integer) iv_details.getTag());
                            String LDT_LEVEL_DATA_ID = monitoringId[position];
                            Log.i("LDT_LEVEL_DATA_ID=", LDT_LEVEL_DATA_ID);
                            String param = "LDT_LEVEL_DATA_ID=" + LDT_LEVEL_DATA_ID;
                            // String param = "LDT_LEVEL_DATA_ID=" + "LDT000000000000123";
                            volleyTaskManager.doMonitoringDitails(param, true, new ServerResponseCallback() {
                                @Override
                                public void onSuccess(JSONObject resultJsonObject) {
                                    JSONObject jsonObjectresult = resultJsonObject.optJSONObject("result");
                                    if (jsonObjectresult.toString().length() > 0) {
                                        final Dialog monitoringDetailsDialog = new Dialog(mActivity);
                                        monitoringDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        monitoringDetailsDialog.setContentView(R.layout.monitoring_details_layout);
                                        monitoringDetailsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                        monitoringDetailsDialog.getWindow().setGravity(Gravity.CENTER);

                                        ImageView iv_close = (ImageView) monitoringDetailsDialog.findViewById(R.id.iv_close);
                                        TextView tv_monitoring_title = (TextView) monitoringDetailsDialog.findViewById(R.id.tv_monitoring_title);
                                        TextView tv_project_monitoring_id = (TextView) monitoringDetailsDialog.findViewById(R.id.tv_project_monitoring_id);
                                        TextView tv_monitoring_remarks = (TextView) monitoringDetailsDialog.findViewById(R.id.tv_monitoring_remarks);
                                        LinearLayout ll_monitoring_document = (LinearLayout) monitoringDetailsDialog.findViewById(R.id.ll_monitoring_document);
                                        tv_monitoring_title.setText(jsonObjectresult.optString("LDT_TITLE"));
                                        tv_project_monitoring_id.setText(jsonObjectresult.optString("LDT_LEVEL_DATA_ID"));
                                        tv_monitoring_remarks.setText(jsonObjectresult.optString("LDT_REMARKS"));
                                        JSONArray jsonArrayDocfiles = jsonObjectresult.optJSONArray("docfiles");
                                        final String[] arrayFilePath = new String[jsonArrayDocfiles.length()];
                                        if (jsonArrayDocfiles.length() > 0) {
                                            for (int i = 0; i < jsonArrayDocfiles.length(); i++) {
                                                layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                                                View docViewLayout = layoutInflater.inflate(R.layout.download_layout, null);
                                                TextView tv_file_name = (TextView) docViewLayout.findViewById(R.id.tv_file_name);
                                                final ImageView iv_set_download = (ImageView) docViewLayout.findViewById(R.id.iv_set_download);

                                                try {
                                                    iv_set_download.setTag(i);
                                                    arrayFilePath[i] = jsonArrayDocfiles.get(i).toString();
                                                    String fileName = jsonArrayDocfiles.get(i).toString().substring(jsonArrayDocfiles.get(i).toString().lastIndexOf('/') + 1);
                                                    tv_file_name.setText(fileName);
                                                    iv_set_download.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                                            String fileURL = arrayFilePath[position];
                                                            String url = fileURL;
                                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                                            Uri u = Uri.parse(url);
                                                            intent.setData(u);
                                                            startActivity(intent);
                                                        }
                                                    });


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                ll_monitoring_document.addView(docViewLayout);
                                            }
                                        } else {

                                        }


                                        iv_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                monitoringDetailsDialog.dismiss();
                                            }
                                        });


                                        monitoringDetailsDialog.setCanceledOnTouchOutside(false);
                                        monitoringDetailsDialog.show();
                                    } else {
                                        Util.showMessageWithOk(mActivity, "Monitoring Details Not Found");
                                    }

                                }

                                @Override
                                public void onError() {

                                }
                            });


                        }
                    });

                    ll_monitering_distribution.addView(monitoringLayout);

                }
            } else
                ll_monitering.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Method to set Data for fund distribution
    private void setMou() {

        try {
            JSONArray jsonArrayMou = new JSONArray(_projectDetails.getMou());
            if (jsonArrayMou != null && jsonArrayMou.length() > 0) {
                final String[] mouId = new String[jsonArrayMou.length()];
                for (int i = 0; i < jsonArrayMou.length(); i++) {
                    layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View mouViewLayout = layoutInflater.inflate(R.layout.single_row_mou, null);
                    TextView tv_mou_id = (TextView) mouViewLayout.findViewById(R.id.tv_mou_id);
                    TextView tv_mou_approval_status = (TextView) mouViewLayout.findViewById(R.id.tv_mou_approval_status);
                    TextView tv_mou_remarks_amount = (TextView) mouViewLayout.findViewById(R.id.tv_mou_remarks_amount);

                    final ImageView iv_details = (ImageView) mouViewLayout.findViewById(R.id.iv_details);
                    iv_details.setTag(i);


                    tv_mou_id.setText(jsonArrayMou.optJSONObject(i).optString("MOU_ID"));
                    tv_mou_approval_status.setText(jsonArrayMou.optJSONObject(i).optString("MOU_APPROVAL_STATUS"));
                    tv_mou_remarks_amount.setText(jsonArrayMou.optJSONObject(i).optString("MOU_REMARKS"));


                    mouId[i] = jsonArrayMou.optJSONObject(i).optString("MOU_ID");

                    iv_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            volleyTaskManager = new VolleyTaskManager(mActivity);
                            int position = Integer.valueOf((Integer) iv_details.getTag());
                            String mouid = mouId[position];
                            String paramlist = "MOU_ID=" + mouid;
                            volleyTaskManager.mouDitails(paramlist, true, new ServerResponseCallback() {
                                @Override
                                public void onSuccess(JSONObject resultJsonObject) {
                                    JSONObject jsonobjectMouDitails = resultJsonObject.optJSONObject("details");
                                    if (jsonobjectMouDitails.toString().length() > 0) {
                                        final Dialog mouDetailsDialog = new Dialog(mActivity);
                                        mouDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        mouDetailsDialog.setContentView(R.layout.mou_details_layout);
                                        mouDetailsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                        mouDetailsDialog.getWindow().setGravity(Gravity.CENTER);
                                        ImageView iv_close = (ImageView) mouDetailsDialog.findViewById(R.id.iv_close);

                                        TextView tv_mou_id = (TextView) mouDetailsDialog.findViewById(R.id.tv_mou_id);
                                        TextView tv_mou_achive = (TextView) mouDetailsDialog.findViewById(R.id.tv_mou_achive);
                                        TextView tv_mou_remarks = (TextView) mouDetailsDialog.findViewById(R.id.tv_mou_remarks);
                                        TextView tv_mou_approval_remarks = (TextView) mouDetailsDialog.findViewById(R.id.tv_mou_approval_remarks);
                                        TextView tv_mou_date = (TextView) mouDetailsDialog.findViewById(R.id.tv_mou_date);

                                        TextView tv_mou_track_title = (TextView) mouDetailsDialog.findViewById(R.id.tv_mou_track_title);
                                        LinearLayout ll_mou_track = (LinearLayout) mouDetailsDialog.findViewById(R.id.ll_mou_track);
                                        LinearLayout ll_mou_document = (LinearLayout) mouDetailsDialog.findViewById(R.id.ll_mou_document);
                                        TextView tv_moufile_title = (TextView) mouDetailsDialog.findViewById(R.id.tv_moufile_title);

                                        TextView tv_status1 = (TextView) mouDetailsDialog.findViewById(R.id.tv_status1);
                                        TextView tv_status2 = (TextView) mouDetailsDialog.findViewById(R.id.tv_status2);

                                        tv_mou_id.setText(jsonobjectMouDitails.optString("MOU_ID"));
                                        tv_mou_achive.setText(jsonobjectMouDitails.optString("MOU_FL_ARCHIVE"));
                                        tv_mou_remarks.setText(jsonobjectMouDitails.optString("MOU_REMARKS"));
                                        tv_mou_approval_remarks.setText(jsonobjectMouDitails.optString("MOU_APPROVAL_REMARKS"));
                                        tv_mou_date.setText(jsonobjectMouDitails.optString("MOU_UPD_TS"));

                                        JSONObject mouApprovalStatus = jsonobjectMouDitails.optJSONObject("MOU_APPROVAL_STATUS");

                                        if (mouApprovalStatus.optString("text").toString().equalsIgnoreCase("Approved")) {


                                            tv_status1.setText(mouApprovalStatus.optString("text"));
                                            tv_status1.setBackgroundColor(Color.parseColor("#82af6f"));
                                            tv_status2.setText(mouApprovalStatus.optString("coment"));
                                            tv_status2.setBackgroundColor(Color.parseColor("#82af6f"));
                                        }


                                        iv_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                mouDetailsDialog.dismiss();
                                            }
                                        });


                                        JSONArray clouserTrackList = resultJsonObject.optJSONArray("mou_track");
                                        if (clouserTrackList.length() > 0) {

                                            for (int i = 0; i < clouserTrackList.length(); i++) {
                                                View viewProposal = LayoutInflater.from(mActivity).inflate(R.layout.approval_track_proposal_view_layout, null);
                                                final TextView tv_approval_deviation_status = (TextView) viewProposal.findViewById(R.id.tv_approval_status);
                                                final TextView tv_user = (TextView) viewProposal.findViewById(R.id.tv_user);
                                                final TextView tv_date_time = (TextView) viewProposal.findViewById(R.id.tv_date_time);
                                                final TextView tv_remarks = (TextView) viewProposal.findViewById(R.id.tv_remarks);

                                                //  tv_approval_deviation_status.setText(panApprovalList.optJSONObject(i).optString("text"));
                                                tv_user.setText(clouserTrackList.optJSONObject(i).optString("username"));
                                                tv_date_time.setText(clouserTrackList.optJSONObject(i).optString("date"));
                                                tv_remarks.setText(clouserTrackList.optJSONObject(i).optString("remarks"));


                                                if (clouserTrackList.optJSONObject(i).optString("text").toString() != null) {
                                                    tv_approval_deviation_status.setText(clouserTrackList.optJSONObject(i).optString("text"));
                                                    if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PREP"))
                                                        tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#abbac3"));
                                                    else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PEND"))
                                                        tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#f89406"));
                                                    else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTH"))
                                                        tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#82af6f"));
                                                    else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTL"))
                                                        tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#428bca"));
                                                    else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("REJCT"))
                                                        tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#de1028"));
                                                    else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFY"))
                                                        tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#f89406"));
                                                    else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFYED")) {
                                                        tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#fee188"));
                                                        tv_approval_deviation_status.setTextColor(Color.parseColor("#000000"));
                                                    }
                                                } else {
                                                    tv_approval_deviation_status.setText("N/A");
                                                    tv_approval_deviation_status.setTextColor(Color.parseColor("#000000"));
                                                }

                                                ll_mou_track.addView(viewProposal);
                                            }
                                        } else {
                                            tv_mou_track_title.setVisibility(View.GONE);
                                        }

                                        JSONArray clouserDocFile = resultJsonObject.optJSONArray("moufile");
                                        if (clouserDocFile.length() > 0) {
                                            final String[] arrayFilePath = new String[clouserDocFile.length()];
                                            for (int j = 0; j < clouserDocFile.length(); j++) {
                                                layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                                                View docViewLayout = layoutInflater.inflate(R.layout.download_layout, null);
                                                TextView tv_file_name = (TextView) docViewLayout.findViewById(R.id.tv_file_name);
                                                final ImageView iv_set_download = (ImageView) docViewLayout.findViewById(R.id.iv_set_download);

                                                try {
                                                    iv_set_download.setTag(j);
                                                    arrayFilePath[j] = clouserDocFile.get(j).toString();
                                                    String fileName = clouserDocFile.get(j).toString().substring(clouserDocFile.get(j).toString().lastIndexOf('/') + 1);
                                                    tv_file_name.setText(fileName);
                                                    iv_set_download.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                                            String fileURL = arrayFilePath[position];
                                                            String url = fileURL;
                                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                                            Uri u = Uri.parse(url);
                                                            intent.setData(u);
                                                            startActivity(intent);
                                                        }
                                                    });


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                ll_mou_document.addView(docViewLayout);
                                            }
                                        } else {
                                            tv_moufile_title.setVisibility(View.GONE);
                                        }
                                        mouDetailsDialog.setCanceledOnTouchOutside(false);
                                        mouDetailsDialog.show();


                                    }


                                }

                                @Override
                                public void onError() {

                                }
                            });
                        }
                    });

                    ll_mou_distribution.addView(mouViewLayout);
                }
            } else
                ll_mou.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Method to set Data for Pan
    private void setPan() {
        try {

            Spanned subSpanned;
            JSONArray jsonArrayPan = new JSONArray(_projectDetails.getPan());
            if (jsonArrayPan != null && jsonArrayPan.length() > 0) {
                final String[] panArrayId = new String[jsonArrayPan.length()];
                for (int i = 0; i < jsonArrayPan.length(); i++) {


                    layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View panViewLayout = layoutInflater.inflate(R.layout.single_row_pan, null);
                    TextView tv_pan_title = (TextView) panViewLayout.findViewById(R.id.tv_pan_title);
                    TextView tv_unique_no = (TextView) panViewLayout.findViewById(R.id.tv_unique_no);
                    TextView tv_pan_amount = (TextView) panViewLayout.findViewById(R.id.tv_pan_amount);
                    TextView tv_pan_status = (TextView) panViewLayout.findViewById(R.id.tv_pan_status);
                    TextView tv_pan_payment_release = (TextView) panViewLayout.findViewById(R.id.tv_pan_payment_release);
                    final ImageView iv_details = (ImageView) panViewLayout.findViewById(R.id.iv_details);
                    iv_details.setTag(i);

                    if (jsonArrayPan.optJSONObject(i).optString("PAP_TITLE").isEmpty())
                        tv_pan_title.setText(jsonArrayPan.optJSONObject(i).optString("PAP_TITLE"));
                    else tv_pan_title.setText("");
                    tv_unique_no.setText(jsonArrayPan.optJSONObject(i).optString("PAP_UNIQ_NO"));
                    tv_pan_amount.setText(jsonArrayPan.optJSONObject(i).optString("PAP_AMOUNT"));
                    tv_pan_status.setText(jsonArrayPan.optJSONObject(i).optString("custom_status"));
                    tv_pan_payment_release.setText(jsonArrayPan.optJSONObject(i).optString("payment_release"));
                    panArrayId[i] = jsonArrayPan.optJSONObject(i).optString("PAP_ID");


                    iv_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int position = Integer.valueOf((Integer) iv_details.getTag());
                            String panid = panArrayId[position];
                            showPanDialog(panid);

                        }
                    });


                    // TODO check here push type and test equality which deviation would u like to open

                    if (isPushType.equalsIgnoreCase("project-pan-view")) {
                        if (projectPanId.equals(panArrayId[i])) {
                            Log.i("projectId==== ", projectPanId);
                            showPanDialog(projectPanId);
                        }
                    }


                    ll_pan_distribution.addView(panViewLayout);
                }

            } else
                ll_pan.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Method to set Data for Deviation
    private void setDeviation() {

        Spanned subSpanned;
        try {
            JSONArray jsonArrayDeviation = new JSONArray(_projectDetails.getDeviation());
            if (jsonArrayDeviation != null && jsonArrayDeviation.length() > 0) {
                final String[] PrdIdArray = new String[jsonArrayDeviation.length()];
                for (int i = 0; i < jsonArrayDeviation.length(); i++) {
                    layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View deviationLayout = layoutInflater.inflate(R.layout.single_row_devation, null);

                    TextView tv_deviation_type = (TextView) deviationLayout.findViewById(R.id.tv_deviation_type);
                    TextView tv_change_in = (TextView) deviationLayout.findViewById(R.id.tv_change_in);
                    TextView tv_additional_cost = (TextView) deviationLayout.findViewById(R.id.tv_additional_cost);
                    TextView tv_additional_day = (TextView) deviationLayout.findViewById(R.id.tv_additional_day);
                    final ImageView iv_details = (ImageView) deviationLayout.findViewById(R.id.iv_details);
                    tv_deviation_type.setText(jsonArrayDeviation.optJSONObject(i).optString("PRD_TYPE"));
                    tv_change_in.setText(jsonArrayDeviation.optJSONObject(i).optString("PRD_CHANGES_IN"));
                    tv_additional_cost.setText(jsonArrayDeviation.optJSONObject(i).optString("PRD_EXTRA_COST"));
                    if (jsonArrayDeviation.optJSONObject(i).optString("PRD_EXTRA_TIMELINE").toString().isEmpty()) {
                        tv_additional_day.setText("0 day");

                    } else
                        tv_additional_day.setText(jsonArrayDeviation.optJSONObject(i).optString("PRD_EXTRA_TIMELINE") + " " + "days");

                    iv_details.setTag(i);
                    PrdIdArray[i] = jsonArrayDeviation.optJSONObject(i).optString("PRD_ID");

                    iv_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = Integer.valueOf((Integer) iv_details.getTag());
                            String PRD_ID = PrdIdArray[position];
                            showDeviatonDialog(PRD_ID);

                        }
                    });

                    // TODO check here push type and test equality which deviation would u like to open

                    if (isPushType.equalsIgnoreCase("project-deviation-view")) {
                        if (deviationId.equals(PrdIdArray[i])) {

                            showDeviatonDialog(deviationId);
                        }
                    }


                    ll_deviation_distribution.addView(deviationLayout);

                }
            } else
                ll_deviation.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDeviatonDialog(final String PRD_ID) {
        volleyTaskManager = new VolleyTaskManager(mActivity);
        // Log.i("PRD_ID=", PRD_ID);

                        /*    PRD_ID=PRD0000000044&USER_ID=USR0000002&DIVISION_ID=D0000001
                            String param = "PRD_ID=" + PRD_ID+"&USER_ID="++"&DIVISION_ID="+;*/

        UserClass user = Util.fetchUserClass(mActivity);
        String paramlist = "USER_ID=" + user.getUserId() + "&PRD_ID=" + PRD_ID + "&DIVISION_ID=" + prefs.getDivisionId() + "&ROLE_ID=" + user.getRoleId();
        // String param = "PRD_ID=" + "PRD0000000048";
        volleyTaskManager.doDeviationDitails(paramlist, true, new ServerResponseCallback() {
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                JSONObject jsonObjectresult = resultJsonObject.optJSONObject("deviation_data");
                if (jsonObjectresult.toString().length() > 0) {
                    final Dialog deviationDitailsDialog = new Dialog(mActivity);
                    deviationDitailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    deviationDitailsDialog.setContentView(R.layout.deviation_details_layout);
                    deviationDitailsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    deviationDitailsDialog.getWindow().setGravity(Gravity.CENTER);

                    ImageView iv_close = (ImageView) deviationDitailsDialog.findViewById(R.id.iv_close);
                    TextView tv_deviation_details_type = (TextView) deviationDitailsDialog.findViewById(R.id.tv_deviation_details_type);
                    TextView tv_deviation_change = (TextView) deviationDitailsDialog.findViewById(R.id.tv_deviation_change);
                    TextView tv_deviation_date = (TextView) deviationDitailsDialog.findViewById(R.id.tv_deviation_date);
                    TextView tv_deviation_reason = (TextView) deviationDitailsDialog.findViewById(R.id.tv_deviation_reason);
                    TextView tv_additional_cost = (TextView) deviationDitailsDialog.findViewById(R.id.tv_additional_cost);
                    TextView tv_additional_time = (TextView) deviationDitailsDialog.findViewById(R.id.tv_additional_time);
                    TextView tv_approval_status = (TextView) deviationDitailsDialog.findViewById(R.id.tv_approval_status);
                    LinearLayout ll_approval_track = (LinearLayout) deviationDitailsDialog.findViewById(R.id.ll_approval_track);
                    TextView tv_docfile_title = (TextView) deviationDitailsDialog.findViewById(R.id.tv_docfile_title);
                    LinearLayout ll_deviation_document = (LinearLayout) deviationDitailsDialog.findViewById(R.id.ll_deviation_document);

                    final EditText et_approval_review = (EditText) deviationDitailsDialog.findViewById(R.id.et_approval_review);
                    final LinearLayout ll_approval_reject = (LinearLayout) deviationDitailsDialog.findViewById(R.id.ll_approval_reject);
                    final LinearLayout ll_resend = (LinearLayout) deviationDitailsDialog.findViewById(R.id.ll_resend);
                    final LinearLayout ll_accept = (LinearLayout) deviationDitailsDialog.findViewById(R.id.ll_accept);
                    final RelativeLayout rl_approval_review = (RelativeLayout) deviationDitailsDialog.findViewById(R.id.rl_approval_review_details);
                    final String projectIdForDeviation = jsonObjectresult.optString("PRD_PROJECT_ID").toString();
                    if (jsonObjectresult.optBoolean("approval_sec_visible")) {

                        rl_approval_review.setVisibility(View.VISIBLE);
//TODO for approval accept

                        ll_accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                volleyTaskManager = new VolleyTaskManager(mActivity);

                                if (et_approval_review.getText().toString().length() > 0) {

                                    UserClass user = Util.fetchUserClass(mActivity);

                                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                                    paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                                    paramsMap.put("USER_ID", user.getUserId());
                                    paramsMap.put("DIVISION_ID", prefs.getDivisionId());
                                    paramsMap.put("PROJECT_ID", projectIdForDeviation);
                                    paramsMap.put("PRD_ID", PRD_ID);
                                    paramsMap.put("action_type", "accept");
                                    volleyTaskManager.doApprovealDeviationReview(paramsMap, true, new ServerResponseCallback() {
                                        @Override
                                        public void onSuccess(JSONObject resultJsonObject) {
                                            Log.d("Responce:", resultJsonObject.toString());
                                            if (resultJsonObject.optString("status").toString().equals("true")) {
                                                Util.showDialog(mActivity, "Cipla", resultJsonObject.optString("message").toString(), new InterfaceDialogCallback() {
                                                    @Override
                                                    public void onClickAlertPositiveButton() {
                                                        // TODO Approval layout will be gone after successful
                                                        // rl_approval_review.setVisibility(View.GONE);
                                                        deviationDitailsDialog.dismiss();
                                                        showDeviatonDialog(PRD_ID);

                                                    }

                                                    @Override
                                                    public void onClickAlertNegativeButton() {

                                                    }
                                                });
                                            } else {
                                                Util.showMessageWithOk(mActivity, "Error! UnSuccessful");
                                            }

                                        }

                                        @Override
                                        public void onError() {
                                            Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                                        }
                                    });


                                } else {
                                    Util.showMessageWithOk(mActivity, "Please enter your review!");

                                }

                            }


                        });

                        //TODO for resent  ....

                        ll_resend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                volleyTaskManager = new VolleyTaskManager(mActivity);

                                if (et_approval_review.getText().toString().length() > 0) {

                                    UserClass user = Util.fetchUserClass(mActivity);

                                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                                    paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                                    paramsMap.put("USER_ID", user.getUserId());
                                    paramsMap.put("DIVISION_ID", prefs.getDivisionId());
                                    paramsMap.put("PROJECT_ID", projectIdForDeviation);
                                    paramsMap.put("PRD_ID", PRD_ID);
                                    paramsMap.put("action_type", "modify");
                                    volleyTaskManager.doApprovealDeviationReview(paramsMap, true, new ServerResponseCallback() {
                                        @Override
                                        public void onSuccess(JSONObject resultJsonObject) {
                                            Log.d("Responce:", resultJsonObject.toString());
                                            if (resultJsonObject.optString("status").toString().equals("true")) {
                                                Util.showDialog(mActivity, "Cipla", resultJsonObject.optString("message").toString(), new InterfaceDialogCallback() {
                                                    @Override
                                                    public void onClickAlertPositiveButton() {
                                                        // TODO Approval layout will be gone after successful
                                                        deviationDitailsDialog.dismiss();
                                                        showDeviatonDialog(PRD_ID);


                                                    }

                                                    @Override
                                                    public void onClickAlertNegativeButton() {

                                                    }
                                                });
                                            }

                                        }

                                        @Override
                                        public void onError() {
                                            Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                                        }
                                    });


                                } else {
                                    Util.showMessageWithOk(mActivity, "Please enter your review!");

                                }

                            }


                        });
//TODO for reject approve .....

                        ll_approval_reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                volleyTaskManager = new VolleyTaskManager(mActivity);

                                if (et_approval_review.getText().toString().length() > 0) {

                                    UserClass user = Util.fetchUserClass(mActivity);

                                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                                    paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                                    paramsMap.put("USER_ID", user.getUserId());
                                    paramsMap.put("DIVISION_ID", prefs.getDivisionId());
                                    paramsMap.put("PROJECT_ID", projectIdForDeviation);
                                    paramsMap.put("PRD_ID", PRD_ID);
                                    paramsMap.put("action_type", "reject");
                                    volleyTaskManager.doApprovealDeviationReview(paramsMap, true, new ServerResponseCallback() {
                                        @Override
                                        public void onSuccess(JSONObject resultJsonObject) {
                                            Log.d("Responce:", resultJsonObject.toString());
                                            if (resultJsonObject.optString("status").toString().equals("true")) {
                                                Util.showDialog(mActivity, "Cipla", resultJsonObject.optString("message").toString(), new InterfaceDialogCallback() {
                                                    @Override
                                                    public void onClickAlertPositiveButton() {
                                                        // TODO Approval layout will be gone after successful
                                                        deviationDitailsDialog.dismiss();
                                                        showDeviatonDialog(PRD_ID);


                                                    }

                                                    @Override
                                                    public void onClickAlertNegativeButton() {

                                                    }
                                                });
                                            }

                                        }

                                        @Override
                                        public void onError() {
                                            Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                                        }
                                    });


                                } else {
                                    Util.showMessageWithOk(mActivity, "Please enter your review!");

                                }

                            }


                        });


                    } else {
                        rl_approval_review.setVisibility(View.GONE);
                    }


                    tv_deviation_details_type.setText(jsonObjectresult.optString("PRD_TYPE"));
                    tv_deviation_change.setText(jsonObjectresult.optString("PRD_CHANGES_IN"));
                    tv_deviation_date.setText(jsonObjectresult.optString("PRD_CRT_TS"));
                    tv_deviation_reason.setText(jsonObjectresult.optString("PRD_REASON"));
                    tv_additional_cost.setText(jsonObjectresult.optString("PRD_EXTRA_COST"));
                    tv_additional_time.setText(jsonObjectresult.optString("PRD_EXTRA_TIMELINE"));
                    JSONObject jsonApprovalStaus = jsonObjectresult.optJSONObject("PRD_APPROVAL_STATUS");


                    if (jsonApprovalStaus.optString("text").toString() != null) {
                        tv_approval_status.setText(jsonApprovalStaus.optString("text") + " " + jsonApprovalStaus.optString("approval_sec_note"));
                        if (jsonApprovalStaus.optString("type").toString().equalsIgnoreCase("PREP"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#abbac3"));
                        else if (jsonApprovalStaus.optString("type").toString().equalsIgnoreCase("PEND"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                        else if (jsonApprovalStaus.optString("type").toString().equalsIgnoreCase("ACEPTH"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#82af6f"));
                        else if (jsonApprovalStaus.optString("type").toString().equalsIgnoreCase("ACEPTL"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#428bca"));
                        else if (jsonApprovalStaus.optString("type").toString().equalsIgnoreCase("REJCT"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#de1028"));
                        else if (jsonApprovalStaus.optString("type").toString().equalsIgnoreCase("MODFY"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                        else if (jsonApprovalStaus.optString("type").toString().equalsIgnoreCase("MODFYED")) {
                            tv_approval_status.setBackgroundColor(Color.parseColor("#fee188"));
                            tv_approval_status.setTextColor(Color.parseColor("#000000"));
                        }
                    } else {
                        tv_approval_status.setText("N/A");
                        tv_approval_status.setTextColor(Color.parseColor("#000000"));
                    }

                    JSONArray approvalList = jsonObjectresult.optJSONArray("approval_list");
                    if (approvalList.length() > 0) {

                        for (int i = 0; i < approvalList.length(); i++) {
                            View viewProposal = LayoutInflater.from(mActivity).inflate(R.layout.approval_track_proposal_view_layout, null);
                            final TextView tv_approval_deviation_status = (TextView) viewProposal.findViewById(R.id.tv_approval_status);
                            final TextView tv_user = (TextView) viewProposal.findViewById(R.id.tv_user);
                            final TextView tv_date_time = (TextView) viewProposal.findViewById(R.id.tv_date_time);
                            final TextView tv_remarks = (TextView) viewProposal.findViewById(R.id.tv_remarks);

                            if (approvalList.optJSONObject(i).optString("text").toString() != null) {
                                tv_approval_deviation_status.setText(approvalList.optJSONObject(i).optString("text"));
                                if (approvalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PREP"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#abbac3"));
                                else if (approvalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PEND"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#f89406"));
                                else if (approvalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTH"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#82af6f"));
                                else if (approvalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTL"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#428bca"));
                                else if (approvalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("REJCT"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#de1028"));
                                else if (approvalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFY"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#f89406"));
                                else if (approvalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFYED")) {
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#fee188"));
                                    tv_approval_deviation_status.setTextColor(Color.parseColor("#000000"));
                                }
                            } else {
                                tv_approval_deviation_status.setText("N/A");
                                tv_approval_deviation_status.setTextColor(Color.parseColor("#000000"));
                            }

                            tv_user.setText(approvalList.optJSONObject(i).optString("username"));
                            tv_date_time.setText(approvalList.optJSONObject(i).optString("date"));

                            if (approvalList.optJSONObject(i).optString("remarks").toString() == null)

                                tv_remarks.setText("");
                            else
                                tv_remarks.setText(approvalList.optJSONObject(i).optString("remarks"));


                            ll_approval_track.addView(viewProposal);
                        }


                    } else {

                    }

                    JSONArray docFileList = jsonObjectresult.optJSONArray("docfiles");
                    final String[] arrayFilePath = new String[docFileList.length()];
                    if (docFileList.length() > 0) {
                        for (int j = 0; j < docFileList.length(); j++) {
                            layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                            View docViewLayout = layoutInflater.inflate(R.layout.download_layout, null);
                            TextView tv_file_name = (TextView) docViewLayout.findViewById(R.id.tv_file_name);
                            final ImageView iv_set_download = (ImageView) docViewLayout.findViewById(R.id.iv_set_download);

                            try {
                                iv_set_download.setTag(j);
                                arrayFilePath[j] = docFileList.get(j).toString();
                                String fileName = docFileList.get(j).toString().substring(docFileList.get(j).toString().lastIndexOf('/') + 1);
                                tv_file_name.setText(fileName);
                                iv_set_download.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                        String fileURL = arrayFilePath[position];
                                        String url = fileURL;
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        Uri u = Uri.parse(url);
                                        intent.setData(u);
                                        startActivity(intent);
                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ll_deviation_document.addView(docViewLayout);
                        }
                    } else {
                        tv_docfile_title.setVisibility(View.GONE);

                    }
                    iv_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deviationDitailsDialog.dismiss();
                        }
                    });

                    deviationDitailsDialog.setCanceledOnTouchOutside(false);
                    deviationDitailsDialog.show();

                } else {
                    Util.showMessageWithOk(mActivity, "Deviation Details Not Found");
                }
            }

            @Override
            public void onError() {

            }
        });

    }


    private void showPanDialog(final String panid) {
        volleyTaskManager = new VolleyTaskManager(mActivity);
        UserClass user = Util.fetchUserClass(mActivity);
        //  String paramlist = "USR_USER_ID=" + user.getUserId() + "&PAP_ID=" + panid + "&DIVISION_ID=" + prefs.getDivisionId();
        String paramlist = "USR_USER_ID=" + user.getUserId() + "&PAP_ID=" + panid + "&DIVISION_ID=" + prefs.getDivisionId() + "&ROLE_ID=" + user.getRoleId();
        volleyTaskManager.doPanDitails(paramlist, true, new ServerResponseCallback() {
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                JSONObject jsonobjectPanDitails = resultJsonObject.optJSONObject("details");
                if (jsonobjectPanDitails.toString().length() > 0) {
                    final Dialog panDetailsDialog = new Dialog(mActivity);
                    panDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    panDetailsDialog.setContentView(R.layout.pan_details_layout);
                    panDetailsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    panDetailsDialog.getWindow().setGravity(Gravity.CENTER);


                    TextView tv_pan_title = (TextView) panDetailsDialog.findViewById(R.id.tv_pan_title);
                    TextView tv_unique_no = (TextView) panDetailsDialog.findViewById(R.id.tv_unique_no);
                    TextView tv_deliverables = (TextView) panDetailsDialog.findViewById(R.id.tv_deliverables);

                    TextView tv_expected_date_of_payment = (TextView) panDetailsDialog.findViewById(R.id.tv_expected_date_of_payment);

                    TextView tv_amount = (TextView) panDetailsDialog.findViewById(R.id.tv_amount);
                    TextView tv_pan_remarks = (TextView) panDetailsDialog.findViewById(R.id.tv_pan_remarks);
                    TextView tv_mou_application = (TextView) panDetailsDialog.findViewById(R.id.tv_mou_application);
                    TextView tv_status1 = (TextView) panDetailsDialog.findViewById(R.id.tv_status1);
                    TextView tv_status2 = (TextView) panDetailsDialog.findViewById(R.id.tv_status2);
                    ImageView iv_close = (ImageView) panDetailsDialog.findViewById(R.id.iv_close);
                    final String panId = jsonobjectPanDitails.optString("PAP_ID").toString();
                    final String projPanId=jsonobjectPanDitails.optString("PAP_PROJECT_ID").toString();

                    if (jsonobjectPanDitails.optString("PAP_TITLE") != null)
                        tv_pan_title.setText(jsonobjectPanDitails.optString("PAP_TITLE"));
                    else
                        tv_pan_title.setText("");


                    if (jsonobjectPanDitails.optString("PAP_UNIQ_NO") != null)
                        tv_unique_no.setText(jsonobjectPanDitails.optString("PAP_UNIQ_NO"));
                    else
                        tv_unique_no.setText("");


                    if (jsonobjectPanDitails.optString("PAP_DELIVERABLE") != null)
                        tv_deliverables.setText(jsonobjectPanDitails.optString("PAP_DELIVERABLE"));
                    else
                        tv_deliverables.setText("");


                    if (jsonobjectPanDitails.optString("PAP_AMOUNT") != null)
                        tv_amount.setText(jsonobjectPanDitails.optString("PAP_AMOUNT"));
                    else
                        tv_amount.setText("");


                    if (jsonobjectPanDitails.optString("PAP_REMARKS") != null)
                        tv_pan_remarks.setText(jsonobjectPanDitails.optString("PAP_REMARKS"));
                    else
                        tv_pan_remarks.setText(" ");


                    if (jsonobjectPanDitails.optString("PAP_MOU_APPLY") != null)
                        tv_mou_application.setText(jsonobjectPanDitails.optString("PAP_MOU_APPLY"));
                    else
                        tv_mou_application.setText("");

                    JSONObject panApprovalStatusJson = jsonobjectPanDitails.optJSONObject("PAP_APPROVAL_STATUS");

                                     /*   if (panApprovalStatusJson.optString("text_1").toString().equals("Approved")) {
                                            tv_status1.setText(panApprovalStatusJson.optString("text_1"));
                                            tv_status1.setBackgroundColor(Color.parseColor("#428bca"));
                                            tv_status2.setText(panApprovalStatusJson.optString("text_2"));
                                            tv_status2.setBackgroundColor(Color.parseColor("#428bca"));
                                        } else {
                                            tv_status1.setText("");
                                            tv_status2.setText("");
                                        }*/


                    tv_status1.setText(panApprovalStatusJson.optString("text_1"));
                    tv_status1.setTextColor(Color.parseColor("#ffffff"));
                    tv_status2.setText(panApprovalStatusJson.optString("text_2"));
                    tv_status2.setTextColor(Color.parseColor("#ffffff"));


                    if (panApprovalStatusJson.optString("type").toString().equalsIgnoreCase("PREP")) {
                        tv_status1.setBackgroundColor(Color.parseColor("#abbac3"));
                        tv_status2.setBackgroundColor(Color.parseColor("#abbac3"));
                    } else if (panApprovalStatusJson.optString("type").toString().equalsIgnoreCase("PEND")) {
                        tv_status1.setBackgroundColor(Color.parseColor("#f89406"));
                        tv_status2.setBackgroundColor(Color.parseColor("#f89406"));
                    } else if (panApprovalStatusJson.optString("type").toString().equalsIgnoreCase("ACEPTH")) {
                        tv_status1.setBackgroundColor(Color.parseColor("#82af6f"));
                        tv_status2.setBackgroundColor(Color.parseColor("#82af6f"));
                    } else if (panApprovalStatusJson.optString("type").toString().equalsIgnoreCase("ACEPTL")) {
                        tv_status1.setBackgroundColor(Color.parseColor("#428bca"));
                        tv_status2.setBackgroundColor(Color.parseColor("#428bca"));
                    } else if (panApprovalStatusJson.optString("type").toString().equalsIgnoreCase("REJCT")) {
                        tv_status1.setBackgroundColor(Color.parseColor("#de1028"));
                        tv_status2.setBackgroundColor(Color.parseColor("#de1028"));
                    } else if (panApprovalStatusJson.optString("type").toString().equalsIgnoreCase("MODFY")) {
                        tv_status1.setBackgroundColor(Color.parseColor("#f89406"));
                        tv_status2.setBackgroundColor(Color.parseColor("#f89406"));
                    } else if (panApprovalStatusJson.optString("type").toString().equalsIgnoreCase("MODFYED")) {
                        tv_status1.setBackgroundColor(Color.parseColor("#fee188"));
                        tv_status2.setBackgroundColor(Color.parseColor("#fee188"));
                    } else {
                        tv_status1.setTextColor(Color.parseColor("#000000"));
                        tv_status2.setTextColor(Color.parseColor("#000000"));
                    }

                    if (panApprovalStatusJson.optString("text_1") == null || panApprovalStatusJson.optString("text_1").isEmpty()) {
                        tv_status1.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    if (panApprovalStatusJson.optString("text_2") == null || panApprovalStatusJson.optString("text_2").isEmpty()) {
                        tv_status2.setBackgroundColor(Color.parseColor("#ffffff"));
                    }


                    if (jsonobjectPanDitails.optString("PAP_EXPECTED_DATE") != null)
                        tv_expected_date_of_payment.setText(jsonobjectPanDitails.optString("PAP_EXPECTED_DATE"));
                    else
                        tv_expected_date_of_payment.setText("");


                    iv_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            panDetailsDialog.dismiss();
                        }
                    });


                    final EditText et_approval_review = (EditText) panDetailsDialog.findViewById(R.id.et_approval_review);
                    final LinearLayout ll_approval_reject = (LinearLayout) panDetailsDialog.findViewById(R.id.ll_approval_reject);
                    final LinearLayout ll_resend = (LinearLayout) panDetailsDialog.findViewById(R.id.ll_resend);
                    final LinearLayout ll_accept = (LinearLayout) panDetailsDialog.findViewById(R.id.ll_accept);
                    final RelativeLayout rl_approval_review = (RelativeLayout) panDetailsDialog.findViewById(R.id.rl_approval_review_details);

                    if (resultJsonObject.optBoolean("approval_sec_visible")) {
                        // if (true) {
                        rl_approval_review.setVisibility(View.VISIBLE);
//for approval accept

                        ll_accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                volleyTaskManager = new VolleyTaskManager(mActivity);

                                if (et_approval_review.getText().toString().length() > 0) {
                                    UserClass user = Util.fetchUserClass(mActivity);
                                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                                    paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                                    paramsMap.put("USER_ID", user.getUserId());
                                    paramsMap.put("DIVISION_ID",prefs.getDivisionId());
                                    paramsMap.put("PROJECT_ID",projPanId);
                                    paramsMap.put("PAP_ID", panId);
                                    paramsMap.put("action_type", "accept");
                                    volleyTaskManager.doApprovealprojectPanReview(paramsMap, true, new ServerResponseCallback() {
                                        @Override
                                        public void onSuccess(JSONObject resultJsonObject) {
                                            Log.d("Responce:", resultJsonObject.toString());
                                            if (resultJsonObject.optString("status").toString().equals("true")) {
                                                Util.showDialog(mActivity, "Cipla", "Successful", new InterfaceDialogCallback() {
                                                    @Override
                                                    public void onClickAlertPositiveButton() {
                                                        // TODO Approval layout will be gone after successful
                                                        //  rl_approval_review.setVisibility(View.GONE);

                                                        panDetailsDialog.dismiss();
                                                        showPanDialog(panId);


                                                    }

                                                    @Override
                                                    public void onClickAlertNegativeButton() {

                                                    }
                                                });
                                            } else {
                                                Util.showMessageWithOk(mActivity, "Error! UnSuccessful");
                                            }

                                        }

                                        @Override
                                        public void onError() {
                                            Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                                        }
                                    });


                                } else {
                                    Util.showMessageWithOk(mActivity, "Please enter your review!");

                                }

                            }


                        });


                        //for resent accept ....

                        ll_resend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                volleyTaskManager = new VolleyTaskManager(mActivity);

                                if (et_approval_review.getText().toString().length() > 0) {

                                    UserClass user = Util.fetchUserClass(mActivity);

                                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                                    paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                                    paramsMap.put("USER_ID", user.getUserId());
                                    paramsMap.put("DIVISION_ID", prefs.getDivisionId());
                                    paramsMap.put("PROJECT_ID",projPanId);
                                    paramsMap.put("PAP_ID", panId);
                                    paramsMap.put("action_type", "modify");
                                    volleyTaskManager.doApprovealprojectPanReview(paramsMap, true, new ServerResponseCallback() {
                                        @Override
                                        public void onSuccess(JSONObject resultJsonObject) {
                                            Log.d("Responce:", resultJsonObject.toString());
                                            if (resultJsonObject.optString("status").toString().equals("true")) {
                                                Util.showDialog(mActivity, "Cipla", "Successful", new InterfaceDialogCallback() {
                                                    @Override
                                                    public void onClickAlertPositiveButton() {
                                                        // TODO Approval layout will be gone after successful
                                                        panDetailsDialog.dismiss();
                                                        showPanDialog(panId);


                                                    }

                                                    @Override
                                                    public void onClickAlertNegativeButton() {

                                                    }
                                                });
                                            } else {
                                                Util.showMessageWithOk(mActivity, "Error! UnSuccessful");
                                            }
                                        }

                                        @Override
                                        public void onError() {
                                            Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                                        }
                                    });


                                } else {
                                    Util.showMessageWithOk(mActivity, "Please enter your review!");

                                }

                            }


                        });
// for reject approve .....

                        ll_approval_reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                volleyTaskManager = new VolleyTaskManager(mActivity);

                                if (et_approval_review.getText().toString().length() > 0) {

                                    UserClass user = Util.fetchUserClass(mActivity);

                                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                                    paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                                    paramsMap.put("USER_ID", user.getUserId());
                                    paramsMap.put("DIVISION_ID", prefs.getDivisionId());
                                    paramsMap.put("PROJECT_ID",projPanId);
                                    paramsMap.put("PAP_ID", panId);
                                    paramsMap.put("action_type", "reject");
                                    volleyTaskManager.doApprovealprojectPanReview(paramsMap, true, new ServerResponseCallback() {
                                        @Override
                                        public void onSuccess(JSONObject resultJsonObject) {
                                            Log.d("Responce:", resultJsonObject.toString());
                                            if (resultJsonObject.optString("status").toString().equals("true")) {
                                                Util.showDialog(mActivity, "Cipla", "Successful", new InterfaceDialogCallback() {
                                                    @Override
                                                    public void onClickAlertPositiveButton() {
                                                        // TODO Approval layout will be gone after successful
                                                        panDetailsDialog.dismiss();
                                                        showPanDialog(panId);


                                                    }

                                                    @Override
                                                    public void onClickAlertNegativeButton() {

                                                    }
                                                });
                                            } else {
                                                Util.showMessageWithOk(mActivity, "Error! UnSuccessful");
                                            }

                                        }

                                        @Override
                                        public void onError() {
                                            Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                                        }
                                    });


                                } else {
                                    Util.showMessageWithOk(mActivity, "Please enter your review!");

                                }

                            }


                        });


                    } else {
                        rl_approval_review.setVisibility(View.GONE);
                    }


                    TextView tv_approval_track_title = (TextView) panDetailsDialog.findViewById(R.id.tv_approval_track_title);
                    LinearLayout ll_approval_track = (LinearLayout) panDetailsDialog.findViewById(R.id.ll_approval_track);
                    TextView tv_docfile_title = (TextView) panDetailsDialog.findViewById(R.id.tv_docfile_title);
                    LinearLayout ll_pan_document = (LinearLayout) panDetailsDialog.findViewById(R.id.ll_pan_document);


                    //**payment details tag** //
                    TextView tv_payment_type = (TextView) panDetailsDialog.findViewById(R.id.tv_payment_type);
                    TextView tv_payment_reference_no = (TextView) panDetailsDialog.findViewById(R.id.tv_payment_reference_no);
                    TextView tv_payment_amount = (TextView) panDetailsDialog.findViewById(R.id.tv_payment_amount);
                    TextView tv_payment_TDS_amount = (TextView) panDetailsDialog.findViewById(R.id.tv_payment_TDS_amount);
                    TextView tv_payment_other_deduction = (TextView) panDetailsDialog.findViewById(R.id.tv_payment_other_deduction);
                    TextView tv_payment_bank_name = (TextView) panDetailsDialog.findViewById(R.id.tv_payment_bank_name);
                    TextView tv_payment_branch_name = (TextView) panDetailsDialog.findViewById(R.id.tv_payment_branch_name);
                    TextView tv_payment_date = (TextView) panDetailsDialog.findViewById(R.id.tv_payment_date);
                    TextView tv_payment_receipt = (TextView) panDetailsDialog.findViewById(R.id.tv_payment_receipt);
                    LinearLayout ll_payment_releasedocfiles = (LinearLayout) panDetailsDialog.findViewById(R.id.ll_payment_releasedocfiles);
                    LinearLayout ll_payment_trak = (LinearLayout) panDetailsDialog.findViewById(R.id.ll_payment_trak);


                    JSONObject panPaymentPanDitails = resultJsonObject.optJSONObject("panPaymentLists");
                    if (panPaymentPanDitails != null) {
                        tv_payment_type.setText(panPaymentPanDitails.optString("PPD_PAYMENT_TYPE"));
                        tv_payment_reference_no.setText(panPaymentPanDitails.optString("PPD_CHK_NO"));
                        tv_payment_amount.setText(panPaymentPanDitails.optString("PPD_PAYMENT_AMOUNT"));
                        tv_payment_TDS_amount.setText(panPaymentPanDitails.optString("PPD_TDS_AMOUNT"));
                        tv_payment_other_deduction.setText(panPaymentPanDitails.optString("PPD_OTHER_AMOUNT"));
                        tv_payment_bank_name.setText(panPaymentPanDitails.optString("PPD_BANK_NAME"));
                        tv_payment_branch_name.setText(panPaymentPanDitails.optString("PPD_BRANCH_NAME"));
                        tv_payment_date.setText(panPaymentPanDitails.optString("PPD_PAYMENT_DATE"));


                        JSONArray releasedocfiles = panPaymentPanDitails.optJSONArray("releasedocfiles");
                        if (releasedocfiles.length() > 0) {
                            final String[] arrayFilePath = new String[releasedocfiles.length()];
                            for (int j = 0; j < releasedocfiles.length(); j++) {
                                layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                                View docViewLayout = layoutInflater.inflate(R.layout.download_layout, null);
                                TextView tv_file_name = (TextView) docViewLayout.findViewById(R.id.tv_file_name);
                                final ImageView iv_set_download = (ImageView) docViewLayout.findViewById(R.id.iv_set_download);

                                try {
                                    iv_set_download.setTag(j);
                                    arrayFilePath[j] = releasedocfiles.get(j).toString();
                                    String fileName = releasedocfiles.get(j).toString().substring(releasedocfiles.get(j).toString().lastIndexOf('/') + 1);
                                    tv_file_name.setText(fileName);
                                    iv_set_download.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                            String fileURL = arrayFilePath[position];
                                            String url = fileURL;
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            Uri u = Uri.parse(url);
                                            intent.setData(u);
                                            startActivity(intent);
                                        }
                                    });


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ll_payment_releasedocfiles.addView(docViewLayout);
                            }

                        }
                    } else {
                        ll_payment_trak.setVisibility(View.GONE);
                    }


                    JSONArray panApprovalList = resultJsonObject.optJSONArray("approval_list");
                    if (panApprovalList.length() > 0) {

                        for (int i = 0; i < panApprovalList.length(); i++) {
                            View viewProposal = LayoutInflater.from(mActivity).inflate(R.layout.approval_track_proposal_view_layout, null);
                            final TextView tv_approval_deviation_status = (TextView) viewProposal.findViewById(R.id.tv_approval_status);
                            final TextView tv_user = (TextView) viewProposal.findViewById(R.id.tv_user);
                            final TextView tv_date_time = (TextView) viewProposal.findViewById(R.id.tv_date_time);
                            final TextView tv_remarks = (TextView) viewProposal.findViewById(R.id.tv_remarks);

                            //  tv_approval_deviation_status.setText(panApprovalList.optJSONObject(i).optString("text"));
                            tv_user.setText(panApprovalList.optJSONObject(i).optString("username"));
                            tv_date_time.setText(panApprovalList.optJSONObject(i).optString("date"));
                            tv_remarks.setText(panApprovalList.optJSONObject(i).optString("remarks"));


                            if (panApprovalList.optJSONObject(i).optString("text").toString() != null) {
                                tv_approval_deviation_status.setText(panApprovalList.optJSONObject(i).optString("text"));
                                if (panApprovalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PREP"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#abbac3"));
                                else if (panApprovalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PEND"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#f89406"));
                                else if (panApprovalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTH"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#82af6f"));
                                else if (panApprovalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTL"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#428bca"));
                                else if (panApprovalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("REJCT"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#de1028"));
                                else if (panApprovalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFY"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#f89406"));
                                else if (panApprovalList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFYED")) {
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#fee188"));
                                    tv_approval_deviation_status.setTextColor(Color.parseColor("#000000"));
                                }
                            } else {
                                tv_approval_deviation_status.setText("N/A");
                                tv_approval_deviation_status.setTextColor(Color.parseColor("#000000"));
                            }

                            ll_approval_track.addView(viewProposal);
                        }
                    } else {
                        tv_approval_track_title.setVisibility(View.GONE);
                    }


                    JSONArray docFileList = resultJsonObject.optJSONArray("docfiles");
                    final String[] arrayFilePath = new String[docFileList.length()];
                    if (docFileList.length() > 0) {
                        for (int j = 0; j < docFileList.length(); j++) {
                            layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                            View docViewLayout = layoutInflater.inflate(R.layout.download_layout, null);
                            TextView tv_file_name = (TextView) docViewLayout.findViewById(R.id.tv_file_name);
                            final ImageView iv_set_download = (ImageView) docViewLayout.findViewById(R.id.iv_set_download);

                            try {
                                iv_set_download.setTag(j);
                                arrayFilePath[j] = docFileList.get(j).toString();
                                String fileName = docFileList.get(j).toString().substring(docFileList.get(j).toString().lastIndexOf('/') + 1);
                                tv_file_name.setText(fileName);
                                iv_set_download.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                        String fileURL = arrayFilePath[position];
                                        String url = fileURL;
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        Uri u = Uri.parse(url);
                                        intent.setData(u);
                                        startActivity(intent);
                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ll_pan_document.addView(docViewLayout);
                        }
                    } else {
                        tv_docfile_title.setVisibility(View.GONE);
                    }

                    panDetailsDialog.setCanceledOnTouchOutside(false);
                    panDetailsDialog.show();

                } else {

                }

            }

            @Override
            public void onError() {

            }
        });

    }



    private  void showClouserDialog(final String clouserid){
        volleyTaskManager = new VolleyTaskManager(mActivity);
        UserClass user = Util.fetchUserClass(mActivity);
        //USR_USER_ID=USR0000021&PRC_ID=PRC0000000062&DIVISION_ID=D0000004&ROLE_ID=3
        //String paramlist = "PRC_ID=" + clouserid;

        String paramlist = "USR_USER_ID=" + user.getUserId() + "&PRC_ID=" + clouserid + "&DIVISION_ID=" + prefs.getDivisionId() + "&ROLE_ID=" + user.getRoleId();
        volleyTaskManager.doclouserDitails(paramlist, true, new ServerResponseCallback() {
            @Override
            public void onSuccess(JSONObject resultJsonObject) {
                JSONObject jsonobjectClouserDitails = resultJsonObject.optJSONObject("details");
                if (jsonobjectClouserDitails.toString().length() > 0) {
                    final Dialog clouserDetailsDialog = new Dialog(mActivity);
                    clouserDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    clouserDetailsDialog.setContentView(R.layout.clouser_details_layout);
                    clouserDetailsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    clouserDetailsDialog.getWindow().setGravity(Gravity.CENTER);
                    ImageView iv_close = (ImageView) clouserDetailsDialog.findViewById(R.id.iv_close);
                    TextView tv_clouser_id = (TextView) clouserDetailsDialog.findViewById(R.id.tv_clouser_id);
                    TextView tv_clouser_deliverable = (TextView) clouserDetailsDialog.findViewById(R.id.tv_clouser_deliverable);
                    TextView tv_clouser_suggesion = (TextView) clouserDetailsDialog.findViewById(R.id.tv_clouser_suggesion);
                    TextView tv_clouser_benificiary_amount = (TextView) clouserDetailsDialog.findViewById(R.id.tv_clouser_benificiary_amount);
                    TextView tv_clouser_date = (TextView) clouserDetailsDialog.findViewById(R.id.tv_clouser_date);
                    TextView tv_total_payment_amount = (TextView) clouserDetailsDialog.findViewById(R.id.tv_total_payment_amount);
                    TextView tv_clouser_track_title = (TextView) clouserDetailsDialog.findViewById(R.id.tv_clouser_track_title);
                    LinearLayout ll_clouser_track = (LinearLayout) clouserDetailsDialog.findViewById(R.id.ll_clouser_track);
                    LinearLayout ll_clouser_document = (LinearLayout) clouserDetailsDialog.findViewById(R.id.ll_clouser_document);
                    TextView tv_clouserfile_title = (TextView) clouserDetailsDialog.findViewById(R.id.tv_clouserfile_title);
                    TextView tv_status1 = (TextView) clouserDetailsDialog.findViewById(R.id.tv_status1);
                    TextView tv_cloused_all = (TextView) clouserDetailsDialog.findViewById(R.id.tv_cloused_all);



                    tv_clouser_id.setText(jsonobjectClouserDitails.optString("PRC_ID"));
                    tv_clouser_deliverable.setText(jsonobjectClouserDitails.optString("PRC_DELIVERABLE_ACHIVE"));
                    tv_clouser_suggesion.setText(jsonobjectClouserDitails.optString("PRC_SUGGESTION"));
                    tv_clouser_benificiary_amount.setText(jsonobjectClouserDitails.optString("PRC_BENIFICIARY"));
                    tv_clouser_date.setText(jsonobjectClouserDitails.optString("PRC_CLOSURE_DATE"));
                    tv_total_payment_amount.setText(jsonobjectClouserDitails.optString("PRC_TOT_PAYMENT"));
                    tv_status1.setText(jsonobjectClouserDitails.optString("PRC_STATUS"));

                    if (jsonobjectClouserDitails.optString("PRC_IS_CLOSED_ALL").toString().equalsIgnoreCase("Y"))
                        tv_cloused_all.setText("Yes");
                    else
                        tv_cloused_all.setText("No");


                    iv_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clouserDetailsDialog.dismiss();
                        }
                    });


                    JSONArray clouserTrackList = resultJsonObject.optJSONArray("clouser_track");
                    if (clouserTrackList.length() > 0) {

                        for (int i = 0; i < clouserTrackList.length(); i++) {
                            View viewProposal = LayoutInflater.from(mActivity).inflate(R.layout.approval_track_proposal_view_layout, null);
                            final TextView tv_approval_deviation_status = (TextView) viewProposal.findViewById(R.id.tv_approval_status);
                            final TextView tv_user = (TextView) viewProposal.findViewById(R.id.tv_user);
                            final TextView tv_date_time = (TextView) viewProposal.findViewById(R.id.tv_date_time);
                            final TextView tv_remarks = (TextView) viewProposal.findViewById(R.id.tv_remarks);

                            //  tv_approval_deviation_status.setText(panApprovalList.optJSONObject(i).optString("text"));
                            tv_user.setText(clouserTrackList.optJSONObject(i).optString("username"));
                            tv_date_time.setText(clouserTrackList.optJSONObject(i).optString("date"));
                            tv_remarks.setText(clouserTrackList.optJSONObject(i).optString("remarks"));
                            if (clouserTrackList.optJSONObject(i).optString("text").toString() != null) {
                                tv_approval_deviation_status.setText(clouserTrackList.optJSONObject(i).optString("text"));
                                if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PREP"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#abbac3"));
                                else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PEND"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#f89406"));
                                else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTH"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#82af6f"));
                                else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTL"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#428bca"));
                                else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("REJCT"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#de1028"));
                                else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFY"))
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#f89406"));
                                else if (clouserTrackList.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFYED")) {
                                    tv_approval_deviation_status.setBackgroundColor(Color.parseColor("#fee188"));
                                    tv_approval_deviation_status.setTextColor(Color.parseColor("#000000"));
                                }
                            } else {
                                tv_approval_deviation_status.setText("N/A");
                                tv_approval_deviation_status.setTextColor(Color.parseColor("#000000"));
                            }

                            ll_clouser_track.addView(viewProposal);
                        }
                    } else {
                        tv_clouser_track_title.setVisibility(View.GONE);
                    }


                    //-----

                    final EditText et_approval_review = (EditText) clouserDetailsDialog.findViewById(R.id.et_approval_review);
                    final LinearLayout ll_approval_reject = (LinearLayout) clouserDetailsDialog.findViewById(R.id.ll_approval_reject);
                    final LinearLayout ll_resend = (LinearLayout) clouserDetailsDialog.findViewById(R.id.ll_resend);
                    final LinearLayout ll_accept = (LinearLayout) clouserDetailsDialog.findViewById(R.id.ll_accept);
                    final RelativeLayout rl_approval_review = (RelativeLayout) clouserDetailsDialog.findViewById(R.id.rl_approval_review_details);

                    if (jsonobjectClouserDitails.optBoolean("approval_sec_visible")) {
                        // if (true) {
                        rl_approval_review.setVisibility(View.VISIBLE);
//for approval accept

                        ll_accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                volleyTaskManager = new VolleyTaskManager(mActivity);

                                if (et_approval_review.getText().toString().length() > 0) {
                                    UserClass user = Util.fetchUserClass(mActivity);
                                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                                    paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                                    paramsMap.put("USER_ID", user.getUserId());
                                    paramsMap.put("DIVISION_ID",prefs.getDivisionId());
                                   // paramsMap.put("PROJECT_ID",projPanId);
                                   // paramsMap.put("PAP_ID", panId);
                                    paramsMap.put("action_type", "accept");
                                    volleyTaskManager.doApprovealprojectPanReview(paramsMap, true, new ServerResponseCallback() {
                                        @Override
                                        public void onSuccess(JSONObject resultJsonObject) {
                                            Log.d("Responce:", resultJsonObject.toString());
                                            if (resultJsonObject.optString("status").toString().equals("true")) {
                                                Util.showDialog(mActivity, "Cipla", "Successful", new InterfaceDialogCallback() {
                                                    @Override
                                                    public void onClickAlertPositiveButton() {
                                                        // TODO Approval layout will be gone after successful
                                                        //  rl_approval_review.setVisibility(View.GONE);

                                                        clouserDetailsDialog.dismiss();
                                                       // showClouserDialog(panId);


                                                    }

                                                    @Override
                                                    public void onClickAlertNegativeButton() {

                                                    }
                                                });
                                            } else {
                                                Util.showMessageWithOk(mActivity, "Error! UnSuccessful");
                                            }

                                        }

                                        @Override
                                        public void onError() {
                                            Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                                        }
                                    });


                                } else {
                                    Util.showMessageWithOk(mActivity, "Please enter your review!");

                                }

                            }


                        });


                        //for resent accept ....

                        ll_resend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                volleyTaskManager = new VolleyTaskManager(mActivity);

                                if (et_approval_review.getText().toString().length() > 0) {

                                    UserClass user = Util.fetchUserClass(mActivity);

                                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                                    paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                                    paramsMap.put("USER_ID", user.getUserId());
                                    paramsMap.put("DIVISION_ID", prefs.getDivisionId());
                                   // paramsMap.put("PROJECT_ID",projPanId);
                                    //paramsMap.put("PAP_ID", panId);
                                    paramsMap.put("action_type", "modify");
                                    volleyTaskManager.doApprovealprojectPanReview(paramsMap, true, new ServerResponseCallback() {
                                        @Override
                                        public void onSuccess(JSONObject resultJsonObject) {
                                            Log.d("Responce:", resultJsonObject.toString());
                                            if (resultJsonObject.optString("status").toString().equals("true")) {
                                                Util.showDialog(mActivity, "Cipla", "Successful", new InterfaceDialogCallback() {
                                                    @Override
                                                    public void onClickAlertPositiveButton() {
                                                        // TODO Approval layout will be gone after successful
                                                        clouserDetailsDialog.dismiss();
                                                       // showPanDialog(panId);


                                                    }

                                                    @Override
                                                    public void onClickAlertNegativeButton() {

                                                    }
                                                });
                                            } else {
                                                Util.showMessageWithOk(mActivity, "Error! UnSuccessful");
                                            }
                                        }

                                        @Override
                                        public void onError() {
                                            Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                                        }
                                    });


                                } else {
                                    Util.showMessageWithOk(mActivity, "Please enter your review!");

                                }

                            }


                        });
// for reject approve .....

                        ll_approval_reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                volleyTaskManager = new VolleyTaskManager(mActivity);

                                if (et_approval_review.getText().toString().length() > 0) {

                                    UserClass user = Util.fetchUserClass(mActivity);

                                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                                    paramsMap.put("APT_REMARKS", et_approval_review.getText().toString());
                                    paramsMap.put("USER_ID", user.getUserId());
                                    paramsMap.put("DIVISION_ID", prefs.getDivisionId());
                                 //   paramsMap.put("PROJECT_ID",projPanId);
                                 //   paramsMap.put("PAP_ID", panId);
                                    paramsMap.put("action_type", "reject");
                                    volleyTaskManager.doApprovealprojectPanReview(paramsMap, true, new ServerResponseCallback() {
                                        @Override
                                        public void onSuccess(JSONObject resultJsonObject) {
                                            Log.d("Responce:", resultJsonObject.toString());
                                            if (resultJsonObject.optString("status").toString().equals("true")) {
                                                Util.showDialog(mActivity, "Cipla", "Successful", new InterfaceDialogCallback() {
                                                    @Override
                                                    public void onClickAlertPositiveButton() {
                                                        // TODO Approval layout will be gone after successful
                                                        clouserDetailsDialog.dismiss();
                                           //             showPanDialog(panId);


                                                    }

                                                    @Override
                                                    public void onClickAlertNegativeButton() {

                                                    }
                                                });
                                            } else {
                                                Util.showMessageWithOk(mActivity, "Error! UnSuccessful");
                                            }

                                        }

                                        @Override
                                        public void onError() {
                                            Util.showMessageWithOk(mActivity, "Error! UnSuccessful");

                                        }
                                    });


                                } else {
                                    Util.showMessageWithOk(mActivity, "Please enter your review!");

                                }

                            }


                        });


                    } else {
                        rl_approval_review.setVisibility(View.GONE);
                    }

                    //

                    JSONArray clouserDocFile = resultJsonObject.optJSONArray("closurefile");
                    if (clouserDocFile.length() > 0) {
                        final String[] arrayFilePath = new String[clouserDocFile.length()];
                        for (int j = 0; j < clouserDocFile.length(); j++) {
                            layoutInflater = (LayoutInflater) mActivity.getSystemService(LAYOUT_INFLATER_SERVICE);
                            View docViewLayout = layoutInflater.inflate(R.layout.download_layout, null);
                            TextView tv_file_name = (TextView) docViewLayout.findViewById(R.id.tv_file_name);
                            final ImageView iv_set_download = (ImageView) docViewLayout.findViewById(R.id.iv_set_download);

                            try {
                                iv_set_download.setTag(j);
                                arrayFilePath[j] = clouserDocFile.get(j).toString();
                                String fileName = clouserDocFile.get(j).toString().substring(clouserDocFile.get(j).toString().lastIndexOf('/') + 1);
                                tv_file_name.setText(fileName);
                                iv_set_download.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int position = Integer.valueOf((Integer) iv_set_download.getTag());
                                        String fileURL = arrayFilePath[position];
                                        String url = fileURL;
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        Uri u = Uri.parse(url);
                                        intent.setData(u);
                                        startActivity(intent);
                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ll_clouser_document.addView(docViewLayout);
                        }
                    } else {
                        tv_clouserfile_title.setVisibility(View.GONE);
                    }
                    clouserDetailsDialog.setCanceledOnTouchOutside(false);
                    clouserDetailsDialog.show();


                }


            }

            @Override
            public void onError() {

            }
        });


    }


    private void inflateViewData(String title, String value, String subValue, LinearLayout parent) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_inflate_data, null);
        TextView tv_data = (TextView) view.findViewById(R.id.tv_data);

        if (!Util.textIfNull(subValue).equalsIgnoreCase(""))
            tv_data.setText(Html.fromHtml("<big><b>" + Util.textIfNull(title) + "</b></big> <br>" + Util.textIfNull(value) + "<br><small>+ " + subValue + "</small>"), TextView.BufferType.SPANNABLE);
        else
            tv_data.setText(Html.fromHtml("<big><b>" + Util.textIfNull(title) + "</b></big> <br>" + Util.textIfNull(value)), TextView.BufferType.SPANNABLE);

        parent.addView(view);

    }

    private void inflateViewData(String title, CharSequence ch, LinearLayout parent) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_inflate_data, null);
        TextView tv_data = (TextView) view.findViewById(R.id.tv_data);
        if (ch != null)
            tv_data.setText(TextUtils.concat(Html.fromHtml("<big><b>" + Util.textIfNull(title) + "</b></big> <br>"), ch), TextView.BufferType.SPANNABLE);

        parent.addView(view);
    }

    //Method to set Data for fund distribution
    private void setDeliverableWiseDistribution() {
        try {
            JSONArray jsonArrayDeliverable = new JSONArray(_projectDetails.getDeliverables());
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

    //Method to set Data for fund distribution
    private void setQuarterWiseDistribution() {
        try {
            JSONArray jsonArrayQuarter = new JSONArray(_projectDetails.getFund_distribution());
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

                            if (_projectDetails.getView_fund_distribution().equalsIgnoreCase("old")) {
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

    private CharSequence statusColorCoding(String status, Spanned subVal) {
        CharSequence charSequence = null;
        Spannable spanna = new SpannableString(status);
        Spannable spanTwo = new SpannableString(subVal);
        if (!Util.textIfNull(status).equalsIgnoreCase("")) {

            switch (status) {
                case "Approved":
                    spanna.setSpan(new BackgroundColorSpan(Color.parseColor("#82af6f")), 0, status.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanTwo.setSpan(new BackgroundColorSpan(Color.parseColor("#82af6f")), 0, subVal.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    charSequence = TextUtils.concat(spanna, spanTwo);
                    break;
                case "Resend for changes":
                    spanna.setSpan(new BackgroundColorSpan(Color.parseColor("#fee188")), 0, status.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanTwo.setSpan(new BackgroundColorSpan(Color.parseColor("#fee188")), 0, subVal.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    charSequence = TextUtils.concat(spanna, spanTwo);
                    break;
                case "Modified":
                    spanna.setSpan(new BackgroundColorSpan(Color.parseColor("#9585bf")), 0, status.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanTwo.setSpan(new BackgroundColorSpan(Color.parseColor("#9585bf")), 0, subVal.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    charSequence = TextUtils.concat(spanna, spanTwo);
                    break;
                case "Rejected":
                    spanna.setSpan(new BackgroundColorSpan(Color.parseColor("#d15b47")), 0, status.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanTwo.setSpan(new BackgroundColorSpan(Color.parseColor("#d15b47")), 0, subVal.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    charSequence = TextUtils.concat(spanna, spanTwo);
                    break;
                default:
                    if (status.contains("Pending from"))
                        spanna.setSpan(new BackgroundColorSpan(Color.parseColor("#fe8d1e")), 0, status.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spanTwo.setSpan(new BackgroundColorSpan(Color.parseColor("#fe8d1e")), 0, subVal.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    charSequence = TextUtils.concat(spanna, spanTwo);
                    break;
            }

        }


        return charSequence;
    }


    private void setTrack() {
        try {
            JSONObject jsonObjTrack = new JSONObject(_projectDetails.getTrack());

            JSONArray jsonArrayProposalTrack = jsonObjTrack.getJSONArray("proposal_track");
            JSONArray jsonArrayDeviationTrack = jsonObjTrack.getJSONArray("deviation_track");
            JSONArray jsonArrayClouserTrack = jsonObjTrack.getJSONArray("clouser_track");
            JSONArray jsonArrayProjectTrack = jsonObjTrack.getJSONArray("project_track");


            if (jsonArrayProposalTrack != null && jsonArrayProposalTrack.length() > 0) {
                for (int i = 0; i < jsonArrayProposalTrack.length(); i++) {
                    View viewProposal = LayoutInflater.from(mActivity).inflate(R.layout.approval_track_proposal_view_layout, null);
                    final TextView tv_approval_status = (TextView) viewProposal.findViewById(R.id.tv_approval_status);
                    final TextView tv_user = (TextView) viewProposal.findViewById(R.id.tv_user);
                    final TextView tv_date_time = (TextView) viewProposal.findViewById(R.id.tv_date_time);
                    final TextView tv_remarks = (TextView) viewProposal.findViewById(R.id.tv_remarks);

                    if (jsonArrayProposalTrack.optJSONObject(i).optString("text").toString() != null) {
                        tv_approval_status.setText(jsonArrayProposalTrack.optJSONObject(i).optString("text"));
                        if (jsonArrayProposalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PREP"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#abbac3"));
                        else if (jsonArrayProposalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PEND"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                        else if (jsonArrayProposalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTH"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#82af6f"));
                        else if (jsonArrayProposalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTL"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#428bca"));
                        else if (jsonArrayProposalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("REJCT"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#de1028"));
                        else if (jsonArrayProposalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFY"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                        else if (jsonArrayProposalTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFYED")) {
                            tv_approval_status.setBackgroundColor(Color.parseColor("#fee188"));
                            tv_approval_status.setTextColor(Color.parseColor("#000000"));
                        }
                    } else {
                        tv_approval_status.setText("N/A");
                        tv_approval_status.setTextColor(Color.parseColor("#000000"));
                    }

                    tv_user.setText(jsonArrayProposalTrack.optJSONObject(i).optString("username"));
                    tv_date_time.setText(jsonArrayProposalTrack.optJSONObject(i).optString("date"));

                    if (jsonArrayProposalTrack.optJSONObject(i).optString("remarks").toString() == null)

                        tv_remarks.setText("");
                    else
                        tv_remarks.setText(jsonArrayProposalTrack.optJSONObject(i).optString("remarks"));


                    ll_proposal_track_view.addView(viewProposal);
                }


            } else {
                ll_proposal_track.setVisibility(View.GONE);
            }


            if (jsonArrayProjectTrack != null && jsonArrayProjectTrack.length() > 0) {
                for (int i = 0; i < jsonArrayProjectTrack.length(); i++) {
                    View viewProjectTrack = LayoutInflater.from(mActivity).inflate(R.layout.approval_track_proposal_view_layout, null);
                    final TextView tv_approval_status = (TextView) viewProjectTrack.findViewById(R.id.tv_approval_status);
                    final TextView tv_user = (TextView) viewProjectTrack.findViewById(R.id.tv_user);
                    final TextView tv_date_time = (TextView) viewProjectTrack.findViewById(R.id.tv_date_time);
                    final TextView tv_remarks = (TextView) viewProjectTrack.findViewById(R.id.tv_remarks);

                    if (!jsonArrayProjectTrack.optJSONObject(i).optString("text").toString().equalsIgnoreCase("")) {
                        tv_approval_status.setText(jsonArrayProjectTrack.optJSONObject(i).optString("text"));
                        if (jsonArrayProjectTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PREP"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#abbac3"));
                        else if (jsonArrayProjectTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PEND"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                        else if (jsonArrayProjectTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTH"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#82af6f"));
                        else if (jsonArrayProjectTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTL"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#428bca"));
                        else if (jsonArrayProjectTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("REJCT"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#de1028"));
                        else if (jsonArrayProjectTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFY"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                        else if (jsonArrayProjectTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFYED")) {
                            tv_approval_status.setBackgroundColor(Color.parseColor("#fee188"));

                        }
                    } else {
                        tv_approval_status.setText("N/A");
                        tv_approval_status.setTextColor(Color.parseColor("#000000"));
                    }
                    tv_user.setText(jsonArrayProjectTrack.optJSONObject(i).optString("username"));
                    tv_date_time.setText(jsonArrayProjectTrack.optJSONObject(i).optString("date"));
                    if (jsonArrayProjectTrack.optJSONObject(i).optString("remarks").toString() == null)

                        tv_remarks.setText("");
                    else
                        tv_remarks.setText(jsonArrayProjectTrack.optJSONObject(i).optString("remarks"));

                    ll_project_track_view.addView(viewProjectTrack);
                }

            } else {
                ll_project_track.setVisibility(View.GONE);
            }

            if (jsonArrayDeviationTrack != null && jsonArrayDeviationTrack.length() > 0) {
                for (int i = 0; i < jsonArrayDeviationTrack.length(); i++) {
                    View viewDeviationTrack = LayoutInflater.from(mActivity).inflate(R.layout.approval_track_proposal_view_layout, null);
                    final TextView tv_approval_status = (TextView) viewDeviationTrack.findViewById(R.id.tv_approval_status);
                    final TextView tv_user = (TextView) viewDeviationTrack.findViewById(R.id.tv_user);
                    final TextView tv_date_time = (TextView) viewDeviationTrack.findViewById(R.id.tv_date_time);
                    final TextView tv_remarks = (TextView) viewDeviationTrack.findViewById(R.id.tv_remarks);

                    if (jsonArrayDeviationTrack.optJSONObject(i).optString("text").toString() != null) {
                        tv_approval_status.setText(jsonArrayDeviationTrack.optJSONObject(i).optString("text"));
                        if (jsonArrayDeviationTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PREP"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#abbac3"));
                        else if (jsonArrayDeviationTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PEND"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                        else if (jsonArrayDeviationTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTH"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#82af6f"));
                        else if (jsonArrayDeviationTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTL"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#428bca"));
                        else if (jsonArrayDeviationTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("REJCT"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#de1028"));
                        else if (jsonArrayDeviationTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFY"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                        else if (jsonArrayDeviationTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFYED")) {
                            tv_approval_status.setBackgroundColor(Color.parseColor("#fee188"));
                            tv_approval_status.setTextColor(Color.parseColor("#000000"));
                        }
                    } else {
                        tv_approval_status.setText("N/A");
                        tv_approval_status.setTextColor(Color.parseColor("#000000"));
                    }

                    tv_user.setText(jsonArrayDeviationTrack.optJSONObject(i).optString("username"));
                    tv_date_time.setText(jsonArrayDeviationTrack.optJSONObject(i).optString("date"));
                    if (jsonArrayDeviationTrack.optJSONObject(i).optString("remarks").toString() == null)

                        tv_remarks.setText("");
                    else
                        tv_remarks.setText(jsonArrayDeviationTrack.optJSONObject(i).optString("remarks"));
                    ll_devition_track_view.addView(viewDeviationTrack);
                }
            } else {
                ll_devition_track.setVisibility(View.GONE);
            }

            if (jsonArrayClouserTrack != null && jsonArrayClouserTrack.length() > 0) {
                for (int i = 0; i < jsonArrayClouserTrack.length(); i++) {
                    View viewClouserTrack = LayoutInflater.from(mActivity).inflate(R.layout.approval_track_proposal_view_layout, null);
                    final TextView tv_approval_status = (TextView) viewClouserTrack.findViewById(R.id.tv_approval_status);
                    final TextView tv_user = (TextView) viewClouserTrack.findViewById(R.id.tv_user);
                    final TextView tv_date_time = (TextView) viewClouserTrack.findViewById(R.id.tv_date_time);
                    final TextView tv_remarks = (TextView) viewClouserTrack.findViewById(R.id.tv_remarks);
                    if (jsonArrayClouserTrack.optJSONObject(i).optString("text").toString() != null) {
                        tv_approval_status.setText(jsonArrayClouserTrack.optJSONObject(i).optString("text"));
                        if (jsonArrayClouserTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PREP"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#abbac3"));
                        else if (jsonArrayClouserTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("PEND"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                        else if (jsonArrayClouserTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTH"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#82af6f"));
                        else if (jsonArrayClouserTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("ACEPTL"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#428bca"));
                        else if (jsonArrayClouserTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("REJCT"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#de1028"));
                        else if (jsonArrayClouserTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFY"))
                            tv_approval_status.setBackgroundColor(Color.parseColor("#f89406"));
                        else if (jsonArrayClouserTrack.optJSONObject(i).optString("type").toString().equalsIgnoreCase("MODFYED")) {
                            tv_approval_status.setBackgroundColor(Color.parseColor("#fee188"));
                            tv_approval_status.setTextColor(Color.parseColor("#000000"));
                        }
                    } else {
                        tv_approval_status.setText("N/A");
                        tv_approval_status.setTextColor(Color.parseColor("#000000"));
                    }

                    tv_user.setText(jsonArrayClouserTrack.optJSONObject(i).optString("username"));
                    tv_date_time.setText(jsonArrayClouserTrack.optJSONObject(i).optString("date"));

                    if (jsonArrayClouserTrack.optJSONObject(i).optString("remarks").toString() == null)

                        tv_remarks.setText("");
                    else
                        tv_remarks.setText(jsonArrayClouserTrack.optJSONObject(i).optString("remarks"));

                    ll_closure_track_view.addView(viewClouserTrack);
                }
            } else {
                ll_closure_track.setVisibility(View.GONE);
            }


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
        // search_text.setVisibility(View.VISIBLE);
        ll_notification.setVisibility(View.GONE);
        if (isPushType.equalsIgnoreCase("project-view")) {
            if (callBackListener != null) {
                callBackListener.onCallBack("isProjectPush");
            }

        }


    }
}

