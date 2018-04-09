package com.backant.lifszyc.shop;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.backant.lifszyc.shop.db.DbAdapter;
import com.backant.lifszyc.shop.db.MySqliteOpenHelper;
import com.backant.lifszyc.shop.requests.LoginRequest;
import com.backant.lifszyc.shop.requests.RegisterRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.itEmail)
    TextInputEditText itEmail;
    @BindView(R.id.itPassword)
    TextInputEditText itPassword;
    private MySqliteOpenHelper mySqliteOpenHelper;
    private SQLiteDatabase mDatabase;
    DbAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mySqliteOpenHelper = new MySqliteOpenHelper(getApplicationContext());
        mDatabase = mySqliteOpenHelper.getReadableDatabase();
        mDatabase.execSQL("delete from user;");
    }

    public void registerForm(View view) {
        Intent myIntent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(myIntent);
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    public void login(View view) {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);
        progress.show();
        // To dismiss the dialog

        final String emailInput = itEmail.getText().toString();
        final String passwordInput = itPassword.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("RESPONSE", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    Log.e("ERROR: ", "" + success);
                    if (success){
                        if (mDatabase != null) {
                            JSONObject userInfo = jsonObject.getJSONObject("data");
                            Log.e("ERROR: ", "" + userInfo);
                            Log.e("ERROR: ", "" + userInfo.getString("email"));

                            long rowId = 0;

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("name", userInfo.getString("name"));
                            contentValues.put("surname", userInfo.getString("surname"));
                            contentValues.put("email", userInfo.getString("email"));
                            contentValues.put("password", passwordInput);
                            contentValues.put("activo", userInfo.getInt("activo"));
                            contentValues.put("role", userInfo.getInt("role"));

                            rowId = mDatabase.insert("user", null, contentValues);
                            Log.e("ROWID", " " + rowId);

                            progress.dismiss();
                            if (rowId != -1) {
                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(i);
                                finish();
                                Toast.makeText(getApplicationContext(), "Insertado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Database is null", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //List<String> list = new ArrayList<String>();
                        for (int i = 0; i <= 2; i++){
                            if(jsonObject.getString("email").length() > 1 ){
                                progress.dismiss();
                                itEmail.setError(jsonObject.getString("email"));
                                showAlert(jsonObject.getString("email"));
                                break;
                            }else if(jsonObject.getString("password").length() > 1 ){
                                progress.dismiss();
                                itPassword.setError(jsonObject.getString("password"));
                                showAlert(jsonObject.getString("password"));
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.dismiss();
                    showAlert("Ocurrió un error inesperado del servidor. Intente de nuevo más tarde.");
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(passwordInput, emailInput, responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(loginRequest);
    }

    public void showAlert(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder((LoginActivity.this));
        builder.setMessage(msg).setNegativeButton("Intentar de nuevo", null).create().show();
    }
}
