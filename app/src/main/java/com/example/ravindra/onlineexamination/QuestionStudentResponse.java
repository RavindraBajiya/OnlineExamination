package com.example.ravindra.onlineexamination;

import java.io.Serializable;

public class QuestionStudentResponse implements Serializable {
    String userAns;
    String realAns;
    boolean attempt;

    public QuestionStudentResponse(String userAns, String realAns, boolean attempt) {
        this.userAns = userAns;
        this.realAns = realAns;
        this.attempt = attempt;
    }

    public QuestionStudentResponse() {
    }

    public String getUserAns() {
        return userAns;
    }

    public void setUserAns(String userAns) {
        this.userAns = userAns;
    }

    public String getRealAns() {
        return realAns;
    }

    public void setRealAns(String realAns) {
        this.realAns = realAns;
    }

    public boolean isAttempt() {
        return attempt;
    }

    public void setAttempt(boolean attempt) {
        this.attempt = attempt;
    }
}
