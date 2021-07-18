package com.charoenpokhandfoodph;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.charoenpokhandfoodph.Fragment.account;
import com.charoenpokhandfoodph.Fragment.completed;
import com.charoenpokhandfoodph.Fragment.order;
import com.charoenpokhandfoodph.Fragment.product;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.charoenpokhandfoodph.function.animIntent;
import static maes.tech.intentanim.CustomIntent.customType;

public class Home extends AppCompatActivity {



    TextView textCartItemCount;
    int mCartItemCount = 10;

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    public static FragmentManager fragmentManager;
    public static FrameLayout container;

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

        drawableMenu();
        FragmentActivity(new order());


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


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.order:
                        FragmentActivity(new order());
                        navigationView.setCheckedItem(item.getItemId());
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
        setupBadge();
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

    private void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}