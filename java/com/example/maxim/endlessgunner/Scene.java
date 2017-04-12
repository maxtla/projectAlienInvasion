package com.example.maxim.endlessgunner;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by maxim on 2017-03-02.
 */

public interface Scene {
    public void update();
    public void draw(Canvas canvas);
    public void terminate();
    public void recieveTouch(MotionEvent event);
}
