package com.example.lnr7605.sudoku_chapter3;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class Sudoku extends AppCompatActivity implements View.OnClickListener{
    public static final String DEBUG_TAG="COMP225_SUDOKU";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);

        View continueButton= findViewById(R.id.continue_button);//reference to the continue button.
        continueButton.setOnClickListener(this);

        View newGameButton = findViewById(R.id.newGame_button);
        newGameButton.setOnClickListener(this);//the on click of THIS class will run for new game.

        findViewById(R.id.about_button).setOnClickListener(this);


        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.continue_button:
                startGame(Game.DIFFICULTY_CONTINUE);
                break;
            case R.id.about_button:
                Intent intent = new Intent(this, About.class);//this intent is the use of the about text found in strings.xml
                startActivity(intent);
                break;
            case R.id.newGame_button:
                openNewGameDialog();
                break;
            case R.id.exit_button:
                finish();
                break;

            //more buttons will go here
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //called when menu creation happens
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.settings:
                //kick off fragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, new Prefs());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Music.play(this, R.raw.main);
    }

    private void openNewGameDialog(){
        // TODO
        AlertDialog.Builder newGameDialog = new AlertDialog.Builder(this);

        newGameDialog.setTitle(R.string.new_game_title);
        newGameDialog.setItems(R.array.difficulty,
                new DialogInterface.OnClickListener() {                          //any item in the array that gets clicked gets called here
                    @Override                                                    // its not in onclick listener because once the difficulty is called it does not need to be
                    public void onClick(DialogInterface dialogInterface, int i) {//looked for again, until another new game.
                        startGame(i);
                    }
                });
        newGameDialog.show();
    }
    private void startGame(int diff){
        Log.d(DEBUG_TAG, "Clicked on " + diff);
        Intent intent = new Intent(Sudoku.this, Game.class);
        intent.putExtra(Game.KEY_DIFFICULTY, diff);
        startActivity(intent);
    }
}
