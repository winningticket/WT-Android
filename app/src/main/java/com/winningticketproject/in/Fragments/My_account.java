package com.winningticketproject.in.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.winningticketproject.RoundNotes.RoundNotesList;
import com.winningticketproject.in.AllUsersLogin.AllUserLogin;
import com.winningticketproject.in.MyAccountTab.About_Us;
import com.winningticketproject.in.MyAccountTab.Add_Funds;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.MyAccountTab.Change_Password;
import com.winningticketproject.in.MyAccountTab.Contact_us;
import com.winningticketproject.in.MyAccountTab.EditAccount_Details;
import com.winningticketproject.in.MyAccountTab.How_it_works;
import com.winningticketproject.in.IndividulaGameType.Individul_play_course_list;
import com.winningticketproject.in.EventTab.ListViewForEmbeddingInScrollView;
import com.winningticketproject.in.SignInSingup.Login_Screen;
import com.winningticketproject.in.MyAccountTab.Notification_Settings;
import com.winningticketproject.in.MyAccountTab.Profile_Image;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.MyAccountTab.SubcriptionInAppPurchase;
import com.winningticketproject.in.MyAccountTab.Transaction_History;
import com.winningticketproject.in.MyAccountTab.WIthdrawal;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.FilePath;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.Player_stats.graphExample;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.winningticketproject.in.AppInfo.Share_it.remove_key;
import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.light;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.decimal_amount;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class My_account extends Fragment implements View.OnClickListener {

    String auth_token = "";
    String handicap_value;
    String handicap_score;
    View view, dialogview;
    String pro_image;
    CircleImageView profile_image;
    ListViewForEmbeddingInScrollView firstlist;
    private static final int CAMERA_REQUEST = 1888;
    public static double wallete_balance;
    com.github.mikephil.charting.charts.BarChart chart;
    Activity activity;
    public static JSONObject fairwa_hit;
    private static final int PICK_FILE_REQUEST = 1;
    String average_score = "0", your_rounds = "0";
    String[] web = {
            "Individual Play Score",
            "Player Stats",
            "Round Notes",
            "Add Funds",
            "Withdrawal",
            "Transaction History",
            "Notification Settings",
            "How It Works",
            "About Us",
            "Contact Us",
            "Terms Of Use",
            "Privacy Policy",
            "Change Password",
            "Log Out",
    };

    String fileName = "";
    Uri selectedFileUri = null;
    File file;
    String currentPhotoPath;
    private String selectedFilePath;
    String file_from = "";

    String[] web_icons = {
            "\uf0a4",
            "\uf080",
            "\uf15c",
            "\uf09d",
            "\uf155",
            "\uf0d6",
            "\uf017",
            "\uf0f3",
            "\uf279",
            "\uf05a",
            "\uf075",
            "\uf0c5",
            "\uf084",
            "\uf08b",
    };


    boolean isPermitted;
    TextView username, fullusername, balance, current_balance, txt_handicap, txt_handicap_score, txt_average, txt_everage_score,add_profile_icon;
    TextView tv_top, tv_top_col1, tv_top_col2, tv_left, tv_right, tv_bottom, tv_shows_last_six;
    LinearLayout layout_edit_profile, handicap_layout;

    public static ArrayList<BarEntry> my_acc_puts_array_values = new ArrayList<>();
    public static ArrayList<Integer> my_acc_puts_max_values = new ArrayList<>();
    public static ArrayList<String> my_acc_putts_bottom_valkues = new ArrayList<>();
    public static ArrayList<Integer> my_aac_putts_colors = new ArrayList<>();

    Button btn_premium_purchase;
    int total;
    boolean calling = true;
    String user_type = "", premium_type = "", premium_end_date;
    android.app.ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_account, container, false);

        pd = new android.app.ProgressDialog(getContext());
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        username = view.findViewById(R.id.username);
        fullusername = view.findViewById(R.id.fullusername);
        balance = view.findViewById(R.id.balance);
        current_balance = view.findViewById(R.id.current_balance);

        txt_handicap = view.findViewById(R.id.txt_handicap);
        txt_handicap_score = view.findViewById(R.id.txt_handicap_score);

        txt_average = view.findViewById(R.id.txt_average);
        txt_everage_score = view.findViewById(R.id.txt_everage_score);

        profile_image = view.findViewById(R.id.profile_image);

        auth_token = get_auth_token("auth_token");

        TextView edit_profile = view.findViewById(R.id.edit_profile);
        edit_profile.setTypeface(regular);
        username.setTypeface(medium);
        fullusername.setTypeface(italic);
        balance.setTypeface(light);
        current_balance.setTypeface(light);

        tv_shows_last_six = view.findViewById(R.id.tv_shows_last_six);
        tv_shows_last_six.setTypeface(regular);

        txt_handicap.setTypeface(light);
        txt_handicap_score.setTypeface(light);

        txt_average.setTypeface(light);
        txt_everage_score.setTypeface(light);

        TextView bell_icon = view.findViewById(R.id.bel_icon);
        bell_icon.setText("\uf0f3");
        bell_icon.setTypeface(webfont);

        add_profile_icon = view.findViewById(R.id.add_profile_icon);
        add_profile_icon.setText("\uf055");
        add_profile_icon.setTypeface(webfont);
        add_profile_icon.setOnClickListener(this);

        TextView edit_icon = view.findViewById(R.id.edit_icon);
        edit_icon.setText("\uf040");
        edit_icon.setTypeface(webfont);


        tv_top = view.findViewById(R.id.tv_top);
        tv_top_col1 = view.findViewById(R.id.tv_top_col1);
        tv_top_col2 = view.findViewById(R.id.tv_top_col2);
        tv_left = view.findViewById(R.id.tv_left);
        tv_right = view.findViewById(R.id.tv_right);
        tv_bottom = view.findViewById(R.id.tv_bottom);

        tv_top.setTypeface(medium);
        tv_top_col1.setTypeface(regular);
        tv_top_col2.setTypeface(regular);
        tv_left.setTypeface(regular);
        tv_right.setTypeface(regular);
        tv_bottom.setTypeface(regular);

        firstlist = view.findViewById(R.id.firstlist);
        Firstlist allevents = new Firstlist(getActivity(), web, web_icons);
        firstlist.setAdapter(allevents);

        layout_edit_profile = view.findViewById(R.id.layout_edit_profile);
        layout_edit_profile.setOnClickListener(this);

        handicap_layout = view.findViewById(R.id.handicap_layout);
        handicap_layout.setOnClickListener(this);

        return view;
    }

    private void Barchart() {

        chart = view.findViewById(R.id.my_account_line_chart);
        chart.clear();
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(100);
        chart.setPinchZoom(false);

        YAxis RightAxis = chart.getAxisRight();

        RightAxis.setLabelCount(6, true);
        chart.getAxisLeft().setAxisMinimum(0);

        if (my_acc_puts_max_values.size() > 0) {
            chart.getAxisLeft().setAxisMaximum(Collections.max(my_acc_puts_max_values) + 5);
        } else {
            chart.getAxisLeft().setAxisMaximum(10);
        }

        RightAxis.setGranularity(1.0f);
        RightAxis.setGranularityEnabled(true); // Required to enable granularity
        RightAxis.setLabelCount(5);
        RightAxis.setEnabled(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(false);
        chart.getLegend().setEnabled(false);

        LimitLine ll1 = new LimitLine(Integer.parseInt(average_score), "");
        ll1.setLineWidth(1f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);

        XAxis xAxis = chart.getXAxis();
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return my_acc_putts_bottom_valkues.get((int) value);
            }

        });

        chart.getAxisLeft().setDrawGridLines(true);
        chart.animateY(2000);
        setData();
    }

    private void setData() {

        BarDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(my_acc_puts_array_values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(my_acc_puts_array_values, "");
            set1.setDrawIcons(false);
            set1.setColors(my_aac_putts_colors);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setBarWidth(0.2f); // set custom bar width
            chart.setData(data);
            data.setValueTextSize(0);
            chart.setFitBars(true); // make the x-axis fit exactly all bars
            chart.invalidate(); // refresh
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_edit_profile:
                startActivity(new Intent(getActivity(), EditAccount_Details.class));
                break;

            case R.id.handicap_layout:
                openpopuplog();
                break;

            case R.id.add_profile_icon:
                calling = false;
                checkRunTimePermission();
                break;

            case R.id.btn_premium_purchase:

                if (user_type.equals("free")) {
                    Intent in = new Intent(getContext(), SubcriptionInAppPurchase.class);
                    startActivity(in);
                }
                break;
        }
    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
            calling = false;
            showPictureDialog();
        }
//        else {
//            calling = false;
//            showPictureDialog();
//            // if already permition granted
//            // PUT YOUR ACTION (Like Open cemara etc..)
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }

                selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(getContext(), selectedFileUri);

                file = new File(selectedFilePath);

                file_from = "sd_card";

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    String[] parts = selectedFilePath.split("/");
                    fileName = parts[parts.length - 1];
                    method_to_upload_file();
                } else {
                    Toast.makeText(getContext(), "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }

            } else if (requestCode == CAMERA_REQUEST) {
                try {

                    file = new File(currentPhotoPath);
                    String[] parts = currentPhotoPath.split("/");
                    fileName = parts[parts.length - 1];
                    selectedFilePath = String.valueOf(file);
                    file_from = "camera";
                    method_to_upload_file();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }

    }


    private void method_to_upload_file() {
        if (!pd.isShowing()) {
            pd.show();
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String type = null;
                if (file_from.equals("camera")) {
                    type = "image/jpeg";
                } else {
                    String extension = MimeTypeMap.getFileExtensionFromUrl(selectedFilePath);
                    if (extension != null) {
                        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    }
                }

                OkHttpClient client = new OkHttpClient();

                RequestBody file_body = RequestBody.create(MediaType.parse(type), file);

                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type", type)
                        .addFormDataPart("profile_image", fileName, file_body)
                        .build();

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(Splash_screen.url + "users/profile_pic")
                        .post(request_body)
                        .addHeader("auth-token", auth_token)
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();

                    if (!response.isSuccessful()) {
                        calling = true;
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                    } else {
                        calling = true;
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                calling=true;
//                                Toast.makeText(getContext(),"profile uploaded successfully",Toast.LENGTH_LONG).show();
                                makeJsonObjectRequest(Splash_screen.url + "users/my_account");

                                //Your UI code here
                            }
                        });

                    }
                } catch (Exception e) {
                }
            }
        });
        t.start();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog;
        pictureDialog = new AlertDialog.Builder(getActivity());
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        choosePhotoFromGallary();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;
                }
            }
        });

        pictureDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openDialogOnce = true;
        if (requestCode == 11111) {

            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        //execute when 'never Ask Again' tick and permission dialog not show
                    } else {
                        if (openDialogOnce) {
                            alertView();
                        }
                    }
                }
            }

            if (isPermitted) {
                showPictureDialog();
            }
        }
    }


    private void alertView() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Permission Denied")
                .setInverseBackgroundForced(true)
                //.setIcon(R.drawable.ic_info_black_24dp)
                .setMessage("Without those permission the app is unable to save your profile. App needs to save profile image in your external storage and also need to get profile image from camera or external storage.Are you sure you want to deny this permission?")

                .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.dismiss();
                    }
                })
                .setPositiveButton("RE-TRY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.dismiss();
                        checkRunTimePermission();

                    }
                }).show();
    }

    public void choosePhotoFromGallary() {

        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("image/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);

    }


    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            selectedFileUri = FileProvider.getUriForFile(getContext(), "com.winningticketproject.in.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedFileUri);
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public void openpopuplog() {
        total = Integer.parseInt(handicap_score);
        final android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogview = inflater.inflate(R.layout.custome_alert_score_popup, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;

        String text = "<font color=#000000>WARNING: </font> <font color=#F9E083>" + "PROFESSIONAL" + "</font>";
        TextView warning_text = alertDialog.findViewById(R.id.warning_text);
        warning_text.setText(Html.fromHtml(text));
        warning_text.setTypeface(semibold);

        final LinearLayout ll_warning = alertDialog.findViewById(R.id.ll_warning);

        if (total > 0) {
            ll_warning.setVisibility(View.VISIBLE);
        } else {
            ll_warning.setVisibility(View.GONE);
        }

        final TextView title = alertDialog.findViewById(R.id.title);
        TextView minus_symbol = alertDialog.findViewById(R.id.minus_symbol);
        final TextView result = alertDialog.findViewById(R.id.result);
        TextView plus_symbol = alertDialog.findViewById(R.id.plus_symbol);
        final TextView message = alertDialog.findViewById(R.id.message);
        Button continue_button = alertDialog.findViewById(R.id.continue_button);


        title.setTypeface(medium);
        result.setTypeface(medium);
        message.setTypeface(italic);
        continue_button.setTypeface(medium);

        if (total == 0) {
            message.setText("Scratch golfer. You will not have any strokes taken off your gross score.");
        } else if (total == -1) {
            message.setText("You will have a stroke TAKEN OFF the hardest hole on the golf course.");
        } else if (total < -1) {
            message.setText("You will have a stroke TAKEN OFF the " + String.valueOf(total).replace("-", "") + " hardest holes on the golf course.");
        } else if (total == 1) {
            message.setText("You will have a stroke ADDED to the easiest hole on the golf course.");
        } else if (total > 1) {
            message.setText("You will have a stroke ADDED to the " + String.valueOf(total).replace("-", "") + " easiest holes on the golf course.");
        }

        minus_symbol.setTypeface(regular);


//        plus_symbol.setText("\uf055");
        plus_symbol.setTypeface(regular);

        result.setText(handicap_score + "");


        plus_symbol.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (total != 36) {
                    String plus = String.valueOf(++total);
                    result.setText(plus);
                    if (total == 0) {
                        message.setText("Scratch golfer. You will not have any strokes taken off your gross score.");
                    } else if (total == -1) {
                        message.setText("You will have a stroke TAKEN OFF the hardest hole on the golf course.");
                    } else if (total < -1) {
                        message.setText("You will have a stroke TAKEN OFF the " + String.valueOf(total).replace("-", "") + " hardest holes on the golf course.");
                    } else if (total == 1) {
                        message.setText("You will have a stroke ADDED to the easiest hole on the golf course.");
                    } else if (total > 1) {
                        message.setText("You will have a stroke ADDED to the " + String.valueOf(total).replace("-", "") + " easiest holes on the golf course.");
                    }
                }
                if (total > 0) {
                    ll_warning.setVisibility(View.VISIBLE);
                } else {
                    ll_warning.setVisibility(View.GONE);
                }

                return false;
            }
        });

        minus_symbol.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (total != -36) {
                    String minus = String.valueOf(--total);
                    result.setText(minus);
                    if (total == 0) {
                        message.setText("Scratch golfer. You will not have any strokes taken off your gross score.");
                    } else if (total == -1) {
                        message.setText("You will have a stroke TAKEN OFF the hardest hole on the golf course.");
                    } else if (total < -1) {
                        message.setText("You will have a stroke TAKEN OFF the " + String.valueOf(total).replace("-", "") + " hardest holes on the golf course.");
                    } else if (total == 1) {
                        message.setText("You will have a stroke ADDED to the easiest hole on the golf course.");
                    } else if (total > 1) {
                        message.setText("You will have a stroke ADDED to the " + total + " easiest holes on the golf course.");
                    }
                }
                if (total > 0) {
                    ll_warning.setVisibility(View.VISIBLE);
                } else {
                    ll_warning.setVisibility(View.GONE);
                }
                return false;
            }
        });


        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handicap_value = result.getText().toString();
                Share_it.savestring("handicapscore", "2");
                Share_it.savestring("first_time", "no");
                Share_it.savestring("handicap_score", handicap_value);
                Share_it.savestring("row_three_postion", "null");

                if (!isNetworkAvailable()) {
                    alertdailogbox("scoreboardapi");
                } else {
                    methodforscorecard();
                }
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    alertDialog.dismiss();
                }
            }
        });

    }

    public void methodforscorecard() {
        try {
            JSONObject params = new JSONObject();
            params.put("handicap", handicap_value);
            JsonObjectRequest myRequest_handicap = new JsonObjectRequest(
                    Request.Method.POST, Splash_screen.url + "users/update_handicap", params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println("-----res-----" + response);
                                if (response.getString("status").equals("Success")) {
                                    if (!isNetworkAvailable()) {
                                        alertdailogbox("update_score");
                                    } else {
                                        makeJsonObjectRequest(Splash_screen.url + "users/my_account");
                                    }
                                } else {
                                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                //nothing
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                // HTTP Status Code: 401 Unauthorized
                                if (error.networkResponse.statusCode == 401) {
                                    Alert_Dailog.showNetworkAlert(getActivity());
                                } else {
                                    Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
            myRequest_handicap.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(myRequest_handicap, "tag");

        } catch (Exception e) {
            //nothing
        }
    }

    public class Firstlist extends BaseAdapter {

        private final String[] web;
        private final String[] imageId;

        public LayoutInflater inflater;

        public Firstlist(Activity context, String[] web, String[] imageId) {
            super();
            this.web = web;
            this.imageId = imageId;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return web.length;
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

        public class ViewHolder {
            TextView text, icons, all_icons;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.custome_myacount, null);


                holder.text = convertView.findViewById(R.id.textview);
                holder.icons = convertView.findViewById(R.id.icon);
                holder.all_icons = convertView.findViewById(R.id.all_icons);


                convertView.setTag(holder);
            } else


                holder = (ViewHolder) convertView.getTag();


            holder.text.setText(web[position]);
//            holder.icons.setImageResource(imageId[position]);
            holder.icons.setText("\uf105");
            holder.icons.setTypeface(webfont);

            holder.all_icons.setText(imageId[position]);
            holder.all_icons.setTypeface(webfont);

            holder.text.setTypeface(light);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (position == 0) {

                        if (user_type.equals("free")) {
                            Subscribe_popu("Please subscribe for premium to see the Individual play course score");
                        } else {
                            startActivity(new Intent(getActivity(), Individul_play_course_list.class));
                        }
                        calling = true;

                    } else if (position == 1) {

                        if (user_type.equals("free")) {
                            Subscribe_popu("Please subscribe for premium to see the Player stats");
                        } else {
                            startActivity(new Intent(getActivity(), graphExample.class));
                        }
                        calling = true;

                    } else if (position == 2) {
                        if (user_type.equals("free")) {
                            Subscribe_popu("Please subscribe for premium to see the Round notes");
                        } else {
                            startActivity(new Intent(getActivity(), RoundNotesList.class));
                        }
                        calling = false;
                    } else if (position == 3) {
                        startActivity(new Intent(getActivity(), Add_Funds.class));
                        calling = true;
//                    }else if (position==3){
//                        startActivity(new Intent(getActivity(),Donate_Funds.class));
//                            calling=true;
                    } else if (position == 4) {
                        Intent ii = new Intent(getActivity(), WIthdrawal.class);
                        ii.putExtra("balance", balance.getText().toString());
                        startActivity(ii);
                        calling = true;
                    } else if (position == 5) {
                        startActivity(new Intent(getActivity(), Transaction_History.class));
                        calling = false;
                    } else if (position == 6) {
                        calling = false;
                        startActivity(new Intent(getActivity(), Notification_Settings.class));
                    } else if (position == 7) {
                        calling = false;
                        startActivity(new Intent(getActivity(), How_it_works.class));
                    } else if (position == 8) {
                        calling = false;
                        Intent in = new Intent(getActivity(), About_Us.class);
                        in.putExtra("page_type", "about_us");
                        startActivity(in);
                    } else if (position == 9) {
                        calling = false;
                        startActivity(new Intent(getActivity(), Contact_us.class));
                    } else if (position == 10) {
                        calling = false;
                        Intent in = new Intent(getActivity(), About_Us.class);
                        in.putExtra("page_type", "terms_of_use");
                        startActivity(in);
                    } else if (position == 11) {
                        calling = false;
                        Intent in = new Intent(getActivity(), About_Us.class);
                        in.putExtra("page_type", "privacy_policy");
                        startActivity(in);
                    } else if (position == 12) {
                        calling = false;
                        startActivity(new Intent(getActivity(), Change_Password.class));

                    } else if (position == 13) {

                        calling = false;

                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setCancelable(false);
                        builder.setTitle("Logout");
                        builder.setMessage("Are you sure you want to logout?");
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                remove_key("auth_token");
                                remove_key("role");
                                remove_key("saveLogin");
                                remove_key("checked");
                                remove_key("user_player_type");
                                remove_key("player_type");
                                remove_key("cross_score_popup");
                                remove_key("CGB_event_code");
                                remove_key("current_event_code");

                                Toast.makeText(getActivity(), "Successfully Logout", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), AllUserLogin.class));
                                getActivity().finish();
                            }
                        });

                        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
                        dialog.show();

                    }
                }
            });

            return convertView;
        }
    }

    private void Subscribe_popu(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("Purchase Premium");
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    public void makeJsonObjectRequest(String url) {
        btn_premium_purchase = view.findViewById(R.id.btn_premium_purchase);
        btn_premium_purchase.setTypeface(medium);
        btn_premium_purchase.setOnClickListener(this);
        if (!pd.isShowing()) {
            pd.show();
        }
        final JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println(pro_image + "----response-----" + response);

                            final JSONObject job = response.getJSONObject("user");
                            pro_image = job.getString("avatar_url");


                            user_type = job.getString("user_type");
                            premium_type = job.getString("premium_type");
                            premium_end_date = job.getString("premium_end_date");
                            //user type checking for user is purchased or not

                            btn_premium_purchase.setVisibility(View.VISIBLE);
                            if (user_type.equals("free")) {
                                savestring("user_type", user_type);
                            } else {
                                btn_premium_purchase.setBackgroundColor(getResources().getColor(R.color.chosen_color));
                                String html = "<b>Premium Memebership Active</b> <br> <small>Explore your benefits and membership details</small>";
                                btn_premium_purchase.setText(Html.fromHtml(html));
                            }

                            handicap_score = response.getString("handicap");

                            if (handicap_score == null || handicap_score.equals(null) || handicap_score.equals("null") || handicap_score.equals("")) {
                                handicap_score = "0";
                            }

                            savestring("handicap_score", handicap_score);
                            savestring("handicap",handicap_score);

                            if (Integer.parseInt(handicap_score) > 0) {
                                txt_handicap_score.setText("+" + handicap_score);
                            } else if (Integer.parseInt(handicap_score) == 0) {
                                txt_handicap_score.setText(handicap_score);
                            } else {
                                txt_handicap_score.setText(handicap_score);
                            }

                            try {
                                savestring("user_profile_logo", pro_image);
                                pro_image = job.getString("avatar_url");
                                if (pro_image != null) {
                                    Glide.with(My_account.this)
                                            .load(pro_image).skipMemoryCache(false)
                                            .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                                            .into(profile_image);
                                }

                                profile_image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        calling = false;
                                        Intent intent = new Intent(getActivity(), Profile_Image.class);
                                        intent.putExtra("avatar_url", pro_image);
                                        startActivity(intent);
                                    }
                                });

                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }//                                Barchart();
                            } catch (Exception e) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }                                //nothing
                            }

                            if (pd.isShowing()) {
                                pd.dismiss();
                            }

                            String names = job.getString("first_name").replaceAll("\\s+", " ");
                            try {
                                names = names.substring(0, 1).toUpperCase() + names.substring(1);
                            } catch (Exception e) {

                            }
                            String lnames = job.getString("last_name").replaceAll("\\s+", " ");
                            try {
                                lnames = lnames.substring(0, 1).toUpperCase() + lnames.substring(1);
                            } catch (Exception e) {

                            }
                            username.setText(names.trim() + " " + lnames);
                            savestring("user_info",names.trim() + " " + lnames);

                            fullusername.setText("");
                            fullusername.setText(job.getString("email"));

                            try {
                                wallete_balance = response.getDouble("wallet");
                                balance.setText("$" + decimal_amount(wallete_balance));
                            } catch (Exception e) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }                                //nothing
                            }
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        } catch (JSONException e) {
                            //nothing
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (pd.isShowing()) {
                            pd.dismiss();
                        }

                        NetworkResponse networkResponse = error.networkResponse;

                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(getActivity());
                            } else {
                                Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
        myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            btn_premium_purchase = view.findViewById(R.id.btn_premium_purchase);
            btn_premium_purchase.setTypeface(medium);
            btn_premium_purchase.setOnClickListener(this);
            if (calling) {
                if (!isNetworkAvailable()) {
                    alertdailogbox("update_score");
                } else {
                    new DeleteImagesTask().execute();
                    makeJsonObjectRequest(Splash_screen.url + "users/my_account");
                }
            }
        } catch (Exception e) {
            //nothing
        }
    }


    private class DeleteImagesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // this might take a while ...
//            Load_Graph_Section_data();
            load_all_graph_data_ate_vise();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }



    public void alertdailogbox(final String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("No Internet");
        builder.setMessage("Internet is required. Please Retry.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


                if (value.equals("scoreboardapi")) {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("scoreboardapi");
                    } else {
                        methodforscorecard();
                    }
                } else if (value.equals("update_score")) {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("update_score");
                    } else {
                        makeJsonObjectRequest(Splash_screen.url + "users/my_account");
                    }

                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }


    private void load_all_graph_data_ate_vise() {
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url + "users/score_card/view_all_scores", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        my_acc_puts_array_values.clear();
                        my_acc_putts_bottom_valkues.clear();
                        my_acc_puts_max_values.clear();
                        my_aac_putts_colors.clear();
                        try {
                            System.out.println("-------score card -----" + response);

                            try {

                                your_rounds = response.getString("your_rounds");
                                if (your_rounds == null || your_rounds.equals("null") || your_rounds.equals("")) {
                                    your_rounds = "0";
                                }
                                txt_everage_score.setText(your_rounds);

                            } catch (Exception e) {
                                txt_everage_score.setText("0");

                            }

                            average_score = response.getString("average_score");

                            if (average_score == null || average_score.equals(null) || average_score.equals("null") || average_score.equals("")) {
                                average_score = "0";
                            }

                            JSONArray array = response.getJSONArray("score_card_details");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject score_board_obj = array.getJSONObject(i);

                                String date = score_board_obj.getString("score_date_mod");
                                String score_values = score_board_obj.getString("total_net_score");
                                if (score_values.equals(null) || score_values.equals("") || score_values.equals("null") || score_values == null) {
                                    score_values = "0";
                                }
                                int values = Integer.parseInt(score_values);
                                my_acc_puts_array_values.add(new BarEntry(i, values, getResources()));
                                my_acc_putts_bottom_valkues.add(date);
                                my_acc_puts_max_values.add(Integer.parseInt(score_values));

                                if (Integer.parseInt(score_values) > Integer.parseInt(average_score)) {
                                    my_aac_putts_colors.add(Color.parseColor("#F46263"));
                                } else {
                                    my_aac_putts_colors.add(Color.parseColor("#00A7DC"));
                                }
                            }

                            Barchart();

                        } catch (Exception e) {
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


}
