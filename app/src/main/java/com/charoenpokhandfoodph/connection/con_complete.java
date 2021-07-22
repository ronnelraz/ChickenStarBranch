package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_complete extends StringRequest {

    public static final String con = config.URL+"accept_done.php";
    private Map<String,String> params;

    public con_complete(String tid, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("id",tid);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
