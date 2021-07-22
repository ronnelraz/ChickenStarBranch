package com.charoenpokhandfoodph.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.Home;
import com.charoenpokhandfoodph.Login;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.ViewOrder;
import com.charoenpokhandfoodph.config;
import com.charoenpokhandfoodph.connection.con_cancel_order;
import com.charoenpokhandfoodph.connection.con_cancel_transaction;
import com.charoenpokhandfoodph.connection.con_login;
import com.charoenpokhandfoodph.connection.con_updateQty;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.order_view_list;
import com.charoenpokhandfoodph.modal.orderlist;
import com.daimajia.swipe.SwipeLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ezy.ui.view.NumberStepper;

import static com.charoenpokhandfoodph.function.animIntent;
import static com.charoenpokhandfoodph.function.getInstance;
import static com.charoenpokhandfoodph.function.intent;
import static com.charoenpokhandfoodph.function.islogin;
import static com.charoenpokhandfoodph.function.toast;


public class OrderViewListAdapter extends RecyclerView.Adapter<OrderViewListAdapter.ViewHolder> {

    Context mContext;
    List<order_view_list> newsList;

    public OrderViewListAdapter(List<order_view_list> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final order_view_list getData = newsList.get(position);

        if(getData.getStatusHeader().equals("ACCEPTED")){
            holder.swipe.setSwipeEnabled(false);
            holder.qty.setEnabled(false);
        }
        else{
            holder.swipe.setSwipeEnabled(true);
            holder.qty.setEnabled(true);
        }


        holder.productname.setText(getData.getName());
        holder.qty.setValue(Integer.valueOf(getData.getQty()));
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


        holder.qty.setOnValueChangedListener((view, value) -> {

            Double newQty = getData.getPromotion_status().equals("0") ? Double.parseDouble(getData.getPrice()) * value : Double.parseDouble(getData.getNewprice()) * value;
            qty(view.getContext(), getData.getOrder_id(),String.valueOf(newQty),String.valueOf(value));
        });

        holder.cancelitem.setOnClickListener(v -> {



            new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("you want to Cancel " + getData.getName() + "?")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(sDialog -> {

                        if(getItemCount() == 1){

                            Response.Listener<String> response = response1 -> {
                            try {
                                JSONObject jsonResponse = new JSONObject(response1);
                                boolean success = jsonResponse.getBoolean("success");


                                if(success){
                                    ViewOrder.loadData(ViewOrder.stc_user_id,ViewOrder.stc_tid,v.getContext());
                                    sDialog.dismissWithAnimation();
                                    updateTransaction(v.getContext(),ViewOrder.stc_transctionNumber);
                                }
                                else{

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        };

                        con_cancel_order get = new con_cancel_order(getData.getOrder_id(),ViewOrder.stc_customer_number,getData.getName(),getData.getContact(),response,null);
                        get.setRetryPolicy(new DefaultRetryPolicy(
                                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        RequestQueue queue = Volley.newRequestQueue(v.getContext());
                        queue.add(get);
                        }
                        else{
                            Response.Listener<String> response = response1 -> {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response1);
                                    boolean success = jsonResponse.getBoolean("success");


                                    if(success){
                                        ViewOrder.loadData(ViewOrder.stc_user_id,ViewOrder.stc_tid,v.getContext());
                                        sDialog.dismissWithAnimation();
                                    }
                                    else{

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            };

                            con_cancel_order get = new con_cancel_order(getData.getOrder_id(),ViewOrder.stc_customer_number,getData.getName(),getData.getContact(),response,null);
                            get.setRetryPolicy(new DefaultRetryPolicy(
                                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            RequestQueue queue = Volley.newRequestQueue(v.getContext());
                            queue.add(get);
                        }


                    })
                    .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
        });



        if(getData.getPromotion_status().equals("0")){
            holder.discount.setVisibility(View.GONE);
            holder.origprice.setVisibility(View.GONE);
            holder.price.setText("₱"+getData.getPrice());
            holder.subtotal.setText("₱"+getData.getTotal_qty());
        }
        else{
            holder.discount.setVisibility(View.VISIBLE);
            holder.discount.setText(getData.getPercent() + "%\nOFF");

            holder.origprice.setVisibility(View.VISIBLE);
            holder.origprice.setText("₱"+getData.getPrice());
            holder.price.setTextColor(Color.parseColor("#e74c3c"));
            holder.origprice.setPaintFlags(holder.origprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            holder.price.setText("₱"+getData.getNewprice());
            holder.subtotal.setText("₱"+getData.getTotal_qty());
        }



    }


    protected  void qty(Context context, String id,String total,String qty){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");


                if(success){
                    ViewOrder.loadData(ViewOrder.stc_user_id,ViewOrder.stc_tid,context);
                }
                else{

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        con_updateQty get = new con_updateQty(id,total,qty,response,null);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(get);
    }


    protected void updateTransaction(Context context,String tid){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");


                if(success){
                    function.intent(Home.class,context);
                    function.animIntent(context,config.rtl);
                }
                else{

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        con_cancel_transaction get = new  con_cancel_transaction(tid,response,null);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(get);
    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }

   class ViewHolder extends RecyclerView.ViewHolder{

        public SwipeLayout swipe;
        public LinearLayout cancelitem;
        public ProgressBar loading;
        public RoundedImageView img;
        public TextView discount,productname,origprice,price,subtotal;
        public NumberStepper qty;

        public ViewHolder(View itemView) {
            super(itemView);
            swipe = itemView.findViewById(R.id.swipe);
            cancelitem = itemView.findViewById(R.id.cancelitem);
            loading = itemView.findViewById(R.id.loading);
            img = itemView.findViewById(R.id.img);
            discount = itemView.findViewById(R.id.discount);
            productname = itemView.findViewById(R.id.productname);
            origprice = itemView.findViewById(R.id.origprice);
            price = itemView.findViewById(R.id.price);
            subtotal = itemView.findViewById(R.id.subtotal);
            qty = itemView.findViewById(R.id.qty);

        }
    }
}
