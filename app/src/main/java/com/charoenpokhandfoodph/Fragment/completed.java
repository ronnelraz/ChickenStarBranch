package com.charoenpokhandfoodph.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.adapter.OrderAdapter;
import com.charoenpokhandfoodph.adapter.completedAdapter;
import com.charoenpokhandfoodph.connection.con_loadCompletedOrder;
import com.charoenpokhandfoodph.connection.con_orderlist;
import com.charoenpokhandfoodph.function;
import com.charoenpokhandfoodph.modal.completedlist;
import com.charoenpokhandfoodph.modal.orderlist;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class completed extends Fragment {


    private MaterialTextView date_to,date_from;
    final Calendar myCalendar = Calendar.getInstance();
    private MaterialButton filterdate;
    protected String stc_date_to,stc_date_from;

    private TextInputEditText search;


    public static SwipeRefreshLayout swipe;
    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<completedlist> list;
    public static ImageView noavailable;

    private static TextView completed,cancelled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.frag_completed,parent,false);
        date_to = view.findViewById(R.id.date_to);
        date_from = view.findViewById(R.id.date_from);
        date_to.setText(getDate());
        date_from.setText(getDate());
        filterdate = view.findViewById(R.id.filterdate);

        completed = view.findViewById(R.id.completed);
        cancelled = view.findViewById(R.id.Cancelled);

        swipe = view.findViewById(R.id.swipe);
        recyclerView = view.findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        noavailable = view.findViewById(R.id.noAvailable);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new completedAdapter(list,getActivity());
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
        return view;
    }

    public void onLoadComplete(){
        loadData(getContext(),stc_date_to,stc_date_from);
        swipe.setRefreshing(false);
    }

    protected void ActionsTop(){

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<completedlist> newList = new ArrayList<>();
                for (completedlist sub : list)
                {
                    String name = sub.getName().toLowerCase();
                    String TransactionNumber = sub.getTransacid().toLowerCase();
                    if(name.contains(s)){
                        newList.add(sub);
                    }
                    else if(TransactionNumber.contains(s)){
                        newList.add(sub);
                    }
                    adapter = new completedAdapter(newList, getActivity());
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        date_to.setOnClickListener(v -> {
            new DatePickerDialog(getActivity(),R.style.picker,getDateto(), myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        date_from.setOnClickListener(v -> {
            new DatePickerDialog(getActivity(),R.style.picker, getDatefrom(), myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        stc_date_to = getDateFilter();
        stc_date_from = getDateFilter();

        loadData(getContext(),stc_date_to,stc_date_from);

        filterdate.setOnClickListener(v -> {
            String to = stc_date_to;
            String from = stc_date_from;
            list.clear();
            adapter.notifyDataSetChanged();
            loadData(getActivity(),to,from);
        });
    }
    public static void loadData(Context cons,String to,String from){
        list.clear();
        adapter.notifyDataSetChanged();
        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");
                String getcompleted = String.valueOf(jsonResponse.getInt("completed"));
                String getcancelled = String.valueOf(jsonResponse.getInt("reject"));

                completed.setText("Completed Order : " + getcompleted);
                cancelled.setText("Cancelled Order : " + getcancelled);
                JSONArray array = jsonResponse.getJSONArray("data");

                if(success){

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        completedlist item = new completedlist(
                                object.getString("id"),
                                object.getString("name"),
                                object.getString("client_id"),
                                object.getString("transacid"),
                                object.getString("status"),
                                object.getString("statusHeader"),
                                object.getString("datetime")
                        );

                        list.add(item);


                    }
                    adapter = new completedAdapter(list,cons);
                    recyclerView.setAdapter(adapter);
                    noavailable.setVisibility(View.GONE);
                }
                else{
                    noavailable.setVisibility(View.VISIBLE);
                    function.noti(cons,"No Data Available","To : " + to + " From : " + from,R.drawable.nofilter);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_loadCompletedOrder get = new con_loadCompletedOrder(function.getUID(),to,from,response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(cons);
        queue.add(get);
    }





    private DatePickerDialog.OnDateSetListener getDateto(){
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            labelTo();
        };
        return date;
    }

    private DatePickerDialog.OnDateSetListener getDatefrom(){
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            labelFrom();
        };
        return date;
    }


    private void labelTo() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_to.setText(sdf.format(myCalendar.getTime()));

        String FilterFormat = "yyyy-M-d";
        SimpleDateFormat sdffilter = new SimpleDateFormat(FilterFormat, Locale.US);
//        function.toast(getActivity(),sdffilter.format(myCalendar.getTime()));
         stc_date_to = sdffilter.format(myCalendar.getTime());
    }


    private void labelFrom() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_from.setText(sdf.format(myCalendar.getTime()));

        String FilterFormat = "yyyy-M-d";
        SimpleDateFormat sdffilter = new SimpleDateFormat(FilterFormat, Locale.US);
//        function.toast(getActivity(),sdffilter.format(myCalendar.getTime()));
        stc_date_from = sdffilter.format(myCalendar.getTime());
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }


    private String getDateFilter() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
