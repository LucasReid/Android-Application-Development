package com.example.lnr7605.sudoku_chapter3;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by lnr7605 on 11/3/16.
 */
public class Music {
    private static MediaPlayer mp =null;

    public static void play(Context context, int resource){
        stop(context);
        if(Prefs.getMusic(context)){
            mp = MediaPlayer.create(context, resource);
            mp.setLooping(true);
            mp.start();
        }
    }

    public static void stop(Context context){
        if (mp != null){
            mp.stop();
            mp.release();
            mp = null;
        }
    }

}
