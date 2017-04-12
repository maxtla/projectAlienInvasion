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

public class Enemy implements GameObject{
    private Rect rectangle;
    private Point enemyPoint;
    public static double speed = 2.0;
    private long initTime;
    private boolean collision = false;
    private boolean markedDead = false;
    private long deathTime;
    private Animation idle;
    private Animation walkLeft;
    private Animation walkRight;
    private Animation death;
    private AnimationManager manager;

    public Enemy(Point enemyPoint)
    {
        this.rectangle = new Rect(0, 0, 100, 100);
        this.enemyPoint = enemyPoint;
        this.initTime = System.currentTimeMillis();
        Bitmap idleImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spider);
        this.idle = new Animation(new Bitmap[]{idleImg}, 2.f);

        Bitmap deathImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spider_dead);
        this.death = new Animation(new Bitmap[]{deathImg}, 2.5f);

        Bitmap walk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spider_walk1);
        Bitmap walk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spider_walk2);
        this.walkLeft = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);
        this.walkRight = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);

        this.manager = new AnimationManager(new Animation[]{idle, walkLeft, walkRight, death});
        update();
    }

    public long getDeathTime()
    {
        return this.deathTime;
    }

    public boolean getMarkedDead()
    {
        return this.markedDead;
    }

    public Rect getRectangle()
    {
        return this.rectangle;
    }

    public void setMarkedDead()
    {
        if(!markedDead)
        {
            this.markedDead = true;
            this.deathTime = System.currentTimeMillis();
        }
    }

    public void setPoint(int x, int y)
    {
        markedDead = false;
        enemyPoint.x = x;
        enemyPoint.y = y;
        rectangle.set(enemyPoint.x - rectangle.width()/2, enemyPoint.y - rectangle.height()/2, enemyPoint.x + rectangle.width()/2, enemyPoint.y + rectangle.height()/2);
    }

    public void moveTowardsPlayer(Point playerPoint)
    {
        if(!markedDead)
        {
            double dx = playerPoint.x - enemyPoint.x;
            double dy = playerPoint.y - enemyPoint.y;
            double hypo = Math.hypot(dx, dy);
            dx /= hypo;
            dy /= hypo;
            double xVel = dx * speed;
            double yVel = dy * speed;

            enemyPoint.x += xVel;
            enemyPoint.y += yVel;

        }
    }

    public boolean playerCollided(Rect player)
    {
        if(!markedDead)
        {
            if(Rect.intersects(this.rectangle, player))
            {
                collision = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        manager.draw(canvas, rectangle);
    }

    @Override
    public void update() {
        this.speed += 0.001;
        int state = 0;
        if(!collision && rectangle.exactCenterX() - enemyPoint.x > 5 && !markedDead)
        {
            rectangle.set(enemyPoint.x - rectangle.width()/2, enemyPoint.y - rectangle.height()/2, enemyPoint.x + rectangle.width()/2, enemyPoint.y + rectangle.height()/2);
            state = 1;
        }
        else if (!collision && rectangle.exactCenterX() - enemyPoint.x < 5 && !markedDead)
        {
            rectangle.set(enemyPoint.x - rectangle.width()/2, enemyPoint.y - rectangle.height()/2, enemyPoint.x + rectangle.width()/2, enemyPoint.y + rectangle.height()/2);
            state = 2;
        }
        else if (markedDead)
        {
            rectangle.set(enemyPoint.x - rectangle.width()/2, enemyPoint.y - rectangle.height()/2, enemyPoint.x + rectangle.width()/2, enemyPoint.y + rectangle.height()/2);
            state = 3;
        }

        manager.playAnim(state);
        manager.update();
    }

    public void update(Point player)
    {
        this.speed += 0.001;
        int state = 0;
        if(!collision && player.x - enemyPoint.x < 5 && !markedDead)
        {
            rectangle.set(enemyPoint.x - rectangle.width()/2, enemyPoint.y - rectangle.height()/2, enemyPoint.x + rectangle.width()/2, enemyPoint.y + rectangle.height()/2);
            state = 1;
        }
        else if (!collision && player.x - enemyPoint.x > 5 && !markedDead)
        {
            rectangle.set(enemyPoint.x - rectangle.width()/2, enemyPoint.y - rectangle.height()/2, enemyPoint.x + rectangle.width()/2, enemyPoint.y + rectangle.height()/2);
            state = 2;
        }
        else if (markedDead)
        {
            //rectangle.set(enemyPoint.x - rectangle.width()/2, enemyPoint.y - rectangle.height()/2, enemyPoint.x + rectangle.width()/2, enemyPoint.y + rectangle.height()/2);
            state = 3;
        }

        manager.playAnim(state);
        manager.update();
    }

}
