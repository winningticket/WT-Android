package com.winningticketproject.in.SignInSingup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.winningticketproject.in.BuildConfig;
import com.winningticketproject.in.R;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class NewBefore_login extends AppCompatActivity implements View.OnClickListener {

    Button btn_signup,btn_login;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int[] layouts;
    boolean doubleBackToExitPressedOnce = false;
    TextView version_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before__login);
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);

        layouts = new int[]{
                R.layout.layout_one,
                R.layout.layout_two,
                R.layout.layout_three,
                R.layout.layout_four};
        addBottomDots(0);
        changeStatusBarColor();
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btn_signup.setTypeface(medium);
        btn_login.setTypeface(medium);

        btn_signup.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        version_name = findViewById(R.id.version_name);
        version_name.setTypeface(regular);
        version_name.setText("Version "+ BuildConfig.VERSION_NAME+"\n©2019 Winning Ticket, LLC.");
    }


    @Override
    public void onClick(View view) {
        Intent in;
        switch (view.getId()){
            case R.id.btn_signup:
                in = new Intent(getApplicationContext(),Registration_form.class);
                startActivity(in);
                break;

            case  R.id.btn_login:
                in = new Intent(getApplicationContext(),Login_Screen.class);
                startActivity(in);
                break;

        }
    }


    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[layouts.length];

        int colorsActive =getResources().getColor(R.color.btn_color);
        int colorsInactive =getResources().getColor(R.color.colorwhite);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            try {
                if(position==0) {
                    TextView text = findViewById(R.id.text);
                    text.setTypeface(medium);

                    TextView text2 = findViewById(R.id.text2);
                    text2.setTypeface(medium);

                    TextView text3 = findViewById(R.id.text3);
                    text3.setTypeface(medium);

                    TextView swipe_to_see = findViewById(R.id.swipe_to_see);
                    swipe_to_see.setTypeface(medium);
                }

                if (position==1){
                    TextView text_steps_1 = findViewById(R.id.text_steps_1);
                    text_steps_1.setTypeface(medium);

                    TextView text_enter_code = findViewById(R.id.text_enter_code);
                    text_enter_code.setTypeface(medium);
                }

                if (position==2){

                    TextView text_steps_2 = findViewById(R.id.text_steps_2);
                    text_steps_2.setTypeface(medium);

                    TextView text_purchase = findViewById(R.id.text_purchase);
                    text_purchase.setTypeface(medium);

                }

                if (position==3){

                    TextView text_steps_3 = findViewById(R.id.text_steps_3);
                    TextView text_access_goal = findViewById(R.id.text_access_goal);
                    TextView text_live_auction = findViewById(R.id.text_live_auction);
                    TextView text_silent_auction = findViewById(R.id.text_silent_auction);
                    TextView text_gift_voucher = findViewById(R.id.text_gift_voucher);
                    TextView text_multipurpose = findViewById(R.id.text_multipurpose);

                    text_steps_3.setTypeface(medium);
                    text_access_goal.setTypeface(medium);
                    text_live_auction.setTypeface(medium);
                    text_silent_auction.setTypeface(medium);
                    text_gift_voucher.setTypeface(medium);
                    text_multipurpose.setTypeface(medium);
                }

            }catch (Exception e){
                //nothing
            }


            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(),"Please click BACK again to exit",Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
