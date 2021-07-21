package com.charoenpokhandfoodph;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class ViewOrder extends AppCompatActivity {

    private LinearLayout action_container,process_container;
    private MaterialButton prepare,deliver,complete;
    private TextView customerName,transactionNumber;
    private MaterialToolbar toolbar;
    private TextView km,SubTotal,DeliveryFee,grandtotal;

    public static String stc_customerName,stc_transctionNumber,stc_sub,stc_total,stc_km,stc_fee;

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
        SubTotal.setText("₱"+stc_sub);
        DeliveryFee = findViewById(R.id.DeliveryFee);
        DeliveryFee.setText("₱"+stc_fee);
        grandtotal = findViewById(R.id.grandtotal);
        grandtotal.setText("₱"+stc_total);




        process_button_orders();

    }

    protected void process_button_orders(){
        prepare.setOnClickListener(v -> {
            prepare.setText("Prepared");
            prepare.setBackgroundColor(Color.parseColor("#4CAF50"));
            prepare.setIconResource(R.drawable.icons8_ok);
            prepare.setIconTint(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            prepare.setTextColor(Color.parseColor("#ffffff"));
            deliver.setEnabled(true);
            deliver.setBackgroundColor(Color.parseColor("#FF9800"));
            prepare.setEnabled(false);
        });

        deliver.setOnClickListener(v -> {

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

        });

        complete.setOnClickListener(v -> {
            complete.setText("Completed");
            complete.setBackgroundColor(Color.parseColor("#4CAF50"));
            complete.setTextColor(Color.parseColor("#ffffff"));
            complete.setIconResource(R.drawable.icons8_ok);
            complete.setIconTint(ColorStateList.valueOf(Color.parseColor("#ffffff")));

        });
    }

    public void reject(View view) {

    }

    public void accept(View view) {
        action_container.setVisibility(View.GONE);
        process_container.setVisibility(View.VISIBLE);
    }
}