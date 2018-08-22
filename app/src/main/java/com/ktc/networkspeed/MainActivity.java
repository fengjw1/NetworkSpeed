package com.ktc.networkspeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ktc.networkspeed.model.HttpDownloadModel;
import com.ktc.networkspeed.utils.GetSpeedTestHostsHandler;
import com.ktc.networkspeed.view.DashboardView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int UPDATE_SPEED = 0x01;
    //tmp
    private Button mButton;
    private Activity mContext;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_SPEED:
//                    float max = mDashboardView.getMax();
//                    float min = mDashboardView.getMin();
//                    mDashboardView.setValue(new Random().nextInt(max - min) + min, true, false);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = this;
        init();

    }

    private void init() {
        mButton = findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                DownloadTestView downloadTestView = new DownloadTestView(MainActivity.this);
                downloadTestView.showAtLocation(mContext.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
        });
    }

}
