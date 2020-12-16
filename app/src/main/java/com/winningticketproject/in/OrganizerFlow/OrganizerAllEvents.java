package com.winningticketproject.in.OrganizerFlow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winningticketproject.in.AppInfo.AllEventGetterSetter;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.R;

import java.util.ArrayList;
import java.util.List;

import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class OrganizerAllEvents extends BaseAdapter {

     Context context;
     LayoutInflater inflater;
    List<AllEventGetterSetter> viewallevent = new ArrayList<>();
    public OrganizerAllEvents(Context context, List<AllEventGetterSetter> viewallevent) {
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
        TextView evnt_name, event_date,event_status;
        ImageView event_img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final OrganizerAllEvents.ViewHolder holder;
        if (convertView == null) {


            holder = new OrganizerAllEvents.ViewHolder();
            convertView = inflater.inflate(R.layout.custome_event_list, null);

            holder.evnt_name = convertView.findViewById(R.id.evnt_name);
            holder.event_date = convertView.findViewById(R.id.event_date);
            holder.event_img = convertView.findViewById(R.id.event_logo);

            holder.event_status = convertView.findViewById(R.id.event_status);
            holder.event_status.setTypeface(italic);

            holder.evnt_name.setTypeface(regular);
            holder.event_date.setTypeface(regular);
            convertView.setTag(holder);
        } else

            holder = (OrganizerAllEvents.ViewHolder) convertView.getTag();


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

        if (viewallevent.get(position).getEvent_status().equals("Live")) {
                holder.event_status.setText("\u2022 Live");
                holder.event_status.setVisibility(View.VISIBLE);
            }else {
            holder.event_status.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, OrganizerAuctioItems.class);
                in.putExtra("id", viewallevent.get(position).getId());
                context.startActivity(in);
            }
        });
        return convertView;
    }

}

