package com.example.mastermindmobile.GameStack;
import java.lang.Math;
import java.util.Arrays;

public class GameRecord extends ColorComplex{
    ColorComplex c = new ColorComplex();
    int currentTurn = 0;
    int[] currentEntry;
    protected int[] answer;
    public final int numberOfColumns;
    public final int numberOfColors;
    public final int numberOfGuesses;
    int iterator = 0;
    public boolean isScam;
    public boolean victory;
    public boolean endOfGame;
    public final boolean isRepeat;
    public int[][] board;
    public GameRecord(int variableCount, int constantCount, boolean isRepeat){
        this.numberOfColumns = variableCount;
        this.numberOfColors = constantCount;
        this.isRepeat = isRepeat;
        this.numberOfGuesses = 2*numberOfColumns + (numberOfColors - 8)*2 + (isRepeat?numberOfColors:0);
        initBoard(numberOfGuesses);
        generateAnswer(variableCount,constantCount,isRepeat);
        victory = false;
        isScam = false;
        endOfGame = false;
    }
    public GameRecord(GameRecord instance){
        this.isRepeat = instance.isRepeat;
        this.numberOfColors = instance.numberOfColors;
        this.numberOfColumns = instance.numberOfColumns;
        this.numberOfGuesses = instance.numberOfGuesses;
        this.answer = new int[instance.numberOfColumns];
        System.arraycopy(instance.answer,0, this.answer,0,answer.length);
        initBoard(numberOfGuesses);
        currentEntry = new int[numberOfColumns];
        victory = false;
        isScam = false;
    }
    private void generateAnswer(int variableCount, int constantCount, boolean isRepeat){
        answer = new int[variableCount];
        currentEntry = new int[variableCount];
        for(int i = 0; i < variableCount; i++){
            int digit;
            digit = (int)Math.floor(Math.random()*constantCount + 1);
            answer[i] = digit;
            for(int j = 0; j < i; j++){
                if(digit == answer[j]){
                    if(!isRepeat){
                        i--;
                    }
                }
            }
        }
    }
    public int countReds(){
        c.reds = c.redScan(answer,currentEntry);
        if(c.redScan(answer,currentEntry) == answer.length)
            endOfGame = victory = true;

        if(currentTurn == numberOfGuesses - 1)
            endOfGame = true;

        return c.reds;
    }
    public int countWhites(){

        return c.whiteScan(answer,currentEntry);
    }

    public int countReds(int row){
        c.reds = c.redScan(answer,board[row]);
        if(c.redScan(answer,currentEntry) == answer.length)
            victory = true;
        return c.reds;
    }

    public int countWhites(int row){
        return c.whiteScan(answer,board[row]);
    }

    public void appendEntry(int currentVal){
        if(iterator == currentEntry.length){
            iterator = 0;
            currentTurn++;
        }
        board[currentTurn][iterator] = currentVal;
        currentEntry[iterator] = currentVal;
        iterator++;
    }
    void resetEntry(){
        victory = false;
        isScam = false;
        iterator = 0;
        currentTurn = 0;
        initBoard(numberOfGuesses);
    }
    public void appendGuess(int[] guess){
        for(int val: guess){
            appendEntry(val);
        }
        this.reds = redScan(answer, guess);
        this.whites = whiteScan(answer,guess);
    }
    public int getCurrentTurn(){
        return currentTurn;
    }

    public void initBoard(int numberOfGuesses){
        board = new int[numberOfGuesses][numberOfColumns];
    }

    public String getAnswer(){
        return Arrays.toString(answer);
    }
}
