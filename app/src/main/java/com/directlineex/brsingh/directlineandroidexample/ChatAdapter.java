package com.directlineex.brsingh.directlineandroidexample;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brsingh on 1/6/2017.
 */
public class ChatAdapter extends BaseAdapter {

    private final List<ChatMessage> chatMessages;
    private Activity context;
    private ArrayList<String> array;
    private Chat_BtnListViewAdapter adapter;

    // 수민
    public static String str = "";

    // 수민
    public String getStr() {
        return str;
    }

    public ChatAdapter(Activity context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public ChatMessage getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChatMessage chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (chatMessage.getMessage() != null) {//일반 메시지일 경우
            convertView = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else{ //일반 메시지가 아닐 경우 -> 버튼 형식의 리스트뷰 레이아웃
            convertView = vi.inflate(R.layout.list_item_btn_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        }

//        if (convertView == null) {
//            if (chatMessage.getMessage() == "") {
//                convertView = vi.inflate(R.layout.list_item_btn_message, null);
//                holder = createViewHolder(convertView);
//                convertView.setTag(holder);
//            } else if (chatMessage.getMessage() != null) {
//                convertView = vi.inflate(R.layout.list_item_chat_message, null);
//                holder = createViewHolder(convertView);
//                convertView.setTag(holder);
//            }
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        boolean myMsg = chatMessage.getIsme();//Just a dummy check
        //to simulate whether it me or other sender

        //setAlignment(holder, myMsg);

        if (chatMessage.getMessage() == null) {
            array=chatMessage.getBtnMsgs();
            setListBtnAlign(holder, myMsg);
            holder.txtInfo.setText(chatMessage.getDate());
        } else {
            setAlignment(holder, myMsg);
            holder.txtMessage.setText(chatMessage.getMessage());
            holder.txtInfo.setText(chatMessage.getDate());
        }


        return convertView;
    }

    //버튼리스트
    public void setListBtnAlign(ViewHolder holder,  boolean isMe) {
        if (!isMe) {
            int height_weight=0; //리스트뷰 안의 내용의 크기에 따라 listview 크기 변하게 하는 가중치
            holder.contentWithBG_list.setBackgroundResource(R.drawable.chatbtn_pink);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG_list.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG_list.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);

            //listview 레이아웃 속성
            layoutParams = (LinearLayout.LayoutParams) holder.listView.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;

            //리스트뷰 어댑터 연결
            adapter = new Chat_BtnListViewAdapter(); //어댑터 객체 생성
            holder.listView.setAdapter(adapter); //어댑터 연결

            for (String msg : array) {
                adapter.addItem(msg);
                height_weight=height_weight+210;
            }
            Log.d("리스트뷰 높이",height_weight+"");
            layoutParams.height=height_weight;
            holder.listView.setLayoutParams(layoutParams);

            //text
            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.chatbtn_gray);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }

    public void add(ChatMessage message) {
        chatMessages.add(message);
    }

    public void add(List<ChatMessage> messages) {
        chatMessages.addAll(messages);
    }

    //일반 메시지
    private void setAlignment(ViewHolder holder, boolean isMe) {
        if (!isMe) {
            Log.d("홀더2222222",holder.toString());
            holder.contentWithBG.setBackgroundResource(R.drawable.chatbtn_pink);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.chatbtn_gray);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        holder.listView = (ListView) v.findViewById(R.id.listview);
        holder.contentWithBG_list = (LinearLayout) v.findViewById(R.id.contentWithBackground_list);
        return holder;
    }

    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
        public ListView listView;
        public LinearLayout contentWithBG_list;
    }

}