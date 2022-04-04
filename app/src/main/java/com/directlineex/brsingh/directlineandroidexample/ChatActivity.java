package com.directlineex.brsingh.directlineandroidexample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.directlineex.brsingh.directlineandroidexample.utils.AudioWriterPCM;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.naver.speech.clientapi.SpeechRecognitionResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.directlineex.brsingh.directlineandroidexample.R.layout.activity_chat;

/**
 * A Chat Screen Activity
 */
public class ChatActivity extends BaseActivity{
    private EditText messageET;
    private ListView messagesContainer;
    private Button sendBtn,speechBtn; //다영
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private String localToken = "";
    private String conversationId = "";
    private String primaryToken = "";
    private String botName = "";
    private TextView profileid;
    private Button logBtn;

    private String lastResponseMsgId = "";

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            pollBotResponses();
        }
    };

    private GoogleApiClient client;


    //다영 : Naver Clova STT API 전역변수
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CLIENT_ID = "2d7dvw2iy3";

    private RecognitionHandler stthandler;
    private NaverRecognizer naverRecognizer;

    private String mResult;

    private AudioWriterPCM writer;
    String database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_chat);
        initControls();

        primaryToken = getMetaData(getBaseContext(), "botPrimaryToken"); //manifest에 metadata로 등록해 놓음
        botName = getMetaData(getBaseContext(), "botName").toLowerCase();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //다영 : STT
        stthandler = new  RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, stthandler, CLIENT_ID);
        isLogOn();
        setSpeechBtn();
        setLayout();
        setButtonListener();
        loginSetting();
        runnable.run();
    }

    private void setLayout(){
        profileid=(TextView)findViewById(R.id.profileid);
        logBtn=(Button)findViewById(R.id.logBtn);
    }

    private void isLogOn() {
        if (getIntent().getStringExtra("userid") != null) {
            setUSER_ID(getIntent().getStringExtra("userid"));
        } else {
            setUSER_ID(null);
        }
    }

    private void loginSetting(){
        if(getUSER_ID()==null){
            logBtn.setText("로그인");
        }else{
            logBtn.setText("로그아웃");
            profileid.setText(getUSER_ID());
        }
    }

    private void setButtonListener(){
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getUSER_ID()==null){
                    Intent intent=new Intent(ChatActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    setUSER_ID(null);
                    Intent intent=new Intent(ChatActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void pollBotResponses() {
        //Toast.makeText(getBaseContext(),
        //       "test",
        //     Toast.LENGTH_SHORT).show();
        String botResponse = "";
        if (conversationId != "" && localToken != "") {
            botResponse = getBotResponse();
            if (botResponse != "") {
                try {
                    JSONObject jsonObject = new JSONObject(botResponse);
                    String responseMsg = "";
                    Integer arrayLength = jsonObject.getJSONArray("activities").length();
                    String msgFrom = jsonObject.getJSONArray("activities").getJSONObject(arrayLength - 1).getJSONObject("from").get("id").toString();
                    String curMsgId = jsonObject.getJSONArray("activities").getJSONObject(arrayLength - 1).get("id").toString();

                    // 수민 선언
                    JSONArray attachments, buttons;
                    Integer attachmentsLength;
                    JSONObject attach1, content;
                    String title, subTitle;
                    ArrayList<String> btnList = new ArrayList<String>();

                    if (msgFrom.trim().toLowerCase().equals(botName)) {

                        if (lastResponseMsgId == "") {
                            responseMsg = jsonObject.getJSONArray("activities").getJSONObject(arrayLength - 1).get("text").toString();
                            AddResponseToChat(responseMsg);
                            lastResponseMsgId = curMsgId;
                        } else if (!lastResponseMsgId.equals(curMsgId)) {

                            if (arrayLength > 2) {
                                for (int i = 1; i <= arrayLength - 1; i++) {
                                    if (arrayLength - 2 == i) {
                                        responseMsg = jsonObject.getJSONArray("activities").getJSONObject(arrayLength - 2).get("text").toString();
                                        AddResponseToChat(responseMsg);
                                        lastResponseMsgId = curMsgId;
                                    }
                                    else {
                                        responseMsg = "";
                                        attachments = jsonObject.getJSONArray("activities").getJSONObject(arrayLength - 1).getJSONArray("attachments");
                                        attachmentsLength = attachments.length();
                                        attach1 = attachments.getJSONObject(attachmentsLength - 1);
                                        content = attach1.getJSONObject("content");

                                        title = content.get("title").toString();
                                        subTitle = content.get("subtitle").toString();

//                                        responseMsg += title + "\n" + subTitle;

                                        buttons = content.getJSONArray("buttons");
                                        Integer buttonsLength = buttons.length();

                                        if (buttonsLength != 0) {
                                            for (int j = 0; j < buttonsLength; j++) {
                                                JSONObject btn = buttons.getJSONObject(j);
                                                String tt = btn.get("title").toString();

//                                                responseMsg += "\n" + tt;
                                                btnList.add(tt);
                                            }
                                        }
//                                        AddResponseToChat(btnList.toString()); // 수민
                                        AddResponseToChatByButton(btnList);
                                        lastResponseMsgId = curMsgId;
                                    }
                                }
//                                responseMsg = jsonObject.getJSONArray("activities").getJSONObject(arrayLength - 2).get("text").toString();
//                                AddResponseToChat(responseMsg);
//                                lastResponseMsgId = curMsgId;
                            }
                            else {
                                responseMsg = jsonObject.getJSONArray("activities").getJSONObject(arrayLength - 1).get("text").toString();
                                AddResponseToChat(responseMsg);
                                lastResponseMsgId = curMsgId;
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        handler.postDelayed(runnable, 1000 * 5);
    }


    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);

        TextView meLabel = (TextView) findViewById(R.id.meLbl);
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        companionLabel.setText("닥터멍"); // Hard Coded
        sayHelloToClient(); //챗봇이 안녕하세요 말함

        // 메세지 보내는 곳
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString(); // 이게 사용자가 친 내용
                if (TextUtils.isEmpty(messageText)) {
                    return;
                };

                // 이미 눌러진 후
                if (adapter.getStr() != "") {
                    Log.v("어댑터값", adapter.getStr());
                    messageText = adapter.getStr();
                }
                Log.v("messageText = ", messageText);

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);

                messageET.setText("");
                displayMessage(chatMessage);

                // 수민
                ChatAdapter.str = "";

                new Connection().execute(messageText); //메시지 챗봇에 보냄 AsynTask로 메인스레드 사용X
            }
        });
    }

    //Get the bot response by polling a GET to directline API
    private String getBotResponse() {
        //Only for demo sake, otherwise the network work should be done over an asyns task
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String UrlText = "https://directline.botframework.com/v3/directline/conversations/" + conversationId + "/activities";
        URL url = null;
        String responseValue = "";

        try {
            url = new URL(UrlText);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            String basicAuth = "Bearer " + localToken;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            int responseCode = urlConnection.getResponseCode(); //can call this instead of con.connect()
            if (responseCode >= 400 && responseCode <= 499) {
                throw new Exception("Bad authentication status: " + responseCode); //provide a more meaningful exception message
            } else {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseValue = readStream(in);
                Log.w("responseSendMsg ", responseValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return responseValue;
    }

    //sends the message by making it an activity to the bot
    private void sendMessageToBot(String messageText) {
        //Only for demo sake, otherwise the network work should be done over an asyns task
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String UrlText = "https://directline.botframework.com/v3/directline/conversations/" + conversationId + "/activities";
        URL url = null;

        try {
            url = new URL(UrlText);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            String basicAuth = "Bearer " + localToken;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", "message");
                jsonObject.put("text", messageText);
                jsonObject.put("from", (new JSONObject().put("id", "user1")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String postData = jsonObject.toString();

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Content-Length", "" + postData.getBytes().length);
            OutputStream out = urlConnection.getOutputStream();
            out.write(postData.getBytes());

            int responseCode = urlConnection.getResponseCode(); //can call this instead of con.connect()
            if (responseCode >= 400 && responseCode <= 499) {
                throw new Exception("Bad authentication status: " + responseCode); //provide a more meaningful exception message
            } else {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String responseValue = readStream(in);
                Log.w("responseSendMsg ", responseValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

    }


    //returns the conversationID
    private String startConversation() {
        //Only for demo sake, otherwise the network work should be done over an asyns task
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String UrlText = "https://directline.botframework.com/v3/directline/conversations";
        URL url = null;
        String responseValue = "";

        try {
            url = new URL(UrlText);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            String basicAuth = "Bearer " + primaryToken;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            responseValue = readStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }


        Log.v("startConversation 갸아", responseValue);
        return responseValue;
    }

    //read the chat bot response
    private String readStream(InputStream in) {
        char[] buf = new char[2048];
        Reader r = null;
        try {
            r = new InputStreamReader(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder s = new StringBuilder();
        while (true) {
            int n = 0;
            try {
                n = r.read(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (n < 0)
                break;
            s.append(buf, 0, n);
        }

        Log.w("streamValue", s.toString());
        return s.toString();
    }


    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void sayHelloToClient() {

        chatHistory = new ArrayList<ChatMessage>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("안녕하세요!");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);

        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter); //어댑터 설정

        for (int i = 0; i < chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
    }

    /*
    Add the bot response to chat window
     */
    private void AddResponseToChat(String botResponse) {
        ChatMessage message = new ChatMessage();
        //message.setId(2);
        message.setMe(false);
        message.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        message.setMessage(botResponse);
        displayMessage(message);
    }

    // 버튼 형식일 때 = 수민
    private void AddResponseToChatByButton(ArrayList btnMsgs) {
        ChatMessage message = new ChatMessage();
        //message.setId(2);
        message.setMe(false);
        message.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        message.setBtnMsgs(btnMsgs);
        displayMessage(message);
    }

    /*
    Get metadata from manifest file against a given key
     */
    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("Metadata", "Unable to load meta-data: " + e.getMessage());
        }
        return null;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Chat Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        naverRecognizer.getSpeechRecognizer().initialize();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        naverRecognizer.getSpeechRecognizer().release();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class Connection extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg0) {
            String conversationTokenInfo = startConversation(); // startConversation()을 하면 conversationId랑 token이 새로 생성됨
            JSONObject jsonObject = null;

            if (conversationTokenInfo != "") {
                try {
                    jsonObject = new JSONObject(conversationTokenInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //send message to bot and get the response using the api conversations/{conversationid}/activities
            if (jsonObject != null) {
                try {
                    conversationId = jsonObject.get("conversationId").toString();
                    localToken = jsonObject.get("token").toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (conversationId != "") {
                sendMessageToBot(arg0[0]);
            }

            return null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        mResult = "";
        messageET.setText("");
        speechBtn.setText("음성");
        speechBtn.setEnabled(true);
    }

    //다영 : STT관련 메서드
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                messageET.setText("연결중...");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
//                Object msgobj=msg.obj;
//
//                mResult = (String) (msg.obj);
//
//                messageET.setText(mResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
                List<String> results = speechRecognitionResult.getResults();
                StringBuilder strBuf = new StringBuilder();
//                for (String result : results) {
//                    strBuf.append(result);
//                    strBuf.append("\n");
//                }
//                mResult = strBuf.toString();

                mResult=results.get(0);
                messageET.setText(mResult);
                Log.d("여기죠기","여기죠기");
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                messageET.setText(mResult);
                speechBtn.setText("음성");
                speechBtn.setEnabled(true);
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                speechBtn.setText("음성");
                speechBtn.setEnabled(true);
                break;
        }
    }

    private void setSpeechBtn(){
        speechBtn=(Button)findViewById(R.id.speechbtn);

        //다영 : 음성 버튼 리스너
        speechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionRecord(); //녹음 권한(RECORD_AUDIO)확인
                if (!naverRecognizer.getSpeechRecognizer().isRunning()) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecongizer by calling recognize().
                    mResult = "";
                    messageET.setText("음성인식 연결중");
                    speechBtn.setText("중지");
                    naverRecognizer.recognize();
                } else {
                    Log.d(TAG, "stop and wait Final Result");
                    speechBtn.setEnabled(false);

                    naverRecognizer.getSpeechRecognizer().stop();
                }

            }
        });
    }

    //녹음 권한 확인
    public boolean requestPermissionRecord() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ChatActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    100);

        } else {
            return true;
        }

        return true;
    }

    static class RecognitionHandler extends Handler {
        private final WeakReference<ChatActivity> mActivity;

        RecognitionHandler(ChatActivity activity) {
            mActivity = new WeakReference<ChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ChatActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

}

