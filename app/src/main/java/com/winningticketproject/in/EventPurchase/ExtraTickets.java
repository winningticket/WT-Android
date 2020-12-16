package com.winningticketproject.in.EventPurchase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.EventTab.ListViewForEmbeddingInScrollView;
import com.winningticketproject.in.EventTab.Purchage_Winning_Ticket;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.decimal_amount;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class ExtraTickets extends AppCompatActivity implements View.OnClickListener {
    int order_item_id;
    ListViewForEmbeddingInScrollView viewevent_listview;
    TextView add_recipents;
    Button button_checkoutreceipent;
    String auth_token="";
    String wallet_status;
    String order_id;
    String walleter_balance;
    Intent in;
    LinearLayout ticketlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_tickets);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        in = getIntent();

        section_one();

        in = getIntent();
        order_item_id = Integer.parseInt(in.getStringExtra("order_item_id"));
        wallet_status = in.getStringExtra("wallet_status");
        order_id = in.getStringExtra("order_id");
        walleter_balance = in.getStringExtra("wallete");

        auth_token = get_auth_token("auth_token");


        add_recipents = findViewById(R.id.add_recipents);
        ticketlayout = findViewById(R.id.ticketlayout);

        button_checkoutreceipent = findViewById(R.id.button_checkoutreceipent);
        button_checkoutreceipent.setTypeface(regular);
        button_checkoutreceipent.setOnClickListener(this);
        add_recipents.setTypeface(medium);


        viewevent_listview = findViewById(R.id.live_auction);
        Liveauction allevents = new Liveauction(ExtraTickets.this, Integer.parseInt(get_auth_token("quantity"))-1);
        viewevent_listview.setAdapter(allevents);

        viewevent_listview.setFocusable(false);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int[] scrcoords = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                view.setFocusable(false);
            }else
            {
                view.setFocusable(true);
            }
        }
        return ret;
    }


    public void section_one() {

        TextView title_page = findViewById(R.id.title_page);
        title_page.setTypeface(medium);

        TextView title_desciption = findViewById(R.id.title_desciption);
        title_desciption.setTypeface(regular);

        ImageView event_logo = findViewById(R.id.event_logo);
        Picasso.with(ExtraTickets.this)
                .load(get_auth_token("imge_url")).error(R.drawable.black_cursor).placeholder(R.drawable.logo)
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

        payment_include_data.setText(get_auth_token("aminities"));

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

        TextView payment_total_amount = findViewById(R.id.payment_total_amount);
        payment_total_amount.setTypeface(medium);
        payment_total_amount.setText("$"+decimal_amount(Double.parseDouble(final_total_amount)));

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_back:
                finish();
                break;

            case R.id.button_checkoutreceipent:
                if (!isNetworkAvailable()) {
                    alertdailogbox();
                }
                else
                {
                    hiddenInputMethod();
                    int itemCount = viewevent_listview.getCount();
                    JSONObject obj = new JSONObject();
                    JSONArray recipients = new JSONArray();
                    JSONObject order_item = new JSONObject();

                    for (int i = 0; i < itemCount; i++) {

                        View view1 = viewevent_listview.getChildAt(i);

                        EditText fname = view1.findViewById(R.id.receipant_fname);
                        EditText lname = view1.findViewById(R.id.receipant_lname);
                        EditText email = view1.findViewById(R.id.receipant_email);

                        String strfname = fname.getText().toString();
                        String strlname = lname.getText().toString();
                        String strfemail = email.getText().toString();

                        String emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,10}";

                        if(strfname.equals("")&&strlname.equals("")&&strfemail.equals("")) {
                            try {

                                JSONObject regdetail_json = new JSONObject();
                                regdetail_json.put("first_name", strfname);
                                regdetail_json.put("last_name", strlname);
                                regdetail_json.put("email", strfemail);
                                recipients.put(regdetail_json);
                                order_item.put("id", order_item_id);
                            } catch (Exception e) {
                                //nothing
                            }
                            if(i+1==itemCount)
                            {
                                try {
                                    obj.put("recipients", recipients);
                                    obj.put("order_item",order_item);
                                    jsonparsingContactInformation(obj, Splash_screen.url+"events/create_update_recipients");
                                } catch (JSONException e) {
                                    //nothing
                                }
                            }
                        }
                        else
                        {
                            if(!strfname.equals("")&&!strlname.equals("")&&!strfemail.equals("")&&strfemail.matches(emailPattern)) {

                                try {

                                    JSONObject regdetail_json = new JSONObject();
                                    regdetail_json.put("first_name", strfname);
                                    regdetail_json.put("last_name", strlname);
                                    regdetail_json.put("email", strfemail);
                                    recipients.put(regdetail_json);
                                    order_item.put("id", order_item_id);
                                } catch (Exception e) {
                                    //nothing
                                }
                                if(i+1==itemCount)
                                {
                                    try {
                                        obj.put("recipients", recipients);
                                        obj.put("order_item",order_item);

                                        System.out.println(obj+"----------");

                                        jsonparsingContactInformation(obj,Splash_screen.url+"events/create_update_recipients");
                                    } catch (JSONException e) {
                                        //nothing
                                    }
                                }
                            }
                            else
                            {
                                if(strfname.equals(""))
                                {
                                    error("Please enter first name");
                                    fname.requestFocus();

                                }else if(strfname.length()<=1){
                                    error("First name minimum 2 characters");
                                    fname.requestFocus();
                                } else  if(strlname.equals(""))
                                {
                                    error("Please enter last name");
                                    lname.requestFocus();
                                }else if(strlname.length()<=1){
                                    error("Last name minimum 2 characters");
                                    lname.requestFocus();

                                }
                                else  if(strfemail.equals(""))
                                {
                                    error("Please enter email adrress");
                                    email.requestFocus();

                                }
                                else  if(!isValidEmail(strfemail))
                                {
                                    error("Invalid email address");
                                    email.requestFocus();

                                }
                                break;
                            }
                        }
                    }
                }


                break;
        }
    }


    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }



    private void jsonparsingContactInformation(JSONObject obj, String url) {
        ProgressDialog.getInstance().showProgress(ExtraTickets.this);
        String tag_json_obj = "";
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {

                    String valueS = response.getString("message");
                    if (response.getString("status").equals("Success")){
                        JSONArray recipients = response.getJSONArray("recipients");
                        ProgressDialog.getInstance().hideProgress();
                        if (recipients.length()>0){
                            Toast.makeText(ExtraTickets.this, valueS , Toast.LENGTH_LONG).show();
                        }

                        Intent in = new Intent(getApplicationContext(), Purchage_Winning_Ticket.class);
                        in.putExtra("order_item_id",order_item_id);
                        in.putExtra("wallet_status",wallet_status);
                        in.putExtra("pageliveauction","2");
                        in.putExtra("order_id", order_id);
                        in.putExtra("wallete",walleter_balance);
                        startActivity(in);
                        finish();
                    }else {
                        Toast.makeText(ExtraTickets.this, valueS , Toast.LENGTH_LONG).show();
                    }
                    ProgressDialog.getInstance().hideProgress();
                } catch (JSONException e) {
                    //nothing
                }
                ProgressDialog.getInstance().hideProgress();
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    if (error.networkResponse.statusCode==401){
                        ProgressDialog.getInstance().hideProgress();
                        Alert_Dailog.showNetworkAlert(ExtraTickets.this);
                    }else {
                        ProgressDialog.getInstance().hideProgress();
                        Toast.makeText(ExtraTickets.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    ProgressDialog.getInstance().hideProgress();
                    Toast.makeText(ExtraTickets.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
        int socketTimeout = 50000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    @SuppressLint("WrongConstant")
    private void error(String s) {
        Snackbar snackbar = Snackbar.make(ticketlayout, s, Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        View  view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue
                .COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorsnakbar));
        snackbar.show();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public class Liveauction extends BaseAdapter
    {
        public int quantity;
        public LayoutInflater inflater;
        public Liveauction(Activity context,int quantity) {

            super();
            this.quantity =quantity;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return quantity;
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

        public class ViewHolder
        {
            EditText receipant_fname,receipant_lname,receipant_email;
            TextInputLayout first_name,last_name,email_text;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            final ViewHolder holder;
            if(convertView==null)
            {
                holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.custome_add_recipents, parent, false);
                    holder.receipant_fname = convertView.findViewById(R.id.receipant_fname);
                    holder.receipant_lname = convertView.findViewById(R.id.receipant_lname);
                    holder.receipant_email = convertView.findViewById(R.id.receipant_email);
                    holder.first_name = convertView.findViewById(R.id.first_name);
                    holder.last_name = convertView.findViewById(R.id.last_name);
                    holder.email_text = convertView.findViewById(R.id.email_text);

                    holder.receipant_fname.setTypeface(medium);
                    holder.receipant_lname.setTypeface(medium);
                    holder.receipant_email.setTypeface(medium);

                    holder.first_name.setTypeface(regular);
                    holder.last_name.setTypeface(regular);
                    holder.email_text.setTypeface(regular);

                holder.receipant_fname.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        holder.receipant_fname.setFocusable(true);
                        holder.receipant_fname.setFocusableInTouchMode(true);
                        return false;
                    }
                });

                holder.receipant_lname.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        holder.receipant_lname.setFocusable(true);
                        holder.receipant_lname.setFocusableInTouchMode(true);
                        return false;
                    }
                });

                holder.receipant_email.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        holder.receipant_email.setFocusable(true);
                        holder.receipant_email.setFocusableInTouchMode(true);
                        return false;
                    }
                });

                 convertView.setTag(holder);
            }
            else
                holder=(ViewHolder)convertView.getTag();


            return convertView;
        }
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


                    int itemCount = viewevent_listview.getCount();
                    JSONObject obj = new JSONObject();
                    JSONArray recipients = new JSONArray();
                    JSONObject order_item = new JSONObject();

                    for (int i = 0; i < itemCount; i++) {

                        View view1 = viewevent_listview.getChildAt(i);

                        EditText fname = view1.findViewById(R.id.receipant_fname);
                        EditText lname = view1.findViewById(R.id.receipant_lname);
                        EditText email = view1.findViewById(R.id.receipant_email);

                        String strfname = fname.getText().toString();
                        String strlname = lname.getText().toString();
                        String strfemail = email.getText().toString();

                        String emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,10}";

                        if(strfname.equals("")&&strlname.equals("")&&strfemail.equals("")) {
                            try {
                                JSONObject regdetail_json = new JSONObject();
                                regdetail_json.put("first_name", strfname);
                                regdetail_json.put("last_name", strlname);
                                regdetail_json.put("email", strfemail);
                                recipients.put(regdetail_json);
                                order_item.put("id", order_item_id);
                            } catch (Exception e) {
                                //nothing
                            }
                            if(i+1==itemCount)
                            {
                                try {
                                    obj.put("recipients", recipients);
                                    obj.put("order_item",order_item);

                                    jsonparsingContactInformation(obj,Splash_screen.url+"events/create_update_recipients");
                                } catch (JSONException e) {
                                    //nothing
                                }
                            }
                        }
                        else
                        {
                            if(!strfname.equals("")&&!strlname.equals("")&&!strfemail.equals("")&&strfemail.matches(emailPattern)) {

                                try {

                                    JSONObject regdetail_json = new JSONObject();
                                    regdetail_json.put("first_name", strfname);
                                    regdetail_json.put("last_name", strlname);
                                    regdetail_json.put("email", strfemail);
                                    recipients.put(regdetail_json);
                                    order_item.put("id", order_item_id);
                                } catch (Exception e) {
                                    //nothing
                                }
                                if(i+1==itemCount)
                                {
                                    try {
                                        obj.put("recipients", recipients);
                                        obj.put("order_item",order_item);

                                        jsonparsingContactInformation(obj,Splash_screen.url+"events/create_update_recipients");
                                    } catch (JSONException e) {
                                        //nothing
                                    }
                                }
                            }
                            else
                            {
                                if(strfname.equals(""))
                                {
                                    error("First name should not be empty");
                                }else if(strfname.length()<=1){
                                    error("First name minimum 2 character required");
                                } else  if(strlname.equals(""))
                                {
                                    error("Last name should not be empty");
                                }else if(strlname.length()<=1){
                                    error("Last name minimum 2 character required");
                                }
                                else  if(strfemail.equals(""))
                                {
                                    error("Email should not be empty");
                                }
                                else  if(!isValidEmail(strfemail))
                                {
                                    error("Invalid email");
                                }
                                break;
                            }
                        }

                    }
                }
            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }


}
