package com.example.saksham_.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String address;
    TextView addTextView;
    TextView latTextView,lonTextView,altTextView,accTextView;
    Geocoder geocoder;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            startListen();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);

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
        };

        if(Build.VERSION.SDK_INT<23)
        {
            startListen();
        }
        else
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location!=null)
                {
                    updateLocationInfo(location);
                }
            }
        }
    }

    public void updateLocationInfo(Location location)
    {
        latTextView=(TextView)findViewById(R.id.latitude);
        lonTextView=(TextView)findViewById(R.id.longitude);
        altTextView=(TextView)findViewById(R.id.Attitude);
        accTextView=(TextView)findViewById(R.id.Accuracy);
        latTextView.setText("Latitude: "+location.getLatitude());
        lonTextView.setText("Longitude: "+location.getLongitude());
        altTextView.setText("Altitude: "+location.getAltitude());
        accTextView.setText("Accuracy: "+location.getAccuracy());

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            address="Could not load Address";

            List<Address> listAddress=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            Log.i("Info",listAddress.get(0).toString());
            if(listAddress!=null && listAddress.size()>0)
            {
                Log.i("LocationInfo","hello");
                address="";
                if(listAddress.get(0).getSubThoroughfare()!=null)
                    address+=listAddress.get(0).getSubThoroughfare()+" ";
                if(listAddress.get(0).getThoroughfare()!=null)
                    address+=listAddress.get(0).getThoroughfare()+"\n";
                if(listAddress.get(0).getLocality()!=null)
                    address+=listAddress.get(0).getLocality()+"\n";
                if(listAddress.get(0).getPostalCode()!=null)
                    address+=listAddress.get(0).getPostalCode()+"\n";
                if(listAddress.get(0).getCountryName()!=null)
                    address+=listAddress.get(0).getCountryName() +"\n";
            }
            addTextView=(TextView)findViewById(R.id.address);

            addTextView.setText("Address: \n"+address);

        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.i("LocationInfo",location.toString());
    }

    public void startListen()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }
    }
}