package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PendingReservationApproval extends Fragment {
    private ListView lv;
    private TextView lblgreeting;
    private ArrayList<HashMap<String,String>> list_service;
    private String username,namabengkel,owner,noplat,jenisservis,sesiservis,tanggalservis,catatanservis,idservis;

    private final String status = "pending";

    private String urlgetservicedata = "https://projectsimmazda.000webhostapp.com/API/getpendingreservation.php";
    private String urlgetselectedservicedata = "https://projectsimmazda.000webhostapp.com/API/getselectedpendingreservationdata.php";
    private String urlactionservicedata = "https://projectsimmazda.000webhostapp.com/API/actionreservation.php";

    private static final String TAG_USER="data";
    private static final String TAG_OWNER="username";
    private static final String TAG_NOPLAT="noplat";
    private static final String TAG_BENGKEL="namabengkel";
    private static final String TAG_JENIS="jenisservis";
    private static final String TAG_SESI="sesiservis";
    private static final String TAG_TANGGAL="tanggalservis";
    private static final String TAG_CATATAN="catatan";
    private static final String TAG_IDRESERVASI="idreservasi";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        namabengkel = getArguments().getString("namabengkel");
        return inflater.inflate(R.layout.activity_pending_reservation_approval, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        lblgreeting = (TextView)getActivity().findViewById(R.id.lblnamabengkel);
        lblgreeting.setText(namabengkel);
        list_service =new ArrayList<HashMap<String, String>>();
        lv = getActivity().findViewById(R.id.listpending);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String  getnoplat = ((TextView) view.findViewById(R.id.lblplat)).getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetselectedservicedata, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONArray member= jObj.getJSONArray(TAG_USER);

                            for (int i=0; i< member.length();i++) {
                                JSONObject a=member.getJSONObject(i);
                                idservis = a.getString(TAG_IDRESERVASI).toString();
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
                        //finish();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("noplat", getnoplat);
                        params.put("status", status);
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                        return params;
                    }
                };
                queue.add(stringRequest);

                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setTitle(getnoplat + " Action?");
                ad.setPositiveButton("Terima Reservasi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlactionservicedata, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    int sukses = jObj.getInt("success");
                                    if (sukses == 1) {
                                        Toast.makeText(getActivity().getApplicationContext(),"Data reservasi servis berhasil diproses",Toast.LENGTH_LONG).show();
                                        PendingReservationApproval fragpendingreserve = new PendingReservationApproval();
                                        fragpendingreserve.setArguments(getActivity().getIntent().getExtras());
                                        Bundle bundle = new Bundle();
                                        bundle.putString("username",username);
                                        bundle.putString("namabengkel",namabengkel);
                                        fragpendingreserve.setArguments(bundle);

                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragpendingreserve).commit();
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Data reservasi servis gagal diproses", Toast.LENGTH_SHORT).show();
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
                                params.put("status", "on progress");
                                params.put("idreservasi",idservis);
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
                ad.setNegativeButton("Tolak Reservasi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlactionservicedata, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    int sukses = jObj.getInt("success");
                                    if (sukses == 1) {
                                        Toast.makeText(getActivity().getApplicationContext(),"Data reservasi servis berhasil ditolak",Toast.LENGTH_LONG).show();
                                        PendingReservationApproval fragpendingreserve = new PendingReservationApproval();
                                        fragpendingreserve.setArguments(getActivity().getIntent().getExtras());
                                        Bundle bundle = new Bundle();
                                        bundle.putString("username",username);
                                        bundle.putString("namabengkel",namabengkel);
                                        fragpendingreserve.setArguments(bundle);

                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragpendingreserve).commit();
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Data reservasi servis gagal ditolak", Toast.LENGTH_SHORT).show();
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
                                params.put("status", "rejected");
                                params.put("idreservasi",idservis);
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
                ad.show();
            }
        });



        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetservicedata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member= jObj.getJSONArray(TAG_USER);
                    //Toast.makeText(SemuaMahasiswaActivity.this, "sesudah tag", Toast.LENGTH_SHORT).show();

                    for (int i=0; i< member.length();i++) {
                        JSONObject a=member.getJSONObject(i);
                        noplat= a.getString(TAG_NOPLAT);
                        owner = a.getString(TAG_OWNER);
                        jenisservis = a.getString(TAG_JENIS);
                        sesiservis = a.getString(TAG_SESI);
                        tanggalservis = a.getString(TAG_TANGGAL);
                        catatanservis = a.getString(TAG_CATATAN);

                        HashMap<String,String> map=new HashMap<>();
                        map.put("noplat",noplat);
                        map.put("owner",owner);
                        map.put("jenis",jenisservis);
                        map.put("sesi",sesiservis);
                        map.put("tanggal",tanggalservis);
                        map.put("catatan",catatanservis);
                        //Toast.makeText(getActivity().getApplicationContext(), "nama"+nama, Toast.LENGTH_SHORT).show();
                        list_service.add(map);
                    }

                    try {
                        //String[] from={"TAG_ID","TAG_NAMA"};//string array
                        String[] from={"noplat","owner","jenis","sesi","tanggal","catatan"};//string array
                        int[] to={R.id.lblplat,R.id.lblowner,R.id.lbljenisservis,R.id.lblsesiservis,R.id.lbltanggalservis,R.id.lblcatatanservis};//int array of views id's
                        ListAdapter adapter=new SimpleAdapter(
                                getActivity().getApplicationContext(),list_service,R.layout.list_pendingreservation,
                                from, to);
                        lv.setAdapter(adapter);
                        //Toast.makeText(SemuaMahasiswaActivity.this, "setelah listview adapter", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception ex){
                        Toast.makeText(getActivity().getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG);
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
                //finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("namabengkel", namabengkel);
                params.put("status", status);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
