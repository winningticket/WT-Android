package com.winningticketproject.RoundNotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Round_notes_details extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Round_note_List> round_note_lists = new ArrayList<>();

    RecyclerView rv_your_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_notes_details);

        load_note_details_top_section();

        load_note_details_recylerview_data();

        if (isNetworkAvailable()){
            load_round_note_api();
        }else {
            Toast.makeText(getApplicationContext(),"No internet Connection",Toast.LENGTH_LONG).show();
        }
    }

    private void load_round_note_api() {
        ProgressDialog.getInstance().showProgress(Round_notes_details.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET, Splash_screen.url +"users/round_note_details/"+getIntent().getStringExtra("round_note_id"),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray hole_list = response.getJSONArray("hole_list");
                            for (int i=0;i<hole_list.length();i++){

                                JSONObject object = hole_list.getJSONObject(i);
                                round_note_lists.add(new Round_note_List("0","Hole "+object.getString("hole_num"),object.getString("notes")));

                            }
                            YourRoundADetailsdapter yourRoundAdapter = new YourRoundADetailsdapter(Round_notes_details.this,round_note_lists);
                            rv_your_note.setAdapter(yourRoundAdapter);
                            rv_your_note.setItemAnimator(new DefaultItemAnimator());

                            ProgressDialog.getInstance().hideProgress();
                        } catch (Exception e) {
                            //nothing
                           ProgressDialog.getInstance().hideProgress();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(getApplicationContext());
                            } else {
                                Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    private void load_note_details_recylerview_data() {
        TextView tv_note_detail_event_name = findViewById(R.id.tv_note_detail_event_name);
        tv_note_detail_event_name.setTypeface(medium);
        tv_note_detail_event_name.setText(getIntent().getStringExtra("rounds_name"));

        TextView tv_note_details_event_date = findViewById(R.id.tv_note_details_event_date);
        tv_note_details_event_date.setTypeface(regular);
        tv_note_details_event_date.setText(getIntent().getStringExtra("rounds_date"));
        rv_your_note= findViewById(R.id.rv_your_note_details);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(Round_notes_details.this);
        rv_your_note.setLayoutManager(mLayoutManager3);

        rv_your_note.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void load_note_details_top_section() {
        TextView cancel_purchage = findViewById(R.id.cancel_purchage);
        cancel_purchage.setOnClickListener(this);
        TextView account_title = findViewById(R.id.account_title);
        account_title.setText("ROUND NOTES");
        account_title.setTypeface(medium);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_purchage:
                finish();
                break;
        }
    }
}
