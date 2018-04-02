package com.example.ravindra.onlineexamination;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExamPageDrower extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {
    GridView gridView;
    FirebaseFirestore db;
    TextView ques;
    RadioButton r1, r2, r3, r4;
    TextView hindi, english;
    int question;
    Button saveAndNext;
    Boolean lan;
    DocumentReference documentReference;
    int num = 1;
    Button v;
    boolean attempt[];
    Button btnqdt, maximumMarks, minusMarking;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_page_drower);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        saveAndNext = findViewById(R.id.saveandNext);
        btnqdt = findViewById(R.id.button4);
        maximumMarks = findViewById(R.id.button5);
        minusMarking = findViewById(R.id.button6);
        ques = findViewById(R.id.question);
        r1 = findViewById(R.id.ansRadio1);
        r2 = findViewById(R.id.ansRadio2);
        r3 = findViewById(R.id.ansRadio3);
        r4 = findViewById(R.id.ansRadio4);
        hindi = findViewById(R.id.languageHindi);
        english = findViewById(R.id.languageEnglish);
        hindi.setOnClickListener(this);
        english.setOnClickListener(this);
        gridView = findViewById(R.id.questionsItem);
        db = FirebaseFirestore.getInstance();
        DatabaseReference myRef = database.getReference("papers/test1/tot_q");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                question = Integer.parseInt(dataSnapshot.getValue().toString());
                attempt = new boolean[question];
                MyAdapter adapter = new MyAdapter(ExamPageDrower.this, question, attempt);
                gridView.setAdapter(adapter);
                loadEnglish(1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("aaa", "Failed to read value.", error.toException());
            }
        });
        saveAndNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num < question) {
                    if (lan) {
                        loadEnglish(num + 1);
                        num++;
                    } else {
                        loadHindi(num + 1);
                        num++;
                    }
                    if (r1.isChecked() || r2.isChecked() || r3.isChecked() || r4.isChecked()) {
                        attempt[num] = true;
                        r1.setChecked(false);
                        r2.setChecked(false);
                        r3.setChecked(false);
                        r4.setChecked(false);

                    }
                }
            }
        });
        GridView gridView1 = findViewById(R.id.list);
        gridView1.setAdapter(new MyAdapter2(this));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                MyAdapter myAdapter = new MyAdapter(ExamPageDrower.this, question, attempt);
                gridView.setAdapter(myAdapter);
                Toast.makeText(ExamPageDrower.this, "drawerOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exam_page_drower, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.finish) {
            Toast.makeText(this, "Finish", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toast.makeText(this, "fjalkfj", Toast.LENGTH_SHORT).show();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.languageEnglish) {
            if (num <= question) {
                loadEnglish(num);
            }

        } else if (id == R.id.languageHindi) {
            if (num <= question) {
                loadHindi(num);
            }
        } else {
            Toast.makeText(this, "No More Questions.", Toast.LENGTH_SHORT).show();
        }
    }


    void loadEnglish(final int questionNumber) {
        lan = true;
        DatabaseReference myRef = database.getReference("papers/test1/questions/q" + questionNumber);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ques1 = "Que." + dataSnapshot.child("q").getValue();
                Log.d("aaa", ques1);

                String o1 = dataSnapshot.child("o1").getValue().toString();
                String o2 = dataSnapshot.child("o2").getValue().toString();
                String o3 = dataSnapshot.child("o3").getValue().toString();
                String o4 = dataSnapshot.child("o4").getValue().toString();
                r1.setText(o1);
                r2.setText(o2);
                r3.setText(o3);
                r4.setText(o4);
                ques.setText(ques1);
                attempt[questionNumber-1] = true;
                btnqdt.setText(questionNumber + "/" + question);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("aaa", "Failed to read value.", error.toException());
            }
        });

    }

    void loadHindi(final int questionNumber) {
        lan = false;
        documentReference = db.collection("questions/questions/q" + questionNumber).document("hindi");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String ques1 = "प्रशन - " + documentSnapshot.get("q").toString();
                String o1 = documentSnapshot.get("o1").toString();
                String o2 = documentSnapshot.get("o2").toString();
                String o3 = documentSnapshot.get("o3").toString();
                String o4 = documentSnapshot.get("o4").toString();
                r1.setText(o1);
                r2.setText(o2);
                r3.setText(o3);
                r4.setText(o4);
                ques.setText(ques1);
                btnqdt.setText(questionNumber + "/" + question);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "position" + position, Toast.LENGTH_SHORT).show();
    }

    class MyAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;
        int question;
        Context context;
        boolean attempt[];


        public MyAdapter(Context context, int question, boolean[] attempt) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.question = question;
            this.attempt = attempt;
        }

        @Override
        public int getCount() {
            return question;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = layoutInflater.inflate(R.layout.drawerlayout, null);
            v = convertView.findViewById(R.id.drawerLayoutButton);
            if (attempt[position]) {
                v.setBackground(getDrawable(R.drawable.visitedbtn));
            } else
                v.setBackground(getDrawable(R.drawable.notvisitedbtn));
            v.setText((position + 1) + "");
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ExamPageDrower.this, "click " + (position + 1), Toast.LENGTH_SHORT).show();
                    loadEnglish(position + 1);
                    v.setBackground(getDrawable(R.drawable.visitedbtn));
                    num = position + 1;
                }
            });
            return convertView;
        }
    }
}