package com.winningticketproject.in.AffiliatorModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.winningticketproject.in.SignInSingup.NewBefore_login;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Refferallist;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Afiliate_Referal_List extends AppCompatActivity implements View.OnClickListener {

    ListView viewevent_listview;
    TextView seach_text;
    TextView referral_title;
    TextView textsreach;
    TextView referral_name;
    TextView referal_role;
    TextView logout;
    Typeface font,textfont;
    Button edit_button,addnew_button,filter_button;
    LinearLayout search_list ;
    String auth_token="";
    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> role = new ArrayList<>();
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> p_number = new ArrayList<>();
    ProgressDialog pd;
    int current_page=1;
    int total_page=0;
    int position;
    boolean loadingMore=false;
    int backButtonCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afiliate__referal__list);
//        view = inflater.inflate(R.layout.activity_afiliate__referal__list, container, false);

        auth_token = get_auth_token("auth_token");

        seach_text = findViewById(R.id.seach_text);
        referral_title = findViewById(R.id.referral_title);
        textsreach = findViewById(R.id.textsreach);
        referral_name = findViewById(R.id.referral_name);
        referal_role = findViewById(R.id.referal_role);

        edit_button = findViewById(R.id.edit_button);
        addnew_button = findViewById(R.id.addnew_button);
        filter_button = findViewById(R.id.filter_button);


        search_list = findViewById(R.id.search_list);

        textfont = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");
        font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        seach_text.setText("\uf002");
        seach_text.setTypeface(font);


        logout = findViewById(R.id.logout);
        logout.setText("\uf08b");
        logout.setTypeface(font);


        referral_title.setTypeface(textfont);
        textsreach.setTypeface(textfont);
        referral_name.setTypeface(textfont);
        referal_role.setTypeface(textfont);
        edit_button.setTypeface(textfont);
        addnew_button.setTypeface(textfont);
        filter_button.setTypeface(textfont);

        pd = new ProgressDialog(Afiliate_Referal_List.this);

        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        viewevent_listview = findViewById(R.id.referral_listview);
        id.clear();
        name.clear();
        role.clear();
        email.clear();
        p_number.clear();
        edit_button.setOnClickListener(this);
        addnew_button.setOnClickListener(this);
        filter_button.setOnClickListener(this);
        search_list.setOnClickListener(this);



        viewevent_listview.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {


                if(firstVisibleItem + visibleItemCount == totalItemCount && !(loadingMore)  ){
                    if(current_page<=total_page){
                        if (!isNetworkAvailable()) {
                            Toast.makeText(getApplicationContext(), "You don't have internet connection.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            try {
                                loadingMore=true;
                                methodforcalleventlist(Splash_screen.url+"referrals?page="+current_page+"&per_page=20");
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){

            }
        });

        logout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Afiliate_Referal_List.this);
                builder.setCancelable(false);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        Share_it.remove_key("auth_token");
                        Share_it.remove_key("role");

//                        Toast.makeText(getApplicationContext(),"Successfully Logout", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getApplicationContext(), NewBefore_login.class));
//                        finishAffinity();
                    }
                });

                android.support.v7.app.AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
                dialog.show();


                return false;
            }
        });




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
        if (!isNetworkAvailable()) {

            alertdailogbox();
        }
        else
        {
            methodforcalleventlist(Splash_screen.url+"referrals");
        }

    }

    public void alertdailogbox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Afiliate_Referal_List.this);
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
                    methodforcalleventlist(Splash_screen.url+"referrals");
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }
    public void methodforcalleventlist(String url){
        if(!pd.isShowing())
        {
            pd.show();
        }
        current_page++;
        position=id.size();

        //alleventlist_List.clear();
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Refferallist alleventlist;

                            if (pd.isShowing()){
                                pd.dismiss();

                            }

                            JSONArray reffreal = response.getJSONArray("referrals");
                            for (int i=0;i<reffreal.length();i++) {
                                JSONObject eventobject = reffreal.getJSONObject(i);
                                JSONObject roleobject = eventobject.getJSONObject("role");

                                JSONObject metaobject = response.getJSONObject("meta");
                                total_page = metaobject.getInt("total_pages");

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


                                id.add(referral_id);
                                name.add(referral_name);
                                role.add(referral_role);
                                email.add(referral_email);
                                p_number.add(phone_no);

                               // alleventlist = new Refferallist(id, name, role,email,phone_no);
                               // alleventlist_List.add(alleventlist);
                            }

                            ViewAfiliatelist allevents = new ViewAfiliatelist(Afiliate_Referal_List.this,id,name,role,email,p_number);
                            viewevent_listview.setAdapter(allevents);
                            viewevent_listview.setSelectionFromTop(position, 0);
                            loadingMore = false;
                            if (pd.isShowing()){
                                pd.dismiss();
                            }


                            if (id.isEmpty()) {
                                viewevent_listview.setVisibility(View.GONE);
                            }
                            else {
                                viewevent_listview.setVisibility(View.VISIBLE);
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
                                Alert_Dailog.showNetworkAlert(Afiliate_Referal_List.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(Afiliate_Referal_List.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(Afiliate_Referal_List.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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



    @Override
    public void onClick(View view) {

        Intent in;

        switch (view.getId()){

            case R.id.edit_button:

                in = new Intent(getApplicationContext(), EditReferal_list.class);
                startActivity(in);

                break;

            case R.id.addnew_button:
                in = new Intent(getApplicationContext(), Add_Referral_list.class);
                startActivity(in);
                break;

            case R.id.filter_button:
                in = new Intent(getApplicationContext(), Afiliate_Filter.class);
                startActivity(in);

                break;


            case R.id.search_list:
                in = new Intent(getApplicationContext(), Search_affiliate_list.class);
                startActivity(in);
                break;



        }
    }

    public class ViewAfiliatelist extends BaseAdapter
    {

        public LayoutInflater inflater;
        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> role = new ArrayList<>();


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
            Button btnview_details;
            TextView evnt_name,event_date;
            LinearLayout layout_event_name;
            LinearLayout layout_event_date;
            LinearLayout layout_event_btn;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            final ViewHolder holder;
            if(convertView==null)
            {
                holder = new ViewHolder();
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



    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        else
        {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


}
