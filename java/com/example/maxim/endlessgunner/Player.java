package com.example.maxim.endlessgunner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by maxim on 2017-03-02.
 */

public class Player implements GameObject {

    private Rect rectangle;
    private Animation idle;
    private Animation walkUp;
    private Animation walkDown;
    private Animation walkLeft;
    private Animation walkRight;
    private AnimationManager animationManager;

    public Player(Rect rectangle)
    {
        this.rectangle = rectangle;
        Constants.PLAYER_WIDTH = rectangle.width();
        Constants.PLAYER_HEIGHT = rectangle.height();

        Bitmap idleImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_stand);
        this.idle = new Animation(new Bitmap[]{idleImg}, 2);

        Bitmap walkUp1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_climb1);
        Bitmap walkUp2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_climb2);
        this.walkUp = new Animation(new Bitmap[]{walkUp1, walkUp2}, 0.5f);

        Bitmap walk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk1);
        Bitmap walk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk2);
        this.walkDown = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);
        this.walkRight = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);
        this.walkLeft = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        this.animationManager = new AnimationManager(new Animation[]{this.idle, this.walkUp, this.walkDown, this.walkLeft, this.walkRight});
    }

    public Rect getRectangle()
    {
        return this.rectangle;
    }

    @Override
    public void draw(Canvas canvas) {
        animationManager.draw(canvas, rectangle);

    }

    @Override
    public void update() {
        animationManager.update();
    }

    public void update(Point point) {
        float oldLeft = rectangle.left;
        float oldTop = rectangle.top;
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);


        int state = 0;
        if(rectangle.top - oldTop < -5)
        {
            state = 1;
        }
        else if (rectangle.top - oldTop > 5)
        {
            state = 2;
        }
        else if(rectangle.left - oldLeft < -5)
        {
            state = 3;
        }
        else if (rectangle.left - oldLeft > 5)
        {
            state = 4;
        }

        animationManager.playAnim(state);
        animationManager.update();
    }
}
