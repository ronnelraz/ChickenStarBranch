package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_mobile_change_contact extends StringRequest {

    public static final String con = config.URL+"_mobile_change_contact.php";
    private Map<String,String> params;

    public con_mobile_change_contact(String id, String number, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("id",id);
        params.put("number",number);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
