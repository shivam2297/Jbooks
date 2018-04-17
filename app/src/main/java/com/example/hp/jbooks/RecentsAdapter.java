package com.example.hp.jbooks;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.MyViewHolder> {
    private List<Subject_Contents> recents;

    public RecentsAdapter(List<Subject_Contents> recents) {
        this.recents = recents;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recents_viewholder, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String timestamp = recents.get(position).getTimestamp();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        Date date = null;
        try {
            date = sdf.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM dd, hh:mm a");
        sdf2.setTimeZone(TimeZone.getTimeZone("IST"));
        String dateStr = sdf2.format(date);
        holder.nameTextView.setText(recents.get(position).getName());
        holder.linkTextView.setText(recents.get(position).getLink());
        holder.timeTextView.setText(dateStr);
    }

    @Override
    public int getItemCount() {
        return recents.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView, linkTextView, timeTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.recent_name);
            linkTextView = (TextView) itemView.findViewById(R.id.recent_link);
            timeTextView = (TextView) itemView.findViewById(R.id.recent_time);
        }
    }
}
