package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddReservation extends Fragment {
    private String username,noplat,checkinput,tanggalservis,jenisservis,bengkelservis,catatanservis,sesiservis;
    private EditText txbnoplat, txbtanggal,txbcatatan;
    private Spinner spinbengkel,spinjenis,spinsesi;
    private Button btnconfirm, btnchoosedate;

    private final String status = "pending";

    private String [] daftarbengkel = {"- Pilih Bengkel -","Mazda Jakarta Timur","Mazda MT Haryono","Mazda Jakarta Barat"};
    private String [] daftarjenisservis = {"- Pilih Kategori Servis -","Fast Track","Servis Berkala","Heavy Repair","Body and paint"};
    private String [] daftarsesi = {"- Pilih Sesi Servis -","Pagi (08.00 - 11.00)","Siang (13.00 - 15.00)"};

    private String urladdreservation = "https://projectsimmazda.000webhostapp.com/API/addservicereservation.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        checkinput = getArguments().getString("checkinput");
        return inflater.inflate(R.layout.activity_add_reservation, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        txbnoplat = (EditText)getActivity().findViewById(R.id.txbnoplat);
        txbtanggal = (EditText)getActivity().findViewById(R.id.txbtanggalservis);
        txbcatatan = (EditText)getActivity().findViewById(R.id.txbcatatan);

        spinbengkel = (Spinner)getActivity().findViewById(R.id.txbbengkel);
        spinjenis = (Spinner)getActivity().findViewById(R.id.txbjenisservis);
        spinsesi = (Spinner)getActivity().findViewById(R.id.txbsesiservis);


        ArrayAdapter bengkeladapter = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,daftarbengkel);
        spinbengkel.setAdapter(bengkeladapter);

        ArrayAdapter jenisadapter = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,daftarjenisservis);
        spinjenis.setAdapter(jenisadapter);

        final ArrayAdapter sesiadapter = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,daftarsesi);
        spinsesi.setAdapter(sesiadapter);

        if(checkinput.equals("terinput")){
            noplat = getArguments().getString("noplat");
            txbnoplat.setEnabled(false);
            txbnoplat.setText(noplat);
        }
        else {
            txbnoplat.setEnabled(true);
        }

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);

        btnchoosedate = (Button)getActivity().findViewById(R.id.btnpilihtanggal);
        btnchoosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mycalendar = Calendar.getInstance();
                DatePickerDialog pickdate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year,month,dayOfMonth);
                        txbtanggal.setText(sdf.format(newDate.getTime()));
                    }
                }, mycalendar.get(Calendar.YEAR), mycalendar.get(Calendar.MONTH),mycalendar.get(Calendar.DAY_OF_MONTH));
                pickdate.show();
            }
        });


        btnconfirm = (Button)getActivity().findViewById(R.id.btnconfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noplat = txbnoplat.getText().toString();
                bengkelservis = spinbengkel.getSelectedItem().toString();
                jenisservis = spinjenis.getSelectedItem().toString();
                sesiservis = spinsesi.getSelectedItem().toString();
                tanggalservis = txbtanggal.getText().toString();
                catatanservis= txbcatatan.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urladdreservation, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            int sukses = jObj.getInt("success");
                            if (sukses == 1) {
                                Toast.makeText(getActivity().getApplicationContext(), "Reservasi servis berhasil dilakukan", Toast.LENGTH_SHORT).show();
                                final MyServiceReservation fragmyservice = new MyServiceReservation();
                                fragmyservice.setArguments(getActivity().getIntent().getExtras());
                                Bundle servicebundle = new Bundle();
                                servicebundle.putString("username",username);
                                fragmyservice.setArguments(servicebundle);

                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragmyservice).commit();
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Reservasi servis gagal dilakukan", Toast.LENGTH_SHORT).show();
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
                        params.put("namabengkel", bengkelservis);
                        params.put("jenisservis", jenisservis);
                        params.put("sesiservis", sesiservis);
                        params.put("tanggalservis", tanggalservis);
                        params.put("catatan", catatanservis);
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
                queue.getCache().clear();
                queue.add(stringRequest);
            }
        });

    }
}
