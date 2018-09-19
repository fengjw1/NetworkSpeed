package com.ktc.tvhelper.networkdiagnose;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktc.tvhelper.R;
import com.ktc.tvhelper.networkdiagnose.view.LVCircularZoom;
import com.ktc.tvhelper.networkspeed.utils.AnimUtils;
import com.ktc.tvhelper.networkspeed.utils.GetSpeedTestHostsHandler;
import com.ktc.tvhelper.networkspeed.utils.NetworkState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class NetworkDiagnoseActivity extends AppCompatActivity {

    private TextView mStatusTvNetwork;
    private ImageView mTvIvNetwork;
    private LinearLayout mLl1Network;
    private LVCircularZoom mLvzoom1;
    private ImageView mRouterIvNetwork;
    private LinearLayout mLl2Network;
    private LVCircularZoom mLvzoom2;
    private ImageView mServerIvNetwork;
    private LinearLayout mLl3Network;

    //anim
    private final static int ANIMATION_DURATION = 1500;

    //network status
    private NetworkState mState;

    //ping test
    private GetSpeedTestHostsHandler mSpeedTestHostsHandler;
    //Thread
    private static boolean THREAD_RUN_FLAG = true;

    private Boolean pingTestStarted = false;
    private Boolean pingTestFinished = false;

    private HashSet<String> tempBlackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_diagnose);
        initView();
        initData();
    }

    private void initData(){

        tempBlackList = new HashSet<>();

        mStatusTvNetwork.setText(R.string.diagnose_network);
        mLl1Network.setBackground(getResources().getDrawable(R.drawable.network_diagnose__bg_highlighted));
        mTvIvNetwork.setBackground(getResources().getDrawable(R.drawable.network_diagnose_icon_helios_normal));
        View[] views = {mLl1Network, mTvIvNetwork};
        AnimUtils.setScaleAnimation(views, ANIMATION_DURATION, this);
        mLvzoom1.setViewColor(Color.rgb(255, 0, 122));
        mLvzoom2.setViewColor(Color.rgb(255, 0, 122));
        mLvzoom1.startAnim();

        mState = new NetworkState(this);

//        if (mState.isNetworkConn()){
//            mSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
//            mSpeedTestHostsHandler.start();
//        }else {
//            mSpeedTestHostsHandler = null;
//        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mState.isNetworkConn()){
                                mLvzoom1.stopAnim();

                                //second stage
                                mLl2Network.setBackground(getResources().getDrawable(R.drawable.network_diagnose__bg_highlighted));
                                mRouterIvNetwork.setBackground(getResources().getDrawable(R.drawable.network_diagnose_icon_network_normal));

                                //anim
                                View[] views = {mLl2Network, mRouterIvNetwork};
                                AnimUtils.setScaleAnimation(views, ANIMATION_DURATION, NetworkDiagnoseActivity.this);

                                mLvzoom2.startAnim();
                                mStatusTvNetwork.setText(R.string.diagnose_server);
                                //threadStart();
                                pingTest();
                            }else {
                                mLvzoom1.stopAnim();
                                mStatusTvNetwork.setText(R.string.diagnose_network_error);
                                startActivity(1);
                            }
                        }
                    });
            }
        }).start();
    }

    private void initView() {
        mStatusTvNetwork = (TextView) findViewById(R.id.network_status_tv);

        mTvIvNetwork = (ImageView) findViewById(R.id.network_tv_iv);
        mLl1Network = (LinearLayout) findViewById(R.id.network_ll_1);

        mLvzoom1 = (LVCircularZoom) findViewById(R.id.lvzoom1);

        mRouterIvNetwork = (ImageView) findViewById(R.id.network_router_iv);
        mLl2Network = (LinearLayout) findViewById(R.id.network_ll_2);

        mLvzoom2 = (LVCircularZoom) findViewById(R.id.lvzoom2);

        mServerIvNetwork = (ImageView) findViewById(R.id.network_server_iv);
        mLl3Network = (LinearLayout) findViewById(R.id.network_ll_3);

    }

    private void pingTest(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }

                String delay = new String();
                Process p =null;
                try{
                    p = Runtime.getRuntime().exec("/system/bin/ping -c 6 "+"www.speedtest.net");
//                    p = Runtime.getRuntime().exec("/system/bin/ping -c 4 "+"www.google.co.jp");
                    BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String str = new String();
//                    Log.d("fengjw", "p.waitFor() : " + p.waitFor());
                    if (p.waitFor() == 0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLvzoom2.stopAnim();
                                finish();
                                mLl3Network.setBackground(getResources().getDrawable(R.drawable.network_diagnose__bg_highlighted));
                                mServerIvNetwork.setBackground(getResources().getDrawable(R.drawable.network_diagnose_icon_internet_normal));
                                mStatusTvNetwork.setText(R.string.diagnose_finish);
                                startActivity(3);
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mStatusTvNetwork.setText(R.string.diagnose_network_error);
                                mLvzoom2.stopAnim();
                                startActivity(2);
                            }
                        });
                    }
//                    while((str = buf.readLine())!=null){
//                        Log.d("fengjw", "str : " + str);
//                        if (str.contains("icmp_seq")){
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mLvzoom2.stopAnim();
//                                    mLl3Network.setBackground(getResources().getDrawable(R.drawable.network_diagnose__bg_highlighted));
//                                    mServerIvNetwork.setBackground(getResources().getDrawable(R.drawable.network_diagnose_icon_internet_normal));
//                                    mStatusTvNetwork.setText(R.string.diagnose_finish);
//                                    startActivity(3);
//                                }
//                            });
//                            break;
//                        }
//                        if (str.contains("received")){
////                            String[] strings = str.split(",");
////                            for (int i = 0; i < strings.length; i ++){
////                                Log.d("fengjw", "i = " + i + " : " + strings[i]);
////                            }
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mStatusTvNetwork.setText(R.string.diagnose_network_error);
//                                    startActivity(2);
//                                }
//                            });
//                        }
//                    }
                }catch(IOException e) {
                    e.printStackTrace();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    private void threadStart(){
//        Log.d("fengjw", "threadStart");
//        THREAD_RUN_FLAG = true;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("fengjw", "run");
//                while (THREAD_RUN_FLAG){
//                    Log.d("fengjw", "THREAD_RUN_FLAG : " + THREAD_RUN_FLAG);
//                    if (mSpeedTestHostsHandler == null) {
//                        Log.d("fengjw", "mSpeedTestHostsHandler == null");
//                        mSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
//                        mSpeedTestHostsHandler.start();
//                    }
//                    int timeCount = 600;
//                    while (!mSpeedTestHostsHandler.isFinished()) {
//                        Log.d("fengjw", "mSpeedTestHostsHandler not Finished()");
//                        timeCount--;
//                        try {
//                            Thread.sleep(100);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        if (timeCount <= 0) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mStatusTvNetwork.setText(R.string.diagnose_network_error);
//                                    startActivity(2);
//                                }
//                            });
//                            mSpeedTestHostsHandler = null;
//                            return;
//                        }
//                    }
//                        //find closest server
//                        HashMap<Integer, String> mapKey = mSpeedTestHostsHandler.getMapKey();
//                        HashMap<Integer, List<String>> mapValue = mSpeedTestHostsHandler.getMapValue();
//
//                        double selfLat = mSpeedTestHostsHandler.getSelfLat();
//                        double selfLon = mSpeedTestHostsHandler.getSelfLon();
//                        double tmp = 19349458;
//                        double dist = 0;
//                        int findServerIndex = 0;
//
//                        for (int index : mapKey.keySet()) {
//                            if (tempBlackList.contains(mapValue.get(index).get(5))) {
//                                continue;
//                            }
//                            Location source = new Location("Source");
//                            source.setLatitude(selfLat);
//                            source.setLongitude(selfLon);
//
//                            List<String> ls = mapValue.get(index);
//                            Location dest = new Location("Dest");
//                            dest.setLatitude(Double.parseDouble(ls.get(0)));
//                            dest.setLongitude(Double.parseDouble(ls.get(1)));
//                            double distance = source.distanceTo(dest);
//                            if (tmp > distance) {
//                                tmp = distance;
//                                dist = distance;
//                                findServerIndex = index;
//                            }
//                        }
//
//                        String uploadAddr = mapKey.get(findServerIndex);
//                        final List<String> info = mapValue.get(findServerIndex);
//                        final double distance = dist;
//                        Log.d("fengjw", "uploadAddr : " + uploadAddr);
//
//                        final List<Double> pingRateList = new ArrayList<>();
//                        final PingModel pingModel = new PingModel(info.get(6).replace(":8080", ""), 6);
//
//                        while (true){
//                            Log.d("fengjw", "true");
//                            if (!pingTestStarted){
//                                pingModel.start();
//                                pingTestStarted = true;
//                            }
//
//                            if (pingTestFinished){
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mLvzoom2.stopAnim();
//                                        mLl3Network.setBackground(getResources().getDrawable(R.drawable.network_diagnose__bg_highlighted));
//                                        mServerIvNetwork.setBackground(getResources().getDrawable(R.drawable.network_diagnose_icon_internet_normal));
//                                        mStatusTvNetwork.setText(R.string.diagnose_finish);
//                                        startActivity(3);
//
//                                    }
//                                });
//                                THREAD_RUN_FLAG = false;
//                                break;
//                            }
//
//                            if (pingModel.isFinished()){
//                                pingTestFinished = true;
//                            }
//                        }
//
//                    }
//                }
//        }).start();
//    }

    private void startActivity(int tag){
        finish();
        Intent intent = new Intent(NetworkDiagnoseActivity.this, NetworkDiagnoseResultActivity.class);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }

}
