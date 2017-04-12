package com.example.maxim.endlessgunner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by maxim on 2017-03-08.
 */

public class Background implements GameObject {
    private Rect rectangle;
    private Animation background;

    public Background()
    {
        rectangle = new Rect(0,0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        Bitmap img = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.black);
        background = new Animation(new Bitmap[]{img}, 1.0f);
    }
    @Override
    public void draw(Canvas canvas) {
        background.play();
        background.draw(canvas, rectangle);
    }

    @Override
    public void update() {

    }
}
