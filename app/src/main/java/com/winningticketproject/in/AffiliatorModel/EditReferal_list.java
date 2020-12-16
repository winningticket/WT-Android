package com.winningticketproject.in.AffiliatorModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class EditReferal_list extends AppCompatActivity implements View.OnClickListener {
    ListView viewevent_listview;
    Typeface font,textfont;
    TextView seach_text,cancel_purchage,toolbar_title,referral_name,referal_role,result_no,cancel_search,view_all;
    EditText textsreach;
    String auth_token="",values="";
    ProgressDialog pd;
    int count=0;
    int current_page=1;
    int total_page=0;
    int position;
    boolean loadingMore=false;
    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> role = new ArrayList<>();
    ArrayList<String> p_number = new ArrayList<>();
    ArrayList<String> email = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_referal_list);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        textfont = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");
        font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        result_no = findViewById(R.id.result_no);
        result_no.setVisibility(View.GONE);

        view_all = findViewById(R.id.view_all);
        view_all.setTypeface(textfont);


        cancel_purchage = findViewById(R.id.cancel_purchage);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Edit Referral");
        cancel_purchage.setOnClickListener(this);

        auth_token = get_auth_token("auth_token");

        pd = new ProgressDialog(EditReferal_list.this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        id.clear();
        name.clear();
        role.clear();
        email.clear();
        p_number.clear();
        //methodforcalleventlist(Splash_screen.url+"referrals","");


        //referal_detail_title = (TextView) findViewById(R.id.referal_detail_title);
        referral_name = findViewById(R.id.referral_name);
        referal_role = findViewById(R.id.referal_role);

        textsreach = findViewById(R.id.textsreach);
        textsreach.setTypeface(textfont);

        seach_text = findViewById(R.id.seach_text);


        cancel_search = findViewById(R.id.cancel_search);
        cancel_search.setTypeface(textfont);


        cancel_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                finish();

                return false;
            }
        });

        textsreach.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                textsreach.setFocusable(true);
                textsreach.setFocusableInTouchMode(true);
                return false;
            }
        });


        textsreach.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                values = String.valueOf(cs);
                if(textsreach.getText().toString().length()%2==0) {


                    if (!isNetworkAvailable()) {

                        alertdailogbox("withname");
                    }
                    else {
                        id.clear();
                        name.clear();
                        role.clear();
                        email.clear();
                        p_number.clear();
                        current_page = 1;
                        result_no.setVisibility(View.VISIBLE);
                        methodforcalleventlist(Splash_screen.url + "referrals?name=" + cs, values);
                    }
                }

                if (cs.length()==0){
                    result_no.setVisibility(View.GONE);

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

        });


        seach_text.setText("\uf002");
        seach_text.setTypeface(font);

        //referal_detail_title.setTypeface(font);
        referral_name.setTypeface(textfont);
        referal_role.setTypeface(textfont);
        cancel_purchage.setTypeface(textfont);
        toolbar_title.setTypeface(textfont);


        viewevent_listview = findViewById(R.id.edit_referal_listview);


        cancel_purchage.setOnClickListener(this);

        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });


        viewevent_listview.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(!pd.isShowing()) {
                    if (totalItemCount >= 20) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount && !(loadingMore)) {
                            if (current_page <= total_page) {
                                if (!isNetworkAvailable()) {
                                    Toast.makeText(getApplicationContext(), "You don't have internet connection.", Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        loadingMore = true;
                                        methodforcalleventlist(Splash_screen.url + "referrals?name=" + values + "&page=" + current_page + "&per_page=20", values);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                    }
                    }
                }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){
                hiddenInputMethod();
            }
        });



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

    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.cancel_purchage:
                finish();
                break;



        }

    }

    public void alertdailogbox(final String name){
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


                if(name.equals("withname"))
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("withname");
                    }
                    else
                    {
                        methodforcalleventlist(Splash_screen.url + "referrals?name=" + values, values);
                    }
                }
                else
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("withoutname");
                    }
                    else {
                        methodforcalleventlist(Splash_screen.url + "referrals", "");
                    }
                }





            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        current_page=1;
        id.clear();
        name.clear();
        role.clear();
        p_number.clear();
        email.clear();
        textsreach.getText().clear();
        if (!isNetworkAvailable()) {

            alertdailogbox();
        }
        else
        {
            methodforcalleventlist(Splash_screen.url+"referrals","");
        }

    }


    public void methodforcalleventlist(String url ,final String csss){
        if(!pd.isShowing())
        {
            pd.show();
        }

       url =  url.replaceAll(" ", "%20");
        current_page++;
        position=id.size();
        //alleventlist_List.clear();
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (pd.isShowing()){
                                pd.dismiss();

                            }

                            JSONArray reffreal = response.getJSONArray("referrals");
                            JSONObject meta = response.getJSONObject("meta");
                            total_page = meta.getInt("total_pages");
                            count = meta.getInt("total_count");
                            for (int i=0;i<reffreal.length();i++) {
                                JSONObject eventobject = reffreal.getJSONObject(i);
                                JSONObject roleobject = eventobject.getJSONObject("role");

                                String referral_id = eventobject.getString("id");
                                String referral_name = eventobject.getString("first_name").replaceAll("\\s+"," ");

                                String referral_rolename=roleobject.getString("name").replaceAll("\\s+"," ");

                                String referral_role = referral_rolename.trim();
                                String referral_email=  eventobject.getString("email");
                                String phone_no= eventobject.getString("phone");

                                if(referral_name.equals("")||referral_name.equals(null))
                                {
                                    referral_name="Not mentioned";
                                }
                                if(referral_role.equals("")||referral_role.equals(null))
                                {
                                    referral_role="Not mentioned";
                                }
                                if(referral_email.equals("")||referral_email.equals(null))
                                {
                                    referral_email="Not mentioned";
                                }
                                if(phone_no.equals("")||phone_no.equals(null))
                                {
                                    phone_no="Not mentioned";
                                }


                                if (referral_role.equals("organizer")){

                                    referral_role = "Event Organizer";
                                }else if(referral_role.equals("contributor"))
                                {
                                    referral_role = "Participant";
                                }else {
                                    referral_role = referral_role;
                                }



                                id.add(referral_id);
                                name.add(referral_name);
                                role.add(referral_role);
                                email.add(referral_email);
                                p_number.add(phone_no);

                            }



                            ViewAfiliatelist allevents = new ViewAfiliatelist(EditReferal_list.this,id,name,role,email,p_number);
                            viewevent_listview.setAdapter(allevents);
                            viewevent_listview.setSelectionFromTop(position, 0);
                            loadingMore = false;
                            allevents.notifyDataSetChanged();
                            result_no.setText(count+" Results for "+"\""+csss+"\"");

                            if (id.isEmpty()) {
                                viewevent_listview.setVisibility(View.GONE);
                                view_all.setVisibility(View.VISIBLE);
                            }
                            else {
                                viewevent_listview.setVisibility(View.VISIBLE);
                                view_all.setVisibility(View.GONE);
                            }

                            if (pd.isShowing()){
                                pd.dismiss();
                            }

                        }catch (Exception e){
                            if (pd.isShowing()){
                                pd.dismiss();
                            }
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
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Alert_Dailog.showNetworkAlert(EditReferal_list.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(EditReferal_list.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(EditReferal_list.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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


    public class ViewAfiliatelist extends BaseAdapter
    {
        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> role = new ArrayList<>();
        ArrayList<String> email = new ArrayList<>();
        ArrayList<String> p_number = new ArrayList<>();
        public LayoutInflater inflater;
        public ViewAfiliatelist(Activity context,ArrayList<String> id,ArrayList<String> name,ArrayList<String> role,ArrayList<String> email,ArrayList<String> p_number) {
            super();
            this.id =id;
            this.name =name;
            this.role =role;
            this.p_number =p_number;
            this.email =email;

            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }




        @Override
        public int getCount() {
            return id.size();
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
            LinearLayout row_list,layout_event_name,layout_event_date,layout_event_btn;
            TextView event_name,event_type,btnview_details;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            final ViewHolder holder;
            if(convertView==null)
            {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.custome_edit_referral, null);
                holder.row_list = convertView.findViewById(R.id.row_list);

                holder.event_name = convertView.findViewById(R.id.event_name);
                holder.event_type = convertView.findViewById(R.id.event_type);

                holder.layout_event_name = convertView.findViewById(R.id.layout_event_name);
                holder.layout_event_date = convertView.findViewById(R.id.layout_event_date);
                holder.layout_event_btn = convertView.findViewById(R.id.layout_event_btn);

                holder.btnview_details = convertView.findViewById(R.id.text_details);

                convertView.setTag(holder);
            }
            else
                holder=(ViewHolder)convertView.getTag();

            if (position % 2 == 0) {
                holder.layout_event_name.setBackgroundColor(Color.parseColor("#E1E1E1"));
                holder.layout_event_date.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.layout_event_btn.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {

                holder.layout_event_name.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.layout_event_date.setBackgroundColor(Color.parseColor("#E1E1E1"));
                holder.layout_event_btn.setBackgroundColor(Color.parseColor("#E1E1E1"));
            }

            holder.event_name.setTypeface(textfont);
            holder.event_type.setTypeface(textfont);

            String eventname="";
            try{
                eventname=name.get(position).substring(0,1).toUpperCase() +name.get(position).substring(1);
            }
            catch (Exception e)
            {

            }
            String eventrole="";
            try{
                eventrole=role.get(position).substring(0,1).toUpperCase() +role.get(position).substring(1);
            }
            catch (Exception e)
            {

            }


            holder.event_name.setText(eventname.trim());
            holder.event_type.setText(eventrole.trim());
            holder.btnview_details.setText("\uf056");
            holder.btnview_details.setTypeface(font);

            holder.row_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent in = new Intent(getApplicationContext(), Edit_Referral_Details.class);
                    in.putExtra("id",id.get(position));
                    in.putExtra("name",name.get(position).replaceAll("\\s+"," "));
                    in.putExtra("role",role.get(position).replaceAll("\\s+"," "));
                    in.putExtra("email",email.get(position));
                    in.putExtra("p_no", p_number.get(position));
                    startActivity(in);

                }
            });



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
                    methodforcalleventlist(Splash_screen.url+"referrals","");
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }
}
