package com.ktc.tvhelper.networkspeed.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class MarqueeText extends AppCompatTextView {

    private boolean canFocused = false;

    public MarqueeText(Context context) {
        super(context);
    }

    public MarqueeText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean hasFocus() {
        return isCanFocused();
    }

    public boolean isCanFocused(){
        return canFocused;
    }

    public void setCanFocused(boolean canFocused){
        this.canFocused = canFocused;
    }

}
