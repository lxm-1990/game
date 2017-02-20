package com.yixia.camera.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yixia.camera.game.views.WuziqiView;

public class WuziqiAcitivity extends AppCompatActivity implements View.OnClickListener{

    private WuziqiView mWuziqiView;
    private Button mBtnRestart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wuziqi);

        initView();
    }

    private void initView() {
        mWuziqiView = (WuziqiView) findViewById(R.id.wuziqi_view);
        mWuziqiView.setmGameOver(new WuziqiView.GameListener() {
            @Override
            public void gameOver() {
                mBtnRestart.setVisibility(View.VISIBLE);
            }
        });
        mBtnRestart = (Button) findViewById(R.id.re_start);
        mBtnRestart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.re_start:
                view.setVisibility(View.GONE);
                mWuziqiView.reStart();
                break;
        }
    }
}
