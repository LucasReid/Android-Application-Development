package com.example.lnr7605.sudoku_chapter3;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;

/**
 * Created by lnr7605 on 9/15/16.
 */
public class Prefs extends PreferenceFragment {
    private static final String OPT_HINTS ="hints";
    private static final boolean OPT_HINTS_DEFAULT = true;

    private static final String OPT_MUSIC ="music";
    private static final boolean OPT_MUSIC_DEFAULT = true;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        if(Build.VERSION.SDK_INT >= 23) {
            int color = getResources().getColor(R.color.background, getActivity().getTheme());
            super.onViewCreated(view, savedInstanceState);
            view.setBackgroundColor(color);
        }

    }
    public static boolean getHints(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_HINTS,
                OPT_HINTS_DEFAULT);
    }
    public static boolean getMusic(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_MUSIC,
                OPT_MUSIC_DEFAULT);
    }

    @Override
    public void onDetach() {
        if(Prefs.getMusic(getActivity())){
            Music.play(getActivity(),R.raw.game);

        }else
            Music.stop(getActivity());
        super.onDetach();
    }
}
