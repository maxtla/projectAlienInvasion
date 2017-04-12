package com.example.maxim.endlessgunner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by maxim on 2017-03-05.
 */

public class Projectile implements GameObject {

    private Point projectilePoint;
    private Rect rectangle;
    private Animation beamBullet;
    private double xVel;
    private double yVel;
    public static double projectileSpeed = 40.0;
    public Projectile(Point weaponPoint, int x, int y)
    {
        this.projectilePoint = new Point(weaponPoint);
        this.rectangle = new Rect(10, 10, 30, 30);
        this.rectangle.set(projectilePoint.x - rectangle.width()/2, projectilePoint.y - rectangle.height()/2, projectilePoint.x + rectangle.width()/2, projectilePoint.y + rectangle.height()/2);
        Bitmap beam = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.laserblue03);
        this.beamBullet = new Animation(new Bitmap[]{beam}, 0.5f);

        double dx = projectilePoint.x - x;
        double dy = projectilePoint.y - y;
        double hypo = Math.hypot(dx, dy);
        dx /= hypo;
        dy /= hypo;
        xVel = dx * projectileSpeed;
        yVel = dy * projectileSpeed;

    }

    @Override
    public void update() {
        projectilePoint.x -= xVel;
        projectilePoint.y -= yVel;
        this.rectangle.set(projectilePoint.x - rectangle.width()/2, projectilePoint.y - rectangle.height()/2, projectilePoint.x + rectangle.width()/2, projectilePoint.y + rectangle.height()/2);
    }

    @Override
    public void draw(Canvas canvas) {
        this.beamBullet.play();
        this.beamBullet.draw(canvas, rectangle);
    }

    public boolean outOfBoundaries()
    {
        return projectilePoint.x < 0 || projectilePoint.x > Constants.SCREEN_WIDTH || projectilePoint.y < 0 || projectilePoint.y > Constants.SCREEN_HEIGHT;
    }

    public Rect getRectangle()
    {
        return this.rectangle;
    }
}
