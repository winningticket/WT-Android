package com.winningticketproject.in.MyAccountTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.winningticketproject.in.R;

/**
 * Created by carmatec on 28/8/17.
 */

public class Profile_Image extends AppCompatActivity {
    String pro_image;
    Intent intent;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_image);
        imageView= findViewById(R.id.imageView);

        TextView account_title = findViewById(R.id.account_title);
        account_title.setText("Profile Pic");

        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

        intent=getIntent();
        pro_image=intent.getStringExtra("avatar_url");
        Glide.with(getApplicationContext())
                .load(pro_image)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .into(imageView);

    }
}
