package com.example.ravindra.onlineexamination;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class ExamPageDrower extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {
    GridView gridView;
    FirebaseFirestore db;
    TextView ques;
    TextView time;
    RadioButton r1, r2, r3, r4;
    int question;
    Button saveAndNext;
    Boolean lan;
    DocumentReference documentReference;
    int num = 1;
    Button v;
    String test;
    boolean attempt[];
    Button btnqdt, maximumMarks, minusMarking;
    FirebaseDatabase database;
    DrawerLayout drawer;
    ArrayList<QuestionsObj> questionsObjs;
    ArrayList<QuestionStudentResponse> questionStudentResponses;
    AVLoadingIndicatorView avi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_page_drower);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        time = findViewById(R.id.time);
        avi = findViewById(R.id.avi);
        avi.show();
        questionsObjs = new ArrayList<>();
        questionStudentResponses = new ArrayList<>();
        new CountDownTimer(3600000, 1000) {

            public void onTick(long millisUntilFinished) {
//                time.setText("seconds remaining: " + millisUntilFinished / 1000);
                int sec = (int) (millisUntilFinished / 1000);
                int hour = sec / 3600;
                sec = sec % 3600;
                int min = sec / 60;
                sec = sec % 60;
                time.setText(hour + ":" + min + ":" + sec);
            }

            public void onFinish() {
                time.setText("done!");
            }
        }.start();
        test = "tot_questions";
        database = MainActivity.databaseObject();
        saveAndNext = findViewById(R.id.saveandNext);
        btnqdt = findViewById(R.id.button4);
        maximumMarks = findViewById(R.id.button5);
        minusMarking = findViewById(R.id.button6);
        ques = findViewById(R.id.question);
        r1 = findViewById(R.id.ansRadio1);
        r2 = findViewById(R.id.ansRadio2);
        r3 = findViewById(R.id.ansRadio3);
        r4 = findViewById(R.id.ansRadio4);
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
        gridView = findViewById(R.id.questionsItem);
        db = FirebaseFirestore.getInstance();
        DatabaseReference myRef = database.getReference("maximum_questions");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                question = Integer.parseInt(dataSnapshot.getValue().toString());
                attempt = new boolean[question];
                MyAdapter adapter = new MyAdapter(ExamPageDrower.this, question, questionStudentResponses, attempt);
                gridView.setAdapter(adapter);
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
                   /* if (r1.isChecked() || r2.isChecked() || r3.isChecked() || r4.isChecked()) {
                        QuestionStudentResponse temp = new QuestionStudentResponse();
                        if (r1.isChecked()) {
                            temp.setAttempt(true);
                            temp.setUserAns("1");
                            temp.setRealAns(questionsObjs.get(num).getAns());
                        } else if (r2.isChecked()) {
                            temp.setAttempt(true);
                            temp.setUserAns("2");
                            temp.setRealAns(questionsObjs.get(num).getAns());

                        } else if (r3.isChecked()) {
                            temp.setAttempt(true);
                            temp.setUserAns("3");
                            temp.setRealAns(questionsObjs.get(num).getAns());

                        }
                        if (r4.isChecked()) {
                            temp.setAttempt(true);
                            temp.setUserAns("4");
                            temp.setRealAns(questionsObjs.get(num).getAns());
                        }
                        attempt[num] = true;
                        questionStudentResponses.add(temp);
                        Log.d("questions",questionStudentResponses.get(0).getRealAns()+" "+questionStudentResponses.get(0).getUserAns()+" "+questionStudentResponses.get(0).isAttempt());
                    }*/
                    r1.setChecked(false);
                    r2.setChecked(false);
                    r3.setChecked(false);
                    r4.setChecked(false);
                    loadEnglish(num + 1);
                    num++;
                } else {
                    Toast.makeText(ExamPageDrower.this, "No More Questions.", Toast.LENGTH_SHORT).show();
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
                MyAdapter myAdapter = new MyAdapter(ExamPageDrower.this, question, questionStudentResponses, attempt);
                gridView.setAdapter(myAdapter);
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
        getQuestionsList(test);
    }

    private void getQuestionsList(final String test) {
        final DatabaseReference reference = database.getReference(test);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (int i = 1; dataSnapshot.child("q" + i).exists(); i++) {
                        QuestionsObj temp = new QuestionsObj();
                        temp.setQ(dataSnapshot.child("q" + i).child("ques").getValue().toString());
                        temp.setA(dataSnapshot.child("q" + i).child("1").getValue().toString());
                        temp.setB(dataSnapshot.child("q" + i).child("2").getValue().toString());
                        temp.setC(dataSnapshot.child("q" + i).child("3").getValue().toString());
                        temp.setD(dataSnapshot.child("q" + i).child("4").getValue().toString());
                        temp.setAns(dataSnapshot.child("q" + i).child("ans").getValue().toString());
                        questionsObjs.add(temp);
                    }
                }
                loadEnglish(1);
                avi.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are You Sure You Want To Finish The Exam.");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            back();
                            Toast.makeText(ExamPageDrower.this, "Finished", Toast.LENGTH_SHORT).show();

                        }
                    });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(ExamPageDrower.this, "Continue...", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

    }

    private void back() {
        super.onBackPressed();
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
            Intent intent = new Intent(this, Result.class);
            intent.putParcelableArrayListExtra("questionList", (ArrayList<? extends Parcelable>) questionStudentResponses);
            startActivity(intent);
            finish();
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
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String ans = questionsObjs.get(num-1).getAns();
        QuestionStudentResponse temp = new QuestionStudentResponse();
          if (id == r1.getId()) {
            temp.setAttempt(true);
            temp.setUserAns("1");
            temp.setRealAns(questionsObjs.get(num-1).getAns());
            if (ans.equals(questionsObjs.get(num-1).getA())){
                Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "false ...Real answer is "+ans, Toast.LENGTH_SHORT).show();
        } else if (id == r2.getId()) {
              temp.setAttempt(true);
              temp.setUserAns("2");
              temp.setRealAns(questionsObjs.get(num-1).getAns());
            if (ans.equals(questionsObjs.get(num-1).getB())){
                Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "false ...Real answer is "+ans, Toast.LENGTH_SHORT).show();
        } else if (id == r3.getId()) {
              temp.setAttempt(true);
              temp.setUserAns("3");
              temp.setRealAns(questionsObjs.get(num-1).getAns());
            if (ans.equals(questionsObjs.get(num-1).getC())){
                Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "false ...Real answer is "+ans, Toast.LENGTH_SHORT).show();

        } else if (id == r4.getId()) {
              temp.setAttempt(true);
              temp.setUserAns("4");
              temp.setRealAns(questionsObjs.get(num-1).getAns());
            if (ans.equals(questionsObjs.get(num-1).getD())){
                Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "false ...Real answer is "+ans, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "No More Questions.", Toast.LENGTH_SHORT).show();

        attempt[num-1] = true;
        questionStudentResponses.add(temp);
        Log.d("questions",questionStudentResponses.get(num-1).getRealAns()+" "+questionStudentResponses.get(num-1).getUserAns()+" "+questionStudentResponses.get(num-1).isAttempt());
    }


    void loadEnglish(final int questionNumber) {
        lan = true;
        QuestionsObj questionsObj = questionsObjs.get(questionNumber - 1);
        String o1 = questionsObj.getA();
        String o2 = questionsObj.getB();
        String o3 = questionsObj.getC();
        String o4 = questionsObj.getD();
        String q = questionsObj.getQ();
        r1.setText(o1);
        r2.setText(o2);
        r3.setText(o3);
        r4.setText(o4);
        ques.setText("Question : " + q);
        attempt[questionNumber - 1] = true;
        btnqdt.setText(questionNumber + "/" + question);

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
        ArrayList<QuestionStudentResponse> questionStudentResponses;


        public MyAdapter(Context context, int question, ArrayList<QuestionStudentResponse> questionStudentResponses, boolean[] attempt) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.question = question;
            this.questionStudentResponses = questionStudentResponses;
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                    drawer = findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }
            });
            return convertView;
        }
    }
}