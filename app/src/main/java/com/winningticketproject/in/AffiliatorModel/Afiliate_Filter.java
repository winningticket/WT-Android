package com.winningticketproject.in.AffiliatorModel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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

/**
 * Created by carmatec on 7/6/17.
 */

public class Afiliate_Filter extends AppCompatActivity {
    String auth_token="";
    ArrayAdapter<String> filters;
    Spinner role;
    Typeface tf;
    Typeface book;
    ListView filter_listview;
    ArrayList<String> filterListView = new ArrayList<String>();
    ArrayList<String> filtersListView = new ArrayList<String>();
    ArrayList<String> filtersListViewid = new ArrayList<String>();
    ProgressDialog pd;
    boolean loadingMore=false;
    int current_page=1;
    int total_page=0;
    int position;
    ViewFilterlist viewFilterlist;
    String mainurl = "";
    TextView user_role,cancel_purchage,account_title,view_all;
    private static final String[] filter=new String[]{ "Select Role Type", "Event Organizer","Participant","Affiliate","Sponsor"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        auth_token = get_auth_token("auth_token");

        mainurl = Splash_screen.url + "referrals?role=";

        pd = new ProgressDialog(Afiliate_Filter.this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        book = Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
        tf = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");

        role = findViewById(R.id.role);

        account_title = findViewById(R.id.account_title);
        account_title.setText("Filter");
        cancel_purchage = findViewById(R.id.cancel_purchage);
        cancel_purchage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        view_all = findViewById(R.id.view_all);
        view_all.setTypeface(tf);

        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });
        if (!isNetworkAvailable()) {

            alertdailogbox();
        }
        else
        {
            methodforcalleventlist(mainurl);
        }

        user_role = findViewById(R.id.user_role);

        filters = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_text, filter);

        filters   = new ArrayAdapter<String>(Afiliate_Filter.this,R.layout.custom_country_text, filter) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTypeface(book);
                ((TextView) v).setTextColor(getResources().getColorStateList(R.color.colorblack));


                return v;
            }


            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                ((TextView) v).setTypeface(book);
                if(((TextView) v).getText().toString().equals("Select Role Type")){
                    // Set the hint text color gray

                    ((TextView) v).setTextColor(Color.GRAY);
                }
                else
                {
                    ((TextView) v).setTextColor(Color.BLACK);
                }
                return v;
            }
        };

        filters.setDropDownViewResource( R.layout.custom_country_text);
        role.setAdapter(filters);



        filter_listview= findViewById(R.id.filter_listview);

        filter_listview.setOnScrollListener(new AbsListView.OnScrollListener(){
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
                                        methodforcalleventlist(mainurl+"&page="+current_page);
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

            }
        });






        user_role.setTypeface(tf);

//        filters = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_text, filter);
//        filters.setDropDownViewResource(R.layout.custom_text);
//        role.setAdapter(filters);

        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = role.getSelectedItem().toString();
                if (item.equals("Select Role Type")) {

                } else if (item.equals("Sponsor")) {
                    current_page=1;
                    mainurl = Splash_screen.url + "referrals?role=sponsor";
                    filterListView.clear();
                    filtersListView.clear();
                    filtersListViewid.clear();
                    methodforcalleventlist(mainurl);
                }else if (item.equals("Participant")) {
                    current_page=1;
                    filterListView.clear();
                    filtersListView.clear();
                    filtersListViewid.clear();
                    mainurl = Splash_screen.url + "referrals?role=contributor";
                    methodforcalleventlist(mainurl);}
                else if (item.equals("Event Organizer")){
                    current_page=1;
                    filterListView.clear();
                    filtersListView.clear();
                    filtersListViewid.clear();
                    mainurl = Splash_screen.url + "referrals?role=organizer";
                    methodforcalleventlist(mainurl);
                }else if (item.equals("Affiliate")){
                    current_page=1;
                    filterListView.clear();
                    filtersListView.clear();
                    filtersListViewid.clear();
                    mainurl = Splash_screen.url + "referrals?role=Affiliate";
                    methodforcalleventlist(mainurl);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        current_page=1;
//        filterListView.clear();
//        filtersListView.clear();
//        methodforcalleventlist(mainurl);

    }




    private void methodforcalleventlist(String url) {
            String tag_json_obj = "";

        current_page++;
        position=filterListView.size();
              if (!pd.isShowing()){
              pd.show();}
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {

                    try {
                       JSONArray reffreal = response.getJSONArray("referrals");
                        for (int i=0;i<reffreal.length();i++) {
                            JSONObject eventobject = reffreal.getJSONObject(i);
                            JSONObject roleobject = eventobject.getJSONObject("role");

                            JSONObject metaobject = response.getJSONObject("meta");
                            total_page = metaobject.getInt("total_pages");

                            String id = eventobject.getString("id");
                            String email = eventobject.getString("email");
                            String phone = eventobject.getString("phone").replaceAll("\\s+"," ");
                            String name=  eventobject.getString("first_name").replaceAll("\\s+"," ");
                            String role="";

                            try{
                                role= roleobject.getString("name").substring(0,1).toUpperCase()+roleobject.getString("name").substring(1).toLowerCase().replaceAll("\\s+"," ");
                            }
                            catch (Exception e)
                            {

                            }

                            if(name.equals("")||name.equals(null))
                            {
                                name="Not mentioned";
                            }
                            if(role.equals("")||role.equals(null))
                            {
                                role="Not mentioned";
                            }

                            if (role.equals("Organizer")){

                                role = "Event Organizer";
                            }else if(role.equals("Contributor"))
                            {
                                role = "Participant";
                            }else {
                                role = role;
                            }

                            filterListView.add(name);
                            filtersListView.add(role);
                            filtersListViewid.add(id);
                        }

                        if (pd.isShowing()){
                            pd.dismiss();}

                        if (!filterListView.isEmpty() && !filtersListView.isEmpty()) {
                            viewFilterlist = new ViewFilterlist(getApplicationContext(), filtersListViewid, filterListView, filtersListView);
                            viewFilterlist.notifyDataSetChanged();
                            filter_listview.setAdapter(viewFilterlist);
                            filter_listview.setSelectionFromTop(position, 0);
                            loadingMore = false;

                        }
                            if (filterListView.isEmpty()) {
                                filter_listview.setVisibility(View.GONE);
                                view_all.setVisibility(View.VISIBLE);
                            } else {
                                filter_listview.setVisibility(View.VISIBLE);
                                view_all.setVisibility(View.GONE);
                            }


                    } catch (Exception e) {
                        //nothing

                        if (pd.isShowing()){
                            pd.dismiss();}
                    }


                    if (pd.isShowing()){
                        pd.dismiss();}

                }
            }, new Response.ErrorListener() {

                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                        // HTTP Status Code: 401 Unauthorized
                        if (error.networkResponse.statusCode==401){
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Alert_Dailog.showNetworkAlert(Afiliate_Filter.this);
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(Afiliate_Filter.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        if (pd.isShowing()){
                            pd.dismiss();}
                        Toast.makeText(Afiliate_Filter.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
            jsonObjReq.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }

    public class ViewFilterlist extends BaseAdapter
    {

        public LayoutInflater inflater;
        ArrayList<String> id = new ArrayList<String>();
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> role = new ArrayList<String>();

        public ViewFilterlist(Context context, ArrayList<String> id, ArrayList<String> name,ArrayList<String> role) {
            super();
            this.id=id;
            this.name=name;
            this.role=role;

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
            TextView evnt_name,event_date;
            Button btnview_details;


        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            final ViewFilterlist.ViewHolder holder;
            if(convertView==null)
            {
                holder = new ViewFilterlist.ViewHolder();
                convertView = inflater.inflate(R.layout.custome_all_events, null);

                holder.btnview_details = convertView.findViewById(R.id.btnview_details);
                holder.evnt_name = convertView.findViewById(R.id.evnt_name);
                holder.event_date = convertView.findViewById(R.id.event_date);

                holder.layout_event_name = convertView.findViewById(R.id.layout_event_name);
                holder.layout_event_date = convertView.findViewById(R.id.layout_event_date);
                holder.layout_event_btn = convertView.findViewById(R.id.layout_event_btn);

                holder.btnview_details.setTypeface(tf);
                holder.evnt_name.setTypeface(tf);
                holder.event_date.setTypeface(tf);
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

            String nameevent="",roleevent="";
            try{
                nameevent=name.get(position).substring(0,1).toUpperCase() + name.get(position).substring(1) + "";
            }
            catch (Exception e)
            {

            }


            try{
                roleevent=role.get(position).substring(0,1).toUpperCase() + role.get(position).substring(1) + "";
            }
            catch (Exception e)
            {

            }

                holder.evnt_name.setText(nameevent.trim());
                holder.event_date.setText(roleevent.trim());
                holder.btnview_details.setTypeface(tf);


            holder.btnview_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent in = new Intent(getApplicationContext(), Referral_Details.class);
                    in.putExtra("id",id.get(position));
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
                    methodforcalleventlist(mainurl);
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

}
