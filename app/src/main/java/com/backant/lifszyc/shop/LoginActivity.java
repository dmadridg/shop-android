package com.backant.lifszyc.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



    }

    public void registerForm(View view) {
        Intent myIntent = new Intent(getApplicationContext(), RegisterActivity.class);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        startActivityForResult(myIntent, 0);
    }
}
