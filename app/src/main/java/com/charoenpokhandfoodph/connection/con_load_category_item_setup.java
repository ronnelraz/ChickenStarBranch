package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_load_category_item_setup extends StringRequest {

    public static final String con = config.URL+"loaditemcategory.php";
    private Map<String,String> params;

    public con_load_category_item_setup(String id,String bid,Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("id",id);
        params.put("bid",bid);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
