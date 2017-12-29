package com.ciplafoundation.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciplafoundation.R;
import com.ciplafoundation.interfaces.DropDownClickListener;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.model.UserRole;
import com.ciplafoundation.model.UserRoleClass;
import com.ciplafoundation.notification.Config;
import com.ciplafoundation.services.VolleyTaskManager;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.ServerResponseCallback;
import com.ciplafoundation.utility.Util;
import com.rey.material.widget.CheckBox;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//import android.widget.CheckBox;


public class LoginActivity extends AppCompatActivity implements
        ServerResponseCallback
        //,DropDownClickListener
{

    EditText et_username,et_password;
    RelativeLayout rl_username,rl_password,rl_role;
    private CheckBox chkbx_rememberMe;
    //private Spinner sp_role;
    private TextView tv_forgot_password;
    private Prefs prefs;
   // private DropDownViewForXML dropdown_userRole;
    private VolleyTaskManager volleyTaskManager;
    //protected DropDownClickListener DropDownClickListener;
    private Context context=LoginActivity.this;
    private boolean isLoginFirstService=false,isLoginSecondService=false,isUserRoleService=false;
    private ArrayList<UserRole> arrlistUserRole=new ArrayList<>();
    private UserClass user = new UserClass();
    private String userRoll_name="",userRole_id="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    /*******
     * initializing view
     ************/
    private void init() {
        prefs=new Prefs(LoginActivity.this);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
     //   dropdown_userRole = (DropDownViewForXML) findViewById(R.id.dropdown_role);
        //sp_role = (Spinner)findViewById(R.id.sp_role) ;
        rl_username=(RelativeLayout)findViewById(R.id.rl_username);
        rl_password=(RelativeLayout)findViewById(R.id.rl_password);
        rl_role=(RelativeLayout)findViewById(R.id.rl_role);
        chkbx_rememberMe=(CheckBox)findViewById(R.id.chkbx_rememberMe);
        tv_forgot_password= (TextView)findViewById(R.id.tv_forgot_password);
     /*   tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showMessageWithOk(context,"Password can be changed from Web!");
            }
        });*/

        if(Util.fetchUserClass(LoginActivity.this) != null) {
            user = Util.fetchUserClass(LoginActivity.this);
        }

        if(user.getIsRemember())
        {
            et_username.setText(user.getEmail());
            et_password.setText(user.getPassword());
         //   dropdown_userRole.setTag(user.getRoleId());
         //   dropdown_userRole.setText(user.getRole());
            userRole_id=user.getRoleId();
            chkbx_rememberMe.setChecked(user.getIsRemember());
        }

        volleyTaskManager = new VolleyTaskManager(LoginActivity.this);
        //setDropDownSelectListener(this);

        //dropdown_userRole.setTag("");


        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    rl_role.setBackgroundResource(R.drawable.username_bg_normal);
                    rl_username.setBackgroundResource(R.drawable.username_bg_hover);
                    et_username.setTextColor(getResources().getColor(R.color.logintext));
                } else {
                    rl_username.setBackgroundResource(R.drawable.username_bg_normal);
                    et_username.setTextColor(getResources().getColor(R.color.logintext));
                    /*if (Util.checkConnectivity(context))
                        UserRoleWebServiceCalling(et_username.getText().toString());
                    else
                        Util.showMessageWithOk(context, getString(R.string.no_internet));*/

                }
            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    rl_role.setBackgroundResource(R.drawable.username_bg_normal);
                    rl_password.setBackgroundResource(R.drawable.username_bg_hover);
                    et_password.setTextColor(getResources().getColor(R.color.logintext));
                } else {
                    rl_password.setBackgroundResource(R.drawable.username_bg_normal);
                    et_password.setTextColor(getResources().getColor(R.color.logintext));
                }
            }
        });


       /* dropdown_userRole.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                Log.v("Dropdown!!!!!!","Focus called!!!");
                if (hasFocus) {
                    rl_role.setBackgroundResource(R.drawable.username_bg_hover);
                    dropdown_userRole.setTextColor(getResources().getColor(R.color.logintext));
                } else {
                    rl_role.setBackgroundResource(R.drawable.username_bg_normal);
                    dropdown_userRole.setTextColor(getResources().getColor(R.color.logintext));
                }
            }
        });*/


        /*dropdown_userRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userRoll_name = arrlistUserRole.get(position).getRole_name();
                userRole_id = arrlistUserRole.get(position).getRole_id();
                dropdown_userRole.setTag(userRoll_name);
            }
        });

        dropdown_userRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdown_userRole.requestFocus();
                rl_username.setBackgroundResource(R.drawable.username_bg_normal);
                rl_password.setBackgroundResource(R.drawable.username_bg_normal);
                rl_role.setBackgroundResource(R.drawable.username_bg_hover);
                dropdown_userRole.setTextColor(getResources().getColor(R.color.logintext));
            }
        });*/
    }

    /**
     * Method to redirect to Sign in activity
     */
    public void onSignInClicked(View mView) {
        if (et_username.getText().toString().length() == 0 ||
                et_password.getText().toString().length() == 0) {
            Util.showMessageWithOk(context, "Please fill all the required fields!");
        }
       /* else if (dropdown_userRole.getText().toString().equalsIgnoreCase("Select")
                || dropdown_userRole.getText().toString().length() == 0) {
            Util.showMessageWithOk(context, "Please fill all the required fields!");
        }*/

        else {
            if (Util.checkConnectivity(context))
                loginWebserviceCalling();
            else {
                if (Util.fetchUserClass(LoginActivity.this) == null) {
                    Util.showMessageWithOk(LoginActivity.this, getString(R.string.no_internet));
                } else if(!Util.fetchUserClass(LoginActivity.this).getUserId().equals(et_username.getText().toString()) ||
                        !Util.fetchUserClass(LoginActivity.this).getPassword().equals(et_password.getText().toString())) {
                    Util.showMessageWithOk(LoginActivity.this, getString(R.string.credentials_missmatch));
                } else {
                    openMapActivity();
                }
            }
        }
    }


    /**
     * Method to redirect to ForgotPassword in activity
     */

public void onForgotPasswordInClicked(View forgotPasswordview){
    if(Util.checkConnectivity(context)){
        openForgotPasswordActivity();
    }

}



    /*********************
     * Method to call User Role WebService
     ************************/
    private void UserRoleWebServiceCalling(String user_email) {

        String paramsMap = "USR_EMAIL=" + user_email;
        isUserRoleService = true;
        volleyTaskManager.doGetUserRole(paramsMap, true);
    }

    /*********************
     * Method to call Login WebService
     ************************/
    private void loginWebserviceCalling() {
        if(Util.fetchUserClass(LoginActivity.this) != null) {
            user = Util.fetchUserClass(LoginActivity.this);
        }
        SharedPreferences pref = context.getSharedPreferences(Config.SHARED_PREF, 0);

        HashMap<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("USR_PASSWORD", et_password.getText().toString());
        paramsMap.put("devicetoken",pref.getString("TokenId", null));
        paramsMap.put("USR_EMAIL", et_username.getText().toString());
        paramsMap.put("deviceType", "A");

        Log.v("Login",""+paramsMap);
        isLoginFirstService = true;
        volleyTaskManager.doLoginFirst(paramsMap, true);
    }
    /*********************
     * Method to call Login WebService
     ************************/
    private void loginSecondWebserviceCalling(String roleId,String userEmail) {

        HashMap<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("USR_EMAIL", userEmail);
        paramsMap.put("USR_ROLE_ID", roleId);
      //  Log.v("Login",""+paramsMap);
        isLoginSecondService = true;
        volleyTaskManager.doLoginSecond(paramsMap, true);
    }
    @Override
    public void onSuccess(JSONObject resultJsonObject) {

        if(isLoginFirstService)
        {
            isLoginFirstService = false;
            if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {

                Dialog dialog=new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_role);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                LinearLayout ll_role=(LinearLayout)dialog.findViewById(R.id.ll_role);
                JSONArray jsonRole=resultJsonObject.optJSONArray("user_roles");
                if(jsonRole!=null && jsonRole.length()>0)
                {
                    for(int i=0;i<jsonRole.length();i++)
                    {
                        View view= LayoutInflater.from(LoginActivity.this).inflate(R.layout.view_btnrole,null);
                        final Button btnRole=(Button)view.findViewById(R.id.btn_role);
                        btnRole.setText(jsonRole.optJSONObject(i).optString("ROLE_SALUTE"));
                        btnRole.setTag(jsonRole.optJSONObject(i).optString("ROLE_ID"));

                        ll_role.addView(view);

                        btnRole.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loginSecondWebserviceCalling(btnRole.getTag().toString(),et_username.getText().toString());
                            }
                        });
                    }
                }
                dialog.setCancelable(false);
                dialog.show();

            } else {
                Util.showMessageWithOk(context, "Login failed !!!");
            }
        }

        if(isLoginSecondService)
        {
            isLoginSecondService = false;
            if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {
                Log.v("Login","Success");
                user.setName(resultJsonObject.optString("name"));
                user.setUserId(resultJsonObject.optString("id"));
                user.setRoleId(resultJsonObject.optString("role_id"));
                user.setRole(resultJsonObject.optString("role_name"));
                user.setEmail(resultJsonObject.optString("email"));
                user.setDivisionId(resultJsonObject.optString("division_id"));
                user.setPassword(et_password.getText().toString());
                user.seIsRemember(chkbx_rememberMe.isChecked());
                user.setIsLoggedin(true);
                Util.isAllDataFetched(context, true);
                Util.saveUserClass(LoginActivity.this, user);
                openMapActivity();
            } else {
                Util.showMessageWithOk(context, "Login failed !!!");
            }
        }
        if(isUserRoleService)
        {
            isUserRoleService = false;
            if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {

                JSONArray detailsJsonArray=resultJsonObject.optJSONArray("details");
                if (detailsJsonArray != null && detailsJsonArray.length() > 0) {
                    UserRoleClass useRollClass = new UserRoleClass();
                    arrlistUserRole.clear();
                    for (int i = 0; i < detailsJsonArray.length(); i++) {
                        JSONObject detailsJsonObject = detailsJsonArray.optJSONObject(i);

                        UserRole userRole=new UserRole();
                        userRole.setRoleId(detailsJsonObject.optString("role_id"));
                        userRole.setUserName(detailsJsonObject.optString("role_name"));
                        arrlistUserRole.add(userRole);
                        useRollClass.setDetails(arrlistUserRole);
                    }
                }
                //populateUserRoleList();

            } else {
                Toast.makeText(context,"No user role Found !!!",Toast.LENGTH_SHORT).show();
                //Util.showMessageWithOk(context, "No user role Found !!!");
                arrlistUserRole.clear();
                //populateUserRoleList();
            }
        }

        Log.d("test","resultJsonObject----"+resultJsonObject);

    }

    @Override
    public void onError() {

    }

  /*  private void populateUserRoleList()
    {
        if (arrlistUserRole.size() > 0) {
            dropdown_userRole.setEnabled(true);
            String[] corridorArray = new String[arrlistUserRole.size()];

            for (int i = 0; i < arrlistUserRole.size(); i++) {
                corridorArray[i] = arrlistUserRole.get(i).getRole_name();
            }
            dropdown_userRole.setItems(corridorArray);
            if(corridorArray.length==1) {
                dropdown_userRole.setText(corridorArray[0].toString());
                dropdown_userRole.setTag(corridorArray[0].toString());
                userRole_id=arrlistUserRole.get(0).getRole_id();
            }
            else
                dropdown_userRole.setText("Select");
            dropdown_userRole.setTag("");


        } else {
            dropdown_userRole.setEnabled(false);
            dropdown_userRole.setText("");

        }

    }*/



    /*********************GOTO Home ************************/
    private void openMapActivity() {

            Intent intent = new Intent(LoginActivity.this, MapActivity.class);
    startActivity(intent);
    finish();

}


    private void openForgotPasswordActivity() {

        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
        finish();


    }

    protected void setDropDownSelectListener(DropDownClickListener DropDownClickListener) {
        // this.DropDownClickListener = DropDownClickListener;
    }

}
