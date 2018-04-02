package com.example.ravindra.onlineexamination;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravindra.onlineexamination.galleryimages.UploadImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wang.avi.AVLoadingIndicatorView;

public class SignUp extends AppCompatActivity {
    TextView nameT, emailT, phoneT, passwordT;
    TextView sinupToSignIn;
    Button signUpBtn;
    ImageView imageView;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        database = FirebaseDatabase.getInstance();
        nameT = findViewById(R.id.signUpName);
        emailT = findViewById(R.id.signUpEmail);
        phoneT = findViewById(R.id.signUpNumber);
        passwordT = findViewById(R.id.signUpPassword);
        sinupToSignIn = findViewById(R.id.signUpSendSignIn);
        signUpBtn = findViewById(R.id.signUpButton);
        imageView = findViewById(R.id.imageViewSignUp);
        mAuth = FirebaseAuth.getInstance();
        avi = findViewById(R.id.avi);
        avi.hide();
        sinupToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    private void uploadImage() {
        Toast.makeText(this, "upload image after signUp.", Toast.LENGTH_SHORT).show();
    }

    private void signUp() {
        String emailId = emailT.getText().toString();
        String pwd = passwordT.getText().toString();
        final String name = nameT.getText().toString();
        final String mobile = phoneT.getText().toString();
        if (emailId.isEmpty()||pwd.isEmpty()||name.isEmpty()||mobile.isEmpty()){
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
        }
        else if (!isEmailValid(emailId)){
            Toast.makeText(this, "please type a valid Email Address", Toast.LENGTH_SHORT).show();
        }
        else if (pwd.length()<6){
            Toast.makeText(this, "Your password is too weak, Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
        }
        else {
            avi.show();
            mAuth.createUserWithEmailAndPassword(emailId, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("aaa", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                avi.hide();
                                updateUI(user);
                                saveData(user,name,mobile);
                                createMenu(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("aaa", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                avi.hide();
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }

    private void createMenu(FirebaseUser user) {
        String uid = user.getUid();
        DatabaseReference myRef = database.getReference("users/"+uid+"/menu/");
        SetMenu menu = new SetMenu("Main Examination",false);
        myRef.child("1").setValue(menu);
        menu.setName("Test Examination");
        menu.setStatus(true);
        myRef.child("2").setValue(menu);
        menu.setName("Result");
        menu.setStatus(true);
        myRef.child("3").setValue(menu);
        menu.setName("Updates");
        menu.setStatus(true);
        myRef.child("4").setValue(menu);

    }

    private void saveData(FirebaseUser user, String name, String mobile) {
        String uid = user.getUid();
        DataModel dataModel = new DataModel(name,mobile);
        // Write a message to the database
        DatabaseReference myRef = database.getReference("users/"+uid+"/user_info");
        myRef.setValue(dataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(SignUp.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
      /*  Toast.makeText(this, "saving data...", Toast.LENGTH_SHORT).show();
        db.collection("users_info").document(uid).set(dataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });*/
    }
    private void updateUI(FirebaseUser user) {
        if (user == null){
            Toast.makeText(this, "User Can't Created", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "User Created Successfully...", Toast.LENGTH_SHORT).show();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
