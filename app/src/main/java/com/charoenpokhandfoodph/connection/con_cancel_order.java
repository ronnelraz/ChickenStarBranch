package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_cancel_order extends StringRequest {

    public static final String con = config.URL+"_cancel_orderb.php";
    private Map<String,String> params;

    public con_cancel_order(String order_id,String contact,String name, String phone, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("order_id",order_id);
        params.put("contact",contact);
        params.put("name",name);
        params.put("cellphone",phone);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
