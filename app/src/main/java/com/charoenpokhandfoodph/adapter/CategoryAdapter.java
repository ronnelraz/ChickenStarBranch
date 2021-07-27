package com.charoenpokhandfoodph.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.charoenpokhandfoodph.Customer_order_view;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.config;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.category_view_list;
import com.charoenpokhandfoodph.modal.completedlist;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context mContext;
    List<category_view_list> newsList;

    public CategoryAdapter(List<category_view_list> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final category_view_list getData = newsList.get(position);

        holder.categoryname.setText(getData.getName());


    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }



   class ViewHolder extends RecyclerView.ViewHolder{

        public TextView categoryname;
        public ViewHolder(View itemView) {
            super(itemView);
            categoryname = itemView.findViewById(R.id.categoryname);

        }
    }
}
