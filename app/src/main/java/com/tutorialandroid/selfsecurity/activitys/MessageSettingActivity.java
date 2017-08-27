package com.tutorialandroid.selfsecurity.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tutorialandroid.selfsecurity.R;
import com.tutorialandroid.selfsecurity.SecurityApplication;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageSettingActivity extends AppCompatActivity {
    private final String TAG = MessageSettingActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.edit_message)
    EditText edtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        edtMessage.setText(sharedPreferences.getString("alertmessage", ""));
        edtMessage.setEnabled(false);
    }

    @OnClick({R.id.btn_back, R.id.btn_save, R.id.btn_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_edit:
                edtMessage.setEnabled(true);
                break;
            case R.id.btn_save:
                if (!TextUtils.isEmpty(edtMessage.getText().toString().trim())) {
                    sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("alertmessage", edtMessage.getText().toString());
                    edit.commit();
                    ((SecurityApplication) getApplication()).saveIntialTimerMessageDetails();
                    Toast.makeText(getApplicationContext(), "alert message are saved successfully", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
