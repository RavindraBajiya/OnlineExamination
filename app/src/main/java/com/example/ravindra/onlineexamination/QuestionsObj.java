package com.example.ravindra.onlineexamination;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class QuestionsObj implements Serializable,Parcelable{
    String q;
    String a;
    String b;
    String c;
    String d;
    String ans;

    public QuestionsObj(String q, String a, String b, String c, String d, String ans) {
        this.q = q;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.ans = ans;
    }

    public QuestionsObj() {
    }

    public QuestionsObj(Parcel in) {
        this.q = in.readString();
        this.a = in.readString();
        this.b = in.readString();
        this.c = in.readString();
        this.d = in.readString();
        this.ans = in.readString();
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(q);
        dest.writeString(a);
        dest.writeString(b);
        dest.writeString(c);
        dest.writeString(d);
        dest.writeString(ans);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public QuestionsObj createFromParcel(Parcel in) {
            return new QuestionsObj(in);
        }

        public QuestionsObj[] newArray(int size) {
            return new QuestionsObj[size];
        }
    };
}
