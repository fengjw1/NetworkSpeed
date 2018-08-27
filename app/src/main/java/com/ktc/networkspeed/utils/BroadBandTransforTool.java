package com.ktc.networkspeed.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.ktc.networkspeed.R;

public class BroadBandTransforTool {

    private static final float DEFAULT_STANDARD_DEFINITION = 4.0f;
    private static final float DEFAULT_HIGH_DEFINITION = 20.0f;

    //speed state


    public static String mbpsTransforMB(Context context, float Mbps){
        if ( 0 < Mbps && Mbps < DEFAULT_STANDARD_DEFINITION){
            return context.getResources().getString(R.string.sd_video);
        }else if (Mbps > DEFAULT_STANDARD_DEFINITION && Mbps < DEFAULT_HIGH_DEFINITION){
            return context.getResources().getString(R.string.hd_video);
        }else if (Mbps > DEFAULT_HIGH_DEFINITION){
            return context.getResources().getString(R.string.fhd_video);
        }else {
            return null;
        }
    }

    public static Drawable returnIntState(Context context, float Mbps){
        if ( 0 < Mbps && Mbps < DEFAULT_STANDARD_DEFINITION){
            return context.getResources().getDrawable(R.drawable.bicycle);
        }else if (Mbps > DEFAULT_STANDARD_DEFINITION && Mbps < DEFAULT_HIGH_DEFINITION){
            return context.getResources().getDrawable(R.drawable.car);
        }else if (Mbps > DEFAULT_HIGH_DEFINITION){
            return context.getResources().getDrawable(R.drawable.rocket);
        }else {
            return null;
        }
    }

    public static int returnTag(Context context, float Mbps){
        if ( 0 < Mbps && Mbps < DEFAULT_STANDARD_DEFINITION){
            return 1;
        }else if (Mbps > DEFAULT_STANDARD_DEFINITION && Mbps < DEFAULT_HIGH_DEFINITION){
            return 2;
        }else if (Mbps > DEFAULT_HIGH_DEFINITION){
            return 3;
        }else {
            return 0;
        }
    }

}
