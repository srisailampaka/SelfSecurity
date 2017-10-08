package com.tutorialandroid.selfsecurity.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tutorialandroid.selfsecurity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_mobile_no)
    EditText edtMobileNo;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.edt_occupation)
    EditText edtOccupation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
       if (toolbar != null) {
            Log.d("Register","toolbar");
            toolbar.setTitle("Personal Information");
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
    }
    @OnClick({R.id.btn_next})
    public void Onclick(View v){
        switch (v.getId()){
            case R.id.btn_next:
                if (!edtName.getText().toString().isEmpty()){
                    if (!edtMobileNo.getText().toString().isEmpty()){
                        if (!edtAddress.getText().toString().isEmpty()){
                            if (!edtOccupation.getText().toString().isEmpty()){
                                sharedPreferences=getSharedPreferences("userinfo",MODE_PRIVATE);
                                SharedPreferences.Editor edit=sharedPreferences.edit();
                                edit.putString("name",edtName.getText().toString());
                                edit.putString("mobileno",edtMobileNo.getText().toString());
                                edit.putString("address",edtAddress.getText().toString());
                                edit.putString("occupation", edtOccupation.getText().toString());
                                edit.commit();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            }else{
                                Toast.makeText(RegisterActivity.this, "Please Enter Occuption", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(RegisterActivity.this, "Please Enter Address", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
