package com.winningticketproject.in.ChatWithOther;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;

public class User_List extends AppCompatActivity implements View.OnClickListener {
    TextView cancel_purchage,toolbar_title,tv_user_not_found;
    RecyclerView rv_chat_user_list;
    ChatUserListAdapter chatUserList;
    ArrayList<User_list_Model> user_list_models = new ArrayList<>();
    ArrayList<User_list_Model> chat_data_user = new ArrayList<>();

    String id,firstname,image,email,key,value;
    SwipeRefreshLayout chat_user_swipe_view;
    String status,last_msg;
    String current_user_id="";
    DatabaseReference databaseReference;
    int position =0;
    String user_status = "offline";
    int progress_type=0;
    JSONArray paid_participants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__list);

        databaseReference = FirebaseDatabase.getInstance().getReference("messages");

        Firebase.setAndroidContext(this);

        current_user_id = get_auth_token("chat_user_id")+"_"+get_auth_token("event_id");

        cancel_purchage = findViewById(R.id.cancel_purchage);

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("ALL CHAT");
        cancel_purchage.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar_title.setTypeface(regular);
        tv_user_not_found = findViewById(R.id.tv_user_not_found);
        tv_user_not_found.setTypeface(regular);

        rv_chat_user_list = findViewById(R.id.rv_chat_user_list);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(User_List.this);
        rv_chat_user_list.setLayoutManager(mLayoutManager3);

        chat_user_swipe_view =  findViewById(R.id.chat_user_swipe_view);
        chat_user_swipe_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Define your work here .
                position=0;
                user_list_models.clear();
                chat_user_swipe_view.setRefreshing(true);
                method_to_cal_user_list_API(2);
            }
        });

    }

    private void method_to_cal_user_list_API_status(String online_offline) {
        JSONObject object = new JSONObject();
        try {
            object.put("user_status",online_offline);
        } catch (JSONException e) { }

        String Url = Splash_screen.imageurl+"api/v2/users/user_status";
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    private void method_to_cal_user_list_API(int values) {
        position=0;
        try {
            if (values==1){
                ProgressDialog.getInstance().showProgress(User_List.this);
            }
        }catch (Exception E){
            //nothing
        }
        progress_type = values;
        user_list_models.clear();
        chat_data_user.clear();
        String Url = Splash_screen.url+"events/all_event_users?event_id="+ get_auth_token("event_id");
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            System.out.println("---------------"+response);

                            JSONObject obj = response.getJSONObject("chat_notifications_count");
                            Iterator<String> keys = obj.keys();

                            paid_participants = response.getJSONArray("paid_participants");
                            for (int i=0;i<paid_participants.length();i++){
                                JSONObject paid_obj = paid_participants.getJSONObject(i);

                                id = paid_obj.getString("id");
                                firstname = paid_obj.getString("first_name")+" "+paid_obj.getString("last_name");
                                image = paid_obj.getString("avatar_url");
                                email = paid_obj.getString("email");
                                user_status = paid_obj.getString("user_status");

                                key = keys.next();
                                value = obj.optString(key);

                                user_list_models.add(new User_list_Model(id, firstname, image, email, key, value,user_status,"",""));
                            }

                            method_to_call_refresh_data(0);

                        }catch (Exception e){
                            //nothing
                            ProgressDialog.getInstance().hideProgress();
                            tv_user_not_found.setVisibility(View.VISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (values==1){
                            ProgressDialog.getInstance().hideProgress();
                        }else {
                            chat_user_swipe_view.setRefreshing(false);
                        }                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(User_List.this);
                            }
                        }else {
                            Toast.makeText(User_List.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    private void method_to_call_refresh_data(int values) {
            String with_other_id = user_list_models.get(values).user_id+"_"+get_auth_token("event_id");

            Query lastQuery = databaseReference.child(current_user_id + "_" + with_other_id).orderByKey().limitToLast(1);

            lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {

                        if (dataSnapshot.getValue() == null){

                            chat_data_user.add(new User_list_Model(
                                    user_list_models.get(position).getUser_id(),
                                    user_list_models.get(position).getName(),
                                    user_list_models.get(position).getAvtar_imge(),
                                    user_list_models.get(position).getEmail_id(),
                                    user_list_models.get(position).getKey(),
                                    user_list_models.get(position).getValue(),
                                    user_list_models.get(position).getStatus(),
                                    "",""));
                        }else {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {

                                String message = child.child("message").getValue().toString();
                                String timer="";
                                try {
                                     timer = child.child("time").getValue().toString();
                                }catch (Exception e){ }

                                chat_data_user.add(new User_list_Model(
                                        user_list_models.get(position).getUser_id(),
                                        user_list_models.get(position).getName(),
                                        user_list_models.get(position).getAvtar_imge(),
                                        user_list_models.get(position).getEmail_id(),
                                        user_list_models.get(position).getKey(),
                                        user_list_models.get(position).getValue(),
                                        user_list_models.get(position).getStatus(),
                                        message,timer));
                            }
                        }

                        position++;
                        if (position==paid_participants.length()){

                            chatUserList = new ChatUserListAdapter(User_List.this,chat_data_user );
                            rv_chat_user_list.setAdapter(chatUserList);

                            if (progress_type==1){
                                ProgressDialog.getInstance().hideProgress();
                            }else {
                                chat_user_swipe_view.setRefreshing(false);
                            }

                            if (chat_data_user.size()>0){
                                chat_user_swipe_view.setVisibility(View.VISIBLE);
                                rv_chat_user_list.setVisibility(View.VISIBLE);
                            }else {
                                tv_user_not_found.setVisibility(View.VISIBLE);
                            }

                        }else {

                            method_to_call_refresh_data(position);

                        }

                    }catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                }

            });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                // Actions to do after 10 seconds
            }
        }, user_list_models.size()*1000);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_purchage:
                method_to_cal_user_list_API_status("offline");
                finish();
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_search, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("search by name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(chatUserList == null){
                    Toast.makeText(getApplicationContext(),"no data available",Toast.LENGTH_SHORT).show();
                }else {
                    chatUserList.getFilter().filter(newText);
                }
                return false;
            }
        });
        return(super.onCreateOptionsMenu(menu));
    }


    @Override
    protected void onResume() {
        super.onResume();
        user_list_models.clear();
        method_to_cal_user_list_API(1);
        method_to_cal_user_list_API_status("online");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver), new IntentFilter("Notification_count")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        method_to_cal_user_list_API_status("offline");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            user_list_models.clear();
            method_to_cal_user_list_API(2);
        }
    };

}