package com.backant.lifszyc.shop;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.backant.lifszyc.shop.db.MySqliteOpenHelper;
import com.backant.lifszyc.shop.requests.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoadingActivity extends AppCompatActivity {

    private MySqliteOpenHelper mySqliteOpenHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Thread thread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(1000);
                    mySqliteOpenHelper = new MySqliteOpenHelper(getApplicationContext());
                    mDatabase = mySqliteOpenHelper.getReadableDatabase();

                    Cursor cursor = mDatabase.rawQuery("select * from user;", null);
                    Log.e("VERGA", "DB: " + cursor.getCount());

                    if (cursor.getCount() > 0){
                        String emailInput = "";
                        String passwordInput = "";

                        while (cursor.moveToNext()){
                            emailInput = cursor.getString(cursor.getColumnIndex("email"));
                            passwordInput = cursor.getString(cursor.getColumnIndex("password"));
                            Log.e("VERGA", "DB ID: " + cursor.getInt(cursor.getColumnIndex(mySqliteOpenHelper.UID)));
                        }



                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    Log.e("ERROR: ", "" + success);
                                    if (success){
                                        if (mDatabase != null) {
                                            JSONObject userInfo = jsonObject.getJSONObject("data");
                                            Log.e("ERROR: ", "" + userInfo);
                                            Log.e("ERROR: ", "" + userInfo.getString("email"));

                                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                            startActivity(i);
                                            finish();

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Database is null", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        //List<String> list = new ArrayList<String>();
                                        mDatabase.execSQL("delete from user");
                                        showAlert("Error al iniciar sesión. Intente de nuevo más tarde.");
                                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        LoginRequest loginRequest = new LoginRequest(passwordInput, emailInput, responseListener);
                        RequestQueue requestQueue = Volley.newRequestQueue(LoadingActivity.this);
                        requestQueue.add(loginRequest);


                    }else{
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void showAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder((LoadingActivity.this));
        builder.setMessage(msg).setNegativeButton("Intentar de nuevo", null).create().show();
    }
}
