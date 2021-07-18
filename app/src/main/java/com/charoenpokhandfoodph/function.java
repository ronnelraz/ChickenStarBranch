package com.charoenpokhandfoodph;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import com.airbnb.lottie.animation.content.Content;
import com.tapadoo.alerter.Alerter;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static maes.tech.intentanim.CustomIntent.customType;

public class function {

    public static void intent(Class<?> activity, Context context){
        Intent i = new Intent(context,activity);
        context.startActivity(i);
    }

    public static void animIntent(Context context,String type){
        customType(context,type);
    }


    public static void notificationAlert(Activity activity, String msg,int icon,String color){
        Alerter.create(activity)
                .setText("Alert text...")
                .setIcon(icon)
                .setIconColorFilter(0) // Optional - Removes white tint
                .setBackgroundColorInt(Color.parseColor(color)) // or setBackgroundColorInt(Color.CYAN)// Optional - default is 38dp
                .show();
    }


    public static void toast(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }



}
