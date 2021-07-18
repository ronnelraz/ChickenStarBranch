package com.charoenpokhandfoodph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.Fragment.account;
import com.charoenpokhandfoodph.Fragment.completed;
import com.charoenpokhandfoodph.Fragment.order;
import com.charoenpokhandfoodph.Fragment.product;
import com.charoenpokhandfoodph.adapter.OrderAdapter;
import com.charoenpokhandfoodph.connection.con_orderlist;
import com.charoenpokhandfoodph.modal.orderlist;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.charoenpokhandfoodph.function.animIntent;
import static com.charoenpokhandfoodph.function.islogin;
import static com.charoenpokhandfoodph.function.toast;
import static maes.tech.intentanim.CustomIntent.customType;

public class Home extends AppCompatActivity {

    TextView textCartItemCount;


    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    public static FragmentManager fragmentManager;
    public static FrameLayout container;

    Handler handler;

    TextView fullname,address;
    MaterialLetterIcon iconx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nvView);
        container = findViewById(R.id.flContent);

        function.getInstance(this);

        drawableMenu();
        FragmentActivity(new order());

        loadData();
        handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                loadData();
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(r, 2000);


    }


    protected  void drawableMenu(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.parseColor("#ffffff"));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //fragment
//        fragmentManager = getSupportFragmentManager();
//        setFrag(new Tab1());


        View headerView = navigationView.getHeaderView(0);
        fullname = headerView.findViewById(R.id.fullname);
        address = headerView.findViewById(R.id.address);
        iconx = headerView.findViewById(R.id.iconx);
        iconx.setShapeColor(Color.parseColor("#e74c3c"));

        String[] getfname = function.getFullname().split(" ");
        String s=getfname[1].substring(0,1);
        iconx.setLetter(s);


        fullname.setText(function.getFullname());
        address.setText(function.getAddress());





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.order:
                        FragmentActivity(new order());
                        navigationView.setCheckedItem(item.getItemId());
                        loadData();
                        break;
                    case R.id.completed:
                        FragmentActivity(new completed());
                        navigationView.setCheckedItem(item.getItemId());
                        break;
                    case R.id.product:
                        FragmentActivity(new product());
                        navigationView.setCheckedItem(item.getItemId());
                        break;
                    case R.id.account:
                        FragmentActivity(new account());
                        navigationView.setCheckedItem(item.getItemId());
                        break;

                    case R.id.logout:
                        new SweetAlertDialog(Home.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("You want to logout your account?")
                                .setConfirmText("Yes")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        islogin("false");
                                        function.intent(Login.class,Home.this);
                                        animIntent(Home.this,config.rtl);
                                    }
                                })
                                .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                        break;
                }

                return true;
            }
        });

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = menuItem.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    private void FragmentActivity(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent,fragment).commit();
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START,true);
        }
    }

    private void loadData(){
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    JSONArray array = jsonResponse.getJSONArray("data");

                    if(success){

                        if(function.getCountorder().equals(String.valueOf(array.length()))){
                            function.setCountorder(String.valueOf(array.length()));
                            textCartItemCount.setText(String.valueOf(Math.min(array.length(), 99)));

                        }

                        else{
                            textCartItemCount.setText(String.valueOf(Math.min(array.length(), 99)));
                            if(array.length() <=  Integer.parseInt(function.getCountorder())){
                                order.loadData(getApplicationContext());
                                function.setCountorder(String.valueOf(array.length()));
                            }
                            else{
                                function.setCountorder(String.valueOf(array.length()));
                                Log.d("notify","webhook");
                                virateme();
                                order.loadData(getApplicationContext());
                                function.notificationAlert(Home.this,"You Received new Order",R.drawable.onlineshop2,"#2ecc71");
                            }

                        }

                    }
                    else{
                        textCartItemCount.setText("0");
                        function.setCountorder("0");
                        order.loadData(getApplicationContext());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        con_orderlist get = new con_orderlist(function.getUID(),response,null);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(get);
    }




    protected void virateme(){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.notify);
        mp.start();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_cart: {
                FragmentActivity(new order());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
       if(drawerLayout.isDrawerOpen(GravityCompat.START)){
           drawerLayout.closeDrawer(GravityCompat.START);
       }
       else{
           super.onBackPressed();
       }
    }


}