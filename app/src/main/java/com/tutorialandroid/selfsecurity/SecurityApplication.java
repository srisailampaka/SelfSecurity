package com.tutorialandroid.selfsecurity;

import android.*;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.UserDictionary;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.tutorialandroid.selfsecurity.activitys.GPSTrack;
import com.tutorialandroid.selfsecurity.database.ContactsProvider;
import com.tutorialandroid.selfsecurity.database.DataBaseHandler;
import com.tutorialandroid.selfsecurity.model.ContactDetails;
import com.tutorialandroid.selfsecurity.model.Message;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by srisailampaka on 24/08/17.
 */

public class SecurityApplication extends Application {
    public static SecurityApplication appInstance;
    private static final String AUTHORITY = "com.tutorialandroid.selfsecurity";
    private static final String BASE_PATH = "contacts";
    private static final String MESSAGE_PATH = "message";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final Uri MESSAGE__URI = Uri.parse("content://" + AUTHORITY + "/" + MESSAGE_PATH);

    private int time;
    private String message;
    private Handler handler;
    private Runnable runnable;
    private String address;
    MyCountDownTimer myCountDownTimer;
    boolean starttimer=false;
    private double latitude, longitude;
    private GPSTrack track;
    private static final int REQUEST_LOCATION = 2;

    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;
//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                int lastItem = 0;
//                ArrayList<Message> list = getMessageDetail(getApplicationContext());
//                if (list != null&&list.size()>0) {
//                    lastItem = getMessageDetail(getApplicationContext()).size() - 1;
//
//                    sendToAllContacts(getContacts(getApplicationContext()), getMessageDetail(getApplicationContext()).get(lastItem).getMessage());
//                   // Toast.makeText(getApplicationContext(),getMessageDetail(getApplicationContext()).get(lastItem).getMessage(),Toast.LENGTH_SHORT).show();
//
//                    handler.postDelayed(runnable, 60000 * Integer.parseInt(getMessageDetail(getApplicationContext()).get(lastItem).getTime()));
//                }
//                else
//                {
//                  Toast.makeText(getApplicationContext(),"Please set the Security message and time",Toast.LENGTH_SHORT).show();
//                }
//            }
//        };


    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private void sendToAllContacts(ArrayList<ContactDetails> contactList, String message) {
        for (int i = 0; i < contactList.size(); i++) {
            sendSMS(getApplicationContext(), contactList.get(i).getNumber(), message);
            Log.w(message,contactList.get(i).getNumber());
        }
    }

    public void startTimer() {
        int lastItem = 0;
        ArrayList<Message> list = getMessageDetail(getApplicationContext());
        if (list != null&&list.size()>0) {
            lastItem = getMessageDetail(getApplicationContext()).size() - 1;
            if (!starttimer) {
                starttimer=true;
                sendToAllContacts(getContacts(getApplicationContext()), getMessageDetail(getApplicationContext()).get(lastItem).getMessage());
                myCountDownTimer = new MyCountDownTimer(Integer.parseInt(getMessageDetail(getApplicationContext()).get(lastItem).getTime()) * 60000, 1000);
                myCountDownTimer.start();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please set the alert Message and time", Toast.LENGTH_LONG).show();
        }

    }

    public void stopTimer() {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
            starttimer=false;
            //myCountDownTimer=null;
        }
    }


    public void saveIntialTimerMessageDetails() {

        ContentValues values = new ContentValues();
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", 0);
        values.put(DataBaseHandler.KEY_TIME, sharedPreferences.getString("alerttime", ".."));
        values.put(DataBaseHandler.KEY_MESSAGE, sharedPreferences.getString("alertmessage", ".."));
        Uri uri = getContentResolver().insert(ContactsProvider.MESSAGE_URI, values);

    }

    public ArrayList<ContactDetails> getContacts(Context context) {
        ArrayList<ContactDetails> detailses = new ArrayList<>();
        // String URL = CONTENT_URI;
        //Uri students = Uri.parse(CONTENT_URI);
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, "name");
        if (cursor.moveToFirst()) {
            do {
                ContactDetails contactDetails = new ContactDetails();
                contactDetails.setId(Integer.parseInt(cursor.getString(0)));
                contactDetails.setName(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_NAME)));
                contactDetails.setNumber(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_NUMBER)));
                detailses.add(contactDetails);
                Log.w("contact",cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_NUMBER)));
                //sendSMS(context, cursor.getString(2), message);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return detailses;


    }

    public ArrayList<Message> getMessageDetail(Context context) {
        ArrayList<Message> detailses = new ArrayList<>();
        // String URL = CONTENT_URI;
        //Uri students = Uri.parse(CONTENT_URI);
        Cursor cursor = context.getContentResolver().query(MESSAGE__URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message();
                message.setTime((cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_TIME))));
                message.setMessage(cursor.getString(cursor.getColumnIndex(DataBaseHandler.KEY_MESSAGE)));
                detailses.add(message);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return detailses;


    }

    private void sendSMS(final Context context, String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);
        //---when the SMS has been sent---
        context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
        //---when the SMS has been delivered---
        context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message + "\n Adress." + getAddress(), sentPI, deliveredPI);
    }


    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (millisUntilFinished / 1000);
            Log.d("timer......."+millisUntilFinished," ....time"+ String.valueOf(progress));

        }

        @Override
        public void onFinish() {

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            } else {
                track = new GPSTrack(getApplicationContext());
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    if (track.canGetLocation()) {
                        latitude = track.getLatitude();
                        longitude = track.getLongitude();
                        address=getAddress(latitude,longitude);
                        Log.w("onresumeapp",latitude+"llll"+longitude+"");
                    }
                } else {
                  //  track.showSettingsAlert();
                }
            }

            int lastItem = 0;
            ArrayList<Message> list = getMessageDetail(getApplicationContext());
            if (list != null) {
                lastItem = getMessageDetail(getApplicationContext()).size() - 1;
                sendToAllContacts(getContacts(getApplicationContext()), getMessageDetail(getApplicationContext()).get(lastItem).getMessage());
            }
            myCountDownTimer.start();
        }

    }
    public boolean getTimerStatus()
    {
        return starttimer;
    }
    private String getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        String address = "";
        String city = "";
        String state = "";
        String country = "";
        try {
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
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
        return address + "," + city + "," + state + "," + country;}
    }
}
