package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AddCar extends Fragment {
    private String username,noplat,carmodel,caryear,carcolor,norangka,nomesin;
    private Spinner spinwarna, spinmodel;
    private Button btnregister;
    private EditText txbnoplat,txbtahunmobil,txbnorangka,txbnomesin;
    private String [] daftarwarna = {"- Pilih Warna -","Red Soul Crystal Metallic","Jet Black Mica","Machine Grey","Meteor Grey"};
    private String [] daftarmodel = {"- Pilih Model -","Mazda 2","Mazda 3","Mazda 6 Sedan"};

    private String urladdcar = "https://projectsimmazda.000webhostapp.com/API/addcar.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_add_car, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        spinmodel = (Spinner)getActivity().findViewById(R.id.txbmodel);
        spinwarna = (Spinner)getActivity().findViewById(R.id.txbwarna);

        ArrayAdapter modeladapter = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,daftarmodel);
        spinmodel.setAdapter(modeladapter);

        ArrayAdapter coloradapter = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,daftarwarna);
        spinwarna.setAdapter(coloradapter);

        txbnoplat = (EditText)getActivity().findViewById(R.id.txbnoplat);
        txbtahunmobil = (EditText)getActivity().findViewById(R.id.txbyearmodel);
        txbnorangka = (EditText)getActivity().findViewById(R.id.txbnorangka);
        txbnomesin = (EditText)getActivity().findViewById(R.id.txbnomesin);

        btnregister = (Button)getActivity().findViewById(R.id.btnconfirm);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noplat = txbnoplat.getText().toString();
                carmodel = spinmodel.getSelectedItem().toString();
                caryear = txbtahunmobil.getText().toString();
                carcolor = spinwarna.getSelectedItem().toString();
                norangka = txbnorangka.getText().toString();
                nomesin = txbnomesin.getText().toString();

                //Toast.makeText(getActivity().getApplicationContext(),noplat + carmodel + caryear + carcolor + norangka + nomesin ,Toast.LENGTH_LONG).show();
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setTitle("Konfirmasi Detail Service");
                ad.setMessage("Apakah anda yakin akan menambahkan data dari mobil dengan nomor plat " + noplat +" di akun anda?");
                ad.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, urladdcar, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    int sukses = jObj.getInt("success");
                                    if (sukses == 1) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Data mobil berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                        MyCarPage fragmycar = new MyCarPage();
                                        fragmycar.setArguments(getActivity().getIntent().getExtras());
                                        Bundle bundle = new Bundle();
                                        bundle.putString("username",username);
                                        fragmycar.setArguments(bundle);

                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragmycar).commit();
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Data mobil gagal ditambahkan", Toast.LENGTH_SHORT).show();
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
                                params.put("username", username);
                                params.put("carmodel", carmodel);
                                params.put("yearmodel", caryear);
                                params.put("color", carcolor);
                                params.put("norangka", norangka);
                                params.put("nomesin", nomesin);
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
    }
}
