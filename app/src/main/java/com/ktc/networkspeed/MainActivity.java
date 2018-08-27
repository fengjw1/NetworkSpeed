package com.ktc.networkspeed;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.ktc.networkspeed.popupwindow.DownloadTestPopupWindow;


public class MainActivity extends AppCompatActivity {

    //tmp
    private Button mButton;
    private Activity mContext;

    private DownloadTestPopupWindow mDownloadTestPopupWindow;

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
                mDownloadTestPopupWindow = new DownloadTestPopupWindow(MainActivity.this);
                mDownloadTestPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
                mDownloadTestPopupWindow.setAnimationStyle(R.style.PopupWindow);
                mDownloadTestPopupWindow.showAtLocation(mContext.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
            }
        });
    }


    /**
     * 设置屏幕背景透明度
     */
    private void backgroundAlpha(float bgAlpha){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

}
