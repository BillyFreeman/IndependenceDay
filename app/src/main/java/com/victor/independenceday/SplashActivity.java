package com.victor.independenceday;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.victor.independenceday.ui.BackUIThread;
import com.victor.independenceday.ui.ExitButton;
import com.victor.independenceday.ui.PlayButton;

/**
 * Created by Віктор on 27.09.2015.
 */
public class SplashActivity extends Activity implements View.OnClickListener {

    private ViewGroup playContainer;
    private ViewGroup exitContainer;

    private PlayButton playButton;
    private ExitButton exitButton;

    private Handler handler;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity = this;

        playContainer = (ViewGroup) findViewById(R.id.play_container);
        exitContainer = (ViewGroup) findViewById(R.id.exit_container);

        playButton = new PlayButton(this, playContainer);
        exitButton = new ExitButton(this, exitContainer);

        playButton.setId(R.id.custom_play_button);
        exitButton.setId(R.id.custom_exit_button);

        playContainer.addView(playButton);
        exitContainer.addView(exitButton);

        playButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 5:
                        Intent game = new Intent(activity, GameActivity.class);
                        startActivity(game);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case 10:
                        System.exit(0);
                        break;
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (playButton != null) {
            playButton.setStep(1f);
            playButton.setIconAlpha(125);
        }
        if (exitButton!= null) {
            playButton.setStep(1f);
            playButton.setIconAlpha(125);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_play_button:
                playButton.setStep(25f);
                playButton.setIconAlpha(250);
                new Thread(new BackUIThread(handler, 1200, 5)).start();
                break;
            case R.id.custom_exit_button:
                exitButton.setStep(30f);
                exitButton.setIconAlpha(250);
                new Thread(new BackUIThread(handler, 1000, 10)).start();
                break;
        }
    }
}
