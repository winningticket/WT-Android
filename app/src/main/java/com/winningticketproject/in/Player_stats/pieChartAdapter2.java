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

public class pieChartAdapter2 extends BaseAdapter {

    private final Activity context;

    ArrayList<fairway_getter_setter> fair_way_ArrayList_data = new ArrayList<>();

    TextView regular_hit_data,regular_date,regular_event_name,regular_miss;
    TextView regular_hit_hint,regular_miss_hint;

    public pieChartAdapter2(Activity context, ArrayList<fairway_getter_setter> pie) {
        this.context = context;
        this.fair_way_ArrayList_data =pie;
    }

    @Override
    public int getCount() {
        return fair_way_ArrayList_data.size();
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
        View rowView=inflater.inflate(R.layout.activity_pie_adapter2, null,true);

        regular_hit_data = rowView.findViewById(R.id.regular_hit_data);
        regular_hit_data.setTypeface(medium);

        regular_hit_hint = rowView.findViewById(R.id.regular_hit_hint);
        regular_hit_hint.setTypeface(regular);

        regular_date = rowView.findViewById(R.id.regular_date);
        regular_date.setTypeface(regular);

        regular_event_name = rowView.findViewById(R.id.regular_event_name);
        regular_event_name.setTypeface(medium);

        regular_miss = rowView.findViewById(R.id.regular_miss);
        regular_miss.setTypeface(medium);

        regular_miss_hint = rowView.findViewById(R.id.regular_miss_hint);
        regular_miss_hint.setTypeface(regular);


        fairway_getter_setter fireway = fair_way_ArrayList_data.get(position);
        regular_hit_data.setText(fireway.left_per+"%");
        regular_date.setText(fireway.score_date_details);
        regular_event_name.setText(fireway.event_name);
        regular_miss.setText(fireway.right_per+"%");

        return rowView;

    }


}
