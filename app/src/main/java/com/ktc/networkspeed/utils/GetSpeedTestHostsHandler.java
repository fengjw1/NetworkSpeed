package com.ktc.networkspeed.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class GetSpeedTestHostsHandler extends Thread {

    HashMap<Integer, String> mapKey = new HashMap<>();
    HashMap<Integer, List<String>> mapValue = new HashMap<>();
    double selfLat = 0.0; //latitude
    double selfLon = 0.0; //longitude
    boolean finished = false;
    boolean pingValue = true;
    public HashMap<Integer, String> getMapKey() {
        return mapKey;
    }

    public HashMap<Integer, List<String>> getMapValue() {
        return mapValue;
    }

    public double getSelfLat(){
        return selfLat;
    }

    public double getSelfLon(){
        return selfLon;
    }

    public boolean isFinished(){
        return finished;
    }

    public boolean isConnected(){
        Log.d("fengjw", "isConnected : " + pingValue);
        return pingValue;
    }

    @Override
    public void run() {
        if (pingTest()) {
            pingValue = true;
            getConfigValue();
            getServerValue();
        }else {
            pingValue = false;
        }
    }

    /**
     * get latitude and longitude
     */
    private void getConfigValue(){
        try {
            URL url = new URL(Constants.URL_CONFIG);
            InputStream is = url.openStream();

            /**
             //<settings>
             //<client ip="113.89.7.242"
             // lat="22.5333" lon="114.1333"
             // isp="China Telecom Guangdong" isprating="3.7"
             // rating="0" ispdlavg="0" ispulavg="0"
             // loggedin="0" country="CN"/>
             */

            int ptr = 0;
            StringBuffer buffer = new StringBuffer();
            while ( (ptr = is.read()) != -1){
                buffer.append((char)ptr);
                if (!buffer.toString().contains("isp=")){
                    continue;
                }
                selfLat = Double.parseDouble(buffer.toString().split("lat=\"")[1].split(" ")[0]
                        .replace("\"", ""));
                selfLon = Double.parseDouble(buffer.toString().split("lon=\"")[1].split(" ")[0]
                        .replace("\"", ""));
                break;
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d("fengjw", "Exception : " + e.getMessage());
            return;
        }
    }

    /**
     * get best server
     */
    private void getServerValue(){
        String uploadAddress = "";
        String name = "";
        String country = "";
        String cc = "";
        String sponsor = "";
        String lat = "";
        String lon = "";
        String host = "";

        int count = 0;

        /**
         * <server url="http://speedtest4.xj.chinamobile.com/speedtest/upload.aspx"
         * lat="81.3241"
         * lon="43.9168"
         * name="Yili"
         * country="China"
         * cc="CN"
         * sponsor="China Mobile Group XinJiang"
         * id="17228"
         * host="speedtest4.xj.chinamobile.com:8080"/>
         */
        try {
            URL url = new URL(Constants.URL_SERVER);
            InputStream is = url.openStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ( (line = br.readLine()) != null){
                if (line.contains("<server url")){
                    uploadAddress = line.split("server url=\"")[1].split("\"")[0];
                    lat = line.split("lat=\"")[1].split("\"")[0];
                    lon = line.split("lon=\"")[1].split("\"")[0];
                    name = line.split("name=\"")[1].split("\"")[0];
                    country = line.split("country=\"")[1].split("\"")[0];
                    cc = line.split("cc=\"")[1].split("\"")[0];
                    sponsor = line.split("sponsor=\"")[1].split("\"")[0];
                    host = line.split("host=\"")[1].split("\"")[0];

                    List<String> ls = Arrays.asList(lat, lon, name, country, cc, sponsor, host);//only read
                    mapKey.put(count, uploadAddress);
                    mapValue.put(count, ls);
                    count ++;
                }
            }

            is.close();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        finished = true;
    }

    /**
     * ping
     */
    //ping
    private boolean pingTest(){

        Process p = null;
        try{
            p = Runtime.getRuntime().exec("/system/bin/ping -c 6 "+"www.speedtest.net");
            Log.d("fengjw", "p.waitFor() : " + p.waitFor());
            if (p.waitFor() == 0){
                return true;
            }else {
                Log.d("fengjw", "ping else ------");
                return false;
            }
        }catch(IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return false;
    }

}
