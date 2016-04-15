package com.victor.independenceday.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.victor.independenceday.GameActivity;
import com.victor.independenceday.bacground.Background;
import com.victor.independenceday.controller.ViewStateController;
import com.victor.independenceday.objects.AbstractObject;
import com.victor.independenceday.objects.ammo.Ammo;
import com.victor.independenceday.userinfo.ProgressContainer;
import com.victor.independenceday.userinfo.RaptorHealth;
import com.victor.independenceday.userinfo.ScoreBox;

import java.util.List;

/**
 * Created by Віктор on 27.09.2015.
 */
public class GameThread extends Thread {

    private boolean running;

    private SurfaceHolder holder;
    private ViewStateController controller;
    private Paint paint;

    private List<AbstractObject> list;
    private List<Background> backgrounds;
    private List<Ammo> rocketList;
    private AbstractObject raptor;
    private RaptorHealth healthLine;
    private ScoreBox scoreBox;
    private ProgressContainer progress;

    private boolean timeToStop;
    private GameActivity context;


    public GameThread(Context context, SurfaceHolder holder, ViewStateController controller) {
        this.holder = holder;
        this.controller = controller;
        this.paint = new Paint();
        timeToStop = false;

        this.context = (GameActivity) context;
    }

    @Override
    public void run() {
        running = true;
        while (running && controller != null) {
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                synchronized (holder) {
                    controller.refreshScene();
                    backgrounds = controller.getBackgroundList();
                    if (!backgrounds.isEmpty()) {
                        for (Background object : backgrounds) {
                            canvas.drawBitmap(object.getBitmap(), object.getSrc(), object.getDst(), paint);
                        }
                    }
                    list = controller.getEnemiesList();
                    if (!list.isEmpty()) {
                        for (AbstractObject object : list) {
                            object.doNextMove();
                            canvas.drawBitmap(object.getBitmap(), object.getSrc(), object.getDst(), paint);
                        }
                    }
                    rocketList = controller.getAmmoList();
                    if (!list.isEmpty()) {
                        for (AbstractObject object : rocketList) {
                            object.doNextMove();
                            canvas.drawBitmap(object.getBitmap(), object.getSrc(), object.getDst(), paint);
                        }
                    }
                    list = controller.getExplosionList();
                    if (!list.isEmpty()) {
                        for (AbstractObject object : list) {
                            object.doNextMove();
                            canvas.drawBitmap(object.getBitmap(), object.getSrc(), object.getDst(), paint);
                        }
                    }
                    raptor = controller.getRaptor();
                    if (raptor != null) {
                        canvas.drawBitmap(raptor.getBitmap(), raptor.getSrc(), raptor.getDst(), paint);
                    } else {
                        isGameOver();
                    }
                    healthLine = controller.getHeathLine();
                    if (healthLine != null) {
                        canvas.drawRect(healthLine.getEmptyBody(), healthLine.getEmptySpace());
                        canvas.drawRect(healthLine.getBody(), healthLine.getPaint());
                    }
                    progress = controller.getProgress();
                    if (progress != null) {
                        canvas.drawBitmap(progress.getBitmap(), progress.getSrc(), progress.getDst(), progress.getPaint());
                    }
                    scoreBox = controller.getScoreBox();
                    if (scoreBox != null) {
                        canvas.drawText(scoreBox.getResult(), scoreBox.getStart(), scoreBox.getEnd(), scoreBox.getX(), scoreBox.getY(), scoreBox.getPaint());
                    }
                    if (controller.isUIAllowed()) {
                        context.setGameOverFlag(true);
                        controller.setUiLock(false);
                        controller.setUiLockD(true);
                    }

                    holder.unlockCanvasAndPost(canvas);
                }
            } else {
                break;
            }
            try {
                sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isRunning() {
        return running;
    }

    public void setController(ViewStateController controller) {
        this.controller = controller;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public ViewStateController getController() {
        return controller;
    }

    public boolean isGameOver() {
        if (controller != null) {
            timeToStop = controller.isGameOver();
            return timeToStop;
        }
        return true;
    }

    public void restartGame() {
        if (controller != null) {
            controller.replay();
        }
    }

    public long getTotalScore() {
        if (controller != null) {
            return controller.getScoreBox().getString();
        }
        return 0;
    }
}
