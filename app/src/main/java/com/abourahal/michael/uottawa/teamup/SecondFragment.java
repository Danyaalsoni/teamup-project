package com.abourahal.michael.uottawa.teamup;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by hocke on 2017-07-16.
 */

public class SecondFragment extends Fragment {
    View myView;
    ArrayList<String> hostList;
    ArrayList<String> adapterHostList;
    ArrayList<String> nonHostList;
    ArrayList<String> adapterNonHostList;
    ArrayList<GoogleMap> mHostMap;
    ArrayList<GoogleMap> mNonHostMap;
    int countMap=0;
    int countPartMap=0;

    ArrayList<MapView> mapViews;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.second_layout,container,false);


        hostList = new ArrayList<String>();
        adapterHostList = new ArrayList<String>();
        nonHostList = new ArrayList<String>();
        adapterNonHostList = new ArrayList<String>();
        mHostMap = new ArrayList<GoogleMap>();
        mNonHostMap = new ArrayList<GoogleMap>();
        countPartMap=0;
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
                } else if(allFields[10].equals("JOINED")) {
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

                mHostMap.add(googleMap);
            mHostMap.get(countMap).setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mHostMap.get(countMap).getUiSettings().setAllGesturesEnabled(false);

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        String[] allItems = adapterHostList.get(countMap).split("\\|\\^\\|");
                        double lat = Double.parseDouble(allItems[0]);
                        double lon = Double.parseDouble(allItems[1]);
                    Marker m = mHostMap.get(countMap).addMarker(new MarkerOptions().position(new LatLng(lat,lon)));


                        if (!allItems[0].equals("")) {
                            if (allItems[8].equalsIgnoreCase("Hockey")) {
                                m.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.hockey));
                            } else if (allItems[8].equalsIgnoreCase("Soccer")) {
                                m.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.soccerballvariant));
                            } else if (allItems[8].equalsIgnoreCase("Football")) {
                                m.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.americanfootball));
                            } else if (allItems[8].equalsIgnoreCase("Baseball")) {
                                m.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.baseball));
                            } else {
                                m.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.basketball));
                            }
                        }

                    CameraPosition lib = CameraPosition.builder().target(new LatLng(lat, lon)).zoom(14).bearing(0).tilt(20).build();
                    mHostMap.get(countMap).moveCamera(CameraUpdateFactory.newCameraPosition(lib));


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

                convertView = getActivity().getLayoutInflater().inflate(R.layout.event_list, null);

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
                if (!allFileItems[0].equals("")) {

                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String file = readFromFile(getActivity());
                            String[] lines = file.split("\\-\\^\\-");
                            String result = "";
                            ArrayList<String> temp = new ArrayList<String>(hostList);
                            for (int i = 0; i < lines.length; ++i) {
                                if (!lines[i].equals((temp.get(position)))) {
                                    result = result + lines[i]+"-^-";;
                                } else {
                                    hostList.remove(position);
                                }
                            }
                            writeToFile(result, getActivity());
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

                            b.putString("line", hostList.get(position));

                            evf.setArguments(b);
                            fragmentManager.beginTransaction().replace(R.id.content_frame, evf).commit();
                        }
                    });
                    double lat = Double.parseDouble(allFileItems[0]);
                    double lon = Double.parseDouble(allFileItems[1]);
                    String title = allFileItems[2];
                    String max = allFileItems[3];
                    String date = allFileItems[4];
                    String start = allFileItems[5];
                    String end = allFileItems[6];
                    btn1.setText("Delete");
                    btn2.setText("Edit");
                    tv1.setText("Event Title: " + title + " \nSport: " + allFileItems[8]+ " \nMax Participant: "+max );
                    tv2.setText("Date: " + date + "\nStart Time: " + start + " End Time: " + end + " \nRecurring: "+allFileItems[9]+"\n" + allFileItems[7]);
                    Double[] latlng = new Double[2];
                    latlng[0] = lat;
                    latlng[1] = lon;
                    adapterHostList.add(hostList.get(position));
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

            mNonHostMap.add(googleMap);
            mNonHostMap.get(countPartMap).setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mNonHostMap.get(countPartMap).getUiSettings().setAllGesturesEnabled(false);

            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (!adapterNonHostList.get(countPartMap).equals("")) {
                    String[] allItems = adapterNonHostList.get(countPartMap).split("\\|\\^\\|");
                    double lat = Double.parseDouble(allItems[0]);
                    double lon = Double.parseDouble(allItems[1]);

                    Marker m = mNonHostMap.get(countPartMap).addMarker(new MarkerOptions().position(new LatLng(lat, lon)));

                    if (!allItems[0].equals("")) {
                        if (allItems[8].equalsIgnoreCase("Hockey")) {
                            m.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.hockey));
                        } else if (allItems[8].equalsIgnoreCase("Soccer")) {
                            m.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.soccerballvariant));
                        } else if (allItems[8].equalsIgnoreCase("Football")) {
                            m.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.americanfootball));
                        } else if (allItems[8].equalsIgnoreCase("Baseball")) {
                            m.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.baseball));
                        } else {
                            m.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.basketball));
                        }
                    }

                CameraPosition lib = CameraPosition.builder().target(new LatLng(lat, lon)).zoom(14).bearing(0).tilt(20).build();
                    mNonHostMap.get(countPartMap).moveCamera(CameraUpdateFactory.newCameraPosition(lib));
            }


        } else {

                Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
            }

            countPartMap++;

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
            Button btn2 = (Button) convertView.findViewById(R.id.btnEdit);
            btn1.setText("Leave");

            String[] allFileItems = (nonHostList.get(position)).split("\\|\\^\\|");
            if(!allFileItems[0].equals("")) {

                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String file = readFromFile(getActivity());
                        String[] lines = file.split("\\-\\^\\-");
                        String result = "";
                        ArrayList<String> temp = new ArrayList<String>(nonHostList);
                        for (int i = 0; i < lines.length; ++i) {
                            if (!lines[i].equals((temp.get(position)))) {
                                result = result + lines[i]+"-^-";
                            } else {
                                nonHostList.remove(position);
                            }
                        }
                        writeToFile(result, getActivity());
                        TabHost host = (TabHost) myView.findViewById(R.id.tabHost);
                        host.setCurrentTab(0);
                        host.setCurrentTab(1);

                    }
                });

            btn2.setVisibility(View.INVISIBLE);

                double lat = Double.parseDouble(allFileItems[0]);
                double lon = Double.parseDouble(allFileItems[1]);
                String title = allFileItems[2];
                String max = allFileItems[3];
                String date = allFileItems[4];
                String start = allFileItems[5];
                String end = allFileItems[6];
                btn1.setText("Delete");
                btn2.setText("Edit");
                tv1.setText("Event Title: " + title + " \nSport: " + allFileItems[8]+ " \nMax Participant: "+max );
                tv2.setText("Date: " + date + "\nStart Time: " + start + " End Time: " + end + " \nRecurring: "+allFileItems[9]+"\n" + allFileItems[7]);
                Double[] latlng = new Double[2];
                latlng[0] =lat;
                latlng[1] =lon;
                adapterNonHostList.add(nonHostList.get(position));
                //mMap.get(position).addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(allFileItems[3]));

                //CameraPosition lib = CameraPosition.builder().target(new LatLng(lat, lon)).zoom(14).bearing(0).tilt(20).build();
                //mMap.get(position).moveCamera(CameraUpdateFactory.newCameraPosition(lib));
            }
            return convertView;
        }


    }
}
