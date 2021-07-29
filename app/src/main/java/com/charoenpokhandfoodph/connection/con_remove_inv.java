package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_remove_inv extends StringRequest {

    public static final String con = config.URL+"remove_inv.php";
    private Map<String,String> params;

    public con_remove_inv(String pid, String bid,  Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("productID",pid);
        params.put("businessID",bid);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
