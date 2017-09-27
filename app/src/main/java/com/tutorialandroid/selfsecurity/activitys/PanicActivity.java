package com.tutorialandroid.selfsecurity.activitys;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class PanicActivity extends AppCompatActivity{
    private final String TAG = PanicActivity.class.getSimpleName();
    @BindView(R.id.btn_back)
    Button btnBAck;
    @BindView(R.id.btn_red_panic)
    Button btnRedPanic;
    @BindView(R.id.btn_panic)
    Button btnPanic;
    private MediaPlayer mediaPlayer;
    private String address;
    private SharedPreferences sharedPreferences;
    private double latitude, longitude;
    private GPSTrack track;
    private static final int REQUEST_LOCATION = 2;
    private static final int MY_PERMISSION_SMS_REQUEST_CODE = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic);
        Log.w("oncreate","onCreate");
        ButterKnife.bind(this);

            if (((SecurityApplication) getApplication()).getTimerStatus()) {
                btnRedPanic.setText(getString(R.string.stop));
            } else {
                btnRedPanic.setText(getString(R.string.start));
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
                track=new GPSTrack(PanicActivity.this);
                btnRedPanic.setText(getString(R.string.stop));
                ((SecurityApplication) getApplication()).setAddress(address);
                ((SecurityApplication) getApplication()).startTimer();
            } else {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                track.stopUsingGPS();
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
        if (!Connectivity.isNetworkAvailable(getApplicationContext())){
            return latitude+","+longitude;
        }else{
            return address ;}
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("onresume","onResume");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            track = new GPSTrack(PanicActivity.this);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                if (track.canGetLocation()) {
                    latitude = track.getLatitude();
                    longitude = track.getLongitude();
                    address=getAddress(latitude,longitude);
                    Log.w("onresume",latitude+"llll"+longitude+"");
                }
            } else {
                track.showSettingsAlert();
            }
        }
    }
}
