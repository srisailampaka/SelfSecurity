package com.tutorialandroid.selfsecurity.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.tutorialandroid.selfsecurity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final String TAG=MainActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_personal_info)
    Button btnPersonalInfo;
    @BindView(R.id.btn_emergency_contacts)
    Button btnEmgCOntacts;
    @BindView(R.id.btn_first_aid_instructions)
    Button btnFirstAidInstruction;
    @BindView(R.id.btn_panic)
    Button btnPanic;
    @BindView(R.id.btn_settings)
    Button btnSettings;
    @BindView(R.id.btn_exit)
    Button btnExit;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (toolbar != null){
            toolbar.setTitle(R.string.main_title);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
        sharedPreferences=getSharedPreferences("userinfo",MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putString("alerttime", "0");
        edit.putString("alertmessage", "");
        edit.commit();
    }
    @OnClick({R.id.btn_personal_info,R.id.btn_emergency_contacts,R.id.btn_panic,R.id.btn_first_aid_instructions,
            R.id.btn_settings,R.id.btn_exit})
    public void onClicl(View v){
        switch (v.getId()){
            case R.id.btn_personal_info:
                startActivity(new Intent(MainActivity.this,PersonalInformationActivity.class));
                break;
            case R.id.btn_emergency_contacts:
                startActivity(new Intent(MainActivity.this,EmergencyContactsActivity.class));
                break;
            case R.id.btn_panic:
                startActivity(new Intent(MainActivity.this,PanicActivity.class));
                break;
            case R.id.btn_first_aid_instructions:
                startActivity(new Intent(MainActivity.this,FirstAidInfoActivity.class));
                break;
            case R.id.btn_settings:
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                break;
            case R.id.btn_exit:
                finish();
                break;

            default:
                break;
        }
    }
}
