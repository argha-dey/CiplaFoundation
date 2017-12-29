package com.ciplafoundation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.andexert.library.RippleView;
import com.ciplafoundation.R;

/**
 * Created by User-66-pc on 3/28/2017.
 */

public class TaskActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_proposal, btn_project, btn_event, btn_dashboard, btn_medical_grant;
    private RippleView rv_medical_grant;
    private RippleView rv_dashboard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_task);
        init();
    }

    public void init() {


        setViewShowHide(true, false, false);
        btn_dashboard = (Button) findViewById(R.id.btn_dashboard);
        rv_medical_grant=(RippleView) findViewById(R.id.rv_medical_grant);
        btn_proposal = (Button) findViewById(R.id.btn_proposal);
        btn_project = (Button) findViewById(R.id.btn_project);
        btn_event = (Button) findViewById(R.id.btn_event);

        rv_dashboard = (RippleView) findViewById(R.id.rv_dashboard);

        if(getIntent().getStringExtra("DivisionId").equals("D0000011")){

            btn_medical_grant = (Button) findViewById(R.id.btn_medical_grant);
            rv_medical_grant.setVisibility(View.VISIBLE);
            btn_medical_grant.setOnClickListener(this);
        }

        btn_proposal.setOnClickListener(this);
        btn_project.setOnClickListener(this);
        btn_event.setOnClickListener(this);
        btn_dashboard.setOnClickListener(this);


        /*if(getResources().getBoolean(R.bool.isTablet))
            rv_dashboard.setVisibility(View.VISIBLE);*/
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btn_proposal:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent event = new Intent(TaskActivity.this, FragmentBaseActivity.class);
                        event.putExtra("task", "proposal");
                        startActivity(event);
                    }
                }, 500);

                break;
            case R.id.btn_project:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent project = new Intent(TaskActivity.this, FragmentBaseActivity.class);
                        project.putExtra("task", "project");
                        startActivity(project);
                    }
                }, 500);
                break;
            case R.id.btn_event:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent event = new Intent(TaskActivity.this, FragmentBaseActivity.class);
                        event.putExtra("task", "event");
                        startActivity(event);
                    }
                }, 500);
                break;

            case R.id.btn_medical_grant:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent medical = new Intent(TaskActivity.this, FragmentBaseActivity.class);
                        medical.putExtra("task", "medical");
                        startActivity(medical);
                    }
                }, 500);
                break;
            case R.id.btn_dashboard:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       /* Intent project=new Intent(TaskActivity.this,WebViewActivity.class);
                        startActivity(project);*/
                    }
                }, 500);
                break;
            default:
                break;
        }
    }
}
