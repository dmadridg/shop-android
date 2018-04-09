package com.backant.lifszyc.shop.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_R_URL = "http://192.168.0.17/api/regUser";
    private Map<String, String> params;

    public RegisterRequest(String name, String surname, String password, String email, Response.Listener<String> listener){
        super(Method.POST, REGISTER_R_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("surname", surname);
        params.put("password", password);
        params.put("email", email);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }

}
