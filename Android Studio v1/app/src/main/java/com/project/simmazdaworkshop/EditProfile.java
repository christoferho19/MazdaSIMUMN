package com.project.simmazdaworkshop;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class EditProfile extends Fragment {
    private Button btnupdate;
    private EditText txbalamat,txbnotelp;
    private TextView lblnama;
    private String username,nama,alamat,notelp;

    private static final String TAG_USER="data";
    private String urlupdateprofile ="https://projectsimmazda.000webhostapp.com/API/updateprofile.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        nama = getArguments().getString("nama");
        return inflater.inflate(R.layout.activity_edit_profile,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        lblnama = (TextView)getActivity().findViewById(R.id.lblnama);
        lblnama.setText(nama);

        txbalamat = (EditText)getActivity().findViewById(R.id.txbalamat);
        txbnotelp = (EditText)getActivity().findViewById(R.id.txbnotelp);

        btnupdate = (Button)getActivity().findViewById(R.id.btnconfirm);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alamat = txbalamat.getText().toString();
                notelp = txbnotelp.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupdateprofile, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int sukses = jObj.getInt("success");
                            if (sukses == 1) {
                                Toast.makeText(getActivity().getApplicationContext(), "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                                ProfilePageUser fragprofile = new ProfilePageUser();
                                Bundle bundle = new Bundle();
                                bundle.putString("username",username);
                                bundle.putString("nama",lblnama.getText().toString());
                                fragprofile.setArguments(bundle);

                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragprofile).commit();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Data gagal diupdate", Toast.LENGTH_SHORT).show();
                                ProfilePageUser fragprofile = new ProfilePageUser();
                                Bundle bundle = new Bundle();
                                bundle.putString("username",username);
                                bundle.putString("nama",lblnama.getText().toString());
                                fragprofile.setArguments(bundle);

                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragprofile).commit();
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
            }
        });
    }
}
