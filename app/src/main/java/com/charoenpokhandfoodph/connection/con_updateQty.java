package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_updateQty extends StringRequest {

    public static final String con = config.URL+"_mobile_updateorder_new.php";
    private Map<String,String> params;

    public con_updateQty(String order_id, String total, String qty, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("order_id",order_id);
        params.put("total",total);
        params.put("qty",qty);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
