package com.example.ravindra.onlineexamination;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultAdapter extends BaseAdapter {
    ArrayList<QuestionStudentResponse> list;
    Context context;
    LayoutInflater layoutInflater;


    public ResultAdapter(Context context, ArrayList<QuestionStudentResponse> questionStudentResponses) {
        this.context = context;
        list = questionStudentResponses;
    }

    public ResultAdapter() {
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.result_row, null);
        TextView ua = convertView.findViewById(R.id.userAnswer);
        TextView ca = convertView.findViewById(R.id.correctAnswer);
        ua.setText(list.get(position).getUserAns());
        ca.setText(list.get(position).getRealAns());
        return convertView;
    }
}