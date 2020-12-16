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
import android.widget.Button;
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

public class Search_affiliate_list extends AppCompatActivity {

    TextView seach_text,result_no,cancel_search,referal_name,referalrole,view_all;
    ListView viewevent_listview;
    EditText searchtext;
    Typeface font,textfont;
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
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> p_number = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_affiliate_list);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        seach_text = findViewById(R.id.seach_text);
        referal_name = findViewById(R.id.referal_name);
        referalrole = findViewById(R.id.referalrole);

         font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf");

        textfont = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");

        seach_text.setText("\uf002");
        seach_text.setTypeface(font);
        result_no = findViewById(R.id.result_no);
        viewevent_listview = findViewById(R.id.search_listview);
        searchtext = findViewById(R.id.searchtext);

        view_all = findViewById(R.id.view_all);
        view_all.setTypeface(textfont);

        auth_token =  get_auth_token("auth_token");

        pd = new ProgressDialog(Search_affiliate_list.this);

        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        id.clear();
        name.clear();
        role.clear();
        email.clear();
        p_number.clear();




        if (!isNetworkAvailable()) {

            alertdailogbox("withoutname");
        }
        else
        {
            result_no.setVisibility(View.GONE);
            methodforcalleventlist(Splash_screen.url+"referrals","");
        }
        searchtext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {


                // When user changed the Text
                 values = String.valueOf(cs);

               // if(searchtext.getText().toString().length()%2==0) {


                if (!isNetworkAvailable()) {

                    alertdailogbox("withname");
                }
                else
                {
                    id.clear();
                    name.clear();
                    role.clear();
                    email.clear();
                    p_number.clear();
                    current_page=1;
                    result_no.setVisibility(View.VISIBLE);
                    methodforcalleventlist(Splash_screen.url + "referrals?name=" + values, values);
                }



                // }
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

        cancel_search = findViewById(R.id.cancel_search);


        cancel_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                finish();

                return false;
            }
        });

        searchtext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                searchtext.setFocusable(true);
                searchtext.setFocusableInTouchMode(true);
                return false;
            }
        });

        viewevent_listview.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
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
                                    } catch (Exception ignored) {
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


        cancel_search.setTypeface(textfont);
        referal_name.setTypeface(textfont);
        referalrole.setTypeface(textfont);
        result_no.setTypeface(textfont);
        searchtext.setTypeface(textfont);

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



    public void methodforcalleventlist(String url,final String csss){
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
                                String referral_role = roleobject.getString("name").replaceAll("\\s+"," ");
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


//




                                id.add(referral_id);
                                name.add(referral_name);
                                role.add(referral_role);
                                email.add(referral_email);
                                p_number.add(phone_no);



//                                alleventlist = new Refferallist(id, name, role,email,phone_no);
//                                alleventlist_List.add(alleventlist);
                            }



                            ViewAfiliatelist allevents = new ViewAfiliatelist(Search_affiliate_list.this,id,name,role,email,p_number);
                            viewevent_listview.setAdapter(allevents);
                            viewevent_listview.setSelectionFromTop(position, 0);
                            loadingMore = false;
                            result_no.setText(count+" Results for "+"\""+csss+"\"");
                            allevents.notifyDataSetChanged();



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
                                Alert_Dailog.showNetworkAlert(Search_affiliate_list.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(Search_affiliate_list.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(Search_affiliate_list.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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



    public class ViewAfiliatelist extends BaseAdapter
    {
        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> role = new ArrayList<>();
        public LayoutInflater inflater;
        public ViewAfiliatelist(Activity context,ArrayList<String> id,ArrayList<String> name,ArrayList<String> role,ArrayList<String> email,ArrayList<String> p_number) {
            super();
            this.id =id;
            this.name =name;
            this.role =role;
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
            LinearLayout layout_event_name;
            LinearLayout layout_event_date;
            LinearLayout layout_event_btn;
            Button btnview_details;
            TextView evnt_name,event_date;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            final ViewAfiliatelist.ViewHolder holder;
            if(convertView==null)
            {
                holder = new ViewAfiliatelist.ViewHolder();
                convertView = inflater.inflate(R.layout.custome_all_events, null);

                holder.btnview_details = convertView.findViewById(R.id.btnview_details);
                holder.evnt_name = convertView.findViewById(R.id.evnt_name);
                holder.event_date = convertView.findViewById(R.id.event_date);

                holder.layout_event_name = convertView.findViewById(R.id.layout_event_name);
                holder.layout_event_date = convertView.findViewById(R.id.layout_event_date);
                holder.layout_event_btn = convertView.findViewById(R.id.layout_event_btn);

                holder.btnview_details.setTypeface(textfont);
                holder.evnt_name.setTypeface(textfont);
                holder.event_date.setTypeface(textfont);
                convertView.setTag(holder);
            }
            else
                holder=(ViewAfiliatelist.ViewHolder)convertView.getTag();



            if (position % 2 == 0) {
                holder.layout_event_name.setBackgroundColor(Color.parseColor("#E1E1E1"));
                holder.layout_event_date.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.layout_event_btn.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {

                holder.layout_event_name.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.layout_event_date.setBackgroundColor(Color.parseColor("#E1E1E1"));
                holder.layout_event_btn.setBackgroundColor(Color.parseColor("#E1E1E1"));
            }

            String evnt_names = name.get(position);
            try{
                evnt_names=evnt_names.substring(0,1).toUpperCase() +evnt_names.substring(1);
            }
            catch (Exception e)
            {
            }
            holder.evnt_name.setText( evnt_names.trim());
            holder.event_date.setText( role.get(position).substring(0,1).toUpperCase() +role.get(position).substring(1)+"");

            holder.btnview_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent ii=new Intent(getApplicationContext(), Referral_Details.class);
                    ii.putExtra("id", id.get(position));
                    startActivity(ii);
                }
            });



            return convertView;
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
}
