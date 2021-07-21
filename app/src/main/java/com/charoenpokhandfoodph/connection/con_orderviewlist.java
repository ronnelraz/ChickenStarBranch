package com.charoenpokhandfoodph.connection;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.charoenpokhandfoodph.config;

import java.util.HashMap;
import java.util.Map;

public class con_orderviewlist extends StringRequest {

    public static final String con = config.URL+"_mobile_load_transaction.php";
    private Map<String,String> params;

    public con_orderviewlist(String user_id,String tid, Response.Listener<String> Listener, Response.ErrorListener errorListener){
        super(Method.POST,con,Listener,errorListener);
        params = new HashMap<>();
        params.put("user_id",user_id);
        params.put("tid",tid);
    }

    @Override
    protected Map<String, String> getParams(){
        return params;
    }
}
