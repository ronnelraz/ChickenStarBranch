package com.charoenpokhandfoodph.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.Home;
import com.charoenpokhandfoodph.MainActivity;
import com.charoenpokhandfoodph.R;

import com.charoenpokhandfoodph.adapter.OrderAdapter;
import com.charoenpokhandfoodph.connection.con_orderlist;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.orderlist;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class order extends Fragment {

    public static SwipeRefreshLayout swipe;
    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<orderlist> list;
    public static ImageView noavailable;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.frag_order,parent,false);
        swipe = view.findViewById(R.id.swipe);
        recyclerView = view.findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        noavailable = view.findViewById(R.id.noAvailable);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(list,getActivity());
        recyclerView.setAdapter(adapter);



        swipe.setOnRefreshListener(() -> {
                list.clear();
                onLoadComplete();
        });


        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        loadData(getContext());
        return view;
    }

    public void onLoadComplete()
    {
        loadData(getContext());
        swipe.setRefreshing(false);
    }


    public static void loadData(Context cons){
        list.clear();
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray array = jsonResponse.getJSONArray("data");

                    if(success){

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                          orderlist item = new orderlist(
                                  object.getString("order_id"),
                                  object.getString("transid"),
                                  object.getString("product_id"),
                                  object.getString("sub"),
                                  object.getString("charge"),
                                  object.getString("total"),
                                  object.getString("accept"),
                                  object.getString("name"),
                                  object.getString("datetime"),
                                  object.getString("client_id")
                          );

                          list.add(item);


                        }
                        adapter = new OrderAdapter(list,cons);
                        recyclerView.setAdapter(adapter);
                        noavailable.setVisibility(View.GONE);


                    }
                    else{
                        noavailable.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        con_orderlist get = new con_orderlist(function.getUID(),response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(cons);
        queue.add(get);
    }
}
