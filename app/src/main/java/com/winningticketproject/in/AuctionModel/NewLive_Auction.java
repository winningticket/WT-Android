package com.winningticketproject.in.AuctionModel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class NewLive_Auction extends AppCompatActivity implements View.OnClickListener{
    LinearLayout linearLayout,ongoing_layout,sold_layout,upcoming_layout;
    View ongoing_view,sold_view , upcoming_view;
    TextView ongoing_text,sold_text,upcoming_text, cancel_purchage,toolbar_title;
    String auth_token="",id="";
    Typeface regular,medium;
    String from="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_live__auction);

        Share_it share_it = new Share_it(this);
        auth_token = get_auth_token("auth_token");
        id = get_auth_token("event_id");

        regular = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
        medium = Typeface.createFromAsset(getAssets(), "Montserrat-Medium.ttf");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        cancel_purchage = findViewById(R.id.cancel_purchage);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Silent Auction");
        cancel_purchage.setOnClickListener(this);

        cancel_purchage.setTypeface(medium);
        toolbar_title.setTypeface(medium);

        linearLayout = findViewById(R.id.linearLayout);
        ongoing_layout = findViewById(R.id.ongoing_layout);
        sold_layout = findViewById(R.id.sold_layout);
        upcoming_layout = findViewById(R.id.upcoming_layout);

        ongoing_layout.setOnClickListener(this);
        sold_layout.setOnClickListener(this);
        upcoming_layout.setOnClickListener(this);

        ongoing_view = findViewById(R.id.ongoing_view);
        sold_view = findViewById(R.id.sold_view);
        upcoming_view = findViewById(R.id.upcoming_view);

        ongoing_text = findViewById(R.id.ongoing_text);
        sold_text = findViewById(R.id.sold_text);
        upcoming_text = findViewById(R.id.upcoming_text);
        ongoing_text.setTypeface(regular);
        sold_text.setTypeface(regular);
        upcoming_text.setTypeface(regular);

        Fragment profilefragment = new OnGoing_Auction_items();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, profilefragment);
        fragmentTransaction.commit();

        parseIntent(getIntent());

        cancel_purchage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (from.equals("app")) {
                    finish();
                } else if (from.equals("link")) {
                    Intent in = new Intent(getApplicationContext(), Splash_screen.class);
                    startActivity(in);
                    finish();
                }
                return false;
            }
        });

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
    }

    private void parseIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            from = "link";
            String recipeId = appLinkData.getLastPathSegment();
            Share_it.savestring ("Auction_id", recipeId);
            id = get_auth_token("Auction_id");

            if(auth_token.isEmpty()){
                Alert_Dailog.showNetworkAlert1(NewLive_Auction.this);
            }
                Fragment profilefragment = new OnGoing_Auction_items();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, profilefragment);
                fragmentTransaction.commit();

        }else {
            if (!isNetworkAvailable()) {
                Toast.makeText(this,"Internet not available",Toast.LENGTH_LONG).show();
            } else {
                id = get_auth_token("id");
                from = "app";

                Fragment profilefragment = new OnGoing_Auction_items();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, profilefragment);
                fragmentTransaction.commit();

            }
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ongoing_layout:
                Fragment profilefragment = new OnGoing_Auction_items();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, profilefragment);
                fragmentTransaction.commit();

                ongoing_view.setVisibility(View.VISIBLE);
                sold_view.setVisibility(View.INVISIBLE);
                upcoming_view.setVisibility(View.INVISIBLE);

                break;

            case R.id.upcoming_layout:

                Fragment soldfragment = new Closed_Auction_items();
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.frameLayout, soldfragment);
                fragmentTransaction1.commit();

                ongoing_view.setVisibility(View.INVISIBLE);
                sold_view.setVisibility(View.INVISIBLE);
                upcoming_view.setVisibility(View.VISIBLE);

                break;

            case R.id.sold_layout:
                Fragment closedfragment = new  Sold_Auction_items();
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.frameLayout, closedfragment);
                fragmentTransaction2.commit();

                ongoing_view.setVisibility(View.INVISIBLE);
                sold_view.setVisibility(View.VISIBLE);
                upcoming_view.setVisibility(View.INVISIBLE);

                break;

            case R.id.cancel_purchage:
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (from.equals("app")) {
            finish();
        } else if (from.equals("link")) {
            Intent in = new Intent(getApplicationContext(), Splash_screen.class);
            startActivity(in);
            finish();
        }

    }

    @Override
    public void onRestart() {
        super.onRestart();
    }
}
