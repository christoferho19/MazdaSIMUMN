package com.project.simmazdaworkshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class ChangePassword extends Fragment {
    private EditText txbpasswordlama,txbpasswordbaru;
    private Button btnconfim;

    private static final String TAG_USER="data";
    private static final String TAG_Password="password";
    private String username,passwordlama,passwordbaru;
    private String temppassword;

    private String urlcekpassword ="https://projectsimmazda.000webhostapp.com/API/cekpassword.php";
    private String urlchangepassword ="https://projectsimmazda.000webhostapp.com/API/changepassword.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_change_password, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        txbpasswordlama = (EditText)getActivity().findViewById(R.id.txbpasswordlama);
        txbpasswordbaru = (EditText)getActivity().findViewById(R.id.txbpasswordbaru);

        btnconfim = (Button)getActivity().findViewById(R.id.btnconfirm);
        btnconfim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordlama = txbpasswordlama.getText().toString();
                passwordbaru = txbpasswordbaru.getText().toString();
                //Toast.makeText(getActivity().getApplicationContext(),passwordlama+passwordbaru,Toast.LENGTH_LONG).show();
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlcekpassword, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int sukses = jObj.getInt("success");
                            if (sukses == 1) {
                                JSONArray member= jObj.getJSONArray(TAG_USER);
                                JSONObject a=member.getJSONObject(0);
                                temppassword=a.getString(TAG_Password);

                                if(temppassword.equals(passwordlama)){
                                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlchangepassword, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jObj = new JSONObject(response);
                                                int sukses = jObj.getInt("success");
                                                if (sukses == 1) {
                                                    Toast.makeText(getActivity().getApplicationContext(), "Data password berhasil diganti silahkan login ulang", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(getActivity().getApplicationContext(),Loginpage.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                } else {
                                                    Toast.makeText(getActivity().getApplicationContext(), "Data password gagal diganti", Toast.LENGTH_SHORT).show();
                                                    SettingPageUser fragsetting = new SettingPageUser();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("username",username);
                                                    fragsetting.setArguments(bundle);

                                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragsetting).commit();
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
                                            Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                        } }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("username", username);
                                            params.put("password", passwordbaru);
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
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Password anda yang lama salah", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity().getApplicationContext(), "silahkan cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                    } }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", passwordlama);
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
