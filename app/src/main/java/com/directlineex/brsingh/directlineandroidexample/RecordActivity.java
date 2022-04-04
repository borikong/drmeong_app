package com.directlineex.brsingh.directlineandroidexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    ListView recordList;
    RecordAdapter adapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordList = (ListView)findViewById(R.id.listView);
        adapter = new RecordAdapter();

        adapter.addItem(new RecordItem("08월 30일","10:59","PM","설사를 한다","심한 설사를 반복한다","토를 한다"));

        recordList.setAdapter(adapter);

    }


    class RecordAdapter extends BaseAdapter{
        ArrayList<RecordItem> items = new ArrayList<RecordItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(RecordItem item){
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            RecordItemView v = new RecordItemView(getApplicationContext());
            RecordItem item = items.get(position);
            v.setDate(item.getDate());
            v.setTime(item.getTime());
            v.setSymptom1(item.getSymptom1());
            v.setSymptom2(item.getSymptom2());
            v.setSymptom3(item.getSymptom3());
            v.setAMorPM(item.getAMorPM());

            return v;
        }
    }
}
