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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class WorkshopList extends Fragment {

    private ListView lv;
    private ArrayList<HashMap<String,String>> list_bengkel;

    private String urlgetdatabengkel = "https://projectsimmazda.000webhostapp.com/API/getdatabengkel.php";

    private static final String TAG_USER="data";
    private static final String TAG_NAMA="nama";
    private static final String TAG_ALAMAT="alamat";
    private static final String TAG_NOTELP="notelp";
    private static final String TAG_LATITUDE="latitude";
    private static final String TAG_LONGITUDE="longitude";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_workshop_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        list_bengkel =new ArrayList<HashMap<String, String>>();
        lv = getActivity().findViewById(R.id.listdealer);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  getnama = ((TextView) view.findViewById(R.id.edtnama)).getText().toString();
                String  getalamat = ((TextView) view.findViewById(R.id.edtalamat)).getText().toString();
                DetailBengkel fragdetailbengkel = new DetailBengkel();
                Bundle bundle = new Bundle();
                bundle.putString("nama",getnama);
                bundle.putString("alamat",getalamat);
                fragdetailbengkel.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragdetailbengkel).commit();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlgetdatabengkel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray member= jObj.getJSONArray(TAG_USER);
                    //Toast.makeText(SemuaMahasiswaActivity.this, "sesudah tag", Toast.LENGTH_SHORT).show();

                    for (int i=0; i< member.length();i++) {
                        JSONObject a=member.getJSONObject(i);
                        String nama=a.getString(TAG_NAMA);
                        String alamat=a.getString(TAG_ALAMAT);

                        HashMap<String,String> map=new HashMap<>();
                        map.put("nama",nama);
                        map.put("alamat",alamat);
                        //Toast.makeText(getActivity().getApplicationContext(), "nama"+nama, Toast.LENGTH_SHORT).show();
                        list_bengkel.add(map);
                    }

                    //String[] from={"TAG_ID","TAG_NAMA"};//string array
                    String[] from={"nama","alamat"};//string array
                    int[] to={R.id.edtnama,R.id.edtalamat};//int array of views id's
                    ListAdapter adapter=new SimpleAdapter(
                            getActivity().getApplicationContext(),list_bengkel,R.layout.list_bengkel,
                            from, to);
                    lv.setAdapter(adapter);
                    //Toast.makeText(SemuaMahasiswaActivity.this, "setelah listview adapter", Toast.LENGTH_SHORT).show();

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
        });
        queue.add(stringRequest);

    }
}
