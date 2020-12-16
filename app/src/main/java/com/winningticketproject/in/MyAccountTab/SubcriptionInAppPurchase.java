
package com.winningticketproject.in.MyAccountTab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.BuildConfig.LICENCE_KEY;
import static com.winningticketproject.in.BuildConfig.MERCHANT_ID;

public class SubcriptionInAppPurchase extends Activity implements View.OnClickListener {

	private BillingProcessor bp;
	private boolean readyToPurchase = false;
    android.support.v7.app.AlertDialog alertDialog_2;
    Button btn_montly;
    Button btn_yearly;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_round_popup_permium_my_account);

        method_to_load_top_header();

        if(!BillingProcessor.isIabServiceAvailable(this)) {
            showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }

        bp = new BillingProcessor(this, LICENCE_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                if (bp.isValidTransactionDetails(details)){
                    JSONObject object = new JSONObject();
                    try {
                        object.put("subscription_id",details.productId+"");
                        object.put("purchase_token",details.purchaseToken+"");
                        method_to_call_premium_putchase(object,details.productId+"");
                    }catch (Exception e){ }
                }
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
                showToast("Subscription not purchased");
                btn_montly.setClickable(true);
                btn_yearly.setClickable(true);
            }
            @Override
            public void onBillingInitialized() {
                readyToPurchase = true;
            }
            @Override
            public void onPurchaseHistoryRestored() {
            }
        });
    }

    private void method_to_call_premium_putchase(JSONObject object,final String product_id) {
        ProgressDialog.getInstance().showProgress(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"payments/subscription-android",object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ProgressDialog.getInstance().hideProgress();
                     if (response.getString("status").equals("Success")){
                         show_success_method(product_id+"");
                     }
                }
                catch (Exception e)
                {
                    //nothing
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    if (error.networkResponse.statusCode == 401) {
                        Alert_Dailog.showNetworkAlert(SubcriptionInAppPurchase.this);
                    } else {
                        Toast.makeText(SubcriptionInAppPurchase.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SubcriptionInAppPurchase.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy( 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private void show_success_method(String product_id) {

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SubcriptionInAppPurchase.this, R.style.CustomDialogTheme);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View premium_purchase_view = inflater.inflate(R.layout.activity_order_success_page, null);
            final android.support.v7.app.AlertDialog popupDia = builder.create();
            popupDia.setView(premium_purchase_view, 0, 0, 0, 0);
            popupDia.setCanceledOnTouchOutside(true);
            popupDia.setCancelable(false);
            popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            popupDia.show();

            alertDialog_2 = popupDia;
            alertDialog_2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

             TextView purchase_success = alertDialog_2.findViewById(R.id.purchase_success);
             purchase_success.setTypeface(medium);

             TextView premium_type_title = alertDialog_2.findViewById(R.id.premium_type_title);
             premium_type_title.setTypeface(medium);

            TextView premium_amount_title = alertDialog_2.findViewById(R.id.premium_amount_title);
            premium_amount_title.setTypeface(medium);

            TextView premium_type_value = alertDialog_2.findViewById(R.id.premium_type_value);
            premium_type_value.setTypeface(medium);

            TextView premium_amount = alertDialog_2.findViewById(R.id.premium_amount);
            premium_amount.setTypeface(medium);

            if (product_id.equals("wt_monthly")){
                premium_type_value.setText("Monthly");
                premium_amount.setText("$1.99");
            }else {
                premium_type_value.setText("Yearly");
                premium_amount.setText("$14.99");
            }

            Button btn_finish = alertDialog_2.findViewById(R.id.btn_finish);
            btn_finish.setTypeface(medium);

            btn_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent();
                    setResult(RESULT_OK, i);
                    finish();

                    alertDialog_2.dismiss();
//                    finish();
                }
            });

    }

    private void method_to_load_top_header() {

        TextView tv_premium_purchase = findViewById(R.id.tv_premium_purchase);
        tv_premium_purchase.setTypeface(regular);

        TextView tv_premium = findViewById(R.id.tv_premium);
        TextView tv_chose_plan = findViewById(R.id.tv_chose_plan);
        tv_premium.setTypeface(semibold);
        tv_chose_plan.setTypeface(regular);
         btn_montly = findViewById(R.id.btn_montly);
         btn_yearly = findViewById(R.id.btn_yearly);

        btn_montly.setTypeface(semibold);
        btn_yearly.setTypeface(semibold);

        btn_montly.setOnClickListener(this);
        btn_yearly.setOnClickListener(this);

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

    }

    @Override
	protected void onResume() {
		super.onResume();
	}

	@Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void onClick(View v) {
        if (!readyToPurchase) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_montly:
                bp.subscribe(this,"wt_monthly");
                btn_montly.setClickable(false);
                btn_yearly.setClickable(false);
                break;
            case R.id.btn_yearly:
                bp.subscribe(this,"wt_yearly");
                btn_montly.setClickable(false);
                btn_yearly.setClickable(false);
                break;
            default:
                break;
        }
    }

}
