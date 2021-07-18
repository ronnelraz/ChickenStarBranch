package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_login extends StringRequest {

    public static final String con = config.URL+"_mobile_login.php";
    private Map<String,String> params;

    public con_login(String username, String password, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
        params.put("OTP","1234");
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
