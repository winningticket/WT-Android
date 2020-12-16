package com.winningticketproject.in.CourseTab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.winningticketproject.in.AppInfo.Course_Data;
import com.winningticketproject.in.R;

import java.util.List;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class Course_Search_adapter extends RecyclerView.Adapter<Course_Search_adapter.MyViewHolder> {

    public Activity context;
    public List<Course_Data> course_list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image_loca_icon;
        TextView tv_course_name,tv_course_location_name,tv_course_distance_miles,tv_course_type;
        public MyViewHolder(View view) {
            super(view);
            tv_course_name = view.findViewById(R.id.tv_course_name);
            tv_course_location_name = view.findViewById(R.id.tv_course_location_name);

            tv_course_name.setTypeface(semibold);
            tv_course_location_name.setTypeface(regular);

            tv_course_distance_miles = view.findViewById(R.id.tv_course_distance_miles);
            tv_course_distance_miles.setTypeface(medium);

            tv_course_type = view.findViewById(R.id.tv_course_type);
            tv_course_type.setTypeface(medium);

            image_loca_icon = view.findViewById(R.id.image_loca_icon);

        }
    }

    public Course_Search_adapter(Activity context, List<Course_Data> course_list) {
        super();
        this.context = context;
        this.course_list = course_list;
    }

    @Override
    public Course_Search_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_adapter, parent, false);

        return new Course_Search_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Course_Search_adapter.MyViewHolder holder, final int position) {
        holder.tv_course_name.setText(course_list.get(position).getNames());
        holder.tv_course_location_name.setText(course_list.get(position).getAddres());

        holder.tv_course_distance_miles.setText(course_list.get(position).getDistance_mils() + " miles");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(context, Course_Detail_Page.class);
                ii.putExtra("id", course_list.get(position).getId());
                ii.putExtra("distance", course_list.get(position).getDistance_mils());
                ii.putExtra("city_state", course_list.get(position).getAddres());
                context.startActivity(ii);
            }
        });

        if(course_list.get(position).getType().equals("private")){
            holder.tv_course_type.setText("Private");
            holder.tv_course_type.setTextColor(Color.parseColor("#FD2726"));
            holder.image_loca_icon.setVisibility(View.VISIBLE);
        }else  if(course_list.get(position).getType().equals("public")){
            holder.tv_course_type.setText("Public");
            holder.image_loca_icon.setVisibility(View.GONE);
            holder.tv_course_type.setTextColor(Color.parseColor("#00AC2C"));
        }else if (course_list.get(position).getType().equals("semi-private")){
            holder.tv_course_type.setText("Semi-private");
            holder.image_loca_icon.setVisibility(View.VISIBLE);
            holder.tv_course_type.setTextColor(Color.parseColor("#FD2726"));
        }

    }

    @Override
    public int getItemCount() {
        return course_list.size();
    }
}