 package com.winningticketproject.in.EventPurchase;

 import android.annotation.SuppressLint;
 import android.app.ProgressDialog;
 import android.content.Context;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.graphics.Typeface;
 import android.os.Bundle;
 import android.support.v7.app.AlertDialog;
 import android.support.v7.app.AppCompatActivity;
 import android.support.v7.widget.LinearLayoutManager;
 import android.support.v7.widget.RecyclerView;
 import android.view.LayoutInflater;
 import android.view.MotionEvent;
 import android.view.View;
 import android.view.ViewGroup;
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
 import com.winningticketproject.in.AppInfo.AppController;
 import com.winningticketproject.in.R;
 import com.winningticketproject.in.SignInSingup.Splash_screen;

 import org.json.JSONArray;
 import org.json.JSONObject;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;

 import cz.msebera.android.httpclient.HttpStatus;

 import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
 import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

 public class Winning_ticket_detail extends AppCompatActivity implements View.OnClickListener {
    TextView cancel_purchage, toolbar_title,event_title,event_name,some_dummy_values,sponsor_logo;
    Intent in;
     Typeface fonttext;
     Typeface book;
    String id = "", auth_token;
    ProgressDialog pd;
    ImageView gift_bag_iamge;
    LinearLayout backtouch;
    ArrayList<String> key_parameter = new ArrayList<>();
    ArrayList<String> values_parameter = new ArrayList<>();
    ArrayList<String> comapny_url = new ArrayList<>();
    RecyclerView horizontal_recycler_view,list_with_aminities;
    ComaapnyLogo compnay_logo;

     @SuppressLint("ClickableViewAccessibility")
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_ticket_detail);

        fonttext = Typeface.createFromAsset(getAssets(), "Montserrat-Medium.ttf");
        book = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");

        cancel_purchage = findViewById(R.id.cancel_purchage);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Winning Ticket");
        cancel_purchage.setOnClickListener(this);

        auth_token = get_auth_token("auth_token");

        event_title = findViewById(R.id.event_title);
        event_name = findViewById(R.id.event_name);
        some_dummy_values = findViewById(R.id.some_dummy_values);

        gift_bag_iamge = findViewById(R.id.gift_bag_iamge);

        list_with_aminities = findViewById(R.id.list_with_aminities);
        list_with_aminities.setFocusable(false);

        sponsor_logo = findViewById(R.id.sponsor_logo);
        sponsor_logo.setTypeface(book);
        //data = fill_with_data();
        horizontal_recycler_view = findViewById(R.id.horizontal_recycler_view);
        horizontal_recycler_view.setFocusable(false);

        in = getIntent();
        id = in.getStringExtra("id");

        if (!isNetworkAvailable()) {
            alertdailogbox();
        }
        else
        {
            methodforcalleventlist();
        }

        backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });


        cancel_purchage.setTypeface(fonttext);
        toolbar_title.setTypeface(fonttext);
        event_title.setTypeface(fonttext);
        event_name.setTypeface(book);
        some_dummy_values.setTypeface(fonttext);


    }


    public class ComaapnyLogo extends RecyclerView.Adapter<ComaapnyLogo.MyViewHolder> {
        ArrayList<String> complay_url = new ArrayList<>();

        public ComaapnyLogo(ArrayList<String> complay_url, Context context) {
            this.complay_url = complay_url;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView images_sponsor;

            public MyViewHolder(View view) {
                super(view);
                images_sponsor = view.findViewById(R.id.images_sponsor);
            }
        }
        @Override
        public ComaapnyLogo.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sponsor_images, parent, false);
            return new ComaapnyLogo.MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final ComaapnyLogo.MyViewHolder holder, final int position) {
            Picasso.with(Winning_ticket_detail.this)
                    .load(complay_url.get(position)).error(R.drawable.auction_event_empty).placeholder(R.drawable.auction_event_empty)
                    .into(holder.images_sponsor);
        }
        @Override
        public int getItemCount() {
            return complay_url.size();
        }
    }


    public class List_of_animies extends RecyclerView.Adapter<List_of_animies.MyViewHolder> {
        ArrayList<String> key_parameter = new ArrayList<>();
        ArrayList<String> values_parameter = new ArrayList<>();

        public List_of_animies(Context context,ArrayList<String> key_parameter, ArrayList<String> values_parameter) {
            this.key_parameter = key_parameter;
            this.values_parameter = values_parameter;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView values_text;

            public MyViewHolder(View view) {
                super(view);
                values_text = view.findViewById(R.id.values_text);
             }
            }
        @Override
        public List_of_animies.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_winning_ticket_detail, parent, false);
            return new List_of_animies.MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final List_of_animies.MyViewHolder holder, final int position) {
            holder.values_text.setTypeface(book);
            holder.values_text.setText(values_parameter.get(position)+" "+key_parameter.get(position));

        }
        @Override
        public int getItemCount() {
            return values_parameter.size();
        }
    }

    public void alertdailogbox() {
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

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (!isNetworkAvailable()) {

                    alertdailogbox();
                } else {
                    if (!isNetworkAvailable()) {

                        alertdailogbox();
                    } else {
                        methodforcalleventlist();
                    }
                }

            }


        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    private void methodforcalleventlist() {

        JSONObject event_id = new JSONObject();
        try {
            event_id.put("id",id);

        }catch (Exception e){
            //nothing
        }
        pd = new ProgressDialog(Winning_ticket_detail.this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url+"events/winning_ticket",event_id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String event_names = response.getString("event_name");
                            String avatar_url = response.getString("avatar_url");
                            Picasso.with(Winning_ticket_detail.this)
                                    .load(avatar_url).error(R.drawable.auction_event_empty).placeholder(R.drawable.auction_event_empty)
                                    .into(gift_bag_iamge);
                            event_name.setText(event_names.substring(0,1).toUpperCase() +event_names.substring(1));
                            key_parameter.add("");
                            values_parameter.add("Virtual Gift Bag");

                            JSONObject ticket_amenities = response.getJSONObject("ticket_amenities");
                            Iterator<String> keys = ticket_amenities.keys();
                            while(keys.hasNext() ){
                                String key = keys.next();
                                key_parameter.add(key);
                                values_parameter.add(ticket_amenities.getString(key));
                                 }
                            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(Winning_ticket_detail.this, LinearLayoutManager.VERTICAL, false);
                            list_with_aminities.setLayoutManager(horizontalLayoutManager);

                            List_of_animies viewFilterlist = new List_of_animies(Winning_ticket_detail.this, key_parameter, values_parameter);
                            viewFilterlist.notifyDataSetChanged();
                            list_with_aminities.setAdapter(viewFilterlist);
                        }catch (Exception e){
                            //nothing
                            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(Winning_ticket_detail.this, LinearLayoutManager.VERTICAL, false);
                            list_with_aminities.setLayoutManager(horizontalLayoutManager);
                            List_of_animies viewFilterlist = new List_of_animies(Winning_ticket_detail.this, key_parameter, values_parameter);
                            viewFilterlist.notifyDataSetChanged();
                            list_with_aminities.setAdapter(viewFilterlist);

                        }

                        try{
                            JSONArray sponsor_users = response.getJSONArray("sponsor_users");
                            for (int i=0;i<sponsor_users.length();i++){
                                JSONObject sponser_object = sponsor_users.getJSONObject(i);
                                String url_values = sponser_object.getString("company_logo_avatar");
                                comapny_url.add(url_values);
                            }

                            compnay_logo = new ComaapnyLogo(comapny_url, getApplicationContext());
                            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(Winning_ticket_detail.this, LinearLayoutManager.HORIZONTAL, false);
                            horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
                            horizontal_recycler_view.setAdapter(compnay_logo);
                            compnay_logo.notifyDataSetChanged();

                        }catch (Exception e){
                            //nothing

                            if (comapny_url.isEmpty()) {
                                horizontal_recycler_view.setVisibility(View.GONE);
                                sponsor_logo.setVisibility(View.VISIBLE);
                            }
                            else {
                                horizontal_recycler_view.setVisibility(View.VISIBLE);
                                sponsor_logo.setVisibility(View.GONE);
                            }


                        }


                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Alert_Dailog.showNetworkAlert(Winning_ticket_detail.this);
                            }else {

                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(Winning_ticket_detail.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(Winning_ticket_detail.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_purchage:
                finish();
                break;


        }
    }
}
