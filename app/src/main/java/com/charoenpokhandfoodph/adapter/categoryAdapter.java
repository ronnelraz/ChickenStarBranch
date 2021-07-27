package com.charoenpokhandfoodph.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.modal.category_view_list;
import com.charoenpokhandfoodph.modal.completedlist;

import java.util.ArrayList;
import java.util.List;

public class categoryAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<category_view_list> newsList = new ArrayList<>();
    private Context mContext;

    public categoryAdapter(Context context, List<category_view_list> newsList) {
        this.newsList = newsList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_category, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final category_view_list getData = newsList.get(i);
        ((ViewHolder)viewHolder).categoryname.setText(getData.getName());

//        // Set the name of the 'NicePlace'
//        ((ViewHolder)viewHolder).mName.setText(mNicePlaces.get(i).getTitle());
//
//        // Set the image
//        RequestOptions defaultOptions = new RequestOptions()
//                .error(R.drawable.ic_launcher_background);
//        Glide.with(mContext)
//                .setDefaultRequestOptions(defaultOptions)
//                .load(mNicePlaces.get(i).getImageUrl())
//                .into(((ViewHolder)viewHolder).mImage);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryname = itemView.findViewById(R.id.categoryname);
        }
    }
}