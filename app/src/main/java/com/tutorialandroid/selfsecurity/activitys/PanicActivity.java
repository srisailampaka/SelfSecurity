package com.tutorialandroid.selfsecurity.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tutorialandroid.selfsecurity.Connectivity;
import com.tutorialandroid.selfsecurity.R;
import com.tutorialandroid.selfsecurity.SecurityApplication;

import java.sql.DriverManager;
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
    private SharedPreferences sharedPreferences;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int MY_PERMISSION_SMS_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic);
        ButterKnife.bind(this);

        //get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //define the location manager criteria
        Criteria criteria = new Criteria();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            locationProvider = locationManager.getBestProvider(criteria, false);


//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
            Location location = locationManager.getLastKnownLocation(locationProvider);


            //initialize the location
            if (location != null) {

                onLocationChanged(location);
            }

            if (((SecurityApplication) getApplication()).getTimerStatus()) {
                btnRedPanic.setText(getString(R.string.stop));
            } else {
                btnRedPanic.setText(getString(R.string.start));
            }
        }
    }

    @OnClick({R.id.btn_back, R.id.btn_red_panic, R.id.btn_panic})
    public void onCLick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_red_panic:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {

                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_SMS_REQUEST_CODE);
                    }
                } else {
                    sendSms();
                }


                break;
            case R.id.btn_panic:
                sendSms();
                break;
            default:
                break;
        }
    }

    private void sendSms() {
        sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("alertswitch", false)) {
            if (btnRedPanic.getText().toString().equalsIgnoreCase(getString(R.string.start))) {
                mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
                mediaPlayer.start();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                locationManager.requestLocationUpdates(locationProvider, 400, 1, this);
                btnRedPanic.setText(getString(R.string.stop));
                ((SecurityApplication) getApplication()).setAddress(address);
                ((SecurityApplication) getApplication()).startTimer();
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                locationManager.removeUpdates(this);
                btnRedPanic.setText(getString(R.string.start));
                ((SecurityApplication) getApplication()).stopTimer();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please On the Security Setting Switch", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
        if (Connectivity.isNetworkAvailable(getApplicationContext())) {
            address = getAddress(latitude, longitude);
        } else {
            address = latitude + "," + longitude;
        }

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
