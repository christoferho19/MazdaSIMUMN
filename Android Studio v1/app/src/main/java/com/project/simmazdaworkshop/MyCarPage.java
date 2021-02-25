package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import java.util.Map;

public class MyCarPage extends Fragment {

    private ListView lv;
    private ArrayList<HashMap<String,String>> list_car;
    private FloatingActionButton fab;

    private String urlgetcardata = "https://projectsimmazda.000webhostapp.com/API/getcardata.php";

    private static final String TAG_USER="data";
    private static final String TAG_MODEL="carmodel";
    private static final String TAG_YEARMODEL="yearmodel";
    private static final String TAG_COLOR="color";
    private static final String TAG_NOPLAT="noplat";

    private String username,model,year,color,plat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_my_car_page, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        list_car =new ArrayList<HashMap<String, String>>();
        lv = getActivity().findViewById(R.id.listcar);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  getnoplat = ((TextView) view.findViewById(R.id.lblplat)).getText().toString();
                DetailMobil fragdetailmobil = new DetailMobil();
                Bundle bundle = new Bundle();
                bundle.putString("noplat",getnoplat);
                bundle.putString("username",username);
                fragdetailmobil.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragdetailmobil).commit();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetcardata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member= jObj.getJSONArray(TAG_USER);
                    //Toast.makeText(SemuaMahasiswaActivity.this, "sesudah tag", Toast.LENGTH_SHORT).show();

                    for (int i=0; i< member.length();i++) {
                        JSONObject a=member.getJSONObject(i);
                        plat= a.getString(TAG_NOPLAT);
                        model= a.getString(TAG_MODEL) +" "+ a.getString(TAG_YEARMODEL);
                        color = a.getString(TAG_COLOR);

                        HashMap<String,String> map=new HashMap<>();
                        map.put("noplat",plat);
                        map.put("carmodel",model);
                        map.put("color",color);
                        //Toast.makeText(getActivity().getApplicationContext(), "nama"+nama, Toast.LENGTH_SHORT).show();
                        list_car.add(map);
                    }

                    try {
                        //String[] from={"TAG_ID","TAG_NAMA"};//string array
                        String[] from={"noplat","carmodel","color"};//string array
                        int[] to={R.id.lblplat,R.id.lblmodel,R.id.lblwarna};//int array of views id's
                        ListAdapter adapter=new SimpleAdapter(
                                getActivity().getApplicationContext(),list_car,R.layout.list_car,
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
                AddCar fragaddcar = new AddCar();
                fragaddcar.setArguments(getActivity().getIntent().getExtras());
                Bundle addcarbundle = new Bundle();
                addcarbundle.putString("username",username);
                fragaddcar.setArguments(addcarbundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragaddcar).commit();
            }
        });
    }
}
