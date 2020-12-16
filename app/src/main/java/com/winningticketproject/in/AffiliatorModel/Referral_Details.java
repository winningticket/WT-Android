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
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Referral_Details extends AppCompatActivity implements View.OnClickListener {
    ListView viewevent_listview;
    Typeface textfont;
    Typeface book;
    Typeface italic_font;
    ProgressDialog pd;
    TextView cancel_purchage,toolbar_title,refferal_name,sponsar_name,phone_title,phone_values,message_title,massage_values,sponsars_title,items_name,items_values,view_all;
    String auth_token="",id="";
    int current_page=1,total_page=0,position;
    boolean loadingMore=false;


    ArrayList<String> referal_id = new ArrayList<>();
    ArrayList<String> referal_name = new ArrayList<>();
    ArrayList<String> referal_values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral__details);

        book = Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
        textfont = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");
        italic_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "Montserrat-Italic.ttf");

        pd = new ProgressDialog(Referral_Details.this);

        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        cancel_purchage = findViewById(R.id.cancel_purchage);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Referral Detail");
        cancel_purchage.setOnClickListener(this);

        auth_token = get_auth_token("auth_token");

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
         id =iin.getStringExtra("id");


        if (!isNetworkAvailable()) {

            alertdailogbox();
        }
        else
        {
            methodtogenrateclienttoken(Splash_screen.url+"referrals/show/"+id);
        }


        view_all = findViewById(R.id.view_all);
        view_all.setTypeface(textfont);

        refferal_name = findViewById(R.id.refferal_name);
        sponsar_name = findViewById(R.id.sponsar_name);
        phone_title = findViewById(R.id.phone_title);
        phone_values = findViewById(R.id.phone_values);
        message_title = findViewById(R.id.message_title);
        massage_values = findViewById(R.id.massage_values);
        sponsars_title = findViewById(R.id.sponsars_title);
        items_name = findViewById(R.id.items_name);
        items_values = findViewById(R.id.items_values);

//        referal_detail_title.setTypeface(textfont);
        refferal_name.setTypeface(textfont);
        sponsar_name.setTypeface(italic_font);
        phone_title.setTypeface(textfont);
        phone_values.setTypeface(book);
        message_title.setTypeface(textfont);
        massage_values.setTypeface(book);
        sponsars_title.setTypeface(textfont);
        items_name.setTypeface(textfont);
        items_values.setTypeface(textfont);
        cancel_purchage.setTypeface(textfont);
        toolbar_title.setTypeface(textfont);



        viewevent_listview = findViewById(R.id.referal_list_listview);


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
                                        methodtogenrateclienttoken(Splash_screen.url + "referrals/show/" + id + "?page=" + current_page);                                    } catch (Exception e) {
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

        cancel_purchage.setOnClickListener(this);


        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){
           case  R.id.cancel_purchage:
               finish();
            break;


        }
    }

    public class ViewAfiliatelist extends BaseAdapter
    {
        public LayoutInflater inflater;
        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        public ViewAfiliatelist(Activity context, ArrayList<String> id,ArrayList<String> name, ArrayList<String> values) {
            super();
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            this.id = id;
            this.name = name;
            this.values = values;
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

            String eventname=name.get(position);
            try{
                eventname=eventname.substring(0,1).toUpperCase() +eventname.substring(1);
            }
            catch (Exception e)
            {

            }

            holder.evnt_name.setText(eventname.trim());
            holder.event_date.setText(values.get(position));


            holder.btnview_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    startActivity(new Intent(getActivity(),Referral_Details.class));

                }
            });



            return convertView;
        }
    }


    public void methodtogenrateclienttoken(String url){
        pd.show();
        current_page++;
        position=referal_id.size();
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject refferal_details=response.getJSONObject("referral");
                            JSONObject rolobj=refferal_details.getJSONObject("role");
                            pd.dismiss();
                            String roles="";
                            if(response.getString("status").equals("Success")){


                                JSONObject meta = response.getJSONObject("meta");

                                total_page = meta.getInt("total_pages");


                                String referral_name = refferal_details.getString("first_name").replaceAll("\\s+"," ");

                                try{
                                    referral_name=referral_name.substring(0,1).toUpperCase() +referral_name.substring(1);
                                }
                                catch (Exception e)
                                {

                                }

                                String referral_email=  refferal_details.getString("email");
                                String phone_no= refferal_details.getString("phone");
                                if(referral_name.equals("")||referral_name.equals(null))
                                {
                                    referral_name="Not mentioned";
                                }
                                if(referral_email.equals("")||referral_email.equals(null))
                                {
                                    referral_email="Not mentioned";
                                }
                                if(phone_no.equals("")||phone_no.equals(null))
                                {
                                    phone_no="Not mentioned";
                                }
                                refferal_name.setText( referral_name.trim());

                                roles = rolobj.getString("name");

                                if(roles.equals("organizer")){

                                    roles= "Event Organizer";
                                    String title ="All " +roles+ " Events";
                                    sponsars_title.setText(title);


                                }else if(roles.equals("contributor"))
                                {
                                    roles = "Participant";
                                    String title ="All " +roles+ " Events";
                                    sponsars_title.setText(title);

                                }else if(roles.equals("affiliate"))
                                {
                                    roles = "Affiliate";
                                    String title ="All " +roles+ " Referrals";
                                    sponsars_title.setText(title);

                                }else if(roles.equals("sponsor"))
                                {
                                    roles = "Sponsor";
                                    String title ="All " +roles+ " Donations";
                                    sponsars_title.setText(title);

                                }

                                sponsar_name.setText(roles);
                                phone_values.setText(phone_no);
                                massage_values.setText(referral_email);

                                JSONArray events;
                                if(rolobj.getString("name").equals("affiliate"))
                                {
                                    events=response.getJSONArray("referrals");
                                }else {
                                    events = response.getJSONArray("events");
                                }
//                                {"referral":{"email":"sponsora@carmatec.com","phone":"9869845674","first_name":"sponsor",
//                                        "role":{"name":"sponsor"}},
//                                    "events":[{"amount_contribution":2000,"event":{"id":284,"name":"test64abc"}},
//                                    {"amount_contribution":100,"event":{"id":237,"name":"Ecofriendly"}}],
//                                    "meta":{"current_page":1,"next_page":0,"prev_page":0,"total_pages":1,"total_count":2},
//                                  "status":"Success"}


                                if(rolobj.getString("name").equals("sponsor")){

                                    items_name.setText("Item Name");
                                    items_values.setText("Item Value");
                                    view_all.setText("No Donations Found");
                                    for(int i=0;i<events.length();i++)
                                    {
                                        JSONObject eventsobj= events.getJSONObject(i);
                                        JSONObject events_name= eventsobj.getJSONObject("event");
                                        String evname="",amount_date="";
                                        evname=events_name.getString("name").replaceAll("\\s+"," ");
                                        amount_date=eventsobj.getString("amount_contribution");


                                        if(evname.equals("")||evname.equals(null))
                                        {
                                            evname="Not mentioned";
                                        }


                                        try {

                                            evname=evname.substring(0,1).toUpperCase() +evname.substring(1).replaceAll("\\s+"," ");

                                        }catch (Exception e){

                                        }

                                        String finalprice="";

                                        String pricevalue=amount_date;
                                        if(pricevalue.contains(".")) {
                                            String[] pricesplit = pricevalue.split("\\.");
                                            if (pricesplit[1].length() == 1) {
                                                finalprice = pricevalue + "0";
                                            }
                                            else if(pricesplit[1].length() == 2){
                                                finalprice = pricevalue ;
                                            }
                                        }
                                        else
                                        {
                                            finalprice = pricevalue + ".00";
                                        }


                                        referal_id.add(events_name.getString("id"));
                                        referal_name.add(evname);
                                        referal_values.add(finalprice);
                                    }

                                    ViewAfiliatelist allevents = new ViewAfiliatelist(Referral_Details.this,referal_id,referal_name,referal_values);
                                    viewevent_listview.setAdapter(allevents);
                                    viewevent_listview.setSelectionFromTop(position, 0);
                                    loadingMore = false;


                                    if (referal_id.isEmpty()) {
                                        viewevent_listview.setVisibility(View.GONE);
                                        view_all.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        viewevent_listview.setVisibility(View.VISIBLE);
                                        view_all.setVisibility(View.GONE);
                                    }


                                }else if(rolobj.getString("name").equals("contributor")){
                                    items_name.setText("Event Name");
                                    items_values.setText("Event Date");
                                    view_all.setText("No Events Found");

                                    for(int i=0;i<events.length();i++)
                                    {
                                        JSONObject eventsobj= events.getJSONObject(i);
                                        String evname="",amount_date="";
                                        evname=eventsobj.getString("name").replaceAll("\\s+"," ");
                                        amount_date=eventsobj.getString("start_date");
                                        referal_id.add(eventsobj.getString("id"));



                                        if(evname.equals("")||evname.equals(null))
                                        {
                                            evname="Not mentioned";
                                        }
                                        if(amount_date.equals("")||amount_date.equals(null))
                                        {
                                            amount_date="Not mentioned";
                                        }

                                        try {

                                            evname=evname.substring(0,1).toUpperCase() +evname.substring(1).replaceAll("\\s+"," ");

                                        }catch (Exception e){

                                        }

                                        referal_name.add(evname);


                                        try {
                                            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                            java.util.Date date = null;
                                            try {
                                                date = format.parse(amount_date);
                                            } catch (ParseException e) {

                                                //nothing
                                            }
                                            java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MMM dd, yyyy \nHH:mm aa");
                                            String newDateStr = postFormater.format(date);
                                            referal_values.add(newDateStr.replace("am","AM").replace("pm","PM")+" EST");
                                        }
                                        catch (Exception e)
                                        {
                                            referal_values.add(amount_date);
                                        }
                                    }

                                    ViewAfiliatelist allevents = new ViewAfiliatelist(Referral_Details.this,referal_id,referal_name,referal_values);
                                    viewevent_listview.setAdapter(allevents);
                                    viewevent_listview.setSelectionFromTop(position, 0);
                                    loadingMore = false;


                                    if (referal_id.isEmpty()) {
                                        viewevent_listview.setVisibility(View.GONE);
                                        view_all.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        viewevent_listview.setVisibility(View.VISIBLE);
                                        view_all.setVisibility(View.GONE);
                                    }


                                }
                                else if(rolobj.getString("name").equals("organizer")){

                                    items_name.setText("Event Name");
                                    items_values.setText("Event Date");
                                    view_all.setText("No Events Found");

                                    for(int i=0;i<events.length();i++)
                                    {
                                        JSONObject eventsobj= events.getJSONObject(i);
                                        String evname="",amount_date="";
                                        evname=eventsobj.getString("name");
                                        amount_date=eventsobj.getString("start_date");
                                        referal_id.add(eventsobj.getString("id"));

                                        if(evname.equals("")||evname.equals(null))
                                        {
                                            evname="Not mentioned";
                                        }
                                        if(amount_date.equals("")||amount_date.equals(null))
                                        {
                                            amount_date="Not mentioned";
                                        }

                                        try {

                                            evname=evname.substring(0,1).toUpperCase() +evname.substring(1).replaceAll("\\s+"," ");

                                        }catch (Exception e){

                                        }

                                        referal_name.add(evname);


                                        try {
                                            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                            java.util.Date date = null;
                                            try {
                                                date = format.parse(amount_date);
                                            } catch (ParseException e) {

                                                //nothing
                                            }
                                            java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MMM dd, yyyy \nHH:mm aa");
                                            String newDateStr = postFormater.format(date);
                                            referal_values.add(newDateStr.replace("am","AM").replace("pm","PM")+" EST");
                                        }
                                        catch (Exception e)
                                        {
                                            referal_values.add(amount_date);
                                        }
                                    }

                                    ViewAfiliatelist allevents = new ViewAfiliatelist(Referral_Details.this,referal_id,referal_name,referal_values);
                                    viewevent_listview.setAdapter(allevents);
                                    viewevent_listview.setSelectionFromTop(position, 0);
                                    loadingMore = false;


                                    if (referal_id.isEmpty()) {
                                        viewevent_listview.setVisibility(View.GONE);
                                        view_all.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        viewevent_listview.setVisibility(View.VISIBLE);
                                        view_all.setVisibility(View.GONE);
                                    }
                                } else if(rolobj.getString("name").equals("affiliate")){




                                    items_name.setText("Referral Name");
                                    items_values.setText("User Role");
                                    view_all.setText("No Referrals Found");
                                    for(int i=0;i<events.length();i++)
                                    {

////                                     {
//                                        "referral": {
//                                        "email": "sdfsdfsdfsd@gmail.com",
//                                                "phone": "4534543535",
//                                                "first_name": "Sdsdfsdf",
//                                                "role": {
//                                            "name": "affiliate"
//                                        }
//                                    },
//                                        "referrals": [
//                                        {
//                                            "id": 75,
//                                                "email": "sxwred@gmail.com",
//                                                "phone": "234234234",
//                                                "first_name": "testwe",
//                                                "role": {
//                                            "name": "sponsor"
//                                        }
//                                        }
//],
//                                        "meta": {
//                                        "current
//                                        _page": 1,
//                                        "next_page": 0,
//                                                "prev_page": 0,
//                                                "total_pages": 1,
//                                                "total_count": 1
//                                    },
//                                        "status": "Success"
//                                    }


                                        JSONObject eventsobj= events.getJSONObject(i);


                                        String evname="",amount_date="";
                                        evname=eventsobj.getString("first_name");
                                        referal_id.add(eventsobj.getString("id"));

                                        JSONObject user_role = eventsobj.getJSONObject("role");
                                        String role = user_role.getString("name").substring(0,1).toUpperCase() +user_role.getString("name").substring(1);


                                        if(evname.equals("")||evname.equals(null))
                                        {
                                            evname="Not mentioned";
                                        }else {
                                            evname = evname.substring(0,1).toUpperCase() +evname.substring(1);

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

                                        referal_name.add(evname);
                                        referal_values.add(role);


                                    }

                                    ViewAfiliatelist allevents = new ViewAfiliatelist(Referral_Details.this,referal_id,referal_name,referal_values);
                                    viewevent_listview.setAdapter(allevents);
                                    viewevent_listview.setSelectionFromTop(position, 0);
                                    loadingMore = false;


                                    if (referal_id.isEmpty()) {
                                        viewevent_listview.setVisibility(View.GONE);
                                        view_all.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        viewevent_listview.setVisibility(View.VISIBLE);
                                        view_all.setVisibility(View.GONE);
                                    }
                                }

//

                            }
                        }catch (Exception E){
                            //nothing
                            pd.dismiss();
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
                                Alert_Dailog.showNetworkAlert(Referral_Details.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(Referral_Details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(Referral_Details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
                    methodtogenrateclienttoken(Splash_screen.url+"referrals/show/"+id);
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }
}
