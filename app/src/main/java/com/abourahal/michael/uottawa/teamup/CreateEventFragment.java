package com.abourahal.michael.uottawa.teamup;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

/**
 * Created by hocke on 2017-07-17.
 */

public class CreateEventFragment extends Fragment implements OnMapReadyCallback{
    View myView;
    private GoogleMap mMap;
    MapView mMapView;
    double latitude;
    double longitude;
    private Button etDate;
    private Button etStartTime;
    private Button etEndTime;
    private int year,month,day;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_create_event,container,false);
        //writeToFile("", getActivity());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            latitude = bundle.getDouble("latitude", 0);
            longitude = bundle.getDouble("longitude", 0);
        }
        Spinner spSport = (Spinner) getActivity().findViewById(R.id.spSport);
        etDate = (Button) myView.findViewById(R.id.etDate);
        etStartTime = (Button) myView.findViewById(R.id.etStartTime);
        etEndTime = (Button) myView.findViewById(R.id.etEndTime);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c=Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        etStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etStartTime.setText(String.format("%02d:%02d",selectedHour , selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        etEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etEndTime.setText(String.format("%02d:%02d",selectedHour , selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        //fab.set
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText etDescription = (EditText) getActivity().findViewById(R.id.etDescription);
                Spinner spSport = (Spinner) getActivity().findViewById(R.id.spSport);
                CheckBox chkRepeat = (CheckBox) getActivity().findViewById(R.id.chkRepeat);
                boolean flag=true;
                final EditText etTitle = (EditText) getActivity().findViewById(R.id.etTitle);
                final EditText etMaxNumber = (EditText) getActivity().findViewById(R.id.etMaxNumber);
                final Button etDate = (Button) getActivity().findViewById(R.id.etDate);


                if(etTitle.getText().toString().equals("")&&etMaxNumber.getText().toString().equals("")&&etDate.getText().toString().equals("")
                        &&etStartTime.getText().toString().equals("")&&etEndTime.getText().toString().equals("")){
                    flag=false;
                    AlertDialog.Builder dialog=new AlertDialog.Builder(myView.getContext());
                    dialog.setMessage("Please enter all the missing information");
                    dialog.setTitle("Missing information");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etTitle.requestFocus();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.create().show();
                }
                else if(etTitle.getText().toString().equals("")){
                    flag=false;
                    AlertDialog.Builder dialog=new AlertDialog.Builder(myView.getContext());
                    dialog.setMessage("Please enter the Event Title");
                    dialog.setTitle("Missing information");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etTitle.requestFocus();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.create().show();
                }
                else if(etMaxNumber.getText().toString().equals("")){
                    flag=false;
                    AlertDialog.Builder dialog=new AlertDialog.Builder(myView.getContext());
                    dialog.setMessage("Please enter the Number of Members");
                    dialog.setTitle("Missing information");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etMaxNumber.requestFocus();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.create().show();
                }
                else if(etDate.getText().toString().equals("")) {
                    flag = false;
                    AlertDialog.Builder dialog = new AlertDialog.Builder(myView.getContext());
                    dialog.setMessage("Please enter the Date");
                    dialog.setTitle("Missing information");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etDate.requestFocus();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.create().show();
                }
                else if(etStartTime.getText().toString().equals("")){
                    flag=false;
                    AlertDialog.Builder dialog=new AlertDialog.Builder(myView.getContext());
                    dialog.setMessage("Please enter the Start Time");
                    dialog.setTitle("Missing information");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etStartTime.requestFocus();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.create().show();
                }
                else if(etEndTime.getText().toString().equals("")){
                    flag=false;
                    AlertDialog.Builder dialog=new AlertDialog.Builder(myView.getContext());
                    dialog.setMessage("Please enter the End Time");
                    dialog.setTitle("Missing information");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            etEndTime.requestFocus();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.create().show();
                }else{
                    flag=true;
                }
                boolean check = chkRepeat.isChecked();
                String checkString ="";
                if(check)
                checkString="yes";
                else
                checkString="no";
                if(flag==true) {
                    //writeToFile("", getActivity());
                    String data =  latitude+"|^|"+longitude+"|^|"+etTitle.getText().toString()+"|^|"+etMaxNumber.getText().toString()+"|^|"+etDate.getText().toString()+"|^|"+etStartTime.getText().toString()+"|^|"+etEndTime.getText().toString()+"|^|"+etDescription.getText().toString()+"|^|"+spSport.getSelectedItem().toString()+"|^|"+checkString+"|^|"+"HOST"+"-^-";
                    String previousData = readFromFile(getActivity());
                    writeToFile(previousData + data, getActivity());
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new FirstFragment()).commit();
                    Toast.makeText(getActivity(), "Event added at coordinates " + latitude + " latitude, " + longitude + " longitude", Toast.LENGTH_LONG).show();
                }


            }
        });

        return myView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) myView.findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


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
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("New Event"));
            CameraPosition lib = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(14).bearing(0).tilt(20).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(lib));

        } else {

            Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
        }



    }



}