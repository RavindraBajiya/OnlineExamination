package com.example.ravindra.onlineexamination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Result extends AppCompatActivity {
    ListView listView;
    ArrayList<QuestionsObj> questionsObjs;
    int question;
    ArrayList<QuestionStudentResponse> questionStudentResponses;
    Button goToListBtn;
    ScrollView scrollView;
    TextView maxQue;
    TextView attQue;
    TextView rigQue;
    TextView wroQue;
    TextView posMark;
    TextView negMark;
    TextView oveResult;
    TextView oveResultMarks;
    float positive;
    float negative;
    float getMarks;
    float minusMarks;
    float oveMarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        maxQue = findViewById(R.id.textView11);
        attQue = findViewById(R.id.textView12);
        rigQue = findViewById(R.id.textView13);
        wroQue = findViewById(R.id.textView14);
        posMark = findViewById(R.id.textView15);
        negMark = findViewById(R.id.textView16);
        oveResult = findViewById(R.id.textView17);
        oveResultMarks = findViewById(R.id.textView18);
        scrollView = findViewById(R.id.resultScrollView);
        goToListBtn = findViewById(R.id.goListButton);
        Intent intent = getIntent();
        questionsObjs = intent.getParcelableArrayListExtra("totQues");
        question = intent.getIntExtra("question", 0);
        questionStudentResponses = intent.getParcelableArrayListExtra("questionList");
        ArrayList<QuestionsObj> questionsObjs1 = new ArrayList<>();
        for (int i=0;i<question;i++){
            questionsObjs1.add(i,questionsObjs.get(i));
        }
        ResultAdapter resultAdapter = new ResultAdapter(this, questionStudentResponses, questionsObjs1);
        Toast.makeText(this, "question obj == "+questionsObjs.size(), Toast.LENGTH_SHORT).show();
        listView = findViewById(R.id.resultList);
        listView.setAdapter(resultAdapter);
        positive = intent.getFloatExtra("positive",0);
        negative = intent.getFloatExtra("negative",0);
        setResultData();
    }

    private void setResultData() {
        maxQue.setText(question + "");

        int attempt=0;
        for (int i=0;questionStudentResponses.size()>i;i++){
            if (questionStudentResponses.get(i).getAttempt()==1){
                attempt++;
            }
        }
        attQue.setText(attempt + "");
        int right = 0;
        int wrong = 0;
        for (int i=0;i<attempt;i++){
            if (questionStudentResponses.get(i).getAttempt()==1) {
                if (questionStudentResponses.get(i).getRealAns().trim().equals(questionStudentResponses.get(i).getUserAns().trim())) {
                    right++;
                }
            }
        }
        wrong = attempt-right;
        rigQue.setText(right+"");
        wroQue.setText(wrong+"");
        getMarks = positive*right;
        minusMarks = negative*wrong;
        oveMarks = getMarks-minusMarks;
        float result = (positive*right-wrong*negative)*100/(question*positive);
        posMark.setText(getMarks+"");
        negMark.setText(minusMarks+"");
        oveResultMarks.setText(oveMarks+"/"+question*positive);
        oveResult.setText(result+"%");
    }

    public void goToList(View view) {
        if (listView.getVisibility() == View.VISIBLE) {
            listView.setVisibility(View.GONE);
            goToListBtn.setText("Get Detail...");
            scrollView.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            goToListBtn.setText("Go Back...");
        }
    }
}