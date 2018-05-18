package com.example.ravindra.onlineexamination;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = findViewById(R.id.list);
        setContentView(R.layout.activity_result);
        ArrayList<QuestionStudentResponse> questionStudentResponses = getIntent().getParcelableArrayListExtra("questionList");
        String []array = new String[questionStudentResponses.size()];
        for (int i = 0;i<questionStudentResponses.size();i++){
            QuestionStudentResponse temp = questionStudentResponses.get(i);
            array[i] = temp.getRealAns();
            Log.d("resultdude",array[i]);
        }
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array);
               listView.setAdapter(adapter);
    }
}
