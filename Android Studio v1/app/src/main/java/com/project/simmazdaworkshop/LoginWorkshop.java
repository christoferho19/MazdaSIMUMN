package com.project.simmazdaworkshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class LoginWorkshop extends AppCompatActivity {
    private Spinner spinbengkel;
    private EditText txbpassword;
    private Button btnverify,btncancel;
    private TextView lblgreetings;

    private String username,password,namabengkel;
    private String [] daftarbengkel = {"- Pilih Bengkel -","Mazda Jakarta Timur","Mazda MT Haryono","Mazda Jakarta Barat"};

    private String urlcheckdata ="https://projectsimmazda.000webhostapp.com/API/verifybengkel.php";

    private static final String TAG_USER="data";
    private static final String TAG_NAMABENGKEL="namabengkel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_workshop);

        username = getIntent().getStringExtra("username");

        spinbengkel = (Spinner)findViewById(R.id.txbbengkel);
        btnverify = (Button)findViewById(R.id.btnverify);
        lblgreetings = (TextView)findViewById(R.id.lblgreetuser);
        txbpassword = (EditText)findViewById(R.id.txbpassword);

        lblgreetings.setText("Hello " + username + System.lineSeparator() + "Please choose your workshop" );

        ArrayAdapter bengkeladapter = new ArrayAdapter(LoginWorkshop.this,android.R.layout.simple_spinner_item,daftarbengkel);
        spinbengkel.setAdapter(bengkeladapter);

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namabengkel = spinbengkel.getSelectedItem().toString();
                password = txbpassword.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(LoginWorkshop.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlcheckdata, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int sukses = jObj.getInt("success");
                            if (sukses == 1) {
                                JSONArray member= jObj.getJSONArray(TAG_USER);
                                JSONObject a=member.getJSONObject(0);

                                namabengkel=a.getString(TAG_NAMABENGKEL);
                                Intent i = new Intent(getApplicationContext(),MainMenuStaff.class);
                                i.putExtra("username",username);
                                i.putExtra("namabengkel",namabengkel);
                                startActivity(i);
                                //Toast.makeText(LoginWorkshop.this, namabengkel, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginWorkshop.this, "Password yang anda masukkan salah", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LoginWorkshop.this, "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                    } }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("namabengkel", namabengkel);
                        params.put("passwordbengkel", password);
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
        btncancel = (Button)findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginWorkshop.this,Loginpage.class);
                startActivity(i);
                finish();
            }
        });
    }
}
