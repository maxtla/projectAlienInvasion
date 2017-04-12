package com.example.maxim.endlessgunner;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.support.annotation.IntegerRes;
import android.view.MotionEvent;

/**
 * Created by maxim on 2017-03-09.
 */

public class HighScoreScene implements Scene {
    private Paint playerPaint;
    private Paint scorePaint;
    private Button backButton;
    private Background background;
    private String player;
    private String score;
    private MediaPlayer buttonClickSound;
    private long initTime;

    public HighScoreScene() {
        playerPaint = new Paint();
        scorePaint = new Paint();
        playerPaint.setColor(Color.WHITE);
        scorePaint.setColor(Color.WHITE);
        playerPaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setTextAlign(Paint.Align.CENTER);
        playerPaint.setTextSize(140);
        scorePaint.setTextSize(100);
        backButton = new Button(new Rect(0, 0, 400, 100), new Point(Constants.SCREEN_WIDTH/10* 5, Constants.SCREEN_HEIGHT/25 * 22), "Back");
        background = new Background();
        SharedPreferences preferences = Constants.CURRENT_CONTEXT.getSharedPreferences(Constants.PREFERENCE, Constants.CURRENT_CONTEXT.MODE_PRIVATE);
        player = preferences.getString(Constants.PLAYER_NAME, "");
        int temp = preferences.getInt(Constants.CURRENT_HIGHSCORE, 0);
        score = String.valueOf(temp);
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

    @Override
    public void update() {
        if(MenuScene.backgroundMusic != null && !MenuScene.backgroundMusic.isPlaying())
        {
            MenuScene.backgroundMusic = MediaPlayer.create(Constants.CURRENT_CONTEXT, R.raw.menu_playback);
            MenuScene.backgroundMusic.setLooping(true);
            MenuScene.backgroundMusic.setVolume(10.0f, 10.0f);
            MenuScene.backgroundMusic.start();
            MenuScene.backgroundMusic.seekTo(MenuScene.currentPosMediaPlayer);
        }
        background.update();
    }

    @Override
    public void draw(Canvas canvas) {
        background.draw(canvas);
        backButton.draw(canvas);
        canvas.drawText(player, Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/8*3, playerPaint);
        canvas.drawText(score, Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/8*4, scorePaint);

    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
        SceneManager.scenes.remove(1);
        buttonClickSound.release();
    }

    @Override
    public void recieveTouch(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(backButton.getRectangle().contains((int)event.getX(),(int)event.getY()))
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
}
