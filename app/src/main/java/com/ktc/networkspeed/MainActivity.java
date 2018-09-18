package com.ktc.networkspeed;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.ktc.networkdiagnose.NetworkDiagnoseActivity;
import com.ktc.networkspeed.view.MLinearLayout;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MLinearLayout mHomeDiagnoseLlNetwork;
    private MLinearLayout mHomeSpeedLlNetwork;
    private MLinearLayout mHomeUpdateLlNetwork;
    private MLinearLayout mHomeMemoryLlNetwork;
    private MLinearLayout mHomeFileLlNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mHomeDiagnoseLlNetwork = (MLinearLayout) findViewById(R.id.network_home_diagnose_ll);
        mHomeDiagnoseLlNetwork.setOnClickListener(this);
        mHomeSpeedLlNetwork = (MLinearLayout) findViewById(R.id.network_home_speed_ll);
        mHomeSpeedLlNetwork.setOnClickListener(this);
        mHomeSpeedLlNetwork.requestFocus();
        mHomeUpdateLlNetwork = (MLinearLayout) findViewById(R.id.network_home_update_ll);
        mHomeUpdateLlNetwork.setOnClickListener(this);
        mHomeMemoryLlNetwork = (MLinearLayout) findViewById(R.id.network_home_memory_ll);
        mHomeMemoryLlNetwork.setOnClickListener(this);
        mHomeFileLlNetwork = (MLinearLayout) findViewById(R.id.network_home_file_ll);
        mHomeFileLlNetwork.setOnClickListener(this);
    }

    /**
     * 设置屏幕背景透明度
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String pkgName = null;
        String className = null;
        switch (v.getId()) {
            case R.id.network_home_diagnose_ll:
                // TODO 18/09/06
                intent = new Intent(MainActivity.this, NetworkDiagnoseActivity.class);
                startActivity(intent);
                break;
            case R.id.network_home_speed_ll:
                // TODO 18/09/06
                Intent intent1 = new Intent(MainActivity.this, NetworkSpeedActivity.class);
                startActivity(intent1);
                break;
            case R.id.network_home_update_ll:// TODO 18/09/18
                pkgName = "com.ktc.apkupdatetool";
                className = "com.ktc.apkupdatetool.DownloadAllActivity";
                intent.setComponent(new ComponentName(pkgName, className));
                startActivity(intent);
                break;
            case R.id.network_home_memory_ll:// TODO 18/09/18
                pkgName = "com.ktc.systemmanager";
                className = "com.ktc.systemmanager.HomeDialogActivity";
                intent.setComponent(new ComponentName(pkgName, className));
                startActivity(intent);
                break;
            case R.id.network_home_file_ll:// TODO 18/09/18
                pkgName = "com.ktc.filemanager";
                className = "com.ktc.filemanager.activity.FirstPageActivity";
                intent.setComponent(new ComponentName(pkgName, className));
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
