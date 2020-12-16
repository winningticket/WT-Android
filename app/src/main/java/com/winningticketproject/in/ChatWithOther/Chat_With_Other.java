package com.winningticketproject.in.ChatWithOther;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.HttpStatus;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.winningticketproject.in.EventTab.NewEventDetailPage.event_type;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;

public class Chat_With_Other extends AppCompatActivity implements View.OnClickListener,MediaPlayerUtils.Listener {

    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    CircleImageView user_list_logo;
    TextView chat_with,current_time,current_date;
    LinearLayout back_button;
    DatabaseReference mDatabase;
    String public_key="";
    String notification="false",messageText="";
    JsonObjectRequest myRequest;
    Handler handler;
    Runnable runnable;
    ChildEventListener childEventListener;
    Typeface regular_new;

    ImageView attached_image;
    // view for image view
    // Uri indicates, where the image will be picked from
    private Uri filePath;
    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    //records buttons
    private static String mFileName = null;
    LinearLayout msg_layout;

    LinearLayout top_layout;

    AudioListAdapter chatUserList;
    ArrayList<chat_data> chat_data = new ArrayList<>();

    String mime_type="";
     RecordButton send_text1;
    LinearLayout tv_send_item;

    RecordView recordView;
    String outputFile;
    MediaRecorder myAudioRecorder;
    RecordButton record_button;

    private static final int RC_PERMISSION = 1001;

    public List<AudioStatus> audioStatusList = new ArrayList<>();

    private Context context;

    @BindView(R.id.recylerview_chat_data)
    RecyclerView recylerview_chat_data;

    private Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__with__other);

        ButterKnife.bind(this);
        context = Chat_With_Other.this;

        regular_new = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");

        LinearLayoutManager mLayoutManager3 = new LinearLayoutManager(Chat_With_Other.this);
        recylerview_chat_data.setLayoutManager(mLayoutManager3);
        chatUserList = new AudioListAdapter(Chat_With_Other.this,chat_data );
        recylerview_chat_data.setAdapter(chatUserList);

        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);

        tv_send_item = findViewById(R.id.tv_send_item);
        send_text1 = findViewById(R.id.record_button);
        tv_send_item.setOnClickListener(this);

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE}, 0);
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 0);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        } else {
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            myAudioRecorder.setOutputFile(outputFile);
        }

        recordView = findViewById(R.id.record_view);
        record_button = findViewById(R.id.record_button);

        record_button.setRecordView(recordView);

        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 8
        recordView.setCancelBounds(8);
        recordView.setSmallMicColor(Color.parseColor("#c2185b"));
        //prevent recording under one Second
        recordView.setLessThanSecondAllowed(false);
        recordView.setSlideToCancelText("Slide To Cancel");
        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);

        audio_records();

//        messageArea.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if (charSequence.length()>0){
//                    tv_send_item.setVisibility(View.VISIBLE);
//                    send_text1.setVisibility(View.GONE);
//                }else {
//                    tv_send_item.setVisibility(View.GONE);
//                    recordView.setVisibility(View.VISIBLE);
//                    send_text1.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });

        messageArea.setTypeface(regular_new);

        if (getIntent().getStringExtra("notification_type").equals("chat")){
            UserDetails.chatWith_user_name = getIntent().getStringExtra("sender_first_name");

            UserDetails.chatWith_id = getIntent().getStringExtra("sender_id")+"_"+getIntent().getStringExtra("event_id");

            UserDetails.current_user_chat_id = getIntent().getStringExtra("receiver_id")+"_"+getIntent().getStringExtra("event_id");
        }else {

            UserDetails.current_user_chat_id = get_auth_token("chat_user_id")+"_"+get_auth_token("event_id");
        }

        msg_layout = findViewById(R.id.msg_layout);
        method_to_call_reset_notification();

        handler = new Handler();

        Firebase.setAndroidContext(this);

        top_layout = findViewById(R.id.top_layout);

        if (event_type.equals("passed")){
            top_layout.setVisibility(View.GONE);
        }

        user_list_logo = findViewById(R.id.user_list_logo);

        try {
            Picasso.with(this)
                    .load(getIntent().getStringExtra("chat_with_icon")).skipMemoryCache()
                    .placeholder(R.drawable.profile_pic).error(R.drawable.profile_pic)         // optional
                    .into(user_list_logo);
        }catch (Exception e){ }

        user_list_logo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (UserDetails.user_progile.equals("") || UserDetails.user_progile.equals(null) || UserDetails.user_progile.equals("null")){
                    Toast.makeText(Chat_With_Other.this,"Profile pic not uploaded",Toast.LENGTH_LONG).show();
                }else {
                    image_popup();
                }
                return false;
            }
        });

        current_date = findViewById(R.id.current_date);
        current_time = findViewById(R.id.current_time);

        current_time.setTypeface(regular_new);
        current_date.setTypeface(regular_new);

        load_current_date_time();

        chat_with = findViewById(R.id.tv_user_name);
        chat_with.setTypeface(regular_new);
        chat_with.setText(UserDetails.chatWith_user_name);

        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(this);

        reference1 = new Firebase("https://winning-ticket-e617f.firebaseio.com/messages/" + UserDetails.current_user_chat_id + "_" + UserDetails.chatWith_id);
        reference2 = new Firebase("https://winning-ticket-e617f.firebaseio.com/messages/" + UserDetails.chatWith_id + "_" + UserDetails.current_user_chat_id);

        mDatabase = FirebaseDatabase.getInstance().getReference("messages").child(UserDetails.current_user_chat_id + "_" + UserDetails.chatWith_id);
        public_key = mDatabase.push().getKey();

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // image attachment
        attached_image = findViewById(R.id.attached_image);
        attached_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRunTimePermission();
            }
        });

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                final Map map = dataSnapshot.getValue(Map.class);

                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                String timer_values="";
                try {
                  timer_values = map.get("time").toString();
                }catch (Exception e){
                    timer_values="";
                }

                String attachment_utl="";
                String mime_type2="";
                mime_type="";
                try {
                    attachment_utl = map.get("attachment_url").toString();
                    mime_type = map.get("mime_type").toString();
                    mime_type2 = map.get("mime_type").toString();
                }catch (Exception e){
                    mime_type="text";
                    mime_type2="text";
                }

                if(userName.equals(UserDetails.current_user_chat_id)){
                    chat_data.add(new chat_data(message, "1",attachment_utl, mime_type2,timer_values));
                }
                else{
                    chat_data.add(new chat_data(message, "2",attachment_utl, mime_type2,timer_values));
                }
                audioStatusList.add(new AudioStatus(AudioStatus.AUDIO_STATE.IDLE.ordinal(), 0));

                recylerview_chat_data.setAdapter(chatUserList);
                chatUserList.notifyDataSetChanged();
                recylerview_chat_data.smoothScrollToPosition(chat_data.size());

                if (public_key.equals(map.get("msg_id").toString())){
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            //Perform any task here which you want to do after time finish.
                            if (!map.get("receiver").toString().equals("received") && notification.equals("true")) {
                                System.out.println(map.get("receiver").toString()+"------------"+notification);
                                go_to_push_notification(map.get("message").toString(),mime_type+"");
                            }
                        }
                    };
                    handler.postDelayed(runnable, 3000);
                }else if (!public_key.equals(map.get("msg_id").toString())){

                    reference1.child(map.get("msg_id").toString()).child("receiver").setValue("received");
                    reference2.child(map.get("msg_id").toString()).child("receiver").setValue("received");

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Map maps = dataSnapshot.getValue(Map.class);
                System.out.println(maps+"----- changed ----"+maps.get("msg_id").toString());
                if (public_key.equals(maps.get("msg_id").toString())){
                    if (!maps.get("receiver").equals("received")){
                        notification="true";
                    }else{
                        notification="false";
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(FirebaseError databaseError) {
            }
        };
        reference1.addChildEventListener(childEventListener);

        for(int i = 0; i < chat_data.size(); i++) {
            audioStatusList.add(new AudioStatus(AudioStatus.AUDIO_STATE.IDLE.ordinal(), 0));
        }

        requestPermissionIfNeeded();

    }

    public boolean requestPermissionIfNeeded() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSION);
                return true;
            }
        }
        return false;
    }

    private void audio_records() {

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE}, 0);

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 0);

        recordView.setVisibility(View.VISIBLE);
        
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                } catch (IOException ioe) {
                    // make something
                }
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onFinish(long recordTime) {
                String time = getHumanTimeText(recordTime);
                if (recordTime>0){
                    uploadaudiofile(Uri.fromFile(new File(outputFile)));
                }
            }

            @Override
            public void onLessThanSecond() {
            }
        });
    }

    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    private void uploadaudiofile(Uri path) {
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        // Defining the child of storageReference
        StorageReference ref_audio = storageReference.child("audio/" + UUID.randomUUID().toString()+".mp3");
        // adding listeners on upload
        // or failure of image

        System.out.println("-------ref_audio----------"+ref_audio);

        StorageMetadata metadata = new StorageMetadata.Builder().setContentType("audio/mp4").build();
        ref_audio.putFile(path,metadata).addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {

                        String SaveCurrenttime="";
                        try {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                            SaveCurrenttime = currentTime.format(calendar.getTime());
                        }catch (Exception e){ }

                        public_key = mDatabase.push().getKey();
                        Map<String, String> map = new HashMap<>();
                        map.put("message", "");
                        map.put("attachment_url", ref_audio+"");
                        map.put("mime_type", "audio/mp4");
                        map.put("msg_id", public_key);
                        map.put("status", "sent");
                        map.put("receiver", UserDetails.chatWith_id);
                        map.put("user", UserDetails.current_user_chat_id);
                        map.put("time", SaveCurrenttime);
                        notification = "true";
                        reference1.child(public_key).setValue(map);
                        reference2.child(public_key).setValue(map);
                        hiddenInputMethod();
                        // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast
                                .makeText(Chat_With_Other.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {

                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(
                                    UploadTask.TaskSnapshot taskSnapshot)
                            {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage(
                                        "Uploaded "
                                                + (int)progress + "%");
                            }
                        });

    }

    private void checkRunTimePermission() {
            Intent intent = new Intent();
            //sets the select file to all types of files
            intent.setType("image/*");
            //allows to select data and return it
            intent.setAction(Intent.ACTION_GET_CONTENT);
            //starts new activity to select file and return data
            startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_IMAGE_REQUEST);
    }


    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContentResolver(), filePath);
                uploadImage();

            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            // Defining the child of storageReference
            StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpeg").build();
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString()+".jpg");
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath,metadata).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                      {
                                          String SaveCurrenttime="";
                                          try {
                                              Calendar calendar = Calendar.getInstance();
                                              SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                                              SaveCurrenttime = currentTime.format(calendar.getTime());
                                          }catch (Exception e){ }

                                        public_key = mDatabase.push().getKey();
                                        Map<String, String> map = new HashMap<>();
                                        map.put("message", "");
                                        map.put("attachment_url", ref+"");
                                        map.put("mime_type", "image/jpeg");
                                        map.put("msg_id", public_key);
                                        map.put("status", "sent");
                                        map.put("time", SaveCurrenttime);
                                        map.put("receiver", UserDetails.chatWith_id);
                                        map.put("user", UserDetails.current_user_chat_id);
                                        notification = "true";
                                        reference1.child(public_key).setValue(map);
                                        reference2.child(public_key).setValue(map);
                                        hiddenInputMethod();
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(Chat_With_Other.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }


    private void method_to_call_reset_notification() {
        JSONObject put_method = new JSONObject();
        try{
            String[] user_id = UserDetails.chatWith_id.split("_");
            String[] sender_d = UserDetails.current_user_chat_id.split("_");

            put_method.put("reciever_id",sender_d[0]);
            put_method.put("sender_id",user_id[0]);
            put_method.put("event_id",get_auth_token("event_id"));
        }catch (Exception e){ }

       JsonObjectRequest Resetnotification = new JsonObjectRequest(Request.Method.PUT, Splash_screen.url+"notification_settings/reset_notification",put_method,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) { }
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);Resetnotification.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(Resetnotification, "tag");
    }


    private void go_to_push_notification(String massage,String mime_type) {

        JSONObject put_method = new JSONObject();

        String[] user_id = UserDetails.chatWith_id.split("_");
        String[] sender_d = UserDetails.current_user_chat_id.split("_");

        try {
            put_method.put("user_id",user_id[0]);
            put_method.put("sender_id",sender_d[0]);
            put_method.put("event_id",get_auth_token("event_id"));

            if (mime_type.equals("image/jpeg")){
                put_method.put("message","attachment file");
            }else  if (mime_type.equals("audio/mp4")){
                put_method.put("message","Voice Message");
            }else{
                put_method.put("message",massage+"");
            }

        }catch (Exception e){ }

        System.out.println("------userlist------"+put_method);
        myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"notification_settings/push_notification",put_method,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) { }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ProgressDialog.getInstance().hideProgress();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(Chat_With_Other.this);
                            }else {
                                ProgressDialog.getInstance().hideProgress();
                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(Chat_With_Other.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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


    private void load_current_date_time() {

        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMM dd");
        SimpleDateFormat simpledateformat_time = new SimpleDateFormat("HH:mm aa");

        String Date = simpledateformat.format(calander.getTime());

        String time = simpledateformat_time.format(calander.getTime());

        current_date.setText(Date);
        current_time.setText(time);

    }

    private void image_popup() {
        Dialog nagDialog = new Dialog(Chat_With_Other.this);
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        nagDialog.setCancelable(true);
        nagDialog.setCanceledOnTouchOutside(true);
        nagDialog.setContentView(R.layout.chat_popup_preview_image);

        ImageView ivPreview = nagDialog.findViewById(R.id.iv_preview_image);
        Picasso.with(Chat_With_Other.this)
                .load(UserDetails.user_progile).skipMemoryCache()
                .placeholder(R.drawable.profile_pic).error(R.drawable.profile_pic)         // optional
                .into(ivPreview);

        nagDialog.show();

    }

    public void hiddenInputMethod() {

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.back_button:
                finish();
               notification="false";
               reference1.removeEventListener(childEventListener);
               reference2.removeEventListener(childEventListener);
                break;

            case R.id.tv_send_item:
                method_to_call_textmessage();
                break;
        }
    }


    private void method_to_call_textmessage() {
        messageText = messageArea.getText().toString();
        if(!messageText.equals("")){
            String SaveCurrenttime="";
            try {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                SaveCurrenttime = currentTime.format(calendar.getTime());
            }catch (Exception e){ }

            // Creating new user node, which returns the unique key value
            // new user node would be /users/$userid/
            public_key = mDatabase.push().getKey();
            Map<String, String> map = new HashMap<>();
            map.put("message", messageText);
            map.put("msg_id", public_key);
            map.put("status", "sent");
            map.put("receiver", UserDetails.chatWith_id);
            map.put("user", UserDetails.current_user_chat_id);
            map.put("time", SaveCurrenttime);

            notification = "true";
            reference1.child(public_key).setValue(map);
            reference2.child(public_key).setValue(map);
            messageArea.setText("");
            hiddenInputMethod();
            tv_send_item.setVisibility(View.VISIBLE);
            send_text1.setVisibility(View.GONE);
//            recordView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Store its state
        state = recylerview_chat_data.getLayoutManager().onSaveInstanceState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Main position of RecyclerView when loaded again
        if (state != null) {
            recylerview_chat_data.getLayoutManager().onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerUtils.releaseMediaPlayer();
    }

    @Override
    public void onAudioUpdate(int currentPosition) {
        int playingAudioPosition = -1;
        for(int i = 0; i < audioStatusList.size(); i++) {
            AudioStatus audioStatus = audioStatusList.get(i);
            if(audioStatus.getAudioState() == AudioStatus.AUDIO_STATE.PLAYING.ordinal()) {
                playingAudioPosition = i;
                break;
            }
        }

        if(playingAudioPosition != -1) {
            AudioListAdapter.ViewHolder holder
                    = (AudioListAdapter.ViewHolder) recylerview_chat_data.findViewHolderForAdapterPosition(playingAudioPosition);
            if (holder != null) {
                holder.seekBarAudio.setProgress(currentPosition);
            }
        }
    }

    @Override
    public void onAudioComplete() {
        // Store its state
        state = recylerview_chat_data.getLayoutManager().onSaveInstanceState();

        audioStatusList.clear();
        for(int i = 0; i < chat_data.size(); i++) {
            audioStatusList.add(new AudioStatus(AudioStatus.AUDIO_STATE.IDLE.ordinal(), 0));
        }

        recylerview_chat_data.setAdapter(chatUserList);
        chatUserList.notifyDataSetChanged();

        // Main position of RecyclerView when loaded again
        if (state != null) {
            recylerview_chat_data.getLayoutManager().onRestoreInstanceState(state);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
//        notification="false";
//        reference1.removeEventListener(childEventListener);
//        reference2.removeEventListener(childEventListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        notification="false";
        reference1.removeEventListener(childEventListener);
        reference2.removeEventListener(childEventListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
