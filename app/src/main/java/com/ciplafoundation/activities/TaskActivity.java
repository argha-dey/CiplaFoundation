package com.ciplafoundation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ciplafoundation.R;

/**
 * Created by User-66-pc on 3/28/2017.
 */

public class TaskActivity extends BaseActivity implements View.OnClickListener{

    private Button btn_proposal,btn_project,btn_event;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_task);
        init();
    }

    public void init()
    {
        setViewShowHide(true,false,false);
        btn_proposal=(Button)findViewById(R.id.btn_proposal);
        btn_project=(Button)findViewById(R.id.btn_project);
        btn_event=(Button)findViewById(R.id.btn_event);
        btn_proposal.setOnClickListener(this);
        btn_project.setOnClickListener(this);
        btn_event.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId())
        {
            case R.id.btn_proposal:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(TaskActivity.this,FragmentBaseActivity.class));
                    }
                },500);

                break;
            case R.id.btn_project:
                Toast.makeText(TaskActivity.this,"Coming soon...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_event:
                Toast.makeText(TaskActivity.this,"Coming soon...",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
