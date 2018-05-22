package com.example.ravindra.onlineexamination;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ScrollView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        scrollView = findViewById(R.id.resultScrollView);
        goToListBtn = findViewById(R.id.goListButton);
        questionsObjs = getIntent().getParcelableArrayListExtra("totQues");
        question = getIntent().getIntExtra("question", 0);
        questionStudentResponses = getIntent().getParcelableArrayListExtra("questionList");
        ResultAdapter resultAdapter = new ResultAdapter(this, questionStudentResponses, questionsObjs);
        listView = findViewById(R.id.resultList);
        listView.setAdapter(resultAdapter);
    }

    public void goToList(View view) {
        if (listView.getVisibility()==View.VISIBLE){
            listView.setVisibility(View.GONE);
            goToListBtn.setText("Get Detail...");
            scrollView.setVisibility(View.VISIBLE);
        }else{
            scrollView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            goToListBtn.setText("Go Back...");
        }
    }
}
