package jem2dc.cs2110.virginia.edu.game.Characters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import jem2dc.cs2110.virginia.edu.game.R;

/**
 * Created by student on 4/13/2015.
 */


public class Duke extends View implements Actors {

    Bitmap duke = BitmapFactory.decodeResource(getResources(), R.drawable.duke);
    public Rect bounds;
    public Point point;
    public Point vel;


    public Duke(Context context) {
        super(context);
    }

    @Override
    public void move() {

    }

    public int getcharX() {
        return point.x;
    }

    public int getcharY() {
        return point.y;
    }

    @Override
    public void setCharX( int x ) {
        point.x = x;
    }

    @Override
    public void setCharY(int y ) {
        point.y = y ;
    }

    @Override
    public Bitmap getBMap() {
        return duke;
    }

    //returns the collision boundaries
    @Override
    public Rect getRect() {
        return bounds;
    }

    //returns center
    @Override
    public Point getPoint() {
        return point;
    }

    @Override
    public int getVelocityY() {
        return 0;
    }

    @Override
    public int getVelocityX() {
        return 0;
    }

    @Override
    public void setVelocity(int x, int y) {
        vel = new Point (x,y);
    }

    // sets center of sprite
    @Override
    public void setPoint(int x, int y) {

        point = new Point(x, y);

    }

    //discover bounds of sprite for collision controllers
    @Override
    public int getCharHeight() {
        return duke.getHeight();
    }

    @Override
    public int getCharWidth() {
        return duke.getWidth();
    }

}