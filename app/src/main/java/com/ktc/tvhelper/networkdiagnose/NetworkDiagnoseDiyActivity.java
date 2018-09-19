package com.ktc.tvhelper.networkdiagnose;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktc.tvhelper.R;
import com.ktc.tvhelper.networkdiagnose.view.LVCircularZoom;


public class NetworkDiagnoseDiyActivity extends AppCompatActivity implements View.OnClickListener {

    private LVCircularZoom mLvzoom1;
    private LVCircularZoom mLvzoom2;
    private TextView mExceptionDiagnose;
    private LinearLayout mNetworkContent1Diagnose;
    private LinearLayout mNetworkContent2Diagnose;
    private Button mFinishBtnDiagnose;
    private Button mDiagnoseBtnRestart;
    private ImageView mErrorIvTv;
    private ImageView mErrorIvNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_diagnose_diy);

        Intent intent = getIntent();
        int tag = intent.getIntExtra("tag", 0);

        initView();
        initData(tag);
    }

    private void initData(int tag) {
        if (tag == 1) {
            mNetworkContent1Diagnose.setVisibility(View.VISIBLE);
            mNetworkContent2Diagnose.setVisibility(View.GONE);
            mErrorIvTv.setVisibility(View.VISIBLE);
            mErrorIvNetwork.setVisibility(View.GONE);
            mLvzoom1.setViewColor(Color.argb(200, 251, 72, 91));
        } else if (tag == 2) {
            mNetworkContent1Diagnose.setVisibility(View.GONE);
            mNetworkContent2Diagnose.setVisibility(View.VISIBLE);
            mErrorIvTv.setVisibility(View.GONE);
            mErrorIvNetwork.setVisibility(View.VISIBLE);
            mLvzoom2.setViewColor(Color.argb(200, 251, 72, 91));
        }else {
            mErrorIvTv.setVisibility(View.GONE);
            mErrorIvNetwork.setVisibility(View.GONE);
        }
    }

    private void initView() {
        mLvzoom1 = (LVCircularZoom) findViewById(R.id.lvzoom1);
        mLvzoom2 = (LVCircularZoom) findViewById(R.id.lvzoom2);
        mExceptionDiagnose = (TextView) findViewById(R.id.diagnose_exception);
        mNetworkContent1Diagnose = (LinearLayout) findViewById(R.id.diagnose_network_content_1);
        mNetworkContent2Diagnose = (LinearLayout) findViewById(R.id.diagnose_network_content_2);
        mFinishBtnDiagnose = (Button) findViewById(R.id.diagnose_finish_btn);
        mFinishBtnDiagnose.setOnClickListener(this);
        mDiagnoseBtnRestart = (Button) findViewById(R.id.restart_diagnose_btn);
        mDiagnoseBtnRestart.setOnClickListener(this);
        mErrorIvTv = (ImageView) findViewById(R.id.tv_error_iv);
        mErrorIvNetwork = (ImageView) findViewById(R.id.network_error_iv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.diagnose_finish_btn:
                // TODO 18/09/11
                finish();
                break;
            case R.id.restart_diagnose_btn:
                // TODO 18/09/11
                finish();
                Intent intent = new Intent(this, NetworkDiagnoseActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
