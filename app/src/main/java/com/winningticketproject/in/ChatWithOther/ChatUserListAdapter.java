package com.winningticketproject.in.ChatWithOther;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winningticketproject.in.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

@SuppressWarnings("unchecked")
public class ChatUserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    List<User_list_Model> feedItemList;
    private Context mContext;
    List<User_list_Model> user_list_models_full;

    public ChatUserListAdapter(Context context, List<User_list_Model> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        user_list_models_full = new ArrayList<>(feedItemList);
    }
    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View viewTWO = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_user_chat_list, parent, false);
                return new ChatUserListAdapter.CustomViewHolderlist(viewTWO);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ChatUserListAdapter.CustomViewHolderlist)holder).bindData(feedItemList.get(position).getUser_id(),feedItemList.get(position).getName(),feedItemList.get(position).getAvtar_imge(),feedItemList.get(position).getEmail_id(),feedItemList.get(position).getKey(),feedItemList.get(position).getValue(),feedItemList.get(position).getStatus(),feedItemList.get(position).getLast_msg(),feedItemList.get(position).getTiming());
    }
    @Override
    public int getItemCount() {
        return feedItemList.size();
    }

    public  class CustomViewHolderlist extends RecyclerView.ViewHolder {
        private TextView evnt_name,text_chart_count,test_msg,online_status;
        View view;
        LinearLayout chart_count;
        CircleImageView event_logo;
        TextView tv_timer;
        public CustomViewHolderlist(View itemView) {

            super(itemView);
            view = itemView;

            evnt_name = view.findViewById(R.id.tv_user_name);
            evnt_name.setTypeface(medium);

            test_msg = view.findViewById(R.id.test_msg);
            test_msg.setTypeface(regular);


            online_status = view.findViewById(R.id.online_status);

            text_chart_count = view.findViewById(R.id.text_chart_count);
            text_chart_count.setTypeface(semibold);
            chart_count = view.findViewById(R.id.chart_count);

            event_logo = view.findViewById(R.id.user_list_logo);

            tv_timer = view.findViewById(R.id.tv_timer);
            tv_timer.setTypeface(regular);



        }

        public void bindData(final String user_id, final String user_name, final String user_avtar, String user_email, String k_id,String count,String status,String last_msg,String timer_values) {


            evnt_name.setText(user_name);
            int number = Integer.parseInt(count);
            if(number > 0) {
                chart_count.setVisibility(View.VISIBLE);
                text_chart_count.setText(count);
            }

            Picasso.with(mContext)
                    .load(user_avtar).skipMemoryCache()
                    .placeholder(R.drawable.profile_pic).error(R.drawable.profile_pic)         // optional
                    .into(event_logo);

            test_msg.setText(last_msg);

            tv_timer.setText(timer_values);

            if (status.equals("offline")) {
                online_status.setBackgroundResource(R.drawable.red_chat_count);
            }else {
                online_status.setBackgroundResource(R.drawable.status_green);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserDetails.chatWith_id = user_id+"_"+get_auth_token("event_id");
                    UserDetails.chatWith_user_name = user_name;
                    UserDetails.user_progile= user_avtar;
                    Intent in = new Intent(mContext,Chat_With_Other.class);
                    in.putExtra("notification_type","event");
                    in.putExtra("chat_with_icon",user_avtar);
                    mContext.startActivity(in);
                }
            });
        }
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User_list_Model> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(user_list_models_full);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (User_list_Model item : user_list_models_full) {
                    if (item.getName().toString().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            feedItemList.clear();
            feedItemList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}