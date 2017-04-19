package com.ciplafoundation.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ciplafoundation.R;
import com.ciplafoundation.adapter.AdapterDivisionList;
import com.ciplafoundation.model.UserClass;
import com.ciplafoundation.model.UserDivision;
import com.ciplafoundation.utility.AlertDialogCallBack;
import com.ciplafoundation.utility.Prefs;
import com.ciplafoundation.utility.Util;

import java.util.ArrayList;


/**
 * Created by User-66-pc on 3/21/2017.
 */

public class BaseActivity extends FragmentActivity implements OnClickListener{

    protected FrameLayout frm_Container;
    private TextView tv_division_list;
    private RelativeLayout rl_notification,rl_divisionList;
    private ImageButton ib_option,ib_bck_btn;
    private Context mContext;
    public PopupWindow popupWindow_logout,popupWindow_divisionlist;
    private LinearLayoutManager mLayoutManager;
    private AdapterDivisionList mAdapterDivisionList;
    private ArrayList<UserDivision> arrUserDivision;
    public  int display_height=0,display_width=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_layout);
        initview();
    }

    public void initview()
    {
        mContext=BaseActivity.this;
        frm_Container=(FrameLayout)findViewById(R.id.frm_Container);
        ib_bck_btn=(ImageButton)findViewById(R.id.ib_back_arrow);
        ib_option=(ImageButton)findViewById(R.id.ib_option);
        tv_division_list=(TextView)findViewById(R.id.tv_divisionList);
        rl_notification=(RelativeLayout)findViewById(R.id.rl_notifiaction);
        rl_divisionList=(RelativeLayout)findViewById(R.id.rl_divisionList);

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

    public void setViewShowHide(boolean back_btn,boolean notification,boolean divisionList)
    {
        if(back_btn)
            ib_bck_btn.setVisibility(View.VISIBLE);
        else
            ib_bck_btn.setVisibility(View.GONE);

        if(notification)
            rl_notification.setVisibility(View.VISIBLE);
        else
            rl_notification.setVisibility(View.GONE);

        if(divisionList)
            tv_division_list.setVisibility(View.VISIBLE);
        else
            tv_division_list.setVisibility(View.GONE);
    }

    public void setContentLayout(int resId) {
        frm_Container.removeAllViews();
        frm_Container.addView(LayoutInflater.from(mContext).inflate(resId, frm_Container, false));
    }

    public void setDivisionList()
    {
        arrUserDivision= Util.fetchUserDivision(mContext);
        if(arrUserDivision!=null && arrUserDivision.size()>0)
        {
            rl_divisionList.setVisibility(View.VISIBLE);
            tv_division_list.setText(""+arrUserDivision.size());
        }

        else
            rl_divisionList.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ib_back_arrow:
                //ib_bck_btn.setBackgroundResource(R.color.header_hover);
                rl_divisionList.setBackgroundResource(R.color.header_bg);
                ib_option.setBackgroundResource(R.color.header_bg);
                super.onBackPressed();
                Activity activity = (Activity) mContext;
                activity.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
                break;

            case R.id.rl_divisionList:

                if (popupWindow_logout!=null && popupWindow_logout.isShowing())
                    popupWindow_logout.dismiss();
                if(popupWindow_divisionlist == null || !popupWindow_divisionlist.isShowing())
                {
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
                    mAdapterDivisionList = new AdapterDivisionList(BaseActivity.this,popupWindow_divisionlist,rl_divisionList);
                    if(arrUserDivision!=null)
                    if(arrUserDivision.size()!=0)
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
                } else
                {
                    rl_divisionList.setBackgroundResource(R.color.header_bg);
                    popupWindow_divisionlist.dismiss();
                }

                break;

            case R.id.ib_option:

                Log.v("Button clicked","clicked clicked clicked clicked");
                if (popupWindow_divisionlist!=null && popupWindow_divisionlist.isShowing())
                    popupWindow_divisionlist.dismiss();

                if (popupWindow_logout == null || !popupWindow_logout.isShowing()) {
                    ib_option.setBackgroundResource(R.color.header_hover);
                    rl_divisionList.setBackgroundResource(R.color.header_bg);
                    //ib_bck_btn.setBackgroundResource(R.color.header_bg);
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.option_menu, null);
                    popupWindow_logout = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow_logout.setAnimationStyle(R.style.animationLogoutPopup);
                    popupWindow_logout.setBackgroundDrawable(new BitmapDrawable());

                    TextView tv_logout = (TextView) popupView.findViewById(R.id.tv_logout);
                    TextView tv_changeRoll = (TextView) popupView.findViewById(R.id.tv_changeRoll);

                    tv_logout.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            popupWindow_logout.dismiss();

                            Util.showCallBackMessageWithOkCancel(BaseActivity.this, "Do you want to log out?", new AlertDialogCallBack() {

                                @Override
                                public void onSubmit() {
                                    UserClass user = Util.fetchUserClass(BaseActivity.this);
                                    user.setIsLoggedin(false);
                                    Util.saveUserClass(BaseActivity.this, user);
                                    Prefs prefs=new Prefs(mContext);
                                    prefs.clearPrefdata();
                                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancel() {
                                    ib_option.setBackgroundResource(R.color.header_bg);

                                }
                            });

                        }
                    });

                    tv_changeRoll.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            Toast.makeText(mContext,"Coming soon...",Toast.LENGTH_SHORT).show();

                        }
                    });

                    popupWindow_logout.setOutsideTouchable(false);
                    popupWindow_logout.showAsDropDown(v, 100, 0);
                } else {
                    Log.v("Else Part","Else Partttttt");
                    ib_option.setBackgroundResource(R.color.header_bg);
                    popupWindow_logout.dismiss();
                }
                break;
            default:
                break;
        }

    }
}
