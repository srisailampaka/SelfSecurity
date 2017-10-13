package com.tutorialandroid.selfsecurity.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tutorialandroid.selfsecurity.R;
import com.tutorialandroid.selfsecurity.SecurityApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmerencyCallActivity extends AppCompatActivity {
    private final String TAG = EmerencyCallActivity.class.getSimpleName();

    @BindView(R.id.btn_emergency_call)
    Button btnEmgCall;
    @BindView(R.id.btn_set_emg_number)
    Button btnSetEmgNumber;
    @BindView(R.id.btn_save_emg_number)
    Button btnSave;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.lnlr_save)
    LinearLayout lnlSave;
    @BindView(R.id.edt_emg_number)
    EditText edtEmgNumber;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    private final static int PERMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emerency_call);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        edit = sharedPreferences.edit();

    }

    @OnClick({R.id.btn_set_emg_number, R.id.btn_emergency_call, R.id.btn_save_emg_number, R.id.btn_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_emg_number:
                lnlSave.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_emergency_call:
                String emgNumber = sharedPreferences.getString("emgNumber", "");
                if (!emgNumber.isEmpty()){
                    makeAcall(emgNumber);
                }else {
                    Toast.makeText(this, "Please Set Emergency Number", Toast.LENGTH_SHORT).show();
                }
                    break;
            case R.id.btn_save_emg_number:
                edit.putString("emgNumber", edtEmgNumber.getText().toString());
                edit.commit();
                ((SecurityApplication) getApplication()).saveIntialTimerMessageDetails(getApplicationContext());
                lnlSave.setVisibility(View.GONE);
                break;

            case R.id.btn_back:
                finish();
                break;

            default:
                break;
        }
    }

    public void makeAcall(String s) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + s));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            requestForCallPermission();

        } else {
            startActivity(intent);

        }
    }

    public void requestForCallPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
