package com.example.ravindra.onlineexamination;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ravindra.onlineexamination.galleryimages.UploadImage;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    TextView name;
    ListView listView;
    boolean flag = false;
    ImageView dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        dp = findViewById(R.id.homeImageView);
        name = findViewById(R.id.homePersonName);
        listView = findViewById(R.id.homeExamList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ExamPageDrower.class);
                startActivity(intent);
            }
        });
    /*  //  flag = getIntent().getBooleanExtra("value", false);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("flag", flag);
        editor.apply();
        flag = sharedPref.getBoolean("flag",true);*/
        user = mAuth.getCurrentUser();
//       String email = getIntent().getStringExtra("email");
        assert user != null;
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
      /* // if (!flag) {
            name.setText(user.getDisplayName());
            if (user.getPhotoUrl()!=null) {
                Glide.with(this).load(user.getPhotoUrl())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(dp);
         //   }*/
       // } else {
            downloadImg();
            dp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadDp();
                }
            });
            DocumentReference docRef = db.collection("users_info").document(uid);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DataModel dataModel = documentSnapshot.toObject(DataModel.class);
                    String name1 = dataModel.getName();
                    name.setText(name1);
                }
            });
      //  }
    }

    private void downloadImg() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://onlineexamination-51dc0.appspot.com/images/users/profile_pic").child(mAuth.getUid()+".jpg");
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
        } catch (IOException e ) {
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
}