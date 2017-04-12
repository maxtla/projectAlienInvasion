package com.example.maxim.endlessgunner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by maxim on 2017-03-02.
 */

public class GameplayScene implements Scene{
    private Background background;
    private EnemyHandler enemyHandler;
    private Ammunition ammo;
    private boolean gameOver = false;
    private Rect r = new Rect();
    private Player player;
    private Weapon weapon;
    private Point playerPoint;
    private ArrayList<Projectile> projectiles;
    private int nrOfProjectiles;
    private String currentAmountOfProjectiles;
    private Paint paint;
    private OrientationData orientationData;
    private long frameTime;
    private Button playAgain;
    private Button submitHighscore;
    private Button quit;
    public static String name;
    private boolean updateHighscoreFile;
    public static MediaPlayer backgroundMusic;
    public static int currentPosMediaPlayer = 0;
    private MediaPlayer laser;
    private MediaPlayer gameOverSound;
    private MediaPlayer enemyHitSound;
    private MediaPlayer buttonClickSound;
    private MediaPlayer ammoSound;
    private long initTime;

    public GameplayScene() {
        background = new Background();
        player = new Player(new Rect(0,0,100,100));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2);
        player.update(playerPoint);
        enemyHandler = new EnemyHandler();
        weapon = new Weapon(playerPoint);
        ammo = new Ammunition();
        this.projectiles = new ArrayList<>();
        this.nrOfProjectiles = 20;
        this.currentAmountOfProjectiles = new String("x " + String.valueOf(this.nrOfProjectiles));
        this.paint = new Paint();
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setColor(Color.BLUE);
        paint.setTextSize(70);
        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
        playAgain = new Button(new Rect(0, 0, 400, 100), new Point(Constants.SCREEN_WIDTH/10* 5, Constants.SCREEN_HEIGHT/25 * 15), "Play Again");
        submitHighscore = new Button(new Rect(0, 0, 400, 100), new Point(Constants.SCREEN_WIDTH/10* 5, Constants.SCREEN_HEIGHT/25 * 17), "Submit Score");
        quit = new Button(new Rect(0, 0, 400, 100), new Point(Constants.SCREEN_WIDTH/10* 5, Constants.SCREEN_HEIGHT/25 * 19), "Quit to menu");
        name = "";
        updateHighscoreFile = false;
        backgroundMusic = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.gameplay_playback);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(10.0f, 10.0f);
        backgroundMusic.start();
    }

    public void reset()
    {
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/2);
        player.update(playerPoint);
        Enemy.speed = 2.0;
        this.enemyHandler = new EnemyHandler();
        this.projectiles = new ArrayList<>();
        nrOfProjectiles = 30;
        this.ammo = new Ammunition();
        this.orientationData = new OrientationData();
        orientationData.register();
        backgroundMusic.seekTo(0);
        backgroundMusic.start();
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
        SceneManager.scenes.remove(1);
        Enemy.speed = 2.0;
        backgroundMusic.release();
        if(laser != null)
            laser.release();
        if(enemyHitSound != null)
            enemyHitSound.release();
        gameOverSound.release();
        buttonClickSound.release();
    }

    @Override
    public void draw(Canvas canvas) {
        background.draw(canvas);
        player.draw(canvas);
        weapon.draw(canvas);
        enemyHandler.draw(canvas);
        ammo.draw(canvas);
        for (Projectile item: projectiles) {
            item.draw(canvas);
        }
        if(gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.BLUE);
            drawCenterText(canvas, paint, "Game Over");
            playAgain.draw(canvas);
            submitHighscore.draw(canvas);
            quit.draw(canvas);
        }
        canvas.drawText(currentAmountOfProjectiles, Constants.SCREEN_WIDTH - 50, 50 + paint.descent() - paint.ascent(), paint);
    }

    @Override
    public void update() {
        if(!gameOver)
        {
            if(backgroundMusic != null && !backgroundMusic.isPlaying())
            {
                backgroundMusic = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.gameplay_playback);
                backgroundMusic.setLooping(true);
                backgroundMusic.setVolume(10.0f, 10.0f);
                backgroundMusic.start();
                backgroundMusic.seekTo(currentPosMediaPlayer);
            }

            if(frameTime < Constants.INIT_TIME)
            {
                frameTime = Constants.INIT_TIME;
            }
            int elapsedTime = (int)(System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            enemyHandler.update(playerPoint);
            if(orientationData.getOrientation() != null && orientationData.getStartOrientation() != null)
            {
                float deltaPitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];
                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH/500f;
                float ySpeed = deltaPitch * Constants.SCREEN_HEIGHT/500f;

                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed * elapsedTime : 0;
            }

            if(playerPoint.x < player.getRectangle().width()/2)
                playerPoint.x = player.getRectangle().width()/2;
            else if (playerPoint.x > Constants.SCREEN_WIDTH - player.getRectangle().width()/2)
                playerPoint.x = Constants.SCREEN_WIDTH - player.getRectangle().width()/2;

            if(playerPoint.y < player.getRectangle().height()/2)
                playerPoint.y = player.getRectangle().height()/2;
            else if(playerPoint.y > Constants.SCREEN_HEIGHT - player.getRectangle().height()/2)
                playerPoint.y = Constants.SCREEN_HEIGHT - player.getRectangle().height()/2;

            player.update(playerPoint);
            weapon.update(playerPoint);

            if(ammo.playerCollided(player.getRectangle()))
            {
                ammo.update();
                nrOfProjectiles += 5;
                playAmmoSound();
            }
            this.currentAmountOfProjectiles = "x " + String.valueOf(this.nrOfProjectiles);

            if(enemyHandler.playerCollided(player.getRectangle()))
            {
                gameOver = true;
                backgroundMusic.pause();
                playGameOverSound();
            }

           Iterator<Projectile> iterator = projectiles.iterator();
            while (iterator.hasNext())
            {
                Projectile bullet = iterator.next();
                bullet.update();
                boolean remove = false;
                if(enemyHandler.projectileCollided(bullet.getRectangle()))
                {
                    remove = true;
                    playEnemyHitSound();
                }
                else if(bullet.outOfBoundaries())
                    remove = true;

                if(remove)
                    iterator.remove();
            }


        }
        else if(gameOver && updateHighscoreFile && !name.equals(""))
        {
            updateHighScoreFile();
            updateHighscoreFile = false;
            name = "";
        }
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch ( event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && !player.getRectangle().contains((int)event.getX(), (int)event.getY()) && nrOfProjectiles != 0)
                {
                    projectiles.add(new Projectile(weapon.getWeaponPoint(), (int)event.getX(), (int)event.getY()));
                    nrOfProjectiles--;
                    playLaserSound();
                }
                if(gameOver && playAgain.getRectangle().contains((int)event.getX(), (int)event.getY()))
                {
                    playButtonClickSound();
                    reset();
                    gameOver = false;
                    orientationData.newGame();
                }
                if(gameOver && submitHighscore.getRectangle().contains((int)event.getX(), (int)event.getY()))
                {
                    getInputFromPlayer();
                    updateHighscoreFile = true;
                    playButtonClickSound();
                }
                if(gameOver && quit.getRectangle().contains((int)event.getX(), (int)event.getY()))
                {
                    playButtonClickSound();
                    long endTime = System.currentTimeMillis();
                    while(endTime - initTime < 300)
                        endTime = System.currentTimeMillis();
                    terminate();
                }
                break;
        }
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    private void getInputFromPlayer()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Constants.CURRENT_CONTEXT);
        final EditText input = new EditText(Constants.CURRENT_CONTEXT);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GameplayScene.name = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private boolean updateHighScoreFile()
    {
        boolean success = false;
        if(!name.equals(""))
        {
            SharedPreferences preferences = Constants.CURRENT_CONTEXT.getSharedPreferences(Constants.PREFERENCE, Constants.CURRENT_CONTEXT.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            if(enemyHandler.getScore() > preferences.getInt(Constants.CURRENT_HIGHSCORE, 0))
            {
                editor.putString(Constants.PLAYER_NAME, name);
                editor.putInt(Constants.CURRENT_HIGHSCORE, enemyHandler.getScore());
                editor.commit();
                success = true;
            }
        }
        return success;
    }

    private void playLaserSound()
    {
        if(laser != null)
        {
            laser.stop();
            laser.release();
        }
        laser = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.laser);
        laser.setVolume(0.5f, 0.5f);
        if(laser != null)
            laser.start();
    }

    private void playEnemyHitSound()
    {
        if(enemyHitSound != null)
        {
            enemyHitSound.stop();
            enemyHitSound.release();
        }
        enemyHitSound = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.enemy_hit);
        enemyHitSound.setVolume(10.f, 10.f);
        if(enemyHitSound != null)
            enemyHitSound.start();
    }

    private void playGameOverSound()
    {
        if(gameOverSound != null)
        {
            gameOverSound.stop();
            gameOverSound.release();
        }
        gameOverSound = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.game_over);
        if(gameOverSound != null)
            gameOverSound.start();
    }

    private void playButtonClickSound()
    {
        if(buttonClickSound != null)
        {
            buttonClickSound.stop();
            buttonClickSound.release();
        }
        buttonClickSound = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.button_click);
        if(buttonClickSound != null)
        {
            buttonClickSound.start();
            initTime = System.currentTimeMillis();
        }
    }

    private void playAmmoSound()
    {
        if(ammoSound != null)
        {
            ammoSound.stop();
            ammoSound.release();
        }
        ammoSound = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.ammo_pickup);
        if(ammoSound != null)
            ammoSound.start();
    }

}
