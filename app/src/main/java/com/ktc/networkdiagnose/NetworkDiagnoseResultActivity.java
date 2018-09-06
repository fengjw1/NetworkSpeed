package com.ktc.networkdiagnose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktc.networkspeed.R;

public class NetworkDiagnoseResultActivity extends Activity {

    private int tag = 0;
    private TextView mTv1Network;
    private ImageView mIv1Network;
    private TextView mTv2Network;
    private ImageView mIv2Network;
    private TextView mTv3Network;
    private ImageView mIv3Network;

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

    private void initData(int tag){
        switch (tag){
            case 1:
                mIv1Network.setImageDrawable(getResources().getDrawable(R.drawable.network_icon_error));
                mIv2Network.setImageDrawable(getResources().getDrawable(R.drawable.network_icon_error));
                mIv3Network.setImageDrawable(getResources().getDrawable(R.drawable.network_icon_error));
                break;
            case 2:
                mIv3Network.setImageDrawable(getResources().getDrawable(R.drawable.network_icon_error));
                break;
            case 3:
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
    }
}
