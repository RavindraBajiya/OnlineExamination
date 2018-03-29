package com.example.ravindra.onlineexamination;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    TextView email;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = findViewById(R.id.fpEmail);
        submit = findViewById(R.id.fpSubmit);
        submit.setOnClickListener(this);
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onClick(View v) {
        String emailId = email.getText().toString();
        if (emailId.isEmpty()){
            Toast.makeText(this, "Please Enter an Email.", Toast.LENGTH_SHORT).show();
        }
        else if(!isEmailValid(emailId)){
            Toast.makeText(this, "Invalid Email Id.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Sending Recovery Email...", Toast.LENGTH_SHORT).show();
           FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
           firebaseAuth.sendPasswordResetEmail(emailId);
        }
    }
}
