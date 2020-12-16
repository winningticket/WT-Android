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

public class pieChartAdapter1 extends BaseAdapter {
    private final Activity context;
    ArrayList<fairway_getter_setter> pieArrayList = new ArrayList<>();
    TextView fair_way_event_date,fair_way_event_name;
    TextView fair_way_hit_hint,fair_way_miss_left_hint,firway_right_hint;
    TextView fair_hit_data,fair_way_miss_left,fair_ways_right;

    public pieChartAdapter1(Activity context, ArrayList<fairway_getter_setter> pie) {
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
        View rowView=inflater.inflate(R.layout.activity_pie_adapter1, null,true);

        fair_way_event_date = rowView.findViewById(R.id.fair_way_event_date);
        fair_way_event_name = rowView.findViewById(R.id.fair_way_event_name);

        fair_way_event_date.setTypeface(regular);
        fair_way_event_name.setTypeface(medium);

        fair_way_hit_hint = rowView.findViewById(R.id.fair_way_hit_hint);
        fair_way_hit_hint.setTypeface(regular);
        fair_way_miss_left_hint = rowView.findViewById(R.id.fair_way_miss_left_hint);
        fair_way_miss_left_hint.setTypeface(regular);
        firway_right_hint = rowView.findViewById(R.id.firway_right_hint);
        firway_right_hint.setTypeface(regular);

        fairway_getter_setter fireway = pieArrayList.get(position);
        fair_way_event_date.setText(fireway.getScore_date_details());
        fair_way_event_name.setText(fireway.getEvent_name());


        fair_hit_data = rowView.findViewById(R.id.fair_hit_data);
        fair_hit_data.setTypeface(medium);
        fair_hit_data.setText(fireway.center_per+"%");
        fair_way_miss_left = rowView.findViewById(R.id.fair_way_miss_left);
        fair_way_miss_left.setTypeface(medium);
        fair_way_miss_left.setText(fireway.left_per+"%");
        fair_ways_right = rowView.findViewById(R.id.fair_ways_right);
        fair_ways_right.setTypeface(medium);
        fair_ways_right.setText(fireway.right_per+"%");

        return rowView;

    }


}
