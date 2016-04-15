package com.victor.independenceday.controller;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import com.victor.independenceday.R;
import com.victor.independenceday.bacground.Background;
import com.victor.independenceday.objects.AbstractObject;
import com.victor.independenceday.objects.ObjectConstants;
import com.victor.independenceday.objects.ammo.Ammo;
import com.victor.independenceday.objects.ammo.Rocket;
import com.victor.independenceday.objects.enemies.Arrow;
import com.victor.independenceday.objects.enemies.Reaper;
import com.victor.independenceday.objects.enemies.Scout;
import com.victor.independenceday.objects.explosion.EnemyExplosion;
import com.victor.independenceday.objects.explosion.RaptorExplosion;
import com.victor.independenceday.objects.raptor.Raptor;
import com.victor.independenceday.physics.BackLinearVertical;
import com.victor.independenceday.physics.Trajectory;
import com.victor.independenceday.userinfo.ProgressContainer;
import com.victor.independenceday.userinfo.RaptorHealth;
import com.victor.independenceday.userinfo.ScoreBox;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Віктор on 27.09.2015.
 */
public class ViewStateController {

    private static final float TAP_AREA = .15f;
    private static final int ENEMIES_QUEUE = 5;

    private static ViewStateController controller;

    private AbstractObject raptor;
    private List<AbstractObject> enemiesList;
    private List<Ammo> ammoList;
    private List<Background> backgroundList;
    private List<AbstractObject> explosionList;

    private RaptorHealth heathLine;
    private ScoreBox scoreBox;
    private ProgressContainer progress;

    private PointF touchCenter;

    private float raptorPrevX;
    private float raptorX;

    private float enemyX;

    private Deque<Background> backgrounds;
    private Deque<AbstractObject> queue;

    private int displayRight;
    private int displayBottom;

    private boolean touched;

    private Activity context;
    private Resources res;

    private List<String> nameList;

    private Map<String, Bitmap> bitMap;

    private Trajectory rocketTrajectory;
    private boolean alive;

    private long finalScore;
    private boolean uiLock;
    private boolean uiLockD;

    private LevelThread level;

    private ViewStateController(Activity context, int displayRight, int displayBottom) {
        this.uiLock = false;
        this.uiLockD = false;

        this.context = context;
        this.displayRight = displayRight;
        this.displayBottom = displayBottom;

        res = this.context.getResources();

        enemiesList = new ArrayList<>();
        ammoList = new ArrayList<>();
        backgroundList = new ArrayList<>();
        explosionList = new ArrayList<>();

        queue = new ArrayDeque<>();
        backgrounds = new ArrayDeque<>();
        bitMap = new HashMap<>();

        nameList = new ArrayList<>();
        rocketTrajectory = new BackLinearVertical(displayRight, displayBottom);

        loadBitmap();
        loadClass();
        objectsInit();
    }

    private void loadBitmap() {
        bitMap.put("background", BitmapFactory.decodeResource(res, R.drawable.earth_surface_3));
        bitMap.put("raptor", BitmapFactory.decodeResource(res, R.drawable.f22_raptor_273x200));
        bitMap.put("scout", BitmapFactory.decodeResource(res, R.drawable.enemy_1));
        bitMap.put("reaper", BitmapFactory.decodeResource(res, R.drawable.enemy_2));
        bitMap.put("arrow", BitmapFactory.decodeResource(res, R.drawable.enemy_3));
        bitMap.put("enemy_explosion", BitmapFactory.decodeResource(res, R.drawable.explosion_enemy));
        bitMap.put("raptor_explosion", BitmapFactory.decodeResource(res, R.drawable.explosion_raptor));
        bitMap.put("rocket", BitmapFactory.decodeResource(res, R.drawable.rocket));
        bitMap.put("progress_container", BitmapFactory.decodeResource(res, R.drawable.gi_progress_element));
    }

    private void loadClass() {
        nameList.add("scout");
        nameList.add("reaper");
        nameList.add("arrow");
    }

    private void objectsInit() {
        backgroundList.add(new Background(bitMap.get("background"), displayRight, displayBottom, 0));
        backgrounds.add(backgroundList.get(0));
        raptor = Raptor.getRaptor(null, bitMap.get("raptor"), new PointF(displayRight / 2, displayBottom * .85f), displayBottom * Raptor.RAPTOR_SIZE);
        alive = true;

        progress = new ProgressContainer(bitMap.get("progress_container"), displayRight, displayBottom);
        heathLine = new RaptorHealth(raptor.getHealthPoints(), progress.getDst());
        scoreBox = new ScoreBox(progress.getDst());

        level = new LevelThread(raptor);
        new Thread(level).start();
    }

    public static ViewStateController getController(Activity context, int width, int height) {
        if (controller == null) {
            controller = new ViewStateController(context, width, height);
        }
        return controller;
    }

    public void onFingerMove(PointF point) {
        if (touched && alive) {
            raptorMove(point);
        }
    }

    public void raptorMove(PointF point) {
        point.y = point.y - displayBottom * (Raptor.RAPTOR_SIZE);
        raptor.doNextMove(point);
    }

    public void refreshScene() {
        if (raptor != null && touched && alive) {
            raptorX = raptor.getBasePoint().x;
            int n = raptor.getSpriteNumber();
            if (raptorX - raptorPrevX > 5) {
                if (n < 7) {
                    raptor.changeSprite(n + 1);
                } else {
                    raptor.changeSprite(7);
                }
            } else if (raptorX - raptorPrevX < -5) {
                if (n > 1) {
                    raptor.changeSprite(n - 1);
                } else {
                    raptor.changeSprite(1);
                }
            } else if (raptorX == raptorPrevX) {
                if (n != 4) {
                    raptor.changeSprite(n + ((4 - n) / Math.abs(4 - n)));
                }
            }
            raptorPrevX = raptorX;
        } else if (raptor != null && !touched && alive) {
            int n = raptor.getSpriteNumber();
            if (n != 4) {
                raptor.changeSprite(n + ((4 - n) / Math.abs(4 - n)));
            }
        }

        if (touched && alive) {
            makeShot();
        } else {

        }

        blowUpUsedRocket();
        renderEnemies();
        hitCheck();
        checkEnemiesDestroyed();

        checkExplosionFinished(explosionList);
        for (AbstractObject object : explosionList) {
            object.changeSprite(object.getSpriteNumber() + 1);
        }

        removeUsedEnemies();
        recycleBackground();

        if (isGameOver() && alive) {
            raptor = new RaptorExplosion(bitMap.get("raptor_explosion"), raptor.getBasePoint(), raptor.getHeight() * 2);
            alive = false;
        } else if (raptor != null && isGameOver() && !alive && raptor.getSpriteNumber() == 49) {
            saveFinalScore(raptor.getScore());
            if (!uiLock && !uiLockD) {
                uiLock = true;
            }
            raptor = null;
        } else if (raptor != null && !alive && raptor.getSpriteNumber() < 49) {
            raptor.changeSprite(raptor.getSpriteNumber() + 1);
        }
    }

    private void recycleBackground() {
        for (Background b : backgrounds) {
            b.doNextMove();
        }
        if (backgrounds.getLast().getDst().top > 0) {
            int newBaseLine = (int) backgrounds.getLast().getDst().top - displayBottom;
            backgrounds.add(new Background(bitMap.get("background"), displayRight, displayBottom, newBaseLine));
            backgroundList.add(backgrounds.getLast());
        }
        removeExtraElements();
    }

    public void removeExtraElements() {
        if (backgrounds.getFirst().getDst().top > displayBottom) {
            backgrounds.removeFirst();
        }
    }

    public void renderEnemies() {
        if (queue.isEmpty()) {
            for (int i = 0; i < ENEMIES_QUEUE; i++) {
                queue.add(generateEnemy());
            }
        } else if (!enemiesList.isEmpty()) {
            if ((enemiesList.get(enemiesList.size() - 1).getBasePoint().y >= displayBottom * .3f)) {
                enemiesList.add(queue.poll());
            }
        } else enemiesList.add(queue.poll());

        if (!enemiesList.isEmpty()) {
            for (AbstractObject object : enemiesList) {
                enemyX = object.getBasePoint().x;
                int n = object.getSpriteNumber();
                if (enemyX - object.getXPrev() > 3) {
                    if (n < 7) {
                        object.changeSprite(n + 1);
                    } else {
                        object.changeSprite(7);
                    }
                } else if (enemyX - object.getXPrev() < -3) {
                    if (n > 1) {
                        object.changeSprite(n - 1);
                    } else {
                        object.changeSprite(1);
                    }
                } else if (Math.abs(enemyX - object.getXPrev()) < 3) {
                    if (n != 4) {
                        object.changeSprite(n + ((4 - n) / Math.abs(4 - n)));
                    }
                }
                object.setXPrev(enemyX);
            }
        }

        isCrashed(raptor, enemiesList);
    }

    public void removeUsedEnemies() {
        if (!enemiesList.isEmpty()) {
            if (enemiesList.get(0).getDst().top > displayBottom) {
                enemiesList.remove(0);
            }
        }
    }

    public AbstractObject generateEnemy() {
        int count = nameList.size();
        int pos = (int) Math.round((count - 1) * Math.random());
        AbstractObject object = null;
        switch (nameList.get(pos)) {
            case "scout":
                object = createScout();
                break;
            case "reaper":
                object = createReaper();
                break;
            case "arrow":
                object = createArrow();
                break;
        }
        return object;
    }

    public boolean isCrashed(AbstractObject hero, List<AbstractObject> enemies) {
        if (hero != null && !enemies.isEmpty()) {
            Iterator<AbstractObject> iterator = enemies.listIterator();
            while (iterator.hasNext()) {
                AbstractObject obj = iterator.next();
                float xDelta = Math.abs(raptor.getBasePoint().x - obj.getBasePoint().x);
                float yDelta = Math.abs(raptor.getBasePoint().y - obj.getBasePoint().y);
                if ((xDelta <= (raptor.getWidth() * 0.85)) && ((yDelta <= (raptor.getWidth() / 2)))) {
                    raptorHit(obj);
                    raptor.setScore(raptor.getScore() + obj.getHealthPoints());
                    refreshScore(raptor.getScore());
                    makeExplosion(obj);
                    iterator.remove();
                }
            }
        }
        return false;
    }

    public void raptorHit(AbstractObject object) {
        if (raptor != null) {
            raptor.setHealthPoints(raptor.getHealthPoints() - object.getHealthPoints());
            raptor.setScore(raptor.getScore() + object.getHealthPoints());
            heathLine.recalculate(raptor.getHealthPoints());
        }
    }

    public void makeExplosion(AbstractObject object) {
        explosionList.add(new EnemyExplosion(object.getTrajectory(), bitMap.get("enemy_explosion"), object.getBasePoint(), object.getHeight(), ObjectConstants.BACKGROUND_SPEED));
    }

    public void checkExplosionFinished(List<AbstractObject> list) {
        if (list != null && !list.isEmpty()) {
            Iterator<AbstractObject> iterator = list.listIterator();
            while (iterator.hasNext()) {
                AbstractObject obj = iterator.next();
                if (obj.getSpriteNumber() == EnemyExplosion.SPRITE_AMOUNT) {
                    iterator.remove();
                }
            }
        }
    }

    public void makeShot() {
        if (ammoList.isEmpty()) {
            lunchRocket();
        } else {
            Ammo r = ammoList.get(ammoList.size() - 1);
            if ((r.getLounchPointY() - r.getBasePoint().y) > displayBottom * r.getNextShotDistance()) {
                lunchRocket();
            }
        }
    }

    public void lunchRocket() {
        ammoList.add(new Rocket(rocketTrajectory, bitMap.get("rocket"), new PointF(raptorX, raptor.getBasePoint().y - .5f * raptor.getHeight()), displayBottom * Rocket.ROCKET_SIZE));
    }

    public void blowUpUsedRocket() {
        if (!ammoList.isEmpty()) {
            Ammo r = ammoList.get(0);
            if ((r.getLounchPointY() - r.getBasePoint().y) > displayBottom * r.getDistance()) {
                ammoList.remove(0);
            }
        }
    }

    public void hitCheck() {
        if (!enemiesList.isEmpty() && !ammoList.isEmpty()) {
            for (AbstractObject obj : enemiesList) {
                Iterator<Ammo> ammoIterator = ammoList.listIterator();
                while (ammoIterator.hasNext()) {
                    if (isHit(obj, ammoIterator.next())) {
                        long hitPoints = obj.getHealthPoints() - raptor.getDemage() < 0 ? obj.getHealthPoints() : raptor.getDemage();
                        raptor.setScore(raptor.getScore() + hitPoints);
                        refreshScore(raptor.getScore());
                        ammoIterator.remove();
                        obj.setHealthPoints(obj.getHealthPoints() - raptor.getDemage());
                    }
                }
            }
        }
    }

    private boolean isHit(AbstractObject enemy, Ammo rocket) {
        if (Math.abs(enemy.getBasePoint().x - rocket.getBasePoint().x) < .1f * enemy.getWidth()) {
            if (Math.abs(enemy.getBasePoint().y - rocket.getBasePoint().y) < .5f * enemy.getHeight()) {
                return true;
            }
        } else if (Math.abs(enemy.getBasePoint().x - rocket.getBasePoint().x) < .25f * enemy.getWidth()) {
            if (Math.abs(enemy.getBasePoint().y - rocket.getBasePoint().y) < .2f * enemy.getHeight()) {
                return true;
            }
        } else if (Math.abs(enemy.getBasePoint().x - rocket.getBasePoint().x) < .4f * enemy.getWidth()) {
            if (Math.abs(enemy.getBasePoint().y - rocket.getBasePoint().y) < .05f * enemy.getHeight()) {
                return true;
            }
        }
        return false;
    }

    public void checkEnemiesDestroyed() {
        if (!enemiesList.isEmpty()) {
            Iterator<AbstractObject> iterator = enemiesList.listIterator();
            while (iterator.hasNext()) {
                AbstractObject obj = iterator.next();
                if (obj.getHealthPoints() < 0) {
                    makeExplosion(obj);
                    iterator.remove();
                }
            }
        }
    }

    public boolean isGameOver() {
        if (raptor != null) {
            return raptor.getHealthPoints() <= 0 ? true : false;
        }
        return true;
    }


    public List<AbstractObject> getEnemiesList() {
        return enemiesList;
    }

    public List<Ammo> getAmmoList() {
        return ammoList;
    }

    public List<Background> getBackgroundList() {
        return backgroundList;
    }

    public List<AbstractObject> getExplosionList() {
        return explosionList;
    }

    public AbstractObject getRaptor() {
        return raptor;
    }

    public int getRandomePos(int var, float dist) {
        double temp = var * dist;
        int rnd = (int) (temp * Math.random());
        return (int) (var * ((1 - dist) / 2) + rnd);
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void setTouched(boolean touched, PointF point) {
        if (raptor != null) {
            touchCenter = raptor.getBasePoint();
            float xDelta = Math.abs(point.x - touchCenter.x);
            float yDelta = Math.abs(point.y - touchCenter.y);
            float hipo = (float) Math.sqrt(xDelta * xDelta + yDelta * yDelta);
            if (hipo <= displayBottom * TAP_AREA) {
                this.touched = touched;
            }
        }
    }

    private AbstractObject createScout() {
        Scout scout = new Scout(bitMap.get("scout"), new PointF(getRandomePos(displayRight, Scout.DISTRIBUTION), 0 - displayBottom * .2f), displayBottom * Scout.SCOUT_SIZE, level.getScoutSpeed());
        scout.getTrajectory().setWidth(displayRight).setHeight(displayBottom);
        return scout;
    }

    private AbstractObject createReaper() {
        Reaper reaper = new Reaper(bitMap.get("reaper"), new PointF(getRandomePos(displayRight, Reaper.DISTRIBUTION), 0 - displayBottom * .2f), displayBottom * Reaper.REAPER_SIZE, level.getReaperSpeed());
        reaper.getTrajectory().setWidth(displayRight).setHeight(displayBottom);
        return reaper;
    }

    private AbstractObject createArrow() {
        Arrow scout = new Arrow(bitMap.get("arrow"), new PointF(getRandomePos(displayRight, Arrow.DISTRIBUTION), 0 - displayBottom * .2f), displayBottom * Arrow.ARROW_SIZE, level.getArrowSpeed());
        scout.getTrajectory().setWidth(displayRight).setHeight(displayBottom);
        return scout;
    }

    private void saveFinalScore(long score) {
        this.finalScore = score;
    }

    public long getFinalScore() {
        return finalScore;
    }

    public RaptorHealth getHeathLine() {
        return heathLine;
    }

    public ScoreBox getScoreBox() {
        return scoreBox;
    }

    public ProgressContainer getProgress() {
        return progress;
    }

    public void refreshScore(long score) {
        if (scoreBox != null && alive) {
            scoreBox.setString(score);
        }
    }

    public void replay() {
        if (raptor == null) {
            uiLock = false;
            uiLockD = false;
            raptor = Raptor.getRaptor(null, bitMap.get("raptor"), new PointF(displayRight / 2, displayBottom * .85f), displayBottom * Raptor.RAPTOR_SIZE);
            raptor.setHealthPoints(ObjectConstants.RAPTOR_HEALTH);
            raptor.setScore(0);
            alive = true;
            heathLine = null;
            scoreBox = null;
            heathLine = new RaptorHealth(raptor.getHealthPoints(), progress.getDst());
            scoreBox = new ScoreBox(progress.getDst());
            for(AbstractObject o : enemiesList){
                o.setSpeed(ObjectConstants.SCOUT_SPEED);
            }
            if (level != null) {
                level.stop();
            }
            level = new LevelThread(raptor);
            new Thread(level).start();
        }
    }

    public boolean isUIAllowed() {
        return uiLock;
    }

    public void setUiLock(boolean flag) {
        uiLock = flag;
    }

    public void setUiLockD(boolean uiLockD) {
        this.uiLockD = uiLockD;
    }
}
