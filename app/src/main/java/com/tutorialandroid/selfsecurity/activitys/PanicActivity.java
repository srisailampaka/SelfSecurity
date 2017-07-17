package com.tutorialandroid.selfsecurity.activitys;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tutorialandroid.selfsecurity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PanicActivity extends AppCompatActivity {
    private final String TAG= PanicActivity.class.getSimpleName();
    @BindView(R.id.btn_back)
    Button btnBAck;
    @BindView(R.id.btn_red_panic)
    Button btnRedPanic;
    @BindView(R.id.btn_panic)
    Button btnPanic;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic);
        ButterKnife.bind(this);
        mediaPlayer= MediaPlayer.create(this,R.raw.demonstrative);

    }
    @OnClick({R.id.btn_back,R.id.btn_red_panic,R.id.btn_panic})
    public void onCLick(View view){
        switch (view.getId()){
            case R.id.btn_back:
              finish();
                break;
            case R.id.btn_red_panic:
                mediaPlayer.start();
                break;
            case R.id.btn_panic:
                mediaPlayer.start();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
