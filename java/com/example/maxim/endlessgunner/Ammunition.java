package com.example.maxim.endlessgunner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by maxim on 2017-03-08.
 */

public class Ammunition implements GameObject {
    private Rect rectangle;
    private Point point;
    private Animation animation;
    private boolean pickedUp;

    public Ammunition()
    {
        this.rectangle = new Rect(0, 0, 50 ,50);
        double x = Math.random() * Constants.SCREEN_WIDTH;
        double y = Math.random() * Constants.SCREEN_HEIGHT;
        this.point = new Point((int)x,(int)y);
        this.rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);

        Bitmap sprite = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.powerupblue_bolt);
        this.animation = new Animation(new Bitmap[]{sprite}, 1.0f);
        this.pickedUp = false;
    }

    @Override
    public void draw(Canvas canvas) {
        this.animation.play();
        this.animation.draw(canvas, rectangle);
    }

    @Override
    public void update() {
        if(pickedUp)
        {
            double x = Math.random() * Constants.SCREEN_WIDTH;
            double y = Math.random() * Constants.SCREEN_HEIGHT;
            this.point.x = (int)x;
            this.point.y = (int)y;
            this.rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);
            pickedUp = false;
        }
    }

    public boolean playerCollided(Rect player)
    {
        if(Rect.intersects(rectangle, player))
            pickedUp = true;
        return pickedUp;
    }
}
