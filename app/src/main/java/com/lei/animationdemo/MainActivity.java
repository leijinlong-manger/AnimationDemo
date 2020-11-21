package com.lei.animationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout giftContainer;
    private Button gift1;
    private Button gift2;
    private Button gift3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        AnimationManager.init(this);
        AnimationManager.addGiftContainer(giftContainer);

//        animaMessage = ;
    }

    private void initView() {
        giftContainer = (LinearLayout) findViewById(R.id.ll_gift_container);
        giftContainer.setOnClickListener(this);
        gift1 = (Button) findViewById(R.id.gift_1);
        gift1.setOnClickListener(this);
        gift2 = (Button) findViewById(R.id.gift_2);
        gift2.setOnClickListener(this);
        gift3 = (Button) findViewById(R.id.gift_3);
        gift3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gift_1:
                AnimationManager.addAnimalMessage(new AnimaMessage("雷金泷",100,"飞机"));
                break;
            case R.id.gift_2:
                AnimationManager.addAnimalMessage(new AnimaMessage("徐新荣",1000,"游轮"));
                break;
            case R.id.gift_3:
                AnimationManager.addAnimalMessage(new AnimaMessage("王八羔子",1,"火箭"));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnimationManager.release();
    }
}
