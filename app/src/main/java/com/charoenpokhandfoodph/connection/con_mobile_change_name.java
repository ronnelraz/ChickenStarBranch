package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_mobile_change_name extends StringRequest {

    public static final String con = config.URL+"_mobile_change_name.php";
    private Map<String,String> params;

    public con_mobile_change_name(String id, String fname,String mname,String lname,String exname, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("id",id);
        params.put("fname",fname);
        params.put("mname",mname);
        params.put("lname",lname);
        params.put("exname",exname);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
