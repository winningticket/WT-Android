package com.winningticketproject.in.AllUsersLogin;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winningticketproject.in.CourseTab.Courses_list_flow;
import com.winningticketproject.in.R;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.isLocationEnabled;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class VerifyGuestUser extends AppCompatActivity implements View.OnClickListener {

    TextView verify_text ,info_verify ,user_info_text;
    Button btn_continue ,btn_not_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_guest_user);

        initilize_all_fields();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initilize_all_fields() {

        verify_text = findViewById(R.id.verify_text);
        verify_text.setTypeface(semibold);

        info_verify = findViewById(R.id.info_verify);
        info_verify.setTypeface(regular);

        user_info_text = findViewById(R.id.user_info_text);
        user_info_text.setTypeface(regular);

        Bundle b = getIntent().getExtras();
        String code = b.getString("player_code");

        if((get_auth_token("partner_name").equals(""))) {
            user_info_text.setText(Html.fromHtml("<tr><th>First Name: </th><td><b>"+ get_auth_token("first_name") +"</b></td></tr>" +
                    "<br><br><tr><th>Last Name: </th><td><b>"+get_auth_token("last_name")+"</b></td></tr>" +
                    "<br><br><tr><th>Team: </th><td><b>"+get_auth_token("team_name")+"</b></td></tr> " +
                    "<br><br><tr><th>starting Hole: </th><td><b>"+get_auth_token("starting_hole")+"</b></td></tr> " +
                    "<br><br><tr><th>Player Code: </th><td><b>"+code+"</b></td></tr>"));
        }else {
            user_info_text.setText(Html.fromHtml("<tr><th>First Name: </th><td><b>"+ get_auth_token("first_name") +"</b></td></tr>" +
                    "<br><br><tr><th>Last Name: </th><td><b>"+get_auth_token("last_name")+"</b></td></tr>" +
                    "<br><br><tr><th>Partner Name: </th><td><b>"+get_auth_token("partner_name")+"</b></td></tr>" +
                    "<br><br><tr><th>Team: </th><td><b>"+get_auth_token("team_name")+"</b></td></tr> " +
                    "<br><br><tr><th>starting Hole: </th><td><b>"+get_auth_token("starting_hole")+"</b></td></tr> " +
                    "<br><br><tr><th>Player Code: </th><td><b>"+code+"</b></td></tr>"));
        }

         user_info_text.setTypeface(regular);

        btn_continue = findViewById(R.id.btn_continues);
        btn_continue.setTypeface(semibold);
        btn_continue.setOnClickListener(this);

        btn_not_me = findViewById(R.id.btn_this_not_me);
        btn_not_me.setTypeface(semibold);
        btn_not_me.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_this_not_me:
                finish();
                break;

            case R.id.btn_continues:
                    Intent in = new Intent(this , Courses_list_flow.class);
                    startActivity(in);
                    finish();
                    finishAffinity();
                break;
        }
    }
}
