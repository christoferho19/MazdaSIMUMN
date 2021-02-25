package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.nio.channels.SelectableChannel;

public class MainMenuUser extends AppCompatActivity {

    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_user);

        username = getIntent().getStringExtra("username");

        final HomeUser fraghomeuser = new HomeUser();
        fraghomeuser.setArguments(getIntent().getExtras());

        BottomNavigationView bottomnav = findViewById(R.id.bottom_navmenu);
        bottomnav.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) navlistener);

        Bundle startbundle = new Bundle();
        startbundle.putString("username",username);
        fraghomeuser.setArguments(startbundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fraghomeuser).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedfragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.navhome:
                            selectedfragment = new HomeUser();
                            Bundle homebundle = new Bundle();
                            homebundle.putString("username",username);
                            selectedfragment.setArguments(homebundle);
                            break;
                        case R.id.navprofile:
                            selectedfragment = new ProfilePageUser();
                            Bundle profilebundle = new Bundle();
                            profilebundle.putString("username",username);
                            selectedfragment.setArguments(profilebundle);
                            break;
                        case R.id.navlocation:
                            selectedfragment = new WorkshopList();
                            break;
                        case R.id.navsetting:
                            selectedfragment = new SettingPageUser();
                            Bundle settingbundle = new Bundle();
                            settingbundle.putString("username",username);
                            selectedfragment.setArguments(settingbundle);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,
                            selectedfragment).commit();
                    return true;
                }
            };
}
