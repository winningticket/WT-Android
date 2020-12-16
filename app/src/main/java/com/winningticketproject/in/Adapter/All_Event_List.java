package com.winningticketproject.in.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.winningticketproject.in.AppInfo.AllEventGetterSetter;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.internal.zzail.runOnUiThread;
import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class All_Event_List extends BaseAdapter {

     Context context;
     LayoutInflater inflater;
     int push_notification_count = 0;
    List<AllEventGetterSetter> viewallevent = new ArrayList<>();
    public All_Event_List(Context context,List<AllEventGetterSetter> viewallevent) {
        super();
        this.context = context;
        this.viewallevent = viewallevent;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return viewallevent.size();
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
        TextView evnt_name, event_date,event_status ,text_chart_count;
        ImageView event_img;
        LinearLayout chart_count;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final All_Event_List.ViewHolder holder;
        if (convertView == null) {


            holder = new All_Event_List.ViewHolder();
            convertView = inflater.inflate(R.layout.custome_event_list, null);

            holder.evnt_name = convertView.findViewById(R.id.evnt_name);
            holder.event_date = convertView.findViewById(R.id.event_date);
            holder.event_img = convertView.findViewById(R.id.event_logo);

            holder.chart_count = convertView.findViewById(R.id.chart_count);
            holder.text_chart_count = convertView.findViewById(R.id.text_chart_count);

            holder.event_status = convertView.findViewById(R.id.event_status);
            holder.event_status.setTypeface(italic);

            holder.evnt_name.setTypeface(regular);
            holder.event_date.setTypeface(regular);
            convertView.setTag(holder);
        } else

            holder = (All_Event_List.ViewHolder) convertView.getTag();

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#fafafa"));
        }

        String names="";
        try{
            names=viewallevent.get(position).getName().substring(0,1).toUpperCase() +viewallevent.get(position).getName().substring(1);
        }
        catch (Exception e)
        {

        }
        Picasso.with(context)
                .load(viewallevent.get(position).getEvent_logo()).placeholder(R.drawable.auction_event_empty).error(R.drawable.auction_event_empty)
                .into(holder.event_img);

        holder.evnt_name.setText(names.trim());

        holder.event_date.setText(Html.fromHtml(viewallevent.get(position).getStart_date().replace("\n", "<br>")));
        if (viewallevent.get(position).getEvent_type().equals("upcoming_or_live")){
            if (viewallevent.get(position).getEvent_status().equals("Live")) {
                holder.event_status.setText("\u2022 Live");
                holder.event_status.setVisibility(View.VISIBLE);
            }
        }

        runOnUiThread(new Runnable() {
            public void run() {
                if(push_notification_count > 0){
                    holder.chart_count.setVisibility(View.VISIBLE);
                    holder.text_chart_count.setText(viewallevent.get(position).getPush_notification_count());
                }else {
                    holder.chart_count.setVisibility(View.GONE);
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, NewEventDetailPage.class);
                in.putExtra("id", viewallevent.get(position).getId());
                in.putExtra("event_type",viewallevent.get(position).getEvent_type());
                in.putExtra("homepage","1");
                context.startActivity(in);
            }
        });


        return convertView;
    }

}

