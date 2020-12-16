package com.winningticketproject.in.OrganizerFlow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.winningticketproject.in.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class OrgPurchaseConf extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_purchase_conf);

        idInitilization();

    }

    private void idInitilization() {

        TextView txt_item_name = findViewById(R.id.txt_item_name);
        txt_item_name.setTypeface(italic);

        TextView txt_purchase_details = findViewById(R.id.txt_purchase_details);
        txt_purchase_details.setTypeface(regular);

        Button btn_finish = findViewById(R.id.btn_finish);
        btn_finish.setTypeface(medium);


        TextView payment_confirmed = findViewById(R.id.txt_payment_title);
        payment_confirmed.setTypeface(medium);

        String data = getIntent().getStringExtra("response");

        try {
            JSONObject response = new JSONObject(data);
            System.out.println(response+"-----------");

            txt_item_name.setText("#"+response.getString("product_number")+" - "+ response.getString("product_name"));

           String purchase_details = "Customer: <b>"+response.getString("customer_mail")+"</b><br>Purchase Amount: <b>$"+String.format("%.2f",Double.parseDouble(response.getString("purchased_amount")));
            txt_purchase_details.setText(Html.fromHtml(purchase_details));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(OrgPurchaseConf.this, EventListFlow.class);
                startActivity(in);
                finishAffinity();
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
    }
}
