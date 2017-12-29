package com.ciplafoundation.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciplafoundation.R;
import com.ciplafoundation.activities.FragmentBaseActivity;
import com.ciplafoundation.application.AppController;
import com.ciplafoundation.dropdown.DropDownViewForXML;
import com.ciplafoundation.interfaces.MultipartPostCallback;
import com.ciplafoundation.interfaces.OnActivitySelectListener;
import com.ciplafoundation.interfaces.OnFileChooseCallBack;
import com.ciplafoundation.model.FileDetails;
import com.ciplafoundation.model.ProjectDetails;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.model.UserDivision;
import com.ciplafoundation.services.VolleyTaskManager;
import com.ciplafoundation.utility.AlertDialogCallBack;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.ServerResponseCallback;
import com.ciplafoundation.utility.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by RUPANWITA BHATTACHARYA on 24-Jul-17.
 */

public class FragmentAddEvent extends Fragment implements ServerResponseCallback,OnFileChooseCallBack ,OnActivitySelectListener {

    private EditText et_involvedPerson,et_title,et_organisedOn,et_venue,et_organisedBy,et_objective,et_description;
    private ArrayList<FileDetails> fileList = new ArrayList<FileDetails>();

    private TextView tv_warning;
    private static FragmentBaseActivity mActivity;
    private static TextView tv_heading;
    private static LinearLayout ll_pending_approved,ll_image_distribution;
    private Activity activity;
    private Prefs prefs;
    private Button btn_upload,btn_save,btn_reset;
    private DatePickerDialog datePickerDialog;
    private VolleyTaskManager volleyTaskManager;
    private DropDownViewForXML dropDownProject,dropDown_district,dropDown_blockName,dropDown_villageName;
    private LinearLayout ll_village,ll_block;
    private boolean isProjectBasic=false,isBlock=false,isVillage=false,isEditDetails=false,isSaveData=false,isImage=false;
    private ArrayList<ProjectDetails> arrProject=new ArrayList<>();
    private ArrayList<UserDivision> arrDistrict=new ArrayList<>();
    private ArrayList<UserDivision> arrBlock=new ArrayList<>();
    private ArrayList<UserDivision> arrVillage=new ArrayList<>();
    private SimpleDateFormat dateFormatter;
    private boolean isEdit=false;
    private String eventId="",editBlockName="",editVillageName="",editBlockId="",editVillageId="";
    private FragmentTransaction fragmentTransaction;

    public static FragmentAddEvent newInstance(Activity activity,TextView _tv_heading, LinearLayout _ll_pending_approved) {
       // Log.d("test","Fragment Closed");
        FragmentAddEvent f = new FragmentAddEvent();

        Bundle b = new Bundle();

        f.setArguments(b);
        tv_heading=_tv_heading;
        ll_pending_approved=_ll_pending_approved;
        mActivity =(FragmentBaseActivity)activity;
        return f;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_event, container, false);
        eventId= getArguments().getString("eventId");
        initComponent(rootView);
        mActivity.setSuccessClickListener(this);
        return rootView;
    }

    private void initComponent(View view) {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        activity = getActivity();
        tv_warning = (TextView) view.findViewById(R.id.tv_warning);

        btn_upload = (Button) view.findViewById(R.id.btn_upload);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_reset = (Button) view.findViewById(R.id.btn_reset);


        dropDownProject = (DropDownViewForXML) view.findViewById(R.id.dropDownProject);
        dropDown_district = (DropDownViewForXML) view.findViewById(R.id.dropDown_district);
        dropDown_blockName = (DropDownViewForXML) view.findViewById(R.id.dropDown_blockName);
        dropDown_villageName = (DropDownViewForXML) view.findViewById(R.id.dropDown_villageName);

        dropDown_district .setTag("");
        dropDown_blockName.setTag("");
        dropDown_villageName.setTag("");
        dropDownProject.setTag("");

        ll_image_distribution = (LinearLayout) view.findViewById(R.id.ll_image_distribution);
        ll_block = (LinearLayout) view.findViewById(R.id.ll_block);
        ll_village = (LinearLayout) view.findViewById(R.id.ll_village);

        ll_village.setVisibility(View.GONE);
        ll_block.setVisibility(View.GONE);

        et_involvedPerson = (EditText) view.findViewById(R.id.et_involvedPerson);
        et_title = (EditText) view.findViewById(R.id.et_title);
        et_venue = (EditText) view.findViewById(R.id.et_venue);
        et_organisedBy = (EditText) view.findViewById(R.id.et_organisedBy);
        et_objective = (EditText) view.findViewById(R.id.et_objective);
        et_description = (EditText) view.findViewById(R.id.et_description);
        et_organisedOn = (EditText) view.findViewById(R.id.et_organisedOn);

        volleyTaskManager = new VolleyTaskManager(mActivity);
        ll_pending_approved.setVisibility(View.GONE);
        prefs = new Prefs(mActivity);


        if (Util.checkConnectivity(mActivity))
            basicDetailsWebServiceCalling();
        else
            Util.showMessageWithOk(mActivity, getString(R.string.no_internet));


        dropDown_district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dropDown_district.setTag(arrDistrict.get(position).getDivisionId());
                if (Util.checkConnectivity(mActivity))
                    blockWebServiceCalling(arrDistrict.get(position).getDivisionId());
                else
                    Util.showMessageWithOk(mActivity, getString(R.string.no_internet));

            }
        });

        dropDown_blockName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dropDown_blockName.setTag(arrBlock.get(position).getDivisionId());
                if (Util.checkConnectivity(mActivity))
                    villageWebServiceCalling(arrBlock.get(position).getDivisionId());
                else
                    Util.showMessageWithOk(mActivity, getString(R.string.no_internet));

            }
        });
        dropDownProject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dropDownProject.setTag(arrProject.get(position).getId());

            }
        });
        dropDown_villageName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dropDown_villageName.setTag(arrVillage.get(position).getDivisionId());

            }
        });

        btn_upload.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (fileList.size() < 3) {
                        mActivity.attachFile();
                        mActivity.setOnFileChoose(FragmentAddEvent.this);
                    } else

                        Toast.makeText(mActivity, "Maximum number of files has already been selected!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        datePickerDialog = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_organisedOn.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveFormClick();
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddFragment();
            }
        });

        mActivity.setOnActivitySelectListener(this);

        et_organisedOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });


        if (eventId != null && !eventId.equalsIgnoreCase("")) {
            editEventDetailsWebServiceCalling();
        }
        else {
            if (FragmentBaseActivity.level_id == "null" || FragmentBaseActivity.level_id.equalsIgnoreCase(""))
                tv_warning.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(JSONObject resultJsonObject) {
        //Log.d("Add Event","result=---"+resultJsonObject);
    if(isProjectBasic)
    {
        arrProject.clear();
        arrDistrict.clear();
        isProjectBasic=false;
        JSONArray jsonProjectArray=resultJsonObject.optJSONArray("project_list");
        JSONArray jsonDivisionArray=resultJsonObject.optJSONArray("district_list");
        if(jsonDivisionArray!=null && jsonDivisionArray.length()>0)
        {
            for(int i=0;i<jsonDivisionArray.length();i++)
            {
                UserDivision userDivision=new UserDivision();
                userDivision.setDivisionId(jsonDivisionArray.optJSONObject(i).optString("DIS_DISTRICT_ID"));
                userDivision.setName(jsonDivisionArray.optJSONObject(i).optString("DIS_DISTRICT_NAME"));
                arrDistrict.add(userDivision);
            }
            populateDistrictDropdown();

        }

        if(jsonProjectArray!=null && jsonProjectArray.length()>0)
        {
            for(int i=0;i<jsonProjectArray.length();i++)
            {
                ProjectDetails proDetails=new ProjectDetails();
                proDetails.setId(jsonProjectArray.optJSONObject(i).optString("PRS_PROJECT_ID"));
                proDetails.setProject_title(jsonProjectArray.optJSONObject(i).optString("PRS_TITLE"));
                arrProject.add(proDetails);


            }
            populateProjectDropdown();

        }
    }

    else if(isBlock)
    {
        isBlock=false;
        arrBlock.clear();
        JSONArray jsonBlockArray=resultJsonObject.optJSONArray("block_list");

        if(jsonBlockArray!=null && jsonBlockArray.length()>0)
        {
            for(int i=0;i<jsonBlockArray.length();i++)
            {
                UserDivision userDivision=new UserDivision();
                userDivision.setDivisionId(jsonBlockArray.optJSONObject(i).optString("BLK_BLOCK_ID"));
                userDivision.setName(jsonBlockArray.optJSONObject(i).optString("BLK_BLOCK_NAME"));
                arrBlock.add(userDivision);
            }
            populateBlockDropdown();

        }
        else {
            ll_block.setVisibility(View.GONE);
            ll_village.setVisibility(View.GONE);
        }
    }
    else if(isVillage)
    {
        isVillage=false;
        arrVillage.clear();
        JSONArray jsonBlockArray=resultJsonObject.optJSONArray("village_list");

        if(jsonBlockArray!=null && jsonBlockArray.length()>0)
        {
            for(int i=0;i<jsonBlockArray.length();i++)
            {
                UserDivision userDivision=new UserDivision();
                userDivision.setDivisionId(jsonBlockArray.optJSONObject(i).optString("VIL_VILLAGE_ID"));
                userDivision.setName(jsonBlockArray.optJSONObject(i).optString("VIL_VILLAGE_NAME"));
                arrVillage.add(userDivision);
            }
            populateVillageDropdown();

        }
        else
            ll_village.setVisibility(View.GONE);
    }

    else if(isEditDetails)
    {
        isEditDetails=false;
        dropDownProject.setText(Util.textIfNull(resultJsonObject.optString("project_title")));
        dropDownProject.setTag(Util.textIfNull(resultJsonObject.optString("project_id")));
        et_involvedPerson.setText(Util.textIfNull(resultJsonObject.optString("involved_person")));
        dropDown_district.setText(Util.textIfNull(resultJsonObject.optString("district_name")));
        dropDown_district.setTag(Util.textIfNull(resultJsonObject.optString("district_id")));
        editBlockName=Util.textIfNull(resultJsonObject.optString("block_name"));
        editBlockId=Util.textIfNull(resultJsonObject.optString("block_id"));
        editVillageName=Util.textIfNull(resultJsonObject.optString("village_name"));
        editVillageId=Util.textIfNull(resultJsonObject.optString("village_id"));
        et_title.setText(Util.textIfNull(resultJsonObject.optString("title")));
        et_organisedOn.setText(Util.textIfNull(resultJsonObject.optString("organized_on")));
        et_venue.setText(Util.textIfNull(resultJsonObject.optString("venu")));
        et_organisedBy.setText(Util.textIfNull(resultJsonObject.optString("organized_by")));
        et_objective.setText(Util.textIfNull(resultJsonObject.optString("objective")));
        et_description.setText(Util.textIfNull(resultJsonObject.optString("description")));


        if(!Util.textIfNull(resultJsonObject.optString("district_id")).equalsIgnoreCase(""))
            blockWebServiceCalling(resultJsonObject.optString("district_id"));
        if(!Util.textIfNull(resultJsonObject.optString("block_id")).equalsIgnoreCase(""))
            villageWebServiceCalling(resultJsonObject.optString("block_id"));

        if(resultJsonObject.optJSONArray("images_url")!=null && resultJsonObject.optJSONArray("images_url").length()>0)
        {
            fileList.clear();
            for(int i=0;i<resultJsonObject.optJSONArray("images_url").length();i++)
            {
                FileDetails fileDetails =new FileDetails();
                fileDetails.setFilePath(resultJsonObject.optJSONArray("images_url").optJSONObject(i).optString("url"));
                String[] file=resultJsonObject.optJSONArray("images_url").optJSONObject(i).optString("url").split("/");
                fileDetails.setFileName(file[file.length-1]);
                fileList.add(fileDetails);
            }
        }
        generateFileList(fileList);
    }
    else if(isSaveData)
    {
        String id = resultJsonObject.optString("id");
        String message = resultJsonObject.optString("message");

        if (!Util.textIfNull(resultJsonObject.optString("status")).equalsIgnoreCase("") && Util.textIfNull(resultJsonObject.optString("status")).equalsIgnoreCase("true")) {
            if (Util.ifListHasNewFile(fileList)) {

                if (!Util.textIfNull(id).equalsIgnoreCase("")) {
                    imageWebServiceCalling(id);
                } else
                    imageWebServiceCalling(eventId);

                   FragmentBaseActivity.level_id="";
            } else {
                Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                openAddFragment();
            }

        } else
            Toast.makeText(mActivity, "Error occurred while posting data", Toast.LENGTH_SHORT).show();
    }
    }

    @Override
    public void onError() {

    }


    /*****
     * Event Edit Details
     ******/
    public void editEventDetailsWebServiceCalling() {
        isEditDetails=true;
        UserClass user = Util.fetchUserClass(mActivity);
        String user_id = user.getUserId();
        String params = "USR_USER_ID=" + user_id + "&EVENT_ID=" + eventId;
        volleyTaskManager.doGetEditEventDetails(params, true);
    }

    /*****
     * proposalList
     ******/
    public void basicDetailsWebServiceCalling() {
        isProjectBasic=true;
        UserClass user = Util.fetchUserClass(mActivity);
        String user_id = user.getUserId();
        String division_id = prefs.getDivisionId();
        String params = "USR_USER_ID=" + user_id + "&DIVISION_ID=" + division_id;
        volleyTaskManager.doGetAddEventBasic(params, true);
    }



    private void populateProjectDropdown()
    {

        if (arrProject.size() > 0) {
            dropDownProject.setEnabled(true);
            String[] corridorArray = new String[arrProject.size()];

            for (int i = 0; i < arrProject.size(); i++) {
                corridorArray[i] = arrProject.get(i).getProject_title();
            }
            dropDownProject.setItems(corridorArray);
        } else {
            dropDownProject.setEnabled(false);
            dropDownProject.setText("");
        }
        dropDownProject.setText("Select");
        dropDownProject.setTag("");

    }

    private void populateDistrictDropdown()
    {

        if (arrDistrict.size() > 0) {
            dropDown_district.setEnabled(true);
            String[] corridorArray = new String[arrDistrict.size()];

            for (int i = 0; i < arrDistrict.size(); i++) {
                corridorArray[i] = arrDistrict.get(i).getName();
            }
            dropDown_district.setItems(corridorArray);
        } else {
            dropDown_district.setEnabled(false);
            dropDown_district.setText("");
        }
        dropDown_district.setText("Select");
        dropDown_district.setTag("");

    }

    //get block details
    public void blockWebServiceCalling(String id) {
        isBlock=true;
        String params = "district_id=" + id;
        volleyTaskManager.doGetBlock(params, true);
    }
    //set block details on dropdown
    private void populateBlockDropdown()
    {
        if (arrBlock.size() > 0) {
            dropDown_blockName.setEnabled(true);
            String[] corridorArray = new String[arrBlock.size()];

            for (int i = 0; i < arrBlock.size(); i++) {
                corridorArray[i] = arrBlock.get(i).getName();
            }
            dropDown_blockName.setItems(corridorArray);
            ll_block.setVisibility(View.VISIBLE);
        } else {
            dropDown_blockName.setEnabled(false);
            dropDown_blockName.setText("");
        }

        if(!editBlockName.equalsIgnoreCase(""))
            dropDown_blockName.setText(editBlockName);
        else
            dropDown_blockName.setText("Select");

        if(!editBlockId.equalsIgnoreCase(""))
            dropDown_blockName.setTag(editBlockId);
        else
            dropDown_blockName.setTag("");


    }

    //get village details
    public void villageWebServiceCalling(String id) {
        isVillage=true;
        String params ="block_id=" + id;
        volleyTaskManager.doGetVillage(params, true);
    }
    //set village details on dropdown
    private void populateVillageDropdown()
    {
        if (arrVillage.size() > 0) {
            dropDown_villageName.setEnabled(true);
            String[] corridorArray = new String[arrVillage.size()];

            for (int i = 0; i < arrVillage.size(); i++) {
                corridorArray[i] = arrVillage.get(i).getName();
            }
            dropDown_villageName.setItems(corridorArray);
            ll_village.setVisibility(View.VISIBLE);
        } else {
            dropDown_villageName.setEnabled(false);
            dropDown_villageName.setText("");
        }

        if(!editVillageName.equalsIgnoreCase(""))
            dropDown_villageName.setText(editVillageName);
        else
            dropDown_villageName.setText("Select");

        if(!editVillageId.equalsIgnoreCase(""))
            dropDown_villageName.setTag(editVillageId);
        else
            dropDown_villageName.setTag("");

    }
    //get image details
    public void imageWebServiceCalling(String id) {
        isImage=true;
        UserClass user = Util.fetchUserClass(mActivity);
        String user_id = user.getUserId();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("USR_USER_ID",user_id);
        map.put("EVENT_ID",id);
       volleyTaskManager.doSaveImage(mActivity,map,fileList,new MultipartPostCallback()
       {
           @Override
           public void onMultipartPost(String response) {
               {

                   String responseStatus = "";
                   //System.out.println("responseStatus--"+responseStatus);
                   try {
                       JSONObject requestJsonObject = new JSONObject(response);
                       responseStatus = requestJsonObject.optString("status");
                       if (responseStatus != null && responseStatus.trim().equalsIgnoreCase("true")) {
                           if(eventId!=null && !eventId.equalsIgnoreCase(""))
                               Toast.makeText(mActivity,"Event data updated successfully.",Toast.LENGTH_SHORT).show();
                           else
                              Toast.makeText(mActivity,"Event data posted successfully.",Toast.LENGTH_SHORT).show();

                           openAddFragment();

                       } else {
                           Toast.makeText(mActivity,"Error occurred while posting Data",Toast.LENGTH_SHORT).show();

                       }
                   } catch (Exception e) {
                       Toast.makeText(mActivity,"Error occurred while posting Data",Toast.LENGTH_SHORT).show();

                   }
               }
           }
       });
    }


    /**
     * Generating File List from Choose Files
     */
    @SuppressLint("InflateParams")
    private void generateFileList(final ArrayList<FileDetails> arrayFile) {

        ll_image_distribution.removeAllViews();

        if (arrayFile != null && arrayFile.size() > 0) {

            for (int i = 0; i < arrayFile.size(); i++)
            {
                View inflateView=LayoutInflater.from(mActivity).inflate(R.layout.view_image, null);
                //TextView tv_FileName=(TextView)inflateView.findViewById(R.id.tv_fileName);
                final ImageView iv_delete=(ImageView)inflateView.findViewById(R.id.iv_delete);
                final ImageView iv_imageSlide =(ImageView)inflateView.findViewById(R.id.iv_imageSlide);
                final int position=i;
                if(arrayFile.get(i).getFileName() != null)
                {
                    AppController.getInstance().displayUniversalImg(arrayFile.get(position).getFilePath(), iv_imageSlide, R.drawable.no_image);
                    iv_imageSlide.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            final Dialog imgDialog = new Dialog(mActivity);
                            imgDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            imgDialog.setContentView(R.layout.dialog_image_fullimage);

                            imgDialog.show();
                            ImageView iv_imageFile = (ImageView) imgDialog.findViewById(R.id.imageViewFull);
                            String fileType=arrayFile.get(position).getFileName().substring(arrayFile.get(position).getFileName().lastIndexOf(".") + 1);
                            //  Log.d("", "arrayFile.get(position).getFilePath()--"+arrayFile.get(position).getFilePath());

                            if(fileType.equalsIgnoreCase("jpg")||fileType.equalsIgnoreCase("png")){
                                AppController.getInstance().displayUniversalImg(arrayFile.get(position).getFilePath(), iv_imageFile, R.drawable.no_image);
                            }

                            else{

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arrayFile.get(position).getFilePath()));
                                //startActivity(browserIntent);
                                if(browserIntent.resolveActivity(mActivity.getPackageManager()) != null){
                                    mActivity.startActivity(browserIntent);
                                    if(imgDialog!=null && imgDialog.isShowing())
                                        imgDialog.dismiss();
                                }
                                else{
                                    Toast.makeText(mActivity, "Can't view the document.", Toast.LENGTH_LONG).show();
                                    if(imgDialog!=null && imgDialog.isShowing())
                                        imgDialog.dismiss();
                                }

                            }



                        }
                    });

                    //On long click
                    iv_imageSlide.setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {


                            if(!arrayFile.get(position).getFilePath().contains("http://") && arrayFile.size()>0)
                            {
                                iv_delete.setVisibility(View.VISIBLE);

                            }
                            return true;
                        }
                    });

                    iv_delete.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Util.showCallBackMessageWithOkCancelCustomButton(mActivity, "Do you want to delete?", "Ok", "Cancel", new AlertDialogCallBack() {

                                @Override
                                public void onSubmit() {
                                    if(fileList.size()>0)
                                    {
                                        fileList.remove(position);
                                        generateFileList(fileList);
                                    }
                                }

                                @Override
                                public void onCancel() {


                                }
                            });
                        }
                    });
                }
                ll_image_distribution.addView(inflateView);
            }
        }

        if(fileList.size()>=3)
            btn_upload.setVisibility(View.GONE);
        else
            btn_upload.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFileChoose(FileDetails fileDetails) {
        {
            if (fileDetails != null && fileList.size() < 3) {
                fileList.add(fileDetails);
            } else {

                Toast.makeText(mActivity,"File is not Selected",Toast.LENGTH_SHORT).show();
            }
            generateFileList(fileList);
        }
    }

    public void onSaveFormClick() {
        if(eventId==null &&( FragmentBaseActivity.level_id=="null" || FragmentBaseActivity.level_id.equalsIgnoreCase("")))
        {
            Toast.makeText(mActivity, "Please select an activity",Toast.LENGTH_SHORT).show();
        }
       else if (et_title.getText().toString().trim().isEmpty()) {

            Toast.makeText(mActivity, "Please enter the Title!",Toast.LENGTH_SHORT).show();
            et_title.requestFocus();

        }

        else if (et_organisedOn.getText().toString().trim().isEmpty()) {

            Toast.makeText(mActivity, "Please enter the date",Toast.LENGTH_SHORT).show();

        }
        else if (et_venue.getText().toString().trim().isEmpty()) {
            Toast.makeText(mActivity, "Please enter Venue!",Toast.LENGTH_SHORT).show();

            et_venue.requestFocus();

        }

        else if (et_organisedBy.getText().toString().trim().isEmpty()) {

            Toast.makeText(mActivity, "Please enter value for Organized By!",Toast.LENGTH_SHORT).show();
            et_organisedBy.requestFocus();

        }
        else if (et_objective.getText().toString().trim().isEmpty()) {

            Toast.makeText(mActivity, "Please enter Objective!",Toast.LENGTH_SHORT).show();
            et_objective.requestFocus();

        }
        else if (et_description.getText().toString().trim().isEmpty()) {

            Toast.makeText(mActivity, "Please enter Description!",Toast.LENGTH_SHORT).show();
            et_description.requestFocus();

        }

        else  {

            //Log.d("Save", "File list size: " + fileList.size());
            isSaveData=true;
            HashMap<String, String> map = new HashMap<String, String>();
            UserClass user = Util.fetchUserClass(mActivity);
            String user_id = user.getUserId();
            map.put("USR_USER_ID",user_id);
            map.put("DIVISION_ID", prefs.getDivisionId());
            map.put("EVT_PROJECT_ID",Util.textIfNull(dropDownProject.getTag().toString().trim()));
            map.put("EVT_DISTRICT_ID",Util.textIfNull(dropDown_district.getTag().toString().trim()));
            map.put("EVT_VILLAGE_ID",Util.textIfNull(dropDown_villageName.getTag().toString().trim()));
            map.put("EVT_BLOCK_ID", Util.textIfNull(dropDown_blockName.getTag().toString().trim()));
            map.put("EVT_TITLE", et_title.getText().toString().trim());
            map.put("EVT_DATE", et_organisedOn.getText().toString().trim());
            map.put("EVT_LOCATION", et_venue.getText().toString().trim());
            map.put("EVT_ORGANIZED_BY", et_organisedBy.getText().toString().trim());
            map.put("EVT_EXECUTIVE_ID", Util.textIfNull(et_involvedPerson.getText().toString().trim()));
            map.put("EVT_OBJECTIVE", et_objective.getText().toString().trim());
            map.put("EVT_DESCRIPTION", et_description.getText().toString().trim());
            map.put("EVT_LEVEL_ID", FragmentBaseActivity.level_id);

            if(eventId!=null && !eventId.equalsIgnoreCase(""))
            {
                map.put("EVT_ID", eventId);
                volleyTaskManager.doSaveEditedEvent(map,true);
            }
            else
              volleyTaskManager.doSaveEvent(map,true);

           /* String Query=Constant.tokentag+"="+mActivity.user.getTOKEN();
            MultipartPostRequest request = new MultipartPostRequest(getActivity(), Constant.baseURL + Constant.addEvent + projectStageId+"?"+Query,
                    map, fileList, "EVT_DOCS");
            request.mListener = this;
            request.execute();*/
        }

    }



    @Override
    public void onActivitySelect(String id) {

        if(!id.equalsIgnoreCase(""))
            tv_warning.setVisibility(View.GONE);
       // System.out.println("level Id"+id);
    }

    private void  openAddFragment()
    {
        FragmentAddEvent fragmentAddEvent = FragmentAddEvent.newInstance(mActivity, tv_heading, ll_pending_approved);
        mActivity.fragmentManager = mActivity.getSupportFragmentManager();
        fragmentTransaction = mActivity.fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ll_main_container, fragmentAddEvent);
        fragmentTransaction.commit();
    }
}
