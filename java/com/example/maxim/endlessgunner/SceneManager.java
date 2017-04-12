package com.example.maxim.endlessgunner;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by maxim on 2017-03-02.
 */

public class SceneManager implements Scene {
    public static ArrayList<Scene> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE;

    public SceneManager(){
            ACTIVE_SCENE = 0;
        scenes.add(new MenuScene());
    }

    @Override
    public void update() {
        scenes.get(ACTIVE_SCENE).update();
    }

    @Override
    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }

    @Override
    public void terminate() {

    }

    @Override
    public void recieveTouch(MotionEvent event) {
        scenes.get(ACTIVE_SCENE).recieveTouch(event);
    }
}
