package com.winningticketproject.in.EventTab;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.VirtulGiftBag;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Virtual_gift_bag extends AppCompatActivity implements View.OnClickListener {
    TextView cancel_purchage,toolbar_title,view_gift_vocher;
    Intent in;
    LinearLayout backtouch;
    String id="",auth_token;
    ArrayList<VirtulGiftBag> virtulGiftBags = new ArrayList<>();
    ListView list_virtual_gift_bag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_gift_bag);

        cancel_purchage = findViewById(R.id.cancel_purchage);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Virtual Gift Bag");
        cancel_purchage.setOnClickListener(this);

        auth_token = get_auth_token("auth_token");

        view_gift_vocher = findViewById(R.id.view_gift_vocher);
        view_gift_vocher.setTypeface(regular);

        list_virtual_gift_bag = findViewById(R.id.list_virtual_gift_bag);

        in = getIntent();
        id = in.getStringExtra("id");

        if (!isNetworkAvailable()) {

            alertdailogbox();
        }
        else
        {
            methodforcalleventlist();
        }

        cancel_purchage.setTypeface(medium);
        toolbar_title.setTypeface(medium);

    }


    public class Virtaul_gift_bag extends BaseAdapter {

        public LayoutInflater inflater;
        ArrayList<VirtulGiftBag> virtulGiftBags;

        public Virtaul_gift_bag(Activity context, ArrayList<VirtulGiftBag> virtulGiftBags) {
            super();
            this.virtulGiftBags = virtulGiftBags;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return virtulGiftBags.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public class ViewHolder {
            Button view_offer;
            TextView title_gift_bag,offere_date;
            ImageView gift_bag_iamge;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Virtaul_gift_bag.ViewHolder holder;
            if (convertView == null) {
                holder = new Virtaul_gift_bag.ViewHolder();
                convertView = inflater.inflate(R.layout.custome_gift_bag, null);

                holder.view_offer = convertView.findViewById(R.id.view_offer);
                holder.title_gift_bag = convertView.findViewById(R.id.title_gift_bag);
                holder.offere_date = convertView.findViewById(R.id.offere_date);

                holder.gift_bag_iamge = convertView.findViewById(R.id.gift_bag_iamge);

                holder.view_offer.setTypeface(medium);
                holder.title_gift_bag.setTypeface(medium);
                holder.offere_date.setTypeface(regular);
                convertView.setTag(holder);
            } else

              holder = (Virtaul_gift_bag.ViewHolder) convertView.getTag();


            if(virtulGiftBags.get(position).getDate().equals(null) || virtulGiftBags.get(position).getDate().equals("null") ){

                holder.offere_date.setText(Html.fromHtml("<b>" +"Offer expires: " +"</b>"+"Not Mentioned"));

            }else {
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                java.util.Date date = null;
                try
                {
                    date = format.parse(virtulGiftBags.get(position).getDate());
                    java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    String newDateStr = postFormater.format(date);
                    holder.offere_date.setText(Html.fromHtml("<b>" +"Offer expires: " +"</b>"+newDateStr));
                }
                catch (ParseException e)
                {//nothing
                    holder.offere_date.setText(Html.fromHtml("<b>" +"Offer expires: " +"</b>"+"Not Mentioned"));
                }
            }

                holder.title_gift_bag.setText(virtulGiftBags.get(position).getName().substring(0,1).toUpperCase() +virtulGiftBags.get(position).getName().substring(1).replaceAll("\\s+"," "));

                Picasso.with(Virtual_gift_bag.this)
                        .load(virtulGiftBags.get(position).getImage()).error(R.drawable.auction_event_empty).placeholder(R.drawable.auction_event_empty)
                        .into( holder.gift_bag_iamge);

            holder.view_offer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    in = new Intent(getApplicationContext(),Virtual_bag_details.class);
                    in.putExtra("id",id);
                    in.putExtra("vocher_id",virtulGiftBags.get(position).getId());
                    startActivity(in);
                }
            });
            return convertView;

        }
    }


    private void methodforcalleventlist() {
        JSONObject event_id = new JSONObject();
        try {
            event_id.put("event_id",id);
        }catch (Exception e){
            //nothing}
            }

           ProgressDialog.getInstance().showProgress(this);

            JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url+"virtual_gift_bag/list",event_id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println("-------res---"+response);

                            JSONArray virtual_gift_bags = response.getJSONArray("virtual_gift_bags");
                            for (int i =0 ;i<virtual_gift_bags.length();i++){
                                JSONObject virtual_gift_bagsobject = virtual_gift_bags.getJSONObject(i);

                                String ids = virtual_gift_bagsobject.getString("id");
                                String item_name = virtual_gift_bagsobject.getString("item_name").replaceAll("\\s+"," ");
                                String date = virtual_gift_bagsobject.getString("offer_expires_date");
                                String list_date_expire = virtual_gift_bagsobject.getString("offer_does_not_expire");
                                String list_url = virtual_gift_bagsobject.getString("image");

                                virtulGiftBags.add(new VirtulGiftBag(ids, item_name, date, list_url,list_date_expire));

                            }

                            Virtaul_gift_bag gift_bag = new Virtaul_gift_bag(Virtual_gift_bag.this,virtulGiftBags);
                            list_virtual_gift_bag.setAdapter(gift_bag);
                            if (virtulGiftBags.isEmpty()) {
                                list_virtual_gift_bag.setVisibility(View.GONE);
                                view_gift_vocher.setVisibility(View.VISIBLE);
                            } else {
                                list_virtual_gift_bag.setVisibility(View.VISIBLE);
                                view_gift_vocher.setVisibility(View.GONE);
                            }


                        }catch (Exception e){
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
                            if (error.networkResponse.statusCode==401){
                                ProgressDialog.getInstance().hideProgress();

                                Alert_Dailog.showNetworkAlert(Virtual_gift_bag.this);
                            }else {
                                ProgressDialog.getInstance().hideProgress();

                                Toast.makeText(Virtual_gift_bag.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(Virtual_gift_bag.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
                    if (!isNetworkAvailable()) {

                        alertdailogbox();
                    }
                    else
                    {
                        methodforcalleventlist();
                    }
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_purchage:
                finish();
                break;



        }
    }
}
