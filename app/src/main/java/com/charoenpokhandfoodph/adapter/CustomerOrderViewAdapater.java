package com.charoenpokhandfoodph.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.Home;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.ViewOrder;
import com.charoenpokhandfoodph.config;
import com.charoenpokhandfoodph.connection.con_cancel_order;
import com.charoenpokhandfoodph.connection.con_cancel_transaction;
import com.charoenpokhandfoodph.connection.con_updateQty;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.customer_view_list;
import com.charoenpokhandfoodph.modal.order_view_list;
import com.daimajia.swipe.SwipeLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ezy.ui.view.NumberStepper;

import static com.charoenpokhandfoodph.function.getInstance;


public class CustomerOrderViewAdapater extends RecyclerView.Adapter<CustomerOrderViewAdapater.ViewHolder> {

    Context mContext;
    List<customer_view_list> newsList;

    public CustomerOrderViewAdapater(List<customer_view_list> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_customer_order,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final customer_view_list getData = newsList.get(position);


        holder.productname.setText(getData.getName());
        Picasso.get().load(config.URLIMGPRODUCT + getData.img).into(holder.img, new Callback() {
            @Override
            public void onSuccess() {
                holder.loading.setVisibility(View.GONE);
                Picasso.get().load(config.URLIMGPRODUCT + getData.img).into(holder.img);
            }

            @Override
            public void onError(Exception e) {
                holder.loading.setVisibility(View.GONE);
                holder.img.setBackgroundResource(R.drawable.bg);
            }
        });

        if(getData.getPromotion_status().equals("0")){
            holder.discount.setVisibility(View.GONE);
            holder.origprice.setVisibility(View.GONE);
            holder.price.setText("₱"+getData.getPrice());
            holder.subtotal.setText("Qty : " + getData.getQty() +" - ₱"+getData.getTotal_qty());
        }
        else{
            holder.discount.setVisibility(View.VISIBLE);
            holder.discount.setText(getData.getPercent() + "%\nOFF");

            holder.origprice.setVisibility(View.VISIBLE);
            holder.origprice.setText("₱"+getData.getPrice());
            holder.price.setTextColor(Color.parseColor("#e74c3c"));
            holder.origprice.setPaintFlags(holder.origprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.price.setText("₱"+getData.getNewprice());
            holder.subtotal.setText("Qty : " + getData.getQty() +" - ₱"+getData.getTotal_qty());
        }

        if(getData.getStatus().equals("cancel")){
            holder.status.setText("Cancelled");
            holder.status.setTextColor(Color.parseColor("#c0392b"));
            holder.relativebg.setBackgroundResource(R.drawable.rightcancel);
        }
        else{
            holder.status.setText("Completed");
            holder.status.setTextColor(Color.parseColor("#27ae60"));
            holder.relativebg.setBackgroundResource(R.drawable.right_comple);
        }



    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }

   class ViewHolder extends RecyclerView.ViewHolder{

        public ProgressBar loading;
        public RoundedImageView img;
        public TextView discount,productname,origprice,price,subtotal,status;
        public RelativeLayout relativebg;

        public ViewHolder(View itemView) {
            super(itemView);
            relativebg = itemView.findViewById(R.id.relativebg);
            loading = itemView.findViewById(R.id.loading);
            img = itemView.findViewById(R.id.img);
            discount = itemView.findViewById(R.id.discount);
            productname = itemView.findViewById(R.id.productname);
            origprice = itemView.findViewById(R.id.origprice);
            price = itemView.findViewById(R.id.price);
            subtotal = itemView.findViewById(R.id.subtotal);
            status = itemView.findViewById(R.id.status);

        }
    }
}
