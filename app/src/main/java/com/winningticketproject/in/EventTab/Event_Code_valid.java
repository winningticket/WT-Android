package com.winningticketproject.in.EventTab;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winningticketproject.in.AuctionModel.NewLive_Auction;
import com.winningticketproject.in.EventPurchase.Checkout_Page;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Iterator;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.AppInfo.Share_it.ConvertDate;
import static com.winningticketproject.in.AppInfo.Share_it.decimal_amount;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class Event_Code_valid extends AppCompatActivity implements View.OnClickListener,Animation.AnimationListener {
    String id;
    String name;
    String start_date;
    String ticket_price;
    String code;
    String location;
    String organization_name;
    TextView ticket_details, text_event_name, event_code, event_expand, arrow, ticket_time, event_course_name, access_code, need_process, include_title, include_data;
    ImageButton btn_back;
    View onExpand_text;
    boolean expand;
    ImageView imageid ,image_event_bg ;
    Button btn_purchase_ticket;
    Animation animFadein,sidedown;
    CardView card_not_purchased,card_auction_purchased;
    String  amenities="",event_images;
    LinearLayout is_live_layout ,linear_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_details);

        is_live_layout = findViewById(R.id.is_live_layout);
        is_live_layout.setVisibility(View.VISIBLE);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        ticket_details = findViewById(R.id.ticket_details);
        ticket_details.setTypeface(medium);

        text_event_name = findViewById(R.id.text_event_name);
        text_event_name.setTypeface(regular);

        event_code = findViewById(R.id.event_code);
        event_code.setTypeface(regular);

        imageid = findViewById(R.id.imageid);

        need_process = findViewById(R.id.need_process);
        need_process.setTypeface(regular);

        include_data = findViewById(R.id.include_data);
        include_data.setTypeface(regular);

        linear_text = findViewById(R.id.linear_text);
        linear_text.setOnClickListener(this);

        image_event_bg = findViewById(R.id.image_event_bg);

        onExpand_text = findViewById(R.id.onExpand_text);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.side_down);
        animFadein.setAnimationListener(this);

        sidedown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.side_up);
        sidedown.setAnimationListener(this);

        card_not_purchased = findViewById(R.id.card_not_purchased);
        card_not_purchased.setVisibility(View.VISIBLE);

        card_auction_purchased = findViewById(R.id.card_auction_purchased);
        card_auction_purchased.setVisibility(View.VISIBLE);

        TextView silent_auction = findViewById(R.id.silent_auction);
        silent_auction.setTypeface(medium);

        event_expand = findViewById(R.id.event_expand);
        event_expand.setTypeface(medium);

        card_auction_purchased.setOnClickListener(this);

        include_title = findViewById(R.id.include_title);
        include_title.setTypeface(medium);
        include_title.setPaintFlags(include_title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ticket_time = findViewById(R.id.ticket_time);
        ticket_time.setTypeface(regular);

        event_course_name = findViewById(R.id.event_course_name);
        event_course_name.setTypeface(regular);

        access_code = findViewById(R.id.access_code);
        access_code.setTypeface(medium);

        btn_purchase_ticket = findViewById(R.id.btn_purchase_ticket);
        btn_purchase_ticket.setTypeface(regular);

        btn_purchase_ticket.setOnClickListener(this);

        arrow = findViewById(R.id.arrow);
        arrow.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_blue_24dp));

        id = get_auth_token("id");
        name = get_auth_token("name");
        location = get_auth_token("location");
        ticket_price = get_auth_token("ticket_price");
        code = get_auth_token("code");
        start_date = get_auth_token("start_date");
        organization_name = get_auth_token("organization_name");
        String get_course_name = get_auth_token("get_course_name");
        String banner_image = get_auth_token("banner_image");

        event_images = get_auth_token("imge_url")+"";
        Picasso.with(Event_Code_valid.this)
                .load(event_images).error(R.drawable.black_cursor).placeholder(R.drawable.logo)
                .into(imageid);

        try {
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        } catch (Exception e) {
        }

       need_process.setText(Html.fromHtml("Net proceeds from this event benefit <b>"+organization_name+"</b> Thank you for your support!"));

        ticket_details.setText(name.trim());

        String event_code1 = "<b>" + "Event code: " + "</b>"+ code;

        event_code.setText(Html.fromHtml(event_code1));

        Picasso.with(Event_Code_valid.this)
                .load(banner_image+ "").error(R.drawable.auto_image).placeholder(R.drawable.auto_image)
                .into(image_event_bg);

        ticket_price = decimal_amount(Double.parseDouble(ticket_price));
        String text = "<font color=#000000>Event Access: </font> <font color=#00AC2C>"+String.format(ticket_price)+"</font>";
        access_code.setText(Html.fromHtml(text));

        java.util.Date date = ConvertDate(start_date);

        java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("EEEE MMMM dd");
        java.text.SimpleDateFormat postFormater2 = new java.text.SimpleDateFormat("HH:mm aa");
        String newDateStr = postFormater.format(date);
        String date_and_time = postFormater2.format(date).replace("am", "AM").replace("pm", "PM");

        String date_event = newDateStr + " @ " + date_and_time + " EST";
        String newString = date_event.replace("@", "-");
        String asubstring;

        String[] str1 = newString.split("-");
        String String = str1[0];
        String[] str = String.split(" ");

        asubstring  = str[1]+" "+ str[2]  + ", " + Calendar.getInstance().get(Calendar.YEAR) + " -" + str1[1];

        ticket_time.setText(Html.fromHtml("<b>" + "Event Date: " + "</b>"+asubstring));

        if (location.equals("null") || location.equals("") || location.equals(null)) {
            event_course_name.setText("Not Mentioned");
        } else {
            String[] aray = location.split(",");
            String split_data="";
            if (aray.length>2) {
                split_data = aray[0] + "<br>" + aray[1] + "," + aray[2];
            }else if (aray.length==2){
                split_data = aray[0] + "<br>" + aray[1];
            }else {
                split_data = location;
            }
            event_course_name.setText(Html.fromHtml("<big>"+ "<b>"+get_course_name+"</b>"+"</big>"+"<br>"+split_data));
        }

        if (organization_name.equals("null") || organization_name.equals("") || organization_name.equals(null)){
            text_event_name.setText("Not Mentioned");
        }else {
            String locations = organization_name.substring(0,1).toUpperCase() +organization_name.substring(1);
            text_event_name.setText(locations.replaceAll("\\s+"," "));
        }

        try {
            amenities="Live Scoring & GPS \nSilent Auction Entry \nEvent Live Chat \nVirtual Gift Bag\n";
            String ticket_amenities_value = get_auth_token("aminities");
            // amenities response
            JSONObject ticket_amenities = new JSONObject(ticket_amenities_value);

            Iterator<String> keys = ticket_amenities.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = ticket_amenities.getString(key);
                amenities += value + " " + key + "\n";
            }
            include_data.setText(amenities);

        }catch (org.json.JSONException exception){
            include_data.setText(amenities);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_purchase_ticket:
                Share_it.savestring("play_type","event");
                Share_it.savestring("aminities",amenities);
                savestring("premium_type","");
                Intent in = new Intent(getApplicationContext(), Checkout_Page.class);
                startActivity(in);
                break;

            case R.id.linear_text :
                if(expand) {
                    expand = false;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onExpand_text.startAnimation(animFadein);
                            event_expand.setText("click for event details");
                            arrow.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_blue_24dp));

                            onExpand_text.setVisibility(View.GONE);
                        }
                    }, 500);

                }else {
                    expand = true;
                    onExpand_text.setVisibility(View.VISIBLE);

                    event_expand.setText("Hide event details");
                    arrow.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_blue_24dp));
                    onExpand_text.startAnimation(animFadein);

                }
                break;

            case R.id.card_auction_purchased:
                savestring("premium_type","");
                in=new Intent(this, NewLive_Auction.class);
                savestring("id",id);
                startActivity(in);
                break;
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

