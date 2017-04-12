package com.example.maxim.endlessgunner;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by maxim on 2017-03-02.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private SceneManager manager;

    public GamePanel(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        Constants.CURRENT_CONTEXT = context;
        this.thread = new MainThread(getHolder(), this);
        this.manager = new SceneManager();
        setFocusable(true);
        setZOrderOnTop(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        thread = new MainThread(getHolder(), this);
        Constants.INIT_TIME = System.currentTimeMillis();
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        while(retry)
        {
            try {
                thread.setRunning(false);
                thread.join();
                if(MenuScene.backgroundMusic != null)
                {
                    if(MenuScene.backgroundMusic.isPlaying())
                    {
                        MenuScene.currentPosMediaPlayer = MenuScene.backgroundMusic.getCurrentPosition();
                        MenuScene.backgroundMusic.stop();
                    }
                }
                if(GameplayScene.backgroundMusic != null && GameplayScene.backgroundMusic.isPlaying())
                {
                    GameplayScene.currentPosMediaPlayer = GameplayScene.backgroundMusic.getCurrentPosition();
                    GameplayScene.backgroundMusic.stop();
                }
            } catch(Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        manager.recieveTouch(event);
        return true;
        //return super.onTouchEvent(event);
    }

    public void update()
    {
        manager.update();

    }

    @Override
    public void draw(Canvas canvas)
    {
        manager.draw(canvas);
        super.draw(canvas);
    }
}
