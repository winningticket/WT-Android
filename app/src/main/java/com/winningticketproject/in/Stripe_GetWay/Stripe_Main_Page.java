package com.winningticketproject.in.Stripe_GetWay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stripe.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.view.CardMultilineWidget;
import com.winningticketproject.in.BuildConfig;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;

public class Stripe_Main_Page extends AppCompatActivity {
    CardMultilineWidget mCardInputWidget;
    com.stripe.android.Stripe Stripe2;
    ArrayList<Saved_cards> saved_cards = new ArrayList<>();
    String sourse_id="";
    RecyclerView all_cards_info;
    Saved_cards_Adapter saved_cards_adapter;
    private RadioButton selected = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe__main__page);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Stripe.apiKey = BuildConfig.STRIPE_PUBLIC_KEY;

        mCardInputWidget = findViewById(R.id.card_input_widget);

        all_cards_info = findViewById(R.id.all_cards_info);


        mCardInputWidget.setCardNumberTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (selected != null){
                    selected.setChecked(false);
                    saved_cards_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (selected != null){
                    selected.setChecked(false);
                    saved_cards_adapter.notifyDataSetChanged();
                }


            }


            @Override
            public void afterTextChanged(Editable editable) {

                if (selected != null){
                    selected.setChecked(false);
                    saved_cards_adapter.notifyDataSetChanged();
                }

            }
        });

        Button btn_submit = findViewById(R.id.add_funds);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (sourse_id.equals("")){
                    method_to_submit();
                }else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("Payment_token","");
                    returnIntent.putExtra("source_id",sourse_id);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                }
            }
        });


        ImageButton image_button = findViewById(R.id.stripe_btn_back);
        image_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED,returnIntent);
                finish();
                return false;
            }
        });

        method_to_call_saved_cards();

    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

    private void method_to_call_saved_cards() {
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"contributors/stripe-accounts",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("-------response-----"+response);

                            JSONArray stripe_accounts = response.getJSONArray("stripe_accounts");
                            for (int i=0;i<stripe_accounts.length();i++){
                                JSONObject object = stripe_accounts.getJSONObject(i);
                                saved_cards.add(new Saved_cards(object.getString("last_4_digits"),object.getString("id")));
                            }

                            saved_cards_adapter = new Saved_cards_Adapter(Stripe_Main_Page.this,saved_cards);
                            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(Stripe_Main_Page.this, LinearLayoutManager.VERTICAL, false);
                            all_cards_info.setLayoutManager(horizontalLayoutManager);
                            all_cards_info.setAdapter(saved_cards_adapter);

                            ProgressDialog.getInstance().hideProgress();

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




    private class Saved_cards_Adapter extends RecyclerView.Adapter<Saved_cards_Adapter.MyViewHolder> {

        ArrayList<Saved_cards> Saved_cards = new ArrayList<>();

        public Saved_cards_Adapter(Context context,ArrayList<Saved_cards> Saved_cards) {
            this.Saved_cards = Saved_cards;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            RadioButton radio_button;
            TextView tv_card_number;

            public MyViewHolder(View view) {
                super(view);

                radio_button = view.findViewById(R.id.radio_button);
                tv_card_number = view.findViewById(R.id.tv_card_number);
                tv_card_number.setTypeface(medium);

            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_saved_payment_cards, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {


            holder.tv_card_number.setText("**** **** **** "+Saved_cards.get(position).Cards_number);

            holder.radio_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selected != null) {
                        selected.setChecked(false);
                    }
                    holder.radio_button.setChecked(true);
                    selected = holder.radio_button;
                    sourse_id = Saved_cards.get(position).Sourse_id;
                }
            });
        }
        @Override
        public int getItemCount() {
            return Saved_cards.size();
        }

    }

    public void method_to_submit(){

        hiddenInputMethod();

        Stripe2 = new com.stripe.android.Stripe(Stripe_Main_Page.this,"pk_test_qZnz1zTwpsXsc4am3om0BM4800UcaNkTcc");
        Card cardToSave = mCardInputWidget.getCard();

        if (cardToSave!= null) {
            ProgressDialog.getInstance().showProgress(Stripe_Main_Page.this);
            Stripe2.createToken(cardToSave, new TokenCallback() {
                        public void onSuccess(@NonNull Token token) {
                            // Send token to your server
                            ProgressDialog.getInstance().hideProgress();
                            String Token = token.getId();
                            try {
                                System.out.println("------token-----"+Token);
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("Payment_token",Token);
                                returnIntent.putExtra("source_id","");
                                setResult(RESULT_OK,returnIntent);
                                finish();
                            }catch (Exception e){
                                Toast.makeText(Stripe_Main_Page.this, "Some thing went wrong", Toast.LENGTH_LONG
                                ).show();
                            }
                        }

                        public void onError(@NonNull Exception error) {
                            // Show localized error message
                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(Stripe_Main_Page.this, "Some thing went wrong", Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );

        }else {
            Toast.makeText(Stripe_Main_Page.this, "Please enter cards details", Toast.LENGTH_LONG).show();

        }


    }


    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
