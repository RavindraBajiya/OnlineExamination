package com.example.ravindra.onlineexamination;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ravindra.onlineexamination.galleryimages.UploadImage;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    TextView name;
    ImageView dp;
    Button menu1, menu2, menu3, menu4;
    static FirebaseDatabase database;
    FirebaseDatabase database1;
    DataModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        database1 = MainActivity.databaseObject();
        menu1 = findViewById(R.id.HomeButtonOne);
        menu2 = findViewById(R.id.HomeButtonTwo);
        menu3 = findViewById(R.id.HomeButtonThree);
        menu4 = findViewById(R.id.HomeButtonFour);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
        dp = findViewById(R.id.homeImageView);
        name = findViewById(R.id.homePersonName);
        user = mAuth.getCurrentUser();
        assert user != null;
        String uid = user.getUid();
        getMenu();
        downloadImg();
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDp();
            }
        });
        DatabaseReference myRef = database1.getReference("users/" + uid + "/user_info");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name1 = dataSnapshot.child("name").getValue().toString();
                    String mobile = dataSnapshot.child("phone").getValue().toString();
                    model = new DataModel(name1, mobile);
                    name.setText(name1);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("aaa", "Failed to read value.", error.toException());
            }
        });
    }

    private void getMenu() {
        for (int i = 1; i <= 4; i++)
            getMenuItems(i);
    }

    private void getMenuItems(final int i) {
        DatabaseReference myRef = database.getReference("menu_list/" + i);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("rkb",dataSnapshot.toString());
                    switch(i){
                        case 1:
                            menu1.setText(dataSnapshot.getValue().toString());
                            menu1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            menu2.setText(dataSnapshot.getValue().toString());
                            menu2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            menu3.setText(dataSnapshot.getValue().toString());
                            menu3.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            menu4.setText(dataSnapshot.getValue().toString());
                            menu4.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error Accured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadImg() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://onlineexamination-51dc0.appspot.com/images/users/profile_pic").child(mAuth.getUid() + ".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    dp.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void uploadDp() {
        Intent intent = new Intent(this, UploadImage.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.logoutMenu) {
            if (user == null) {
                Toast.makeText(this, "Please Sign In First.", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                finish();
                startActivity(intent);
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.HomeButtonOne){
            DatabaseReference reference = database1.getReference("resister/"+model.getPhone());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        if (dataSnapshot.getValue().toString()=="true"){
                            Toast.makeText(MainActivity.this, "resistered User", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "not resistered user", Toast.LENGTH_SHORT).show();
                        }
                    }
                   else{
                        Toast.makeText(MainActivity.this, "not resistered", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "cancelled : "+databaseError, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (id == R.id.HomeButtonTwo){
            Intent intent = new Intent(this,ExaminationList.class);
            startActivity(intent);
        }
        else if(id == R.id.HomeButtonThree){
            Toast.makeText(this, "Third Button", Toast.LENGTH_SHORT).show();

        }
        else if (id == R.id.HomeButtonFour){
            Toast.makeText(this, "Fourth Button", Toast.LENGTH_SHORT).show();

        }
    }
    public static FirebaseDatabase databaseObject(){
        if(database==null){
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
   }
}