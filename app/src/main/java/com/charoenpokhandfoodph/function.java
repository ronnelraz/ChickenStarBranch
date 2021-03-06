package com.charoenpokhandfoodph;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.animation.content.Content;
import com.charoenpokhandfoodph.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tapadoo.alerter.Alerter;

import java.text.DecimalFormat;
import java.util.Random;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import hari.bounceview.BounceView;


import static maes.tech.intentanim.CustomIntent.customType;

public class function {


    public static function app;
    public static Context cont;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static final String DATA = "data";
    public static final String LOGIN = "Login";
    public static final String FULLNAME = "NAME";
    public static final String ADDRESS = "ADDRESs";
    public static final String UID = "UID";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORd";
    public static final String CONTACT = "CONTACT";
    public static final String COUNTORDER = "0";

    public static String ACTIVITY = "0";



    public MaterialAlertDialogBuilder dialogBuilder;
    public AlertDialog alertDialog;

    public function(Context context){
        cont = context;
    }

    public static synchronized function getInstance(Context context){
        if(app == null){
            app = new function(context);
        }
        return app;
    }


    public static String getCountorder(){
        sharedPreferences = cont.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(COUNTORDER,"0");
    }



    public static boolean setCountorder(String order){
        sharedPreferences = cont.getSharedPreferences(DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(COUNTORDER,order);
        editor.apply();
        return true;
    }



    public static String getActivity(){
        sharedPreferences = cont.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ACTIVITY,"0");
    }

    public static boolean setActivity(String number){
        sharedPreferences = cont.getSharedPreferences(DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(ACTIVITY,number);
        editor.apply();
        return true;
    }



    public static String getPhone(){
        sharedPreferences = cont.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CONTACT,"0");
    }

    public static boolean setPhone(String phone){
        sharedPreferences = cont.getSharedPreferences(DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(CONTACT,phone);
        editor.apply();
        return true;
    }



    public static void ErrorMsg(Context context,String Title,String content){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(Title)
                .setContentText(content)
                .show();
    }

   


    public static String format_number(int number){
        DecimalFormat formatter = new DecimalFormat("#,###,###.00");
        return formatter.format(number);
    }

    public static String getFullname(){
        sharedPreferences = cont.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(FULLNAME,"username");
    }
    public static String getAddress(){
        sharedPreferences = cont.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ADDRESS,"Address");
    }

    public static String getUID(){
        sharedPreferences = cont.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(UID,"Address");
    }

    public static String ifLogin(){
        sharedPreferences = cont.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LOGIN,null);
    }


    public boolean passwordValidator(String password){
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if(password.matches(pattern)){
            return true;
        }
        else{
            return false;
        }
    }


    public void Preloader(Context context){
        dialogBuilder = new MaterialAlertDialogBuilder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.loading,null);
        dialogBuilder.setView(v);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    public static boolean islogin(String login){
        sharedPreferences = cont.getSharedPreferences(DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(LOGIN,login);
        editor.apply();
        return true;
    }


    public static boolean setLogin(String login,String name,String address,String uid,String username,String password){
        sharedPreferences = cont.getSharedPreferences(DATA,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(LOGIN,login);
        editor.putString(FULLNAME,name);
        editor.putString(ADDRESS,address);
        editor.putString(UID,uid);
        editor.putString(USERNAME,username);
        editor.putString(PASSWORD,password);
        editor.apply();
        return true;
    }


    public static void intent(Class<?> activity, Context context){
        Intent i = new Intent(context,activity);
        context.startActivity(i);
    }

    public static void animIntent(Context context,String type){
        customType(context,type);
    }


    public static void notificationAlert(Activity activity, String msg,int icon,String color){
        Alerter.create(activity)
                .setText(msg)
                .setIcon(icon)
                .setIconColorFilter(0)
                .setBackgroundColorInt(Color.parseColor(color)) // or setBackgroundColorInt(Color.CYAN)// Optional - default is 38dp
                .show();
    }


    public static void noti(Context context,String msg,String body,int icon){
//        MaterialButton button = new MaterialButton(context);
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(msg)
                .setContentText(body)
                .setCustomImage(icon)
                .showCancelButton(false)
                .showCancelButton(false)
                .show();

    }




    public static String phoneMask(String phone) {
        String res = "";
        StringBuilder stringBuilder = new StringBuilder(phone);
        res = stringBuilder.replace(3, 7, "****").toString();
        return res;
    }





    public static void toast(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }



}
