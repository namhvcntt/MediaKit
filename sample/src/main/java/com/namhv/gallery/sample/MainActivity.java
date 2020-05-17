package com.namhv.gallery.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.namhv.gallery.activities.GalleryActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 100;
    public static final int PICK_VIDEO = 101;
    private ImageView mIvResult;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIvResult = findViewById(R.id.iv_result);
        rxPermissions = new RxPermissions(this);
        findViewById(R.id.btn_pick_image).setOnClickListener(v -> {
            pickImage();

        });

        findViewById(R.id.btn_pick_video).setOnClickListener(v -> {
            pickVideo();
        });
    }

    void pickImage() {
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                intent.setAction(GalleryActivity.PICK_IMAGE);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

    }

    void pickVideo() {
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                intent.setAction(GalleryActivity.PICK_VIDEO);
                startActivityForResult(intent, PICK_VIDEO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (PICK_IMAGE == requestCode) {
                Log.e("MainActivity", "OnActivityResult:");
                final String path = data.getData().getPath();
                final Uri uri = Uri.fromFile(new File(path));
                Glide.with(this)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(mIvResult);
                return;
            } else if (PICK_VIDEO == requestCode) {
                final String path = data.getData().getPath();
                final Uri uri = Uri.fromFile(new File(path));
                Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
