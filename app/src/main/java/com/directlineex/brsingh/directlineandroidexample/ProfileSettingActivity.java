package com.directlineex.brsingh.directlineandroidexample;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public class ProfileSettingActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout myPageBtn,goChatBtn, settingBtn,recordBtn;TextView changeImageBtn;
    private ImageView userImage;
    Button confirmBtn;
    //카메라때문에 추가한 것
    private static final int PICK_FROM_CAMERA =0;
    private static final int PICK_FROM_ALBUM =1;
    private static final int CROP_FROM_iMAGE =2;
    private Uri mImageCaptureUri;
    private String absolutePath;
    private int id_view;

    //다영선언
    private EditText userid, username, userage, useremail,userpw;
    private RadioGroup usergender;
    private RadioButton xxbtn, xybtn;
    private Button editbtn, backbtn;
    private DatabaseReference databaseReference;
    private String password;

    @Override
    public void onBackPressed() {
        //뒤로 버튼 누르면 MyPageActivity로
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),MyPageActivity.class);
        intent.putExtra("userid",getUSER_ID());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
   //     changeImageBtn = (TextView)findViewById(R.id.changeImageBtn);
   //     userImage = (ImageView)findViewById(R.id.userImage);
       // changeImageBtn.setOnClickListener(this);

        goChatBtn = (LinearLayout)findViewById(R.id.gochatBtn);
        settingBtn = (LinearLayout)findViewById(R.id.settingBtn);
        recordBtn = (LinearLayout)findViewById(R.id.recordBtn);
        myPageBtn = (LinearLayout)findViewById(R.id.myPageBtn);
        goChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                startActivity(intent);
            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),ProfileSettingActivity.class);
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
                startActivity(intent);
            }
        });

        //다영
        setLayout();
        setButtonListener();
        isLogOn();
        getUserData();

    }

    @Override
    public void onClick(View view) {
        id_view = view.getId();
        if(true){//view.getId() == R.id.changeImageBtn){
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    doTakePhotoAction();
                }
            };
            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    doTakeAlbumAction();
                }
            };
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            };
            new AlertDialog.Builder(ProfileSettingActivity.this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("사진촬영",cameraListener)
                    .setNeutralButton("앨범선택",albumListener)
                    .setNegativeButton("취소",cancelListener)
                    .show();
        }
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //저장되어서 바뀔 내용 입력
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != RESULT_OK)
            return;
        switch (requestCode)
        {
            case PICK_FROM_ALBUM:
            {
                mImageCaptureUri = data.getData();
            }

            case PICK_FROM_CAMERA:
            {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri,"image/*");

                intent.putExtra("outputX",200);
                intent.putExtra("outputY",200);
                intent.putExtra("aspectX",1);
                intent.putExtra("aspectY",1);
                intent.putExtra("scale",true);
                intent.putExtra("return-data",true);
                startActivityForResult(intent,CROP_FROM_iMAGE);
                break;
            }

            case CROP_FROM_iMAGE:
            {
                if(resultCode !=RESULT_OK){
                    return; }
                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel/"+System.currentTimeMillis()+".jpg";
                if(extras !=null){
                    Bitmap photo = extras.getParcelable("data");
                    userImage.setImageBitmap(photo);

                    storeCropImage(photo,filePath);
                    absolutePath = filePath;
                    break;
                 }
                File f = new File(mImageCaptureUri.getPath());
                    if(f.exists()){
                        f.delete(); }
            }
        }
    }
    public void doTakePhotoAction()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
        startActivityForResult(intent,PICK_FROM_CAMERA);
    }
    public void doTakeAlbumAction()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }
    private  void storeCropImage(Bitmap bitmap, String filePath){

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel";
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists())
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out= new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(copyFile)));
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setLayout(){
        userid=(EditText)findViewById(R.id.userid);
        username=(EditText)findViewById(R.id.username);
        userage=(EditText)findViewById(R.id.userage);
        useremail=(EditText)findViewById(R.id.useremail);
        userpw=(EditText)findViewById(R.id.userpw);
        usergender=(RadioGroup)findViewById(R.id.genderRadioGroup);
        xxbtn=(RadioButton)findViewById(R.id.xx);
        xybtn=(RadioButton)findViewById(R.id.xy);
        editbtn=(Button)findViewById(R.id.editBtn);
        backbtn=(Button)findViewById(R.id.backBtn);

        editbtn.setText("수정하기");
        backbtn.setText("돌아가기");
    }

    private void setButtonListener(){
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyDatabase();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileSettingActivity.this,MyPageActivity.class);
                intent.putExtra("userid",getUSER_ID());
                startActivity(intent);
            }
        });
    }

    //데이터베이스에서 회원정보 불러오기
    private void getUserData(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Member").child(getUSER_ID());
        userid.setText(getUSER_ID());

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String str=dataSnapshot.getValue().toString();

                if(dataSnapshot.getKey().equals("name")){
                    username.setText(str);
                }else if(dataSnapshot.getKey().equals("age")){
                    userage.setText(str);
                }else if(dataSnapshot.getKey().equals("gender")){
                    if(str.equals("여")){
                        usergender.check(xxbtn.getId());
                    }else{
                        usergender.check(xybtn.getId());
                    }
                }else if(dataSnapshot.getKey().equals("email")){
                    useremail.setText(str);
                }else if(dataSnapshot.getKey().equals("password")){
                    password=str;
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

    //회원정보 데이터베이스 수정
    private void modifyDatabase(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Member");
        String gender=null;
        if(usergender.getCheckedRadioButtonId()==xxbtn.getId()){
            gender="여";
        }else if(usergender.getCheckedRadioButtonId()==xybtn.getId()){
            gender="남";
        }

        if(userpw.getText().toString()==null||userpw.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"비밀번호를 입력해 주세요",Toast.LENGTH_LONG).show();
        }else{
            if(userpw.getText().toString().equals(password)){
                UserDataItem item=new UserDataItem(password,username.getText().toString(),userage.getText().toString(),gender,useremail.getText().toString());
                databaseReference.child(getUSER_ID()).setValue(item);
                Toast.makeText(getApplicationContext(),"회원정보 수정 완료",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(ProfileSettingActivity.this, MyPageActivity.class);
                intent.putExtra("userid",getUSER_ID());
                startActivity(intent);
            }
        }
    }

    private void isLogOn(){
        if(getIntent().getStringExtra("userid")!=null){
            setUSER_ID(getIntent().getStringExtra("userid"));
        }else{
            setUSER_ID(null);
            Intent intent=new Intent(ProfileSettingActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
}
