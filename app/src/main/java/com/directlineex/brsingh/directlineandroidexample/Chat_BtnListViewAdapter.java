package com.directlineex.brsingh.directlineandroidexample;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by DS on 2018-08-20.
 */

public class Chat_BtnListViewAdapter extends BaseAdapter {

    private ArrayList<String> listViewItemList = new ArrayList<String>() ;

    public Chat_BtnListViewAdapter() {

    }
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        Log.d("아이템 아이디",position+"");


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_btnlistitem, parent, false);
        }

        Button btn=(Button)convertView.findViewById(R.id.btn);

        setButtonlistener(pos,btn);

        String listViewItem = listViewItemList.get(position);

        btn.setText(listViewItem);

        return convertView;
    }

    //버튼 눌렀을 때 리스너
    public void setButtonlistener(final int pos,Button btn){

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pos가 0이면 맨 처음거 누른 것
                Log.v("롸롸롸롸", "pos = " + String.valueOf(pos));

                ChatAdapter.str = String.valueOf(pos);

            }
        });

    }


    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수.
    public void addItem(String item) {
       listViewItemList.add(item);
    }

}
