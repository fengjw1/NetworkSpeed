package com.ktc.networkspeed;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktc.networkspeed.model.HttpDownloadModel;
import com.ktc.networkspeed.utils.AnimUtils;
import com.ktc.networkspeed.utils.BroadBandTransforTool;
import com.ktc.networkspeed.utils.GetSpeedTestHostsHandler;
import com.ktc.networkspeed.utils.NetworkState;
import com.ktc.networkspeed.view.DashboardView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class NetworkSpeedActivity extends Activity {

    private DashboardView mDashboardView;
    private RelativeLayout popupwindowRl;

    //second page
    private LinearLayout popupwindowLl;
    private TextView DownloadSpeedTv;
    private TextView BandStandardTv;
    private Button restartBtn;
    private ImageView speedStateIv;

    private NetworkState mState;

    private GetSpeedTestHostsHandler mSpeedTestHostsHandler;
    private boolean downloadTestStarted = false;
    private boolean downloadTestFinished = false;
    private final List<Double> downloadRateList = new ArrayList<>();
    private HashSet<String> tempBlackList;
    private TextView addressTv;

    private int count = 1;

    //Thread
    private Thread mThread;
    private static boolean THREAD_RUN_FLAG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_speed);

        initView();

        if (mState.isNetworkConn()) {
            init();
        }else {
            Toast.makeText(NetworkSpeedActivity.this, R.string.not_network, Toast.LENGTH_LONG).show();
            addressTv.setText(R.string.not_network);
        }
    }

    private void init(){
        tempBlackList = new HashSet<>();
        mSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        mSpeedTestHostsHandler.start();
        threadStart();
    }

    private void initView(){
        mDashboardView = findViewById(R.id.dv);
        addressTv = findViewById(R.id.address_tv);
        popupwindowRl = findViewById(R.id.popupwindow_rl);
        //keycode_back listener
        popupwindowRl.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });

        //second page
        popupwindowLl = (LinearLayout) findViewById(R.id.popupwindow_ll);
        DownloadSpeedTv = (TextView) findViewById(R.id.popupwindow_band_width_tv);
        BandStandardTv = (TextView) findViewById(R.id.video_standard);
        speedStateIv = (ImageView) findViewById(R.id.speed_image_iv);
        restartBtn = (Button) findViewById(R.id.restart_btn);
        restartBtn.setFocusable(true);
        restartBtn.requestFocus();
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View[] views1 = {popupwindowLl, DownloadSpeedTv, BandStandardTv, speedStateIv, restartBtn};
                AnimUtils.setHideAnimation(views1, 3000);

                View[] views2 = {mDashboardView, addressTv};
                AnimUtils.setStartAnimation(views2, 3000);

                initRestartConfig();

            }
        });

        int[] location = new int[2];
        mDashboardView.getLocationOnScreen(location);
        Log.d("fengjw", "location : " + location[0] + " " + location[1]);

        mState = new NetworkState(NetworkSpeedActivity.this);

    }

    private void initRestartConfig(){

        mDashboardView.setValue(0);
        addressTv.setText(R.string.select_address);

        downloadTestStarted = false;
        downloadTestFinished = false;
        mSpeedTestHostsHandler = null;
        threadStart();
    }

    private void threadStart(){

//        try {
//            if (mThread != null && mThread.isAlive()){
//                Log.d("fengjw", "mThread isAlive!");
//                mThread.interrupt();
//                mThread = null;
//                mSpeedTestHostsHandler = null;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        THREAD_RUN_FLAG = true;
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (THREAD_RUN_FLAG) {
                    if (mSpeedTestHostsHandler == null) {
                        Log.d("fengjw", "mSpeedTestHostsHandler == null");
                        mSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                        mSpeedTestHostsHandler.start();
                    }

                    //ping test
                    if (!mSpeedTestHostsHandler.isConnected()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addressTv.setText(R.string.not_connection);
                            }
                        });
                        THREAD_RUN_FLAG = false;
                        break;
                    }

                    int tiemCount = 600;
                    while (!mSpeedTestHostsHandler.isFinished()) {
//                        Log.d("fengjw", "!mSpeedTestHostsHandler.isFinished()");
                        tiemCount--;
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (tiemCount <= 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addressTv.setText(R.string.not_connection);
                                }
                            });
                            mSpeedTestHostsHandler = null;
                            return;
                        }
                    }

                    //find closest server
                    HashMap<Integer, String> mapKey = mSpeedTestHostsHandler.getMapKey();
                    HashMap<Integer, List<String>> mapValue = mSpeedTestHostsHandler.getMapValue();

                    double selfLat = mSpeedTestHostsHandler.getSelfLat();
                    double selfLon = mSpeedTestHostsHandler.getSelfLon();
                    double tmp = 19349458;
                    double dist = 0;
                    int findServerIndex = 0;

                    for (int index : mapKey.keySet()) {
                        if (tempBlackList.contains(mapValue.get(index).get(5))) {
                            continue;
                        }
                        Location source = new Location("Source");
                        source.setLatitude(selfLat);
                        source.setLongitude(selfLon);

                        List<String> ls = mapValue.get(index);
                        Location dest = new Location("Dest");
                        dest.setLatitude(Double.parseDouble(ls.get(0)));
                        dest.setLongitude(Double.parseDouble(ls.get(1)));
                        double distance = source.distanceTo(dest);
                        if (tmp > distance) {
                            tmp = distance;
                            dist = distance;
                            findServerIndex = index;
                        }
                    }

                    String uploadAddr = mapKey.get(findServerIndex);
                    final List<String> info = mapValue.get(findServerIndex);
                    final double distance = dist;
                    Log.d("fengjw", "uploadAddr : " + uploadAddr);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String str = String.format(getResources().getString(R.string.server_address), info.get(5).toString(), info.get(3).toString(),
                                    new DecimalFormat("#.##").format(distance / 1000).toString());
                            addressTv.setText(str);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    final HttpDownloadModel downloadModel = new HttpDownloadModel(
                            uploadAddr.replace(uploadAddr.split("/")[uploadAddr.split("/").length - 1], ""));

                    while (true) {
                        try {
                            Log.d("fengjw", "while");

                            if (!downloadTestFinished) {
                                //Log.d("fengjw", "downloadTestFinished is false.");
                                double downloadRate = downloadModel.getInstantDownloadRate();
//                                Log.d("fengjw", "downloadRate : " + downloadRate);
                                downloadRateList.add(downloadRate);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDashboardView.setValue((float) downloadModel.getInstantDownloadRate(), true, false);
                                    }
                                });
                            } else {
                                //finish test
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        Log.d("fengjw", "downloadModel.getFinalDownloadRate() : " + downloadModel.getFinalDownloadRate());
                                        mDashboardView.setValue((float) downloadModel.getFinalDownloadRate(), true, false);
                                    }
                                });
                                Thread.sleep(2000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        View[] views = {mDashboardView, addressTv};
                                        AnimUtils.setHideAnimation(views, 3000);
                                    }
                                });
//                            Thread.sleep(3200);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DownloadSpeedTv.setText(String.valueOf((float) downloadModel.getFinalDownloadRate()));
                                        String bandwidth = BroadBandTransforTool.mbpsTransforMB(NetworkSpeedActivity.this, (float) downloadModel.getFinalDownloadRate());
                                        BandStandardTv.setText(bandwidth);
                                        Drawable drawable = BroadBandTransforTool.returnIntState(NetworkSpeedActivity.this, (float) downloadModel.getFinalDownloadRate());
                                        speedStateIv.setBackground(drawable);
                                        View[] views = {popupwindowLl, DownloadSpeedTv, BandStandardTv, restartBtn};
                                        AnimUtils.setStartAnimation(views, 3000);
//
                                        int tag = BroadBandTransforTool.returnTag(NetworkSpeedActivity.this, (float) downloadModel.getFinalDownloadRate());
                                        AnimUtils.setStartTranslate(speedStateIv, 5000, tag);
                                    }
                                });
                                THREAD_RUN_FLAG = false;
                                break;
                            }

                            Log.d("fengjw", "downloadTestStarted : " + downloadTestStarted);
                            Log.d("fengjw", "downloadTestFinished : " + downloadTestFinished);
                            //start test
                            if (!downloadTestStarted) {
                                downloadModel.start();
                                downloadTestStarted = true;
                            }

                            if (downloadModel.isFinished()) {
                                Log.d("fengjw", "downloadModel isFinished");
                                downloadTestFinished = true;
                            }

                            if (downloadTestStarted && !downloadTestFinished) {
                                try {
                                    Thread.sleep(300);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("fengjw", "error : " + e.getMessage());
                        }
                    }
                }
            }
        });
        mThread.start();
    }



}
