package com.victor.independenceday.controller;

import android.animation.ObjectAnimator;

import com.victor.independenceday.objects.AbstractObject;
import com.victor.independenceday.objects.ObjectConstants;

/**
 * Created by Віктор on 05.11.2015.
 */
public class LevelThread implements Runnable {

    private AbstractObject raptor;
    private boolean stopped;

    private int reaperSpeed;
    private int arrowSpeed;
    private int scoutSpeed;

    private int currentLevel;
    private float startIndex;
    private double index;

    public LevelThread(AbstractObject raptor) {
        this.raptor = raptor;
        currentLevel = 1;
        startIndex = 1.05f;
        index = 1.05f;

        reaperSpeed = ObjectConstants.REAPER_SPEED;
        arrowSpeed = ObjectConstants.ARROW_SPEED;
        scoutSpeed = ObjectConstants.SCOUT_SPEED;
    }

    @Override
    public void run() {
        while (!stopped) {
            long check = raptor.getScore() / currentLevel;
            if (check >= 1000) {
                currentLevel++;
                index = Math.pow(startIndex, currentLevel);
                raptor.setDemage((int) (index * (float) ObjectConstants.RAPTOR_DAMAGE));
                System.out.println("Score: " + check + " => " + " Arrow: " + " " + getArrowSpeed() + " Scout: " + " " + getScoutSpeed() + " Reaper: " + " " + getReaperSpeed() + "         " + raptor.getDemage());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        stopped = true;
    }

    public int getReaperSpeed() {
        double speed = ObjectConstants.REAPER_SPEED;
        double result = speed * index;
        return (int) result;
    }

    public int getArrowSpeed() {
        double speed = ObjectConstants.ARROW_SPEED;
        double result = speed * index;
        return (int) result;
    }

    public int getScoutSpeed() {
        double speed = ObjectConstants.SCOUT_SPEED;
        double result = speed * index;
        return (int) result;
    }
}
