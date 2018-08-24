package com.ktc.networkspeed.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ktc.networkspeed.R;
import com.ktc.networkspeed.model.HttpDownloadModel;
import com.ktc.networkspeed.utils.GetSpeedTestHostsHandler;
import com.ktc.networkspeed.view.DashboardView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DownloadTestPopupWindow extends PopupWindow {

    private Activity mContext;
    private View contextView;

    private DashboardView mDashboardView;

    private GetSpeedTestHostsHandler mSpeedTestHostsHandler;
    private boolean downloadTestStarted = false;
    private boolean downloadTestFinished = false;
    private final List<Double> downloadRateList = new ArrayList<>();
    private HashSet<String> tempBlackList;
    private TextView addressTv;

    public DownloadTestPopupWindow(final Activity context){
        mContext = context;
        this.initPopupWindow();
    }

    private void initPopupWindow(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contextView = inflater.inflate(R.layout.activity_download, null);
        contextView.setZ(10f);
//        int h = mContext.getWindowManager().getDefaultDisplay().getHeight();
//        int w = mContext.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(contextView);
//        this.setWidth(w * 1 / 2);
//        this.setHeight(h * 1 / 2);
        this.setWidth(1000);
        this.setHeight(800);
        this.setOutsideTouchable(true);

        init();

    }

    private void init(){
        tempBlackList = new HashSet<>();

        mDashboardView = contextView.findViewById(R.id.dv);
        addressTv = contextView.findViewById(R.id.address_tv);


        int[] location = new int[2];
        mDashboardView.getLocationOnScreen(location);
        Log.d("fengjw1", "location : " + location[0] + " " + location[1]);
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
                        mContext.runOnUiThread(new Runnable() {
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

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String str = String.format(mContext.getResources().getString(R.string.server_address), info.get(5).toString(), info.get(3).toString(),
                                new DecimalFormat("#.##").format(distance / 1000).toString());
                        addressTv.setText(str);
                    }
                });
                try {
                    Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }

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
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mDashboardView.setValue((float) downloadModel.getInstantDownloadRate(), true, false);
                                }
                            });
                        }else {
                            //finish test
                            mContext.runOnUiThread(new Runnable() {
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

}