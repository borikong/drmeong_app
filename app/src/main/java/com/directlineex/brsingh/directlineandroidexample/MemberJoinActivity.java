package com.directlineex.brsingh.directlineandroidexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemberJoinActivity extends BaseActivity {

    private Button joinBtn;
    private EditText userid, userpw,username, userage, useremail;
    private RadioGroup usergender;
    private RadioButton xxBtn,xyBtn;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memberjoin);
        setLayout();
        setButtonListner();

    }

    private void setLayout(){
        joinBtn=(Button)findViewById(R.id.joinBtn);
        userid=(EditText)findViewById(R.id.userid);
        userpw=(EditText)findViewById(R.id.userpw);
        username=(EditText)findViewById(R.id.username);
        userage=(EditText)findViewById(R.id.userage);
        usergender=(RadioGroup)findViewById(R.id.genderRadioGroup);
        useremail=(EditText)findViewById(R.id.useremail);
        xxBtn=(RadioButton)findViewById(R.id.xx);
        xyBtn=(RadioButton)findViewById(R.id.xy);
    }

    private void setButtonListner(){
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id, password, name,age, gender, email;
                if(usergender.getCheckedRadioButtonId()==xxBtn.getId()){
                    gender="여";
                }else{
                    gender="남";
                }

                id=userid.getText().toString();
                password=userpw.getText().toString();
                name=username.getText().toString();
                age=userage.getText().toString();
                email=useremail.getText().toString();

                writeNewUser(id,password,name,age,gender,email);
            }
        });
    }

    private void writeNewUser(String id, String password, String name, String age, String gender, String email){
        databaseReference= FirebaseDatabase.getInstance().getReference("Member");
        UserDataItem item=new UserDataItem(password,name,age,gender,email);

        databaseReference.child(id).setValue(item);

        Toast.makeText(getApplicationContext(),"회원가입 성공",Toast.LENGTH_LONG).show();

        Intent intent=new Intent(MemberJoinActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
