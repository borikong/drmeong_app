package com.directlineex.brsingh.directlineandroidexample;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.FileOutputStream;

public class MyPageActivity extends BaseActivity{

    //지수선언
    private LinearLayout myPageBtn,goChatBtn, settingBtn,recordBtn;
    private TabHost tabHost;
    private FrameLayout frameLayout;
    private TextView menu_text,text_id;
    private ImageView setting_img;
    private Button editProfileBtn;

    //다영선언
    private TextView userid, username, userage, useremail,mainuserid;
    private TextView usergender,profileid;
    private RadioButton xxbtn, xybtn;
    private Button logBtn;
    private DatabaseReference databaseReference;

    @Override
    public void onBackPressed() {
        //뒤로 버튼 누르면 MainActivity로
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("userid",getUSER_ID());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        setTitle("마이페이지");

        isLogOn();
        setLayout();
        setMyProfileLayout();
        setMyPageButtonListener();
        setButtonListener();
        init();
        loginSetting();
        getUserData();
    }

    void init(){
        frameLayout = (FrameLayout) findViewById(android.R.id.tabcontent);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String number) {
                switch (number) {

                    case "1":
                        frameLayout.setBackgroundColor(Color.parseColor("#eaeaea"));
                        menu_text.setText("내 정보");
                        break;

                    case "2":
                        frameLayout.setBackgroundColor(Color.parseColor("#eaeaea"));
                        menu_text.setText("강아지 정보");
                        break;
                }
            }
        });
    }

    private void setLayout(){
        menu_text = (TextView)findViewById(R.id.menu_text);

        tabHost = (TabHost) findViewById(R.id.tabhost1);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("1").setContent(R.id.tab1).setIndicator("내 정보");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("2").setContent(R.id.tab2).setIndicator("강아지 정보");

        tabHost.addTab(tab1);
        tabHost.addTab(tab3);
        goChatBtn = (LinearLayout)findViewById(R.id.gochatBtn);
        settingBtn = (LinearLayout)findViewById(R.id.settingBtn);
        recordBtn = (LinearLayout)findViewById(R.id.recordBtn);
        myPageBtn = (LinearLayout)findViewById(R.id.myPageBtn);
        logBtn=(Button)findViewById(R.id.logBtn);
        profileid=(TextView)findViewById(R.id.profileid);
    }

    //마이프로필 레이아웃
    private void setMyProfileLayout(){
        editProfileBtn=(Button) findViewById(R.id.editProfileBtn);
        userid=(TextView)findViewById(R.id.userid);
        username=(TextView)findViewById(R.id.username);
        userage=(TextView)findViewById(R.id.userage);

        usergender=(TextView)findViewById(R.id.usergender);
        useremail=(TextView)findViewById(R.id.useremail);
        mainuserid=(TextView)findViewById(R.id.mainuserid);
    }

    private void setButtonListener(){
        goChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("userid",getUSER_ID());
                startActivity(intent);
            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileSettingActivity.class);
                intent.putExtra("userid",getUSER_ID());
                startActivity(intent);
            }
        });
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        myPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyPageActivity.class);
                intent.putExtra("userid",getUSER_ID());
                startActivity(intent);
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getUSER_ID()==null){
                    Intent intent=new Intent(MyPageActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    setUSER_ID(null);
                    Intent intent=new Intent(MyPageActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setMyPageButtonListener(){
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyPageActivity.this,ProfileSettingActivity.class);
                intent.putExtra("userid",getUSER_ID());
                startActivity(intent);
            }
        });
    }

    private void loginSetting(){
        if(getUSER_ID()==null){
            Intent intent=new Intent(MyPageActivity.this,LoginActivity.class);
            startActivity(intent);
        }else{
            profileid.setText(getUSER_ID());
            logBtn.setText("로그아웃");
        }
    }

    private void getUserData(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Member").child(getUSER_ID());
        userid.setText(getUSER_ID());
        mainuserid.setText(getUSER_ID());

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String str=dataSnapshot.getValue().toString();

                if(dataSnapshot.getKey().equals("name")){
                    username.setText(str);
                }else if(dataSnapshot.getKey().equals("age")){
                    userage.setText(str);
                }else if(dataSnapshot.getKey().equals("gender")){
                    usergender.setText(str);
                }else if(dataSnapshot.getKey().equals("email")){
                    useremail.setText(str);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void isLogOn(){
        if(getIntent().getStringExtra("userid")!=null){
            setUSER_ID(getIntent().getStringExtra("userid"));
        }else{
            setUSER_ID(null);
            Intent intent=new Intent(MyPageActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
}