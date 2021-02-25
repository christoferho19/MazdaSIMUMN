package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenuStaff extends AppCompatActivity {

    private String username,namabengkel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_staff);

        username = getIntent().getStringExtra("username");
        namabengkel = getIntent().getStringExtra("namabengkel");

        final PendingReservationApproval fragpendingreserve = new PendingReservationApproval();
        fragpendingreserve.setArguments(getIntent().getExtras());

        BottomNavigationView bottomnav = findViewById(R.id.bottom_navmenu);
        bottomnav.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) navlistener);

        Bundle startbundle = new Bundle();
        startbundle.putString("username",username);
        startbundle.putString("namabengkel",namabengkel);
        fragpendingreserve.setArguments(startbundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragpendingreserve).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedfragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.navpending:
                            selectedfragment = new PendingReservationApproval();
                            Bundle pendingbundle = new Bundle();
                            pendingbundle.putString("username",username);
                            pendingbundle.putString("namabengkel",namabengkel);
                            selectedfragment.setArguments(pendingbundle);
                            break;
                        case R.id.navonprogress:
                            selectedfragment = new OnProgressReservation();
                            Bundle onpbundle = new Bundle();
                            onpbundle.putString("username",username);
                            onpbundle.putString("namabengkel",namabengkel);
                            selectedfragment.setArguments(onpbundle);
                            break;
                        case R.id.navdone:
                            selectedfragment = new CompletedReservation();
                            Bundle completebundle = new Bundle();
                            completebundle.putString("username",username);
                            completebundle.putString("namabengkel",namabengkel);
                            selectedfragment.setArguments(completebundle);
                            break;
                        case R.id.navreject:
                            selectedfragment = new RejectedReservation();
                            Bundle rejectbundle = new Bundle();
                            rejectbundle.putString("username",username);
                            rejectbundle.putString("namabengkel",namabengkel);
                            selectedfragment.setArguments(rejectbundle);
                            break;
                        case R.id.navsettingstaff:
                            selectedfragment = new SettingPageStaff();
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
