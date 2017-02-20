package com.yixia.camera.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.yixia.camera.game.views.GuaGuaKa;

public class GuagualeActivity extends AppCompatActivity {

    private GuaGuaKa mGuaguaKa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guaguale);

        initView();
    }

    private void initView() {
        mGuaguaKa = (GuaGuaKa) findViewById(R.id.guaguaka);
        mGuaguaKa.setmListener(new GuaGuaKa.OnGuaguaKaCompleteListener() {
            @Override
            public void complete() {
                Toast.makeText(getApplicationContext(),"用户已经刮的差不多了",Toast.LENGTH_SHORT).show();
            }
        });
        mGuaguaKa.setText("你好，lxm");
    }
}
