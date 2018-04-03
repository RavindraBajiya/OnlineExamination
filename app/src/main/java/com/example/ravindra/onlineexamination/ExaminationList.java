package com.example.ravindra.onlineexamination;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExaminationList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    FirebaseDatabase database;
    DatabaseReference reference;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_list);
        database = MainActivity.databaseObject();
        listView = findViewById(R.id.examinationList);
        listView.setOnItemClickListener(this);
        reference = database.getReference("exam_list/");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String array[] = new String[100];
                int i = 1;
                if (dataSnapshot.exists()) {
                    for (i = 1; i < 100; i++) {
                        if (dataSnapshot.child(i + "").exists()) {
                            Log.d("rkb", dataSnapshot.child(i + "").getValue().toString());
                            array[i - 1] = dataSnapshot.child(i + "").getValue().toString();
                        } else {
                            break;
                        }
                    }
                }
                String arr[] = new String[i - 1];
                for (int j = 0; j < i - 1; j++) {
                    arr[j] = array[j];
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExaminationList.this, android.R.layout.simple_list_item_1, arr);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "show test" + (position + 1), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ExamPageDrower.class);
        intent.putExtra("exam", "test" + (position + 1));
        startActivity(intent);
    }
}
