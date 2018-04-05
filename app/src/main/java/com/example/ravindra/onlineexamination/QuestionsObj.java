package com.example.ravindra.onlineexamination;

import java.io.Serializable;

public class QuestionsObj implements Serializable{
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
}
