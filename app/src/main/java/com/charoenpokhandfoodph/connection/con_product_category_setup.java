package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_product_category_setup extends StringRequest {

    public static final String con = config.URL+"cateSetup.php";
    private Map<String,String> params;

    public con_product_category_setup(Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
