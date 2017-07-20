package com.abourahal.michael.uottawa.teamup;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 * Created by hocke on 2017-07-16.
 */

public class FirstFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    MapView mMapView;
    View myView;
    Location lo;
    private GoogleApiClient gc;
    private FusedLocationProviderApi locationProvider = LocationServices.FusedLocationApi;
    private LocationRequest lr;
    private double latitude;
    private double longitude;
    private double selectedLatitude;
    private double selectedLongitude;
    private float zoom =14;
    private float bearing = 0;
    private int count = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.first_layout, container, false);
        gc = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        gc.connect();

        lr = new LocationRequest();
        lr.setInterval(60*1000);
        lr.setFastestInterval(15*1000);
        lr.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        //fab.set
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Adding Event", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                //NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                CreateEventFragment cr = new CreateEventFragment();
                Bundle b = new Bundle();
                b.putDouble("latitude",selectedLatitude);
                b.putDouble("longitude",selectedLongitude);
                cr.setArguments(b);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,cr).commit();

            }
        });
        return myView;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }
    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) myView.findViewById(R.id.mapView2);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getActivity());



        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.clear();
                    loadFile();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Event here?"));
                    selectedLatitude = latLng.latitude;
                    selectedLongitude = latLng.longitude;
                }
            });
        } else {

        Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
    }
    loadFile();

    }

    private void loadFile()
    {
        //writeToFile("",getActivity());
        String allActivity = readFromFile(getActivity());
        String[] allMarker = allActivity.split("\\-\\^\\-");
        for(int i=0;i<allMarker.length;++i)
        {
            String[] allFileItems = (allMarker[i]).split("\\|\\^\\|");
            if(!allFileItems[0].equals("")) {
                double lat = Double.parseDouble(allFileItems[0]);
                double lon = Double.parseDouble(allFileItems[1]);
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(allFileItems[3]));
            }
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdate();


    }
    private void requestLocationUpdate()
    {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(gc, lr, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        if(count ==0)
        {
            zoom= 14;
            bearing = 0;
            CameraPosition lib = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(zoom).bearing(bearing).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(lib));

        }


        count++;
    }

}

