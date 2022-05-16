package com.example.mastermindmobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.mastermindmobile.GameStack.GameRecord;

import java.util.ArrayList;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity {

    private LinearLayout gameBoard;
    private EditText guessEntry;

    DisplayMetrics displayMetrics = new DisplayMetrics();

    ArrayList<ImageView> boxes = new ArrayList<>();
    ArrayList<TextView> guesses = new ArrayList<>();

    GameRecord gameRecord;

    SharedPreferences gameSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        setContentView(R.layout.activity_game);

        gameSettings = GameActivity.this.getSharedPreferences(getString(R.string.pref_class),Context.MODE_PRIVATE);

        gameRecord = new GameRecord(gameSettings.getInt(getString(R.string.var_count),4),
                gameSettings.getInt(getString(R.string.const_count),8),
                gameSettings.getBoolean(getString(R.string.toggle_repeat),false));

        guessEntry = findViewById(R.id.guessEntry);
        guessEntry.setFocusableInTouchMode(true);
        guessEntry.requestFocus();
        guessEntry.setHint("Range: 1-" + gameRecord.numberOfColors + (gameRecord.isRepeat?" +repetitions":""));
        guessEntry.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER
                    && !gameRecord.victory){
                    boolean isAccepted = acceptEntry();
                    if(isAccepted)
                        guessEntry.setText("");
                    if(gameRecord.victory)
                        victoryPopUp();
                    else if(gameRecord.endOfGame)
                        endgamePopUp();
                    return isAccepted;
                }
                return false;
            }
        });

        gameBoard = findViewById(R.id.gameBoard);
        gameBoard.setPadding(0,50,0,10);
        buildBoard(gameRecord.numberOfColumns,gameRecord.numberOfGuesses);

    }

    private boolean acceptEntry(){
        int[] guess = new int[gameRecord.numberOfColumns];
        String entry = guessEntry.getText().toString();
        String[] entries = entry.split("[,. -]+");

        Log.d("EditText", Arrays.toString(entries));

        if(entries.length != gameRecord.numberOfColumns){
            try{
                int guessVal = Integer.parseInt(entry);
                for(int i = gameRecord.numberOfColumns-1; i >= 0; i--){
                    guess[i] = guessVal % 10;
                    guessVal /= 10;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        else{
            try{
                for(int i = gameRecord.numberOfColumns-1; i >= 0; i--){
                    guess[i] = Integer.parseInt(entries[i]);
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        Log.d("EditText", Arrays.toString(guess));

        gameRecord.appendGuess(guess);
        setGuessText(gameRecord.getCurrentTurn(),guess);
        setGridColors(gameRecord.getCurrentTurn(),gameRecord.countReds(),gameRecord.countWhites());

        return true;
    }

    private void setGuessText(int turn, int[] guess){
        StringBuilder displayText = new StringBuilder("");
        for(int val:guess){
            if(val < 10)
                displayText.append(" ");
            displayText.append(val);
            displayText.append(" ");
        }
        displayText.append(" ");
        guesses.get(turn).setText(displayText);
    }

    private void setGridColors(int turn, int reds, int whites){
        int index = turn * gameRecord.numberOfColumns;
        for(int i = index; i < index + gameRecord.numberOfColumns; i++){
            if(reds > 0){
                boxes.get(i).setImageResource(R.drawable.red_box);
                reds--;
            }
            else if(whites > 0){
                boxes.get(i).setImageResource(R.drawable.white_box);
                whites--;
            }
        }
    }

    private void buildBoard(int noOfSquares, int noOfGuesses){

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/DroidSansMono.ttf");

        for(int i = 0;i < noOfGuesses; i++){
            TextView textView = new TextView(this);
            textView.setTextSize((int)108/noOfSquares);
            textView.setTypeface(type);

            StringBuilder text = new StringBuilder(" ");
            for(int j = 0; j < noOfSquares; j++){
                text.append("*  ");
            }

            textView.setText(text);
            textView.setPadding(20,50,10,0);

            GridLayout gridLayout = populateGrid(noOfSquares);
            Space space = new Space(this);
            space.setMinimumWidth(displayMetrics.widthPixels/noOfSquares/noOfSquares);

            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

            guesses.add(textView);

            horizontalLayout.addView(textView);
            horizontalLayout.addView(space);
            horizontalLayout.addView(gridLayout);


            gameBoard.addView(horizontalLayout);
        }
    }

    private GridLayout populateGrid(int squares){
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(squares/2);
        gridLayout.setPadding(20,20,20,20);


        for(int j = 0; j < squares; j++){

            ImageView img = new ImageView(this);
            img.setImageResource(R.drawable.grey_box);
            // set the ImageView bounds to match the Drawable's dimensions
            img.setAdjustViewBounds(true);
            img.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            img.setPadding(5,5,5,5);

            boxes.add(img);

            gridLayout.addView(img);

        }
        return gridLayout;
    }

    private void victoryPopUp(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("You won!!");
        alertDialog.setTitle("Victory");
        alertDialog.setPositiveButton("Yay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                guessEntry.clearFocus();
            }
        });
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }

    private void endgamePopUp(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Out of turns :(\n Answer = " + gameRecord.getAnswer());
        alertDialog.setTitle("Uh oh");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                guessEntry.clearFocus();
            }
        });
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }
}