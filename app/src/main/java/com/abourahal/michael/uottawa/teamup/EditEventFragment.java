package com.abourahal.michael.uottawa.teamup;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by hocke on 2017-07-19.
 */

public class EditEventFragment  extends Fragment implements OnMapReadyCallback {
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_create_event,container,false);

        return myView;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
