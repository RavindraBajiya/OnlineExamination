package com.example.ravindra.onlineexamination;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Result extends AppCompatActivity {
    ListView listView;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ArrayList<QuestionsObj> questionsObjs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        questionsObjs =  getIntent().getParcelableArrayListExtra("totQues");
        ArrayList<QuestionStudentResponse> questionStudentResponses = getIntent().getParcelableArrayListExtra("questionList");
        ResultAdapter resultAdapter = new ResultAdapter(this, questionStudentResponses);
        listView = findViewById(R.id.resultList);
        listView.setAdapter(resultAdapter);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        String array[] = {"item1", "item2", "item3", "item4"};

        for (int i = 0; i < questionsObjs.size(); i++) {
            listDataHeader.add(questionsObjs.get(i).getQ());
            List<String> item = new ArrayList<String>();
            item.add("A. "+questionsObjs.get(i).getA());
            item.add("B. "+questionsObjs.get(i).getB());
            item.add("C. "+questionsObjs.get(i).getC());
            item.add("D. "+questionsObjs.get(i).getD());
            item.add("Ans. "+questionsObjs.get(i).getAns());

            listDataChild.put(listDataHeader.get(i), item);

        }
    }
}
