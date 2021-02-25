package com.project.simmazdaworkshop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
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
import java.util.Locale;
import java.util.Map;

public class DetailBengkel extends Fragment {

    private ImageView fotobengkel;
    private TextView lblnama,lblalamat,lblnotelp;
    private Button btncall,btndirection;

    private String urlgetchoosendatabengkel = "https://projectsimmazda.000webhostapp.com/API/getselecteddatabengkel.php";

    private static final String TAG_USER="data";
    private static final String TAG_NAMA="nama";
    private static final String TAG_ALAMAT="alamat";
    private static final String TAG_NOTELP="notelp";
    private static final String TAG_LATITUDE="latitude";
    private static final String TAG_LONGITUDE="longitude";

    private String nama,alamat,lat,lot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        nama = getArguments().getString("nama");
        alamat = getArguments().getString("alamat");
        return inflater.inflate(R.layout.activity_detail_bengkel, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fotobengkel = (ImageView)getActivity().findViewById(R.id.imgbengkel);
        lblnama = (TextView)getActivity().findViewById(R.id.lblnama);
        lblalamat = (TextView)getActivity().findViewById(R.id.lblalamat);
        lblnotelp = (TextView)getActivity().findViewById(R.id.lblnotelp);

        if(nama.equalsIgnoreCase("Mazda Jakarta Timur")){
            fotobengkel.setImageResource(R.drawable.mazdajakartatimur);
        }
        if (nama.equalsIgnoreCase("Mazda Jakarta Barat")){
            fotobengkel.setImageResource(R.drawable.mazdajakartabarat);
        }
        if (nama.equalsIgnoreCase("Mazda MT Haryono")){
            fotobengkel.setImageResource(R.drawable.mazdamtharyono);
        }


        lblnama.setText(nama);
        lblalamat.setText(alamat);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetchoosendatabengkel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int sukses = jObj.getInt("success");
                    if (sukses == 1) {
                        JSONArray member= jObj.getJSONArray(TAG_USER);
                        JSONObject a=member.getJSONObject(0);
                        lblnotelp.setText(a.getString(TAG_NOTELP));
                        lat = a.getString(TAG_LATITUDE);
                        lot = a.getString(TAG_LONGITUDE);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Data Error", Toast.LENGTH_SHORT).show();
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
                params.put("namabengkel", nama);
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

        btncall = (Button)getActivity().findViewById(R.id.btncall);
        btncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", lblnotelp.getText().toString(), null));
                startActivity(intent);
            }
        });
        btndirection = (Button)getActivity().findViewById(R.id.btndirection);
        btndirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.co.in/maps?q=" + nama;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(i);
            }
        });
    }
}
