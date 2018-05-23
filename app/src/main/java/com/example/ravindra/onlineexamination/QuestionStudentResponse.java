package com.example.ravindra.onlineexamination;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class QuestionStudentResponse implements Parcelable{
    String userAns;
    String realAns;
    int attempt;

    public QuestionStudentResponse(String userAns, String realAns, int attempt) {
        this.userAns = userAns;
        this.realAns = realAns;
        this.attempt = attempt;
    }

    public QuestionStudentResponse() {
    }

    public QuestionStudentResponse(Parcel in) {
        this.userAns = in.readString();
        this.realAns = in.readString();
        this.attempt = in.readInt();
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

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userAns);
        dest.writeString(realAns);
        dest.writeInt(attempt);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public QuestionStudentResponse createFromParcel(Parcel in) {
            return new QuestionStudentResponse(in);
        }

        public QuestionStudentResponse[] newArray(int size) {
            return new QuestionStudentResponse[size];
        }
    };
}
