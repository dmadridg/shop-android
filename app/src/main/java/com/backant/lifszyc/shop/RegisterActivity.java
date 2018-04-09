package com.backant.lifszyc.shop;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.backant.lifszyc.shop.requests.RegisterRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.itName)
    TextInputEditText itName;
    @BindView(R.id.itSurname)
    TextInputEditText itSurname;
    @BindView(R.id.itEmail)
    TextInputEditText itEmail;
    @BindView(R.id.itPassword)
    TextInputEditText itPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setTitle("Registro");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransitionExit();
        return true;
    }

    public void goBack(String msg){
        onBackPressed();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void registerUser(View view) {


        final String name = itName.getText().toString();
        final String surname = itSurname.getText().toString();
        final String email = itEmail.getText().toString();
        final String password = itPassword.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        goBack(jsonObject.getString("data"));
                    }else{
                        //List<String> list = new ArrayList<String>();
                        for (int i = 0; i <= 4; i++){
                            if(jsonObject.getString("name").length() > 1 ){
                                itName.setError(jsonObject.getString("name"));
                                showAlert(jsonObject.getString("name"));
                                break;
                            }else if(jsonObject.getString("surname").length() > 1 ){
                                itSurname.setError(jsonObject.getString("surname"));
                                showAlert(jsonObject.getString("surname"));
                                break;
                            }else if(jsonObject.getString("email").length() > 1 ){
                                itEmail.setError(jsonObject.getString("email"));
                                showAlert(jsonObject.getString("email"));
                                break;
                            }else if(jsonObject.getString("password").length() > 1 ){
                                itPassword.setError(jsonObject.getString("password"));
                                showAlert(jsonObject.getString("password"));
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(name, surname, password, email, responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(registerRequest);
        ProgressBar progressBar = new ProgressBar(RegisterActivity.this, null, android.R.attr.progressBarStyleSmall);
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
    }

    public void showAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder((RegisterActivity.this));
        builder.setMessage(msg).setNegativeButton("Intentar de nuevo", null).create().show();
    }
}
