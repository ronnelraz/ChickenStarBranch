package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_loadCompletedOrder extends StringRequest {

    public static final String con = config.URL+"_mobile_loadproduct.php";
    private Map<String,String> params;

    public con_loadCompletedOrder(String uid,String to,String from, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("uid",uid);
        params.put("to",to);
        params.put("from",from);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
