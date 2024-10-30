package com.app.music.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import com.app.music.R;
import com.app.music.util.DBUtils;

public class MyActivity extends AppCompatActivity implements View.OnClickListener {

    //退出文本组件
    TextView logout_tv;
    //我的喜好
    TextView mylike_tv;
    TextView my_name_tv,my_email_tv,my_tel_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);



        initView();
        initData();
        initEvent();
    }

    private void initView(){
        logout_tv = findViewById(R.id.logout);
        mylike_tv = findViewById(R.id.mylike);
        my_name_tv =  findViewById(R.id.my_name);
        my_email_tv =  findViewById(R.id.my_email);
        my_tel_tv =  findViewById(R.id.my_tel);


        logout_tv.setOnClickListener(this);
        mylike_tv.setOnClickListener(this);
    }

    private void initData(){
        Map<String, String> userMap = DBUtils.readInfo();
        my_name_tv.setText(userMap.get("username"));
        my_email_tv.setText(userMap.get("email"));
        my_tel_tv.setText(userMap.get("tel"));

    }

    private void initEvent(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.logout:
                Intent intent=new Intent();
                intent.setClass(MyActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.mylike:
                Intent mylikeIntent=new Intent();
                mylikeIntent.setClass(MyActivity.this,MyLikeActivity.class);
                startActivity(mylikeIntent);
                break;
        }
    }
}