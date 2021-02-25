package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SettingPageUser extends Fragment {

    private SwitchCompat switch1, switch2;
    private boolean stateswitch1,stateswitch2;
    private Button btnlogout,btnchangepassword,btnchangeemail;

    private String username;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_setting_page_user, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        final SharedPreferences preferences = getActivity().getSharedPreferences("PREFS",0);
        stateswitch1 = preferences.getBoolean("switch1",false);
        stateswitch2 = preferences.getBoolean("switch2",false);



        switch1 = (SwitchCompat)getActivity().findViewById(R.id.settingoption1);
        switch2 = (SwitchCompat)getActivity().findViewById(R.id.settingoption2);

        switch1.setChecked(stateswitch1);
        switch2.setChecked(stateswitch2);

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateswitch1 = !stateswitch1;
                switch1.setChecked(stateswitch1);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch1",stateswitch1);
                editor.apply();

                Toast.makeText(getActivity().getApplicationContext(), "Notification Setting Changed", Toast.LENGTH_SHORT).show();
            }
        });

        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateswitch2 = !stateswitch2;
                switch2.setChecked(stateswitch2);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switch2",stateswitch2);
                editor.apply();
                Toast.makeText(getActivity().getApplicationContext(), "Dark Mode Setting Changed", Toast.LENGTH_SHORT).show();
            }
        });

        btnchangeemail = (Button)getActivity().findViewById(R.id.btnchangeemail);
        btnchangeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeEmail fragchangeemail = new ChangeEmail();
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                fragchangeemail.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragchangeemail).commit();
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
