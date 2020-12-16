package com.winningticketproject.in.AffiliatorModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.winningticketproject.in.R;

public class Bottom_Tabs_For_afffiliate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__tabs__for_afffiliate);

        startActivity(new Intent(getApplicationContext(),Afiliate_Referal_List.class));


    }
}
