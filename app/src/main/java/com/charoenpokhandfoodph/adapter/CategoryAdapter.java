package com.charoenpokhandfoodph.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.charoenpokhandfoodph.Customer_order_view;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.config;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.category_view_list;
import com.charoenpokhandfoodph.modal.completedlist;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import hari.bounceview.BounceView;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context mContext;
    List<category_view_list> newsList;
    private BottomSheetBehavior bottomSheetBehavior;

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
        Picasso.get().load(config.URLIMGPRODUCT + getData.img).into(holder.img, new Callback() {
            @Override
            public void onSuccess() {
                holder.loading.setVisibility(View.GONE);
                Picasso.get().load(config.URLIMGPRODUCT + getData.img).into(holder.img);
            }

            @Override
            public void onError(Exception e) {
                holder.loading.setVisibility(View.GONE);
                holder.img.setBackgroundResource(R.drawable.logo);
            }
        });

        holder.card.setOnClickListener(vheader -> {
            View view = LayoutInflater.from(vheader.getContext()).inflate(R.layout.bottomsheet_category_menu,null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(vheader.getContext(),R.style.BottomSheetDialog);


            LinearLayout linearLayout = view.findViewById(R.id.root);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            params.height = getScreenHeight();
            linearLayout.setLayoutParams(params);


//            MaterialButton close = view.findViewById(R.id.close);
//            close.setOnClickListener(v -> {
//                bottomSheetDialog.dismiss();
//            });

            function.toast(vheader.getContext(), function.getUID() + " " + getData.getId());


            bottomSheetDialog.setContentView(view);
            bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetDialog.show();
        });


    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }



   class ViewHolder extends RecyclerView.ViewHolder{

        public TextView categoryname;
        public ProgressBar loading;
        public RoundedImageView img;
        public MaterialCardView card;
        public ViewHolder(View itemView) {
            super(itemView);
            categoryname = itemView.findViewById(R.id.categoryname);
            loading = itemView.findViewById(R.id.loading);
            img = itemView.findViewById(R.id.img);
            card = itemView.findViewById(R.id.card);

        }
    }
}
