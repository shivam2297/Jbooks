package com.example.hp.jbooks;

/**
 * Created by HP on 1/4/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import java.util.List;

public class RecyclerViewCardViewAdapter extends RecyclerView.Adapter<RecyclerViewCardViewAdapter.ViewHolder> {

    Context context;

    //List<subject_categories> content_categories;
    List<subject_categories> subject_categories;

    public RecyclerViewCardViewAdapter(List<subject_categories> getDataAdapter, Context context){

        super();

        this.subject_categories = getDataAdapter;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        subject_categories getDataAdapter1 =  subject_categories.get(position);

        holder.CategoryName.setText(getDataAdapter1.getName());

    }

    @Override
    public int getItemCount() {

        return subject_categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView CategoryName;


        public ViewHolder(View itemView) {

            super(itemView);

            CategoryName = (TextView) itemView.findViewById(R.id.TextViewCard) ;


        }
    }
}
