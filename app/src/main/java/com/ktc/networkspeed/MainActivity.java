package com.ktc.networkspeed;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ktc.networkdiagnose.NetworkDiagnoseActivity;
import com.ktc.networkspeed.popupwindow.DownloadTestPopupWindow;
import com.ktc.networkspeed.view.MLinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //ping
    private double instantRtt = 0;
    private double avgRtt = 0.0;

    //tmp
    private Button mButton;
    private Activity mContext;

    private DownloadTestPopupWindow mDownloadTestPopupWindow;
    private MLinearLayout mHomeDiagnoseLlNetwork;
    private MLinearLayout mHomeSpeedLlNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mContext = this;
//        init();

    }

    private void initView() {
        mHomeDiagnoseLlNetwork = (MLinearLayout) findViewById(R.id.network_home_diagnose_ll);
        mHomeDiagnoseLlNetwork.setOnClickListener(this);
        mHomeSpeedLlNetwork = (MLinearLayout) findViewById(R.id.network_home_speed_ll);
        mHomeSpeedLlNetwork.setOnClickListener(this);
    }

    /**
     * 设置屏幕背景透明度
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private void pingTest() {
        String delay = new String();
        Process p = null;
        try {
//            p = Runtime.getRuntime().exec("/system/bin/ping -c 4 "+"www.speedtest.net");
            p = Runtime.getRuntime().exec("/system/bin/ping -c 4 " + "www.google.co.jp");
            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = new String();
            while ((str = buf.readLine()) != null) {
                Log.d("fengjw", "str : " + str);
//                if(str.contains("avg")){
//                    int i = str.indexOf("/",20);
//                    int j = str.indexOf(".", i);
//                    System.out.println("延迟:"+str.substring(i+1, j));
//                    delay =str.substring(i+1, j);
//                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_home_diagnose_ll:
                // TODO 18/09/06
                Intent intent = new Intent(MainActivity.this, NetworkDiagnoseActivity.class);
                startActivity(intent);
                break;
            case R.id.network_home_speed_ll:
                // TODO 18/09/06
//                mDownloadTestPopupWindow = new DownloadTestPopupWindow(MainActivity.this);
//                mDownloadTestPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        backgroundAlpha(1f);
//                    }
//                });
//                mDownloadTestPopupWindow.setAnimationStyle(R.style.PopupWindow);
//                mDownloadTestPopupWindow.showAtLocation(mContext.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//                backgroundAlpha(0.8f);
                Intent intent1 = new Intent(MainActivity.this, NetworkSpeedActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

}
