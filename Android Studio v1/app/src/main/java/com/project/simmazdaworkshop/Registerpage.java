package com.project.simmazdaworkshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registerpage extends AppCompatActivity {

    private EditText txbfirstname,txblastname,txbalamat,txbemail,txbnotelp,txbusername,txbpassword;
    private Button btnconfirm;


    private String fnama,lname,alamat,email,notelp,username,password;
    private String urlregistercstdata ="https://projectsimmazda.000webhostapp.com/API/registercstdata.php";
    private String urlregisteruserlogin ="https://projectsimmazda.000webhostapp.com/API/registeruserlogin.php";

    private static final String TAG_USER="data";
    private static final String TAG_STATUS="status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);

        txbfirstname = (EditText)findViewById(R.id.txbfirstname);
        txblastname = (EditText)findViewById(R.id.txblastname);
        txbalamat = (EditText)findViewById(R.id.txbalamat);
        txbemail = (EditText)findViewById(R.id.txbemail);
        txbnotelp = (EditText)findViewById(R.id.txbnotelp);
        txbusername = (EditText)findViewById(R.id.txbusername);
        txbpassword = (EditText)findViewById(R.id.txbpassword);

        btnconfirm = (Button)findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnama = txbfirstname.getText().toString();
                lname = txblastname.getText().toString();
                alamat = txbalamat.getText().toString();
                email = txbemail.getText().toString();
                notelp = txbnotelp.getText().toString();
                username = txbusername.getText().toString();
                password = txbpassword.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(Registerpage.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlregisteruserlogin, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int sukses = jObj.getInt("success");
                            if (sukses == 1) {
                                RequestQueue queue = Volley.newRequestQueue(Registerpage.this);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlregistercstdata, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jObj = new JSONObject(response);
                                            int sukses = jObj.getInt("success");
                                            if (sukses == 1) {
                                                Intent i = new Intent(getApplicationContext(),MainMenuUser.class);
                                                i.putExtra("username",username);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                Toast.makeText(Registerpage.this, "Register data gagal periksa informasi pribadi anda", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(Registerpage.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                                    } }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("username", username);
                                        params.put("firstname", fnama);
                                        params.put("lastname", lname);
                                        params.put("alamat", alamat);
                                        params.put("notelp", notelp);
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
                            } else {
                                Toast.makeText(Registerpage.this, "Register data gagal mohon periksa username, email dan password anda", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Registerpage.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("status", "customer");
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
    }
}
