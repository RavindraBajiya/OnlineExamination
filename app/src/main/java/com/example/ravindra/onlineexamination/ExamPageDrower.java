package com.example.ravindra.onlineexamination;

import android.annotation.SuppressLint;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    float positive;
    float negative;
    Button saveAndNext;
    Boolean lan;
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
        DatabaseReference myRef1 = database.getReference("exam_schedule");
        DatabaseReference myRef = myRef1.child("maximum_question");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                question = Integer.parseInt(dataSnapshot.getValue().toString());
                attempt = new boolean[question];
                for (int i = 0; i < question; i++) {
                    questionStudentResponses.add(i, new QuestionStudentResponse());
                    questionStudentResponses.get(i).setAttempt(0);
                }
//                Toast.makeText(ExamPageDrower.this, "tot question = " + questionStudentResponses.size(), Toast.LENGTH_SHORT).show();
                MyAdapter adapter = new MyAdapter(ExamPageDrower.this, question, questionStudentResponses, attempt);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("aaa", "Failed to read value.", error.toException());
            }
        });
        DatabaseReference databaseReference = database.getReference("exam_schedule").child("positive_marking");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                positive = Integer.parseInt(dataSnapshot.getValue().toString());
                maximumMarks.setText(positive + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ExamPageDrower.this, "Database Error..Try again", Toast.LENGTH_SHORT).show();
            }
        });
        databaseReference = database.getReference("exam_schedule").child("negative_marking");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                negative = Float.parseFloat(dataSnapshot.getValue().toString());
                minusMarking.setText(negative + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ExamPageDrower.this, "Database Error..Try again", Toast.LENGTH_SHORT).show();
            }
        });
        saveAndNext.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (num < question) {
                    if (num==question-1){
                        saveAndNext.setText("Save & finish");
                    }
                    RadioGroup radioGroup = findViewById(R.id.radiogroup);
                    radioGroup.clearCheck();
                    loadEnglish(num + 1);
                    num++;
                }
                else {
                    resultActivity();
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
         myRef = myRef1.child("total_questions");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int tot = Integer.parseInt(dataSnapshot.getValue().toString());
                getQuestionsList(test,tot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("aaa", "Failed to read value.", error.toException());
            }
        });

    }

    private void getQuestionsList(final String test, final int tot) {
        final DatabaseReference reference = database.getReference(test);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int random = new java.util.Random().nextInt(tot);
                    for (int i = 1; dataSnapshot.child("q" + random).exists()&&i<=question; i++) {
                        QuestionsObj temp = new QuestionsObj();
                        temp.setQ(dataSnapshot.child("q" + random).child("ques").getValue().toString().trim());
                        temp.setA(dataSnapshot.child("q" + random).child("1").getValue().toString().trim());
                        temp.setB(dataSnapshot.child("q" + random).child("2").getValue().toString().trim());
                        temp.setC(dataSnapshot.child("q" + random).child("3").getValue().toString().trim());
                        temp.setD(dataSnapshot.child("q" + random).child("4").getValue().toString().trim());
                        temp.setAns(dataSnapshot.child("q" + random).child("ans").getValue().toString().trim());
                        questionsObjs.add(temp);
                        Log.d("ravindrabajiya",questionsObjs.size()+"  random"+random);
                        random = new java.util.Random().nextInt(tot);
                    }
                }
                loadEnglish(num);
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
            alertDialogBuilder.setMessage("Are You Sure You Want To Exit The Exam.");
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
            resultActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resultActivity() {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are You Sure You Want To Finish The Exam.");
        alertDialogBuilder.setPositiveButton("Finish",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(ExamPageDrower.this, Result.class);
                        intent.putParcelableArrayListExtra("questionList", (ArrayList<? extends Parcelable>) questionStudentResponses);
                        intent.putParcelableArrayListExtra("totQues", (ArrayList<? extends Parcelable>) questionsObjs);
                        intent.putExtra("question", question);
                        intent.putExtra("positive", positive);
                        intent.putExtra("negative", negative);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ExamPageDrower.this, "Continue...", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == r1.getId()) {
            Log.d("ravindrabajiya", "question number == " + num);
            questionStudentResponses.get(num-1).setUserAns(questionsObjs.get(num - 1).getA().trim());
            questionStudentResponses.get(num-1).setRealAns(questionsObjs.get(num - 1).getAns().trim());
        } else if (id == r2.getId()) {
            questionStudentResponses.get(num-1).setUserAns(questionsObjs.get(num - 1).getB().trim());
            questionStudentResponses.get(num-1).setRealAns(questionsObjs.get(num - 1).getAns().trim());
        } else if (id == r3.getId()) {
            questionStudentResponses.get(num-1).setUserAns(questionsObjs.get(num - 1).getC().trim());
            questionStudentResponses.get(num-1).setRealAns(questionsObjs.get(num - 1).getAns().trim());
        } else if (id == r4.getId()) {
            questionStudentResponses.get(num-1).setUserAns(questionsObjs.get(num - 1).getD().trim());
            questionStudentResponses.get(num-1).setRealAns(questionsObjs.get(num - 1).getAns().trim());
        } else
            Toast.makeText(this, "No More Questions.", Toast.LENGTH_SHORT).show();
        attempt[num - 1] = true;
//        temp.setAttempt(1);
        setAnswered(num-1);
        questionStudentResponses.get(num-1).setAttempt(1);
        for (int i = 0; i < questionStudentResponses.size(); i++) {
            Log.d("ravindrabajiya", "question " + i + "  " + questionStudentResponses.get(i).getAttempt());
        }
    }


    void loadEnglish(final int questionNumber) {
        lan = true;
        if (questionNumber==question){
            saveAndNext.setText("save & finish");
        }
        else{
            saveAndNext.setText("save & next");
        }
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
        setAnswered(questionNumber-1);
        ques.setText("Question : " + q);
        attempt[questionNumber - 1] = true;
        btnqdt.setText(questionNumber + "/" + question);
    }

    private void setAnswered(int i) {
        if (questionStudentResponses.get(i).getAttempt()==1){
            RadioGroup radioGroup = findViewById(R.id.radiogroup);
            radioGroup.clearCheck();
            String userAns = questionStudentResponses.get(i).getUserAns().trim();
            if (userAns.equals(questionsObjs.get(i).getA().trim())){
                r1.setChecked(true);
            }
            else if (userAns.equals(questionsObjs.get(i).getB().trim())){
                r2.setChecked(true);
            }
            else if (userAns.equals(questionsObjs.get(i).getC().trim())){
                r3.setChecked(true);
            }
            else if (userAns.equals(questionsObjs.get(i).getD().trim())){
                r4.setChecked(true);
            }
//            Toast.makeText(this, "setAnswered"+i, Toast.LENGTH_SHORT).show();

        }
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
            QuestionStudentResponse temp = questionStudentResponses.get(position);
            if (attempt[position] && temp.getAttempt() == 0) {
                temp.setAttempt(2);
            }
            if (temp.getAttempt() == 2) {
                v.setBackground(getDrawable(R.drawable.visitedbtn));
            } else if (temp.getAttempt() == 0)
                v.setBackground(getDrawable(R.drawable.notvisitedbtn));
            else
                v.setBackground(getDrawable(R.drawable.answered));
            v.setText((position + 1) + "");
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioGroup radioGroup = findViewById(R.id.radiogroup);
                    radioGroup.clearCheck();
                    loadEnglish(position+1);
                   // v.setBackground(getDrawable(R.drawable.visitedbtn));
                    num = position + 1;
                    drawer = findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            });
            return convertView;
        }
    }
}