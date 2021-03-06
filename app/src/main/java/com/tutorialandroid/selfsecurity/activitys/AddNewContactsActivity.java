package com.tutorialandroid.selfsecurity.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tutorialandroid.selfsecurity.database.ContactsProvider;
import com.tutorialandroid.selfsecurity.model.ContactDetails;
import com.tutorialandroid.selfsecurity.R;
import com.tutorialandroid.selfsecurity.database.DataBaseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewContactsActivity extends AppCompatActivity {
    private DataBaseHandler dataBaseHandler;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_add_another_contact)
    Button btnAddAnotherContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contacts);
        ButterKnife.bind(this);
        dataBaseHandler=new DataBaseHandler(AddNewContactsActivity.this);

    }
    @OnClick({R.id.btn_save,R.id.btn_back,R.id.btn_add_another_contact})
    public  void onClick(View v){
        switch (v.getId()){
            case R.id.btn_save:
                saveTheDetails();
                finish();
                break;
            case R.id.btn_back:
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
        ContactDetails details=new ContactDetails();
        if (edtName.getText().toString().isEmpty() && edtNumber.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Enter Fields", Toast.LENGTH_SHORT).show();
        }else{
            ContentValues values = new ContentValues();
            SharedPreferences sharedPreferences=getSharedPreferences("userinfo",0);
            values.put(DataBaseHandler.KEY_NAME, edtName.getText().toString());
            values.put(DataBaseHandler.KEY_NUMBER, edtNumber.getText().toString());
            Uri contactUri  = getContentResolver().insert(ContactsProvider.CONTENT_URI,values);
            Toast.makeText(this,"Created Contact " + edtName.getText().toString(),Toast.LENGTH_LONG).show();
        }
    }
    private void clearTheFields()
    {
        edtName.setText("");
        edtNumber.setText("");
    }
}
