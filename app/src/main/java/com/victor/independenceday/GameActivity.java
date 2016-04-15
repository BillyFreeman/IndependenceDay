package com.victor.independenceday;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.victor.independenceday.view.GameView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Віктор on 30.09.2015.
 */
public class GameActivity extends Activity {

    private GameView gameView;
    private boolean gameOverFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(gameView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (gameView.getMainThread() != null && gameOverFlag) {
                    gameOverFlag = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayDialog();
                        }
                    });
                }
            }
        }, 0, 100);
    }

    private void displayDialog() {
        new AlertDialog.Builder(this)
                .setTitle("You have been destroyed")
                .setMessage("Your total score is: " + gameView.getMainThread().getTotalScore())
                .setCancelable(false)
                .setPositiveButton("RESTART", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gameView.getMainThread().restartGame();
                    }
                })
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .create()
                .show();
    }

    public void setGameOverFlag(boolean gameOverFlag) {
        this.gameOverFlag = gameOverFlag;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
