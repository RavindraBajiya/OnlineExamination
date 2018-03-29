package com.example.ravindra.onlineexamination.galleryimages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.ravindra.onlineexamination.R;

/**
 * Created by Ravindra on 3/15/2018.
 */

public class PhotosActivity extends AppCompatActivity {
    int int_position;
    private GridView gridView;
    GridViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);
        gridView = (GridView) findViewById(R.id.gv_folder);
        int_position = getIntent().getIntExtra("value", 0);
        adapter = new GridViewAdapter(this, UploadImage.al_images, int_position);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PhotosActivity.this, "position " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PhotosActivity.this, PhotoShow.class);
                intent.putExtra("value", position);
                intent.putExtra("value1", int_position);
                startActivity(intent);
            }
        });
    }
}
