package com.charoenpokhandfoodph.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.Customer_order_view;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.config;
import com.charoenpokhandfoodph.connection.con_category;
import com.charoenpokhandfoodph.connection.con_category_child;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.category_view_list;
import com.charoenpokhandfoodph.modal.completedlist;
import com.charoenpokhandfoodph.modal.product_list;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hari.bounceview.BounceView;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context mContext;
    List<category_view_list> newsList;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView.Adapter adapter;
    private List<product_list> list;

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


    @SuppressLint("ClickableViewAccessibility")
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

            String getuid = function.getUID();
            String getcateid = getData.getId();

            RecyclerView rviewbottom = view.findViewById(R.id.data);
            TextInputEditText search = view.findViewById(R.id.search);
            SwipeRefreshLayout swipe = view.findViewById(R.id.swipe);
            MaterialButton back = view.findViewById(R.id.back);


            rviewbottom.setOnTouchListener((v, event) -> {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                v.onTouchEvent(event);
                return true;
            });

            rviewbottom.setHasFixedSize(true);
            rviewbottom.setItemViewCacheSize(999999999);

            list = new ArrayList<>();
            rviewbottom.setLayoutManager(new GridLayoutManager(view.getContext(),2));
            adapter = new productAdapter(list,view.getContext());
            rviewbottom.setAdapter(adapter);

            back.setOnClickListener(v -> {
                list.clear();
                bottomSheetDialog.dismiss();
            });


            swipe.setOnRefreshListener(() -> {
                list.clear();
                loadData(view.getContext(),rviewbottom,getuid,getcateid);
                swipe.setRefreshing(false);
            });

            swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ArrayList<product_list> newListbottom = new ArrayList<>();
                    for (product_list sub : list)
                    {
                        String name = sub.getName().toLowerCase();
                        if(name.contains(s)){
                            newListbottom.add(sub);
                        }
                        adapter = new productAdapter(newListbottom,view.getContext());
                        rviewbottom.setAdapter(adapter);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            loadData(view.getContext(),rviewbottom,getuid,getcateid);

            bottomSheetDialog.setContentView(view);
            bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

//            bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//                @Override
//                public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                        bottomSheetDialog.dismiss();
//                    }
//
//                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                        if(!rviewbottom.canScrollVertically(1)) {
//                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        }
//                    }
//                }
//
//                @Override
//                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                }
//            });
            bottomSheetDialog.show();
        });


    }

    protected void loadData(Context context,RecyclerView recyclerView,String uid,String cateid){
        list.clear();
        adapter.notifyDataSetChanged();
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");
                JSONArray array = jsonResponse.getJSONArray("data");

                if(success){

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        product_list item = new product_list(
                                object.getString("id"),
                                object.getString("name"),
                                object.getString("img"),
                                object.getString("qty"),
                                object.getString("status"),
                                object.getString("percent"),
                                object.getString("days"),
                                object.getBoolean("expired")
                        );

                        list.add(item);


                    }
                    adapter = new productAdapter(list,context);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    function.noti(context,"No Data Available","No Product Found!",R.drawable.bg);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_category_child get = new con_category_child(uid,cateid,response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(get);
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
