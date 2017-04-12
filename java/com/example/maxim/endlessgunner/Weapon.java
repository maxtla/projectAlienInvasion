package com.example.maxim.endlessgunner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by maxim on 2017-03-05.
 */

public class Weapon implements GameObject {
    private Point weaponPoint;
    private Rect rectangle;
    private Animation weapon;


    public Weapon(Point playerPoint)
    {
        this.weaponPoint = new Point(playerPoint.x - Constants.PLAYER_WIDTH/2, playerPoint.y + Constants.PLAYER_HEIGHT/3);
        this.rectangle = new Rect(20, 20, 50, 50);
        this.rectangle.set(weaponPoint.x - rectangle.width()/2, weaponPoint.y - rectangle.height()/2, weaponPoint.x + rectangle.width()/2, weaponPoint.y + rectangle.height()/2);
        Bitmap gun = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.gun08);
        this.weapon = new Animation(new Bitmap[]{gun}, 5.f);
    }

    public Point getWeaponPoint()
    {
        return this.weaponPoint;
    }

    public Rect getRectangle()
    {
        return this.rectangle;
    }

    @Override
    public void update() {

    }

    public void update(Point playerPoint)
    {
        this.weaponPoint.set(playerPoint.x - Constants.PLAYER_WIDTH/2, playerPoint.y + Constants.PLAYER_HEIGHT/3);
        this.rectangle.set(weaponPoint.x - rectangle.width()/2, weaponPoint.y - rectangle.height()/2, weaponPoint.x + rectangle.width()/2, weaponPoint.y + rectangle.height()/2);
    }

    @Override
    public void draw(Canvas canvas) {
        this.weapon.play();
        this.weapon.draw(canvas, rectangle);
    }
}
