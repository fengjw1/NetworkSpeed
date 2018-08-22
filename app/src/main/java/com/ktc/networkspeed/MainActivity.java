package com.ktc.networkspeed;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.ktc.networkspeed.view.DownloadTestView;


public class MainActivity extends AppCompatActivity {

    //tmp
    private Button mButton;
    private Activity mContext;

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
                DownloadTestView downloadTestView = new DownloadTestView(MainActivity.this);
                downloadTestView.showAtLocation(mContext.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }
        });
    }

}
