package com.winningticketproject.in.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.AppInfo.AllEventGetterSetter;
import com.winningticketproject.in.R;

import java.text.ParseException;
import java.util.List;

import static com.google.android.gms.internal.zzail.runOnUiThread;
import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class Recycler_Article_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<AllEventGetterSetter> feedItemList;
    private Context mContext;

    public Recycler_Article_Adapter(Context context, List<AllEventGetterSetter> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }
    @Override
    public int getItemViewType(int position) {
        return 1;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View viewTWO = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_event_list, parent, false);
                return new CustomViewHolderlist(viewTWO);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CustomViewHolderlist)holder).bindData(feedItemList.get(position).getName(),feedItemList.get(position).getStart_date(),feedItemList.get(position).getEvent_logo(),feedItemList.get(position).getEvent_type(),feedItemList.get(position).getPush_notification_count());
    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    public  class CustomViewHolderlist extends RecyclerView.ViewHolder {
        private TextView evnt_name,event_date,event_status,text_chart_count;
        LinearLayout chart_count;
        View view;
        ImageView event_logo;
        public CustomViewHolderlist(View itemView) {

            super(itemView);
            view = itemView;

            evnt_name = view.findViewById(R.id.evnt_name);
            event_date = view.findViewById(R.id.event_date);
            event_logo = view.findViewById(R.id.event_logo);

            chart_count = view.findViewById(R.id.chart_count);
            text_chart_count = view.findViewById(R.id.text_chart_count);

            event_status = view.findViewById(R.id.event_status);
            event_status.setTypeface(italic);

            evnt_name.setTypeface(medium);
            event_date.setTypeface(regular);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent in = new Intent(mContext, NewEventDetailPage.class);
                    in.putExtra("id",feedItemList.get(pos).getId());
                    in.putExtra("homepage","1");
                    in.putExtra("event_type",feedItemList.get(pos).getEvent_type());
                    mContext.startActivity(in);
                }
            });
        }

         void bindData(String name, String startdate,String event_logos,String event_type,int push_notification_count) {
            int position = getAdapterPosition();
            if (position % 2 == 0) {
                view.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                view.setBackgroundColor(Color.parseColor("#fafafa"));
            }

            if (event_type.equals("upcoming_or_live")){
                if (feedItemList.get(position).getEvent_status().equals("Live")) {
                    event_status.setText("\u2022 Live");
                    event_status.setVisibility(View.VISIBLE);
                }
            }

             runOnUiThread(new Runnable() {
                 public void run() {
                     if(push_notification_count > 0){
                         chart_count.setVisibility(View.VISIBLE);
                         text_chart_count.setText(push_notification_count+"");
                     }else {
                         chart_count.setVisibility(View.GONE);
                     }
                 }
             });

            event_date.setText(Html.fromHtml(startdate.replace("\n", "<br>")));

            evnt_name.setText(name.substring(0,1).toUpperCase() +name.substring(1).replaceAll("\\s+"," "));

            Picasso.with(mContext)
                    .load(event_logos).placeholder(R.drawable.auction_event_empty).error(R.drawable.auction_event_empty)
                    .into(event_logo);
        }
    }
}
