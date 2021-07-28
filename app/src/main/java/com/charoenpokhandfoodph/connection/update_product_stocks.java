package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class update_product_stocks extends StringRequest {

    public static final String con = config.URL+"update_product_qty.php";
    private Map<String,String> params;

    public update_product_stocks(String id,String qty, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("id",id);
        params.put("value",qty);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
