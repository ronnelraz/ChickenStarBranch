package com.charoenpokhandfoodph.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.charoenpokhandfoodph.Home;

import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.ViewOrder;
import com.charoenpokhandfoodph.config;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.orderlist;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    Context mContext;
    List<orderlist> newsList;

    public OrderAdapter(List<orderlist> list,Context context) {
        super();
        this.newsList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pending_order,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final orderlist getData = newsList.get(position);
        holder.transactionid.setText(getData.getTransid());
        holder.name.setText(getData.getName());
        holder.date.setText(getData.getDatetime());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                function.intent(ViewOrder.class,v.getContext());
                function.animIntent(v.getContext(), config.ltr);

                ViewOrder.stc_customerName = getData.getName();
                ViewOrder.stc_transctionNumber = getData.getTransid();
                ViewOrder.stc_sub = getData.getSub();
                ViewOrder.stc_total = getData.getTotal();
                ViewOrder.stc_km = getData.getKm();
                ViewOrder.stc_fee = getData.getCharge();
            }
        });


    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }

   class ViewHolder extends RecyclerView.ViewHolder{

        public TextView transactionid,name,date;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            transactionid = itemView.findViewById(R.id.transactionNumber);
            name = itemView.findViewById(R.id.customerName);
            date = itemView.findViewById(R.id.date);
            card = itemView.findViewById(R.id.card);
        }
    }
}
