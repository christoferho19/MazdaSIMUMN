package com.project.simmazdaworkshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeUser extends Fragment {

    private  String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        username = getArguments().getString("username");
        return inflater.inflate(R.layout.activity_home_user,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();

    CardView crdmycar = (CardView)getActivity().findViewById(R.id.cardviewmycar);
    crdmycar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final MyCarPage fragmycar = new MyCarPage();
            fragmycar.setArguments(getActivity().getIntent().getExtras());
            Bundle profilebundle = new Bundle();
            profilebundle.putString("username",username);
            fragmycar.setArguments(profilebundle);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragmycar).commit();
        }
    });

    CardView crdmyservice = (CardView)getActivity().findViewById(R.id.cardservice);
    crdmyservice.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final MyServiceReservation fragmyservice = new MyServiceReservation();
            fragmyservice.setArguments(getActivity().getIntent().getExtras());
            Bundle profilebundle = new Bundle();
            profilebundle.putString("username",username);
            fragmyservice.setArguments(profilebundle);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragmyservice).commit();
        }
    });
    }
}
