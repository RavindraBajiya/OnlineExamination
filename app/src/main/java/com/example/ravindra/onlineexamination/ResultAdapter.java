package com.example.ravindra.onlineexamination;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultAdapter extends BaseAdapter {
    ArrayList<QuestionStudentResponse> list;
    Context context;
    ArrayList<QuestionsObj> questionsObjs;
//    LayoutInflater layoutInflater;


    public ResultAdapter(Context context, ArrayList<QuestionStudentResponse> questionStudentResponses, ArrayList<QuestionsObj> questionsObjs) {
        this.context = context;
        list = questionStudentResponses;
        this.questionsObjs = questionsObjs;
    }

    @Override
    public int getCount() {
        return questionsObjs.size();
    }

    @Override
    public Object getItem(int position) {
        return questionsObjs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.result_row, null);
        }

        TextView q = convertView.findViewById(R.id.questionTV);
        TextView option1 = convertView.findViewById(R.id.option1);
        TextView option2 = convertView.findViewById(R.id.option2);
        TextView option3 = convertView.findViewById(R.id.option3);
        TextView option4 = convertView.findViewById(R.id.option4);
        QuestionsObj temp = questionsObjs.get(position);
        q.setText("Que."+(position+1)+" :" + temp.getQ());
        option1.setText("A." + temp.getA());
        option2.setText("B." + temp.getB());
        option3.setText("C." + temp.getC());
        option4.setText("D." + temp.getD());
        ImageView c1 = convertView.findViewById(R.id.c_o_1);
        ImageView c2 = convertView.findViewById(R.id.c_o_2);
        ImageView c3 = convertView.findViewById(R.id.c_o_3);
        ImageView c4 = convertView.findViewById(R.id.c_o_4);
        ImageView w1 = convertView.findViewById(R.id.w_o_1);
        ImageView w2 = convertView.findViewById(R.id.w_o_2);
        ImageView w3 = convertView.findViewById(R.id.w_o_3);
        ImageView w4 = convertView.findViewById(R.id.w_o_4);
        //set correct ans
        if (temp.getAns().equals(temp.getA())) {
            option1.setBackgroundColor(context.getResources().getColor(R.color.correctAns));
            c1.setVisibility(View.VISIBLE);
        } else if (temp.getAns().equals(temp.getB())) {
            option2.setBackgroundColor(context.getResources().getColor(R.color.correctAns));
            c2.setVisibility(View.VISIBLE);
        } else if (temp.getAns().equals(temp.getC())) {
            option3.setBackgroundColor(context.getResources().getColor(R.color.correctAns));
            c3.setVisibility(View.VISIBLE);
        } else if (temp.getAns().equals(temp.getD())) {
            option4.setBackgroundColor(context.getResources().getColor(R.color.correctAns));
            c4.setVisibility(View.VISIBLE);
        }
        if (!temp.getAns().equals(list.get(position).getUserAns())) {
            if (list.get(position).getAttempt()==1) {
                if (list.get(position).getUserAns().equals(temp.getA())) {
                    option1.setBackgroundColor(context.getResources().getColor(R.color.wrongAns));
                    w1.setVisibility(View.VISIBLE);
                } else if (list.get(position).getUserAns().equals(temp.getB())) {
                    option2.setBackgroundColor(context.getResources().getColor(R.color.wrongAns));
                    w2.setVisibility(View.VISIBLE);
                } else if (list.get(position).getUserAns().equals(temp.getC())) {
                    option3.setBackgroundColor(context.getResources().getColor(R.color.wrongAns));
                    w3.setVisibility(View.VISIBLE);
                } else if (list.get(position).getUserAns().equals(temp.getD())) {
                    option4.setBackgroundColor(context.getResources().getColor(R.color.wrongAns));
                    w4.setVisibility(View.VISIBLE);
                }
            }
        }
        return convertView;
    }
}