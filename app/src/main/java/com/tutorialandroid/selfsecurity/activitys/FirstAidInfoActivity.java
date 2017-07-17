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

public class FirstAidInfoActivity extends AppCompatActivity {
    private final String TAG=FirstAidInfoActivity.class.getSimpleName();
    @BindView(R.id.btn_back)
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_info);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.btn_back)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_back:
               finish();
                break;
            default:
                break;
        }
    }
}
