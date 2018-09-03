package com.ktc.networkdiagnose;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.ktc.networkdiagnose.view.LVCircularZoom;
import com.ktc.networkspeed.R;

public class NetworkDiagnoseActivity extends AppCompatActivity {

    private LVCircularZoom mZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_diagnose);

        mZoom = (LVCircularZoom) findViewById(R.id.lvzoom);
        mZoom.setViewColor(Color.rgb(255, 0, 122));
        mZoom.startAnim(5000);

    }

}
