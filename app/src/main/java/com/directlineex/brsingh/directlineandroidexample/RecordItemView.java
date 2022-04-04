package com.directlineex.brsingh.directlineandroidexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.jar.Attributes;

public class RecordItemView extends LinearLayout {

    TextView date_textView,time_textView,AMorPM,sypmtom1_textView,sypmtom2_textView,sypmtom3_textView;


    public RecordItemView(Context context){
        super(context);
        init(context);
    }
    public RecordItemView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public void init(Context context)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.record_item,this,true);

        date_textView = (TextView)findViewById(R.id.date_textView);
        time_textView = (TextView)findViewById(R.id.time_textView);
        AMorPM = (TextView)findViewById(R.id.AMorPM);
        sypmtom1_textView = (TextView)findViewById(R.id.sypmtom1_textView);
        sypmtom2_textView = (TextView)findViewById(R.id.sypmtom2_textView);
        sypmtom3_textView = (TextView)findViewById(R.id.sypmtom3_textView);
    }

    public void setDate(String date){
        date_textView.setText(date);
    }
    public void setTime(String time){
        time_textView.setText(time);
    }

    public void setAMorPM(String time){
        AMorPM.setText(time);
    }

    public void setSymptom1(String symptom1)
    {
        sypmtom1_textView.setText(symptom1);
    }
    public void setSymptom2(String symptom2)
    {
        sypmtom1_textView.setText(symptom2);
    }
    public void setSymptom3(String symptom3)
    {
        sypmtom1_textView.setText(symptom3);
    }


}
