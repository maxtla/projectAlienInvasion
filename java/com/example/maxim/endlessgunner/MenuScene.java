package com.example.maxim.endlessgunner;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.MotionEvent;

/**
 * Created by maxim on 2017-03-08.
 */

public class MenuScene implements Scene {
    private Background background;
    private Button startGame;
    private Button highscore;
    private Button quit;
    private Animation backgroundAnimation1;
    private Animation backgroundAnimation2;
    private Rect backgroundPlayer;
    private Point backgroundPlayerPoint;
    private Rect backgroundEnemy;
    private Point backgroundEnemyPoint;
    private AnimationManager manager1;
    private AnimationManager manager2;
    public static MediaPlayer backgroundMusic;
    public static int currentPosMediaPlayer = 0;
    private boolean recreateBackgroundMusic;
    private MediaPlayer buttonClickSound;
    private Paint headerPaint1;
    private Paint headerPaint2;
    private Paint signaturePaint;

    public MenuScene() {
        background = new Background();
        startGame = new Button(new Rect(0, 0, 400, 100), new Point(Constants.SCREEN_WIDTH/10* 5, Constants.SCREEN_HEIGHT/25 * 15), "Start Game");
        highscore = new Button(new Rect(0, 0, 400, 100), new Point(Constants.SCREEN_WIDTH/10* 5, Constants.SCREEN_HEIGHT/25 * 17), "Highscore");
        quit = new Button(new Rect(0, 0, 400, 100), new Point(Constants.SCREEN_WIDTH/10* 5, Constants.SCREEN_HEIGHT/25 * 19), "Quit");

        Bitmap walk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk1);
        Bitmap walk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk2);
        this.backgroundAnimation1 = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        Bitmap enemyWalk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spider_walk1);
        Bitmap enemyWalk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spider_walk2);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        enemyWalk1 = Bitmap.createBitmap(enemyWalk1, 0, 0, enemyWalk1.getWidth(), enemyWalk1.getHeight(), m, false);
        enemyWalk2 = Bitmap.createBitmap(enemyWalk2, 0, 0, enemyWalk2.getWidth(), enemyWalk2.getHeight(), m, false);
        this.backgroundAnimation2 = new Animation(new Bitmap[]{enemyWalk1, enemyWalk2}, 0.5f);

        backgroundPlayer =  new Rect(0,0, 100, 100);
        backgroundPlayerPoint = new Point(Constants.SCREEN_WIDTH/8 * 5, Constants.SCREEN_HEIGHT/2);
        backgroundPlayer.set(backgroundPlayerPoint.x - backgroundPlayer.width()/2, backgroundPlayerPoint.y - backgroundPlayer.height()/2, backgroundPlayerPoint.x + backgroundPlayer.width()/2, backgroundPlayerPoint.y + backgroundPlayer.height()/2);

        backgroundEnemy =  new Rect(0,0, 100, 100);
        backgroundEnemyPoint = new Point(Constants.SCREEN_WIDTH/8 * 3, Constants.SCREEN_HEIGHT/2);
        backgroundEnemy.set(backgroundEnemyPoint.x - backgroundEnemy.width()/2, backgroundEnemyPoint.y - backgroundEnemy.height()/2, backgroundEnemyPoint.x + backgroundEnemy.width()/2, backgroundEnemyPoint.y + backgroundEnemy.height()/2);

        manager1 = new AnimationManager(new Animation[]{backgroundAnimation1});
        manager2 = new AnimationManager(new Animation[]{backgroundAnimation2});

        backgroundMusic = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.menu_playback);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(10.0f, 10.0f);
        backgroundMusic.start();

        recreateBackgroundMusic = false;

        headerPaint1 = new Paint();
        headerPaint2 = new Paint();
        signaturePaint = new Paint();

        headerPaint1.setColor(Color.WHITE);
        headerPaint2.setColor(Color.WHITE);
        signaturePaint.setColor(Color.WHITE);

        headerPaint1.setTextAlign(Paint.Align.CENTER);
        headerPaint2.setTextAlign(Paint.Align.CENTER);
        signaturePaint.setTextAlign(Paint.Align.CENTER);

        headerPaint1.setTextSize(70);
        headerPaint2.setTextSize(60);
        signaturePaint.setTextSize(40);

        headerPaint1.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
    }

    private void playButtonClickSound()
    {
        if(buttonClickSound != null)
        {
            buttonClickSound.stop();
            buttonClickSound.release();
        }
        buttonClickSound = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.button_click);
        if(buttonClickSound != null) {
            buttonClickSound.start();
        }
    }

    @Override
    public void update() {
        if(backgroundMusic != null && !backgroundMusic.isPlaying())
        {
            backgroundMusic = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.menu_playback);
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(10.0f, 10.0f);
            backgroundMusic.start();
            backgroundMusic.seekTo(currentPosMediaPlayer);
        }
        background.update();
        manager1.playAnim(0);
        manager1.update();
        manager2.playAnim(0);
        manager2.update();
        if(recreateBackgroundMusic)
        {
            backgroundMusic = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.menu_playback);
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(10.0f, 10.0f);
            backgroundMusic.start();
            recreateBackgroundMusic = false;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        background.draw(canvas);
        canvas.drawText("SPACE INVASION: INSECTOIDS", Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/20 * 4, headerPaint1);
        canvas.drawText("ENDLESS MODE", Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/20*6, headerPaint2);
        canvas.drawText("by Max Tlatlik", Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/20*8, signaturePaint);
        manager1.draw(canvas, backgroundPlayer);
        manager2.draw(canvas, backgroundEnemy);
        startGame.draw(canvas);
        highscore.draw(canvas);
        quit.draw(canvas);
    }

    @Override
    public void terminate() {
        backgroundMusic.release();
        if(buttonClickSound != null)
            buttonClickSound.release();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(startGame.getRectangle().contains((int)event.getX(),(int)event.getY()))
                {
                    playButtonClickSound();
                    SceneManager.scenes.add(new GameplayScene());
                    SceneManager.ACTIVE_SCENE = 1;
                    recreateBackgroundMusic = true;
                    backgroundMusic.release();
                    backgroundMusic = null;
                }
                if(highscore.getRectangle().contains((int)event.getX(), (int)event.getY()))
                {
                    playButtonClickSound();
                    SceneManager.scenes.add(new HighScoreScene());
                    SceneManager.ACTIVE_SCENE = 1;
                    SharedPreferences preferences = Constants.CURRENT_CONTEXT.getSharedPreferences(Constants.PREFERENCE, Constants.CURRENT_CONTEXT.MODE_PRIVATE);
                    System.out.println(preferences.getString(Constants.PLAYER_NAME, ""));
                    System.out.println(preferences.getInt(Constants.CURRENT_HIGHSCORE, 0));
                }
                if(quit.getRectangle().contains((int)event.getX(),(int)event.getY()))
                {
                    terminate();
                }
                break;
        }

    }
}
