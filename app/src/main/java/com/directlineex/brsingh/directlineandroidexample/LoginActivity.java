package com.directlineex.brsingh.directlineandroidexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends BaseActivity {

    private Button loginBtn,joinBtn;
    private EditText useridtv, userpwtv;
    private DatabaseReference databaseReference;
    private boolean isuser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isuser=false;

        setLayout();
        setButtonListener();
    }

    private void setLayout(){
        loginBtn=(Button)findViewById(R.id.loginBtn);
        joinBtn=(Button)findViewById(R.id.joinBtn);
        useridtv=(EditText)findViewById(R.id.userid);
        userpwtv=(EditText)findViewById(R.id.userpw);
    }

    private void setButtonListener(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser(useridtv.getText().toString(), userpwtv.getText().toString());
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,MemberJoinActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkUser(final String userid, final String userpw){
        databaseReference= FirebaseDatabase.getInstance().getReference("Member").child(userid);
        isuser=false;
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String str=dataSnapshot.getValue().toString();
                if(dataSnapshot.getKey().equals("password")){
                    if(str.equals(userpw)){
                        isuser=true;
                        userChecked();
                    }
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
        return isuser;
    }

    private void userChecked(){
        if(isuser){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("userid",useridtv.getText().toString());
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"다시 확인해 주세요",Toast.LENGTH_LONG).show();
        }
    }
}
