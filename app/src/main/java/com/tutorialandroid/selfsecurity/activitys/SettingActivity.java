package com.tutorialandroid.selfsecurity.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.tutorialandroid.selfsecurity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {
    private final String TAG=SettingActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_alert_msg_setting)
    Button btnAlertmsgSetting;
    @BindView(R.id.btn_alert_timer_setting)
    Button btnTimerSetting;
    @BindView(R.id.swichbutton)
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        if (toolbar != null){
            toolbar.setTitle(R.string.settings);
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }
    }
    @OnClick({R.id.btn_back,R.id.btn_alert_msg_setting,R.id.btn_alert_timer_setting,R.id.swichbutton})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back:
               finish();
                break;
            case R.id.btn_alert_msg_setting:
                startActivity(new Intent(SettingActivity.this,MessageSettingActivity.class));
                break;
            case R.id.btn_alert_timer_setting:
                startActivity(new Intent(SettingActivity.this,TimerSettingActivity.class));
                break;
            case R.id.swichbutton:
                break;
            default:
                break;
        }
    }
}
