package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilePageUser extends Fragment {
    private TextView lblnama,lblnotelp,lblalamat,lblemail;
    private Button btnedit;

    private static final String TAG_USER="data";
    private static final String TAG_Fnama="firstname";
    private static final String TAG_Lnama="lastname";
    private static final String TAG_alamat="alamat";
    private static final String TAG_email="email";
    private static final String TAG_notelp="notelp";

    private String urlprofile ="https://projectsimmazda.000webhostapp.com/API/profile.php";
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_profile_user, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        lblnama = (TextView)getActivity().findViewById(R.id.lblnama);
        lblalamat= (TextView)getActivity().findViewById(R.id.lblalamat);
        lblnotelp = (TextView)getActivity().findViewById(R.id.lblnotelp);
        lblemail = (TextView)getActivity().findViewById(R.id.lblemail);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlprofile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int sukses = jObj.getInt("success");
                    if (sukses == 1) {
                        JSONArray member= jObj.getJSONArray(TAG_USER);
                        JSONObject a=member.getJSONObject(0);
                        lblnama.setText(a.getString(TAG_Fnama)+" " + a.getString(TAG_Lnama));
                        lblalamat.setText(a.getString(TAG_alamat));
                        lblemail.setText(a.getString(TAG_email));
                        lblnotelp.setText(a.getString(TAG_notelp));
                        //Toast.makeText(Loginpage.this, "status"+LA, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Username dan Password yang anda masukkan salah", Toast.LENGTH_SHORT).show();
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

        btnedit = (Button)getActivity().findViewById(R.id.btneditprofile);
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile frageditprofile = new EditProfile();
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                bundle.putString("nama",lblnama.getText().toString());
                frageditprofile.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,frageditprofile).commit();
            }
        });

    }
}
