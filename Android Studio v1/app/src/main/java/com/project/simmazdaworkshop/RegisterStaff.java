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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterStaff extends AppCompatActivity {
    private EditText txbemail,txbusername,txbpassword;
    private Button btnconfirm;


    private String email,username,password;
    private String urlregisteruserlogin ="https://projectsimmazda.000webhostapp.com/API/registeruserlogin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_staff);

        txbemail = (EditText)findViewById(R.id.txbemail);
        txbusername = (EditText)findViewById(R.id.txbusername);
        txbpassword = (EditText)findViewById(R.id.txbpassword);

        btnconfirm = (Button)findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txbemail.getText().toString();
                username = txbusername.getText().toString();
                password = txbpassword.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(RegisterStaff.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlregisteruserlogin, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int sukses = jObj.getInt("success");
                            if (sukses == 1) {
                                Intent i = new Intent(getApplicationContext(),Loginpage.class);
                                i.putExtra("username",username);
                                startActivity(i);
                                finish();

                                Toast.makeText(RegisterStaff.this,"Staff berhasil didaftarkan",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(RegisterStaff.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                    } }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", password);
                        params.put("email", email);
                        params.put("status", "staff");
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
