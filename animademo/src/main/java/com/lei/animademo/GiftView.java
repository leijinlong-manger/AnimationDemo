package com.lei.animademo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class GiftView extends RelativeLayout {
    private AnimaMessage animaMessage;

    public GiftView(Context context,AnimaMessage message) {
        super(context);
        animaMessage = message;
    }

    public GiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GiftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
