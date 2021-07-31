package com.charoenpokhandfoodph.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.config;
import com.charoenpokhandfoodph.connection.con_add_inv;
import com.charoenpokhandfoodph.connection.con_load_category_item_setup;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.category_view_list;
import com.charoenpokhandfoodph.modal.product_setup_list;
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


public class CategorySetupAdapter extends RecyclerView.Adapter<CategorySetupAdapter.ViewHolder> {

    Context mContext;
    List<category_view_list> newsList;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView.Adapter adapter;
    private List<product_setup_list> list;

    private boolean isclickall = false;

    public CategorySetupAdapter(List<category_view_list> list, Context context) {
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
            View view = LayoutInflater.from(vheader.getContext()).inflate(R.layout.bottomsheet_category_menu_setup,null);

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(vheader.getContext(),R.style.BottomSheetDialog);


            LinearLayout linearLayout = view.findViewById(R.id.root);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            params.height = getScreenHeight();
            linearLayout.setLayoutParams(params);



            RecyclerView rviewbottom = view.findViewById(R.id.data);
            TextInputEditText search = view.findViewById(R.id.search);
            SwipeRefreshLayout swipe = view.findViewById(R.id.swipe);
            MaterialButton back = view.findViewById(R.id.back);
            MaterialButton save = view.findViewById(R.id.save);
            MaterialButton checkall = view.findViewById(R.id.checkall);





            TextView categoryname = view.findViewById(R.id.categoryname);
            categoryname.setText("Category : " +getData.getName());
            TextView selectedItem = view.findViewById(R.id.selectedItem);



            rviewbottom.setOnTouchListener((v, event) -> {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                v.onTouchEvent(event);
                return true;
            });

            rviewbottom.setHasFixedSize(true);
            rviewbottom.setItemViewCacheSize(999999999);

            list = new ArrayList<>();
            rviewbottom.setLayoutManager(new GridLayoutManager(view.getContext(),2));
            adapter = new productSetupAdapter(list,view.getContext(),selectedItem,save,swipe);
            rviewbottom.setAdapter(adapter);

            back.setOnClickListener(v -> {
                list.clear();
                bottomSheetDialog.dismiss();
            });





             save.setOnClickListener(v -> {
                List<product_setup_list> selectedItemlist = new productSetupAdapter(list,view.getContext(),selectedItem,save,swipe).getSelected();


                 for(int i = 0; i <selectedItemlist.size(); i++){
                     if(!(i + 1 < selectedItemlist.size())) {
//                        function.toast(v.getContext(),"last");
                        addinv(v.getContext(),selectedItemlist.get(i).getId(),save);
                        swipe.setRefreshing(true);
                         new Handler().postDelayed(() -> {
                             swipe.setRefreshing(false);
                             loadData(view.getContext(),rviewbottom,getData.getId(),selectedItem,save,swipe);
                         },3000);
                     }
                     else{
//                         function.toast(v.getContext(),selectedItemlist.get(i).getId() + " " + function.getUID());
                         addinv(v.getContext(),selectedItemlist.get(i).getId(),save);
                     }


//
//                    if(i == 0){
////                        function.toast(v.getContext(),selectedItemlist.get(i).getId() + " " + function.getUID());
////                        addinv(v.getContext(),selectedItemlist.get(i).getId(),save);
//                        function.toast(v.getContext(),"last");
////                        loadData(view.getContext(),rviewbottom,getData.getId(),selectedItem,save);
//
//                    }
//                    else{
////
//                        function.toast(v.getContext(),"more" + i);
//
//
//                    }
                }



            });

            swipe.setOnRefreshListener(() -> {
                list.clear();
                loadData(view.getContext(),rviewbottom,getData.getId(),selectedItem,save,swipe);
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
                    ArrayList<product_setup_list> newListbottom = new ArrayList<>();
                    for (product_setup_list sub : list)
                    {
                        String name = sub.getName().toLowerCase();
                        if(name.contains(s)){
                            newListbottom.add(sub);
                        }
                        adapter = new productSetupAdapter(newListbottom,view.getContext(),selectedItem,save,swipe);
                        rviewbottom.setAdapter(adapter);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            loadData(view.getContext(),rviewbottom,getData.getId(),selectedItem,save,swipe);

            bottomSheetDialog.setContentView(view);
            bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetDialog.show();
        });


    }


    protected void addinv(Context context,String id,MaterialButton save){
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");
                if(success){
                 save.setEnabled(false);
                }
                else{

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_add_inv get = new con_add_inv(id,function.getUID(),response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(get);
    }

    protected void loadData(Context context,RecyclerView recyclerView,String id,TextView textView,MaterialButton save,SwipeRefreshLayout swipe){
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

                        product_setup_list item = new product_setup_list(
                                object.getString("id"),
                                object.getString("name"),
                                object.getString("img"),
                                object.getString("price"),
                                object.getBoolean("mark"),
                                false
                        );

                        list.add(item);


                    }
                    adapter = new productSetupAdapter(list,context,textView,save,swipe);
                    recyclerView.setAdapter(adapter);
                }
                else{
//                    function.noti(context,"No Data Available","No Product Found!",R.drawable.bg);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_load_category_item_setup get = new con_load_category_item_setup(id,function.getUID(),response,errorListener);
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
