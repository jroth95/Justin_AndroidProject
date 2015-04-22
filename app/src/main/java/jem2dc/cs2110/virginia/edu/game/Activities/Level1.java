package jem2dc.cs2110.virginia.edu.game.Activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import jem2dc.cs2110.virginia.edu.game.R;
import jem2dc.cs2110.virginia.edu.game.drawing.GameBoard;

import static android.widget.Toast.LENGTH_SHORT;


public class Level1 extends Activity implements View.OnClickListener {
    View screen;

    private Handler frame = new Handler(); //how many times per millisecond the frame will update
    private static final int FRAME_RATE = 20; //50 frames per second

    private Point dukeVel;
    private Point ballVel = new Point(10, 10);
    private float cavX;
    private int dukeMaxY, dukeMaxX, cavmanMaxX, cavmanMaxY;
    public static boolean shootClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Setting up user input for CavMan
        screen = (View) findViewById(R.id.screen);
        screen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cavX = event.getX();
                if (!shootClicked) {
                    ((GameBoard) findViewById(R.id.the_canvas)).b.setCharX((int) cavX + 170), (((GameBoard) findViewById(R.id.the_canvas)).c.getcharY() +80) );

                    ( ((GameBoard) findViewById(R.id.the_canvas)).c.setCharX((int) cavX), (((GameBoard) findViewById(R.id.the_canvas)).c.getcharY()) );
                return false;
            }
        }});

        Handler h = new Handler();

        //Resets the current activity
        ((Button) (findViewById(R.id.the_button))).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Level1.this, Level1.class);
                startActivity(t);
                finish();
            }
        });

        //handles shooting
        (findViewById(R.id.shoot)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shootBall();
            }
        });

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                initialize();
            }
        }, 1000);
    }


    private Point getRandomVelocity() {
        Random r = new Random();
        int min = 1;
        int max = 5;
        int x = r.nextInt(max - min + 1) + min;
        int y = r.nextInt(max - min + 1) + min;
        return new Point(x, y);
    }

    //just has duke rn
    private Point getRandomPoint() {
        Random r = new Random();
        int minX = 0;
        int maxX = findViewById(R.id.the_canvas).getWidth() -
                ((GameBoard) findViewById(R.id.the_canvas)).d.getCharWidth();
        int x = 0;
        int minY = 0;
        int maxY = findViewById(R.id.the_canvas).getHeight() -
                ((GameBoard) findViewById(R.id.the_canvas)).d.getCharHeight();

        int y = 0;
        x = r.nextInt(maxX - minX + 1) + minX;
        y = r.nextInt(maxY - minY + 1) + minY;
        return new Point(x, y);
    }


    synchronized public void initialize() {

        Point p1, p2, p3;
        do {
            p1 = getRandomPoint();
            p2 = new Point(300, 800);
            p3 = new Point(p2.x + 170, p2.y + 80);
        }
        //only sets duke, cavman and ball rn
        while (Math.abs(p1.x - p2.x) < ((GameBoard) findViewById(R.id.the_canvas)).d.getCharHeight() + 200);
        ((GameBoard) findViewById(R.id.the_canvas)).d.setPoint(p1.x, p1.y);
        ((GameBoard) findViewById(R.id.the_canvas)).c.setPoint(p2.x, p2.y);
        (((GameBoard) findViewById(R.id.the_canvas))).b.setPoint(p3.x, p3.y);

        //Give the enemy a random velocity
        dukeVel = getRandomVelocity();

        //Set our boundaries for the sprites
        dukeMaxX = findViewById(R.id.the_canvas).getWidth() -
                ((GameBoard) findViewById(R.id.the_canvas)).d.getCharWidth();
        dukeMaxY = findViewById(R.id.the_canvas).getHeight() -
                ((GameBoard) findViewById(R.id.the_canvas)).d.getCharHeight();
        cavmanMaxX = findViewById(R.id.the_canvas).getWidth() -
                ((GameBoard) findViewById(R.id.the_canvas)).c.getCharWidth();
        cavmanMaxY = findViewById(R.id.the_canvas).getHeight() -
                ((GameBoard) findViewById(R.id.the_canvas)).c.getCharHeight();
        ((Button) findViewById(R.id.the_button)).setEnabled(true);
        frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);


    }


    @Override
    synchronized public void onClick(View v) {
        initialize();
    }

    private Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            //Before we do anything else check for a collision
            if (((GameBoard) findViewById(R.id.the_canvas)).wasCollisionDetected()) {
                Point collisionPoint =
                        ((GameBoard) findViewById(R.id.the_canvas)).getLastCollision();
               // if (collisionPoint.x >= 0) {
                   // ((TextView) findViewById(R.id.the_other_label)).setText("LastCollision XY (" + Integer.toString(collisionPoint.x) + "," + Integer.toString(collisionPoint.y) + ")");
                //}
                if (((GameBoard) findViewById(R.id.the_canvas)).wasBallCollisionDetected()) {
                    Point ballCollisionPoint = ((GameBoard) findViewById(R.id.the_canvas)).getLastCollision();
                }
                return;
            }

            if (((GameBoard) findViewById(R.id.the_canvas)).wasBallCollisionDetected()) {
                ((TextView) findViewById(R.id.the_other_label)).setText("Score: " + (((GameBoard) findViewById(R.id.the_canvas)).getScore()));
                return;
            }


            frame.removeCallbacks(frameUpdate);
            //First get the current positions of both sprites
            Point duke = new Point
                    (((GameBoard) findViewById(R.id.the_canvas)).d.getcharX(),
                            ((GameBoard) findViewById(R.id.the_canvas)).d.getcharY());
            Point ball = new Point(((GameBoard) findViewById(R.id.the_canvas)).b.getcharX(),
                    ((GameBoard) findViewById(R.id.the_canvas)).b.getcharY());
            //Now calc the new positions.
            //Note if we exceed a boundary the direction of the velocity gets reversed.
            duke.x = duke.x + dukeVel.x;
            if (duke.x > dukeMaxX || duke.x < 5) {
                dukeVel.x *= -1;
            }
            duke.y = duke.y + dukeVel.y;
            if (duke.y > dukeMaxY || duke.y < 5) {
                dukeVel.y *= -1;
            }
            //Handles ball movement
            if (shootClicked) {
                ball.y = ball.y - ballVel.y;
                Toast.makeText(Level1.this, "shoot", LENGTH_SHORT).show();
            }

            ((GameBoard) findViewById(R.id.the_canvas)).d.setPoint(duke.x, duke.y);
            ((GameBoard) findViewById(R.id.the_canvas)).b.setPoint(ball.x, ball.y);
            ((GameBoard) findViewById(R.id.the_canvas)).invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };

    public void shootBall() {
        shootClicked = true;
    }

    public void clickScore(View v){
        Intent i = new Intent(this, Winners.class);
        startActivity(i);
    }

}

