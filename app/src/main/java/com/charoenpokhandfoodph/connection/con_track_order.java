package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_track_order extends StringRequest {

    public static final String con = config.URL+"_mobile_track_order_admin.php";
    private Map<String,String> params;

    public con_track_order(String tid,String status, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("tid",tid);
        params.put("status",status);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
