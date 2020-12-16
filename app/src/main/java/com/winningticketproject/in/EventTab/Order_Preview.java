package com.winningticketproject.in.EventTab;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.CourseTab.Courses_list_flow;
import com.winningticketproject.in.Activity.Select_hole;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.decimal_amount;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class Order_Preview extends AppCompatActivity implements View.OnClickListener {
    TextView Account_balance_text;
    TextView account_balance_total_amount;
    Button preview_order2,preview_order;
    double price=0,discount_pric=0,wallete_balance=0,total_price;
    double totals;
    JSONObject jsonObject = new JSONObject();
    String auth_token="",nonce="",payment_source="",page="",wallet="",pageliveauction,pat_ment_type,total_amount_amoont;
    LinearLayout account_balance_layout;
    TextView full_address_payment,payment_detials,title_page,title_desciption;
    ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order__preview);

        //preview_order1 = (Button) findViewById(R.id.preview_order1);
        preview_order2 = findViewById(R.id.preview_order2);

        preview_order = findViewById(R.id.preview_order);
        preview_order.setOnClickListener(this);

        account_balance_layout = findViewById(R.id.layout_account_balnce);
        account_balance_layout.setVisibility(View.VISIBLE);

        Account_balance_text = findViewById(R.id.Account_balance_text);
        account_balance_total_amount = findViewById(R.id.account_balance_total_amount);
        Account_balance_text.setTypeface(regular);
        account_balance_total_amount.setTypeface(regular);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        auth_token = get_auth_token("auth_token");

        full_address_payment = findViewById(R.id.full_address_payment);
        full_address_payment.setTypeface(regular);

        preview_order2.setOnClickListener(this);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("respose");
//        in.putExtra("wallet", "1");

        discount_pric = intent.getDoubleExtra("discount_pric",0);
        wallet = intent.getStringExtra("wallet");

        wallete_balance = intent.getDoubleExtra("wallete_balance",0);

        payment_source = intent.getStringExtra("payment-source");
        nonce = intent.getStringExtra("nonce");

        if(!wallet.equals("1"))
        {
            nonce = intent.getStringExtra("nonce");
        }

        page = intent.getStringExtra("page");
        pageliveauction = intent.getStringExtra("pageliveauction");
        pat_ment_type  = intent.getStringExtra("pat_ment_type");

        payment_detials = findViewById(R.id.payment_detials);
        payment_detials.setTypeface(regular);
        payment_detials.setText(pat_ment_type);

        if (page.equals("individual_play")){

            try {
                JSONObject jObj = new JSONObject(jsonString);
                JSONObject billingaddresss = jObj.getJSONObject("billing_address");
                price =jObj.getDouble("price");
                String finalprice="";
                double total_amount=0;
                finalprice = decimal_amount(price);
                total_price= Double.parseDouble(finalprice+"");

                if(wallet.equals("0"))
                {
                    total_amount = price;
                    total_amount_amoont = decimal_amount(total_amount);
                    account_balance_layout.setVisibility(View.GONE);

                }else if (wallet.equals("1"))
                {
                    account_balance_layout.setVisibility(View.VISIBLE);
                    double wallete_total_amount =  price ;
                    String wallete_balances = decimal_amount(wallete_total_amount);
                    account_balance_total_amount.setText("- $"+wallete_balances+"");
                    total_amount = price - wallete_total_amount;
                    total_amount_amoont = decimal_amount(total_amount);

                }else if (wallet.equals("2"))
                {
                    account_balance_layout.setVisibility(View.VISIBLE);
                    String wallete_balances = decimal_amount(wallete_balance);
                    account_balance_total_amount.setText("- $"+wallete_balances+"");
                    total_amount = price - wallete_balance;
                    total_amount_amoont = decimal_amount(total_amount);
                }

                String address2="";
                try {
                    address2 = billingaddresss.getString("address_line2").substring(0, 1).toUpperCase() + billingaddresss.getString("address_line2").substring(1);
                }
                catch (Exception e)
                {
                    //nothing
                }
                String name =billingaddresss.getString("first_name").replaceAll("\\s+"," ");
                String last_name = billingaddresss.getString("last_name").replaceAll("\\s+"," ");
                String address1 = billingaddresss.getString("address_line1").replaceAll("\\s+"," ");
                String city = billingaddresss.getString("city").replaceAll("\\s+"," ");
                String states =jObj.getString("state");
                String zipcode = billingaddresss.getString("zip_code").replaceAll("\\s+"," ")+"\n";
                String pno = billingaddresss.getString("phone");
                String country = jObj.getString("country");
                if(name.equals("null") || name.equals("") || name.equals(null)){
                    name="";
                }else {
                    name=name.substring(0,1).toUpperCase() +name.substring(1);
                }
                if(last_name.equals("null") || last_name.equals("") || last_name.equals(null)){
                    last_name="";
                }else {
                    last_name=last_name.substring(0,1).toUpperCase() +last_name.substring(1)+", ";
                }
                try{
                    address1=address1.substring(0,1).toUpperCase() +address1.substring(1);
                }
                catch (Exception e)
                {
                }
                if(address2.equals("null") || address2.equals("") || address2.equals(null)){
                    address2="";
                }else {
                    address2=address2.substring(0,1).toUpperCase() +address2.substring(1).trim().replaceAll("\\s+"," ")+", \n";
                }
                if(city.equals("null") || city.equals("") || city.equals(null)){
                    city="";
                }else {
                    city=city.substring(0,1).toUpperCase() +city.substring(1)+", ";
                }
                if(country.equals("null") || country.equals("") || country.equals(null)){
                    country="";
                }else {
                    country=country.substring(0,1).toUpperCase() +country.substring(1)+", \n";
                }
                if(states.equals("null") || states.equals("") || states.equals(null)){
                    states="";
                }else {
                    states=states.substring(0,1).toUpperCase() +states.substring(1)+", ";
                }

                full_address_payment.setText(name+" "+last_name+address1+" "+address2+city+""+states+""+zipcode+""+country+"Phone: "+pno);

                 } catch (JSONException e) {
                //nothing
            }

        }else if(pageliveauction.equals("1")){
            try {
                JSONObject jObj = new JSONObject(jsonString);
                JSONObject billingaddresss = jObj.getJSONObject("billing_address");

                totals = Double.parseDouble(get_auth_token("ticket_price"))* Integer.parseInt(get_auth_token("quantity"));
                String finalprice="";


                double total_amount =0;
                finalprice = decimal_amount(totals);
                total_price= Double.parseDouble(finalprice+"");

                if(wallet.equals("0"))
                {
                    total_amount = totals;
                    total_amount_amoont = decimal_amount(total_amount);
                    account_balance_layout.setVisibility(View.GONE);

                }else if (wallet.equals("1"))
                {
                    account_balance_layout.setVisibility(View.VISIBLE);
                    double wallete_total_amount =  totals ;
                    String wallete_balances = decimal_amount(wallete_total_amount);

                    account_balance_total_amount.setText("- $"+wallete_balances+"");
                    total_amount = totals - wallete_total_amount;
                    total_amount_amoont = decimal_amount(total_amount);

                }else if (wallet.equals("2"))
                {
                    account_balance_layout.setVisibility(View.VISIBLE);
                    String wallete_balances = decimal_amount(wallete_balance);
                    account_balance_total_amount.setText("- $"+wallete_balances+"");
                    total_amount = totals - wallete_balance;
                    total_amount_amoont = decimal_amount(total_amount);
                }

                String address2="";


                try {
                    address2 = billingaddresss.getString("address_line2").substring(0, 1).toUpperCase() + billingaddresss.getString("address_line2").substring(1);
                }
                catch (Exception e)
                {
                    //nothing
                }
                String name =billingaddresss.getString("first_name").replaceAll("\\s+"," ");
                String last_name = billingaddresss.getString("last_name").replaceAll("\\s+"," ");
                String address1 = billingaddresss.getString("address_line1").replaceAll("\\s+"," ");
                String city = billingaddresss.getString("city").replaceAll("\\s+"," ");
                String states =jObj.getString("state");
                String zipcode = billingaddresss.getString("zip_code").replaceAll("\\s+"," ")+"\n";
                String pno = billingaddresss.getString("phone");
                String country = jObj.getString("country");

                if(name.equals("null") || name.equals("") || name.equals(null)){
                    name="";
                }else {
                    name=name.substring(0,1).toUpperCase() +name.substring(1);
                }

                if(last_name.equals("null") || last_name.equals("") || last_name.equals(null)){
                    last_name="";
                }else {
                    last_name=last_name.substring(0,1).toUpperCase() +last_name.substring(1)+", ";
                }


                try{
                    address1=address1.substring(0,1).toUpperCase() +address1.substring(1);
                }
                catch (Exception e)
                {

                }

                if(address2.equals("null") || address2.equals("") || address2.equals(null)){
                    address2="";
                }else {
                    address2=address2.substring(0,1).toUpperCase() +address2.substring(1).trim().replaceAll("\\s+"," ")+", \n";
                }

                if(city.equals("null") || city.equals("") || city.equals(null)){
                    city="";
                }else {
                    city=city.substring(0,1).toUpperCase() +city.substring(1)+", ";
                }


                if(country.equals("null") || country.equals("") || country.equals(null)){
                    country="";
                }else {
                    country=country.substring(0,1).toUpperCase() +country.substring(1)+", \n";
                }


                if(states.equals("null") || states.equals("") || states.equals(null)){
                    states="";
                }else {
                    states=states.substring(0,1).toUpperCase() +states.substring(1)+", ";
                }

                full_address_payment.setText(name+" "+last_name+address1+""+address2+city+""+states+""+zipcode+""+country+"Phone: "+pno);


            } catch (JSONException e) {
                //nothing
            }

        }else if(page.equals("donate_fund"))
        {
            try {
                JSONObject jObj = new JSONObject(jsonString);
                JSONObject billingaddresss = jObj.getJSONObject("billing_address");
                price =jObj.getDouble("price");
                String finalprice="";

                double total_amount=0;
                finalprice = decimal_amount(price);
                total_price= Double.parseDouble(finalprice+"");

                if(wallet.equals("0"))
                {
                    total_amount = price;
                    total_amount_amoont = decimal_amount(total_amount);
                    account_balance_layout.setVisibility(View.GONE);

                }else if (wallet.equals("1"))
                {
                    account_balance_layout.setVisibility(View.VISIBLE);
                    double wallete_total_amount =  price ;
                    String wallete_balances = decimal_amount(wallete_total_amount);

                    account_balance_total_amount.setText("- $"+wallete_balances+"");
                    total_amount = price - wallete_total_amount;
                    total_amount_amoont = decimal_amount(total_amount);

                }else if (wallet.equals("2"))
                {
                    account_balance_layout.setVisibility(View.VISIBLE);
                    String wallete_balances = decimal_amount(wallete_balance);
                    account_balance_total_amount.setText("- $"+wallete_balances+"");
                    total_amount = price - wallete_balance;
                    total_amount_amoont = decimal_amount(total_amount);
                }

                String address2="";

                try {
                    address2 = billingaddresss.getString("address_line2").substring(0, 1).toUpperCase() + billingaddresss.getString("address_line2").substring(1);
                }
                catch (Exception e)
                {
                    //nothing
                }

                String name =billingaddresss.getString("first_name").replaceAll("\\s+"," ");
                String last_name = billingaddresss.getString("last_name").replaceAll("\\s+"," ");
                String address1 = billingaddresss.getString("address_line1").replaceAll("\\s+"," ");
                String city = billingaddresss.getString("city").replaceAll("\\s+"," ");
                String states =jObj.getString("state");
                String zipcode = billingaddresss.getString("zip_code").replaceAll("\\s+"," ")+"\n";
                String pno = billingaddresss.getString("phone");
                String country = jObj.getString("country");

                if(name.equals("null") || name.equals("") || name.equals(null)){
                    name="";
                }else {
                    name=name.substring(0,1).toUpperCase() +name.substring(1);
                }

                if(last_name.equals("null") || last_name.equals("") || last_name.equals(null)){
                    last_name="";
                }else {
                    last_name=last_name.substring(0,1).toUpperCase() +last_name.substring(1)+", ";
                }


                try{
                    address1=address1.substring(0,1).toUpperCase() +address1.substring(1);
                }
                catch (Exception e)
                {

                }

                if(address2.equals("null") || address2.equals("") || address2.equals(null)){
                    address2="";
                }else {
                    address2=address2.substring(0,1).toUpperCase() +address2.substring(1).trim().replaceAll("\\s+"," ")+", \n";
                }

                if(city.equals("null") || city.equals("") || city.equals(null)){
                    city="";
                }else {
                    city=city.substring(0,1).toUpperCase() +city.substring(1)+", ";
                }


                if(country.equals("null") || country.equals("") || country.equals(null)){
                    country="";
                }else {
                    country=country.substring(0,1).toUpperCase() +country.substring(1)+", \n";
                }


                if(states.equals("null") || states.equals("") || states.equals(null)){
                    states="";
                }else {
                    states=states.substring(0,1).toUpperCase() +states.substring(1)+", ";
                }

                full_address_payment.setText(name+" "+last_name+address1+""+address2+city+""+states+""+zipcode+""+country+"Phone: "+pno);

            } catch (JSONException e) {
                //nothing
            }
        }
        else if(page.equals("purchase_wt")) {
            try {
                JSONObject jObj = new JSONObject(jsonString);
                JSONObject billingaddresss = jObj.getJSONObject("billing_address");

                totals = Double.parseDouble(get_auth_token("ticket_price"))* Integer.parseInt(get_auth_token("quantity"));
                String finalprice="";

                double total_amount=0;

                finalprice = decimal_amount(totals);
                total_price= Double.parseDouble(finalprice+"");

                if(wallet.equals("0"))
                {
                    total_amount = totals-discount_pric;
                    total_amount_amoont = decimal_amount(total_amount);
                    account_balance_layout.setVisibility(View.GONE);

                }else if (wallet.equals("1"))
                {
                    double wallete_total_amount =  totals -  discount_pric ;
                    String wallete_balances = decimal_amount(wallete_total_amount);

                    if (wallete_total_amount>0){
                        account_balance_layout.setVisibility(View.VISIBLE);
                        account_balance_total_amount.setText("- $"+wallete_balances+"");
                    }else {
                        account_balance_layout.setVisibility(View.GONE);
                    }
                    total_amount = totals - wallete_total_amount - discount_pric;
                    total_amount_amoont = decimal_amount(total_amount);

                }else if (wallet.equals("2"))
                {
                    String wallete_balances = decimal_amount(wallete_balance);
                    if (wallete_balance>0){
                        account_balance_layout.setVisibility(View.VISIBLE);
                        account_balance_total_amount.setText("- $"+wallete_balances+"");
                    }else {
                        account_balance_layout.setVisibility(View.GONE);
                        account_balance_total_amount.setText("- $"+wallete_balances+"");
                    }
                    total_amount = totals - wallete_balance - discount_pric;
                    total_amount_amoont = decimal_amount(total_amount);

                }

                String address2="";


                try {
                    address2 = billingaddresss.getString("address_line2").substring(0, 1).toUpperCase() + billingaddresss.getString("address_line2").substring(1);
                }
                catch (Exception e)
                {
                }

                String name =billingaddresss.getString("first_name").replaceAll("\\s+"," ");
                String last_name = billingaddresss.getString("last_name").replaceAll("\\s+"," ");
                String address1 = billingaddresss.getString("address_line1").replaceAll("\\s+"," ");
                String city = billingaddresss.getString("city").replaceAll("\\s+"," ");
                String states =jObj.getString("state");
                String zipcode = billingaddresss.getString("zip_code").replaceAll("\\s+"," ")+"\n";
                String pno = billingaddresss.getString("phone");
                String country = jObj.getString("country");

                if(name.equals("null") || name.equals("") || name.equals(null)){
                    name="";
                }else {
                    name=name.substring(0,1).toUpperCase() +name.substring(1);
                }

                if(last_name.equals("null") || last_name.equals("") || last_name.equals(null)){
                    last_name="";
                }else {
                    last_name=last_name.substring(0,1).toUpperCase() +last_name.substring(1)+", ";
                }

                try{
                    address1=address1.substring(0,1).toUpperCase() +address1.substring(1);
                }
                catch (Exception e)
                { }

                if(address2.equals("null") || address2.equals("") || address2.equals(null)){
                    address2="";
                }else {
                    address2=address2.substring(0,1).toUpperCase() +address2.substring(1).trim().replaceAll("\\s+"," ")+", \n";
                }

                if(city.equals("null") || city.equals("") || city.equals(null)){
                    city="";
                }else {
                    city=city.substring(0,1).toUpperCase() +city.substring(1)+", ";
                }

                if(country.equals("null") || country.equals("") || country.equals(null)){
                    country="";
                }else {
                    country=country.substring(0,1).toUpperCase() +country.substring(1)+", \n";
                }

                if(states.equals("null") || states.equals("") || states.equals(null)){
                    states="";
                }else {
                    states=states.substring(0,1).toUpperCase() +states.substring(1)+", ";
                }

                full_address_payment.setText(name+" "+last_name+address1+""+address2+city+""+states+""+zipcode+""+country+"Phone: "+pno);

            } catch (JSONException e) {
                //nothing
            }
        }

        section_one();

    }


    public void section_one() {

        TextView home_icon = findViewById(R.id.home_icon);
        home_icon.setTypeface(webfont);

        TextView card_icon = findViewById(R.id.card_icon);
        card_icon.setTypeface(webfont);

        TextView billing_address = findViewById(R.id.billing_address);
        billing_address.setTypeface(medium);

        TextView payment_method = findViewById(R.id.payment_method);
        payment_method.setTypeface(medium);


        title_page = findViewById(R.id.title_page);
        title_page.setTypeface(medium);
        title_page.setText("Order Review");

        title_desciption = findViewById(R.id.title_desciption);
        title_desciption.setTypeface(regular);


        ImageView event_logo = findViewById(R.id.event_logo);

        if (get_auth_token("imge_url").equals("") || get_auth_token("imge_url").equals("null") || get_auth_token("imge_url").equals(null)){
            savestring("imge_url","null");
        }

         Picasso.with(Order_Preview.this)
                    .load(get_auth_token("imge_url")).error(R.drawable.auto_image).placeholder(R.drawable.logo)
                    .into(event_logo);


        TextView event_access_name = findViewById(R.id.payment_access_name);
        event_access_name.setTypeface(medium);

        TextView payment_event_name = findViewById(R.id.payment_event_name);
        payment_event_name.setTypeface(regular);
        payment_event_name.setText(get_auth_token("name"));

        TextView include_title = findViewById(R.id.payment_include_title);
        include_title.setTypeface(medium);
        include_title.setPaintFlags(include_title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView payment_include_data = findViewById(R.id.payment_include_data);
        payment_include_data.setTypeface(regular);

        if (get_auth_token("premium_type").equals("month") || get_auth_token("premium_type").equals("year")){
            event_access_name.setText("Premium Membersip");
            payment_event_name.setVisibility(View.GONE);
            include_title.setVisibility(View.GONE);
            payment_include_data.setVisibility(View.GONE);
        }

        if (page.equals("donate_fund")){
            payment_include_data.setVisibility(View.GONE);
            include_title.setVisibility(View.GONE);
        }else {

            payment_include_data.setText(get_auth_token("aminities"));

        }

        method_to_calculate();

    }

    public void method_to_calculate(){

        TextView payment_price_title = findViewById(R.id.payment_price_title);
        payment_price_title.setTypeface(regular);

        int qty = Integer.parseInt(get_auth_token("quantity"));
        if (qty>1){
            payment_price_title.setText("Purchase Price (X"+qty+")");
        }

        String final_total_amount = String.valueOf((Double.parseDouble(get_auth_token("ticket_price")) * Integer.parseInt(get_auth_token("quantity"))));


        TextView payment_amount = findViewById(R.id.payment_amount);
        payment_amount.setTypeface(regular);
        payment_amount.setText("$"+decimal_amount(Double.parseDouble(final_total_amount)));

        TextView payment_processing_title = findViewById(R.id.payment_processing_title);
        payment_processing_title.setTypeface(regular);

        TextView payment_total_title = findViewById(R.id.payment_total_title);
        payment_total_title.setTypeface(medium);

        total_price = Double.parseDouble(decimal_amount(Double.parseDouble(final_total_amount)));

        TextView payment_total_amount = findViewById(R.id.payment_total_amount);
        payment_total_amount.setTypeface(medium);
        payment_total_amount.setText("$"+total_amount_amoont);


    }


    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.btn_back:
                finish();
                break;

            case R.id.preview_order:

                if (get_auth_token("user_type").equals("free")){
                    finish();
                }else {
                    if (pageliveauction.equals("3")) {
                        Intent intent1 = new Intent(getApplicationContext(), Select_hole.class);
                        startActivity(intent1);
                        finish();
                    } else if (pageliveauction.equals("1")) {
                        finish();
                    } else {
                        if (page.equals("donate_fund")) {
                            finish();
                        } else if (page.equals("purchase_wt") || page.equals("individual_play")) {
                            Intent in = new Intent(getApplicationContext(), Courses_list_flow.class);
                            startActivity(in);
                            finish();
                            finishAffinity();
                        }
                    }
                }

                break;

            case R.id.preview_order2:

                try {
                    if (Share_it.get_auth_token("play_type").equals("paid")){

                        if(wallet.equals("0"))
                        {
                            if (payment_source.length()>0){
                                jsonObject.put("payment-source", payment_source);
                            }else {
                                jsonObject.put("nonce", nonce);
                            }

                            jsonObject.put("transaction_type", "premium");
                        }
                        else if(wallet.equals("1"))
                        {
                            jsonObject.put("transaction_type", "premium");
                            // jsonObject.put("fund_amount","yes");
                        }
                        else if(wallet.equals("2"))
                        {
                            if (payment_source.length()>0){
                                jsonObject.put("payment-source", payment_source);
                            }else {
                                jsonObject.put("nonce", nonce);
                            }

                            jsonObject.put("transaction_type", "premium");
                        }


                    }else if(page.equals("donate_fund"))
                    {
                        if(wallet.equals("0"))
                        {
                            if (payment_source.length()>0){
                                jsonObject.put("payment-source", payment_source);
                            }else {
                                jsonObject.put("nonce", nonce);
                            }
                        }
                        else if(wallet.equals("2"))
                        {
                            if (payment_source.length()>0){
                                jsonObject.put("payment-source", payment_source);
                            }else {
                                jsonObject.put("nonce", nonce);
                            }
//                      jsonObject.put("fund_amount","yes");
                        }
                    }

                    else if(page.equals("purchase_wt")) {
                        if(wallet.equals("0"))
                        {
                            if (payment_source.length()>0){
                                jsonObject.put("payment-source", payment_source);
                            }else {
                                jsonObject.put("nonce", nonce);
                            }
                            jsonObject.put("transaction_type", "Purchase");
                            jsonObject.put("price", total_price);
                        }
                        else if(wallet.equals("1"))
                        {
                            jsonObject.put("transaction_type", "Purchase");
                            jsonObject.put("price", total_price);
                            // jsonObject.put("fund_amount","yes");
                        }
                        else if(wallet.equals("2"))
                        {
                            if (payment_source.length()>0){
                                jsonObject.put("payment-source", payment_source);
                            }else {
                                jsonObject.put("nonce", nonce);
                            }

                            jsonObject.put("transaction_type", "Purchase");
                            jsonObject.put("price", total_price);//                      jsonObject.put("fund_amount","yes");
                        }
                    }

                    if (!isNetworkAvailable()) {

                        alertdailogbox();
                    }
                    else
                    {
                        Apicall(jsonObject);
                    }

                }catch (Exception e){
                    //nothing
                }
                //
                break;

        }
    }

    private void Apicall(JSONObject jsonObject) {

        System.out.println("-------payments/create----"+jsonObject);

       ProgressDialog.getInstance().showProgress(Order_Preview.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"payments/create",jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("res"+response);

                        try {
                            ProgressDialog.getInstance().hideProgress();
                            if (response.getString("status").equals("Success")){

                                title_page.setText("Order #"+response.getString("order_id")+" confirmed");
                                title_desciption.setText("You now have access to your event! We’ve sent a confirmation to the email address you signed up with.");

                                preview_order.setVisibility(View.VISIBLE);
                                preview_order2.setVisibility(View.GONE);

                                btn_back.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), "" + response.getString("message"), Toast.LENGTH_LONG).show();

                            }else {
                                Toast.makeText(getApplicationContext(),""+response.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception E){
                            //nothing
                            ProgressDialog.getInstance().hideProgress();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                ProgressDialog.getInstance().hideProgress();
                                Alert_Dailog.showNetworkAlert(Order_Preview.this);
                            }else {
                                ProgressDialog.getInstance().hideProgress();
                                Toast.makeText(Order_Preview.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(Order_Preview.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("auth-token", auth_token);
                return headers;
            }

        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");


    }
    public void alertdailogbox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("No Internet");
        builder.setMessage("Internet is required. Please Retry.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                if (!isNetworkAvailable()) {

                    alertdailogbox();
                }
                else
                {

                    Apicall(jsonObject);
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }
}
