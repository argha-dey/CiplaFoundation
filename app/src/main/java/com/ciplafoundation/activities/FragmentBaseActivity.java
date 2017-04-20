package com.ciplafoundation.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciplafoundation.R;
import com.ciplafoundation.adapter.SlideHolder;
import com.ciplafoundation.fragments.FragmentApproved;
import com.ciplafoundation.fragments.FragmentPending;
import com.ciplafoundation.fragments.FragmentProposalDetails;
import com.ciplafoundation.model.AcceptedProposal;
import com.ciplafoundation.model.PendingProposal;
import com.ciplafoundation.model.ProposalDetails;
import com.ciplafoundation.model.TreeDataModel;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.nLevelTree.NLevelAdapter;
import com.ciplafoundation.nLevelTree.NLevelItem;
import com.ciplafoundation.nLevelTree.NLevelListItem;
import com.ciplafoundation.nLevelTree.NLevelView;
import com.ciplafoundation.services.VolleyTaskManager;
import com.ciplafoundation.utility.AlertDialogCallBack;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.ServerResponseCallback;
import com.ciplafoundation.utility.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class FragmentBaseActivity extends BaseActivity implements View.OnClickListener,ServerResponseCallback {

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private Button btn_pending, btn_approved;
    private ImageButton tree_view;
    private TextView tv_heading;
    private RelativeLayout rlActivityDataListHeader;
    private LinearLayout ll_drawer_layout, ll_pending_approved;
    private FragmentPending fragmentPending = new FragmentPending();
    private FragmentApproved fragmentApproved = new FragmentApproved();
    private FragmentProposalDetails fragmentProposalDetails = new FragmentProposalDetails();
    private boolean isProposalList = false, isProposalDetails = false, isTreeList = false;
    private Context mContext;
    private Prefs prefs;
    private ListView list_slidermenu;
    private AutoCompleteTextView ac_categories;
    private VolleyTaskManager volleyTaskManager;
    private ArrayList<AcceptedProposal> acceptedProposalList = new ArrayList<>();
    private ArrayList<PendingProposal> pendingProposalList = new ArrayList<>();
    private ArrayList<ProposalDetails> proposalDetailList = new ArrayList<>();
    private NLevelAdapter nLevelAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<NLevelItem> nLevelList=new ArrayList<>();
    private SlideHolder mSliderHolder;
    private ProgressDialog pDialog;

    private TreeDataModel selectedTreeDataModel = null;
    private TreeDataModel selectedTreeActivity = null;
    private ArrayList<TreeDataModel> treeList = new ArrayList<TreeDataModel>(), searchList = new ArrayList<TreeDataModel>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_pending_approved);

        initComponent();
        //initFragment();
        initNaviDrawer();
    }

    private void initComponent() {
        mContext = FragmentBaseActivity.this;
        prefs = new Prefs(mContext);
        volleyTaskManager = new VolleyTaskManager(mContext);
        ll_pending_approved = (LinearLayout) findViewById(R.id.ll_pending_approved);
        tv_heading = (TextView) findViewById(R.id.tv_heading);
        btn_pending = (Button) findViewById(R.id.btn_pending);
        btn_pending.setOnClickListener(this);
        btn_approved = (Button) findViewById(R.id.btn_approved);
        btn_approved.setOnClickListener(this);
        tree_view = (ImageButton) findViewById(R.id.menu_icon);
        tree_view.setOnClickListener(this);
        setViewShowHide(true, false, false);
        fragmentApproved = FragmentApproved.newInstance(tv_heading, ll_pending_approved);
        fragmentPending = FragmentPending.newInstance(tv_heading, ll_pending_approved);
        if (Util.checkConnectivity(mContext)) {
            proposalListWebServiceCalling();
            TreeListwebServiceCalling();

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
        ac_categories=(AutoCompleteTextView)findViewById(R.id.ac_categories);
    }

    /*****
     * proposalList
     ******/

    public void proposalListWebServiceCalling() {
        isProposalList = true;
        UserClass user = Util.fetchUserClass(mContext);
        String user_id = user.getUserId();
        String division_id = prefs.getDivisionId();
        String params = "USR_USER_ID=" + user_id + "&DIVISION_ID=" + division_id;
        volleyTaskManager.doGetProposalList(params, false);
    }

    private void TreeListwebServiceCalling() {
        isTreeList = true;
        //UserClass user = Util.fetchUserClass(mContext);
        //  String user_id = user.getUserId();
        String division_id = prefs.getDivisionId();
        //String params = "USR_USER_ID=" + user_id;// + "&DIVISION_ID=" + division_id;
        String params = "DIVISION_ID=" + division_id;
        volleyTaskManager.doGetTreeList(params, false);
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

    private void initFragment() {
        replaceFragment(fragmentPending);
    }

    private void replaceFragment(Fragment fragment) {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ll_main_container, fragment);
        fragmentTransaction.commit();
    }

    private void replaceDetailsFragment(Fragment fragment) {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ll_main_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_pending:
                replaceFragment(fragmentPending);
                break;
            case R.id.btn_approved:
                replaceFragment(fragmentApproved);
                break;
            case R.id.menu_icon:
                addingTree(null, treeList, 5, 15, 15, Typeface.BOLD, "#333333");
               // navigationDrawer();
                mSliderHolder.toggle();

                //mSliderHolder.toggleImmediately();
                break;

            default:
                break;
        }
    }

    private void addingTree(NLevelItem nLevelItem,
                            final ArrayList<TreeDataModel> treeDataModel, final int marginLeft,
                            final int marginTop, final int marginBottom, final int style,
                            final String textColor) {
        final LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < treeDataModel.size(); i++) {

            NLevelItem parent = new NLevelItem(treeDataModel.get(i), nLevelItem, new NLevelView() {

                @Override
                public View getView(NLevelItem item) {
                    View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
                    RadioButton rb_plusMinus = (RadioButton) view.findViewById(R.id.rb_plusMinus);
                    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    llp.setMargins(marginLeft, marginTop, 0, marginBottom);
                    rb_plusMinus.setLayoutParams(llp);
                    rb_plusMinus.setTextColor(Color.parseColor(textColor));
                    rb_plusMinus.setTypeface(null, style);
                    rb_plusMinus.setText(item.getTreeDataModel().getLevelDesc());

                    if (item.getTreeDataModel().getIsSearched()) {
                        rb_plusMinus.setBackgroundColor(Color.parseColor("#33B5E5"));
                    } else if (item.getTreeDataModel().getIsClicked()) {
                        rb_plusMinus.setBackgroundColor(Color.parseColor("#EBEBEC"));
                    } else {
                        rb_plusMinus
                                .setBackgroundColor(Color.TRANSPARENT);
                    }

                    if (item.getTreeDataModel().getChildDataEntity().size() > 0) {
                        rb_plusMinus.setChecked(item.isExpanded());
                    } else {
                        rb_plusMinus.setButtonDrawable(R.drawable.transparent_box);
                    }

                    view.setTag(item.getTreeDataModel().getLevelId());

                    return view;
                }
            });


           nLevelList.add(parent);
            if (treeDataModel.get(i).getChildDataEntity().size() > 0) {
                addingTree(parent, treeDataModel.get(i).getChildDataEntity(),
                        (marginLeft + 37), 0, 0, Typeface.NORMAL, "#666666");
            } else {

            }
        }
    }

    @Override
    public void onSuccess(JSONObject resultJsonObject) {
        if (isProposalList) {
            isProposalList = false;
            if (resultJsonObject.optBoolean("status") == true) {
                JSONArray accepetedJsonArray = resultJsonObject.optJSONArray("accepted_proposal");
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

                        acceptedProposalList.add(acceptedProposal);
                    }
                    Util.saveAcceptedProposal(mContext, acceptedProposalList);
                } else {
                    acceptedProposalList.clear();
                    Util.saveAcceptedProposal(mContext, acceptedProposalList);
                }

                JSONArray pendingJsonArray = resultJsonObject.optJSONArray("pending_proposal");
                if (pendingJsonArray != null && pendingJsonArray.length() > 0) {
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

                        pendingProposalList.add(pendingProposal);
                    }
                    Util.savePendingProposal(mContext, pendingProposalList);
                } else {
                    pendingProposalList.clear();
                    Util.savePendingProposal(mContext, pendingProposalList);
                }

                initFragment();

            } else {
                acceptedProposalList.clear();
                pendingProposalList.clear();
                Util.saveAcceptedProposal(mContext, acceptedProposalList);
                Util.savePendingProposal(mContext, pendingProposalList);
                //Util.showMessageWithOk(mContext, "No Proposal List Found!!!");
                initFragment();
            }
        }

        if (prefs.getIsProjectDetails()) {
            isProposalDetails = false;
            prefs.setIsProjectDetails(false);

            if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {
                Log.v("Proposal Details", "Success");
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

                proposalDetailList.add(proposalDetails);
                fragmentProposalDetails = FragmentProposalDetails.newInstance(tv_heading, ll_pending_approved, proposalDetailList);
                replaceDetailsFragment(fragmentProposalDetails);


            } else {
                Util.showMessageWithOk(mContext, "No Proposal Details Found !!!");
            }


        }

        /*------------------------------*/


        // As server responded successfully, dismissing the
        // progress dialog
        // pDialog.dismiss();
        Log.v("JSON Response:", resultJsonObject.toString());
        // Parsing data from the response object



           /* JSONArray searchJsonArray = resultJsonObject.optJSONArray("search_level_list");

            for (int i = 0; i < searchJsonArray.length(); i++) {
                TreeDataModel searchData = new TreeDataModel();
                JSONObject searchJsonObject = searchJsonArray
                        .optJSONObject(i);

                searchData.setLevelId(searchJsonObject
                        .optString("LVL_LEVEL_ID"));
                searchData.setLevelDesc(searchJsonObject
                        .optString("LVL_LEVEL_DESC"));

                searchList.add(searchData);
            }*/
        if (isTreeList) {
            isTreeList=false;



            JSONArray eventJsonArray = resultJsonObject.optJSONArray("tree");

            for (int i= 0; i < eventJsonArray.length(); i++) {

                TreeDataModel treeData = new TreeDataModel();
                JSONObject treeJsonObject = eventJsonArray
                        .optJSONObject(i);

                treeData.setLevelId(treeJsonObject.optString("level_id"));
                treeData.setParentId(treeJsonObject.optString("parent_id"));
                treeData.setLevelDesc(treeJsonObject.optString("level_desc"));
                treeData.setParentLevel(treeJsonObject.optString("parent_level"));
                treeData.setBreadCrumb(treeData.getLevelId() + ",");

                if (treeJsonObject.has("child")) {
                    //  JSONArray childTreeJsonArray = treeJsonObject.getJSONArray("child");
                    JSONArray childTreeJsonArray = treeJsonObject.optJSONArray("child");
                    ArrayList<TreeDataModel> childTreeDataList = new ArrayList<TreeDataModel>();
                    childTreeDataList = recursivellyParse(childTreeJsonArray,treeData.getBreadCrumb());

                    treeData.setChildDataEntity(childTreeDataList);

                }

                treeList.add(treeData);

            }


        }
    }

        private ArrayList<TreeDataModel> recursivellyParse (JSONArray childTreeJsonArray, String
        breadCrumb){

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
        public void onError () {
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
 /*       String searchArray[] = new String[searchList.size()];
        for (int i = 0; i < searchList.size(); i++) {
            searchArray[i] = searchList.get(i).getLevelDesc();
        }

      arrayAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_item, searchArray);


        ac_categories.setAdapter(arrayAdapter);
        ac_categories.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {

            @Override
            public void onDismiss() {
                Log.v("OnDismissListener", "OnDismissListener is called");
                Util.hideSoftKeyboard(FragmentBaseActivity.this,
                        ac_categories);

                Util util = new Util();

                if (util.isConnectionPossible()) {
                    System.out.println("Connected");

                   // searchingItem(ac_categories.getText().toString());

                }

            }
        });

        Log.v("treeList", treeList.size() + "");*/

        nLevelList = new ArrayList<NLevelItem>();
     //   addingTree(null, treeList, 5, 15, 15, Typeface.BOLD, "#333333");

        nLevelAdapter = new NLevelAdapter(nLevelList);
        list_slidermenu.setAdapter(nLevelAdapter);
        list_slidermenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {


          @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.v("position:", position + "");
                Log.v("view id:", view.getTag().toString());
                Log.v("nLevelAdapterCount()", nLevelAdapter.getCount() + "");

              //  Util util = new Util(ActivityDataListActivity.this);

                if (Util.isConnectionPossible()) {
                    System.out.println("Connected");

                    ((NLevelAdapter) list_slidermenu.getAdapter())
                            .toggle(position);
                    ((NLevelAdapter) list_slidermenu.getAdapter()).getFilter()
                            .filter();

                    //selectedItem(view.getTag().toString());

                } else {
                    Util.showErrorDialog(FragmentBaseActivity.this,
                            "Connection is not possible. Please check your internet connectivity..!");
                }

            }
        });

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }




/*
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

        rlActivityDataListHeader.setVisibility(View.VISIBLE);

        txt_dexcription.setText(selectedTreeDataModel.getLevelDesc());
        getLevelListData(selectedTreeDataModel.getLevelId());


        Log.v("selectedTreeDataModel", selectedTreeDataModel.getLevelId());
        Log.v("selectedTreeDataModel", selectedTreeDataModel.getLevelDesc());
    }
*/




/*
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

                Log.v("breadCrumb:", breadCrumb);
            } else {
                nLevelList.get(i).getTreeDataModel().setIsClicked(false);
                nLevelList.get(i).getTreeDataModel().setIsSearched(false);
            }
        }

        if (searchFound) {
            Log.e("selectedTreeDataModel", selectedTreeDataModel.getLevelId());
            Log.e("selectedTreeDataModel", selectedTreeDataModel.getLevelDesc());

            String[] parts = breadCrumb.split(",");
            for (int i = 0; i < parts.length; i++) {
                openingTree(parts[i]);
            }

            rlActivityDataListHeader.setVisibility(View.VISIBLE);

            txt_dexcription.setText(selectedTreeDataModel.getLevelDesc());

            getLevelListData(selectedTreeDataModel.getLevelId());



        } else {
            Toast.makeText(FragmentBaseActivity.this,
                    "No search result found!", Toast.LENGTH_LONG).show();
        }
    }*/

    private void openingTree(String eachNode) {

        for (int i = 0; i < nLevelAdapter.getCount(); i++) {
            if (nLevelAdapter.getItem(i).getView().getTag().toString()
                    .equalsIgnoreCase(eachNode)) {
                Log.v("found at position:", i + "");

                nLevelAdapter.toggle(i);

                List<NLevelListItem> tempfiltered = new ArrayList<NLevelListItem>();
                OUTER: for (NLevelListItem item : nLevelList) {
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

                Log.e("From Adapter.getCount()", nLevelAdapter.getCount() + "");

                break;
            }
        }

    }


    private void closingAllNodes() {
        for (int i = 0; i < nLevelAdapter.getCount(); i++) {
            nLevelAdapter.collapseNode(i);
        }
    }




    }
