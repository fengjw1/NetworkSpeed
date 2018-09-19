package com.ktc.tvhelper.networkspeed.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkState {

    private Context mContext;

    public NetworkState(Context context){
        mContext = context;
    }

    public boolean isNetworkConn(){
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info == null || !connectivityManager.getBackgroundDataSetting()){
            return false;
        }

        int netType = info.getType();
        int netSubtype = info.getSubtype();
        if (netType == ConnectivityManager.TYPE_WIFI){
            return info.isConnected();
        }else if (netType == ConnectivityManager.TYPE_MOBILE
                && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
                && !telephonyManager.isNetworkRoaming()){
            return info.isConnected();
        }else if (info != null && info.isAvailable()){
            return info.isAvailable();
        }else {
            return false;
        }

    }

}
