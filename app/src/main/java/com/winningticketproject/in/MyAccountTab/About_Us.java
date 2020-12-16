package com.winningticketproject.in.MyAccountTab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.winningticketproject.in.R;

import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

// Sub branch and master branch testing. Courses_list_flow test
public class About_Us extends AppCompatActivity  {
    TextView account_title, work_description;
    ImageView cancel_purchage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__us);

        cancel_purchage = findViewById(R.id.cancel_purchage);

        work_description = findViewById(R.id.work_description);
        account_title = findViewById(R.id.account_title);

        work_description.setTypeface(regular);
        account_title.setTypeface(semibold);


        cancel_purchage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

        String page_type = getIntent().getStringExtra("page_type");

        if (page_type.equals("about_us")) {

            account_title.setText("About Us");
            work_description.setText(Html.fromHtml(getString(R.string.About_us)));

        } else if (page_type.equals("terms_of_use")) {

            account_title.setText("Terms of Service");
            work_description.setText(Html.fromHtml(getString(R.string.Term_of_use)));

        } else {
            account_title.setText("Privacy Policy");
            work_description.setText(Html.fromHtml(getString(R.string.Privacy_policy)));
        }

    }
}
