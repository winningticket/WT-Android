package com.winningticketproject.in.Player_stats;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class BarChartAdapter extends BaseAdapter {
    private final Activity context;
    private GridView list;
    HashMap<Integer,ArrayList<Bar>> hashMap = new HashMap<>();
    HashMap<Integer,ArrayList<bar>> hashMap1= new HashMap<>();

    public BarChartAdapter(Activity context, HashMap<Integer,ArrayList<Bar>> hashMap, HashMap<Integer,ArrayList<bar>> hashMap1) {
        this.context = context;
        this.hashMap = hashMap;
        this.hashMap1= hashMap1;
    }

    @Override
    public int getCount() {
        return hashMap.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();

        View view=inflater.inflate(R.layout.activity_bar_adapter,null,true);

        TextView puts_score = view.findViewById(R.id.puts_score);
        puts_score.setTypeface(medium);

        TextView putts_score_hint = view.findViewById(R.id.putts_score_hint);
        putts_score_hint.setTypeface(regular);

        TextView putts_dates = view.findViewById(R.id.putts_dates);
        putts_dates.setTypeface(regular);

        TextView puts_course_name = view.findViewById(R.id.puts_course_name);
        puts_course_name.setTypeface(medium);

        puts_score.setText(hashMap.get(position).get(position).Percentage);
        putts_dates.setText(hashMap.get(position).get(position).Date);
        puts_course_name.setText(hashMap.get(position).get(position).Cource_name);

        list=view.findViewById(R.id.grid_list_data);

        ArrayList<bar> barhash = hashMap1.get(position);

        TextView  empty_hole_information = view.findViewById(R.id.empty_hole_information);
        empty_hole_information.setTypeface(regular);

        if (barhash.size()>0){
            BarChartActivity.Puts_BarChartAdapter adapter1 =new BarChartActivity.Puts_BarChartAdapter(context,barhash);
            list.setAdapter(adapter1);
            Share_it.setGridViewHeightBasedOnChildren(list,6);

            empty_hole_information.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);

        }else {
            empty_hole_information.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }

        return view;
    }

}
