package com.yixia.camera.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mButton;
    private Button mBtnGuaGuaKa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.btn_wuziqi);
        mButton.setOnClickListener(this);
        mBtnGuaGuaKa = (Button) findViewById(R.id.btn_guaguale);
        mBtnGuaGuaKa.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_wuziqi:
                Intent intent = new Intent(this,WuziqiAcitivity.class);
                startActivity(intent);
                break;
            case R.id.btn_guaguale:
                Intent intent1 = new Intent(this,GuagualeActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
