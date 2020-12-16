package com.winningticketproject.in.Player_stats;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winningticketproject.in.R;

import java.util.ArrayList;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class pieChartAdapter3 extends BaseAdapter {

    private final Activity context;
    public ArrayList<scoring_getter_setter> pieArrayList = new ArrayList<>();
    public pieChartAdapter3(Activity context, ArrayList<scoring_getter_setter> pie) {
        this.context = context;
        this.pieArrayList =pie;
    }

    @Override
    public int getCount() {
        return pieArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {


        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_pie_adapter3, null,true);

        scoring_getter_setter pie = pieArrayList.get(position);

        TextView score_scoring_hint = rowView.findViewById(R.id.score_scoring_hint);
        score_scoring_hint.setTypeface(regular);


        TextView scoring_hits_score = rowView.findViewById(R.id.scoring_hits_score);
        scoring_hits_score.setText(pie.Event_score+"%");
        scoring_hits_score.setTypeface(medium);

        TextView  scoring_day_date = rowView.findViewById(R.id.scoring_day_date);
        scoring_day_date.setText(pie.Event_date);
        scoring_day_date.setTypeface(regular);

        TextView  scoring_corse_name = rowView.findViewById(R.id.scoring_corse_name);
        scoring_corse_name.setText(pie.Event_name);
        scoring_corse_name.setTypeface(medium);


        TextView  scoring_eagle = rowView.findViewById(R.id.scoring_eagle);
        scoring_eagle.setText(pie.Event_eagle+"%");
        scoring_eagle.setTypeface(regular);

        TextView  scoring_bride = rowView.findViewById(R.id.scoring_bride);
        scoring_bride.setText(pie.Event_bride+"%");
        scoring_bride.setTypeface(regular);


        TextView  scoring_par = rowView.findViewById(R.id.scoring_par);
        scoring_par.setText(pie.Event_par+"%");
        scoring_par.setTypeface(regular);


        TextView  scoring_bogey = rowView.findViewById(R.id.scoring_bogey);
        scoring_bogey.setText(pie.Event_bogey+"%");
        scoring_bogey.setTypeface(regular);

        TextView  scoring_double_bogey = rowView.findViewById(R.id.scoring_bouble_bogey);
        scoring_double_bogey.setText(pie.Event_double_bogey+"%");
        scoring_double_bogey.setTypeface(regular);

        return rowView;

    }


}
