package com.winningticketproject.in.OrganizerFlow;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stripe.android.TokenCallback;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.BuildConfig;
import com.winningticketproject.in.R;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class ScanAndPay extends AppCompatActivity implements View.OnClickListener {

    Button btn_scann_pay;
    EditText ext_email,et_first_name,ext_last_name;
    String event_id,amount_type,auction_id,amount;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String stripe_token;
    ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_and_pay);

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        Intent in = getIntent();
        event_id = in.getStringExtra("event_id");
        auction_id = in.getStringExtra("auction_id");
        amount = in.getStringExtra("amount");
        amount_type = in.getStringExtra("amount_type");

        btn_scann_pay = findViewById(R.id.btn_scann_pay);
        btn_scann_pay.setTypeface(medium);
        btn_scann_pay.setOnClickListener(this);

        et_first_name = findViewById(R.id.et_first_name);
        et_first_name.setTypeface(regular);

        ext_last_name = findViewById(R.id.ext_last_name);
        ext_last_name.setTypeface(regular);

        ext_email = findViewById(R.id.ext_email);
        ext_email.setTypeface(regular);


        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_scann_pay:
                if(et_first_name.getText().toString().isEmpty()){
                    Toast.makeText(this,"Please enter first name",Toast.LENGTH_LONG).show();
                }else if (ext_email.getText().toString().isEmpty()){
                    Toast.makeText(this,"Please enter email address",Toast.LENGTH_LONG).show();
                }else if (!ext_email.getText().toString().trim().matches(emailPattern)){
                    Toast.makeText(this,"Please enter valid email",Toast.LENGTH_LONG).show();
                }else {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("amount",amount);
                        object.put("event_id",event_id);
                        object.put("auction_item_id",auction_id);
                        object.put("first_name",et_first_name.getText().toString());
                        object.put("last_name",ext_last_name.getText().toString());
                        object.put("email",ext_email.getText().toString());
                        object.put("auction_order_type",Integer.parseInt(amount_type));
                        method_to_call_token(object);
                    }catch (Exception e){ }
                }
                break;

                case R.id.btn_back:
                finish();
                break;
        }

    }

    private void method_to_call_token(JSONObject object ) {
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.imageurl+"api/v3/auction/order/create",object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ProgressDialog.getInstance().hideProgress();
                            System.out.println("-------res---"+response);
                                Intent in = new Intent(ScanAndPay.this,Organiser_Billing_Add.class);
                                in.putExtra("order_id", response.getString("order_id"));
                                in.putExtra("f_name",response.getString("user_first_name"));
                                in.putExtra("l_name",response.getString("user_last_name"));
                                in.putExtra("user_email",response.getString("user_email"));
                                startActivity(in);
                                finish();
                        }catch (Exception E){
                            //nothing
                            ProgressDialog.getInstance().hideProgress();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ProgressDialog.getInstance().hideProgress();
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            System.out.println("------------"+responseBody);
                        }catch (Exception e){

                        }

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("auth-token", get_auth_token("auth_token"));
                return headers;
            }

        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }

}
