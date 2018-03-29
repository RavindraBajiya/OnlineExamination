package com.example.ravindra.onlineexamination.galleryimages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ravindra.onlineexamination.MainActivity;
import com.example.ravindra.onlineexamination.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class PhotoShow extends AppCompatActivity {
    ImageView imageView;
    Button uploadImg;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    ArrayList<Model_images> al_menu = UploadImage.al_images;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_show);
        uploadImg = (Button) findViewById(R.id.upload);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imageView = (ImageView) findViewById(R.id.imageView);
        final int value = getIntent().getIntExtra("value", 0);
        final int int_position = getIntent().getIntExtra("value1", 0);
        Glide.with(this).load("file://" + al_menu.get(int_position).getAl_imagepath().get(value))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     Log.d("aaa", "Uploading Image...");
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                String name = "pofile_pic.jpg";
                Uri uri = Uri.fromFile(new File("file://" + al_menu.get(int_position).getAl_imagepath().get(value)));
                StorageReference storageReference = mStorageRef.child("images/users/uid/" + uid + "/" + name);
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.d("aaaa", downloadUrl.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("aaaa", "Failed " + e);
                    }
                });*/
                uploadImage(int_position, value);
            }
        });

    }

    private void uploadImage(int int_position, int value) {
        String filePath = "file://" + al_menu.get(int_position).getAl_imagepath().get(value);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/users/profile_pic/" + mAuth.getUid() + ".jpg");
            ref.putFile(Uri.parse(filePath))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(PhotoShow.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PhotoShow.this,MainActivity.class);
                            finishFromChild(PhotoShow.this);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PhotoShow.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            progressDialog.setCancelable(false);
                        }
                    });
        }
    }
}
