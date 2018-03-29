package com.example.ravindra.onlineexamination;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static int SPLASH_TIME = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(final FirebaseUser currentUser) {
        final Intent[] intent = new Intent[1];
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser == null) {
                    intent[0] = new Intent(SplashActivity.this, SignIn.class);
                } else {
                    intent[0] = new Intent(SplashActivity.this, MainActivity.class);

            boolean flag = sharedPref.getBoolean("flag",true);
            intent[0].putExtra("value",flag);
            intent[0].putExtra("email", currentUser.getEmail());
                }
                finish();
                startActivity(intent[0]);
            }
        },SPLASH_TIME);
    }
}
