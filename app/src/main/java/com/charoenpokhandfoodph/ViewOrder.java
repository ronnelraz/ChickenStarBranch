package com.charoenpokhandfoodph;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.adapter.OrderViewListAdapter;
import com.charoenpokhandfoodph.connection.con_accept_order;
import com.charoenpokhandfoodph.connection.con_complete;
import com.charoenpokhandfoodph.connection.con_customer_info;
import com.charoenpokhandfoodph.connection.con_orderviewlist;
import com.charoenpokhandfoodph.connection.con_deliver_order;
import com.charoenpokhandfoodph.connection.con_process_order;
import com.charoenpokhandfoodph.modal.order_view_list;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hari.bounceview.BounceView;

import static com.charoenpokhandfoodph.function.getInstance;

public class ViewOrder extends AppCompatActivity {

    private static LinearLayout action_container,process_container;
    private static MaterialButton prepare,deliver,complete;
    private TextView customerName,transactionNumber;
    private MaterialToolbar toolbar;
    public static TextView km,SubTotal,DeliveryFee,grandtotal;

    private static CardView orderinfomation;

    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<order_view_list> list;

    public static TextView textView;

    public static String stc_customerName,stc_transctionNumber,stc_sub,stc_total,stc_km,stc_fee,stc_user_id,stc_tid,stc_customer_number;

    private String name = "";
    private String contact = "";
    private String address = "";


    private static MaterialButton reject,accept;
    private static ProgressBar actionloading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        //toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            function.intent(Home.class,this);
            function.animIntent(this,config.ltr);
            finish();
        });

        action_container = findViewById(R.id.action_container);
        process_container = findViewById(R.id.process_container);

        textView = findViewById(R.id.orderinstruction);

        //action
        reject = findViewById(R.id.reject);
        accept = findViewById(R.id.accept);
        actionloading = findViewById(R.id.actionloading);


        //button process
        prepare = findViewById(R.id.prepare);
        deliver = findViewById(R.id.deliver);
        complete = findViewById(R.id.complete);
        //header
        customerName = findViewById(R.id.customerName);
        customerName.setText(stc_customerName);
        transactionNumber = findViewById(R.id.transactionNumber);
        transactionNumber.setText(stc_transctionNumber);
        //summary
        km = findViewById(R.id.km);
        km.setText(stc_km);
        SubTotal = findViewById(R.id.SubTotal);
        DeliveryFee = findViewById(R.id.DeliveryFee);
        DeliveryFee.setText("₱"+stc_fee);
        grandtotal = findViewById(R.id.grandtotal);
        grandtotal.setText("₱"+stc_total);

        //recyclerview
        recyclerView = findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderViewListAdapter(list,this);
        recyclerView.setAdapter(adapter);

        //cardview special order
        orderinfomation = findViewById(R.id.orderinfomation);


        process_button_orders();
        loadData(stc_user_id,stc_tid,getBaseContext());

    }

    protected void process_button_orders(){
        prepare.setOnClickListener(v -> {
            function.getInstance(v.getContext()).Preloader(v.getContext());
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if(success){
                            getInstance(v.getContext()).alertDialog.dismiss();
                            prepare.setText("Prepared");
                            prepare.setBackgroundColor(Color.parseColor("#4CAF50"));
                            prepare.setIconResource(R.drawable.icons8_ok);
                            prepare.setIconTint(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                            prepare.setTextColor(Color.parseColor("#ffffff"));
                            deliver.setEnabled(true);
                            deliver.setBackgroundColor(Color.parseColor("#FF9800"));
                            prepare.setEnabled(false);

                        }
                        else{
                            getInstance(v.getContext()).alertDialog.dismiss();
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
            con_process_order get = new con_process_order(stc_transctionNumber,response,errorListener);
            get.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(get);
        });

        deliver.setOnClickListener(v -> {

            function.getInstance(v.getContext()).Preloader(v.getContext());
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if(success){
                            getInstance(v.getContext()).alertDialog.dismiss();
                            deliver.setText("On the Way");
                            deliver.setTextSize(10);
                            deliver.setBackgroundColor(Color.parseColor("#4CAF50"));
                            deliver.setIconResource(R.drawable.icons8_ok);
                            deliver.setIconTint(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                            deliver.setTextColor(Color.parseColor("#ffffff"));
                            deliver.setEnabled(false);
                            prepare.setEnabled(false);
                            complete.setEnabled(true);
                            complete.setBackgroundColor(Color.parseColor("#FF9800"));

                        }
                        else{
                            getInstance(v.getContext()).alertDialog.dismiss();
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
            con_deliver_order get = new con_deliver_order(stc_transctionNumber,response,errorListener);
            get.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(get);

        });

        complete.setOnClickListener(v -> {
            function.getInstance(v.getContext()).Preloader(v.getContext());
            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if(success){
                            getInstance(v.getContext()).alertDialog.dismiss();
                            complete.setText("Completed");
                            complete.setBackgroundColor(Color.parseColor("#4CAF50"));
                            complete.setTextColor(Color.parseColor("#ffffff"));
                            complete.setIconResource(R.drawable.icons8_ok);
                            complete.setIconTint(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                            function.intent(Home.class,v.getContext());
                            function.animIntent(v.getContext(),config.rtl);

                        }
                        else{
                            getInstance(v.getContext()).alertDialog.dismiss();
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
            con_complete get = new con_complete(stc_transctionNumber,response,errorListener);
            get.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(get);


        });
    }

    public void reject(View view) {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to Reject this order?")
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();

    }

    public void accept(View view) {

        new SweetAlertDialog(view.getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(R.drawable.icons8_ok_2)
                .setTitleText("Accept Now?")
                .setConfirmText("Accept!")
                .setConfirmButtonBackgroundColor(Color.parseColor("#27ae60"))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        function.getInstance(view.getContext()).Preloader(view.getContext());

                        Response.Listener<String> response = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if(success){
                                        getInstance(view.getContext()).alertDialog.dismiss();
                                        action_container.setVisibility(View.GONE);
                                        process_container.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        getInstance(view.getContext()).alertDialog.dismiss();
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
                        con_accept_order get = new con_accept_order(stc_transctionNumber,response,errorListener);
                        get.setRetryPolicy(new DefaultRetryPolicy(
                                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        queue.add(get);
                    }
                })
                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();





//        function.toast(view.getContext(),stc_transctionNumber);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ordermenu, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_orderinfo);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onOptionsItemSelected(menuItem);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_orderinfo: {
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
                                    name = object.getString("name");
                                    contact = object.getString("contact");
                                    address = object.getString("address");
                                }



                                AlertDialog.Builder details = new AlertDialog.Builder(ViewOrder.this);
                                View view = LayoutInflater.from(ViewOrder.this).inflate(R.layout.customer_info, null);
                                TextView setcustomername = view.findViewById(R.id.customername);
                                TextView setaddress = view.findViewById(R.id.address);
                                TextView setcontact = view.findViewById(R.id.contact);
                                MaterialButton ok = view.findViewById(R.id.ok);




                                setcustomername.setText(name);
                                setaddress.setText(address);
                                setcontact.setText(contact);
                                details.setView(view);
                                AlertDialog alert = details.create();

                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alert.dismiss();

                                    }
                                });

                                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                alert.setCanceledOnTouchOutside(false);
                                alert.setCancelable(true);
                                BounceView.addAnimTo(alert);
                                alert.show();



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
                con_customer_info get = new con_customer_info(stc_user_id,response,errorListener);
                get.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(get);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    //load order
    public static void loadData(String user_id,String tid,Context context){
        list.clear();
        reject.setEnabled(false);
        accept.setEnabled(false);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
//                    String[] feex = jsonResponse.getString("charge").split("\\.");
                    String fee = jsonResponse.getString("charge");
                    int subtotal = jsonResponse.getInt("subtol");
                    SubTotal.setText("₱"+function.format_number(subtotal));

                    int newfree = fee.isEmpty() ? 0 : Integer.valueOf(jsonResponse.getString("charge").split("\\.")[0]);

                    int newTotal = subtotal + newfree;
                    ViewOrder.grandtotal.setText("₱"+function.format_number(newTotal));

                    String specialOrder = jsonResponse.getString("remarks");
                    String actionButton = jsonResponse.getString("action");
                    String processButton = jsonResponse.getString("processButton");

                    if(actionButton.equals("ACCEPTED")){
                        action_container.setVisibility(View.GONE);
                        process_container.setVisibility(View.VISIBLE);
                    }
                    else{
                        action_container.setVisibility(View.VISIBLE);
                        process_container.setVisibility(View.GONE);
                    }

                    if(processButton.equals("PROCESSED")){
                        prepare.setText("Prepared");
                        prepare.setBackgroundColor(Color.parseColor("#4CAF50"));
                        prepare.setIconResource(R.drawable.icons8_ok);
                        prepare.setIconTint(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                        prepare.setTextColor(Color.parseColor("#ffffff"));
                        deliver.setEnabled(true);
                        deliver.setBackgroundColor(Color.parseColor("#FF9800"));
                        prepare.setEnabled(false);
                    }
                    else if(processButton.equals("PREPARED")){
                        prepare.setText("Prepared");
                        prepare.setBackgroundColor(Color.parseColor("#4CAF50"));
                        prepare.setIconResource(R.drawable.icons8_ok);
                        prepare.setIconTint(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                        prepare.setTextColor(Color.parseColor("#ffffff"));
                        deliver.setEnabled(true);
                        deliver.setBackgroundColor(Color.parseColor("#FF9800"));
                        prepare.setEnabled(false);

                        deliver.setText("On the Way");
                        deliver.setTextSize(10);
                        deliver.setBackgroundColor(Color.parseColor("#4CAF50"));
                        deliver.setIconResource(R.drawable.icons8_ok);
                        deliver.setIconTint(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                        deliver.setTextColor(Color.parseColor("#ffffff"));
                        deliver.setEnabled(false);
                        prepare.setEnabled(false);
                        complete.setEnabled(true);
                        complete.setBackgroundColor(Color.parseColor("#FF9800"));
                    }


                    if(specialOrder.isEmpty()){
                        orderinfomation.setVisibility(View.GONE);
                    }
                    else{
                        orderinfomation.setVisibility(View.VISIBLE);
                        textView.setText(specialOrder);
                    }

                    JSONArray array = jsonResponse.getJSONArray("data");

                    if(success){
                        actionloading.setVisibility(View.GONE);
                        reject.setEnabled(true);
                        accept.setEnabled(true);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            order_view_list item = new order_view_list(
                                    object.getString("order_id"),
                                    object.getString("product_id"),
                                    object.getString("img"),
                                    object.getString("name"),
                                    object.getString("price"),
                                    object.getString("tag"),
                                    object.getString("total_qty"),
                                    object.getString("qty"),
                                    object.getString("status"),
                                    object.getString("datetime"),
                                    object.getString("contact"),
                                    object.getString("complete"),
                                    object.getString("statusHeader"),
                                    object.getString("percent"),
                                    object.getString("newprice"),
                                    object.getString("promotion_status"),
                                    object.getString("promotion_code")

                            );
                            list.add(item);
                        }
                        adapter = new OrderViewListAdapter(list,context);
                        recyclerView.setAdapter(adapter);


                    }
                    else{

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
        con_orderviewlist get = new con_orderviewlist(user_id,tid,response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(get);
    }

    public void close(View view) {
        orderinfomation.setVisibility(View.GONE);
    }
}