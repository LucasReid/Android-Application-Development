package com.example.lnr7605.graphicsdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by lnr7605 on 9/20/16.
 */
public class GraphicsView extends View {
    int width, height;

    public GraphicsView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas){

        int palette[] = {
                Color.BLACK,
                Color.BLUE,
                Color.GRAY,
                Color.GREEN
        };
        width = canvas.getWidth();
        height = canvas.getHeight();

        // Paint cpaint = new Paint();
        //Paint tpaint = new Paint();

        // tpaint.setTextSize(20.0F);
        // tpaint.setColor(Color.WHITE);
        // cpaint.setColor(Color.MAGENTA);
        // Path circle = new Path();
        // circle.addCircle(150,150 ,100, Path.Direction.CW);
        // canvas.drawPath(circle, cpaint);

        // canvas.drawTextOnPath(message, circle, 0, 20,tpaint);
        Paint lpaint = new Paint();
        lpaint.setColor(Color.GREEN);
        lpaint.setStrokeWidth(8.0f);

        String message = "Many is a word that somehow keeps you guessing";

        int x,y;
        int deltaX = height/100;
        int deltaY= width/100;
        //int deltaX =
        for (int i=0; i < 100; i++){
            x = deltaX*i;
            y = deltaY*i;
            lpaint.setColor(palette[i%palette.length]);
            canvas.drawLine(0 , x ,y , height, lpaint);


        }
    }
}
