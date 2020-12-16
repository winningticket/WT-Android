package com.winningticketproject.RoundNotes;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.winningticketproject.in.R;

import java.util.ArrayList;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class YourRoundADetailsdapter extends RecyclerView.Adapter<YourRoundADetailsdapter.CustomViewHolderNote> {
    private Context mContext;
    ArrayList<Round_note_List> round_note_lists = new ArrayList<>();

    YourRoundADetailsdapter(Context context,ArrayList<Round_note_List> round_note_lists) {
        this.mContext = context;
        this.round_note_lists = round_note_lists;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public CustomViewHolderNote onCreateViewHolder(ViewGroup parent, int viewType) {
                View viewTWO = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_round_notes_details, parent, false);
                return new YourRoundADetailsdapter.CustomViewHolderNote(viewTWO);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolderNote customViewHolderNote, int i) {

        customViewHolderNote.tv_note_event_hole.setText(round_note_lists.get(i).name);

        if (round_note_lists.get(i).date == null || round_note_lists.get(i).date.equals("null")){

            customViewHolderNote.tv_note_event_note.setText("No note has been added.");
            customViewHolderNote.tv_note_event_note.setTextColor(mContext.getColor(R.color.colordarkgray));

        }else {
            customViewHolderNote.tv_note_event_note.setText(round_note_lists.get(i).date);
        }

    }


    @Override
    public int getItemCount() {
        return round_note_lists.size();
    }

    public  class CustomViewHolderNote extends RecyclerView.ViewHolder {
        private TextView tv_note_event_hole,tv_note_event_note;
        View view;

        public CustomViewHolderNote(View itemView) {

            super(itemView);
            view = itemView;

            tv_note_event_hole = view.findViewById(R.id.tv_note_event_hole);
            tv_note_event_hole.setTypeface(medium);

            tv_note_event_note = view.findViewById(R.id.tv_note_event_note);
            tv_note_event_note.setTypeface(regular);

        }

        }
    }

