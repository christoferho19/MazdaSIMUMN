package com.project.simmazdaworkshop;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangeEmail extends Fragment {
    private EditText txbemailbaru,txbpassword;
    private Button btnconfim;

    private static final String TAG_USER="data";
    private String username,password,mailnew;
    private String urlchangeemail ="https://projectsimmazda.000webhostapp.com/API/changeemail.php";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_change_email, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        txbemailbaru = (EditText)getActivity().findViewById(R.id.txbemailbaru);
        txbpassword = (EditText)getActivity().findViewById(R.id.txbpassword);



        btnconfim = (Button)getActivity().findViewById(R.id.btnconfirm);
        btnconfim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = txbpassword.getText().toString();
                mailnew = txbemailbaru.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlchangeemail, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int sukses = jObj.getInt("success");
                            if (sukses == 1) {
                                Toast.makeText(getActivity().getApplicationContext(), "Data email berhasil diganti", Toast.LENGTH_SHORT).show();
                                SettingPageUser fragsetting = new SettingPageUser();
                                Bundle bundle = new Bundle();
                                bundle.putString("username",username);
                                fragsetting.setArguments(bundle);

                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragsetting).commit();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Data email gagal diganti", Toast.LENGTH_SHORT).show();
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
                        params.put("password", password);
                        params.put("email", mailnew);
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
