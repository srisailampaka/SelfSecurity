package com.tutorialandroid.selfsecurity.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tutorialandroid.selfsecurity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmergencyContactsActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_add_from_contacts)
    Button btnAddFromContacts;
    @BindView(R.id.btn_add_new_contacts)
    Button btnAddNewContacts;
    @BindView(R.id.btn_saved_contacts)
    Button btnSAvedCotacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.btn_add_from_contacts,R.id.btn_back,R.id.btn_add_new_contacts,R.id.btn_saved_contacts})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_add_from_contacts:
                startActivity(new Intent(EmergencyContactsActivity.this,AddFromContactsActivity.class));
                break;
            case R.id.btn_add_new_contacts:
                startActivity(new Intent(EmergencyContactsActivity.this,AddNewContactsActivity.class));
                break;
            case R.id.btn_saved_contacts:
                startActivity(new Intent(EmergencyContactsActivity.this,SavedContactListActivity.class));
                break;
            case R.id.btn_back:
               finish();
                break;
            default:
                break;
        }
    }
}
