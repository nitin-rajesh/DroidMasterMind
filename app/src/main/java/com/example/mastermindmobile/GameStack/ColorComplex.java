package com.example.mastermindmobile.GameStack;

public class ColorComplex {
    public int reds;
    public int whites;
    int redScan(int[] answer, int[] currentEntry){
        reds = 0;
        for(int i = 0; i < answer.length; i++){
            if(answer[i] == currentEntry[i]){
                reds++;
            }
        }
        return reds;
    }
    int whiteScan(int[] answer, int[] currentEntry){
        whites = 0;
        boolean[] ansMarked = new boolean[answer.length];
        for (int k = 0; k < currentEntry.length; k++) {
            for (int i = 0; i < answer.length; i++) {
                if(ansMarked[i])
                    continue;
                if (answer[i] == currentEntry[k] && !ansMarked[i]) {
                    whites++;
                    ansMarked[i] = true;
                    break;
                }
            }
        }
        whites -= redScan(answer,currentEntry);
        return whites;
    }
    public ColorComplex(){
        reds = 0;
        whites = 0;
    }
    public static ColorComplex colorCount(int[] answer, int[] guess){
        ColorComplex temp = new ColorComplex();
        int red = 0;
        for(int i = 0; i < answer.length; i++){
            if(answer[i] == guess[i]){
                red++;
            }
        }
        int white = 0;
        boolean[] ansMarked = new boolean[answer.length];
        for (int k = 0; k < guess.length; k++) {
            for (int i = 0; i < answer.length; i++) {
                if(ansMarked[i])
                    continue;
                if (answer[i] == guess[k] && !ansMarked[i]) {
                    white++;
                    ansMarked[i] = true;
                    break;
                }
            }
        }
        white -= red;
        temp.reds = red;
        temp.whites = white;
        return temp;
    }
    public static boolean colorCompare(ColorComplex a, ColorComplex b){
        return (a.reds == b.reds && a.whites == b.whites);
    }
}
