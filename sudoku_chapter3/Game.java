package com.example.lnr7605.sudoku_chapter3;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Game extends AppCompatActivity {

    public static final String KEY_DIFFICULTY = "com.lnr7605.comp325";
    public static final String DEBUG_TAG = "COMP225";

    public static final int DIFFICULTY_CONTINUE = -1;
    public static final String PREF_PUZZLE = "puzzle";

    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFUCULTY_MEDIUM = 1;
    public static final int DIFFUCULTY_HARD = 2;

    private int puzzle[]= new int[9+9];
    private int[][][] used = new int[9][9][]; //ragged 3D array
    private PuzzleView puzzleView;



    private final String easyPuzzle =
                    "360000000004230800000004200" +
                    "070460003820000014500013020" +
                    "001900000007048300000000045";

    private final String mediumPuzzle =
                    "650000070000506000014000005" +
                    "007009000002314700000700800" +
                    "500000630000201000030000097";

    private final String hardPuzzle =
                    "009000000080605020501078000" +
                    "000000700706040102004000000" +
                    "000720903090301080000000600";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
        puzzle = getPuzzle(diff);

        calculateUsedTiles();
        puzzleView = new PuzzleView(this);
        setContentView(puzzleView);
        puzzleView.requestFocus();
        getIntent().putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);

    }
    private void calculateUsedTiles(){
        for (int i = 0; i<9; i++){
            for (int j =0; j<9; j++){
                used[i][j] = calculateUsedTiles(i,j);
            }
        }
    }
    private int[] calculateUsedTiles(int x, int y){
        int c[] = new int[9];

        //horizontal
        for(int i=0; i<9; i++){
            if(i == y)
                continue;

            int t =getTile(x,i);
            if(t != 0)
                c[t -1] =t;
        }
        for(int i=0; i<9; i++){
            if(i == x)
                continue;

            int t =getTile(i,y);
            if(t != 0)
                c[t -1] =t;
        }
            //3x3 block of cells
        int startx = (x/3) *3;
        int starty = (y/3) *3;

        for (int i=startx; i <startx+3; i++){
            for(int j = starty; j<starty+3; j++){
                if(x==y && y ==j)
                    continue;
                int t = getTile(i,j);
                if(t != 0)
                    c[t -1]=t;
            }
        }
        //Now compress the array
        //first count number of tiles in the array
        int nused =0;
        for(int t : c)
            if (t != 0)
                nused++;

        int compressed[] = new int[nused];
        nused =0;
        for (int t:c){
            if(t!=0)
                compressed[nused++] = t;
        }
        return compressed;
    }
    private int[] getPuzzle(int diff){
        String puz;
        //TODO continue previous state of the game
        switch(diff){
            case DIFFICULTY_CONTINUE:
                puz = getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE,
                        easyPuzzle);
                break;
            case DIFFUCULTY_HARD:
                puz = hardPuzzle;
                break;
            case DIFFUCULTY_MEDIUM:
                puz = mediumPuzzle;
                break;
            case DIFFICULTY_EASY:
            default:
                puz = easyPuzzle;
                break;
        }
        return fromPuzzleString(puz);
    }
    protected String getTilesString(int x, int y){
        int val = getTile(x,y);
        if(val ==0)
            return "";
        else
            return String.valueOf(val);
    }

    private int getTile(int x, int y){
        return puzzle[y * 9 + x];
    }
    protected boolean setTileIfValid(int x, int y, int value){
        //TODO
        //Will return a true if the input tile value is valid
        int tiles[] = getUsedTiles(x,y);
        if(value != 0){
            for (int tile :tiles){
                if(tile == value)
                    return false;
            }
        }
        setTile(x, y, value);
        calculateUsedTiles();
        return true;
    }

    private void setTile(int x, int y, int value){
        puzzle[y* 9+x] = value;
    }
    protected int[] getUsedTiles(int x, int y){
        return used[x][y];
    }


    protected void showKeyPadOrError(int x, int y){
        //TODO
        Log.i(DEBUG_TAG, "shoKeyPadOrError should run!");

        int tiles[] = getUsedTiles(x, y);

        if(tiles.length ==9){
            Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }else{
            Log.d(DEBUG_TAG, "show_keypad: used = "+toPuzzleString(tiles));
            Dialog dialog = new Keypad(this, tiles, puzzleView);
            dialog.show();
        }
    }
    private static int[] fromPuzzleString(String str){
        int[] puzzle = new int[str.length()];
        for(int i =0; i<str.length(); i++){
            puzzle[i] = Character.digit(str.charAt(i),10);//the 10 determins character value hex, ASCII.
        }
        return puzzle;
    }
    private static String toPuzzleString(int[] puzzle){
        StringBuilder buffer = new StringBuilder();
        for(int elem: puzzle){
            buffer.append(elem);
        }
        return buffer.toString();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(DEBUG_TAG, "onPause called");
        // add music pause here
        //now save current state of game
        getPreferences(MODE_PRIVATE).edit().putString(PREF_PUZZLE,
                toPuzzleString(puzzle)).commit();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Music.play(this, R.raw.game);
    }
}




















