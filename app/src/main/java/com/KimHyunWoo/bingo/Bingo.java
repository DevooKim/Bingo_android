package com.KimHyunWoo.bingo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class Bingo extends AppCompatActivity {

    TextView matrix[][] = new TextView[5][5];
    TextView state;

    setMatrix player, computer;
    private int playerClickNumber= -1, computerClickNumber= -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bingo_matrix);

        state = (TextView)findViewById(R.id.state);
        int getID;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                getID = getResources().getIdentifier("m_" + i + "_" + j, "id", this.getPackageName());
                matrix[i][j] = (TextView) findViewById(getID);
            }
        }

        computer = new setMatrix();
        player = new setMatrix();
        for(int i = 0; i<5; i++){
            for(int j =0; j<5; j++){
                matrix[i][j].setText(String.valueOf(player.nMatrix[i][j]));
            }
        }
        clickCell();

    }

    protected void clickCell(){
        for(int i =0; i<5; i++){
            for(int j = 0; j<5; j++){
                matrix[i][j].setOnClickListener(new findIndexOnClickListener(i,j) {
                    @Override
                    public void onClick(View v) {
                        playerClickNumber = Integer.parseInt(matrix[i][j].getText().toString());
                        matrix[i][j].setText("X");
                        //todo X일때 토스트
                        //todo 숫자 눌렀을때 턴 넘기기
                    }
                });
            }
        }
    }

    void marking(){
        for(int i = 0; i<5; i++){
            for(int j = 0; j<5; j++){
                if(player.nMatrix[i][j] == playerClickNumber){
                    player.nMatrix[i][j] = -1;
                    matrix[i][j].setText("X");
                }
                if(computer.nMatrix[i][j] == computerClickNumber){
                    computer.nMatrix[i][j] = -1;
                }
            }
        }
    }

    private void computerTurn(){

        /* 마킹 갯수*/
        int dia1=0, dia2=0; //dia1: 오른쪽상단 to 왼쪽하단 대각선, dia2: 왼쪽상단 to 오른쪽하단 대각선
        int[] wid = new int[5];
        int[] hei = new int[5];

        //가운데 우선//
        if(computer.nMatrix[2][2] != -1){
            computer.nMatrix[2][2] = computerClickNumber;
            computer.nMatrix[2][2] = -1;
        }else{
            for(int i =0; i<5; i++){
                for(int j= 0; j<5; j++){
                    
                }
            }
        }



    }

}
