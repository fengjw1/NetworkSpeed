package com.ktc.networkdiagnose.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class PingModel extends Thread {

    HashMap<String, Object> result = new HashMap<>();
    String server = "";
    int count;
    double instantRtt = 0;
    double avgRtt = 0.0;
    boolean finished = false;
    boolean started = false;

    public PingModel(String serverIpAddress, int pingTryCount){
        this.server = serverIpAddress;
        this.count = pingTryCount;
        Log.d("fengjw", "server : " + server + " count : " + count);
    }

    public double getInstantRtt() {
        return instantRtt;
    }

    public double getAvgRtt() {
        return avgRtt;
    }

    public boolean isFinished() {
        Log.d("fengjw", "isFinished : " + finished);
        return finished;
    }

    @Override
    public void run() {
        try {
            ProcessBuilder ps = new ProcessBuilder("ping", "-c " + count, this.server);
            ps.redirectErrorStream(true);
            Process pr = ps.start();//cmd order

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            Log.d("fengjw", "in : " + in.toString());
            String line = "";
            String output = "";

            while ((line = in.readLine()) != null){
                if (line.contains("icmp_seq")){
                    instantRtt = Double.parseDouble(line.split(" ")
                            [line.split(" ").length - 2].replace("time=", ""));
                }
                if (line.startsWith("rtt ")){
                    avgRtt = Double.parseDouble(line.split("/")[4]);
                    break;
                }
            }
            pr.waitFor();
            in.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        finished = true;
    }
}
