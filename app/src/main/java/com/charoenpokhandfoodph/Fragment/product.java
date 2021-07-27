package com.charoenpokhandfoodph.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.adapter.CategoryAdapter;
import com.charoenpokhandfoodph.adapter.completedAdapter;
import com.charoenpokhandfoodph.connection.con_category;
import com.charoenpokhandfoodph.connection.con_loadCompletedOrder;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.category_view_list;
import com.charoenpokhandfoodph.modal.completedlist;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class product extends Fragment {


    private static final String TAG = "MainActivity";
    private TextInputEditText search;
    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<category_view_list> list;
    public static SwipeRefreshLayout swipe;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.frag_product,parent,false);

        swipe = view.findViewById(R.id.swipe);
        recyclerView = view.findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryAdapter(list,getActivity());
        recyclerView.setAdapter(adapter);

        search = view.findViewById(R.id.search);

        swipe.setOnRefreshListener(() -> {
            list.clear();
            onLoadComplete();
        });

        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        ActionsTop();
        loadData();
        return view;

    }

    public void onLoadComplete(){
        loadData();
        swipe.setRefreshing(false);
    }

    protected void ActionsTop(){

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<category_view_list> newList = new ArrayList<>();
                for (category_view_list sub : list)
                {
                    String name = sub.getName().toLowerCase();
                    if(name.contains(s)){
                        newList.add(sub);
                    }
                    adapter = new CategoryAdapter(newList, getActivity());
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    protected void loadData(){
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

                        category_view_list item = new category_view_list(
                                object.getString("id"),
                                object.getString("name"),
                                object.getString("bg")
                        );

                        list.add(item);


                    }
                    adapter = new CategoryAdapter(list,getActivity());
                    recyclerView.setAdapter(adapter);
                }
                else{
                    function.noti(getActivity(),"No Data Available","No Category Found!",R.drawable.bg);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_category get = new con_category(function.getUID(),response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(get);
    }






}
