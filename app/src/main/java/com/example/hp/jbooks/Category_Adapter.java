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

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder> {

    Context context;

    //List<Subject_Categories> content_categories;
    List<Subject_Categories> Subject_Categories;

    public Category_Adapter(List<Subject_Categories> getDataAdapter, Context context){

        super();

        this.Subject_Categories = getDataAdapter;

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

        Subject_Categories getDataAdapter1 =  Subject_Categories.get(position);

        holder.CategoryName.setText(getDataAdapter1.getName());

    }

    @Override
    public int getItemCount() {

        return Subject_Categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView CategoryName;


        public ViewHolder(View itemView) {

            super(itemView);

            CategoryName = (TextView) itemView.findViewById(R.id.TextViewCard) ;


        }
    }
}
