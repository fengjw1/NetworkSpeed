package com.ktc.networkspeed.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AnimUtils {

    private static AlphaAnimation mHideAnimation;
    private static AlphaAnimation mShowAnimation;

    public static void setHideAnimation(final View[] views, int duration){
        if (views.length == 0 || duration < 0){
            return;
        }

        if (null != mHideAnimation){
            mHideAnimation.cancel();
        }

        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                for (int i = 0; i < views.length; i ++) {
                    views[i].setVisibility(View.GONE);
                }
            }
        });
        for (int i = 0; i < views.length; i ++) {
            views[i].startAnimation(mHideAnimation);
        }
    }

    public static void setStartAnimation(final View[] views, int duration){
        if (views == null || duration < 0){
            return;
        }

        if (null != mShowAnimation){
            mShowAnimation.cancel();
        }

        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        mShowAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                for (int i = 0; i < views.length; i ++) {
                    views[i].setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        for (int i = 0; i < views.length; i ++) {
            views[i].startAnimation(mShowAnimation);
        }

    }

}
