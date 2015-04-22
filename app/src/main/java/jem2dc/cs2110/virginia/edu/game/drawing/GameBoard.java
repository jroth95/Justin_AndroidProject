package jem2dc.cs2110.virginia.edu.game.drawing;

/**
 * Created by Justin on 4/21/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import jem2dc.cs2110.virginia.edu.game.Activities.Level1;
import jem2dc.cs2110.virginia.edu.game.Characters.Actors;
import jem2dc.cs2110.virginia.edu.game.Characters.Ball;
import jem2dc.cs2110.virginia.edu.game.Characters.Cavman;
import jem2dc.cs2110.virginia.edu.game.Characters.Duke;
import jem2dc.cs2110.virginia.edu.game.Characters.UNC;
import jem2dc.cs2110.virginia.edu.game.R;

public class GameBoard extends View {
    int score = 0;
    private Paint p;

   public ArrayList<Actors> enemyList = new ArrayList<>();
   public UNC u;
   public Duke d;
   public Cavman c;
   public Ball b;


    private boolean collisionDetected = false;
    private boolean ballCollision = false;
    private Point lastCollision = new Point(-1, -1);
    //initializes the Bitmaps of characters




    //return the point of the last collision
    synchronized public Point getLastCollision() {
        return lastCollision;
    }

    //return the collision flag
    synchronized public boolean wasCollisionDetected() {
        return collisionDetected;
    }

    synchronized public boolean wasBallCollisionDetected() {
        return ballCollision;
    }

    public GameBoard(Context context, AttributeSet aSet) {
        super(context, aSet);

        u = new UNC(context);
        d = new Duke(context);
        c = new Cavman(context);
        b = new Ball(context);

        enemyList.add(u);
        enemyList.add(d);

        u.setPoint(-1,-1);
        d.setPoint(-1,-1);
        c.setPoint(-1,-1);
        b.setPoint(-1,-1);

        u.bounds = new Rect (0, 0,u.getBMap().getWidth(), u.getBMap().getHeight());
        d.bounds = new Rect (0, 0,u.getBMap().getWidth(), u.getBMap().getHeight());
        c.bounds = new Rect (0, 0,u.getBMap().getWidth(), u.getBMap().getHeight());
        b.bounds = new Rect (0, 0,u.getBMap().getWidth(), u.getBMap().getHeight());

        p = new Paint();
    }

    private boolean checkForCollision() {
        if (d.getcharX() < 0 && c.getcharX() < 0 && d.getcharY() < 0 && c.getcharY() < 0) return false;
        Rect r1 = new Rect(d.getcharX(), d.getcharY(), d.getcharX()
                + d.bounds.width(), d.getcharY() + d.bounds.height());
        Rect r2 = new Rect(c.getcharX(), c.getcharY(), c.getcharX() +
                c.bounds.width(), c.getcharY() + c.bounds.height());
        Rect r3 = new Rect(r1);
        if (r1.intersect(r2)) {
            for (int i = r1.left; i < r1.right; i++) {
                for (int j = r1.top; j < r1.bottom; j++) {
                    if (b.getBMap().getPixel(i - r3.left, j - r3.top) !=
                            Color.TRANSPARENT) {
                        if (c.getBMap().getPixel(i - r2.left, j - r2.top) !=
                                Color.TRANSPARENT) {
                            lastCollision = new Point(c.getcharX() +
                                    i - r2.left, c.getcharY() + j - r2.top);
                            return true;
                        }
                    }
                }
            }
        }
        lastCollision = new Point(-1, -1);
        return false;
    }

    private boolean checkForBallCollision() {
        if (d.getcharX() < 0 && b.getcharX() < 0 && d.getcharY() < 0 && b.getcharY() < 0) return false;
        Rect r1 = new Rect(d.getcharX(), d.getcharY(), d.getcharX()
                + d.bounds.width(), d.getcharY() + d.bounds.height());
        Rect r2 = new Rect(b.getcharX(), b.getcharY(), b.getcharX() +
                b.bounds.width(), b.getcharY() + b.bounds.height());
        Rect r3 = new Rect(r1);
        if (r1.intersect(r2)) {
            for (int i = r1.left; i < r1.right; i++) {
                for (int j = r1.top; j < r1.bottom; j++) {
                    if (d.getBMap().getPixel(i - r3.left, j - r3.top) !=
                            Color.TRANSPARENT) {
                        if (b.getBMap().getPixel(i - r2.left, j - r2.top) !=
                                Color.TRANSPARENT) {
                            lastCollision = new Point(b.getcharX() +
                                    i - r2.left, b.getcharY() + j - r2.top);
                            return true;
                        }
                    }
                }
            }
        }
        lastCollision = new Point(-1, -1);
        return false;
    }

    @Override
    synchronized public void onDraw(Canvas canvas) {
        for( int i = 0; i < enemyList.size(); ++i ){
            canvas.drawBitmap(enemyList.get(i).getBMap(), enemyList.get(i).getcharX()- (enemyList.get(i).getBMap().getWidth() / 2), enemyList.get(i).getcharY() - (enemyList.get(i).getBMap().getHeight() / 2), null);
            enemyList.get(i).move();
        }




        //create a transparent canvas
        p.setColor(Color.TRANSPARENT);
        p.setAlpha(0);
        p.setStrokeWidth(1);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);


        //drawing the main character sprites

        if (c.getcharX() >= 0) {
            canvas.drawBitmap(c.getBMap(), c.getcharX() , c.getcharY(), null);
        }
        if (b.getcharY() >= 0) {
            canvas.drawBitmap(b.getBMap(), b.getcharX(), b.getcharY(), null);
        }
        else if (b.getcharY() < 0 || ballCollision) {
            canvas.drawBitmap(b.getBMap(), c.getcharX()  +170, c.getcharY() + 80, null);
            Level1.shootClicked =false;
        }

        collisionDetected = checkForCollision();
        if (collisionDetected) {
            //draws a red x
            p.setColor(Color.RED);
            p.setAlpha(255);
            p.setStrokeWidth(5);
            canvas.drawLine(lastCollision.x - 5, lastCollision.y - 5,
                    lastCollision.x + 5, lastCollision.y + 5, p);
            canvas.drawLine(lastCollision.x + 5, lastCollision.y - 5,
                    lastCollision.x - 5, lastCollision.y + 5, p);
        }

        ballCollision = checkForBallCollision();
        if(ballCollision) {
            //blue x for testing collision
            p.setColor(Color.BLUE);
            p.setAlpha(255);
            p.setStrokeWidth(5);
            canvas.drawLine(lastCollision.x - 5, lastCollision.y - 5,
                    lastCollision.x + 5, lastCollision.y + 5, p);
            canvas.drawLine(lastCollision.x + 5, lastCollision.y - 5,
                    lastCollision.x - 5, lastCollision.y + 5, p);
            score += 100;

        }

    }
//Score settings
    public int getScore() {
        return score;
    }

}


