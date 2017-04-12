package com.example.maxim.endlessgunner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;


/**
 * Created by maxim on 2017-03-08.
 */

public class Button implements GameObject {
    private Rect rectangle;
    private Point point;
    private Animation button;
    private Paint paint;
    private String info;

    public Button(Rect rect, Point p, String info)
    {
        this.rectangle = rect;
        this.point = p;
        this.info = info;
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);
        Rect temp = new Rect();
        paint.getTextBounds(info, 0, info.length(), temp);
        point.x = point.x - temp.width()/2;
        point.y = point.y + temp.height()/3;
        Bitmap img = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.buttonblue);
        button = new Animation(new Bitmap[]{img}, 1.0f);
    }

    @Override
    public void draw(Canvas canvas) {
        button.play();
        button.draw(canvas, rectangle);
        canvas.drawText(info, point.x, point.y, paint);
    }

    @Override
    public void update() {

    }

    public Rect getRectangle()
    {
        return rectangle;
    }
}
