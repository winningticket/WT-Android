package com.winningticketproject.in.MyAccountTab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winningticketproject.in.R;

import java.text.ParseException;
import java.util.ArrayList;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;

public class Transactionhistory_Adapter extends BaseAdapter {
    public Context context;
    ArrayList<Transaction_data> transaction_data = new ArrayList<>();

    public Transactionhistory_Adapter(Context context,ArrayList<Transaction_data> transaction_data) {
        super();
        this.context = context;
        this.transaction_data=transaction_data;
    }


    @Override
    public int getCount() {
        return transaction_data.size();
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

    public class ViewHolder
    {
        TextView amount_text,tranjaction_number,tranjaction_date,tranjaction_type;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= inflater.inflate(R.layout.custome_history_details_page,null);

            holder.amount_text = convertView.findViewById(R.id.amount_text);
            holder.tranjaction_number = convertView.findViewById(R.id.tranjaction_number);
            holder.tranjaction_date = convertView.findViewById(R.id.tranjaction_date);
            holder.tranjaction_type = convertView.findViewById(R.id.tranjaction_type);

            holder.amount_text.setTypeface(medium);
            holder.tranjaction_number.setTypeface(medium);
            holder.tranjaction_date.setTypeface(medium);
            holder.tranjaction_type.setTypeface(medium);

            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();

        holder.tranjaction_number.setText("#"+transaction_data.get(position).transation_orderid);

        if(transaction_data.get(position).transaction_type.equals("add_funds")){
            holder.amount_text.setTextColor(Color.parseColor("#00a651"));
        }else {
            holder.amount_text.setTextColor(Color.parseColor("#FD2726"));
        }

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        java.util.Date date = null;
        try
        {
            date = format.parse(transaction_data.get(position).transation_date);
        }
        catch (ParseException e)
        {
            //nothing
        }
        java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm aa");
        String newDateStr = postFormater.format(date);

        holder.tranjaction_date.setText(newDateStr.replace("am","AM").replace("pm","PM")  +" EST");


        try{
            if(transaction_data.get(position).transaction_type.equals("add_funds")){
                holder.tranjaction_type.setText("Add Funds");

            }else {
                String transaction_types=transaction_data.get(position).transaction_type.substring(0,1).toUpperCase() +transaction_data.get(position).transaction_type.substring(1);
                holder.tranjaction_type.setText(transaction_types);
            }
        }
        catch (Exception e) { }

        double pricevalue = Double.parseDouble(transaction_data.get(position).trandation_amount);
        holder.amount_text.setText(String.format("$ %.2f", pricevalue));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, Transaction_histrory_Details.class);
                in.putExtra("id",transaction_data.get(position).id);
                context.startActivity(in);


            }
        });


        return convertView;
    }
}