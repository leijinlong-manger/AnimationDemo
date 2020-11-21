package com.lei.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 发送礼物的动画
 */

public class GiftView extends RelativeLayout {
    private AnimaMessage animaMessage;
    private static TranslateAnimation giftLaoutInAnima;//礼物飞进的动画
    //icon缩放的动画
    private static ScaleAnimation iconScaleAnima;
    //用户名飞进的动画
    private static TranslateAnimation userNameIn;
    //礼物名飞进的动画
    private static TranslateAnimation giftNameIn;

    //java里
    public GiftView(Context context, AnimaMessage message) {
        super(context);
        animaMessage = message;
        init();
    }

    //Xml里的
    public GiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //共用的
    public GiftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        giftLaoutInAnima = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(),
                R.anim.gift_in);
        iconScaleAnima = (ScaleAnimation) AnimationUtils.loadAnimation(getContext(),
                R.anim.icon_scale);
        userNameIn = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(),
                R.anim.gift_username_in);
        giftNameIn = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(),
                R.anim.gift_name_in);
        LayoutInflater.from(getContext()).inflate(R.layout.item_gift_anima, this);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);

        final RelativeLayout giftTextLayout = (RelativeLayout) findViewById(R.id.rlparent);
        final TextView sendUser = (TextView) findViewById(R.id.send_user);
        final TextView giftName = (TextView) findViewById(R.id.gift_name);
        ImageView userIcon = (ImageView) findViewById(R.id.user_icon);
        final ImageView giftIcon = (ImageView) findViewById(R.id.ivGift);
        final TextView giftNumView = (TextView) findViewById(R.id.giftNum);

        giftName.setText(String.format(getContext().getResources().getString(R.string.gift_tip),
                animaMessage.getGiftName()));
        sendUser.setText(animaMessage.getUserName());
        giftIcon.setImageResource(R.mipmap.live_red_packet);

        userIcon.startAnimation(iconScaleAnima);
        giftNumView.setTag(1);
        animaMessage.setUpdateTime(System.currentTimeMillis());//设置时间标记
        setTag(animaMessage);
        iconScaleAnima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sendUser.setVisibility(VISIBLE);
                giftName.setVisibility(VISIBLE);
                giftTextLayout.setVisibility(VISIBLE);
                giftTextLayout.startAnimation(giftLaoutInAnima);//开始执行显示礼物的动画
                sendUser.startAnimation(userNameIn);
                giftName.startAnimation(giftNameIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        giftLaoutInAnima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                giftNumView.setVisibility(VISIBLE);
                giftIcon.setVisibility(VISIBLE);
                giftNumView.setText("x" + giftNumView.getTag());
                startComboAnim(giftNumView);//设置一开始的连击事件
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void startComboAnim(final TextView giftNumView) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(giftNumView, "scaleX", 1.8f, 1.0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(giftNumView, "scaleY", 1.8f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.playTogether(animator1, animator2);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ((AnimaMessage) getTag()).setUpdateTime(System.currentTimeMillis());//设置时间标记
                // 为了让新添加的礼物可以正常的计时消失
                giftNumView.setTag((Integer) giftNumView.getTag() + 1);
                //如果当前显示中的礼物数量L<= AnimaMessage中的数量，继续触发Combo动画
                if ((Integer) giftNumView.getTag() <= ((AnimaMessage) getTag()).getNum()) {
                    giftNumView.setText("x" + giftNumView.getTag());
                    startComboAnim(giftNumView);
                } else {
                    ((AnimaMessage) getTag()).setComboAnimationOver(true);
                    return;
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
