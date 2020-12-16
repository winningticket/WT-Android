package com.winningticketproject.in.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winningticketproject.in.CourseTab.Course_Detail_Page;
import com.winningticketproject.in.AppInfo.Course_Data;
import com.winningticketproject.in.R;

import java.util.List;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.mediumitalic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class Allcourse_horizontal extends RecyclerView.Adapter<Allcourse_horizontal.MyViewHolder> {

    public Activity context;
    public List<Course_Data> course_list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_course_name,tv_course_location_name,tv_course_distance_miles;
        public MyViewHolder(View view) {
            super(view);
            tv_course_name = view.findViewById(R.id.tv_course_name);
            tv_course_location_name = view.findViewById(R.id.tv_course_location_name);

            tv_course_name.setTypeface(medium);
            tv_course_location_name.setTypeface(regular);

            tv_course_distance_miles = view.findViewById(R.id.tv_course_distance_miles);
            tv_course_distance_miles.setTypeface(regular);

        }
    }

    public Allcourse_horizontal(Activity context,List<Course_Data> course_list){
        super();
        this.context = context;
        this.course_list = course_list;
    }

    @Override
    public Allcourse_horizontal.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_details_course_name, parent, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;

        int item_width = deviceWidth - (deviceWidth/100 * 10);

        itemView.setMinimumWidth(item_width);

        return new Allcourse_horizontal.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Allcourse_horizontal.MyViewHolder holder, final int position) {
        holder.tv_course_name.setText(course_list.get(position).getNames());
        holder.tv_course_location_name.setText(course_list.get(position).getAddres());

        holder.tv_course_distance_miles.setText(course_list.get(position).getDistance_mils()+" miles");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(context, Course_Detail_Page.class);
                ii.putExtra("id", course_list.get(position).getId());
                ii.putExtra("distance",course_list.get(position).getDistance_mils());
                ii.putExtra("city_state",course_list.get(position).getAddres());
                context.startActivity(ii);
            }
        });
    }

    @Override
    public int getItemCount() {
        return course_list.size();
    }

}
