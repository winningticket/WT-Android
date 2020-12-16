package com.winningticketproject.in.EventPurchase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.EventTab.Purchage_Winning_Ticket;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.decimal_amount;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
public class Checkout_Page extends AppCompatActivity implements View.OnClickListener{
    String id;
    String ticket_amount;
    String name;
    String order_item_id;
    String order_quantity;
    String order_price;
    String auth_token="";
    String wallet_status="";
    String order_id="";
    String imge_url;
    JSONObject user_code = new JSONObject();
    ImageButton btn_back;
    Button btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_checkout;
    int quantity;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_checkout__page);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        auth_token = get_auth_token("auth_token");

        ticket_amount = get_auth_token("ticket_price");
        name = get_auth_token("name");
        id = get_auth_token("id");

        imge_url = get_auth_token("imge_url");

        section_one();

        section_two();

        btn_checkout = findViewById(R.id.btn_checkout);
        btn_checkout.setTypeface(medium);
        btn_checkout.setOnClickListener(this);


    }

    private void section_two() {

        TextView payment_smartphone_title = findViewById(R.id.payment_smartphone_title);
        payment_smartphone_title.setTypeface(medium);

        TextView payment_smartphone_subtitle = findViewById(R.id.payment_smartphone_subtitle);
        payment_smartphone_subtitle.setTypeface(regular);
        String html = "Select the amount of <u>extra tickets</u> you would like to add to your order.";
        payment_smartphone_subtitle.setText(Html.fromHtml(html));

        btn_1 = findViewById(R.id.btn_1);
        btn_1.setTypeface(medium);
        btn_1.setOnClickListener(this);

        btn_2 = findViewById(R.id.btn_2);
        btn_2.setTypeface(medium);
        btn_2.setOnClickListener(this);

        btn_3 = findViewById(R.id.btn_3);
        btn_3.setTypeface(medium);
        btn_3.setOnClickListener(this);

        btn_4 = findViewById(R.id.btn_4);
        btn_4.setTypeface(medium);
        btn_4.setOnClickListener(this);

        btn_5 = findViewById(R.id.btn_5);
        btn_5.setTypeface(medium);
        btn_5.setOnClickListener(this);

        btn_6 = findViewById(R.id.btn_6);
        btn_6.setTypeface(medium);
        btn_6.setOnClickListener(this);


        quantity=1;
        btn_1.setBackgroundResource(R.drawable.blue_text_drawble);

        method_to_calculate(1.0);
    }

    public void method_to_calculate(double quantitys){

        TextView payment_price_title = findViewById(R.id.payment_price_title);
        payment_price_title.setTypeface(regular);

        String final_total_amount = String.valueOf((Double.parseDouble(ticket_amount) * quantitys));

        TextView payment_amount = findViewById(R.id.payment_amount);
        payment_amount.setTypeface(regular);
        payment_amount.setText("$"+decimal_amount(Double.parseDouble(final_total_amount)));

        TextView payment_processing_title = findViewById(R.id.payment_processing_title);
        payment_processing_title.setTypeface(regular);

//        TextView payment_processing_amount = findViewById(R.id.payment_processing_amount);
//        payment_processing_amount.setTypeface(regular);
//        double procesing_fees = (Double.parseDouble(ticket_amount) * 9.5 *quantitys) / 100;
//        payment_processing_amount.setText("$"+procesing_fees);

        TextView payment_total_title = findViewById(R.id.payment_total_title);
        payment_total_title.setTypeface(medium);

        TextView payment_total_amount = findViewById(R.id.payment_total_amount);
        payment_total_amount.setTypeface(medium);
        payment_total_amount.setText("$"+decimal_amount(Double.parseDouble(final_total_amount)));

    }

    public void section_one() {

        TextView title_page = findViewById(R.id.title_page);
        title_page.setTypeface(medium);

        TextView title_desciption = findViewById(R.id.title_desciption);
        title_desciption.setTypeface(regular);


        ImageView event_logo = findViewById(R.id.event_logo);
        Picasso.with(Checkout_Page.this)
                .load(imge_url).error(R.drawable.black_cursor).placeholder(R.drawable.logo)
                .into(event_logo);

        TextView event_access_name = findViewById(R.id.payment_access_name);
        event_access_name.setTypeface(medium);

        TextView payment_event_name = findViewById(R.id.payment_event_name);
        payment_event_name.setTypeface(regular);
        payment_event_name.setText(name);

        TextView include_title = findViewById(R.id.payment_include_title);
        include_title.setTypeface(medium);
        include_title.setPaintFlags(include_title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        TextView payment_include_data = findViewById(R.id.payment_include_data);
        payment_include_data.setText(get_auth_token("aminities"));
        payment_include_data.setTypeface(regular);

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                quantity=1;
                method_to_calculate(1);
                btn_1.setBackgroundResource(R.drawable.blue_text_drawble);
                btn_2.setBackgroundResource(R.drawable.editext_baground);
                btn_3.setBackgroundResource(R.drawable.editext_baground);
                btn_4.setBackgroundResource(R.drawable.editext_baground);
                btn_5.setBackgroundResource(R.drawable.editext_baground);
                btn_6.setBackgroundResource(R.drawable.editext_baground);
                break;

            case R.id.btn_2:
                quantity=2;
                method_to_calculate(2);
                btn_2.setBackgroundResource(R.drawable.blue_text_drawble);
                btn_1.setBackgroundResource(R.drawable.editext_baground);
                btn_3.setBackgroundResource(R.drawable.editext_baground);
                btn_4.setBackgroundResource(R.drawable.editext_baground);
                btn_5.setBackgroundResource(R.drawable.editext_baground);
                btn_6.setBackgroundResource(R.drawable.editext_baground);
                break;

            case R.id.btn_3:
                quantity=3;
                method_to_calculate(3);
                btn_2.setBackgroundResource(R.drawable.editext_baground);
                btn_1.setBackgroundResource(R.drawable.editext_baground);
                btn_4.setBackgroundResource(R.drawable.editext_baground);
                btn_5.setBackgroundResource(R.drawable.editext_baground);
                btn_3.setBackgroundResource(R.drawable.blue_text_drawble);
                btn_6.setBackgroundResource(R.drawable.editext_baground);
                break;

            case R.id.btn_4:
                method_to_calculate(4);
                quantity=4;
                btn_2.setBackgroundResource(R.drawable.editext_baground);
                btn_3.setBackgroundResource(R.drawable.editext_baground);
                btn_1.setBackgroundResource(R.drawable.editext_baground);
                btn_5.setBackgroundResource(R.drawable.editext_baground);
                btn_4.setBackgroundResource(R.drawable.blue_text_drawble);
                btn_6.setBackgroundResource(R.drawable.editext_baground);
                break;

            case R.id.btn_5:
                quantity=5;
                method_to_calculate(5);
                btn_2.setBackgroundResource(R.drawable.editext_baground);
                btn_3.setBackgroundResource(R.drawable.editext_baground);
                btn_4.setBackgroundResource(R.drawable.editext_baground);
                btn_1.setBackgroundResource(R.drawable.editext_baground);
                btn_5.setBackgroundResource(R.drawable.blue_text_drawble);
                btn_6.setBackgroundResource(R.drawable.editext_baground);

                break;

            case R.id.btn_6:
                quantity=6;
                method_to_calculate(6);
                btn_2.setBackgroundResource(R.drawable.editext_baground);
                btn_3.setBackgroundResource(R.drawable.editext_baground);
                btn_4.setBackgroundResource(R.drawable.editext_baground);
                btn_1.setBackgroundResource(R.drawable.editext_baground);
                btn_5.setBackgroundResource(R.drawable.editext_baground);
                btn_6.setBackgroundResource(R.drawable.blue_text_drawble);

                break;

            case R.id.btn_checkout:

                        if (!isNetworkAvailable()) {
                            Toast.makeText(getApplicationContext(), "Internet not available, Please check your internet connection", Toast.LENGTH_LONG).show();
                        } else {

                            try {
                                user_code.put("event_id", get_auth_token("id"));
                                user_code.put("quantity", quantity);
                                ProgressDialog.getInstance().showProgress(Checkout_Page.this);
                            } catch (Exception E) {
                                //nothing
                            }

                            System.out.println("--------user_data-----"+user_code);

                            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url + "events/create_order", user_code,
                                    new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {

                                                System.out.println("------res-----"+response);

                                                order_item_id = response.getString("order_item_id");
                                                order_quantity = response.getString("quantity");
                                                order_price = response.getString("price");
                                                order_id = response.getString("order_id");

                                                String finalprice = String.format("%.2f",Double.parseDouble(order_price));

                                                Share_it.savestring("event_code", response.getString("event_code"));
                                                Share_it.savestring("event_name", response.getString("event_name"));
                                                Share_it.savestring("location", response.getString("location"));
                                                Share_it.savestring("quantity", response.getString("quantity"));
                                                Share_it.savestring("price", response.getString("price"));
                                                Share_it.savestring("organization_name", response.getString("organization_name"));
                                                Share_it.savestring("event_id", id);
                                                Share_it.savestring("user_type", "event");

                                                Intent in;
                                                if (quantity == 1) {
                                                    in = new Intent(getApplicationContext(), Purchage_Winning_Ticket.class);
                                                    in.putExtra("order_item_id", order_item_id);
                                                    in.putExtra("wallet_status", wallet_status);
                                                    in.putExtra("order_id", order_id);
                                                    in.putExtra("pageliveauction", "2");
                                                    startActivity(in);
                                                } else {
                                                    in = new Intent(getApplicationContext(), ExtraTickets.class);
                                                    in.putExtra("quantity", order_quantity);
                                                    in.putExtra("order_item_id", order_item_id);
                                                    in.putExtra("order_price", String.valueOf(finalprice));
                                                    in.putExtra("order_id", order_id);
                                                    in.putExtra("wallet_status", wallet_status);
                                                    startActivity(in);
                                                }
                                            } catch (Exception e) {

                                                //nothing
                                            }

                                            ProgressDialog.getInstance().hideProgress();

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            NetworkResponse networkResponse = error.networkResponse;
                                            if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                                // HTTP Status Code: 401 Unauthorized
                                                if (error.networkResponse.statusCode == 401) {
                                                    ProgressDialog.getInstance().hideProgress();
                                                    Alert_Dailog.showNetworkAlert(Checkout_Page.this);
                                                } else {
                                                    ProgressDialog.getInstance().hideProgress();
                                                    Toast.makeText(Checkout_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                                                }
                                            } else {
                                                ProgressDialog.getInstance().hideProgress();
                                                Toast.makeText(Checkout_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            myRequest.setRetryPolicy(policy);
                            AppController.getInstance().addToRequestQueue(myRequest, "tag");
                        }

                break;
            case R.id.btn_back:
                finish();
                break;
        }

    }

}
