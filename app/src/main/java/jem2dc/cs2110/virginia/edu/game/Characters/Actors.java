package jem2dc.cs2110.virginia.edu.game.Characters;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by student on 4/13/2015.
 */
public interface Actors {

    public void move();

    public Bitmap getBMap();

    public int getcharX();

    public int getcharY();

    public void setCharX( int x);

    public void setCharY( int y);

    public Rect getRect();

    public Point getPoint();

    public int getVelocityY();

    public int getVelocityX();

    public void setVelocity(int x, int y);

    public void setPoint(int x, int y);

    public int getCharHeight();

    public int getCharWidth();
}
