package com.winningticketproject.in.Fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.AppInfo.AllEventGetterSetter;
import com.winningticketproject.in.R;

import java.text.ParseException;
import java.util.List;

import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class Home_Page_Recycler_Article_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<AllEventGetterSetter> feedItemList;
    private Context mContext;

    public Home_Page_Recycler_Article_Adapter(Context context, List<AllEventGetterSetter> feedItemList) {
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
                View viewTWO = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_custome_event_list, parent, false);
                return new CustomViewHolderlist(viewTWO);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CustomViewHolderlist)holder).bindData(feedItemList.get(position).getId(),feedItemList.get(position).getName(),feedItemList.get(position).getStart_date(),feedItemList.get(position).getEvent_logo(),feedItemList.get(position).getEvent_type());
    }

    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    public  class CustomViewHolderlist extends RecyclerView.ViewHolder {
        private TextView evnt_name,event_date,event_status,live_event_text;
        View view;
        ImageView event_logo;
        public CustomViewHolderlist(View itemView) {

            super(itemView);
            view = itemView;

            evnt_name = view.findViewById(R.id.evnt_name);
            event_date = view.findViewById(R.id.event_date);
            event_logo = view.findViewById(R.id.event_logo);

            event_status = view.findViewById(R.id.event_status);
            event_status.setTypeface(medium);

            evnt_name.setTypeface(medium);
            event_date.setTypeface(regular);

            live_event_text = view.findViewById(R.id.live_event_text);
            live_event_text.setTypeface(italic);


            itemView.setOnClickListener(new View.OnClickListener() {
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

        public void bindData(String id,String name, String startdate,String event_logos,String event_type) {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            java.util.Date date = null;
            try
            {
                date = format.parse(startdate);
            }
            catch (ParseException e)
            {//nothing
            }

            try {
                java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MMMM dd, yyyy - hh:mm aa");
                String newDateStr = postFormater.format(date);
                event_date.setText(newDateStr+" EST");

            }catch (Exception e){
            }

            evnt_name.setText(name.substring(0,1).toUpperCase() +name.substring(1).replaceAll("\\s+"," "));

            Picasso.with(mContext)
                    .load(event_logos).placeholder(R.drawable.auction_event_empty).error(R.drawable.auction_event_empty)
                    .into(event_logo);
        }
    }
}
