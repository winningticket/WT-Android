package com.winningticketproject.RoundNotes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.winningticketproject.in.R;

import java.util.ArrayList;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class YourRoundAdapter extends RecyclerView.Adapter<YourRoundAdapter.CustomViewHolderlist> {
    private Context mContext;
    ArrayList<Round_note_List> round_note_lists = new ArrayList<>();

    YourRoundAdapter(Context context,ArrayList<Round_note_List> round_note_lists) {
        this.mContext = context;
        this.round_note_lists = round_note_lists;
    }

    public  class CustomViewHolderlist extends RecyclerView.ViewHolder {
        private TextView tv_note_event_name, tv_note_event_date;
        View view;

        public CustomViewHolderlist(View itemView) {

            super(itemView);
            view = itemView;

            tv_note_event_name = view.findViewById(R.id.tv_note_event_name);
            tv_note_event_name.setTypeface(medium);

            tv_note_event_date = view.findViewById(R.id.tv_note_event_date);
            tv_note_event_date.setTypeface(regular);

        }
    }

        @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public CustomViewHolderlist onCreateViewHolder(ViewGroup parent, int viewType) {
                View viewTWO = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_round_notes, parent, false);
                return new YourRoundAdapter.CustomViewHolderlist(viewTWO);
    }

    @Override
    public void onBindViewHolder(YourRoundAdapter.CustomViewHolderlist holder, final int position) {

        holder.tv_note_event_name.setText(round_note_lists.get(position).name);
        holder.tv_note_event_date.setText(round_note_lists.get(position).date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(mContext, Round_notes_details.class);
                in.putExtra("round_note_id",round_note_lists.get(position).id);
                in.putExtra("rounds_name",round_note_lists.get(position).name);
                in.putExtra("rounds_date",round_note_lists.get(position).date);
                mContext.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return round_note_lists.size();
    }

}

