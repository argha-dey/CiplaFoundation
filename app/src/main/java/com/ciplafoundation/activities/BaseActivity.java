package com.ciplafoundation.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciplafoundation.R;
import com.ciplafoundation.adapter.AdapterDivisionList;
import com.ciplafoundation.interfaces.OnFileChooseCallBack;
import com.ciplafoundation.interfaces.OnActivityCallBackListner;
import com.ciplafoundation.model.FileDetails;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.model.UserDivision;
import com.ciplafoundation.services.VolleyTaskManager;
import com.ciplafoundation.utility.AlertDialogCallBack;
import com.ciplafoundation.utility.Constants;
import com.ciplafoundation.utility.FilePath;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.ServerResponseCallback;
import com.ciplafoundation.utility.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static android.graphics.Color.parseColor;


/**
 * Created by User-66-pc on 3/21/2017.
 */

public class BaseActivity extends FragmentActivity implements OnClickListener, ServerResponseCallback {

    private static final int galleryRequestCode = 1001, cameraRequestCode = 1002;
    protected FrameLayout frm_Container;
    private TextView tv_division_list;
    private RelativeLayout rl_notification, rl_divisionList;
    private ImageButton ib_option, ib_bck_btn;
    private Context mContext;
    private LayoutInflater inflater;
    private OnActivityCallBackListner onRoleChangeListner;

    public PopupWindow popupWindow_logout, popupWindow_divisionlist;
    private LinearLayoutManager mLayoutManager;
    private AdapterDivisionList mAdapterDivisionList;
    private ArrayList<UserDivision> arrUserDivision;
    public int display_height = 0, display_width = 0;
    private String cameraImagePath = "";
    private OnFileChooseCallBack onFileChooseCallBack;
    private Boolean isResetRolePasswordForDiffrenceuser = false, isSwitcheuser = false, isSuperAdmin = false;
    private VolleyTaskManager volleyTaskManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_layout);
        mContext = BaseActivity.this;
        initview();
    }

    public void initview() {

        frm_Container = (FrameLayout) findViewById(R.id.frm_Container);
        ib_bck_btn = (ImageButton) findViewById(R.id.ib_back_arrow);
        ib_option = (ImageButton) findViewById(R.id.ib_option);
        tv_division_list = (TextView) findViewById(R.id.tv_divisionList);
        rl_notification = (RelativeLayout) findViewById(R.id.rl_notifiaction);
        rl_divisionList = (RelativeLayout) findViewById(R.id.rl_divisionList);

        ib_bck_btn.setOnClickListener(this);
        ib_option.setOnClickListener(this);
        rl_divisionList.setOnClickListener(this);

        /*arrUserDivision= Util.fetchUserDivision(mContext);
        if(arrUserDivision!=null && arrUserDivision.size()>0)
            tv_division_list.setText(""+arrUserDivision.size());
        else
            rl_divisionList.setVisibility(View.GONE);*/

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        display_height = displayMetrics.heightPixels;
        display_width = displayMetrics.widthPixels;

    }

    public void setViewShowHide(boolean back_btn, boolean notification, boolean divisionList) {
        if (back_btn)
            ib_bck_btn.setVisibility(View.VISIBLE);
        else
            ib_bck_btn.setVisibility(View.GONE);

        if (notification)
            rl_notification.setVisibility(View.VISIBLE);
        else
            rl_notification.setVisibility(View.GONE);

        if (divisionList)
            tv_division_list.setVisibility(View.VISIBLE);
        else
            tv_division_list.setVisibility(View.GONE);
    }

    public void setContentLayout(int resId) {
        frm_Container.removeAllViews();
        frm_Container.addView(LayoutInflater.from(mContext).inflate(resId, frm_Container, false));
    }

    public void setDivisionList() {
        arrUserDivision = Util.fetchUserDivision(mContext);
        if (arrUserDivision != null && arrUserDivision.size() > 0) {
            rl_divisionList.setVisibility(View.VISIBLE);
            tv_division_list.setText("" + arrUserDivision.size());
        } else
            rl_divisionList.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ib_back_arrow:
                //ib_bck_btn.setBackgroundResource(R.color.header_hover);
                rl_divisionList.setBackgroundResource(R.color.header_bg);
                ib_option.setBackgroundResource(R.color.header_bg);
                super.onBackPressed();
                Activity activity = (Activity) mContext;
                activity.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
                break;

            case R.id.rl_divisionList:

                if (popupWindow_logout != null && popupWindow_logout.isShowing())
                    popupWindow_logout.dismiss();
                if (popupWindow_divisionlist == null || !popupWindow_divisionlist.isShowing()) {
                    rl_divisionList.setBackgroundResource(R.color.header_hover);
                    ib_option.setBackgroundResource(R.color.header_bg);
                    //ib_bck_btn.setBackgroundResource(R.color.header_bg);
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    final View popupView = layoutInflater.inflate(R.layout.popup_division_list, null);
                    popupWindow_divisionlist = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow_divisionlist.setAnimationStyle(R.style.animationName);
                    popupWindow_divisionlist.setBackgroundDrawable(new BitmapDrawable());
                    RecyclerView rv_divisionlist = (RecyclerView) popupView.findViewById(R.id.rv_divisionlist);
                    mLayoutManager = new LinearLayoutManager(mContext);
                    rv_divisionlist.setLayoutManager(mLayoutManager);
                    mAdapterDivisionList = new AdapterDivisionList(BaseActivity.this, popupWindow_divisionlist, rl_divisionList);
                    if (arrUserDivision != null)
                        if (arrUserDivision.size() != 0)
                            mAdapterDivisionList.AddArray(arrUserDivision);
                    rv_divisionlist.setAdapter(mAdapterDivisionList);

                    rv_divisionlist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow_divisionlist.dismiss();
                        }
                    });

                    popupWindow_divisionlist.setOutsideTouchable(false);
                    popupWindow_divisionlist.showAsDropDown(v, 100, 0);
                } else {
                    rl_divisionList.setBackgroundResource(R.color.header_bg);
                    popupWindow_divisionlist.dismiss();
                }

                break;

            case R.id.ib_option:

                // Log.v("Button clicked","clicked clicked clicked clicked");
                if (popupWindow_divisionlist != null && popupWindow_divisionlist.isShowing())
                    popupWindow_divisionlist.dismiss();

                if (popupWindow_logout == null || !popupWindow_logout.isShowing()) {
                    ib_option.setBackgroundResource(R.color.header_hover);
                    rl_divisionList.setBackgroundResource(R.color.header_bg);

                    //ib_bck_btn.setBackgroundResource(R.color.header_bg);

                    final LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.option_menu, null);
                    popupWindow_logout = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow_logout.setAnimationStyle(R.style.animationLogoutPopup);
                    popupWindow_logout.setBackgroundDrawable(new BitmapDrawable());


                    TextView tv_logout = (TextView) popupView.findViewById(R.id.tv_logout);
                    TextView tv_changeRoll = (TextView) popupView.findViewById(R.id.tv_changeRoll);
                    RelativeLayout rl_login_as = (RelativeLayout) popupView.findViewById(R.id.rl_login_as);
                    TextView tv_login_as = (TextView) popupView.findViewById(R.id.tv_login_as);

                    // TODO to Set Login as option for Super user.....

                    if (Util.fetchUserClass(mContext) != null) {
                        UserClass user = Util.fetchUserClass(mContext);
                        if (user.getRoleId().toString().equalsIgnoreCase("0"))
                            rl_login_as.setVisibility(View.VISIBLE);
                    }


                    tv_login_as.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow_logout.dismiss();
                            Intent logAs = new Intent(mContext, LoginAsActivity.class);
                            startActivity(logAs);
                        }
                    });


//-- LOgin
/*




                    tv_login_as.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(mContext);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.login_as_layout);
                            dialog.getWindow().setGravity(Gravity.TOP);
                            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                            final ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
                            final TextView tv_search_user_list = (TextView) dialog.findViewById(R.id.tv_search_user_list);
                            final EditText et_search_login_as = (EditText) dialog.findViewById(R.id.et_search_login_as);
                            final ProgressDialog progress = new ProgressDialog(mContext);
                            final LinearLayout ll_login_as_list = (LinearLayout) dialog.findViewById(R.id.ll_login_as_list);
                            final ArrayList<UserRole> userListDetails = new ArrayList<>();
                            for (int i = 0; i < 10; i++) {
                                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                                View userListView = layoutInflater.inflate(R.layout.user_single_list_row, null);
                                final RelativeLayout rl_single_row = (RelativeLayout) userListView.findViewById(R.id.rl_single_row);
                                rl_single_row.setTag(i);
                                UserRole ur = new UserRole();
                                ur.setRole_id(""+i);
                                ur.setRole_name(""+i);
                                userListDetails.add(ur);
                                rl_single_row.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        System.out.println("Select id" + userListDetails.get((Integer) rl_single_row.getTag()).getRole_name());

                                    }
                                });

                                ll_login_as_list.addView(userListView);

                            }

             *//*               tv_search_user_list.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    progress.setMessage("Loading...");
                                    progress.show();
                                    new Handler().postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            progress.dismiss();
//whatever you want just you have to launch overhere.
                                            ll_login_as_list.removeAllViews();
                                            for (int i = 0; i < 4; i++) {
                                                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                                                View userListView = layoutInflater.inflate(R.layout.user_single_list_row, null);
                                                ll_login_as_list.addView(userListView);

                                            }


                                        }
                                    }, 3000);//just mention the time when you want to launch your action

                                }
                            });*//*


                            tv_search_user_list.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ll_login_as_list.removeAllViews();
                                    for (int i = 0; i < 4; i++) {
                                        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                                        View userListView = layoutInflater.inflate(R.layout.user_single_list_row, null);
                                        ll_login_as_list.addView(userListView);

                                    }

                                }
                            });


                            iv_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.setCancelable(false);
                            dialog.show();


                        }
                    });*/

                    tv_logout.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            popupWindow_logout.dismiss();

                           Util.showCallBackMessageWithOkCancel(mContext, "Do you want to log out?", new AlertDialogCallBack() {

                                @Override
                                public void onSubmit() {

                                    volleyTaskManager = new VolleyTaskManager(mContext);
                                    UserClass user = Util.fetchUserClass(mContext);
                                    String paramsMap = "USR_USER_ID=" + user.getUserId();
                                    volleyTaskManager.doLogout(paramsMap, true, new ServerResponseCallback() {
                                        @Override
                                        public void onSuccess(JSONObject resultJsonObject) {
                                            if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {
                                                UserClass user = Util.fetchUserClass(mContext);
                                                user.setIsLoggedin(false);
                                                Util.saveUserClass(BaseActivity.this, user);
                                                Prefs prefs = new Prefs(mContext);
                                                prefs.clearPrefdata();
                                                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });

                                }

                                @Override
                                public void onCancel() {
                                    ib_option.setBackgroundResource(R.color.header_bg);

                                }
                            });
                       /*     UserClass user = Util.fetchUserClass(mContext);
                            user.setIsLoggedin(false);
                            Util.saveUserClass(BaseActivity.this, user);
                            Prefs prefs = new Prefs(mContext);
                            prefs.clearPrefdata();
                            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();*/

                        }
                    });

                    tv_changeRoll.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            popupWindow_logout.dismiss();

                            volleyTaskManager = new VolleyTaskManager(mContext);
                            final UserClass user = Util.fetchUserClass(BaseActivity.this);
                            String params = "USR_EMAIL=" + user.getEmail() + "&" + "USR_ROLE_ID=" + user.getRoleId();
                            Log.v("Login", "" + params);
                            isResetRolePasswordForDiffrenceuser = true;
                            volleyTaskManager.doResetRole(params, true, new ServerResponseCallback() {
                                @Override
                                public void onSuccess(JSONObject resultJsonObject) {
                                    Log.v("Response =", resultJsonObject.toString());
                                    if (isResetRolePasswordForDiffrenceuser) {
                                        isResetRolePasswordForDiffrenceuser = false;
                                        final Dialog dialog = new Dialog(mContext);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.change_role_for_password_layout);
                                        dialog.getWindow().setGravity(Gravity.TOP);
                                        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
                                        LinearLayout ll_role = (LinearLayout) dialog.findViewById(R.id.ll_role);
                                        RelativeLayout rl_current_role_view = (RelativeLayout) dialog.findViewById(R.id.rl_current_role_view);
                                        TextView tv_current_role = (TextView) dialog.findViewById(R.id.tv_current_role);
                                        JSONObject JobjCurrentRole = resultJsonObject.optJSONObject("currentrole");
                                        rl_current_role_view.setBackgroundColor(parseColor("#FFB8C692"));
                                        String simple = "Your current role is  ";
                                        String colored = JobjCurrentRole.optString("role_name");
                                        SpannableStringBuilder builder = new SpannableStringBuilder();
                                        builder.append(simple);
                                        int start = builder.length();
                                        builder.append(colored);
                                        int end = builder.length();
                                        builder.setSpan(new ForegroundColorSpan(parseColor("#45812c")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        tv_current_role.setText(builder);
                                        JSONArray jsonRole = resultJsonObject.optJSONArray("details");
                                        if (jsonRole != null && jsonRole.length() > 0) {
                                            for (int i = 0; i < jsonRole.length(); i++) {
                                                View view = LayoutInflater.from(mContext).inflate(R.layout.change_role_view, null);
                                                final TextView btnRole = (TextView) view.findViewById(R.id.btn_role);
                                                final ImageView iv_set_bulet = (ImageView) view.findViewById(R.id.iv_set_bulet);
                                                final ImageView iv_set_right_arrow = (ImageView) view.findViewById(R.id.iv_set_right_arrow);
                                                iv_set_bulet.setImageResource(R.drawable.greybulet);
                                                iv_set_right_arrow.setImageResource(R.drawable.rightarrow);
                                                String simple_new = "To switch your role to  ";
                                                String colorednew = jsonRole.optJSONObject(i).optString("role_name");
                                                SpannableStringBuilder builder_new = new SpannableStringBuilder();
                                                builder_new.append(simple_new);
                                                int start_new = builder_new.length();
                                                builder_new.append(colorednew);
                                                int end_new = builder_new.length();
                                                builder_new.setSpan(new ForegroundColorSpan(parseColor("#FF2998A3")), start_new, end_new, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                btnRole.setText(builder_new);
                                                iv_set_right_arrow.setTag(jsonRole.optJSONObject(i).optString("role_id"));
                                                ll_role.addView(view);

                                                iv_set_right_arrow.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                        final Dialog forgotPassworddialog = new Dialog(mContext);
                                                        forgotPassworddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                        forgotPassworddialog.setContentView(R.layout.change_password_layout);
                                                        forgotPassworddialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                                        forgotPassworddialog.getWindow().setGravity(Gravity.TOP);
                                                        final TextView tv_back = (TextView) forgotPassworddialog.findViewById(R.id.tv_back);
                                                        final Button btn_done = (Button) forgotPassworddialog.findViewById(R.id.btn_done);
                                                        final EditText et_password_forgot = (EditText) forgotPassworddialog.findViewById(R.id.et_password_forgot);


                                                        tv_back.setOnClickListener(new OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                // dialog.show();
                                                                forgotPassworddialog.dismiss();
                                                                dialog.show();
                                                            }
                                                        });
                                                        btn_done.setOnClickListener(new OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (et_password_forgot.getText().toString().length() == 0) {
                                                                    Util.showMessageWithOk(mContext, "Please fill  the required fields!");
                                                                } else {
                                                                    if (Util.checkConnectivity(mContext)) {
                                                                        volleyTaskManager = new VolleyTaskManager(mContext);
                                                                        final UserClass currentUser = Util.fetchUserClass(BaseActivity.this);
                                                                        String paramsMap = "USR_PASSWORD=" + et_password_forgot.getText() + "&" + "USR_ROLEID=" + iv_set_right_arrow.getTag() + "&" + "USR_ID=" + user.getUserId();
                                                                        currentUser.setRoleId(iv_set_right_arrow.getTag().toString());
                                                                        currentUser.setEmail(user.getEmail().toString());
                                                                        Util.saveUserClass(mContext, currentUser);
                                                                        Log.v("first response=", "" + paramsMap);
                                                                        isSwitcheuser = true;
                                                                        volleyTaskManager.doSwitchRoleFirst(paramsMap, true, new ServerResponseCallback() {
                                                                            @Override
                                                                            public void onSuccess(JSONObject resultJsonObject) {
                                                                                Log.v("Response =", resultJsonObject.toString());
                                                                                if (isSwitcheuser) {
                                                                                    isSwitcheuser = false;
                                                                                    if (resultJsonObject.optString("authentication").equalsIgnoreCase("true")) {
                                                                                        Util.fetchUserClass(mContext);


                                                                                        webserviceForMapPage(currentUser.getRoleId(), currentUser.getEmail());


                                                                                    } else {
                                                                                        Util.showMessageWithOk(mContext, "authentication fail!");
                                                                                    }
                                                                                }


                                                                            }

                                                                            private void webserviceForMapPage(String roleId, String userEmail) {
                                                                                HashMap<String, String> paramsMap = new HashMap<String, String>();
                                                                                paramsMap.put("USR_EMAIL", userEmail);
                                                                                paramsMap.put("USR_ROLE_ID", roleId);
                                                                                volleyTaskManager.doSwitchRoleSecond(paramsMap, true, new ServerResponseCallback() {
                                                                                    @Override
                                                                                    public void onSuccess(JSONObject resultJsonObject) {
                                                                                        Log.v("Response after Auth =", resultJsonObject.toString());
                                                                                        if (resultJsonObject.optString("status").equalsIgnoreCase("true")) {

                                                                                            Log.v("Login", "Success");
                                                                                            UserClass user = new UserClass();
                                                                                            user.setName(resultJsonObject.optString("name"));
                                                                                            user.setUserId(resultJsonObject.optString("id"));
                                                                                            user.setRoleId(resultJsonObject.optString("role_id"));
                                                                                            user.setRole(resultJsonObject.optString("role_name"));
                                                                                            user.setEmail(resultJsonObject.optString("email"));
                                                                                            user.setDivisionId(resultJsonObject.optString("division_id"));
                                                                                            user.setIsLoggedin(true);
                                                                                            Util.isAllDataFetched(mContext, true);
                                                                                            Util.saveUserClass(mContext, user);
                                                                                            openMapActivity();

                                                                                        } else {
                                                                                            Util.showMessageWithOk(mContext, "Password is wrong!");
                                                                                        }

                                                                                    }

                                                                                    @Override
                                                                                    public void onError() {

                                                                                    }
                                                                                });

                                                                            }

                                                                            @Override
                                                                            public void onError() {

                                                                            }
                                                                        });

                                                                    } else {

                                                                        Util.showMessageWithOk(mContext, getString(R.string.no_internet));
                                                                    }
                                                                }


                                                            }


                                                        });

                                                        forgotPassworddialog.setCancelable(false);
                                                        forgotPassworddialog.show();

                                                    }
                                                });
                                            }
                                        }
                                        dialog.setCancelable(false);
                                        dialog.show();

                                        iv_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onError() {

                                }
                            });

                        }


                    });


                    popupWindow_logout.setOutsideTouchable(false);
                    popupWindow_logout.showAsDropDown(v, 100, 0);
                } else {
                    //Log.v("Else Part","Else Partttttt");
                    ib_option.setBackgroundResource(R.color.header_bg);
                    popupWindow_logout.dismiss();
                }
                break;
            default:
                break;
        }

    }

    private void openMapActivity() {

        Intent intent = new Intent(BaseActivity.this, MapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    /******************************************
     * //File Attachment//
     *************************************************/

    public void attachFile() {
        final Dialog dialog = new Dialog(mContext, R.style.dialogTheme);
        dialog.setContentView(R.layout.attach_file_dialog);
        dialog.getWindow().setLayout(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // set the custom dialog components - text, image and button
        Button btn_takePicture = (Button) dialog.findViewById(R.id.btn_takePicture);
        Button btn_attach_file = (Button) dialog.findViewById(R.id.btn_attach_file);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        btn_attach_file.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //System.out.println(Build.VERSION.SDK_INT);
                Intent galleryintent = new Intent();
                if (Build.VERSION.SDK_INT < 19) {
                    galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    galleryintent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    galleryintent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                galleryintent.setType("*/*");
                startActivityForResult(galleryintent, galleryRequestCode);
            }
        });
        btn_takePicture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cameraImagePath = Util.getDefaultFilePathForImg();
                Intent cameraIntent;
                cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);// action
                // to
                // capture
                // only
                // images
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cameraImagePath)));
                startActivityForResult(cameraIntent, cameraRequestCode);
            }
        });
        btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        // dialog.getWindow().getAttributes().windowAnimations =
        // R.style.dialog_bottom_animation;

        dialog.show();
    }

    /*
         * (non-Javadoc)
         *
         * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
         * android.content.Intent)
         */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case galleryRequestCode:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri selectedFileUri = data.getData();
                    String mimeType = getContentResolver().getType(selectedFileUri);
                    // System.out.println("mimeType--"+mimeType);
                    String selectedFilePath = FilePath.getPath(this, selectedFileUri);
                    Uri uri = data.getData();
                    int actionType = Util.checkFileExtension(mContext, uri);
                    //Log.e(" ", "uri" + uri);
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    //Log.e(" ", "uri" + uri);
                    String displayName = null;
                    FileDetails file = null;
                    // int sizeIndex = 0;
                    try {
                        InputStream input = mContext.getContentResolver().openInputStream(uri);
                        if (uriString.startsWith("content://")) {
                            Cursor cursor = null;
                            try {
                                cursor = getContentResolver().query(uri, null, null, null, null);
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                    // sizeIndex =
                                    // cursor.getColumnIndex(OpenableColumns.SIZE);
                                }
                            } finally {
                                cursor.close();
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile.getName();
                        }
                        String fileType = displayName.substring(displayName.lastIndexOf(".") + 1);

                        if (Util.isExist(Constants.fileTypes, fileType) || (mimeType != null && Util.isExist(Constants.mimeTypes, mimeType))) {
                            file = new FileDetails();
                            file.setFileName(displayName);
                            file.setFilePath(uriString);
                            file.setSelectedFilePath(selectedFilePath);
                            // file.setFileSize(sizeIndex);
                            file.setInputStream(input);
                            file.setActionType(actionType);

                            if (onFileChooseCallBack != null) {

                                //System.out.println("Get on file choose callback");
                                onFileChooseCallBack.onFileChoose(file);
                            }
                        } else {

                            Toast.makeText(mContext, "Please choose another file type.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (FileNotFoundException e) {

                        e.printStackTrace();
                    }
                    //Log.e("dalmia", "displayName=--" + displayName);


                }
                break;

            case cameraRequestCode:
                if (resultCode == Activity.RESULT_OK) {
                    int actionType = Util.checkFileExtension(mContext, Uri.parse(cameraImagePath));
                    FileDetails file = null;
                    //Log.e("Dalmia", cameraImagePath + " " + actionType);
                    try {
                        File img = new File(cameraImagePath);
                        InputStream input = mContext.getContentResolver().openInputStream(Uri.fromFile(img));
                        file = new FileDetails();
                        file.setFileName(img.getName());
                        file.setFilePath(Uri.fromFile(img).toString());
                        file.setSelectedFilePath(img.getAbsolutePath());
                        // file.setFileSize(img.length());
                        file.setInputStream(input);
                        file.setActionType(actionType);
                    } catch (FileNotFoundException e) {

                        e.printStackTrace();
                    }
                    if (onFileChooseCallBack != null) {
                        onFileChooseCallBack.onFileChoose(file);

                    }

                }
                break;
        }

    }

    public void setOnFileChoose(OnFileChooseCallBack onFileChooseCallBack) {
        this.onFileChooseCallBack = onFileChooseCallBack;
    }


    @Override
    public void onSuccess(JSONObject resultJsonObject) {


    }

    @Override
    public void onError() {

    }
}
