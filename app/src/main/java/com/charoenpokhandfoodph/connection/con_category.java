package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_category extends StringRequest {

    public static final String con = config.URL+"_mobile_inventory_category.php";
    private Map<String,String> params;

    public con_category(String uid, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("uid",uid);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
