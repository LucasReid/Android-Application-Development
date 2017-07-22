package com.example.lnr7605.fragmentmusicplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity
        implements SongTitleFragment.OnTitleSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_layout);

        if(findViewById(R.id.fragment_container)!=null){

            if(savedInstanceState != null){
                return;
            }
            //create an instance of SongTitleFragment and throw in the container
            SongTitleFragment titleFragment = new SongTitleFragment();

            titleFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_container, titleFragment).commit();
        }
    }

    @Override
    public void onTitleSelected(int position) {
        InfoFragment songInfo = (InfoFragment) getSupportFragmentManager().
                findFragmentById(R.id.info_fragment);

        if(songInfo!= null){
            //call a metod in the song info fragment to update its content
            songInfo.updateTitleView(position);
        }else{
            //if fragment is not in the one-pane layout we have to swap.
            InfoFragment fragment = new InfoFragment();
            Bundle args = new Bundle();
            args.putInt(InfoFragment.ARG_POSITION, position);
            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }
}
