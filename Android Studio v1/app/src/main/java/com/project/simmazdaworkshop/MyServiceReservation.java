package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyServiceReservation extends Fragment {
    private ListView lv;
    private ArrayList<HashMap<String,String>> list_service;
    private String username,noplat,bengkel,jenis,sesi,tanggal,catatan;
    private FloatingActionButton fab;

    private final String status = "pending";

    private String urlgetservicedata = "https://projectsimmazda.000webhostapp.com/API/getservicedata.php";
    private String urldeleteservicedata = "https://projectsimmazda.000webhostapp.com/API/deletereservation.php";

    private static final String TAG_USER="data";
    private static final String TAG_NOPLAT="noplat";
    private static final String TAG_BENGKEL="namabengkel";
    private static final String TAG_JENIS="jenisservis";
    private static final String TAG_SESI="sesiservis";
    private static final String TAG_TANGGAL="tanggalservis";
    private static final String TAG_CATATAN="catatan";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_my_service_reservation, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        list_service =new ArrayList<HashMap<String, String>>();
        lv = getActivity().findViewById(R.id.listservice);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  getnoplat = ((TextView) view.findViewById(R.id.lblplat)).getText().toString();
                String  gettanggal = ((TextView) view.findViewById(R.id.lbltanggalservis)).getText().toString();
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setTitle("Konfirmasi Hapus");
                ad.setMessage("Apakah anda yakin akan menghapus reservasi servis mobil dengan nomor plat " + getnoplat +" pada tanggal " + gettanggal + " dari akun anda?");
                ad.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urldeleteservicedata, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    int sukses = jObj.getInt("success");
                                    if (sukses == 1) {
                                        Toast.makeText(getActivity().getApplicationContext(),"Data reservasi servis berhasil dihapus",Toast.LENGTH_LONG).show();
                                        MyServiceReservation fragmyservice = new MyServiceReservation();
                                        fragmyservice.setArguments(getActivity().getIntent().getExtras());
                                        Bundle bundle = new Bundle();
                                        bundle.putString("username",username);
                                        fragmyservice.setArguments(bundle);

                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragmyservice).commit();
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Data reservasi servis gagal dihapus", Toast.LENGTH_SHORT).show();
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
                                params.put("tanggalservis",tanggal);
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
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
                        bengkel = a.getString(TAG_BENGKEL);
                        jenis = a.getString(TAG_JENIS);
                        sesi = a.getString(TAG_SESI);
                        tanggal = a.getString(TAG_TANGGAL);
                        catatan = a.getString(TAG_CATATAN);

                        HashMap<String,String> map=new HashMap<>();
                        map.put("noplat",noplat);
                        map.put("bengkel",bengkel);
                        map.put("jenis",jenis);
                        map.put("sesi",sesi);
                        map.put("tanggal",tanggal);
                        map.put("catatan",catatan);
                        //Toast.makeText(getActivity().getApplicationContext(), "nama"+nama, Toast.LENGTH_SHORT).show();
                        list_service.add(map);
                    }

                    try {
                        //String[] from={"TAG_ID","TAG_NAMA"};//string array
                        String[] from={"noplat","bengkel","jenis","sesi","tanggal","catatan"};//string array
                        int[] to={R.id.lblplat,R.id.lblnamabengkel,R.id.lbljenisservis,R.id.lblsesiservis,R.id.lbltanggalservis,R.id.lblcatatanservis};//int array of views id's
                        ListAdapter adapter=new SimpleAdapter(
                                getActivity().getApplicationContext(),list_service,R.layout.list_service,
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
                params.put("username", username);
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

        fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddReservation fragaddreser = new AddReservation();
                fragaddreser.setArguments(getActivity().getIntent().getExtras());
                Bundle addreservastionbundle = new Bundle();
                addreservastionbundle.putString("username",username);
                addreservastionbundle.putString("checkinput","belum");
                fragaddreser.setArguments(addreservastionbundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragaddreser).commit();
            }
        });
    }
}
