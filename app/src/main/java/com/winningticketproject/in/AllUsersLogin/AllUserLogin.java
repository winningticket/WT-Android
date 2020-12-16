package com.winningticketproject.in.AllUsersLogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winningticketproject.in.MyAccountTab.About_Us;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Login_Screen;
import com.winningticketproject.in.SignInSingup.Registration_form;

import cz.msebera.android.httpclient.protocol.HTTP;

import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class AllUserLogin extends AppCompatActivity implements View.OnClickListener {

    TextView sign_up_text,create_account,enter_code;
    LinearLayout layout_create_account ,layout_enter_code;
    Button button_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_login);

        initilize_all_fields();
    }

    private void initilize_all_fields() {

        sign_up_text = findViewById(R.id.sign_up_text);
        sign_up_text.setTypeface(semibold);

        button_sign_in = findViewById(R.id.button_sign_in);
        button_sign_in.setTypeface(medium);

        button_sign_in.setOnClickListener(this);

        create_account = findViewById(R.id.create_account);
        create_account.setTypeface(regular);
        create_account.setText(Html.fromHtml("<b><big>Create an account</big></b> <br><small> Sign up to start participating in events. \n Free everyday Play is also available.</small>"));

        enter_code = findViewById(R.id.enter_code);
        enter_code.setTypeface(regular);
        enter_code.setText(Html.fromHtml("<b><big>Enter player code</big></b> <br><small> Verify that the code you entered matches up with your information.</small>"));

        layout_create_account = findViewById(R.id.layout_create_account);
        layout_create_account.setOnClickListener(this);

        layout_enter_code = findViewById(R.id.layout_enter_code);
        layout_enter_code.setOnClickListener(this);

        sing_in_click_term_of_user();
    }

    private void sing_in_click_term_of_user() {

//        sign_up_text.setTypeface(italic);

        TextView privacy_policy = findViewById(R.id.privacy_policy);
        privacy_policy.setTypeface(regular);
        SpannableString privacy_policy_string = new SpannableString("By signing up, you are agree to Winning Ticket’s \n Terms of Service and Privacy Policy");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent in = new Intent(getApplicationContext(), About_Us.class);
                in.putExtra("page_type", "terms_of_use");
                startActivity(in);
            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent in = new Intent(getApplicationContext(), About_Us.class);
                in.putExtra("page_type", "privacy_policy");
                startActivity(in);
            }
        };

        privacy_policy_string.setSpan(clickableSpan1,49,66,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        privacy_policy_string.setSpan(new ForegroundColorSpan(Color.parseColor("#757575")),49, 66, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        privacy_policy_string.setSpan(clickableSpan2,71,85,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        privacy_policy_string.setSpan(new ForegroundColorSpan(Color.parseColor("#757575")),71, 85, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        privacy_policy.setMovementMethod(LinkMovementMethod.getInstance());
        privacy_policy.setText(privacy_policy_string);
    }

    @Override
    public void onClick(View v) {

        Intent in;

        switch (v.getId()) {

            case R.id.layout_create_account :
                in = new Intent(getApplicationContext(), Registration_form.class);
                startActivity(in);
                break;

            case R.id.layout_enter_code :
                in = new Intent(getApplicationContext() , Enter_Player_Code.class);
                startActivity(in);
                break;

            case R.id.button_sign_in :
                in = new Intent(getApplicationContext() , Login_Screen.class);
                startActivity(in);
                break;
        }
    }
}