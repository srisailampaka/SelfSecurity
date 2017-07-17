package com.tutorialandroid.selfsecurity.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.tutorialandroid.selfsecurity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimerSettingActivity extends AppCompatActivity {
    private final String TAG = TimerSettingActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.number_picker)
    NumberPicker numberPicker;
    private String npValues="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_setting);
        ButterKnife.bind(this);
        sharedPreferences=getSharedPreferences("userinfo",MODE_PRIVATE);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(sharedPreferences.getInt("alerttime",1));
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                npValues= String.valueOf(newVal);
            }
        });
    }
    @OnClick({R.id.btn_back,R.id.btn_save})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back:
               finish();
                break;
            case R.id.btn_save:
                sharedPreferences=getSharedPreferences("userinfo",MODE_PRIVATE);
                SharedPreferences.Editor edit=sharedPreferences.edit();
                edit.putInt("alerttime",numberPicker.getValue());
                edit.commit();
                Toast.makeText(this, "Setting time "+npValues+"M", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
    }
}
