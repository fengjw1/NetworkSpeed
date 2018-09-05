package com.ktc.networkspeed.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.ktc.networkspeed.R;

public class AnimUtils {

    private static AlphaAnimation mHideAnimation;
    private static AlphaAnimation mShowAnimation;

    //
    private static TranslateAnimation sTranslateAnimation;
    private static AlphaAnimation sAlphaAnimation;
    private static AnimatorSet sAnimatorSet;

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

    public static void setStartTranslate(final View view, int duration, int tag){
        view.setVisibility(View.VISIBLE);
        ObjectAnimator animator, animator1;
        if (tag < 3) {
            animator = ObjectAnimator.ofFloat(view, "TranslationX", -400, 0);
            animator1 = ObjectAnimator.ofFloat(view, "TranslationY", 0, 0);
        }else {
            animator = ObjectAnimator.ofFloat(view, "TranslationX", 0, 0);
            animator1 = ObjectAnimator.ofFloat(view, "TranslationY", 800, 0);
        }
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        sAnimatorSet = new AnimatorSet();
        sAnimatorSet.playTogether(animator, animator1, animator2);
        sAnimatorSet.setDuration(duration);
        sAnimatorSet.start();
    }

    public static void setScaleAnimation(final View[] views, int duration, Context context){
        ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(context, R.anim.scale);
        scaleAnimation.setDuration(duration);
        for (int i = 0; i < views.length; i ++){
            views[i].startAnimation(scaleAnimation);
        }
    }

}
