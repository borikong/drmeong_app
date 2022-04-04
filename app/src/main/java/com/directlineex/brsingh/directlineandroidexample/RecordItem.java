package com.directlineex.brsingh.directlineandroidexample;

public class RecordItem {
    String date;
    String time;
    String AMorPM;
    String symptom1;
    String symptom2;
    String symptom3;

    public RecordItem(String date, String time, String AMorPM, String symptom1) {
        this.date = date;
        this.time = time;
        this.AMorPM = AMorPM;
        this.symptom1 = symptom1;
    }

    public RecordItem(String date, String time, String AMorPM, String symptom1, String symptom2) {
        this.date = date;
        this.time = time;
        this.AMorPM = AMorPM;
        this.symptom1 = symptom1;
        this.symptom2 = symptom2;
    }

    public RecordItem(String date, String time, String AMorPM, String symptom1, String symptom2, String symptom3) {
        this.date = date;
        this.time = time;
        this.AMorPM = AMorPM;
        this.symptom1 = symptom1;
        this.symptom2 = symptom2;
        this.symptom3 = symptom3;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSymptom1() {
        return symptom1;
    }

    public void setSymptom1(String symptom1) {
        this.symptom1 = symptom1;
    }

    public String getSymptom2() {
        return symptom2;
    }

    public void setSymptom2(String symptom2) {
        this.symptom2 = symptom2;
    }

    public String getSymptom3() {
        return symptom3;
    }

    public void setSymptom3(String symptom3) {
        this.symptom3 = symptom3;
    }

    public String getAMorPM() {
        return AMorPM;
    }

    public void setAMorPM(String AMorPM) {
        this.AMorPM = AMorPM;
    }
}
