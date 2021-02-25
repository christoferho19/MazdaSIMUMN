package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SettingPageStaff extends Fragment {
    private Button btnlogout,btnchangepassword,btnchangeworkshop,btnadduser;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_setting_page_staff, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        btnadduser = (Button)getActivity().findViewById(R.id.btnaddnewsatf);
        btnadduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),RegisterStaff.class);
                startActivity(i);
            }
        });
        btnchangepassword = (Button)getActivity().findViewById(R.id.btnchangepassword);
        btnchangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword fragchangepassword = new ChangePassword();
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                fragchangepassword.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragchangepassword).commit();
            }
        });

        btnchangeworkshop = (Button)getActivity().findViewById(R.id.btnchangeworkshop);
        btnchangeworkshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),LoginWorkshop.class);
                i.putExtra("username",username);
                startActivity(i);
                getActivity().finish();
            }
        });

        btnlogout = (Button)getActivity().findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(),Loginpage.class);
                startActivity(i);
                getActivity().finish();
                Toast.makeText(getActivity().getApplicationContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
