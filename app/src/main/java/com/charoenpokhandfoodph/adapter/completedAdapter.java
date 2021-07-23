package com.charoenpokhandfoodph.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.charoenpokhandfoodph.Customer_order_view;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.ViewOrder;
import com.charoenpokhandfoodph.config;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.completedlist;
import com.charoenpokhandfoodph.modal.orderlist;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;


public class completedAdapter extends RecyclerView.Adapter<completedAdapter.ViewHolder> {

    Context mContext;
    List<completedlist> newsList;
    BottomSheetBehavior bottomSheetBehavior;

    public completedAdapter(List<completedlist> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_completed_order,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final completedlist getData = newsList.get(position);

        if(getData.getStatusHeader().equals("DONE")){
            holder.iconstatus.setImageResource(R.drawable.icons8_ok_3);
        }
        else{
            holder.iconstatus.setImageResource(R.drawable.icons8_cancel_4);
        }

        holder.customer.setText(getData.getName());
        holder.transactionNumber.setText(getData.getTransacid());
        holder.date.setText(getData.getDatetime());

        holder.card.setOnClickListener(v -> {
            function.intent(Customer_order_view.class,v.getContext());
            function.animIntent(v.getContext(),config.ltr);
            Customer_order_view.stc_tid = getData.getTransacid();
            Customer_order_view.stc_customer_id = getData.getClient_id();
            Customer_order_view.stc_status = getData.getStatusHeader();
            Customer_order_view.stc_customer_name = getData.getName();

        });


    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }



   class ViewHolder extends RecyclerView.ViewHolder{

        public TextView customer,transactionNumber,date;
        public CardView card;
        public ImageView iconstatus;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            iconstatus = itemView.findViewById(R.id.iconstatus);
            customer= itemView.findViewById(R.id.customer);
            transactionNumber = itemView.findViewById(R.id.transactionNumber);
            date = itemView.findViewById(R.id.datetime);


        }
    }
}
