package com.directlineex.brsingh.directlineandroidexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by DS on 2018-08-15.
 */

public class MainActivity extends BaseActivity {

    private LinearLayout myPageBtn,goChatBtn, settingBtn,recordBtn;
    private Button startBtn,logBtn;
    private TextView profileid;
    AlertDialog.Builder alert;
    AlertDialog alertDialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLogOn();
        setLayout();
        setButtonListener();
        loginSetting();
    }

    private void setLayout(){
        startBtn = (Button)findViewById(R.id.startBtn);
        goChatBtn = (LinearLayout)findViewById(R.id.gochatBtn);
        settingBtn = (LinearLayout)findViewById(R.id.settingBtn);
        recordBtn = (LinearLayout)findViewById(R.id.recordBtn);
        myPageBtn = (LinearLayout)findViewById(R.id.myPageBtn);
        logBtn=(Button)findViewById(R.id.logBtn);
        profileid=(TextView)findViewById(R.id.profileid);
    }

    private void setButtonListener(){
        goChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                if(getUSER_ID()!=null){
                    intent.putExtra("userid",getUSER_ID());
                }else{
                }
                startActivity(intent);
            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileSettingActivity.class);
                if(getUSER_ID()!=null){
                    intent.putExtra("userid",getUSER_ID());
                }else{
                }
                startActivity(intent);
            }
        });
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RecordActivity.class);
                if(getUSER_ID()!=null){
                    intent.putExtra("userid",getUSER_ID());
                }else{
                }
                startActivity(intent);
            }
        });
        myPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getUSER_ID()!=null){
                    Intent intent = new Intent(getApplicationContext(),MyPageActivity.class);
                    intent.putExtra("userid",getUSER_ID());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"로그인이 필요한 서비스입니다.",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        //시작하기 버튼 누르면 채팅창으로
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                if(getUSER_ID()!=null){
                    intent.putExtra("userid",getUSER_ID());
                }else{
                }
                startActivity(intent);
            }
        });
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getUSER_ID()==null){
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    setUSER_ID(null);
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void loginSetting(){
        if(getUSER_ID()==null){
            logBtn.setText("로그인");
        }else{
            logBtn.setText("로그아웃");
            profileid.setText(getUSER_ID());
        }
    }

    private void isLogOn(){
        if(getIntent().getStringExtra("userid")!=null){
            setUSER_ID(getIntent().getStringExtra("userid"));
            Log.d("사용자 아이디",getUSER_ID());
        }else{
            setUSER_ID(null);
        }
    }
}

