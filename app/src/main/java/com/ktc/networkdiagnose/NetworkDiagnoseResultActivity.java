package com.ktc.networkdiagnose;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktc.networkdiagnose.view.LVCircularZoom;
import com.ktc.networkspeed.R;

public class NetworkDiagnoseResultActivity extends Activity implements View.OnClickListener {

    private int tag = 0;
    private TextView mTv1Network;
    private ImageView mIv1Network;
    private TextView mTv2Network;
    private ImageView mIv2Network;
    private TextView mTv3Network;
    private ImageView mIv3Network;
    private Button mFinishBtnDiagnose;
    private Button mDiagnoseBtnRestart;
    private LVCircularZoom mLvzoom1;
    private ImageView mErrorIvTv;
    private LVCircularZoom mLvzoom2;
    private ImageView mErrorIvNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_diagnose_result);
        initView();

        Intent intent = getIntent();
        tag = intent.getIntExtra("tag", 0);
        Log.d("fengjw", "tag : " + tag);
        initData(tag);
    }

    private void initData(int tag) {
        switch (tag) {
            case 1:
                mIv1Network.setImageDrawable(getResources().getDrawable(R.drawable.network_icon_error));
                mTv1Network.setTextColor(Color.argb(255, 225, 36, 36));
                mIv2Network.setImageDrawable(getResources().getDrawable(R.drawable.network_icon_error));
                mTv2Network.setTextColor(Color.argb(255, 225, 36, 36));
                mIv3Network.setImageDrawable(getResources().getDrawable(R.drawable.network_icon_error));
                mTv3Network.setTextColor(Color.argb(255, 225, 36, 36));
                mFinishBtnDiagnose.setText(R.string.diagnose_network_diy);
                mErrorIvTv.setVisibility(View.VISIBLE);
                mErrorIvNetwork.setVisibility(View.GONE);
                mLvzoom1.setViewColor(Color.argb(200, 251, 72, 91));
                break;
            case 2:
                mIv3Network.setImageDrawable(getResources().getDrawable(R.drawable.network_icon_error));
                mTv3Network.setTextColor(Color.argb(255, 225, 36, 36));
                mFinishBtnDiagnose.setText(R.string.diagnose_network_diy);
                mErrorIvTv.setVisibility(View.GONE);
                mErrorIvNetwork.setVisibility(View.VISIBLE);
                mLvzoom2.setViewColor(Color.argb(200, 251, 72, 91));
                break;
            case 3:
                mFinishBtnDiagnose.setText(R.string.diagnose_success);
                break;
            default:
                break;
        }
    }

    private void initView() {
        mTv1Network = (TextView) findViewById(R.id.network_tv_1);
        mIv1Network = (ImageView) findViewById(R.id.network_iv_1);
        mTv2Network = (TextView) findViewById(R.id.network_tv_2);
        mIv2Network = (ImageView) findViewById(R.id.network_iv_2);
        mTv3Network = (TextView) findViewById(R.id.network_tv_3);
        mIv3Network = (ImageView) findViewById(R.id.network_iv_3);
        mFinishBtnDiagnose = (Button) findViewById(R.id.diagnose_finish_btn);
        mFinishBtnDiagnose.setOnClickListener(this);
        mDiagnoseBtnRestart = (Button) findViewById(R.id.restart_diagnose_btn);
        mDiagnoseBtnRestart.setOnClickListener(this);
        mLvzoom1 = (LVCircularZoom) findViewById(R.id.lvzoom1);
        mErrorIvTv = (ImageView) findViewById(R.id.tv_error_iv);
        mLvzoom2 = (LVCircularZoom) findViewById(R.id.lvzoom2);
        mErrorIvNetwork = (ImageView) findViewById(R.id.network_error_iv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.diagnose_finish_btn:
                if (tag < 3) {
                    finish();
                    Intent intent = new Intent(this, NetworkDiagnoseDiyActivity.class);
                    intent.putExtra("tag", tag);
                    startActivity(intent);
                } else {
                    finish();
                }
                break;
            case R.id.restart_diagnose_btn:
                finish();
                Intent intent = new Intent(this, NetworkDiagnoseActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
