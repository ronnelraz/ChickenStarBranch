package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_findmyaccount extends StringRequest {

    public static final String con = config.URL+"findmyaccount.php";
    private Map<String,String> params;

    public con_findmyaccount(String username,String contact, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("username",username);
        params.put("contact",contact);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
