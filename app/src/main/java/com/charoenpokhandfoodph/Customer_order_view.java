package com.charoenpokhandfoodph;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.adapter.CustomerOrderViewAdapater;
import com.charoenpokhandfoodph.adapter.OrderViewListAdapter;
import com.charoenpokhandfoodph.connection.con_customer_info;
import com.charoenpokhandfoodph.connection.con_customer_list;
import com.charoenpokhandfoodph.connection.con_orderviewlist;
import com.charoenpokhandfoodph.connection.con_track_order;
import com.charoenpokhandfoodph.modal.customer_view_list;
import com.charoenpokhandfoodph.modal.order_view_list;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hari.bounceview.BounceView;

public class Customer_order_view extends AppCompatActivity {

    public static String stc_tid,stc_customer_id,stc_status,stc_customer_name;
    private MaterialToolbar toolbar;
    private String name = "";
    private String contact = "";
    private String address = "";

    private TextView status;

    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter adapter;
    public static List<customer_view_list> list;

    private static double totalCancel,totalComplete = 0;
    private static TextView SubTotal,km,DeliveryFee,grandtotal;
    private static Boolean ismeron = false;
    private static String Remarks;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        toolbar.setTitle(stc_customer_name);
        toolbar.setSubtitle(stc_tid);
        //recyclerview
        recyclerView = findViewById(R.id.data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(999999999);
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerOrderViewAdapater(list,this);
        recyclerView.setAdapter(adapter);


        SubTotal = findViewById(R.id.SubTotal);
        km = findViewById(R.id.km);
        DeliveryFee = findViewById(R.id.DeliveryFee);
        grandtotal = findViewById(R.id.grandtotal);

        loadData();



        status = findViewById(R.id.status);
        if(stc_status.equals("DONE")){
            status.setBackgroundColor(Color.parseColor("#27ae60"));
            status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#27ae60")));
            status.setText("ORDER STATUS : COMPLETED");
        }
        else{
            status.setBackgroundColor(Color.parseColor("#e74c3c"));
            status.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e74c3c")));
            status.setText("ORDER STATUS : CANCELLED");
        }


    }


    //load order
    public void loadData(){
        list.clear();
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String fee = jsonResponse.getString("charge");
                    int subtotal = jsonResponse.getInt("subtol");
                    String specialOrder = jsonResponse.getString("remarks");
                    String getkm = jsonResponse.getString("km");
                    DeliveryFee.setText("₱"+function.format_number((int)Double.parseDouble(fee)));
                    km.setText(getkm);
                    JSONArray array = jsonResponse.getJSONArray("data");

                    ismeron = specialOrder.isEmpty() ? false : true;
                    Remarks = specialOrder;

                    if(success){
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            if(stc_status.equals("DONE")){
                                if(object.getString("status").equals("completed")){
                                    totalComplete += Double.parseDouble(object.getString("total_qty"));
                                    SubTotal.setText("₱"+function.format_number((int) totalComplete));
                                }

                                int grandtol = ((int)totalComplete) + (int)Double.parseDouble(fee);
                                grandtotal.setText("₱"+function.format_number(grandtol));

                            }
                            else{
                                if(object.getString("status").equals("cancel")){
                                    totalCancel += Double.parseDouble(object.getString("total_qty"));
                                    SubTotal.setText("₱"+function.format_number((int) totalCancel));
                                }

                                int grandtol = ((int)totalCancel) + (int)Double.parseDouble(fee);
                                grandtotal.setText("₱"+function.format_number(grandtol));
                            }




                            customer_view_list item = new customer_view_list(
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
                        adapter = new CustomerOrderViewAdapater(list,getApplicationContext());
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
        con_customer_list get = new con_customer_list(stc_customer_id,stc_tid,response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(get);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_order_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_orderinfo);
        menuItem.setOnMenuItemClickListener(item -> {
            onOptionsItemSelected(menuItem);
            return true;
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_orderinfo:
                function.getInstance(this).Preloader(this);
                Response.Listener<String> response = response1 -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response1);
                        boolean success = jsonResponse.getBoolean("success");
                        JSONArray array = jsonResponse.getJSONArray("data");

                        if(success){
                            function.getInstance(this).alertDialog.dismiss();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                name = object.getString("name");
                                contact = object.getString("contact");
                                address = object.getString("address");
                            }



                            AlertDialog.Builder details = new AlertDialog.Builder(Customer_order_view.this);
                            View view = LayoutInflater.from(Customer_order_view.this).inflate(R.layout.customer_info, null);
                            TextView setcustomername = view.findViewById(R.id.customername);
                            TextView setaddress = view.findViewById(R.id.address);
                            TextView setcontact = view.findViewById(R.id.contact);
                            MaterialButton ok = view.findViewById(R.id.ok);




                            setcustomername.setText(name);
                            setaddress.setText(address);
                            setcontact.setText(contact);
                            details.setView(view);
                            AlertDialog alert = details.create();

                            ok.setOnClickListener(v -> {
                                alert.dismiss();
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
                };
                Response.ErrorListener errorListener = error -> {

                };
                con_customer_info get = new con_customer_info(stc_customer_id,response,errorListener);
                get.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(get);
                return true;

            case R.id.track_order:
                    trackOrder();
                break;

            case R.id.order_info:
                String getinstruction = Remarks.isEmpty() ? "No Special Instruction" : Remarks;
               new SweetAlertDialog(this,SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                       .setTitleText("Order Instruction")
                       .setContentText(getinstruction)
                       .setCustomImage(R.drawable.icons8_info_1)
                       .showCancelButton(false)
                       .showCancelButton(false)
                       .show();
               break;
            }



        return super.onOptionsItemSelected(item);
    }


    protected void trackOrder(){

        Response.Listener<String> response = response1 -> {
            try {
                JSONObject jsonResponse = new JSONObject(response1);
                boolean success = jsonResponse.getBoolean("success");
                JSONArray array = jsonResponse.getJSONArray("data");

                if(success){
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        View view = getLayoutInflater().inflate(R.layout.bottomsheet_track_order,null);
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,R.style.BottomSheetDialog);


                        LinearLayout linearLayout = view.findViewById(R.id.root);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                        params.height = getScreenHeight();
                        linearLayout.setLayoutParams(params);


                        MaterialButton close = view.findViewById(R.id.close);
                        close.setOnClickListener(v -> {
                            bottomSheetDialog.dismiss();
                        });

                        TextView order_completed = view.findViewById(R.id.order_completed);
                        ImageView status_icon_complete = view.findViewById(R.id.status_icon_complete);
                        ImageView complete_icon = view.findViewById(R.id.complete_icon);
                        TextView complete_status = view.findViewById(R.id.complete_status);

                        if(stc_status.equals("DONE")){
                            View v2 = view.findViewById(R.id.v2);
                            View v3 = view.findViewById(R.id.v3);
                            View v4 = view.findViewById(R.id.v4);
                            v2.setVisibility(View.VISIBLE);
                            v3.setVisibility(View.VISIBLE);
                            v4.setVisibility(View.VISIBLE);
                            status_icon_complete.setImageResource(R.drawable.icons8_ok_2);
                            order_completed.setText(object.getString("complete_time"));
                            complete_status.setText("Order Completed");
                        }
                        else{
                            View v2 = view.findViewById(R.id.v2);
                            View v3 = view.findViewById(R.id.v3);
                            View v4 = view.findViewById(R.id.v4);
                            v2.setVisibility(View.GONE);
                            v3.setVisibility(View.GONE);
                            v4.setVisibility(View.GONE);

                            RelativeLayout accept_container = view.findViewById(R.id.accept_container);
                            RelativeLayout prepare_container = view.findViewById(R.id.prepare_container);
                            RelativeLayout deliver_container = view.findViewById(R.id.deliver_container);

                            accept_container.setVisibility(View.GONE);
                            prepare_container.setVisibility(View.GONE);
                            deliver_container.setVisibility(View.GONE);

                            status_icon_complete.setImageResource(R.drawable.icons8_cancel_4);
                            complete_icon.setImageResource(R.drawable.icons8_fail);
                            order_completed.setText(object.getString("cancel"));
                            complete_status.setText("Order Cancelled");

                        }

                        TextView trasactionNumber = view.findViewById(R.id.transactionNumber);
                        trasactionNumber.setText("Order Number : "+ stc_tid);
                        TextView ordersubmitted = view.findViewById(R.id.ordersubmitted);
                        ordersubmitted.setText(object.getString("order_time"));
                        TextView orderaccepted = view.findViewById(R.id.orderaccepted);
                        orderaccepted.setText(object.getString("accept_time"));
                        TextView order_prepare = view.findViewById(R.id.order_prepare);
                        order_prepare.setText(object.getString("process_prepare_time"));
                        TextView order_deliver = view.findViewById(R.id.order_deliver);
                        order_deliver.setText(object.getString("deliver_time"));


                        bottomSheetDialog.setContentView(view);
                        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        bottomSheetDialog.show();

                        //end
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener errorListener = error -> {

        };
        con_track_order get = new con_track_order(stc_tid,stc_status,response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(get);

    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    @Override
    public void onBackPressed() {
        totalCancel = 0;
        totalComplete = 0;
        ismeron = false;
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();

        } else {

            super.onBackPressed();
        }
        function.animIntent(this,config.rtl);
    }
}