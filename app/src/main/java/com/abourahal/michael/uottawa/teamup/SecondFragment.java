package com.abourahal.michael.uottawa.teamup;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by hocke on 2017-07-16.
 */

public class SecondFragment extends Fragment {
    View myView;
    ArrayList<String> hostList;
    ArrayList<String> nonHostList;
    ArrayList<GoogleMap> mMap;
    int countMap=0;
    ArrayList<Double[]> coor1;
    ArrayList<Double[]> coor2;
    ArrayList<MapView> mapViews;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.second_layout,container,false);


        hostList = new ArrayList<String>();
        nonHostList = new ArrayList<String>();
        mMap = new ArrayList<GoogleMap>();
        coor1 = new ArrayList<Double[]>();
        coor2 = new ArrayList<Double[]>();
        countMap =0;

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        //coor.add(new String[2]);
        TabHost host = (TabHost) myView.findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Hosting");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Hosting");
        host.addTab(spec);

        spec = host.newTabSpec("Participating");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Participating");
        host.addTab(spec);

        String file = readFromFile(getActivity());
        String[] lines = file.split("\\-\\^\\-");
        for(int i=0;i< lines.length;++i)
        {
            if(!lines[i].equals("")) {
                String[] allFields = lines[i].split("\\|\\^\\|");
                if (allFields[10].equals("HOST")) {
                    hostList.add(lines[i]);
                } else if(allFields[10].equals("J0INED")) {
                    nonHostList.add(lines[i]);
                }
            }
        }
        //int pos = host.getCurrentTab();
        //if(pos == 0)
        if(hostList.size()!=0) {
            HostAdapter hostAdapter = new HostAdapter();
            ListView ls1 = (ListView)myView.findViewById(R.id.ls1);

            ls1.setAdapter(hostAdapter);
        }
       // else
        if(nonHostList.size()!=0) {
            ParticipatingAdapter partAdapter = new ParticipatingAdapter();
            ListView ls2 = (ListView)myView.findViewById(R.id.ls2);

            ls2.setAdapter(partAdapter);
        }
        return myView;
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

    public String readFromFile(Context context) {

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



    class HostAdapter extends BaseAdapter  implements OnMapReadyCallback{
        @Override
        public int getCount() {

            return hostList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public void onMapReady(GoogleMap googleMap) {

            MapsInitializer.initialize(getActivity());



            mMap.add(googleMap);
            mMap.get(countMap).setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.get(countMap).getUiSettings().setScrollGesturesEnabled(false);
            mMap.get(countMap).getUiSettings().setZoomControlsEnabled(false);
            mMap.get(countMap).getUiSettings().setTiltGesturesEnabled(false);

            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mMap.get(countMap).addMarker(new MarkerOptions().position(new LatLng(coor1.get(countMap)[0], coor1.get(countMap)[1])));

                CameraPosition lib = CameraPosition.builder().target(new LatLng(coor1.get(countMap)[0], coor1.get(countMap)[1])).zoom(14).bearing(0).tilt(20).build();
                mMap.get(countMap).moveCamera(CameraUpdateFactory.newCameraPosition(lib));

            } else {

                Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
            }

            countMap++;

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getActivity().getLayoutInflater().inflate(R.layout.event_list,null);
            MapView mp = (MapView) convertView.findViewById(R.id.mapView3);

            if (mp != null) {
                mp.onCreate(null);
                mp.onResume();
                mp.getMapAsync(this);

            }
            TextView tv1 = (TextView) convertView.findViewById(R.id.etListTitle);
            TextView tv2 = (TextView) convertView.findViewById(R.id.etDateTime);
            Button btn1 = (Button) convertView.findViewById(R.id.btnAction);


            String[] allFileItems = (hostList.get(position)).split("\\|\\^\\|");
                if(!allFileItems[0].equals("")) {

                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String file = readFromFile(getActivity());
                            String[] lines = file.split("\\-\\^\\-");
                            String result ="";
                            ArrayList<String> temp = new ArrayList<String>(hostList);
                            for(int i=0;i<lines.length;++i)
                            {
                                if(!lines[i].equals((temp.get(position))))
                                {
                                    result = result+ lines[i];
                                }
                                else
                                {
                                    hostList.remove(position);
                                }
                            }
                            writeToFile(result,getActivity());
                            TabHost host = (TabHost) myView.findViewById(R.id.tabHost);
                            host.setCurrentTab(1);
                            host.setCurrentTab(0);

                        }
                    });

                    Button btn2 = (Button) convertView.findViewById(R.id.btnEdit);
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager fragmentManager = getFragmentManager();
                            EditEventFragment evf = new EditEventFragment();
                            Bundle b = new Bundle();

                            b.putString("line",hostList.get(position));

                            evf.setArguments(b);
                            fragmentManager.beginTransaction().replace(R.id.content_frame,evf).commit();
                        }
                    });
                    double lat = Double.parseDouble(allFileItems[0]);
                    double lon = Double.parseDouble(allFileItems[1]);
                    String title = allFileItems[2];
                    String date = allFileItems[3];
                    String sTime = allFileItems[4];
                    String eTime = allFileItems[5];
                    btn1.setText("Delete");
                    btn2.setText("Edit");
                    tv1.setText("Event Title: "+title+ " Sport: "+allFileItems[8]);
                    tv2.setText("Date: "+date+" Start Time: "+sTime+" End Time: "+eTime+" \n"+allFileItems[7]);
                    Double[] latlng = new Double[2];
                    latlng[0] =lat;
                    latlng[1] =lon;

                    coor1.add(latlng);
                    //mMap.get(position).addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(allFileItems[3]));

                    //CameraPosition lib = CameraPosition.builder().target(new LatLng(lat, lon)).zoom(14).bearing(0).tilt(20).build();
                    //mMap.get(position).moveCamera(CameraUpdateFactory.newCameraPosition(lib));
                }
            return convertView;
        }
    }
    class ParticipatingAdapter extends BaseAdapter  implements OnMapReadyCallback{
        @Override
        public int getCount() {

            return nonHostList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public void onMapReady(GoogleMap googleMap) {

            MapsInitializer.initialize(getActivity());



            mMap.add(googleMap);
            mMap.get(countMap).setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.get(countMap).getUiSettings().setScrollGesturesEnabled(false);
            mMap.get(countMap).getUiSettings().setZoomControlsEnabled(false);
            mMap.get(countMap).getUiSettings().setTiltGesturesEnabled(false);

            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mMap.get(countMap).addMarker(new MarkerOptions().position(new LatLng(coor2.get(countMap)[0], coor2.get(countMap)[1])));

                CameraPosition lib = CameraPosition.builder().target(new LatLng(coor2.get(countMap)[0], coor2.get(countMap)[1])).zoom(14).bearing(0).tilt(20).build();
                mMap.get(countMap).moveCamera(CameraUpdateFactory.newCameraPosition(lib));

            } else {

                Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
            }

            countMap++;

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getActivity().getLayoutInflater().inflate(R.layout.event_list,null);
            MapView mp = (MapView) convertView.findViewById(R.id.mapView3);

            if (mp != null) {
                mp.onCreate(null);
                mp.onResume();
                mp.getMapAsync(this);

            }
            TextView tv1 = (TextView) convertView.findViewById(R.id.etListTitle);
            TextView tv2 = (TextView) convertView.findViewById(R.id.etDateTime);
            Button btn1 = (Button) convertView.findViewById(R.id.btnAction);
            Button btn2 = (Button) convertView.findViewById(R.id.btnEdit);
            String[] allFileItems = (nonHostList.get(position)).split("\\|\\^\\|");
            if(!allFileItems[0].equals("")) {
                double lat = Double.parseDouble(allFileItems[0]);
                double lon = Double.parseDouble(allFileItems[1]);
                String title = allFileItems[2];
                String date = allFileItems[3];
                String sTime = allFileItems[4];
                String eTime = allFileItems[5];
                btn1.setText("Leave");
                btn2.setVisibility(View.INVISIBLE);
                //btn2.setText("Edit");
                tv1.setText("Event Title: "+title+ " Sport: "+allFileItems[8]);
                tv2.setText("Date: "+date+" Start Time: "+sTime+" End Time: "+eTime);
                Double[] latlng = new Double[2];
                latlng[0] =lat;
                latlng[1] =lon;

                coor2.add(latlng);
                //mMap.get(position).addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(allFileItems[3]));

                //CameraPosition lib = CameraPosition.builder().target(new LatLng(lat, lon)).zoom(14).bearing(0).tilt(20).build();
                //mMap.get(position).moveCamera(CameraUpdateFactory.newCameraPosition(lib));
            }
            return convertView;
        }
    }
}
