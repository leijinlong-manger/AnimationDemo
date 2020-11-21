package com.lei.animationdemo;

/**
 * 主要负责一条礼物的展示
 */

public class AnimaMessage {
    private String userName;//发送礼物的用户
    private int num;//发送礼物的数量
    private String giftName;//礼物的名字
    private boolean isComboAnimationOver;//判断当前礼物连击动画是否完成
    private long updateTime;//当前礼物展示的时间，用来对比礼物是否3秒内消失

    public AnimaMessage() {
    }

    public AnimaMessage(String userName, int num, String giftName) {
        this.userName = userName;
        this.num = num;
        this.giftName = giftName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public boolean isComboAnimationOver() {
        return isComboAnimationOver;
    }

    public void setComboAnimationOver(boolean comboAnimationOver) {
        isComboAnimationOver = comboAnimationOver;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
