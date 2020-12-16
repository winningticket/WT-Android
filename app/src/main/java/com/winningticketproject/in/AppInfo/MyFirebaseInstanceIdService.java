package com.winningticketproject.in.AppInfo;

import android.content.Intent;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.winningticketproject.in.CourseTab.Course_Search_Page;
import com.winningticketproject.in.CourseTab.Courses_list_flow;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    //this method will be called
    //when the token is generated
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //now we will have the token
        String token = FirebaseInstanceId.getInstance().getToken();
        //for now we are displaying the token in the log
        //copy it as this method is called only when the new token is generated
        //and usually new token is only generated when the app is reinstalled or the data is cleared
        Log.d("MyRefreshedToken", token);
        send_token_to_server(token);
    }

    private void send_token_to_server(String deivce_token) {
        JSONObject user = new JSONObject();
        try {
            JSONObject parameters = new JSONObject();
            parameters.put("device_type","android");
            parameters.put("device_id",deivce_token);
            user.put("user",parameters);

            System.out.println("---user----"+user);
        }catch (Exception e){
            //nothing
        }
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"notification_settings/device_registration",user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println("-----service ----response----"+response);
                            if (response.getString("status").equals("Success")){
                                Intent in = new Intent(getApplicationContext(),Courses_list_flow.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                startActivity(in);
                            }
                        }catch (Exception e){//nothing
                        }
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
}
