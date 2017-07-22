package com.example.lnr7605.fragmentmusicplayer;



import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


public class InfoFragment extends Fragment implements Runnable, OnSeekBarChangeListener,
        View.OnClickListener{

    public static String DEBUG_TAG = "InfoFragment";
    final static String ARG_POSITION = "position";
    private SeekBar seekBar;
    private Button play;
    private int currentPosition = -1;
    private MediaPlayer mp = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //will be needed in the two page layout
        if(savedInstanceState !=null){
            currentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        View inflate = inflater.inflate(R.layout.info_layout, container, false);

        seekBar = (SeekBar) inflate.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        play = (Button) inflate.findViewById(R.id.play);
        play.setOnClickListener(this);
        // Inflate the layout for this fragment
        return inflate;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if(args != null) {
            updateTitleView(args.getInt(ARG_POSITION));
            startMediaPlayer(args.getInt(ARG_POSITION));
        }else if(currentPosition != -1){
            updateTitleView(currentPosition);
            startMediaPlayer(currentPosition);
        }


    }

    public void updateTitleView(int position){
        TextView notes = (TextView) getActivity().findViewById(R.id.info);
        notes.setText(SongDatabase.songInfo[position]);
        currentPosition = position;
    }
    public void startMediaPlayer(int position){

        try{
            if(mp == null) {
                mp = new MediaPlayer();
                //start seekBar
                seekBar.setEnabled(true);
            }
            mp.reset();
            //new way to play media!
            //convert to file descriptor
            AssetFileDescriptor afd = getResources().openRawResourceFd(SongDatabase.resourceID[position]);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
            mp.start();

            seekBar.setMax(mp.getDuration());
            new Thread(this).start();

            //seekBar Stuff
        }catch(Exception e){
            Log.i(DEBUG_TAG, "Error on Player!");
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer mp){
                play.setText("Play");
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(ARG_POSITION, currentPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        if(mp.isPlaying())
            mp.pause();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void run() {
        int currentPosition = mp.getCurrentPosition();
        int songDuration = mp.getDuration();
        while (mp != null && currentPosition < songDuration) {
            try {
                Thread.sleep(10);
                currentPosition = mp.getCurrentPosition();
            }catch (Exception e) {
                return;
            }
            seekBar.setProgress(currentPosition);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        try{
            if(mp.isPlaying() || mp!=null){
                if(fromUser)
                    mp.seekTo(progress);
            }else if(mp == null){
                seekBar.setProgress(0);
            }
        }catch (Exception e){
            Log.e("seek bar",""+e);
            seekBar.setEnabled(false);
        }
    }


    public void onClick(View v) {
        if (v.equals(play)) {
            if (mp.isPlaying()) {
                mp.pause();
                play.setText("play");
            } else {
                mp.start();
                play.setText("pause");
                seekBar.setMax(mp.getDuration());
                new Thread(this).start();
            }
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}






















