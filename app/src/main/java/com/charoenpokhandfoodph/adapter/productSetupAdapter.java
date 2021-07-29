package com.charoenpokhandfoodph.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.config;
import com.charoenpokhandfoodph.connection.con_load_category_item_setup;
import com.charoenpokhandfoodph.connection.con_remove_inv;
import com.charoenpokhandfoodph.connection.con_update_product;
import com.charoenpokhandfoodph.connection.update_product_stocks;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.product_list;
import com.charoenpokhandfoodph.modal.product_setup_list;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;



public class productSetupAdapter extends RecyclerView.Adapter<productSetupAdapter.ViewHolder> {

    Context mContext;
    List<product_setup_list> newsList;
    TextView selectedItem;
    MaterialButton save;


    public productSetupAdapter(List<product_setup_list> list, Context context,TextView selectedItem,MaterialButton save) {
        super();
        this.newsList = list;
        this.mContext = context;
        this.selectedItem = selectedItem;
        this.save = save;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product_setup,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final product_setup_list getData = newsList.get(position);



        holder.card.setOnClickListener(v -> {

            if(getData.isMark()){
                new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to remove " + getData.getName() + "to your inventory?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                Response.Listener<String> response = response1 -> {
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response1);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if(success){
                                            sDialog.dismissWithAnimation();
                                            holder.mark.setImageResource(R.drawable.icons8_round);
                                        }
                                        else{

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                };
                                Response.ErrorListener errorListener = error -> {

                                };
                                con_remove_inv get = new con_remove_inv(getData.getId(),function.getUID(),response,errorListener);
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



            }
            else{
                if(getData.isSelected){
                    holder.mark.setImageResource(R.drawable.icons8_round);
                    getData.isSelected = false;
                    if(getSelected().size() == 0){
                       save.setEnabled(false);
                    }

                     String newitems = getSelected().size() <= 1 ? "Selected Item : " + getSelected().size() : "Selected Items : " + getSelected().size();
                    selectedItem.setText(newitems);



                }
                else{
                    holder.mark.setImageResource(R.drawable.icons8_ok_4);
                    getData.isSelected = true;
                    String newitems = getSelected().size() <= 1 ? "Selected Item : " + getSelected().size() : "Selected Items : " + getSelected().size();
                    selectedItem.setText(newitems);
                    save.setEnabled(true);
                }
            }
        });





        if (getData.isMark()){
            holder.mark.setImageResource(R.drawable.icons8_ok_4);
        }
        else{
            holder.mark.setImageResource(R.drawable.icons8_round);
        }

        holder.productname.setText(getData.getName());
        holder.price.setText(config.PESO+function.format_number(Integer.parseInt(getData.getPrice())));

        holder.loading.setVisibility(View.VISIBLE);
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

    }


    public List<product_setup_list> getSelected(){
        List<product_setup_list> selected = new ArrayList<>();
        for(product_setup_list list : newsList){
            if(list.isSelected()){
                selected.add(list);
            }
        }
        return selected;
    }

    public void selectAllItem(boolean isSelectedAll) {
        try {
            if (newsList != null) {
                for (int index = 0; index < newsList.size(); index++) {
                    newsList.get(index).setSelected(isSelectedAll);
                }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();

    }

   class ViewHolder extends RecyclerView.ViewHolder{

        public TextView productname,price;
        public ProgressBar loading;
        public RoundedImageView img;
        public ImageView mark;
        public MaterialCardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            productname = itemView.findViewById(R.id.productname);
            mark = itemView.findViewById(R.id.mark);
            loading = itemView.findViewById(R.id.loading);
            img = itemView.findViewById(R.id.img);
            price = itemView.findViewById(R.id.price);
            card = itemView.findViewById(R.id.card);

        }
    }
}
