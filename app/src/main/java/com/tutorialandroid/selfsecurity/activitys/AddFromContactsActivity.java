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
import android.provider.ContactsContract;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tutorialandroid.selfsecurity.database.ContactsProvider;
import com.tutorialandroid.selfsecurity.model.ContactDetails;
import com.tutorialandroid.selfsecurity.R;
import com.tutorialandroid.selfsecurity.database.DataBaseHandler;

import java.util.ArrayList;
import java.util.List;

import auto.*;
import auto.Person;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import library.FilteredArrayAdapter;
import library.TokenCompleteTextView;

public class AddFromContactsActivity extends AppCompatActivity implements TokenCompleteTextView.TokenListener<Person> {
    private DataBaseHandler dataBaseHandler;
    private String phoneNumber = "";
    public static final int PERMISSION_REQUEST_CONTACT = 100;
    @BindView(R.id.actv_name)
    ContactsCompletionView actvName;
    @BindView(R.id.btn_back)
    Button btnBack;
    Person[] people = new Person[0];
    ArrayAdapter<Person> adapter;
    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_add_another_contact)
    Button btnAddAnotherContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_contacts);
        ButterKnife.bind(this);


        dataBaseHandler = new DataBaseHandler(AddFromContactsActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            adapter();
        }


    }

    public void adapter() {
        people = getContactDetails();
        adapter = new FilteredArrayAdapter<Person>(this, R.layout.person_layout, people) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {

                    LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    convertView = l.inflate(R.layout.person_layout, parent, false);
                }

                Person p = getItem(position);
                ((TextView) convertView.findViewById(R.id.name)).setText(p.getName());
                ((TextView) convertView.findViewById(R.id.email)).setText(p.getEmail());

                return convertView;
            }

            @Override
            protected boolean keepObject(Person person, String mask) {

                mask = mask.toLowerCase();
                return person.getName().toLowerCase().startsWith(mask) || person.getEmail().toLowerCase().startsWith(mask);

            }
        };

        actvName.setAdapter(adapter);
        actvName.setTokenListener((TokenCompleteTextView.TokenListener<Person>) this);
        actvName.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);
    }

    @OnClick({R.id.btn_back, R.id.btn_save,R.id.btn_add_another_contact})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_save:
                saveTheDetails();
                finish();
                break;
            case R.id.btn_add_another_contact:
                saveTheDetails();
                clearTheFields();
                break;
            default:
                break;
        }
    }

    private void saveTheDetails() {
        ContactDetails details = new ContactDetails();
        if (actvName.getText().toString().isEmpty() && edtNumber.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Fields", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferences=getSharedPreferences("userinfo",0);
            ContentValues values = new ContentValues();
            values.put(DataBaseHandler.KEY_NAME, actvName.getText().toString());
            values.put(DataBaseHandler.KEY_NUMBER, edtNumber.getText().toString());
            Uri contactUri = getContentResolver().insert(ContactsProvider.CONTENT_URI, values);
            Toast.makeText(this, "Created Contact " + actvName.getText().toString(), Toast.LENGTH_LONG).show();
        }
    }
    private void clearTheFields()
    {
        actvName.setText("");
        edtNumber.setText("");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CONTACT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                adapter();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Person[] getContactDetails() {
        Person[] persons = new Person[0];

        Cursor phones = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        }

        List<Person> personList = new ArrayList<Person>();
        while (phones.moveToNext()) {

            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            personList.add(new Person(name, phoneNumber));


        }
        try {
            persons = new Person[personList.size()];
            persons = personList.toArray(persons);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return persons;
    }

    @Override
    public void onTokenAdded(Person token) {
        actvName.setText(token.getName());
        edtNumber.setText(token.getEmail());
    }

    @Override
    public void onTokenRemoved(Person token) {

    }
}