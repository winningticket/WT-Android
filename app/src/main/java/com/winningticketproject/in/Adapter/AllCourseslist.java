package com.winningticketproject.in.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winningticketproject.in.CourseTab.Course_Detail_Page;
import com.winningticketproject.in.AppInfo.Course_Data;
import com.winningticketproject.in.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.mediumitalic;

public class AllCourseslist extends BaseAdapter {

    public Activity context;
    public LayoutInflater inflater;
    public List<Course_Data> course_list;

    public AllCourseslist(Activity context,List<Course_Data> course_list){
        super();
        this.context = context;
        this.course_list = course_list;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return course_list.size();
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
        TextView tv_course_name,tv_course_location_name,tv_course_distance_miles;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.course_details_course_name, null);

            holder.tv_course_name = convertView.findViewById(R.id.tv_course_name);
            holder.tv_course_location_name = convertView.findViewById(R.id.tv_course_location_name);

            holder.tv_course_distance_miles = convertView.findViewById(R.id.tv_course_distance_miles);
            holder.tv_course_distance_miles.setTypeface(medium);

            holder.tv_course_name.setTypeface(medium);
            holder.tv_course_location_name.setTypeface(mediumitalic);

            convertView.setTag(holder);
        } else

            holder=(ViewHolder)convertView.getTag();

        holder.tv_course_name.setText(course_list.get(position).getNames());
        holder.tv_course_location_name.setText(course_list.get(position).getAddres());

        holder.tv_course_distance_miles.setText(course_list.get(position).getDistance_mils()+" Miles");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(context, Course_Detail_Page.class);
                ii.putExtra("id", course_list.get(position).getId());
                ii.putExtra("distance",course_list.get(position).getDistance_mils());
                ii.putExtra("city_state",course_list.get(position).getAddres());
                context.startActivity(ii);
            }
        });

        return convertView;

    }
}
