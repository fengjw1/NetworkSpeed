package com.ktc.tvhelper.networkspeed.model;

import android.util.Log;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpDownloadModel extends Thread {

    public String fileURL = "";
    private long startTime = 0;
    private long endTime = 0;
    private double downloadElapsedTime = 0;
    private int downloadedByte = 0;
    private double finalDownloadRate =0.0;
    private double instantDownloadRate = 0.0;
    private boolean finished = false;
    private double timeout = 8;

    private HttpURLConnection httpConn = null;

    public HttpDownloadModel(String fileURL){
        Log.d("fengjw", "HttpDownloadModel");
        this.fileURL = fileURL;
    }

    public double getInstantDownloadRate() {
        Log.d("fengjw", "getInstantDownloadRate");
        return instantDownloadRate;
    }


    public void setInstantDownloadRate(int downloadedByte, double elapsedTime){
        Log.d("fengjw", "setInstantDownloadRate");
        if (downloadedByte >= 0){
            this.instantDownloadRate = round((Double) (((downloadedByte * 8) / (1000 * 1000)) / elapsedTime), 2);
        }else {
            this.instantDownloadRate = 0.0;
        }
    }

    public double getFinalDownloadRate() {
        return round(finalDownloadRate, 2);
    }

    public boolean isFinished() {
        return finished;
    }

    private double round(double value, int places){
//        Log.d("fengjw", "value : " + value);
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd; //bignums
        try {
            bd = new BigDecimal(value);
        }catch (Exception e){
            e.printStackTrace();
            return 0.0;
        }
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void run() {
        Log.d("fengjw", "run");
        URL url = null;
        downloadedByte = 0;
        int responseCode = 0;

        List<String> fileUrls = new ArrayList<>();
        fileUrls.add(fileURL + "random4000x4000.jpg");
        fileUrls.add(fileURL + "random3000x3000.jpg");

        startTime = System.currentTimeMillis();

        outer:
        for (String link : fileUrls){
            try {
                url = new URL(link);
                httpConn = (HttpURLConnection) url.openConnection();
                responseCode = httpConn.getResponseCode();
                Log.d("fengjw", "responseCode : " + responseCode);
            }catch (Exception e){
                Log.d("fengjw", "Exception : " + e.getMessage());
                e.printStackTrace();
            }

            try {
                if (responseCode == HttpURLConnection.HTTP_OK){
                    byte[] buffer = new byte[10240];
                    InputStream inputStream = httpConn.getInputStream();
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1){
                        downloadedByte += len;
                        endTime = System.currentTimeMillis();
                        downloadElapsedTime = (endTime - startTime) / 1000.0;
                        setInstantDownloadRate(downloadedByte, downloadElapsedTime);
                        Log.d("fengjw", "downloadedByte : " + downloadedByte);
                        if (downloadElapsedTime >= timeout){
                            Log.d("fengjw", "timeout");
                            break outer;
                        }
                    }
                    inputStream.close();
                    httpConn.disconnect();
                }else {
                    Log.d("fengjw", "Link not found.");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
            endTime = System.currentTimeMillis();
            downloadElapsedTime = (endTime - startTime) / 1000.0;
            finalDownloadRate = ((downloadedByte * 8) / (1000 * 1000.0)) / downloadElapsedTime;
            finished = true;
            Log.d("fengjw", "HttpDownloadModel finished = true");

    }
}
