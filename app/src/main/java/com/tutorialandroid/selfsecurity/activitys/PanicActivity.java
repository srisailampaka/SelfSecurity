package com.tutorialandroid.selfsecurity.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tutorialandroid.selfsecurity.Connectivity;
import com.tutorialandroid.selfsecurity.R;
import com.tutorialandroid.selfsecurity.SecurityApplication;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PanicActivity extends AppCompatActivity implements LocationListener {
    private final String TAG = PanicActivity.class.getSimpleName();
    @BindView(R.id.btn_back)
    Button btnBAck;
    @BindView(R.id.btn_red_panic)
    Button btnRedPanic;
    @BindView(R.id.btn_panic)
    Button btnPanic;
    private MediaPlayer mediaPlayer;
    private LocationManager locationManager;
    private String locationProvider;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic);
        ButterKnife.bind(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.demonstrative);
        //get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //define the location manager criteria
        Criteria criteria = new Criteria();

        locationProvider = locationManager.getBestProvider(criteria, false);
        locationManager.requestLocationUpdates(locationProvider, 400, 1, this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);


        //initialize the location
        if (location != null) {

            onLocationChanged(location);
        }

    }

    @OnClick({R.id.btn_back, R.id.btn_red_panic, R.id.btn_panic})
    public void onCLick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_red_panic:
                sendSms();
                break;
            case R.id.btn_panic:
                sendSms();
                break;
            default:
                break;
        }
    }

    private void sendSms() {
        if (btnRedPanic.getText().toString().equalsIgnoreCase(getString(R.string.start))) {
            mediaPlayer.start();
            btnRedPanic.setText(getString(R.string.stop));
            ((SecurityApplication) getApplication()).startTimer();
        } else {
            mediaPlayer.stop();
            btnRedPanic.setText(getString(R.string.start));
            ((SecurityApplication) getApplication()).stopTimer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
        if (Connectivity.isNetworkAvailable(getApplicationContext())) {
            address = getAddress(latitude, longitude);
        }
        else
        {
            address=latitude+","+longitude;
        }
        Toast.makeText(getBaseContext(), address, Toast.LENGTH_LONG).show();

    }

    private String getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        String address = "";
        String city = "";
        String state = "";
        String country = "";
        try {
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        } catch (Exception e) {

        }
        return address + "," + city + "," + state + "," + country;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
