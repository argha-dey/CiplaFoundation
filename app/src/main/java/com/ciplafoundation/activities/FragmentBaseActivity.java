package com.ciplafoundation.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.ciplafoundation.R;
import com.ciplafoundation.adapter.SlideHolder;
import com.ciplafoundation.dropdown.DropDownViewForXML;
import com.ciplafoundation.fragments.FragmentApproved;
import com.ciplafoundation.fragments.FragmentClosed;
import com.ciplafoundation.fragments.FragmentEvent;
import com.ciplafoundation.fragments.FragmentEventDetails;
import com.ciplafoundation.fragments.FragmentMadicalApprove;
import com.ciplafoundation.fragments.FragmentMadicalProposalApprovedDetails;
import com.ciplafoundation.fragments.FragmentMadicalReject;
import com.ciplafoundation.fragments.FragmentMedicalPending;
import com.ciplafoundation.fragments.FragmentPending;
import com.ciplafoundation.fragments.FragmentProjectDetails;
import com.ciplafoundation.fragments.FragmentProposalDetails;
import com.ciplafoundation.fragments.FragmentRejected;
import com.ciplafoundation.fragments.FragmentTreeChildDataView;
import com.ciplafoundation.interfaces.CallBackListener;
import com.ciplafoundation.interfaces.OnActivitySelectListener;
import com.ciplafoundation.model.AcceptedProposal;
import com.ciplafoundation.model.EventDetails;
import com.ciplafoundation.model.MedicalDetails;
import com.ciplafoundation.model.PendingProposal;
import com.ciplafoundation.model.ProjectDetails;
import com.ciplafoundation.model.ProposalDetails;
import com.ciplafoundation.model.TreeDataModel;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.model.UserList;
import com.ciplafoundation.nLevelTree.NLevelAdapter;
import com.ciplafoundation.nLevelTree.NLevelItem;
import com.ciplafoundation.nLevelTree.NLevelListItem;
import com.ciplafoundation.nLevelTree.NLevelView;
import com.ciplafoundation.services.VolleyTaskManager;
import com.ciplafoundation.utility.AlertDialogCallBack;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.ServerResponseCallback;
import com.ciplafoundation.utility.Util;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.ciplafoundation.R.id.ll_main_container;


public class FragmentBaseActivity extends BaseActivity implements View.OnClickListener, ServerResponseCallback, CallBackListener {
    private FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button btn_pending, btn_approved, btn_closed, btn_rejected;
    private ImageButton tree_view, iv_search_icon, iv_refresh_icon;
    private ImageView iv_notification_close;
    private TextView tv_heading, tv_to_date, tv_from_date, search_text, tv_notification_need_approve,tv_initiated_by;
    private DropDownViewForXML dropDown_search_option;
    private String toDate, fromDate, iniateUser, iniateUserName;
    private RelativeLayout rlActivityDataListHeader, rl_search_option, rl_headerTree, rl_notification;
    private LinearLayout ll_drawer_layout, ll_notification, ll_pending_approved, ll_from_date, ll_to_date, ll_search_button, ll_search_initiated_by;
    private RippleView rpv_closed, rpv_rejected;
    private ServerResponseCallback serverResponseCallback;
    private OnActivitySelectListener onActivitySelect;
    private String tag = "proposal";
    private FragmentPending fragmentPending = new FragmentPending();
    private FragmentApproved fragmentApproved = new FragmentApproved();
    private FragmentClosed fragmentClosed = new FragmentClosed();
    private FragmentRejected fragmentRejected = new FragmentRejected();
    private FragmentMadicalReject fragmentMadicalReject = new FragmentMadicalReject();
    private FragmentTreeChildDataView fragmentTreeChildDataView = new FragmentTreeChildDataView();
    private FragmentProposalDetails fragmentProposalDetails = new FragmentProposalDetails();
    private FragmentMadicalProposalApprovedDetails fragmentMadicalProposalDetails = new FragmentMadicalProposalApprovedDetails();
    private FragmentProjectDetails fragmentProjectDetails = new FragmentProjectDetails();
    private FragmentEventDetails fragmentEventDetails = new FragmentEventDetails();
    private FragmentEvent fragmentEvent = new FragmentEvent();
    private boolean isfragmentProposalPending = false, searchFlag = false, isfragmentProposalApproved = false, isPopulateDropdownReload = true, isfragmentProposalRejected = false, isinitiatedService = false,
            initiateByFlag = false, initiateByMedicalFlag = false, initiateByProjectFlag = false, isInitiatedByFillte = false, medicalList = false, isProposalList = false, isProposalDetails = false, isTreeList = false, isSearchTreeList = false, isProjectDetails = false, isEventList = false, isEventDetails = false;
    private Context mContext;
    private Prefs prefs;
    private ListView list_slidermenu;
    private AutoCompleteTextView ac_categories;
    private VolleyTaskManager volleyTaskManager;
    private ArrayList<AcceptedProposal> acceptedProposalList = new ArrayList<>();
    private ArrayList<PendingProposal> pendingProposalList = new ArrayList<>();
    private ArrayList<PendingProposal> rejectedProposalList = new ArrayList<>();
    private ArrayList<PendingProposal> rejectedProposalListMd = new ArrayList<>();

    private ArrayList<PendingProposal> closedProposalList = new ArrayList<>();
    private ArrayList<ProposalDetails> proposalDetailList = new ArrayList<>();
    private ArrayList<MedicalDetails> medicalDetailsList = new ArrayList<>();
    private ArrayList<EventDetails> eventList = new ArrayList<>();
    protected ArrayList<UserList> UserList = new ArrayList<UserList>();

    private NLevelAdapter nLevelAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<NLevelItem> nLevelList;
    private SlideHolder mSliderHolder;
    private ProgressDialog pDialog;

    public static String level_id = "";
    private TreeDataModel selectedTreeDataModel = null;
    private TreeDataModel selectedTreeActivity = null;
    private ArrayList<TreeDataModel> treeList = new ArrayList<TreeDataModel>(), searchList = new ArrayList<TreeDataModel>();
    private SimpleDateFormat dateFormatter;
    Calendar newCalendar = Calendar.getInstance();
    public static String deviationId=" ",projectPanId="", task = " ", roleId = "", divisionId = "", userId = "", Id = "", notification_type = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_pending_approved);
        task = getIntent().getStringExtra("task");
        initComponent();
        //initFragment();
        initNaviDrawer();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }

    private void intentDataGetFromPush() {
        Intent intent = getIntent();
        Bundle bundleData = intent.getExtras();
        if (bundleData != null) {
            roleId = (String) bundleData.get("roleId");
            divisionId = (String) bundleData.get("divisionId");
            prefs.setDivisionId(divisionId);
            userId = (String) bundleData.get("userId");
            Id = (String) bundleData.get("Id");
            deviationId=(String) bundleData.get("deviationId");
            projectPanId=(String) bundleData.get("projectPanId");
            notification_type = (String) bundleData.get("notification-type");
          //  pushNotification = (String) bundleData.get("PUSH");


            System.out.print("notification_type==>" + notification_type);

            if (notification_type.equalsIgnoreCase("proposal-view")) {
                volleyTaskManager = new VolleyTaskManager(mContext);
                String paramMap = "USR_USER_ID=" + userId + "&ID=" + Id + "&DIVISION_ID=" + divisionId + "&ROLE_ID=" + roleId;
                prefs.setIsProposalDetails(true);
                volleyTaskManager.doGetProposalDetails(paramMap, true);


            }


            if (notification_type.equalsIgnoreCase("project-view")) {
                volleyTaskManager = new VolleyTaskManager(mContext);
                String params = "USR_USER_ID=" + userId + "&ID=" + Id + "&DIVISION_ID=" + divisionId;
                prefs.setIsProjectDetails(true);
                volleyTaskManager.doGetProjectDetails(params, true);

            }


            if (notification_type.equalsIgnoreCase("project-deviation-view")) {

                volleyTaskManager = new VolleyTaskManager(mContext);
                String params = "USR_USER_ID=" + userId + "&ID=" + Id + "&DIVISION_ID=" + divisionId;
                prefs.setIsProjectDetails(true);
                volleyTaskManager.doGetProjectDetails(params, true);


            }

            if (notification_type.equalsIgnoreCase("project-pan-view")) {

                volleyTaskManager = new VolleyTaskManager(mContext);
                String params = "USR_USER_ID=" + userId + "&ID=" + Id + "&DIVISION_ID=" + divisionId;
                prefs.setIsProjectDetails(true);
                volleyTaskManager.doGetProjectDetails(params, true);


            }

            if (notification_type.equalsIgnoreCase("event-view")) {



            }
        }


    }


    private void initComponent() {

        mContext = FragmentBaseActivity.this;
        prefs = new Prefs(mContext);
        volleyTaskManager = new VolleyTaskManager(mContext);


        dropDown_search_option = (DropDownViewForXML) findViewById(R.id.dropDown_search_option);

        dropDown_search_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                iniateUser = UserList.get(position).getUser_id();
                iniateUserName = UserList.get(position).getUser_name();
                dropDown_search_option.setTag("");
            }
        });
        rl_search_option = (RelativeLayout) findViewById(R.id.rl_search_option);
        tv_initiated_by=(TextView)findViewById(R.id.tv_initiated_by);
        rl_headerTree = (RelativeLayout) findViewById(R.id.rl_headerTree);
        ll_from_date = (LinearLayout) findViewById(R.id.ll_from_date);
        ll_from_date.setOnClickListener(this);
        iv_search_icon = (ImageButton) findViewById(R.id.iv_search_icon);
        iv_search_icon.setOnClickListener(this);
        ll_to_date = (LinearLayout) findViewById(R.id.ll_to_date);
        ll_to_date.setOnClickListener(this);
        // search_text = (TextView) findViewById(R.id.search_text);
        tv_notification_need_approve = (TextView) findViewById(R.id.tv_notification_need_approve);
        iv_notification_close = (ImageView) findViewById(R.id.iv_notification_close);
        tv_from_date = (TextView) findViewById(R.id.tv_from_date);
        tv_from_date.setOnClickListener(this);
        ll_notification = (LinearLayout) findViewById(R.id.ll_notification);
        dropDown_search_option = (DropDownViewForXML) findViewById(R.id.dropDown_search_option);
        dropDown_search_option.setOnClickListener(this);

        tv_to_date = (TextView) findViewById(R.id.tv_to_date);
        tv_to_date.setOnClickListener(this);

        ll_search_button = (LinearLayout) findViewById(R.id.ll_search_button);
        ll_search_button.setOnClickListener(this);
        ll_search_initiated_by = (LinearLayout) findViewById(R.id.ll_search_initiated_by);
        //ll_search_initiated_by.setVisibility(View.VISIBLE);

        ll_pending_approved = (LinearLayout) findViewById(R.id.ll_pending_approved);
        tv_heading = (TextView) findViewById(R.id.tv_heading);

        list_slidermenu = (ListView) findViewById(R.id.lv_slidermenu);

        btn_pending = (Button) findViewById(R.id.btn_pending);
        btn_pending.setOnClickListener(this);
        iv_refresh_icon = (ImageButton) findViewById(R.id.iv_refresh_icon);
        iv_refresh_icon.setOnClickListener(this);
        btn_approved = (Button) findViewById(R.id.btn_approved);
        btn_approved.setOnClickListener(this);

        btn_closed = (Button) findViewById(R.id.btn_closed);
        btn_closed.setOnClickListener(this);

        btn_rejected = (Button) findViewById(R.id.btn_rejected);
        btn_rejected.setOnClickListener(this);

        tree_view = (ImageButton) findViewById(R.id.menu_icon);

        rpv_closed = (RippleView) findViewById(R.id.rpv_closed);
        rpv_rejected = (RippleView) findViewById(R.id.rpv_rejected);
        tree_view.setOnClickListener(this);
        setViewShowHide(true, false, false);
        fragmentApproved = FragmentApproved.newInstance(tv_heading, ll_pending_approved);
        fragmentPending = FragmentPending.newInstance(tv_heading, ll_pending_approved);
        fragmentClosed = FragmentClosed.newInstance(tv_heading, ll_pending_approved);
        fragmentRejected = FragmentRejected.newInstance(tv_heading, ll_pending_approved);
        fragmentMadicalReject = FragmentMadicalReject.newInstance(tv_heading, ll_pending_approved);


        if (task != null && task.equalsIgnoreCase("project")) {
            rl_search_option.setVisibility(View.VISIBLE);
            tv_initiated_by.setVisibility(View.VISIBLE);
            ll_pending_approved.setWeightSum(3);
            rpv_closed.setVisibility(View.VISIBLE);
            rpv_rejected.setVisibility(View.GONE);
            tag = "project";
        } else if (task != null && task.equalsIgnoreCase("medical")) {
            rl_search_option.setVisibility(View.GONE);
            tv_initiated_by.setVisibility(View.GONE);
            ll_pending_approved.setWeightSum(3);
            rpv_closed.setVisibility(View.GONE);
            rpv_rejected.setVisibility(View.VISIBLE);
            tag = "medical";
        } else {
            rl_search_option.setVisibility(View.VISIBLE);
            tv_initiated_by.setVisibility(View.VISIBLE);
            ll_pending_approved.setWeightSum(3);
            rpv_closed.setVisibility(View.GONE);
            rpv_rejected.setVisibility(View.VISIBLE);
        }


        if (getIntent().getStringExtra("notification-type") != null) {
            intentDataGetFromPush();
        } else {

            ServiceCalling();
        }

    }

    private void ServiceCalling() {
        if (Util.checkConnectivity(mContext)) {
            if (task != null && task.equalsIgnoreCase("project")) {
                if (initiateByProjectFlag) {
                    initiateByProjectFlag = false;
                    InitiatedByFillterProjectWebService();
                } else {
                    projectListWebServiceCalling();
                }
            } else if (task != null && task.equalsIgnoreCase("event"))
                eventListWebServiceCalling();
            else if (task != null && task.equalsIgnoreCase("medical")) {
                if (initiateByMedicalFlag) {
                    initiateByMedicalFlag = false;
                    InitiatedByFillterMadicalWebService();
                } else {
                    medicalListWebServiceCalling();
                }
            } else {
                if (initiateByFlag) {
                    initiateByFlag = false;
                    InitiatedByFillterWebService();
                } else {
                    // searchFlag = true;
                    proposalListWebServiceCalling();
                }
            }
        } else
            //Util.showMessageWithOk(mContext,getString(R.string.no_internet));
            Util.showCallBackMessageWithOk(mContext, getString(R.string.no_internet), new AlertDialogCallBack() {
                @Override
                public void onSubmit() {
                    onBackPressed();
                }

                @Override
                public void onCancel() {

                }
            });

    }

    private void initNaviDrawer() {
        ll_drawer_layout = (LinearLayout) findViewById(R.id.ll_drawer_layout);
        mSliderHolder = (SlideHolder) findViewById(R.id.ll_slide);
        ac_categories = (AutoCompleteTextView) findViewById(R.id.ac_categories);
    }

    /*****
     * proposalList
     ******/

    public void proposalListWebServiceCalling() {
        isProposalList = true;

        UserClass user = Util.fetchUserClass(mContext);
        String user_id = user.getUserId();
        String division_id = prefs.getDivisionId();
        String params = "USR_USER_ID=" + user_id + "&DIVISION_ID=" + division_id + "&FILTER=false";
        volleyTaskManager.doGetProposalList(params, true);
    }

    public void proposalListWebServiceCallingWithParam(String user_id, String division_id) {
        isProposalList = true;

        UserClass user = Util.fetchUserClass(mContext);
        String params = "USR_USER_ID=" + user_id + "&DIVISION_ID=" + division_id;
        volleyTaskManager.doGetProposalList(params, true);
    }

    /*****
     * proposalList
     ******/
    public void projectListWebServiceCalling() {
        isProposalList = true;
        UserClass user = Util.fetchUserClass(mContext);
        String user_id = user.getUserId();
        String division_id = prefs.getDivisionId();
        // String params = "USR_USER_ID=" + user_id + "&DIVISION_ID=" + division_id;
        String params = "DIVISION_ID=" + division_id;
        volleyTaskManager.doGetProjectList(params, true);
    }


    public void medicalListWebServiceCalling() {
        isProposalList = true;
        UserClass user = Util.fetchUserClass(mContext);
        String user_id = user.getUserId();
        String division_id = prefs.getDivisionId();
        // String params = "USR_USER_ID=" + user_id + "&DIVISION_ID=" + division_id;

        //  String params = "DIVISION_ID=" + division_id;
        String params = "DIVISION_ID=" + "D0000011";
        volleyTaskManager.doGetMadicalList(params, true);
    }


  /*  public void projectListDetailsWebCalling() {
        volleyTaskManager = new VolleyTaskManager(mContext);
        isProposalList = true;
        UserClass user = Util.fetchUserClass(mContext);
        String user_id = user.getUserId();
        String division_id = prefs.getDivisionId();
        // String params = "USR_USER_ID=" + user_id + "&DIVISION_ID=" + division_id;
        String params = "DIVISION_ID=" + division_id;
        volleyTaskManager.doGetProjectList(params, true);
    }*/

    /*****
     * eventList
     ******/
    public void eventListWebServiceCalling() {
        isEventList = true;
        UserClass user = Util.fetchUserClass(mContext);
        String user_id = user.getUserId();
        String division_id = prefs.getDivisionId();
        String params = "USR_USER_ID=" + user_id + "&DIVISION_ID=" + division_id;
        volleyTaskManager.doGetEventList(params, true);
    }

    private void TreeListwebServiceCalling() {
        isTreeList = true;
        String division_id = prefs.getDivisionId();
        String params = "DIVISION_ID=" + division_id;
        volleyTaskManager.doGetTreeList(params, true);
    }

    private void SearchTreeListwebServiceCalling() {
        isSearchTreeList = true;
        String division_id = prefs.getDivisionId();
        String params = "DIVISION_ID=" + division_id;
        volleyTaskManager.doGetSearchTreeList(params, true);
    }


    private void showProgressDialog() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (!pDialog.isShowing())
                    pDialog.show();
            }
        });

    }

    private void hideProgressDialog() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (pDialog.isShowing()) {

                    pDialog.dismiss();
                }

            }
        });
    }

    private void initPendingProposalFragment() {
        isfragmentProposalRejected = false;
        isfragmentProposalApproved = false;
        isfragmentProposalPending = true;

        if (tag.equalsIgnoreCase("medical")) {
            replaceFragment(FragmentMedicalPending.newInstance(tv_heading, ll_pending_approved));
        } else {
            replaceFragment(FragmentPending.newInstance(tv_heading, ll_pending_approved));
        }
        if (mSliderHolder.close()) {
            mSliderHolder.toggle();
        }
        // dropDown_search_option.setText(iniateUserName);
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void initApprovedProposalFragment() {
        isfragmentProposalApproved = true;
        isfragmentProposalRejected = false;
        isfragmentProposalPending = false;
        if (tag.equalsIgnoreCase("medical")) {
            replaceFragment(FragmentMadicalApprove.newInstance(tv_heading, ll_pending_approved));
        } else {
            replaceFragment(FragmentApproved.newInstance(tv_heading, ll_pending_approved));
        }
        if (mSliderHolder.close()) {
            mSliderHolder.toggle();
        }
        // dropDown_search_option.setText(iniateUserName);
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void initRejectedProposalFragment() {
        isfragmentProposalRejected = true;
        isfragmentProposalApproved = false;
        isfragmentProposalPending = false;
        if (tag.equalsIgnoreCase("medical")) {
            replaceFragment(FragmentMadicalReject.newInstance(tv_heading, ll_pending_approved));
        } else {
            replaceFragment(FragmentRejected.newInstance(tv_heading, ll_pending_approved));
        }
        if (mSliderHolder.close()) {
            mSliderHolder.toggle();
        }
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void replaceFragment(Fragment fragment) {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(ll_main_container, fragment);
        fragmentTransaction.commit();
    }

    private void replaceDetailsFragment(Fragment fragment) {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(ll_main_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_pending:
                initPendingProposalFragment();
                break;
            case R.id.btn_approved:
                initApprovedProposalFragment();
                break;
            case R.id.btn_closed:
                replaceFragment(fragmentClosed);
                if (mSliderHolder.close()) {
                    mSliderHolder.toggle();
                }
                break;
            case R.id.btn_rejected:
                initRejectedProposalFragment();
                break;
            case R.id.menu_icon:
                navigationDrawer();
                mSliderHolder.toggle();
                break;
            case R.id.ll_search_button:
                Log.i("TASK=",task);
              if(task.equalsIgnoreCase("medical")) {
                  if (tv_from_date.getText().toString().length() == 0)
                      Util.showMessageWithOk(mContext, "Please fill the from date fields!");
                  else if (tv_to_date.getText().toString().length() == 0)
                      Util.showMessageWithOk(mContext, "Please fill the to date fields!");
                  else if (tv_from_date.getText().toString().equals(tv_to_date.getText().toString()))
                      Util.showMessageWithOk(mContext, "Don't chooses the same date!");
                  else if (task != null && task.equalsIgnoreCase("medical")) {
                      isPopulateDropdownReload = false;
                      initiateByMedicalFlag = true;
                      ServiceCalling();

                  }

              }
              else {

                  if (dropDown_search_option.getText().toString().length() == 0 || dropDown_search_option.getText().toString().equals("Select"))
                      Util.showMessageWithOk(mContext, "Please Select an Option from dropDown fields!");
                  else if (tv_from_date.getText().toString().length() == 0)
                      Util.showMessageWithOk(mContext, "Please fill the from date fields!");
                  else if (tv_to_date.getText().toString().length() == 0)
                      Util.showMessageWithOk(mContext, "Please fill the to date fields!");
                  else if (tv_from_date.getText().toString().equals(tv_to_date.getText().toString()))
                      Util.showMessageWithOk(mContext, "Don't chooses the same date!");

                  else {
                      if (task != null && task.equalsIgnoreCase("project")) {
                          isPopulateDropdownReload = false;
                          initiateByProjectFlag = true;
                          ServiceCalling();
                      }  else {
                          isPopulateDropdownReload = false;
                          initiateByFlag = true;
                          ServiceCalling();
                      }
                  }
              }

                break;
            case R.id.iv_search_icon:
                if (ll_search_initiated_by.isShown()) {
                    Util.collapse(ll_search_initiated_by);
                } else {
                    dropDown_search_option.setText("");
                    tv_to_date.setText("");
                    tv_from_date.setText("");
                    Util.expand(ll_search_initiated_by);
                }

                break;

            case R.id.iv_refresh_icon:
                ServiceCalling();
                break;

            case R.id.tv_to_date:
                Calendar mcurrentTimeTo = Calendar.getInstance();
                int day = mcurrentTimeTo.get(Calendar.DAY_OF_MONTH);
                int month = mcurrentTimeTo.get(Calendar.MONTH);
                int year = mcurrentTimeTo.get(Calendar.YEAR);
                DatePickerDialog mTimePicker;

                mTimePicker = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        monthOfYear += 1;
                        tv_to_date.setText(dayOfMonth + "-" + monthOfYear + "-" + selectedYear);
                        toDate = tv_to_date.getText().toString();
                    }
                }, year, month, day);
                mTimePicker.setTitle("Select To Date:");
                mTimePicker.show();

                break;

            case R.id.tv_from_date:
                Calendar mcurrentTime = Calendar.getInstance();
                int dayfrom = mcurrentTime.get(Calendar.DAY_OF_MONTH);
                int monthfrom = mcurrentTime.get(Calendar.MONTH);
                int yearfrom = mcurrentTime.get(Calendar.YEAR);
                DatePickerDialog mTimePickerfrom;
                mTimePickerfrom = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        monthOfYear += 1;
                        tv_from_date.setText(dayOfMonth + "-" + monthOfYear + "-" + selectedYear);
                        fromDate = tv_from_date.getText().toString();
                    }
                }, yearfrom, monthfrom, dayfrom);
                mTimePickerfrom.setTitle("Select From Date:");
                mTimePickerfrom.show();
                break;


            default:
                break;
        }
    }

    private void InitiatedByFillterWebService() {
        // String Params = "FILTER=true" + "&&" + "fromdate=" + "18-10-2017" + "&" + "todate=" + "25-10-2017" + "&" + "PRS_CRT_ID=" + "USR0000002";
        String Params = "FILTER=true" + "&&" + "fromdate=" + fromDate + "&" + "todate=" + toDate + "&" + "PRS_CRT_ID=" + iniateUser;
        isProposalList = true;
        volleyTaskManager.doGetInitiatedByFillte(Params, true);
    }


    private void InitiatedByFillterProjectWebService() {
        String division_id = prefs.getDivisionId();
        String Params = "USR_USER_ID=" + iniateUser + "&DIVISION_ID=" + division_id + "&fromdate=" + fromDate + "&todate=" + toDate;

        //  String Params="USR_USER_ID="+"USR0000002"+"&DIVISION_ID="+"D0000001"+"&fromdate="+"01-01-2015"+"&todate="+"30-11-2017" ;
        isProposalList = true;
        volleyTaskManager.doGetInitiatedByProjectFillte(Params, true);
    }


    private void InitiatedByFillterMadicalWebService() {
        String division_id = prefs.getDivisionId();
        String Params = "DIVISION_ID=" + division_id + "&fromdate=" + fromDate + "&todate=" + toDate;

       // String Params = "DIVISION_ID=" + "D0000011"+ "&fromdate=" + "2016-05-30" + "&todate=" + "2017-12-31";
        isProposalList = true;
        volleyTaskManager.doGetInitiatedByMedicalFillte(Params, true);
    }


    private void addingTree(NLevelItem nLevelItem,
                            final ArrayList<TreeDataModel> treeDataModel, final int marginLeft,
                            final int marginTop, final int marginBottom, final int style,
                            final String textColor, final int textSize) {
        final LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < treeDataModel.size(); i++) {

            NLevelItem parent = new NLevelItem(treeDataModel.get(i), nLevelItem, new NLevelView() {

                @Override
                public View getView(NLevelItem item) {
                    View view = inflater.inflate(R.layout.n_level_list_item, null);
                    RadioButton rb_plusMinus = (RadioButton) view.findViewById(R.id.rb_plusMinus);
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    llp.setMargins(marginLeft, marginTop, 0, marginBottom);
                    rb_plusMinus.setLayoutParams(llp);
                    rb_plusMinus.setTextColor(Color.parseColor(textColor));
                    rb_plusMinus.setTypeface(null, style);
                    rb_plusMinus.setText(item.getTreeDataModel().getLevelDesc());
                    rb_plusMinus.setTextSize(textSize);

                    if (item.getTreeDataModel().getIsSearched()) {
                        //rb_plusMinus.setBackgroundColor(Color.parseColor("#33B5E5"));
                        rb_plusMinus.setTextColor(Color.parseColor("#33B5E5"));
                    } /*else if (item.getTreeDataModel().getIsClicked()) {
                        //rb_plusMinus.setBackgroundColor(Color.parseColor("#EBEBEC"));
                        rb_plusMinus.setTextColor(Color.parseColor("#EBEBEC"));
                    } */ else {
                        rb_plusMinus
                                .setBackgroundColor(Color.TRANSPARENT);
                    }

                    if (item.getTreeDataModel().getChildDataEntity().size() > 0) {
                        rb_plusMinus.setChecked(item.isExpanded());
                    } else {
                        rb_plusMinus.setButtonDrawable(R.drawable.transparent_box);
                        //  rb_plusMinus.setButtonDrawable(R.drawable.bar);
                    }

                    view.setTag(item.getTreeDataModel().getLevelId());

                    return view;
                }
            });


            nLevelList.add(parent);
            if (treeDataModel.get(i).getChildDataEntity().size() > 0) {
                addingTree(parent, treeDataModel.get(i).getChildDataEntity(),
                        (marginLeft + 37), 0, 0, Typeface.NORMAL, "#FFFFFF", 15);
                list_slidermenu.setDivider(null);
                list_slidermenu.setDividerHeight(0);
            } else {
                list_slidermenu.setDivider(getResources().getDrawable(R.color.white));
                list_slidermenu.setDividerHeight(0);
            }
        }
    }


    protected void populateUserListDropdown() {


        if (UserList.size() > 0) {
            dropDown_search_option.setEnabled(true);
            String[] corridorArray = new String[UserList.size()];

            for (int i = 0; i < UserList.size(); i++) {
                corridorArray[i] = UserList.get(i).getUser_name();
            }
            dropDown_search_option.setItems(corridorArray);
        }
    }


    @Override
    public void onSuccess(JSONObject resultJsonObject) {
        Log.i("JSON Response:", resultJsonObject.toString());


        if (isProposalList) {
            isProposalList = false;
            if (resultJsonObject.optBoolean("status") == true) {
             /*   searchFlag = true;
            }
            if (searchFlag) {
                if (resultJsonObject.optBoolean("status") == true) {*/


                JSONArray rejectedJsonArray = resultJsonObject.optJSONArray("regected_proposal");
                if (rejectedJsonArray != null && rejectedJsonArray.length() > 0) {
                    Log.v("Response =", resultJsonObject.toString());
                    rejectedProposalList.clear();
                    for (int i = 0; i < rejectedJsonArray.length(); i++) {
                        JSONObject detailsJsonObject = rejectedJsonArray.optJSONObject(i);


                        PendingProposal pendingProposal = new PendingProposal();
                        pendingProposal.setId(detailsJsonObject.optString("id"));
                        pendingProposal.setTitle(detailsJsonObject.optString("title"));
                        pendingProposal.setTime_line_date(detailsJsonObject.optString("time_line_date"));
                        pendingProposal.setTime_line_duration(detailsJsonObject.optString("time_line_duration"));
                        pendingProposal.setNgo_type(detailsJsonObject.optString("ngo_type"));
                        pendingProposal.setNgo(detailsJsonObject.optString("ngo"));
                        pendingProposal.setBudget(detailsJsonObject.optString("budget"));
                        pendingProposal.setCreated_by(detailsJsonObject.optString("created_by"));
                        pendingProposal.setCreated_date(detailsJsonObject.optString("created_dt"));
                        rejectedProposalList.add(pendingProposal);
                    }
                    Util.saveRejectedProject(mContext, rejectedProposalList);
                } else {
                    rejectedProposalList.clear();
                    Util.saveRejectedProject(mContext, rejectedProposalList);
                }


                JSONArray rejectedMadicalJsonArray = resultJsonObject.optJSONArray("regected_" + tag);
                if (rejectedMadicalJsonArray != null && rejectedMadicalJsonArray.length() > 0) {
                    Log.v("Response =", resultJsonObject.toString());
                    rejectedProposalList.clear();
                    for (int i = 0; i < rejectedMadicalJsonArray.length(); i++) {
                        JSONObject detailsJsonObject = rejectedMadicalJsonArray.optJSONObject(i);


                        PendingProposal pendingMd = new PendingProposal();
                        pendingMd.setId(detailsJsonObject.optString("id"));
                        pendingMd.setTitle(detailsJsonObject.optString("title"));
                        pendingMd.setTime_line_date(detailsJsonObject.optString("time_line_date"));
                        pendingMd.setTime_line_duration(detailsJsonObject.optString("time_line_duration"));
                        pendingMd.setNgo_type(detailsJsonObject.optString("ngo_type"));
                        pendingMd.setNgo(detailsJsonObject.optString("ngo"));
                        pendingMd.setBudget(detailsJsonObject.optString("budget"));
                        pendingMd.setCreated_by(detailsJsonObject.optString("created_by"));
                        pendingMd.setCreated_date(detailsJsonObject.optString("created_dt"));

                        pendingMd.setCost_treatment(detailsJsonObject.optString("cost_treatment"));
                        pendingMd.setFund_requested(detailsJsonObject.optString("fund_requested"));
                        pendingMd.setSanctioned_amount(detailsJsonObject.optString("sanctioned_amount"));

                        rejectedProposalList.add(pendingMd);
                    }
                    Util.saveRejectedProject(mContext, rejectedProposalList);
                } else {
                    rejectedProposalList.clear();
                    Util.saveRejectedProject(mContext, rejectedProposalList);
                }


                JSONArray accepetedJsonArray = resultJsonObject.optJSONArray("accepted_" + tag);
                if (accepetedJsonArray != null && accepetedJsonArray.length() > 0) {
                    acceptedProposalList.clear();
                    for (int i = 0; i < accepetedJsonArray.length(); i++) {
                        JSONObject detailsJsonObject = accepetedJsonArray.optJSONObject(i);

                        AcceptedProposal acceptedProposal = new AcceptedProposal();
                        acceptedProposal.setId(detailsJsonObject.optString("id"));
                        acceptedProposal.setProposal_no(detailsJsonObject.optString("proposal_no"));
                        acceptedProposal.setTime_line_date(detailsJsonObject.optString("time_line_date"));
                        acceptedProposal.setTime_line_duration(detailsJsonObject.optString("time_line_duration"));
                        acceptedProposal.setNgo_type(detailsJsonObject.optString("ngo_type"));
                        acceptedProposal.setNgo(detailsJsonObject.optString("ngo"));
                        acceptedProposal.setTitle(detailsJsonObject.optString("title"));
                        acceptedProposal.setBudget(detailsJsonObject.optString("budget"));
                        acceptedProposal.setCreated_by(detailsJsonObject.optString("created_by"));
                        acceptedProposal.setCreated_date(detailsJsonObject.optString("created_dt"));


                        acceptedProposal.setCost_treatment(detailsJsonObject.optString("cost_treatment"));
                        acceptedProposal.setFund_requested(detailsJsonObject.optString("fund_requested"));
                        acceptedProposal.setSanctioned_amount(detailsJsonObject.optString("sanctioned_amount"));

                        acceptedProposalList.add(acceptedProposal);
                    }
                    Util.saveAcceptedProposal(mContext, acceptedProposalList);
                } else {
                    acceptedProposalList.clear();
                    Util.saveAcceptedProposal(mContext, acceptedProposalList);
                }


                JSONArray pendingJsonArray = resultJsonObject.optJSONArray("pending_" + tag);
                if (pendingJsonArray != null && pendingJsonArray.length() > 0) {
                    Log.i("Response =", resultJsonObject.toString());
                    pendingProposalList.clear();
                    for (int i = 0; i < pendingJsonArray.length(); i++) {
                        JSONObject detailsJsonObject = pendingJsonArray.optJSONObject(i);

                        PendingProposal pendingProposal = new PendingProposal();
                        pendingProposal.setId(detailsJsonObject.optString("id"));
                        pendingProposal.setTitle(detailsJsonObject.optString("title"));
                        pendingProposal.setTime_line_date(detailsJsonObject.optString("time_line_date"));
                        pendingProposal.setTime_line_duration(detailsJsonObject.optString("time_line_duration"));
                        pendingProposal.setNgo_type(detailsJsonObject.optString("ngo_type"));
                        pendingProposal.setNgo(detailsJsonObject.optString("ngo"));
                        pendingProposal.setBudget(detailsJsonObject.optString("budget"));
                        pendingProposal.setCreated_by(detailsJsonObject.optString("created_by"));
                        pendingProposal.setCreated_date(detailsJsonObject.optString("created_dt"));

                        pendingProposal.setCost_treatment(detailsJsonObject.optString("cost_treatment"));
                        pendingProposal.setFund_requested(detailsJsonObject.optString("fund_requested"));
                        pendingProposal.setSanctioned_amount(detailsJsonObject.optString("sanctioned_amount"));

                        pendingProposalList.add(pendingProposal);
                    }
                    Util.savePendingProposal(mContext, pendingProposalList);

                } else {
                    pendingProposalList.clear();
                    Util.savePendingProposal(mContext, pendingProposalList);
                }


                // -----------------------
                JSONArray closedJsonArray = resultJsonObject.optJSONArray("closed_" + tag);
                if (closedJsonArray != null && closedJsonArray.length() > 0) {
                    closedProposalList.clear();
                    for (int i = 0; i < closedJsonArray.length(); i++) {
                        JSONObject detailsJsonObject = closedJsonArray.optJSONObject(i);

                        PendingProposal pendingProposal = new PendingProposal();
                        pendingProposal.setId(detailsJsonObject.optString("id"));
                        pendingProposal.setTitle(detailsJsonObject.optString("title"));
                        pendingProposal.setTime_line_date(detailsJsonObject.optString("time_line_date"));
                        pendingProposal.setTime_line_duration(detailsJsonObject.optString("time_line_duration"));
                        pendingProposal.setNgo_type(detailsJsonObject.optString("ngo_type"));
                        pendingProposal.setNgo(detailsJsonObject.optString("ngo"));
                        pendingProposal.setBudget(detailsJsonObject.optString("budget"));
                        pendingProposal.setCreated_by(detailsJsonObject.optString("created_by"));
                        pendingProposal.setCreated_date(detailsJsonObject.optString("created_dt"));

                        closedProposalList.add(pendingProposal);
                    }
                    Util.saveClosedProject(mContext, closedProposalList);
                } else {
                    closedProposalList.clear();
                    Util.saveClosedProject(mContext, closedProposalList);
                }

                if (isfragmentProposalApproved)
                    initApprovedProposalFragment();

                else if (isfragmentProposalRejected)
                    initRejectedProposalFragment();
                else
                    initPendingProposalFragment();
                // searchFlag = false;


            } else {
                acceptedProposalList.clear();
                pendingProposalList.clear();
                closedProposalList.clear();
                rejectedProposalListMd.clear();
                rejectedProposalList.clear();
                Util.saveAcceptedProposal(mContext, acceptedProposalList);
                Util.savePendingProposal(mContext, pendingProposalList);
                Util.saveClosedProject(mContext, closedProposalList);
                Util.saveRejectedProject(mContext, rejectedProposalList);
                // Util.saveRejectedProject(mContext, rejectedProposalListMd);

                Util.showCallBackMessageWithOk(mContext, "No Record Found !!!", new AlertDialogCallBack() {
                    @Override
                    public void onSubmit() {
                        dropDown_search_option.setText("");
                        tv_to_date.setText("");
                        tv_from_date.setText("");
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                initPendingProposalFragment();
            }
            TreeListwebServiceCalling();

            /* else {
                Util.showMessageWithOk(mContext, "No Record Found !!!");
            }*/
        } else if (isTreeList)

        {
            isTreeList = false;
            JSONArray eventJsonArray = resultJsonObject.optJSONArray("tree");

            treeList.clear();
            for (int i = 0; i < eventJsonArray.length(); i++) {

                TreeDataModel treeData = new TreeDataModel();
                JSONObject treeJsonObject = eventJsonArray
                        .optJSONObject(i);

                treeData.setLevelId(treeJsonObject.optString("level_id"));
                treeData.setParentId(treeJsonObject.optString("parent_id"));
                treeData.setLevelDesc(treeJsonObject.optString("level_desc"));
                treeData.setParentLevel(treeJsonObject.optString("parent_level"));
                treeData.setBreadCrumb(treeData.getLevelId() + ",");

                if (treeJsonObject.has("child")) {
                    JSONArray childTreeJsonArray = treeJsonObject.optJSONArray("child");
                    ArrayList<TreeDataModel> childTreeDataList = new ArrayList<TreeDataModel>();
                    childTreeDataList = recursivellyParse(childTreeJsonArray, treeData.getBreadCrumb());

                    treeData.setChildDataEntity(childTreeDataList);

                }

                treeList.add(treeData);

            }
            SearchTreeListwebServiceCalling();
            // navigationDrawer(); // Populating drawer list values


        } else if (isSearchTreeList)

        {
            isSearchTreeList = false;

            JSONArray searchJsonArray = resultJsonObject.optJSONArray("search_level_list");

            Log.v("JSON Response:", resultJsonObject.toString());

            for (int i = 0; i < searchJsonArray.length(); i++) {
                TreeDataModel searchData = new TreeDataModel();
                JSONObject searchJsonObject = searchJsonArray
                        .optJSONObject(i);

                searchData.setLevelId(searchJsonObject
                        .optString("LVL_LEVEL_ID"));
                searchData.setLevelDesc(searchJsonObject
                        .optString("LVL_LEVEL_DESC"));

                searchList.add(searchData);
            }

            if (isPopulateDropdownReload) {
                isPopulateDropdownReload = false;
                String division_id = prefs.getDivisionId();
                String params = "DIVISION_ID=" + division_id;
                isinitiatedService = true;
                volleyTaskManager.doGetInitiated(params, true, new ServerResponseCallback() {
                    @Override
                    public void onSuccess(JSONObject resultJsonObject) {
                        if (isinitiatedService) {
                            isinitiatedService = false;
                            UserList.clear();
                            JSONArray fDGetUserResultJsonArray = resultJsonObject.optJSONArray("userlist");

                            Log.v("JSON Response:", resultJsonObject.toString());
                            if (fDGetUserResultJsonArray != null) {
                                for (int i = 0; i < fDGetUserResultJsonArray.length(); i++) {
                                    JSONObject userJsonObject = fDGetUserResultJsonArray
                                            .optJSONObject(i);

                                    UserList userList = new UserList();
                                    userList.setUser_id(userJsonObject.optString("userId"));
                                    userList.setUser_name(userJsonObject.optString("userName"));
                                    UserList.add(userList);
                                }
                                populateUserListDropdown();

                            }


                        }

                    }

                    @Override
                    public void onError() {

                    }
                });
            }

        }
        else if (prefs.getIsMadicalDetails())

        {
            //  isProposalDetails = false;
            prefs.setIsMadicalDetails(false);
            JSONObject grantDetaillsJsonObject = resultJsonObject.optJSONObject("grant_detaills");

            if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {
                //Log.v("Proposal Details", "Success");
                medicalDetailsList.clear();
                MedicalDetails medicalDetails = new MedicalDetails();
                medicalDetails.setTitle(grantDetaillsJsonObject.optString("title"));
                medicalDetails.setCase_detailing(grantDetaillsJsonObject.optString("case_detailing"));
                medicalDetails.setTimeline(grantDetaillsJsonObject.optString("time_line_date"));
                medicalDetails.setTime_line_duration(grantDetaillsJsonObject.optString("time_line_duration"));
                medicalDetails.setReffered_by(grantDetaillsJsonObject.optString("reffered_by"));
                medicalDetails.setRefferal_details(grantDetaillsJsonObject.optString("refferal_details"));
                medicalDetails.setType(grantDetaillsJsonObject.optString("type"));
                medicalDetails.setAliment(grantDetaillsJsonObject.optString("ailment"));
                medicalDetails.setTretment(grantDetaillsJsonObject.optString("treatment"));
                medicalDetails.setName_of_hospital(grantDetaillsJsonObject.optString("name_of_hospital"));
                medicalDetails.setLocation(grantDetaillsJsonObject.optString("location"));
                medicalDetails.setType_of_hospital(grantDetaillsJsonObject.optString("type_of_hospital"));
                medicalDetails.setDate_of_receiving_of_request(grantDetaillsJsonObject.optString("date_of_receiving_of_request"));
                medicalDetails.setTotal_cost_of_treatment(grantDetaillsJsonObject.optString("total_cost_of_treatment"));
                medicalDetails.setTotal_fund_request(grantDetaillsJsonObject.optString("total_fund_requested"));
                medicalDetails.setAmount_to_be_sanction(grantDetaillsJsonObject.optString("amount_to_be_sanction"));
                medicalDetails.setFamily_contribution(grantDetaillsJsonObject.optString("family_contribution"));
                medicalDetails.setFamily_annual_income(grantDetaillsJsonObject.optString("family_annual_income"));
                medicalDetails.setAddress_of_the_patient(grantDetaillsJsonObject.optString("address_of_the_patient"));
                medicalDetails.setContact_mobile(grantDetaillsJsonObject.optString("contact_mobile"));
                medicalDetails.setContact_other(grantDetaillsJsonObject.optString("contact_other"));
                medicalDetails.setAny_deatail_to_be_mintained(grantDetaillsJsonObject.optString("any_deatail_to_be_mintained"));
                medicalDetails.setReceived_support_under_goverment_schemes(grantDetaillsJsonObject.optString("received_support_under_goverment_schemes"));
                medicalDetails.setReceived_from_other(grantDetaillsJsonObject.optString("received_from_other"));

                medicalDetails.setFamily_detaills(resultJsonObject.optJSONArray("family_detaills").toString());
                medicalDetails.setFinancial_medical_grant(resultJsonObject.optJSONArray("financial_medical_grant").toString());
                if (!resultJsonObject.optJSONArray("documents_attached").toString().isEmpty()) {
                    medicalDetails.setDocuments_attached(resultJsonObject.optJSONArray("documents_attached").toString());
                }



                if (resultJsonObject.has("pan")) {

                    JSONObject dataObject = resultJsonObject.optJSONObject("pan");

                    if (dataObject != null) {
                        medicalDetails.setPan_details(resultJsonObject.optJSONObject("pan").toString());

                    } else {
                        medicalDetails.setPan_details(resultJsonObject.optJSONArray("pan").toString());

                    }
                }




                medicalDetails.setApproval_sec_visible(resultJsonObject.optBoolean("approval_sec_visible"));
                medicalDetails.setDocuments(resultJsonObject.optJSONArray("documents").toString());
                medicalDetails.setApprovalTracker(resultJsonObject.optJSONArray("approvalTracker").toString());
                medicalDetails.setApprovalMessage(resultJsonObject.optString("approvalMessage"));


                if (notification_type.equalsIgnoreCase("proposal-view")) {
                    TreeListwebServiceCalling();

                }


                medicalDetailsList.add(medicalDetails);
                fragmentMadicalProposalDetails = FragmentMadicalProposalApprovedDetails.newInstance(notification_type, ll_search_initiated_by, ll_notification, iv_notification_close, tv_notification_need_approve, iv_refresh_icon, iv_search_icon, tv_heading, ll_pending_approved, medicalDetailsList);
                replaceDetailsFragment(fragmentMadicalProposalDetails);


            } else {
                Util.showMessageWithOk(mContext, "No Medical Details Found !!!");
            }


        } else if (prefs.getIsProposalDetails())

        {
            isProposalDetails = false;
            prefs.setIsProposalDetails(false);

            if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {
                //Log.v("Proposal Details", "Success");
                proposalDetailList.clear();
                ProposalDetails proposalDetails = new ProposalDetails();
                proposalDetails.setSchedule_vii(resultJsonObject.optString("schedule_vii"));
                proposalDetails.setNgo_type(resultJsonObject.optString("ngo_type"));
                proposalDetails.setNgo_vendor(resultJsonObject.optString("ngo_vendor"));
                proposalDetails.setDuration_funding(resultJsonObject.optString("duration_funding"));
                proposalDetails.setTitle(resultJsonObject.optString("title"));
                proposalDetails.setDetails(resultJsonObject.optString("details"));
                proposalDetails.setTimeline(resultJsonObject.optString("timeline"));
                proposalDetails.setDuration(resultJsonObject.optString("duration"));
                proposalDetails.setDuration_word(resultJsonObject.optString("duration_word"));
                proposalDetails.setFund_requested(resultJsonObject.optString("fund_requested"));
                proposalDetails.setDistrict(resultJsonObject.optString("district"));
                proposalDetails.setBlock(resultJsonObject.optString("block"));
                proposalDetails.setVillage(resultJsonObject.optString("village"));
                proposalDetails.setLocation(resultJsonObject.optString("location"));
                proposalDetails.setDistance(resultJsonObject.optString("distance"));
                proposalDetails.setProfile_benificiary(resultJsonObject.optString("profile_benificiary"));
                proposalDetails.setRationale_project(resultJsonObject.optString("rationale_project"));
                proposalDetails.setProposal_no(resultJsonObject.optString("proposal_no"));
                proposalDetails.setApprovalMessage(resultJsonObject.optString("approvalMessage"));
                proposalDetails.setView_fund_distribution(resultJsonObject.optString("view_fund_distribution"));
                proposalDetails.setQuarter_qise_distribution(resultJsonObject.optJSONArray("fund_distribution").toString());
                proposalDetails.setDeliverable_wise_distribution(resultJsonObject.optJSONArray("deliverable_distribution").toString());
                proposalDetails.setApproved_track(resultJsonObject.optJSONArray("approvalTracker").toString());
                proposalDetails.setDownload_track(resultJsonObject.optJSONArray("documents").toString());
                proposalDetails.setApproval_status(resultJsonObject.optBoolean("approval_sec_visible"));
                proposalDetails.setProposal_id(resultJsonObject.optString("proposal_id"));

                if (notification_type.equalsIgnoreCase("proposal-view")) {
                    TreeListwebServiceCalling();

                }


                proposalDetailList.add(proposalDetails);
                fragmentProposalDetails = FragmentProposalDetails.newInstance(notification_type, ll_search_initiated_by, ll_notification, iv_notification_close, tv_notification_need_approve, iv_refresh_icon, iv_search_icon, tv_heading, ll_pending_approved, proposalDetailList);
                replaceDetailsFragment(fragmentProposalDetails);


            } else {
                Util.showMessageWithOk(mContext, "No Proposal Details Found !!!");
            }


        } else if (prefs.getIsProjectDetails())

        {
            isProjectDetails = false;
            prefs.setIsProjectDetails(false);

            if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {
                //Log.v("Project Details", "Success");
                ProjectDetails projectDetails = new ProjectDetails();
                projectDetails.setProject_title(resultJsonObject.optString("project_title"));
                projectDetails.setBudget(resultJsonObject.optString("budget"));
                projectDetails.setDeviation_cost(resultJsonObject.optString("deviation_cost"));
                projectDetails.setReport_submission_interval(resultJsonObject.optString("report_submission_interval"));

                projectDetails.setTimeline(resultJsonObject.optString("timeline"));
                projectDetails.setDuration(resultJsonObject.optString("duration"));
                projectDetails.setDuration_word(resultJsonObject.optString("duration_word"));
                projectDetails.setDeviation_time(resultJsonObject.optString("deviation_time"));
                projectDetails.setDetails_brief(resultJsonObject.optString("details_brief"));
                projectDetails.setExpenditure(resultJsonObject.optString("expenditure"));
                projectDetails.setProject_no(resultJsonObject.optString("project_no"));
                projectDetails.setView_fund_distribution(resultJsonObject.optString("view_fund_distribution"));

                projectDetails.setDeliverables(resultJsonObject.optJSONArray("deliverables").toString());
                projectDetails.setMou(resultJsonObject.optJSONArray("mou").toString());
                projectDetails.setDeviation(resultJsonObject.optJSONArray("deviation").toString());
                projectDetails.setPan(resultJsonObject.optJSONArray("pan").toString());
                projectDetails.setMonitering(resultJsonObject.optJSONArray("monitering").toString());
                projectDetails.setClosure(resultJsonObject.optJSONArray("closure").toString());
                projectDetails.setFund_distribution(resultJsonObject.optJSONArray("fund_distribution").toString());
                projectDetails.setTrack(resultJsonObject.optString("track").toString());
                projectDetails.setDocuments(resultJsonObject.optString("alldocuments").toString());
                projectDetails.setApprovalMessageVisibility(resultJsonObject.optBoolean("project_approval_visible"));
                projectDetails.setApprovalMessage(resultJsonObject.optString("project_approval_sec_note"));

                if (notification_type.equalsIgnoreCase("project-view")) {
                    TreeListwebServiceCalling();

                }


                fragmentProjectDetails = FragmentProjectDetails.newInstance(notification_type,projectPanId,deviationId, ll_search_initiated_by, ll_notification, iv_notification_close, tv_notification_need_approve, iv_refresh_icon, iv_search_icon, tv_heading, ll_pending_approved, projectDetails);
                replaceDetailsFragment(fragmentProjectDetails);

            } else {
                Util.showMessageWithOk(mContext, "No Project Details Found !!!");
            }
        } else if (isEventList)

        {
            isEventList = false;
            if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {
                //Log.v("Project Details", "Success");
                JSONArray eventJsonArray = resultJsonObject.optJSONArray("event_lists");
                if (eventJsonArray != null && eventJsonArray.length() > 0) {
                    eventList.clear();
                    for (int i = 0; i < eventJsonArray.length(); i++) {
                        JSONObject eventJsonObject = eventJsonArray.optJSONObject(i);
                        EventDetails eventDetails = new EventDetails();
                        eventDetails.setTitle(Util.textIfNull(eventJsonObject.optString("title")));
                        eventDetails.setDate(Util.textIfNull(eventJsonObject.optString("date")));
                        eventDetails.setId(Util.textIfNull(eventJsonObject.optString("id")));
                        eventDetails.setDivision(Util.textIfNull(eventJsonObject.optString("division")));
                        eventDetails.setDescription(Util.textIfNull(eventJsonObject.optString("description")));
                        eventDetails.setEventSchedule(Util.textIfNull(eventJsonObject.optString("eventschedule")));
                        eventDetails.setEventOccation(Util.textIfNull(eventJsonObject.optString("eventoccation")));
                        eventDetails.setCreated_by(Util.textIfNull(eventJsonObject.optString("created_by")));
                        eventDetails.setEdit_enable(Util.textIfNull(eventJsonObject.optString("edit_enable")));
                        eventDetails.setImage_url(Util.textIfNull(eventJsonObject.optString("image_url")));

                        eventList.add(eventDetails);
                    }
                }
                fragmentEvent = FragmentEvent.newInstance(this, tv_heading, ll_pending_approved, eventList);
                replaceDetailsFragment(fragmentEvent);
                TreeListwebServiceCalling();
            } else {
                eventList.clear();
                Util.showMessageWithOk(mContext, "No Event List Found !!!");
            }
        } else if (prefs.getEventDetails())

        {
            isEventDetails = false;
            prefs.setIsEventDetails(false);

            if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {
                //Log.v("Project Details", "Success");
                EventDetails eventDetails = new EventDetails();
                eventDetails.setProject(resultJsonObject.optString("project"));
                eventDetails.setInvolved_person(resultJsonObject.optString("involved_person"));
                eventDetails.setDistrict_name(resultJsonObject.optString("district_name"));
                eventDetails.setBlock_name(resultJsonObject.optString("block_name"));

                eventDetails.setVillage_name(resultJsonObject.optString("village_name"));
                eventDetails.setTitle(resultJsonObject.optString("title"));
                eventDetails.setOrganized_on(resultJsonObject.optString("organized_on"));
                eventDetails.setVenu(resultJsonObject.optString("venu"));
                eventDetails.setOrganized_by(resultJsonObject.optString("organized_by"));
                eventDetails.setObjective(resultJsonObject.optString("objective"));
                eventDetails.setDescription(resultJsonObject.optString("description"));
                JSONArray imagesArray = resultJsonObject.optJSONArray("images_url");
                if (imagesArray != null && imagesArray.length() > 0) {
                    ArrayList<String> arrImage = new ArrayList<>();
                    for (int i = 0; i < imagesArray.length(); i++) {
                        arrImage.add(imagesArray.optJSONObject(i).optString("url"));

                    }
                    eventDetails.setImageUrls(arrImage);
                }
                fragmentEventDetails = FragmentEventDetails.newInstance(tv_heading, ll_pending_approved, eventDetails);
                replaceDetailsFragment(fragmentEventDetails);

            } else {
                Util.showMessageWithOk(mContext, "No Event Details Found !!!");
            }
        }
        //Log.v("JSON Response:", resultJsonObject.toString());
        if (serverResponseCallback != null)
            serverResponseCallback.onSuccess(resultJsonObject);
    }

    private ArrayList<TreeDataModel> recursivellyParse(JSONArray childTreeJsonArray, String
            breadCrumb) {

        ArrayList<TreeDataModel> childTreeDataList = new ArrayList<TreeDataModel>();

        for (int i = 0; i < childTreeJsonArray.length(); i++) {
            JSONObject childJsonObject = childTreeJsonArray.optJSONObject(i);
            TreeDataModel childTreeData = new TreeDataModel();

            childTreeData.setLevelId(childJsonObject.optString("level_id"));
            childTreeData.setParentId(childJsonObject.optString("parent_id"));
            childTreeData.setLevelDesc(childJsonObject.optString("level_desc").trim());
            childTreeData.setParentLevel(childJsonObject.optString("parent_level"));
            childTreeData.setBreadCrumb(breadCrumb + childTreeData.getLevelId()
                    + ",");

            if (childJsonObject.has("child")) {
                JSONArray subChildJsonArray = childJsonObject
                        .optJSONArray("child");
                ArrayList<TreeDataModel> subChildList = recursivellyParse(
                        subChildJsonArray, childTreeData.getBreadCrumb());

                childTreeData.setChildDataEntity(subChildList);

            }

            childTreeDataList.add(childTreeData);
        }

        return childTreeDataList;

    }

    @Override
    public void onError() {
        Util.showCallBackMessageWithOk(mContext, "Internet error occurred ", new AlertDialogCallBack() {
            @Override
            public void onSubmit() {
                finish();
            }

            @Override
            public void onCancel() {

            }
        });

    }


    private void navigationDrawer() {
        String searchArray[] = new String[searchList.size()];
        for (int i = 0; i < searchList.size(); i++) {
            searchArray[i] = searchList.get(i).getLevelDesc();
        }

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, searchArray);


        ac_categories.setAdapter(arrayAdapter);
        ac_categories.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {

            @Override
            public void onDismiss() {
                //Log.v("OnDismissListener", "OnDismissListener is called");
                Util.hideSoftKeyboard(FragmentBaseActivity.this,
                        ac_categories);
                Util util = new Util();

                if (util.checkConnectivity(mContext)) {
                    // System.out.println("Connected");
                    searchingItem(ac_categories.getText().toString());
                }
            }
        });
        //Log.v("treeList", treeList.size() + "");
        nLevelList = new ArrayList<NLevelItem>();
        addingTree(null, treeList, 5, 15, 15, Typeface.BOLD, "#ffffff", 17);

        nLevelAdapter = new NLevelAdapter(nLevelList);
        list_slidermenu.setAdapter(nLevelAdapter);
        list_slidermenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Log.v("position:", position + "");
                // Log.v("view id:", view.getTag().toString());
                // Log.v("nLevelAdapterCount()", nLevelAdapter.getCount() + "");

                if (Util.checkConnectivity(mContext)) {
                    //System.out.println("Connected");

                    ((NLevelAdapter) list_slidermenu.getAdapter())
                            .toggle(position);
                    ((NLevelAdapter) list_slidermenu.getAdapter()).getFilter()
                            .filter();

                    selectedItem(view.getTag().toString());


                } else {
                    Util.showErrorDialog(FragmentBaseActivity.this,
                            "Connection is not possible. Please check your internet connectivity..!");
                }
            }
        });
    }

    protected void selectedItem(String levelID) {
        for (int i = 0; i < nLevelList.size(); i++) {
            if (nLevelList.get(i).getTreeDataModel().getLevelId()
                    .equalsIgnoreCase(levelID)) {
                selectedTreeDataModel = nLevelList.get(i).getTreeDataModel();
                nLevelList.get(i).getTreeDataModel().setIsClicked(true);

            } else {
                nLevelList.get(i).getTreeDataModel().setIsClicked(false);
                nLevelList.get(i).getTreeDataModel().setIsSearched(false);
            }
        }


        tv_heading.setText(selectedTreeDataModel.getLevelDesc());

        tv_heading.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        level_id = selectedTreeDataModel.getLevelId();
        if (onActivitySelect != null)

            onActivitySelect.onActivitySelect(selectedTreeDataModel.getLevelId());
        else
            replaceFragment(fragmentTreeChildDataView);

        if (selectedTreeDataModel.getChildDataEntity().size() == 0) {

            mSliderHolder.toggle();

        }
        //Log.v("selectedTreeDataModel", selectedTreeDataModel.getLevelId());
        // Log.v("selectedTreeDataModel", selectedTreeDataModel.getLevelDesc());
    }

    protected void searchingItem(String searchString) {
        closingAllNodes();
        boolean searchFound = false;
        String breadCrumb = "";
        for (int i = 0; i < nLevelList.size(); i++) {
            if (nLevelList.get(i).getTreeDataModel().getLevelDesc()
                    .equalsIgnoreCase(searchString)) {
                searchFound = true;
                nLevelList.get(i).getTreeDataModel().setIsSearched(true);
                breadCrumb = nLevelList.get(i).getTreeDataModel()
                        .getBreadCrumb();

                selectedTreeDataModel = nLevelList.get(i).getTreeDataModel();

                //Log.v("breadCrumb:", breadCrumb);
            } else {
                nLevelList.get(i).getTreeDataModel().setIsClicked(false);
                nLevelList.get(i).getTreeDataModel().setIsSearched(false);
            }
        }

        if (searchFound) {
            //Log.e("selectedTreeDataModel", selectedTreeDataModel.getLevelId());
            //Log.e("selectedTreeDataModel", selectedTreeDataModel.getLevelDesc());

            String[] parts = breadCrumb.split(",");
            for (int i = 0; i < parts.length; i++) {
                openingTree(parts[i]);
            }

        } else {
            Toast.makeText(FragmentBaseActivity.this,
                    "No search result found!", Toast.LENGTH_LONG).show();
        }
    }

    private void openingTree(String eachNode) {

        for (int i = 0; i < nLevelAdapter.getCount(); i++) {
            if (nLevelAdapter.getItem(i).getView().getTag().toString()
                    .equalsIgnoreCase(eachNode)) {
                //Log.v("found at position:", i + "");

                nLevelAdapter.toggle(i);

                List<NLevelListItem> tempfiltered = new ArrayList<NLevelListItem>();
                OUTER:
                for (NLevelListItem item : nLevelList) {
                    // add expanded items and top level items
                    // if parent is null then its a top level item
                    if (item.getParent() == null) {
                        tempfiltered.add(item);
                    } else {
                        // go through each ancestor to make sure they are all
                        // expanded
                        NLevelListItem parent = item;
                        while ((parent = parent.getParent()) != null) {
                            if (!parent.isExpanded()) {
                                // one parent was not expanded
                                // skip the rest and continue the OUTER for loop
                                continue OUTER;
                            }
                        }
                        tempfiltered.add(item);
                    }
                }
                nLevelAdapter
                        .setFiltered((ArrayList<NLevelListItem>) tempfiltered);
                nLevelAdapter.notifyDataSetChanged();
                //Log.e("From Adapter.getCount()", nLevelAdapter.getCount() + "");
                break;
            }
        }

    }

    private void closingAllNodes() {
        for (int i = 0; i < nLevelAdapter.getCount(); i++) {
            nLevelAdapter.collapseNode(i);
        }
    }

    public void setSuccessClickListener(ServerResponseCallback serverResponseCallback) {
        this.serverResponseCallback = serverResponseCallback;
    }

    public void setOnActivitySelectListener(OnActivitySelectListener OnActivitySelectListener) {
        this.onActivitySelect = OnActivitySelectListener;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("FragmentBase Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    @Override
    public void onCallBack(String pushmsg) {

        if (pushmsg.equalsIgnoreCase("isProposalPush")) {
            proposalListWebServiceCalling();
        } else if (pushmsg.equalsIgnoreCase("isProjectPush")) {
            task = "project";
            ll_pending_approved.setWeightSum(3);
            rpv_closed.setVisibility(View.VISIBLE);
            rpv_rejected.setVisibility(View.GONE);
            projectListWebServiceCalling();

        }
    }
}

