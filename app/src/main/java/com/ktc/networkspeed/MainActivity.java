package com.ktc.networkspeed;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ktc.networkspeed.model.HttpDownloadModel;
import com.ktc.networkspeed.utils.GetSpeedTestHostsHandler;
import com.ktc.networkspeed.view.DashboardView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DashboardView mDashboardView;

    private static final int UPDATE_SPEED = 0x01;

    private GetSpeedTestHostsHandler mSpeedTestHostsHandler;
    private boolean downloadTestStarted = false;
    private boolean downloadTestFinished = false;
    private final List<Double> downloadRateList = new ArrayList<>();
    private HashSet<String> tempBlackList;


    //tmp
    private Button mButton;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_SPEED:
//                    float max = mDashboardView.getMax();
//                    float min = mDashboardView.getMin();
//                    mDashboardView.setValue(new Random().nextInt(max - min) + min, true, false);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init(){
        tempBlackList = new HashSet<>();
        final DecimalFormat dec = new DecimalFormat("#.##");
        mDashboardView = findViewById(R.id.dv);
        mButton = findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int max = mDashboardView.getMax();
//                int min = mDashboardView.getMin();
//                mDashboardView.setValue(new Random().nextInt(max - min) + min, true, false);
            }
        });

        mSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        mSpeedTestHostsHandler.start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (mSpeedTestHostsHandler == null){
                    Log.d("fengjw", "mSpeedTestHostsHandler == null");
                    mSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                    mSpeedTestHostsHandler.start();
                }

                int tiemCount = 600;
                while (!mSpeedTestHostsHandler.isFinished()){
                    Log.d("fengjw", "!mSpeedTestHostsHandler.isFinished()");
                    tiemCount --;
                    try{
                        Thread.sleep(100);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (tiemCount <= 0){
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

                for (int index : mapKey.keySet()){
                    if (tempBlackList.contains(mapValue.get(index).get(5))){
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
                    if (tmp > distance){
                        tmp = distance;
                        dist = distance;
                        findServerIndex = index;
                    }
                }

                String uploadAddr = mapKey.get(findServerIndex);
                final List<String> info = mapValue.get(findServerIndex);
                final double distance = dist;
                Log.d("fengjw", "uploadAddr : " + uploadAddr);
                final HttpDownloadModel downloadModel = new HttpDownloadModel(
                        uploadAddr.replace(uploadAddr.split("/")[uploadAddr.split("/").length - 1], ""));

                while (true){
                    try {
                        Log.d("fengjw", "while");

                        if (!downloadTestFinished){
                            //Log.d("fengjw", "downloadTestFinished is false.");
                            double downloadRate = downloadModel.getInstantDownloadRate();
                            Log.d("fengjw", "downloadRate : " + downloadRate);
                            downloadRateList.add(downloadRate);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDashboardView.setValue((float) downloadModel.getInstantDownloadRate(), true, false);
                                }
                            });
                        }else {
                            //finish test
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("fengjw", "downloadModel.getFinalDownloadRate() : " + downloadModel.getFinalDownloadRate());
                                    mDashboardView.setValue((float)downloadModel.getFinalDownloadRate(), true, false);
                                }
                            });
                            break;
                        }

                        Log.d("fengjw", "downloadTestStarted : " + downloadTestStarted);
                        Log.d("fengjw", "downloadTestFinished : " + downloadTestFinished);
                        //start test
                        if (!downloadTestStarted){
                            downloadModel.start();
                            downloadTestStarted = true;
                        }

                        if (downloadModel.isFinished()){
                            Log.d("fengjw", "downloadModel isFinished");
                            downloadTestFinished = true;
                        }

                        if (downloadTestStarted && !downloadTestFinished){
                            try {
                                Thread.sleep(300);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            try {
                                Thread.sleep(100);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }



                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("fengjw", "error : " + e.getMessage());
                    }
                }
            }
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
    }

}
