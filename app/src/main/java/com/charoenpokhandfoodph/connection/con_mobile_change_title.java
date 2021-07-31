package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_mobile_change_title extends StringRequest {

    public static final String con = config.URL+"_mobile_change_title.php";
    private Map<String,String> params;

    public con_mobile_change_title(String id,String title, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("id",id);
        params.put("title",title);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
