package com.example.maxim.endlessgunner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;


/**
 * Created by maxim on 2017-03-02.
 */

public class EnemyHandler {
    private Enemy[] enemies;
    private long startTime;
    public static int ENEMIES_AMOUNT_AT_CREATION = 10;
    private int score = 0;

    public EnemyHandler()
    {
        this.enemies = new Enemy[ENEMIES_AMOUNT_AT_CREATION];
        this.startTime = System.currentTimeMillis();
        populateEnemies();
    }

    public int getScore(){return score;}

    private void populateEnemies()
    {
        int offSetX = 200;
        int offSetY = 300;
        for(int i = 0; i < ENEMIES_AMOUNT_AT_CREATION; i++)
        {
            int randX = (int) (Math.random() * (Constants.SCREEN_WIDTH - offSetX));
            int randY = (int) (Math.random() * (Constants.SCREEN_WIDTH + offSetY));
            enemies[i]=new Enemy(new Point(randX, -randY));
        }
    }

    public boolean projectileCollided(Rect projectile)
    {
        for (Enemy item: enemies) {
            if(!item.getMarkedDead())
            {
                if(Rect.intersects(item.getRectangle(), projectile))
                {
                    item.setMarkedDead();
                    score += 10;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean playerCollided(Rect player)
    {
        for (Enemy item: enemies) {
            if(item.playerCollided(player))
                return true;
        }
        return false;
    }

    public void update(Point playerPoint)
    {
        if(startTime < Constants.INIT_TIME)
            startTime = Constants.INIT_TIME;
        for (Enemy item: enemies)
        {
            if(!item.getMarkedDead())
            {
                item.moveTowardsPlayer(playerPoint);
                item.update(playerPoint);
            }
            else
            {
                item.update(playerPoint);
                if(System.currentTimeMillis() - item.getDeathTime() > 1500)
                {
                    int offSetX = 200;
                    int offSetY = 300;
                    int randX = (int) (Math.random() * (Constants.SCREEN_WIDTH - offSetX));
                    int randY = (int) (Math.random() * (Constants.SCREEN_WIDTH + offSetY));
                    item.setPoint(randX, -randY);
                }
            }
        }
    }

    public void draw(Canvas canvas)
    {
        for(Enemy item: enemies)
            item.draw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(70);
        canvas.drawText("" + score, 50, 50 + paint.descent() - paint.ascent(), paint);
    }
}
