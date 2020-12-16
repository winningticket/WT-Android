package com.winningticketproject.in.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.EventTab.Score_board_new;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.AppInfo.Your_round_data;
import com.winningticketproject.in.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.winningticketproject.in.IndividulaGameType.Location_Services.selected_handicap;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.selected_hole;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.selected_tee;
import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class Your_Round extends BaseAdapter {

    public Activity context;
    public LayoutInflater inflater;
    List<Your_round_data> course_list;

    public Your_Round(Activity context, List<Your_round_data> course_list){
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
        CircleImageView courses_image;
        TextView text_course_name,text_course_address,text_course_distance,text_last_played,text_tee_hole;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custome_all_rounded, null);

            holder.text_course_name = convertView.findViewById(R.id.text_course_name);
            holder.courses_image = convertView.findViewById(R.id.courses_image);
            holder.text_course_address = convertView.findViewById(R.id.text_course_address);

            holder.text_last_played = convertView.findViewById(R.id.text_last_played);
            holder.text_tee_hole = convertView.findViewById(R.id.text_tee_hole);

            holder.text_course_name.setTypeface(semibold);
            holder.text_course_address.setTypeface(medium);
            holder.text_course_distance = convertView.findViewById(R.id.text_course_distance);
            holder.text_course_distance.setTypeface(semibold);
            holder.text_last_played.setTypeface(italic);
            holder.text_tee_hole.setTypeface(italic);

            convertView.setTag(holder);
        } else

            holder=(ViewHolder)convertView.getTag();

        holder.text_course_name.setText(course_list.get(position).getR_course_name());
        holder.text_course_address.setText(course_list.get(position).getR_address());

        System.out.println("------------"+course_list.get(position).getR_image());

        Picasso.with(context)
                .load(course_list.get(position).getR_image()).placeholder(R.drawable.courses_empty).error(R.drawable.courses_empty)
                .into(holder.courses_image);

        holder.text_last_played.setText("Last Played "+course_list.get(position).getR_last_played());

        holder.text_course_distance.setText(" "+course_list.get(position).getR_km()+" MILES");

        holder.text_tee_hole.setText("Tee: " + course_list.get(position).getR_tee()+"  Starting hole: "+course_list.get(position).getR_starting_hole() +"  Current Score: "+course_list.get(position).getR_current_score());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_to_show_countinue_poup(position);
            }
        });




        return convertView;

    }

    private void method_to_show_countinue_poup(final int position) {

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.CustomDialogTheme);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogview = inflater.inflate(R.layout.play_round_popup, null);
            final android.support.v7.app.AlertDialog popupDia = builder.create();
            popupDia.setView(dialogview, 0, 0, 0, 0);
            popupDia.setCanceledOnTouchOutside(false);
            popupDia.setCancelable(false);
            popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            popupDia.show();
            final android.support.v7.app.AlertDialog alertDialog = popupDia;
            TextView location_services = alertDialog.findViewById(R.id.location_services);
            TextView nearby_courses = alertDialog.findViewById(R.id.st_round_nearby_courses);

            nearby_courses.setVisibility(View.GONE);

            location_services.setText("Continue your round");
            location_services.setTypeface(semibold);

            Button enable = alertDialog.findViewById(R.id.enable);
            enable.setText("Continue");
            Button not_now = alertDialog.findViewById(R.id.not_now);
            enable.setTypeface(medium);
            not_now.setTypeface(medium);

            enable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    Share_it.savestring("play_type", "paid");
                    Share_it.savestring("event_id", course_list.get(position).getR_id());
                    Share_it.savestring("first_time", "no");
                    Share_it.savestring("Event_name", course_list.get(position).getR_course_name());
                    Share_it.savestring("Game_type","Course");
                    Location_Services.game_name="Course";
                    selected_tee = course_list.get(position).getR_tee();
                    selected_hole = course_list.get(position).getR_starting_hole();
                    selected_handicap = course_list.get(position).getR_handicap_score();

                    Intent intent1 = new Intent(context, Score_board_new.class);
                    Share_it.savestring("selected_hole_postion",selected_hole);
                    Share_it.savestring("handicap_score",selected_handicap);
                    context.startActivity(intent1);
                }
            });

            not_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
    }
}
