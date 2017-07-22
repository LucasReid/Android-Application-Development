package com.example.lnr7605.sudoku_chapter3;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by lnr7605 on 10/13/16.
 */
public class Keypad extends Dialog {

    protected static final String DEBUG_TAG = "Sudoku";
    private final View[] keys =new View[9];
    private View keypad;
    private final PuzzleView puzzleView;

    private final int[] inUsedTiles;

    public Keypad(Context context, int inUsedTiles[], PuzzleView puzzleView){
        super(context);
        this.inUsedTiles = inUsedTiles;
        this.puzzleView = puzzleView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.keypad_title);
        setContentView(R.layout.keypad);
        findViews();

        for(int element :inUsedTiles){
            if(element !=0){
                keys[element -1].setVisibility(View.INVISIBLE);
            }
        }
        setListeners();
    }
    private void findViews(){
        keypad = findViewById(R.id.keypad);

        keys[0] = findViewById(R.id.keypad_1);
        keys[1] = findViewById(R.id.keypad_2);
        keys[2] = findViewById(R.id.keypad_3);
        keys[3] = findViewById(R.id.keypad_4);
        keys[4] = findViewById(R.id.keypad_5);
        keys[5] = findViewById(R.id.keypad_6);
        keys[6] = findViewById(R.id.keypad_7);
        keys[7] = findViewById(R.id.keypad_8);
        keys[8] = findViewById(R.id.keypad_9);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        int title =0;
        switch(keyCode){
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE:
                title = 0;
                break;
            case KeyEvent.KEYCODE_1:
                title = 1;
                break;
            case KeyEvent.KEYCODE_2:
                title = 2;
                break;
            case KeyEvent.KEYCODE_3:
                title = 3;
                break;
            case KeyEvent.KEYCODE_4:
                title = 4;
                break;
            case KeyEvent.KEYCODE_5:
                title = 5;
                break;
            case KeyEvent.KEYCODE_6:
                title = 6;
                break;
            case KeyEvent.KEYCODE_7:
                title = 7;
                break;
            case KeyEvent.KEYCODE_8:
                title = 8;
                break;
            case KeyEvent.KEYCODE_9:
                title = 9;
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        if (isValid(title))
            returnResult(title);
        return true;
    }
    private void setListeners(){
        //TODO
        for(int i=0; i<keys.length; i++) {
            final int t = i + 1;
            keys[i].setOnClickListener(new View.OnClickListener() {
                public void onClick (View v){
                    returnResult(t);
                    }
            });

            }
        }
    private boolean isValid(int tile){
        for(int t :inUsedTiles){
            if(t == tile)
                return false;
        }
        return true;
    }
          private void returnResult(int tile){
              puzzleView.setSelectedTile(tile);
              dismiss();

        }
}




