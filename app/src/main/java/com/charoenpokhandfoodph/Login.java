package com.charoenpokhandfoodph;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.charoenpokhandfoodph.connection.con_login;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import static com.charoenpokhandfoodph.function.*;


public class Login extends AppCompatActivity {

    private TextInputEditText username,password;
    private MaterialCheckBox keepmelogin;

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


                    if(success){
                        String isgetchecked = ischecked == true ? "true" : "false";
                        function.setLogin(isgetchecked,name,address,UID,username,password);
                        islogin(isgetchecked);
                        intent(Home.class,Login.this);
                        animIntent(Login.this,config.ltr);
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
}