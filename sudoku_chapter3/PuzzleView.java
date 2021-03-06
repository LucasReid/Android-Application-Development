package com.example.lnr7605.sudoku_chapter3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * TODO: document your custom view class.
 */
public class PuzzleView extends View {

    private static final String DEBUG_TAG = "Sudoku";
    private final Game game;

    private static final String SELX = "selX";
    private static final String SELY = "selY";
    private static final String VIEW_STATE = "viewState";
    private static int ID = 42;

    private float width, height;//width and height of the title
    private int selX, selY;//the current selection
    private final Rect selRect = new Rect();//currently selected title

    @SuppressWarnings("ResourceType")
    public PuzzleView(Context context) {
        super(context);
        game = (Game) context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setId(ID);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w/9.0f;
        height = h/9.0f;
        selRect.set(getRect(selX, selY));
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private Rect getRect(int x, int y){
        Rect newRect = new Rect( (int) (x * width), (int) (y * height),
                (int) (x * width +width), (int) (y * height +height));
        return newRect;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float strokeWidthMajor = 10.9f, strokeWidthMinor = 5.0f;
        //draw the board
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.puzzle_background));
        canvas.drawRect(0,0, getWidth(), getHeight(), background);

        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));
        dark.setStrokeWidth(8.0f);

        Paint hilite = new Paint();
        hilite.setColor(getResources().getColor(R.color.puzzle_hilite));

        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.puzzle_light));
        light.setStrokeWidth(5.0f);

        for(int i = 0; i<9; i++){
            float strokeWidth =0;
            if(i %3 ==0)
                strokeWidth = strokeWidthMajor;
            else
                strokeWidth = strokeWidthMinor;

            hilite.setStrokeWidth(strokeWidth);
            canvas.drawLine(0, i*height+ strokeWidth,getWidth(), i * height+strokeWidth, hilite);


            canvas.drawLine(i*width+strokeWidth, 0, i *width+strokeWidth,getHeight(), hilite);
        }
        for(int i=0; i<9; i++){
            canvas.drawLine(0, i*height, getWidth(), i *height, light);
            canvas.drawLine( i*width, 0 ,  i *width, getHeight(), light);
        }
        for(int i = 0; i<9; i++){
            if(i%3 != 0)
                continue;
            canvas.drawLine(0, i * height, getWidth(), i * height, dark);
            //canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);


            canvas.drawLine(i * width, 0 , i * width, getHeight(),dark);
            //canvas.drawLine(i * width + 1, 0 , i * width + 1, getHeight(),hilite);

        }


        //define the colors
        //draw minor grind lines
        //draw major grid lines
        //draw the numbers
        Paint forground = new Paint(Paint.ANTI_ALIAS_FLAG);
        forground.setColor(getResources().getColor(R.color.puzzle_foreground));

        forground.setStyle(Paint.Style.FILL);
        forground.setTextSize(height * 0.75f);
        forground.setTextScaleX(width/height);
        forground.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm = forground.getFontMetrics();

        float x=width/2;
        float y = height/2 - (fm.ascent +fm.descent)/2.0f;

        for(int i = 0; i<9; i++){
            for(int j = 0; j<9; j++){
                canvas.drawText(this.game.getTilesString(i, j),
                        i *width +x, j* height +y, forground);
            }
        }
        //draw hints
        //pick a hint color based on number of moves left
        //red means no moves
        if(Prefs.getHints(getContext())) {
            Paint hint = new Paint();
            int colors[] = {
                    getResources().getColor(R.color.puzzle_hint_0),
                    getResources().getColor(R.color.puzzle_hint_1),
                    getResources().getColor(R.color.puzzle_hint_2)
            };

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int movesLeft = 9 - game.getUsedTiles(i, j).length;
                    if (movesLeft < colors.length) {
                        Rect r = getRect(i, j);
                        hint.setColor(colors[movesLeft]);
                        canvas.drawRect(r, hint);
                    }
                }
            }
        }
        //draw selection
        Log.i(DEBUG_TAG, "selRect ="+selRect);
        Paint selected = new Paint();
        selected.setColor(getResources().getColor(R.color.puzzle_selected));
        canvas.drawRect(selRect, selected);


        super.onDraw(canvas);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        Log.d(DEBUG_TAG, "onKeyDown: keycode= "+keyCode+", event = "+event);

        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                select(selX, selY -1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                select(selX, selY +1);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                select(selX - 1,selY);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                select(selX + 1, selY);
                break;
            case KeyEvent.KEYCODE_SPACE:
                setSelectedTile(0);
                break;
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_1:
                setSelectedTile(1);
                break;
            case KeyEvent.KEYCODE_2:
                setSelectedTile(2);
                break;
            case KeyEvent.KEYCODE_3:
                setSelectedTile(3);
                break;
            case KeyEvent.KEYCODE_4:
                setSelectedTile(4);
                break;
            case KeyEvent.KEYCODE_5:
                setSelectedTile(5);
                break;
            case KeyEvent.KEYCODE_6:
                setSelectedTile(6);
                break;
            case KeyEvent.KEYCODE_7:
                setSelectedTile(7);
                break;
            case KeyEvent.KEYCODE_8:
                setSelectedTile(8);
                break;
            case KeyEvent.KEYCODE_9:
                setSelectedTile(9);
                break;

            case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    game.showKeyPadOrError(selX,selY);
                    break;
            default:
                return super.onKeyDown(keyCode, event);

        }
        return true;

    }
    public void setSelectedTile(int tile){
        if(game.setTileIfValid(selX, selY,tile)){
            invalidate();
        } else{
            Log.d(DEBUG_TAG, "setSelectedTile: invalid: "+tile);
            startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake));
        }
    }

    private void select(int x, int y){
        invalidate(selRect);
        selX = Math.min(Math.max(x,0),8);
        selY = Math.min(Math.max(y,0),8);
        selRect.set(getRect(selX,selY));
        invalidate(selRect);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable state = super.onSaveInstanceState();
        Log.d(DEBUG_TAG, "onSavedInstanceState");
        Bundle bundle = new Bundle();
        bundle.putInt(SELX, selX);
        bundle.putInt(SELY, selY);
        bundle.putParcelable(VIEW_STATE, state);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.d(DEBUG_TAG, "onRestoreInstanceState");
        Bundle bundle = new Bundle();
        int x = bundle.getInt(SELX);
        int y = bundle.getInt(SELY);
        select(x,y);
        super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE));
        return;
    }

    private Point getIndex(float x, float y){
        Point location = new Point((int) (x/width),(int) (y/height));
        return location;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float locX = event.getX();
        float locY = event.getY();

        Point loc = getIndex(locX, locY);
        if(loc.x ==selX && loc.y == selY){
            game.showKeyPadOrError(selX,selY);
        }else {
            select(loc.x, loc.y);
        }
        return super.onTouchEvent(event);
    }
}












