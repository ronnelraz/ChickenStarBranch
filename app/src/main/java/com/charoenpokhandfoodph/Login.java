package com.charoenpokhandfoodph;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import com.charoenpokhandfoodph.Home;
import com.charoenpokhandfoodph.R;
import com.charoenpokhandfoodph.config;
import com.charoenpokhandfoodph.connection.con_findmyaccount;
import com.charoenpokhandfoodph.connection.con_login;
import com.charoenpokhandfoodph.connection.con_mobile_change_password;
import com.charoenpokhandfoodph.function;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import hari.bounceview.BounceView;
import in.aabhasjindal.otptextview.OtpTextView;

import static com.charoenpokhandfoodph.function.*;


public class Login extends AppCompatActivity {

    private TextInputEditText username,password;
    private MaterialCheckBox keepmelogin;
    @BindViews({R.id.back,R.id.confirm,R.id.verify,R.id.changepassword})
    MaterialButton[] back,confirm,verify,changepassword;
    @BindViews({R.id.username,R.id.contact,R.id.password,R.id.confirmpassword})
    TextInputEditText[] tusername,tcontact,tpassword,tconfirmpassword;
    @BindViews({R.id.conconfirm,R.id.converify,R.id.connewpassword})
    LinearLayout[] con1,con2,con3;
    @BindView(R.id.code)
    OtpTextView otp;
    AlertDialog dialog;
    private int intcode = 0;
    private CardView frameOne, frameTwo, frameThree, frameFour;
    private boolean isAtLeast8 = false, hasUppercase = false, hasNumber = false, hasSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        keepmelogin = findViewById(R.id.keepmelogin);
        function.getInstance(this);
    }

    public void login(View view) {
        String getusername = username.getText().toString();
        String getpassword = password.getText().toString();
        boolean getchecked = keepmelogin.isChecked();


        if(getusername.isEmpty()){
            function.notificationAlert(Login.this,"Enter your Username",R.drawable.user,"#c0392b");
            username.requestFocus();
        }
        else if(getpassword.isEmpty()){
            function.notificationAlert(Login.this,"Enter your Password",R.drawable.password,"#c0392b");
            password.requestFocus();
        }
        else{
            function.getInstance(this).Preloader(this);
            Login(getusername,getpassword,getchecked);
        }


    }


    public void Login(String username,String password,boolean ischecked){
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String name = jsonResponse.getString("name");
                    String address = jsonResponse.getString("address");
                    String UID = jsonResponse.getString("UID");
                    String Phone = jsonResponse.getString("phone");


                    if(success){
                        String isgetchecked = ischecked == true ? "true" : "false";
                        function.setLogin(isgetchecked,name,address,UID,username,password);
                        islogin(isgetchecked);
                        function.setPhone(Phone);
                        intent(Home.class,Login.this);
                        animIntent(Login.this, config.ltr);
                        getInstance(Login.this).alertDialog.dismiss();
                    }
                    else{
                        getInstance(Login.this).alertDialog.dismiss();
                        function.notificationAlert(Login.this,"Invalid Username and Password",R.drawable.icons8_cancel_1,"#c0392b");
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                function.notificationAlert(Login.this,"could not connect to the server.",R.drawable.icons8_cloud_database,"#c0392b");
            }
        };
        con_login get = new con_login(username,password,response,errorListener);
        get.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(get);
    }

    public void forgot(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.full_screen_dialog);
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.forgotpassword, null);
        ButterKnife.bind(this,view);

        frameOne = view.findViewById(R.id.frameOne);
        frameTwo = view.findViewById(R.id.frameTwo);
        frameThree = view.findViewById(R.id.frameThree);
        frameFour = view.findViewById(R.id.frameFour);

        back[0].setOnClickListener(v1 -> dialog.dismiss());
        confirm[1].setOnClickListener(v1 -> {
            String getusername = tusername[0].getText().toString();
            String getcontact = tcontact[1].getText().toString();

            if(getusername.isEmpty()){
                tusername[0].requestFocus();
                function.toast(v1.getContext(),"Enter your username");
            }
            else if(getcontact.isEmpty()){
                tcontact[1].requestFocus();
                function.toast(v1.getContext(),"Enter your Contact number");
            }
            else{
                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String uid = jsonResponse.getString("uid");
                            int code = jsonResponse.getInt("code");
                            Log.d("CODE",String.valueOf(code));


                            if(success){
                                con1[0].setVisibility(View.GONE);
                                con2[1].setVisibility(View.VISIBLE);
                                intcode = code;
                            }
                            else{
                                new SweetAlertDialog(v1.getContext(),SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Invalid Username or Contact number")
                                        .showCancelButton(false)
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        function.notificationAlert(Login.this,"could not connect to the server.",R.drawable.icons8_cloud_database,"#c0392b");
                    }
                };
                con_findmyaccount get = new con_findmyaccount(getusername,getcontact,response,errorListener);
                get.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(get);
            }
        });
        verify[2].setOnClickListener(v1 ->{
            if(otp.getOTP().equals(String.valueOf(intcode))){
                con3[2].setVisibility(View.VISIBLE);
                con2[1].setVisibility(View.GONE);
            }
            else{
                new SweetAlertDialog(v1.getContext(),SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Invalid Code")
                        .showCancelButton(false)
                        .show();
            }
        });
        changepassword[3].setOnClickListener(v1 -> {
            String getpassword = tpassword[2].getText().toString();
            String getconfirmpassword = tconfirmpassword[3].getText().toString();

            if(getpassword.isEmpty()){
                tpassword[2].requestFocus();
                function.toast(v1.getContext(),"Enter your new password");
            }
            else if(getconfirmpassword.isEmpty()){
                tconfirmpassword[3].requestFocus();
                function.toast(v1.getContext(),"Enter your new password");
            }
            else if(!getconfirmpassword.equals(getpassword)){
                tconfirmpassword[3].requestFocus();
                function.toast(v1.getContext(),"Confirm password not match");
            }
            else{
                Response.Listener<String> responsep = response2 -> {
                    try {
                        JSONObject jsonResponses = new JSONObject(response2);
                        boolean successs = jsonResponses.getBoolean("success");
                        if(successs){

                            new SweetAlertDialog(v.getContext(),SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Changed Successfully!")
                                    .showCancelButton(false)
                                    .show();
                            dialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                Response.ErrorListener errorListener = error -> {

                };
                con_mobile_change_password get = new con_mobile_change_password(UID,getconfirmpassword,responsep,errorListener);
                get.setRetryPolicy(new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(get);
            }

        });
        tpassword[2].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registrationDataCheck(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        builder.setView(view);
        dialog = builder.create();
        BounceView.addAnimTo(dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @SuppressLint("ResourceType")
    private void registrationDataCheck(String password) {


        if (password.length() >= 8) {
            isAtLeast8 = true;
            changepassword[3].setEnabled(true);
            frameOne.setCardBackgroundColor(Color.parseColor(getString(R.color.success_stroke_color)));
        } else {
            changepassword[3].setEnabled(false);
            isAtLeast8 = false;
            frameOne.setCardBackgroundColor(Color.parseColor("#A4AA9D"));

        }
        if (password.matches("(.*[A-Z].*)")) {
            hasUppercase = true;
            changepassword[3].setEnabled(true);
            frameTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.success_stroke_color)));
        } else {
            hasUppercase = false;
            changepassword[3].setEnabled(false);
            frameTwo.setCardBackgroundColor(Color.parseColor("#A4AA9D"));
        }
        if (password.matches("(.*[0-9].*)")) {
            hasNumber = true;
            changepassword[3].setEnabled(true);
            frameThree.setCardBackgroundColor(Color.parseColor(getString(R.color.success_stroke_color)));
        } else {
            changepassword[3].setEnabled(false);
            hasNumber = false;
            frameThree.setCardBackgroundColor(Color.parseColor("#A4AA9D"));
        }
        if (password.matches("^(?=.*[_.()@]).*$")) {
            hasSymbol = true;
            changepassword[3].setEnabled(true);
            frameFour.setCardBackgroundColor(Color.parseColor(getString(R.color.success_stroke_color)));
        } else {
            hasSymbol = false;
            changepassword[3].setEnabled(false);
            frameFour.setCardBackgroundColor(Color.parseColor("#A4AA9D"));
        }


    }

}