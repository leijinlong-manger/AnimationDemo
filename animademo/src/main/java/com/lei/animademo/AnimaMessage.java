package com.lei.animademo;

public class AnimaMessage {
    //发送礼物的用户名
    private String UserName;
    //发送礼物的数量
    private int num;
    //发送礼物的礼物名
    private String GiftName;
    //判断当前礼物连击动画是否完成
    private boolean isComboAnimationOver;
    //当前礼物的展示时间,用来对比礼物是否3秒消失
    private long updateTime;

    public AnimaMessage(String userName, int num, String giftName) {
        UserName = userName;
        this.num = num;
        GiftName = giftName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getGiftName() {
        return GiftName;
    }

    public void setGiftName(String giftName) {
        GiftName = giftName;
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
