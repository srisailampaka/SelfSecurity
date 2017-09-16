package com.tutorialandroid.selfsecurity.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tutorialandroid.selfsecurity.R;
import com.tutorialandroid.selfsecurity.adapter.AddFromContactAdapter;
import com.tutorialandroid.selfsecurity.database.ContactsProvider;
import com.tutorialandroid.selfsecurity.database.DataBaseHandler;
import com.tutorialandroid.selfsecurity.model.ContactDetails;

import java.util.ArrayList;
import java.util.List;

import auto.ContactsCompletionView;
import auto.Person;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import library.FilteredArrayAdapter;
import library.TokenCompleteTextView;

public class ContactsActivity extends AppCompatActivity {
    private DataBaseHandler dataBaseHandler;
    private String phoneNumber = "";
    public static final int PERMISSION_REQUEST_CONTACT = 100;
    @BindView(R.id.contacts_recyclerview)
    RecyclerView mRecyclerView;
    AddFromContactAdapter adapter;
    ArrayList<ContactDetails> arrayList;
    private LinearLayoutManager mLayoutManager;
    private GestureDetector mGestureDetector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        mGestureDetector = new GestureDetector(ContactsActivity.this, simpleOnGestureListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
           // adapter();
            arrayList=getContactDetails();
        }
        mLayoutManager = new LinearLayoutManager(ContactsActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new AddFromContactAdapter(ContactsActivity.this,arrayList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(onItemTouchListener);
    }
    private RecyclerView.OnItemTouchListener onItemTouchListener=new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && mGestureDetector.onTouchEvent(e)) {
                Intent contact=new Intent(ContactsActivity.this,AddFromContactsActivity.class);
                contact.putExtra("contacts",arrayList.get(rv.getChildAdapterPosition(child)));
                startActivity(contact);
                finish();
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CONTACT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
               // adapter();
                arrayList=getContactDetails();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<ContactDetails> getContactDetails() {
        Cursor phones = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        }

        ArrayList<ContactDetails> personList = new ArrayList<ContactDetails>();
        while (phones.moveToNext()) {
                ContactDetails details=new ContactDetails();
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            details.setName(name);
            details.setNumber(phoneNumber);
            personList.add(details);
        }

        return personList;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("onstop", "Onstop");
        finish();
    }
}