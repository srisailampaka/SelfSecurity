package com.tutorialandroid.selfsecurity.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tutorialandroid.selfsecurity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInformationActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_mobile_no)
    EditText edtmobleNo;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.edt_occupation)
    EditText edtOccupation;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.btn_save)
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        sharedPreferences=getSharedPreferences("userinfo",MODE_PRIVATE);
        edtName.setText(sharedPreferences.getString("name",""));
        edtmobleNo.setText(sharedPreferences.getString("mobileno",""));
        edtAddress.setText(sharedPreferences.getString("address",""));
        edtOccupation.setText(sharedPreferences.getString("occupation",""));
       enable(false);
    }
    @OnClick({R.id.btn_back,R.id.btn_edit,R.id.btn_save})
    public void onclick(View view){
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_edit:
              enable(true);
                break;
            case R.id.btn_save:
                sharedPreferences=getSharedPreferences("userinfo",MODE_PRIVATE);
                SharedPreferences.Editor edit=sharedPreferences.edit();
                edit.putString("name",edtName.getText().toString());
                edit.putString("mobileno",edtmobleNo.getText().toString());
                edit.putString("address",edtAddress.getText().toString());
                edit.putString("occupation", edtOccupation.getText().toString());
                edit.commit();
                Toast.makeText(getApplicationContext(),"user details are saved successfully",Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }

    private void enable(boolean enabled)
    {
        edtName.setEnabled(enabled);
        edtAddress.setEnabled(enabled);
        edtOccupation.setEnabled(enabled);
    }
}
