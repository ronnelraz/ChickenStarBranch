package com.charoenpokhandfoodph.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.charoenpokhandfoodph.Customer_order_view;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.config;
import com.charoenpokhandfoodph.connection.con_update_product;
import com.charoenpokhandfoodph.connection.update_product_stocks;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.completedlist;
import com.charoenpokhandfoodph.modal.product_list;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class productAdapter extends RecyclerView.Adapter<productAdapter.ViewHolder> {

    Context mContext;
    List<product_list> newsList;

    public productAdapter(List<product_list> list, Context context) {
        super();
        this.newsList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final product_list getData = newsList.get(position);


        holder.loading.setVisibility(View.VISIBLE);
        holder.price.setText(config.PESO+function.format_number(Integer.parseInt(getData.getPrice())));
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

            holder.productname.setText(getData.getName());

            String getstatus = getData.status.equals("Y") ? "Available" : "Not Available";
            String color = getData.status.equals("Y") ? "#27ae60" : "#c0392b";
            int icon = getData.status.equals("Y") ? R.drawable.visible : R.drawable.hide;
            holder.status.setText(getstatus);
            holder.status.setTextColor(Color.parseColor(color));
            holder.status.setCompoundDrawablesWithIntrinsicBounds( icon, 0, 0, 0);

            holder.stock.setNumber(getData.getQty());
            holder.stock.setOnValueChangeListener((view, oldValue, newValue) -> {
                String value = String.valueOf(newValue);
                if(newValue == 0){
//                        holder.stocks.setEnabled(false);
                        updatestatus(getData.id,"N",view.getContext());
                    holder.switchButton.setChecked(false);
                    holder.status.setTextColor(Color.parseColor("#c0392b"));
                    holder.status.setText("Not Available");
                    holder.status.setCompoundDrawablesWithIntrinsicBounds( R.drawable.hide, 0, 0, 0);
                }

//

                if(newValue < Integer.parseInt(getData.getQty())){
                    Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
//                                    function.toast(view.getContext(),"Update : " + value);
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener errorListener = error -> {

                };
                Log.d("ID",getData.getId());
                update_product_stocks get = new update_product_stocks(getData.getId(),"minus",response,errorListener);
                get.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                queue.add(get);
                }
                else{
                    Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
//                                    function.toast(view.getContext(),"Update : " + value);
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener errorListener = error -> {

                };
                update_product_stocks get = new update_product_stocks(getData.getId(),"plus",response,errorListener);
                get.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                queue.add(get);
                }
            });


        holder.switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
//                    function.toast(getData.getId());
                    updatestatus(getData.id,"Y",view.getContext());
                    holder.status.setTextColor(Color.parseColor("#27ae60"));
                    holder.status.setText("Available");
                    holder.status.setCompoundDrawablesWithIntrinsicBounds( R.drawable.visible, 0, 0, 0);
                }
                else{
//                    function.toast(getData.getId());
                    updatestatus(getData.id,"N",view.getContext());
                    holder.status.setTextColor(Color.parseColor("#c0392b"));
                    holder.status.setText("Not Available");
                    holder.status.setCompoundDrawablesWithIntrinsicBounds( R.drawable.hide, 0, 0, 0);

                }
            }
        });


        if(getData.getStatus().equals("Y")){
            holder.status.setText("Available");
            holder.status.setTextColor(Color.parseColor("#27ae60"));
            holder.switchButton.setChecked(true);
        }
        else{
            holder.status.setText("Not Available");
            holder.status.setTextColor(Color.parseColor("#c0392b"));
            holder.switchButton.setChecked(false);
        }

        if(getData.getPercent().isEmpty()){
            holder.percent.setVisibility(View.GONE);
            holder.days.setVisibility(View.GONE);
        }
        else{
            holder.percent.setVisibility(View.VISIBLE);
            holder.days.setVisibility(View.VISIBLE);
            holder.percent.setText(getData.getPercent()+"%\nOFF");
            if(getData.getDays().equals("false")){
                holder.percent.setVisibility(View.GONE);
                holder.days.setVisibility(View.GONE);
            }
            else{
                if(Integer.parseInt(getData.getDays()) == 0){
                    holder.days.setText("Last day");
                }
                else if(Integer.parseInt(getData.getDays()) == 1){
                    holder.days.setText(getData.getDays()+" day left");
                }
                else{
                    holder.days.setText(getData.getDays()+" days left");
                }
            }
        }

    }

    public void updatestatus(String id,String status,Context context){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonObject = new JSONObject(response1);
                boolean success = jsonObject.getBoolean("success");
                if(success){
                    function.noti(context,"Product","Update status Successfully!",R.drawable.icons8_ok_2);
                }

            }
            catch (JSONException e){
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_update_product get = new con_update_product(id,status,response,errorListener);//function.getInstance(this).getDeviceToken()
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(get);
        //end
    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }



   class ViewHolder extends RecyclerView.ViewHolder{

        public TextView productname,status,percent,days,price;
        public ElegantNumberButton stock;
        public SwitchButton switchButton;
        public ProgressBar loading;
        public RoundedImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
           productname = itemView.findViewById(R.id.productname);
            status = itemView.findViewById(R.id.status);
            stock = itemView.findViewById(R.id.qty);
            switchButton = itemView.findViewById(R.id.toggle);
            percent = itemView.findViewById(R.id.discount);
            days = itemView.findViewById(R.id.days);
            loading = itemView.findViewById(R.id.loading);
            img = itemView.findViewById(R.id.img);
            price = itemView.findViewById(R.id.price);

        }
    }
}
