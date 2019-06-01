package com.KimHyunWoo.bingo;

import java.io.Serializable;
import java.util.Random;

public class setMatrix implements Serializable {

    private final int maxRange = 51; //0~50
    int [][] nMatrix = new int[5][5];

    private int ClickNumber;

    private final int X = -1;

    public void setClickNumber(int n){
        this.ClickNumber = n;
    }
    public int getClickNumber(){
        return ClickNumber;
    }

    setMatrix(){
        int num;
        Random r = new Random();

        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                num = r.nextInt(maxRange);
                if(check(nMatrix, num)){
                    nMatrix[i][j]=num;
                }
                else{ j--;}
            }
        }
    }

    protected boolean check(int nMatrix[][], int checkNum){
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if(nMatrix[i][j]==checkNum){
                    return false;
                }
            }
        }
        return true;
    }


}
