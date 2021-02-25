package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class DetailMobil extends Fragment {

    private String username,noplat,warna,norangka,nomesin,model,tahun;

    private Button btnreservasi, btndelete;

    private ImageView fotomobil;
    private TextView lblnoplat,lblwarna,lblnorangka,lblnomesin,lblmodel,lbltahun;

    private String urlgetchoosendatamobil = "https://projectsimmazda.000webhostapp.com/API/getselectedcar.php";
    private String urldeletecar = "https://projectsimmazda.000webhostapp.com/API/deletecar.php";

    private static final String TAG_USER="data";
    private static final String TAG_MODEL="carmodel";
    private static final String TAG_YEAR="yearmodel";
    private static final String TAG_COLOR="color";
    private static final String TAG_RANGKA="norangka";
    private static final String TAG_MESIN="nomesin";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noplat = getArguments().getString("noplat");
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_detail_mobil, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fotomobil = (ImageView)getActivity().findViewById(R.id.imgmobil);
        lblnoplat = (TextView)getActivity().findViewById(R.id.lblnoplat);
        lblmodel = (TextView)getActivity().findViewById(R.id.lblmodelmobil);
        lblwarna = (TextView)getActivity().findViewById(R.id.lblwarna);
        lblnorangka = (TextView)getActivity().findViewById(R.id.lblnorangka);
        lblnomesin = (TextView)getActivity().findViewById(R.id.lblnomesin);
        lbltahun = (TextView)getActivity().findViewById(R.id.lbltahun);

        lblnoplat.setText(noplat);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetchoosendatamobil, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    int sukses = jObj.getInt("success");
                    if (sukses == 1) {
                        JSONArray member= jObj.getJSONArray(TAG_USER);
                        JSONObject a=member.getJSONObject(0);
                        model = a.getString(TAG_MODEL);
                        lblmodel.setText(model);
                        lblnomesin.setText(a.getString(TAG_MESIN));
                        lblnorangka.setText(a.getString(TAG_RANGKA));
                        lbltahun.setText(a.getString(TAG_YEAR));
                        lblwarna.setText(a.getString(TAG_COLOR));

                        if(model.equalsIgnoreCase("Mazda 2")){
                            fotomobil.setImageResource(R.drawable.mazda2);
                        }
                        if (model.equalsIgnoreCase("Mazda 3")){
                            fotomobil.setImageResource(R.drawable.mazda3);
                        }
                        if (model.equalsIgnoreCase("Mazda 6 Sedan")){
                            fotomobil.setImageResource(R.drawable.mazda6sedan);
                        }

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
                params.put("noplat", noplat);
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

        btndelete = (Button)getActivity().findViewById(R.id.btndelete);
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setTitle("Konfirmasi Hapus");
                ad.setMessage("Apakah anda yakin akan menghapus data dari mobil dengan nomor plat " + noplat +" dari akun anda?");
                ad.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urldeletecar, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    int sukses = jObj.getInt("success");
                                    if (sukses == 1) {
                                        Toast.makeText(getActivity().getApplicationContext(),"Data Mobil berhasil dihapus",Toast.LENGTH_LONG).show();
                                        final MyCarPage fragmycar = new MyCarPage();
                                        fragmycar.setArguments(getActivity().getIntent().getExtras());
                                        Bundle bundle = new Bundle();
                                        bundle.putString("username",username);
                                        fragmycar.setArguments(bundle);

                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragmycar).commit();
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Data Mobil gagal dihapus", Toast.LENGTH_SHORT).show();
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
                                params.put("noplat", noplat);
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
                ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad.show();
            }
        });

        btnreservasi = (Button)getActivity().findViewById(R.id.btnreservasiservis);
        btnreservasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReservation fragaddreser = new AddReservation();
                fragaddreser.setArguments(getActivity().getIntent().getExtras());
                Bundle addreservastionbundle = new Bundle();
                addreservastionbundle.putString("username",username);
                addreservastionbundle.putString("noplat",noplat);
                addreservastionbundle.putString("checkinput","terinput");
                fragaddreser.setArguments(addreservastionbundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragaddreser).commit();
            }
        });
    }
}
