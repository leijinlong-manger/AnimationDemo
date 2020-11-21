package com.lei.animationdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 停留3秒后消失
 * 礼物同时只展示3个,为防止信息系丢失,需要做一个消息队列缓存这些消息,定时便利消息队列
 * 数字连击的动画,进行追加,礼物正处于连击中,或者连击完成还没有消失,用户又送了一次礼物,还要做一个追加
 */
public class AnimationManager {
    //礼物飞出的动画
    private static Animation giftOutAnim;

    private static Context mcontext;

    //礼物定时器,用于清除和礼物队列中取礼物
    private static Timer giftClearTimer;

    //礼物队列
    private static ArrayList<AnimaMessage> giftList = new ArrayList<>();

    //礼物容器
    private static LinearLayout animViewContainer;

    //礼物定时器执行的间隔
    private static final int GIFT_CLEAR_TIMER_INTERVAL = 1500;

    //礼物无更新后存在的时间
    private static final int GIFT_CLEAR_INTERVAL = 3000;

    //同时存在放的最大礼物数目
    private static final int GIFT_MAX_NUMBER = 3;

    /**
     * init动画和context
     * @param context
     */
    public static void init(Context context){
        mcontext = context;
        giftOutAnim = AnimationUtils.loadAnimation(context,R.anim.gift_out);

    }

    /**
     * 添加礼物的container layout
     * @param linearLayout
     */
    public static void addGiftContainer(LinearLayout linearLayout){
        if (linearLayout==null||linearLayout.getOrientation()==LinearLayout.HORIZONTAL){
            return;
        }
        animViewContainer = linearLayout;
    }

    /**
     * 将动画信息添加到动画队列
     * @param animaMessage
     */
    public static void addAnimalMessage(AnimaMessage animaMessage){
        if (animaMessage!=null){
            giftList.add(animaMessage);
            if (giftClearTimer==null&&animViewContainer!=null&&mcontext!=null){
                startTimer();
            }
        }
    }

    /**
     * 添加动画View
     * @param animaMessage
     * @return
     */
    private static View addAnimalView(AnimaMessage animaMessage){
        return new GiftView(mcontext,animaMessage);
    }

    private static void removeAnimalView(final int index){
        //如果index超出LinearLayout子View的数量,不继续执行
        if (index>= animViewContainer.getChildCount()){
            return;
        }
        final View removeView = animViewContainer.getChildAt(index);
        giftOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().post(new Runnable(){
                    @Override
                    public void run() {
                        //根据index删除当前礼物View
                        animViewContainer.removeViewAt(index);
                    }
                });
                if (giftList.size()==0&&animViewContainer.getChildCount()==0&&giftClearTimer!=null){
                    giftClearTimer.cancel();
                    giftClearTimer=null;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ((Activity)mcontext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeView.startAnimation(giftOutAnim);
            }
        });
    }

    /**
     * 定时清除礼物
     * 使用list作为消息队列,使用定时器来定时清除当前已经展示完毕的view,并从消息队列中取出消息，添加新的view
     * uptime是指这个view最后的活跃时间的间隔,他需要不断的更新
     * 但是更新的频率不得小于定时器的间隔,否则会被当做已经展示完毕的view被清除掉
     */
    private static void startTimer() {
        giftClearTimer = new Timer();
        giftClearTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                int count = animViewContainer.getChildCount();
                Log.e("ljl", "run: "+count );
                //清除礼物
                for (int i = 0; i < count; i++) {
                    View view = animViewContainer.getChildAt(i);
                    AnimaMessage message = (AnimaMessage) view.getTag();
                    long nowTime = System.currentTimeMillis();
                    long upTime = message.getUpdateTime();
                    if ((nowTime-upTime)>=GIFT_CLEAR_INTERVAL){
                        removeAnimalView(i);
                        return;
                    }
                }

                //添加礼物
                if (count<GIFT_MAX_NUMBER){
                    if (giftList.size()>0){
                        ((Activity)mcontext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startGiftAnim(giftList.get(0));
                                giftList.remove(0);
                            }
                        });
                    }
                }
            }
        },0,GIFT_CLEAR_TIMER_INTERVAL);
    }

    /**
     * 通过message,可以找到队列当中相同用户名送的相同礼物,以方便追加礼物数量暴击效果
     * @param message
     * @return
     */
    private static View findViewByMessage(AnimaMessage message){
        for (int i = 0; i < animViewContainer.getChildCount(); i++) {
            AnimaMessage giftMessage = (AnimaMessage) animViewContainer.getChildAt(i).getTag();
            if (giftMessage.getUserName().equals(message.getUserName())&&
                    giftMessage.getGiftName().equals(message.getGiftName())){
                return animViewContainer.getChildAt(i);
            }
        }
        return null;
    }

    /**
     * 显示礼物的方法
     * 需要先检查后面的礼物消息在当前展示中是否有对应的view存在
     * 如果没有,直接重建,如果已经存在,则需要更新AnimaMessage中的礼物数量,用来达到追加的目的
     * @param giftMessage
     */
    private static void startGiftAnim(AnimaMessage giftMessage) {
        View giftView = findViewByMessage(giftMessage);
        if (giftView==null){//该用户不在礼物显示的列表内,或者又送了一个新的礼物
            giftView = addAnimalView(giftMessage);
            animViewContainer.addView(giftView);//将礼物添加到layout中
            animViewContainer.invalidate();//该方法会引起View树的重绘
        }else {
            //该用户在里屋显示列表,1,连击情况还没有结束，只需要更新message即可
            AnimaMessage message = (AnimaMessage) giftView.getTag();//原来礼物的view的信息
            message.setNum(message.getNum()+giftMessage.getNum());//合并礼物数量
            giftView.setTag(message);
            if (message.isComboAnimationOver()){
                //连击动画已经完成,此View还未消失,除了1的操作之外,还需要重启连击动画
                TextView giftNum = giftView.findViewById(R.id.giftNum);
                giftNum.setText("x"+giftNum.getTag());
                ((GiftView)giftView).startComboAnim(giftNum);
            }
        }
    }

    /**
     * 释放资源
     */
    public static  void release(){
        if (giftClearTimer != null){
            giftClearTimer.cancel();
            giftClearTimer = null;
        }
        giftList.clear();
        animViewContainer.removeAllViews();
        giftOutAnim = null;
        mcontext = null;
    }

}