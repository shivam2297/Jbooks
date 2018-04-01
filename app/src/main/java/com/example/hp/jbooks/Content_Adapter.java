package com.example.hp.jbooks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Content_Adapter extends RecyclerView.Adapter<Content_Adapter.ViewHolder> {

    Context context;

    //List<Subject_Categories> content_categories;
    List<Subject_Contents> subject_contents;

    public Content_Adapter(List<Subject_Contents> subject_contents, Context context) {
        super();
        this.context = context;
        this.subject_contents = subject_contents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contents, parent, false);

        Content_Adapter.ViewHolder viewHolder = new Content_Adapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Subject_Contents getDataAdapter1 = subject_contents.get(position);

        holder.contentName.setText(getDataAdapter1.getName());
        holder.contentLink.setText(getDataAdapter1.getLink());

    }

    @Override
    public int getItemCount() {
        return subject_contents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView contentName, contentLink;

        public ViewHolder(View itemView) {
            super(itemView);

            contentName = (TextView) itemView.findViewById(R.id.TextViewCard);
            contentLink = (TextView) itemView.findViewById(R.id.TextViewLink);
        }
    }
}
