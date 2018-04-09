package com.backant.lifszyc.shop.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String REGISTER_R_URL = "http://192.168.0.17/api/logUser";
    private Map<String, String> params;

    public LoginRequest(String password, String email, Response.Listener<String> listener){
        super(Method.POST, REGISTER_R_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }

}
