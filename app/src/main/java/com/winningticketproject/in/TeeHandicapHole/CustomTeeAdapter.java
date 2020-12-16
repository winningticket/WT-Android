package com.winningticketproject.in.TeeHandicapHole;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winningticketproject.in.R;

import java.util.ArrayList;

import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class CustomTeeAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> tee_values;
    LayoutInflater inflter;

    public CustomTeeAdapter(Context applicationContext, ArrayList<String> tee_values) {
        this.context = applicationContext;
        this.tee_values = tee_values;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return tee_values.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_select_hole_handicap, null);
        TextView names = view.findViewById(R.id.textViewSpinnerItem);
        names.setText(Html.fromHtml(tee_values.get(i)));
        names.setTypeface(regular);

        return view;
    }
}
