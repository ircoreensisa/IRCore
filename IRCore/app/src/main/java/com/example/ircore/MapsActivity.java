package com.example.ircore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.ircore.cloudanchor.CloudAnchorFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location myLocation ;
    private int etage ;
    private int etage_dest;
    private int zone_dest ;
    private int zone ;
    private boolean climbing;
    private Salles salles;
    private Handler handler;
    private Runnable runnable;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View v = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        v.setAlpha(0.5f); // Change this value to set the desired alpha

        etage = 1;
        zone = 2;
        salles = new Salles();

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.salles_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String salle = (String)parent.getItemAtPosition(position);
                Iterator<Salle> i = salles.getList().iterator();
                while(i.hasNext()) {
                    Salle current_salle = i.next() ;
                    if(current_salle.getName().equalsIgnoreCase(salle)) {
                        etage_dest = current_salle.getfloor() ;
                        zone_dest = current_salle.getzone() ;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(etage == 1) {
                    if(zone==1) ((TextView)findViewById(R.id.Location)).setText("Rez de chaussée , Amphis");
                    if(zone==2) ((TextView)findViewById(R.id.Location)).setText("Rez de chaussée , Hall");
                    if(zone==4) ((TextView)findViewById(R.id.Location)).setText("Rez de chaussée , Amicale");
                    if(zone==3) ((TextView)findViewById(R.id.Location)).setText("Rez de chaussée , Salles TP physique");
                }
                if(etage == 2) {
                    if(zone==1) ((TextView)findViewById(R.id.Location)).setText("Etage 2 , Scolarité");
                    if(zone==2) ((TextView)findViewById(R.id.Location)).setText("Etage 2 , Hall");
                    if(zone==3) ((TextView)findViewById(R.id.Location)).setText("Etage 2 , Salles Informatiques");
                    if(zone==4) ((TextView)findViewById(R.id.Location)).setText("Etage 2 , E20 - E21");
                    if(zone==6) ((TextView)findViewById(R.id.Location)).setText("Etage 2 , E22 - E25");
                    if(zone==5) ((TextView)findViewById(R.id.Location)).setText("Etage 2 , Bureaux Professeurs");
                    if(zone==7) ((TextView)findViewById(R.id.Location)).setText("Etage 2 , Bureaux Professeurs");
                }
                if(etage == 3) {
                    if(zone==2) ((TextView)findViewById(R.id.Location)).setText("Etage 3 , Hall");
                    if(zone==4) ((TextView)findViewById(R.id.Location)).setText("Etage 3 , E30 - E32");
                    if(zone==6) ((TextView)findViewById(R.id.Location)).setText("Etage 3 , E33 - E37");
                    if(zone==5) ((TextView)findViewById(R.id.Location)).setText("Etage 3 , Bureaux Professeurs");
                    if(zone==7) ((TextView)findViewById(R.id.Location)).setText("Etage 3 , Bureaux Professeurs");
                }
                handler.postDelayed(this,500);
            }
        };

        handler.postDelayed(runnable,500);
    }



    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        findViewById(R.id.Etage1_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.Etage2_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.Etage3_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.menu_sous).setVisibility(View.INVISIBLE);

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        int smallWidth = display.widthPixels - (3*display.widthPixels/5);
        int smallHeight = display.heightPixels - (2*display.heightPixels/3);
        View v = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        v.setLayoutParams(new RelativeLayout.LayoutParams(smallWidth,smallHeight));

        mMap.setMinZoomPreference(17);
        mMap.setIndoorEnabled(true);
        this.buildEnsisaLimit();




        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            if(!mMap.isMyLocationEnabled())
                mMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 2, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    myLocation = location ;
                    LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
                    if(etage == 1) {
                        if(latlng.latitude < 47.729807 && latlng.latitude > 47.729484 && latlng.longitude < 7.310883 && latlng.longitude > 7.310473) {
                            ((TextView)findViewById(R.id.Location)).setText("Rez de chaussée , Amphis");
                            zone = 1 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.729604 && latlng.latitude > 47.729193 && latlng.longitude < 7.310689 && latlng.longitude > 7.310182) {
                            ((TextView)findViewById(R.id.Location)).setText("Rez de chaussée , Hall");
                            zone = 2 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.729325 && latlng.latitude > 47.729126 && latlng.longitude < 7.310303 && latlng.longitude > 7.310032) {
                            ((TextView)findViewById(R.id.Location)).setText("Rez de chaussée , Amicale");
                            zone = 4 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.729342 && latlng.latitude > 47.729001 && latlng.longitude < 7.310926 && latlng.longitude > 7.310405) {
                            ((TextView)findViewById(R.id.Location)).setText("Rez de chaussée , Salles TP physique");
                            zone = 3 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                    }
                    if(etage == 2) {
                        if(latlng.latitude < 47.729807 && latlng.latitude > 47.729484 && latlng.longitude < 7.310883 && latlng.longitude > 7.310473) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 2 , Scolarité");
                            zone = 1 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.729604 && latlng.latitude > 47.729193 && latlng.longitude < 7.310689 && latlng.longitude > 7.310182) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 2 , Hall");
                            zone = 2 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.729604 && latlng.latitude > 47.729193 && latlng.longitude < 7.310926 && latlng.longitude > 7.310405) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 2 , Salles Informatiques");
                            zone = 3 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.729325 && latlng.latitude > 47.728861 && latlng.longitude < 7.310303 && latlng.longitude > 7.309834) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 2 , E20 - E21");
                            zone = 4 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.729070 && latlng.latitude > 47.728739 && latlng.longitude < 7.310163 && latlng.longitude > 7.309694) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 2 , E22 - E25");
                            zone = 6 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.729090 && latlng.latitude > 47.728866 && latlng.longitude < 7.310478 && latlng.longitude > 7.310163) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 2 , Bureaux Professeurs");
                            zone = 5 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.728861 && latlng.latitude > 47.728628 && latlng.longitude < 7.310364 && latlng.longitude > 7.309942) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 2 , Bureaux Professeurs");
                            zone = 7 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                    }
                    if(etage == 3) {
                        if(latlng.latitude < 47.729423 && latlng.latitude > 47.729270 && latlng.longitude < 7.310402 && latlng.longitude > 7.310185) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 3 , Hall");
                            zone = 2 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.729325 && latlng.latitude > 47.728861 && latlng.longitude < 7.310303 && latlng.longitude > 7.309834) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 3 , E30 - E32");
                            zone = 4 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.729070 && latlng.latitude > 47.728739 && latlng.longitude < 7.310163 && latlng.longitude > 7.309694) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 3 , E33 - E37");
                            zone = 6 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.729090 && latlng.latitude > 47.728866 && latlng.longitude < 7.310478 && latlng.longitude > 7.310163) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 3 , Bureaux Professeurs");
                            zone = 5 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                        if(latlng.latitude < 47.728861 && latlng.latitude > 47.728628 && latlng.longitude < 7.310364 && latlng.longitude > 7.309942) {
                            ((TextView)findViewById(R.id.Location)).setText("Etage 3 , Bureaux Professeurs");
                            zone = 7 ;
                            CloudAnchorFragment frag = (CloudAnchorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
                            frag.setZone(zone);
                        }
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.729309,7.310529), 17), 1000, null);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                View v = getSupportFragmentManager().findFragmentById(R.id.map).getView();

                DisplayMetrics display = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(display);
                int smallWidth = display.widthPixels - (3*display.widthPixels/5);
                int smallHeight = display.heightPixels - (2*display.heightPixels/3);

                if(v.getLayoutParams().height==display.heightPixels) {
                    findViewById(R.id.Etage1_layout).setVisibility(View.INVISIBLE);
                    findViewById(R.id.Etage2_layout).setVisibility(View.INVISIBLE);
                    findViewById(R.id.Etage3_layout).setVisibility(View.INVISIBLE);
                    findViewById(R.id.menu_sous).setVisibility(View.INVISIBLE);
                    findViewById(R.id.Currentlocation).setVisibility(View.INVISIBLE);
                    findViewById(R.id.Textguide).setVisibility(View.INVISIBLE);
                    findViewById(R.id.Location).setVisibility(View.INVISIBLE);
                    v.setLayoutParams(new RelativeLayout.LayoutParams(smallWidth,smallHeight));
                    v.setAlpha(0.5f);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.729309,7.310529), 17), 1000, null);
                }
                else {
                    findViewById(R.id.menu_sous).setVisibility(View.VISIBLE);
                    findViewById(R.id.Currentlocation).setVisibility(View.VISIBLE);
                    findViewById(R.id.Textguide).setVisibility(View.VISIBLE);
                    findViewById(R.id.Location).setVisibility(View.VISIBLE);
                    v.setLayoutParams(new RelativeLayout.LayoutParams(display.widthPixels,display.heightPixels));
                    v.setAlpha(0.7f);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.729309,7.310529), 18), 1000, null);
                }

            }
        });

    }


    public void buildEnsisaLimit() {
        PolylineOptions limite = new PolylineOptions();
        limite.add(new LatLng(47.729809,7.310873));
        limite.add(new LatLng(47.729805,7.310689));
        limite.add(new LatLng(47.729485,7.310415));
        limite.add(new LatLng(47.729508,7.310357));
        limite.add(new LatLng(47.729587,7.310429));
        limite.add(new LatLng(47.729610,7.310376));
        limite.add(new LatLng(47.729470,7.310170));
        limite.add(new LatLng(47.729422,7.310331));
        limite.add(new LatLng(47.729298,7.310147));
        limite.add(new LatLng(47.728767,7.309690));
        limite.add(new LatLng(47.728739, 7.309782));
        limite.add(new LatLng(47.728769,7.309812));
        limite.add(new LatLng(47.728759,7.309837));
        limite.add(new LatLng(47.728785,7.309858));
        limite.add(new LatLng(47.728722,7.310016));
        limite.add(new LatLng(47.728734,7.310021));
        limite.add(new LatLng(47.728698, 7.310124));
        limite.add(new LatLng(47.728681,7.310116));
        limite.add(new LatLng(47.728621,7.310269));
        limite.add(new LatLng(47.728737,7.310374));
        limite.add(new LatLng(47.728860,7.310058));
        limite.add(new LatLng(47.728984,7.310164));
        limite.add(new LatLng(47.728858,7.310475));
        limite.add(new LatLng(47.728973,7.310572));
        limite.add(new LatLng(47.729126,7.310183));
        limite.add(new LatLng(47.729269,7.310300));
        limite.add(new LatLng(47.729269,7.310320));
        limite.add(new LatLng(47.729253,7.310317));
        limite.add(new LatLng(47.729243,7.310325));
        limite.add(new LatLng(47.729234,7.310352));
        limite.add(new LatLng(47.729239,7.310379));
        limite.add(new LatLng(47.729247,7.310375));
        limite.add(new LatLng(47.729268,7.310385));
        limite.add(new LatLng(47.729269,7.310401));
        limite.add(new LatLng(47.729191,7.310402));
        limite.add(new LatLng(47.728995,7.310932));
        limite.add(new LatLng(47.729809,7.310873));
        limite.color(Color.BLACK);
        mMap.addPolyline(limite);

        if(etage == 1) {
            mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729342, 7.310503))
                    .add(new LatLng(47.729193, 7.310405))
                    .add(new LatLng(47.729001, 7.310926))
                    .add(new LatLng(47.729214, 7.310917))
                    .add(new LatLng(47.729342, 7.310503))
                    .fillColor(Color.BLUE)
                    .strokeColor(Color.TRANSPARENT));

            mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729342, 7.310503))
                    .add(new LatLng(47.729484, 7.310689))
                    .add(new LatLng(47.729546, 7.310473))
                    .add(new LatLng(47.729485, 7.310420))
                    .add(new LatLng(47.729509, 7.310355))
                    .add(new LatLng(47.729586, 7.310420))
                    .add(new LatLng(47.729604, 7.310377))
                    .add(new LatLng(47.729474, 7.310182))
                    .add(new LatLng(47.729422, 7.310334))
                    .add(new LatLng(47.729445, 7.310277))
                    .add(new LatLng(47.729422, 7.310334))
                    .add(new LatLng(47.729325, 7.310185))
                    .add(new LatLng(47.729270, 7.310303))
                    .add(new LatLng(47.729271, 7.310401))
                    .add(new LatLng(47.729193, 7.310405))
                    .add(new LatLng(47.729342, 7.310503))
                    .fillColor(Color.RED)
                    .strokeColor(Color.TRANSPARENT));

            mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729484, 7.310689))
                    .add(new LatLng(47.729598, 7.310883))
                    .add(new LatLng(47.729807, 7.310867))
                    .add(new LatLng(47.729805, 7.310692))
                    .add(new LatLng(47.729546, 7.310473))
                    .fillColor(Color.LTGRAY)
                    .strokeColor(Color.TRANSPARENT));

            mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729270, 7.310303))
                    .add(new LatLng(47.729325, 7.310185))
                    .add(new LatLng(47.729169, 7.310032))
                    .add(new LatLng(47.729126, 7.310179))
                    .add(new LatLng(47.729270, 7.310303))
                    .fillColor(Color.CYAN)
                    .strokeColor(Color.TRANSPARENT));

            mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729271, 7.310320))
                    .add(new LatLng(47.729257, 7.310319))
                    .add(new LatLng(47.729248, 7.310324))
                    .add(new LatLng(47.729240, 7.310348))
                    .add(new LatLng(47.729244, 7.310371))
                    .add(new LatLng(47.729250, 7.310378))
                    .add(new LatLng(47.729270, 7.310385))
                    .add(new LatLng(47.729271, 7.310320))
                    .fillColor(Color.YELLOW)
                    .strokeColor(Color.TRANSPARENT));
        }
        if(etage == 2) {
                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729342, 7.310503))
                        .add(new LatLng(47.729193, 7.310405))
                        .add(new LatLng(47.729001, 7.310926))
                        .add(new LatLng(47.729214, 7.310917))
                        .add(new LatLng(47.729342, 7.310503))
                        .fillColor(Color.BLUE)
                        .strokeColor(Color.TRANSPARENT));
                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729342, 7.310503))
                        .add(new LatLng(47.729484, 7.310689))
                        .add(new LatLng(47.729546, 7.310473))
                        .add(new LatLng(47.729485, 7.310420))
                        .add(new LatLng(47.729509, 7.310355))
                        .add(new LatLng(47.729586, 7.310420))
                        .add(new LatLng(47.729604, 7.310377))
                        .add(new LatLng(47.729474, 7.310182))
                        .add(new LatLng(47.729422, 7.310334))
                        .add(new LatLng(47.729445, 7.310277))
                        .add(new LatLng(47.729422, 7.310334))
                        .add(new LatLng(47.729325, 7.310185))
                        .add(new LatLng(47.729270, 7.310303))
                        .add(new LatLng(47.729271, 7.310401))
                        .add(new LatLng(47.729193, 7.310405))
                        .add(new LatLng(47.729342, 7.310503))
                        .fillColor(Color.RED)
                        .strokeColor(Color.TRANSPARENT));

                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729484, 7.310689))
                        .add(new LatLng(47.729598, 7.310883))
                        .add(new LatLng(47.729807, 7.310867))
                        .add(new LatLng(47.729805, 7.310692))
                        .add(new LatLng(47.729546, 7.310473))
                        .fillColor(Color.LTGRAY)
                        .strokeColor(Color.TRANSPARENT));

                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729270, 7.310303))
                        .add(new LatLng(47.729325, 7.310185))
                        .add(new LatLng(47.728933, 7.309834))
                        .add(new LatLng(47.728861, 7.310053))
                        .add(new LatLng(47.729090, 7.310269))
                        .add(new LatLng(47.729126, 7.310179))
                        .add(new LatLng(47.729270, 7.310303))
                        .fillColor(Color.CYAN)
                        .strokeColor(Color.TRANSPARENT));

                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.728986, 7.310163))
                        .add(new LatLng(47.729070, 7.309949))
                        .add(new LatLng(47.728772, 7.309694))
                        .add(new LatLng(47.728739, 7.309782))
                        .add(new LatLng(47.728771, 7.309811))
                        .add(new LatLng(47.728764, 7.309832))
                        .add(new LatLng(47.728787, 7.309855))
                        .add(new LatLng(47.728752, 7.309942))
                        .add(new LatLng(47.728861, 7.310053))
                        .fillColor(Color.GRAY)
                        .strokeColor(Color.TRANSPARENT));

                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.728861, 7.310053))
                        .add(new LatLng(47.728752, 7.309942))
                        .add(new LatLng(47.728729, 7.310010))
                        .add(new LatLng(47.728738, 7.310019))
                        .add(new LatLng(47.728698, 7.310124))
                        .add(new LatLng(47.728687, 7.310117))
                        .add(new LatLng(47.728628, 7.310269))
                        .add(new LatLng(47.728737, 7.310364))
                        .add(new LatLng(47.728861, 7.310053))
                        .fillColor(Color.MAGENTA)
                        .strokeColor(Color.TRANSPARENT));


                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.728986, 7.310163))
                        .add(new LatLng(47.729090, 7.310269))
                        .add(new LatLng(47.728975, 7.310570))
                        .add(new LatLng(47.728866, 7.310478))
                        .add(new LatLng(47.728986, 7.310163))
                        .fillColor(Color.GREEN)
                        .strokeColor(Color.TRANSPARENT));

                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729271, 7.310320))
                        .add(new LatLng(47.729257, 7.310319))
                        .add(new LatLng(47.729248, 7.310324))
                        .add(new LatLng(47.729240, 7.310348))
                        .add(new LatLng(47.729244, 7.310371))
                        .add(new LatLng(47.729250, 7.310378))
                        .add(new LatLng(47.729270, 7.310385))
                        .add(new LatLng(47.729271, 7.310320))
                        .fillColor(Color.YELLOW)
                        .strokeColor(Color.TRANSPARENT));
        }
        if(etage == 3) {

                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729270, 7.310303))
                        .add(new LatLng(47.729325, 7.310185))
                        .add(new LatLng(47.728933, 7.309834))
                        .add(new LatLng(47.728861, 7.310053))
                        .add(new LatLng(47.729090, 7.310269))
                        .add(new LatLng(47.729126, 7.310179))
                        .add(new LatLng(47.729270, 7.310303))
                        .fillColor(Color.CYAN)
                        .strokeColor(Color.TRANSPARENT));

                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.728986, 7.310163))
                        .add(new LatLng(47.729070, 7.309949))
                        .add(new LatLng(47.728772, 7.309694))
                        .add(new LatLng(47.728739, 7.309782))
                        .add(new LatLng(47.728771, 7.309811))
                        .add(new LatLng(47.728764, 7.309832))
                        .add(new LatLng(47.728787, 7.309855))
                        .add(new LatLng(47.728752, 7.309942))
                        .add(new LatLng(47.728861, 7.310053))
                        .fillColor(Color.GRAY)
                        .strokeColor(Color.TRANSPARENT));

                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.728861, 7.310053))
                        .add(new LatLng(47.728752, 7.309942))
                        .add(new LatLng(47.728729, 7.310010))
                        .add(new LatLng(47.728738, 7.310019))
                        .add(new LatLng(47.728698, 7.310124))
                        .add(new LatLng(47.728687, 7.310117))
                        .add(new LatLng(47.728628, 7.310269))
                        .add(new LatLng(47.728737, 7.310364))
                        .add(new LatLng(47.728861, 7.310053))
                        .fillColor(Color.MAGENTA)
                        .strokeColor(Color.TRANSPARENT));

                mMap.addPolygon(new PolygonOptions().add(new LatLng( 47.729423, 7.310332))
                        .add(new LatLng(47.729325, 7.310185))
                        .add(new LatLng(47.729270, 7.310303))
                        .add(new LatLng(47.729271, 7.310402))
                        .add(new LatLng(47.729423, 7.310332))
                        .fillColor(Color.RED)
                        .strokeColor(Color.TRANSPARENT));

                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.728986, 7.310163))
                        .add(new LatLng(47.729090, 7.310269))
                        .add(new LatLng(47.728975, 7.310570))
                        .add(new LatLng(47.728866, 7.310478))
                        .add(new LatLng(47.728986, 7.310163))
                        .fillColor(Color.GREEN)
                        .strokeColor(Color.TRANSPARENT));

                mMap.addPolygon(new PolygonOptions().add(new LatLng(47.729271, 7.310320))
                        .add(new LatLng(47.729257, 7.310319))
                        .add(new LatLng(47.729248, 7.310324))
                        .add(new LatLng(47.729240, 7.310348))
                        .add(new LatLng(47.729244, 7.310371))
                        .add(new LatLng(47.729250, 7.310378))
                        .add(new LatLng(47.729270, 7.310385))
                        .add(new LatLng(47.729271, 7.310320))
                        .fillColor(Color.YELLOW)
                        .strokeColor(Color.TRANSPARENT));
        }
    }

    public void BuildTps(View view){
        mMap.clear();
        this.buildEnsisaLimit();
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729309,7.310529)).title("TP Electro 1.16"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729255,7.310689)).title("TP Physique 1.15"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729199,7.310543)).title("TP Système 1.4 U"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729157,7.310667)).title("Mbot 1.5"));
    }

    public void BuildAmphis(View view){
        mMap.clear();
        this.buildEnsisaLimit();
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729676 ,7.310641)).title("Grand Amphi"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729584,7.310716)).title("Petit Amphi"));
    }

    public void BuildLoisirs(View view){
        mMap.clear();
        this.buildEnsisaLimit();
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729495, 7.310661)).title("K'Fet"));
        mMap.addMarker(new MarkerOptions().position(new LatLng( 47.730888,7.311565)).title("Restau U"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729173,7.310142)).title("Amicale"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729226,7.310462)).title("XID"));
    }

    public void buildAccueil(View view){
        mMap.clear();
        this.buildEnsisaLimit();
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729366,7.310278)).title("Accueil"));
    }

    public void buildE2(View view) {
        mMap.clear();
        this.buildEnsisaLimit();
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729231,7.310125)).title("E20A"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729197,7.310105)).title("E20B"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729087,7.310045)).title("E21"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729038,7.309997)).title("E22"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728977,7.309966)).title("E23"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728919,7.309912)).title("E24"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728820,7.309793)).title("E25"));
    }

    public void BuildPC2(View view) {
        mMap.clear();
        this.buildEnsisaLimit();
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729180,7.310680)).title("PC1"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729125,7.310803)).title("PC2"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729202,7.310782)).title("PC3"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729301,7.310527)).title("PC4"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729202,7.310593)).title("PCMaster"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729268,7.310580)).title("TP informatique industriel"));
    }

    public void BuildScolarite(View view){
        mMap.clear();
        this.buildEnsisaLimit();
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729660,7.310713)).title("Scolarité"));
    }

    public void BuildBureauxProfs(View view) {
        mMap.clear();
        this.buildEnsisaLimit();
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728955,7.310381)).title("Bureaux Professeurs"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728758,7.310142)).title("Bureaux Professeurs"));
    }

    public void buildE3(View view) {
        mMap.clear();
        this.buildEnsisaLimit();
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729227,7.310140)).title("E30"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729195,7.310096)).title("E31"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729114,7.310029)).title("E32"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729091,7.310002)).title("E33"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729006,7.309967)).title("E34"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728943,7.309909)).title("E35"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728918,7.309873)).title("E36"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728884,7.309848)).title("E37"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728818,7.310065)).title("Salle Réseau"));
    }

    public void buildEtage1(View view){
        mMap.clear();
        findViewById(R.id.Etage1_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.Etage2_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.Etage3_layout).setVisibility(View.INVISIBLE);
        this.buildEnsisaLimit();
    }

    public void buildEtage2(View view){
        mMap.clear();
        findViewById(R.id.Etage1_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.Etage2_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.Etage3_layout).setVisibility(View.INVISIBLE);
        this.buildEnsisaLimit();
    }

    public void buildEtage3(View view){
        mMap.clear();
        findViewById(R.id.Etage1_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.Etage2_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.Etage3_layout).setVisibility(View.VISIBLE);
        this.buildEnsisaLimit();
    }

    public void start(View view){
        this.guide(etage_dest,zone_dest);
    }

    public void guide(int etage_dest, int zone){
        if(etage_dest != etage && !climbing) {
            mMap.clear();
            this.buildEnsisaLimit();
            ((TextView)findViewById(R.id.Textguide)).setText("Aller à l'étage "+etage_dest+", puis appuyer sur start");
            mMap.addMarker(new MarkerOptions().position(new LatLng(47.729260,7.310348)));
            climbing = true ;
        } else {
            climbing = false ;
            mMap.clear();
            etage = etage_dest ;
            this.zone = 2 ;
            this.buildEnsisaLimit();
            ((TextView)findViewById(R.id.Textguide)).setText("Suivez les marqueurs");
            if(etage_dest == 1) this.guideE1(zone);
            if(etage_dest == 2) this.guideE2(zone);
            if(etage_dest == 3) this.guideE3(zone);
        }
    }

    public void buildZ1() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729674,7.310705)));
        mMap.addCircle(new CircleOptions().center(new LatLng(47.729674,7.310705)).fillColor(Color.BLACK).radius(2));
    }

    public void buildZ2() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729393,7.310437)));
        mMap.addCircle(new CircleOptions().center(new LatLng(47.729393,7.310437)).fillColor(Color.BLACK).radius(2));
    }

    public void buildZ3() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729212,7.310626)));
        mMap.addCircle(new CircleOptions().center(new LatLng(47.729212,7.310626)).fillColor(Color.BLACK).radius(2));
    }

    public void buildZ4() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.729170,7.310121)));
        mMap.addCircle(new CircleOptions().center(new LatLng(47.729170,7.310121)).fillColor(Color.BLACK).radius(2));
    }

    public void buildZ5() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728961,7.310381)));
        mMap.addCircle(new CircleOptions().center(new LatLng(47.728961,7.310381)).fillColor(Color.BLACK).radius(2));
    }

    public void buildZ6() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728891,7.309916)));
        mMap.addCircle(new CircleOptions().center(new LatLng(47.728891,7.309916)).fillColor(Color.BLACK).radius(2));
    }

    public void buildZ7() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(47.728744,7.310146)));
        mMap.addCircle(new CircleOptions().center(new LatLng(47.728744,7.310146)).fillColor(Color.BLACK).radius(2));
    }

    public void guideE1(int zone){
        switch(zone) {
            case 1 :
                if(this.zone == 1) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 3) {
                    this.buildZ2();
                    this.buildZ1();
                };
                if(this.zone == 2) {
                    this.buildZ1();
                }
                if(this.zone == 4) {
                    this.buildZ2();
                    this.buildZ1();
                }
                break;

            case 3 :
                if(this.zone == 1) {
                    this.buildZ2();
                    this.buildZ3();
                }
                if(this.zone == 3) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 2) {
                    this.buildZ3();
                }
                if(this.zone == 4) {
                    this.buildZ2();
                    this.buildZ3();
                }
                break;

            case 2 :
                if(this.zone == 1) this.buildZ1();
                if(this.zone == 2) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 3) this.buildZ3();
                if(this.zone == 4) this.buildZ4();
                break;

            case 4 :
                if(this.zone == 1) {
                    this.buildZ2();
                    this.buildZ4();
                }
                if(this.zone == 4) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 3) {
                    this.buildZ2();
                    this.buildZ4();
                }
                if(this.zone == 2) {
                    this.buildZ4();
                }
                break;
        }
    }
    public void guideE2(int zone) {
        switch(zone) {
            case 1 :
                if(this.zone == 1) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 2) {
                    this.buildZ1();
                }
                if(this.zone == 5) {
                    this.buildZ4();
                    this.buildZ2();
                    this.buildZ1();
                }
                if(this.zone == 7) {
                    this.buildZ6();
                    this.buildZ4();
                    this.buildZ2();
                    this.buildZ1();
                }
                if(this.zone == 3) {
                    this.buildZ2();
                    this.buildZ1();
                }
                if(this.zone == 4) {
                    this.buildZ2();
                    this.buildZ1();
                }
                if(this.zone == 6) {
                    this.buildZ4();
                    this.buildZ2();
                    this.buildZ1();
                }
                break;

            case 2 :
                if(this.zone == 1) {
                    this.buildZ2();
                }
                if(this.zone == 2) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 5) {
                    this.buildZ4();
                    this.buildZ2();
                }
                if(this.zone == 7) {
                    this.buildZ6();
                    this.buildZ4();
                    this.buildZ2();
                }
                if(this.zone == 3) {
                    this.buildZ2();
                }
                if(this.zone == 4) {
                    this.buildZ2();
                }
                if(this.zone == 6) {
                    this.buildZ4();
                    this.buildZ2();
                }
                break;

            case 5 :
                if(this.zone == 1) {
                    this.buildZ2();
                    this.buildZ4();
                    this.buildZ5();
                }
                if(this.zone == 2) {
                    this.buildZ4();
                    this.buildZ5();
                }
                if(this.zone == 5) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 7) {
                    this.buildZ6();
                    this.buildZ4();
                    this.buildZ5();
                }
                if(this.zone == 3) {
                    this.buildZ2();
                    this.buildZ4();
                    this.buildZ5();
                }
                if(this.zone == 4) {
                    this.buildZ5();
                }
                if(this.zone == 6) {
                    this.buildZ4();
                    this.buildZ5();
                }
                break;

            case 7 :
                if(this.zone == 1) {
                    this.buildZ2();
                    this.buildZ4();
                    this.buildZ6();
                    this.buildZ7();
                }
                if(this.zone == 2) {
                    this.buildZ4();
                    this.buildZ6();
                    this.buildZ7();
                }
                if(this.zone == 5) {
                    this.buildZ4();
                    this.buildZ6();
                    this.buildZ7();
                }
                if(this.zone == 7) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 3) {
                    this.buildZ2();
                    this.buildZ4();
                    this.buildZ6();
                    this.buildZ7();
                }
                if(this.zone == 4) {
                    this.buildZ6();
                    this.buildZ7();
                }
                if(this.zone == 6) {
                    this.buildZ7();
                }
                break;

            case 3 :
                if(this.zone == 1) {
                    this.buildZ2();
                    this.buildZ3();
                }
                if(this.zone == 2) {
                    this.buildZ3();
                }
                if(this.zone == 5) {
                    this.buildZ4();
                    this.buildZ2();
                    this.buildZ3();
                }
                if(this.zone == 7) {
                    this.buildZ6();
                    this.buildZ4();
                    this.buildZ2();
                    this.buildZ3();
                }
                if(this.zone == 3) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 4) {
                    this.buildZ2();
                    this.buildZ3();
                }
                if(this.zone == 6) {
                    this.buildZ4();
                    this.buildZ2();
                    this.buildZ3();
                }
                break;

            case 4 :
                if(this.zone == 1) {
                    this.buildZ2();
                    this.buildZ4();
                }
                if(this.zone == 2) {
                    this.buildZ4();
                }
                if(this.zone == 5) {
                    this.buildZ4();
                }
                if(this.zone == 7) {
                    this.buildZ6();
                    this.buildZ4();
                }
                if(this.zone == 3) {
                    this.buildZ2();
                    this.buildZ4();
                }
                if(this.zone == 4) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 6) {
                    this.buildZ4();
                }
                break;

            case 6 :
                if(this.zone == 1) {
                    this.buildZ2();
                    this.buildZ4();
                    this.buildZ6();
                }
                if(this.zone == 2) {
                    this.buildZ4();
                    this.buildZ6();
                }
                if(this.zone == 5) {
                    this.buildZ4();
                    this.buildZ6();
                }
                if(this.zone == 7) {
                    this.buildZ6();
                }
                if(this.zone == 3) {
                    this.buildZ2();
                    this.buildZ4();
                    this.buildZ6();
                }
                if(this.zone == 4) {
                    this.buildZ6();
                }
                if(this.zone == 6) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                break;
        }
    }
    public void guideE3(int zone){
        switch(zone) {

            case 5 :
                if(this.zone == 5) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 7) {
                    this.buildZ6();
                    this.buildZ4();
                    this.buildZ5();
                }
                if(this.zone == 4) {
                    this.buildZ5();
                }
                if(this.zone == 2) {
                    this.buildZ4();
                    this.buildZ5();
                }
                if(this.zone == 6) {
                    this.buildZ4();
                    this.buildZ5();
                }
                break;

            case 7 :
                if(this.zone == 5) {
                    this.buildZ4();
                    this.buildZ6();
                    this.buildZ7();
                }
                if(this.zone == 2) {
                    this.buildZ4();
                    this.buildZ6();
                    this.buildZ7();
                }
                if(this.zone == 7) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 4) {
                    this.buildZ4();
                    this.buildZ7();
                }
                if(this.zone == 6) {
                    this.buildZ7();
                }
                break;

            case 4 :
                if(this.zone == 5) {
                    this.buildZ4();
                }
                if(this.zone == 2) {
                    this.buildZ4();
                }
                if(this.zone == 7) {
                    this.buildZ6();
                    this.buildZ4();
                }
                if(this.zone == 4) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                if(this.zone == 6) {
                    this.buildZ4();
                }
                break;

            case 6 :
                if(this.zone == 5) {
                    this.buildZ4();
                    this.buildZ6();
                }
                if(this.zone == 2) {
                    this.buildZ4();
                    this.buildZ6();
                }
                if(this.zone == 7) {
                    this.buildZ6();
                }
                if(this.zone == 4) {
                    this.buildZ6();
                }
                if(this.zone == 6) ((TextView)findViewById(R.id.Textguide)).setText("Vous êtes au bonne endroit regardez autour de vous");
                break;
        }
    }
}
