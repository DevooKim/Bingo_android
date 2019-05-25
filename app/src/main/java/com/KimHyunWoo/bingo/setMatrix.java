package com.KimHyunWoo.bingo;

import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class setMatrix {

    private final int max = 51; //0~50
    int [][] nMatrix = new int[5][5];

    setMatrix(){
        int num;
        Random r = new Random();

        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                num = r.nextInt(max);
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
