package com.project.simmazdaworkshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Loginpage extends AppCompatActivity {

    private TextView lblregister;
    private EditText edtusername,edtpassword;
    private String username,password;
    private String urlcheckdata ="https://projectsimmazda.000webhostapp.com/API/login.php";

    private static final String TAG_USER="data";
    private static final String TAG_STATUS="status";
    private static final String TAG_USERNAME="username";
    private String LA,Uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        Button btnlogin = (Button)findViewById(R.id.btnlogin);
        edtusername = (EditText) findViewById(R.id.txbusername);
        edtpassword = (EditText) findViewById(R.id.txbpassword);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtusername.getText().toString();
                password = edtpassword.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(Loginpage.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlcheckdata, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int sukses = jObj.getInt("success");
                            if (sukses == 1) {
                                JSONArray member= jObj.getJSONArray(TAG_USER);
                                JSONObject a=member.getJSONObject(0);
                                LA=a.getString(TAG_STATUS);
                                Uname = a.getString(TAG_USERNAME);
                                //Toast.makeText(Loginpage.this, "status"+LA, Toast.LENGTH_SHORT).show();

                                if(LA.equals("customer")){
                                    Intent i = new Intent(getApplicationContext(),MainMenuUser.class);
                                    i.putExtra("username",Uname);
                                    startActivity(i);
                                    finish();
                                }
                                if(LA.equals("staff")){
                                    Intent i = new Intent(getApplicationContext(),LoginWorkshop.class);
                                    i.putExtra("username",Uname);
                                    startActivity(i);
                                    finish();
                                }
                                finish();
                            } else {
                                Toast.makeText(Loginpage.this, "Username dan Password yang anda masukkan salah", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception ex) {
                            Log.e("Error", ex.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.getMessage());
                        Toast.makeText(Loginpage.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                    } }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", password);
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                        return params;
                    }
                };
                queue.getCache().clear();
                queue.add(stringRequest);
            }
        });

        lblregister = (TextView)findViewById(R.id.lblregister);
        lblregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Loginpage.this,Registerpage.class);
                startActivity(i);
            }
        });
    }
}
