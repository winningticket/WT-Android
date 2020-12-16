package com.winningticketproject.in.MyAccountTab;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winningticketproject.in.R;

public class How_it_works  extends AppCompatActivity implements View.OnClickListener {

    TextView cancel_purchage,account_title,charity_text,enter_charity_text,sponsor_charity_text,hit_text1,hit_text2,hit_text3,hit_text4,hit_text5,hit_text6,hit_text7,hit_text8,hit_text9;
    Typeface textfont,book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_how_it_works);

        book = Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
        textfont = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");

        cancel_purchage = findViewById(R.id.cancel_purchage);
        cancel_purchage.setOnClickListener(this);

        account_title = findViewById(R.id.account_title);
        account_title.setText("How It Works");


        cancel_purchage.setTypeface(textfont);
        account_title.setTypeface(textfont);

        charity_text = findViewById(R.id.charity_text);
        enter_charity_text = findViewById(R.id.enter_charity_text);
        sponsor_charity_text = findViewById(R.id.sponsor_charity_text);

        charity_text.setTypeface(textfont);
        enter_charity_text.setTypeface(textfont);
        sponsor_charity_text.setTypeface(textfont);

        charity_text.setText(Html.fromHtml("<b>" + "CREATE "+ "</b>" + "A CHARITY EVENT"));
        enter_charity_text.setText(Html.fromHtml("<b>" +"ENTER " +"</b>"+"A CHARITY EVENT"));
        sponsor_charity_text.setText(Html.fromHtml("<b>" +"SPONSOR " +"</b>"+"A CHARITY EVENT"));


        hit_text1 = findViewById(R.id.hit_text1);
        hit_text2 = findViewById(R.id.hit_text2);
        hit_text3 = findViewById(R.id.hit_text3);
        hit_text4 = findViewById(R.id.hit_text4);
        hit_text5 = findViewById(R.id.hit_text5);
        hit_text6 = findViewById(R.id.hit_text6);
        hit_text7 = findViewById(R.id.hit_text7);
        hit_text8 = findViewById(R.id.hit_text8);
        hit_text9 = findViewById(R.id.hit_text9);

        hit_text1.setTypeface(book);
        hit_text2.setTypeface(book);
        hit_text3.setTypeface(book);
        hit_text4.setTypeface(book);
        hit_text5.setTypeface(book);
        hit_text6.setTypeface(book);
        hit_text7.setTypeface(book);
        hit_text8.setTypeface(book);
        hit_text9.setTypeface(book);

        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_purchage:
                finish();
                break;
        }
    }

}
