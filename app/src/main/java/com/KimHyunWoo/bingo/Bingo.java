package com.KimHyunWoo.bingo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Bingo extends AppCompatActivity {

    TextView matrix[][] = new TextView[5][5];
    TextView state;

    setMatrix player, computer;
    private int playerClickNumber= -1, computerClickNumber= -1;
    private final int X = -1;

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

        /*컴퓨터와 플레이어 빙고판 선언*/
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
                        if(player.nMatrix[i][j] == -1){
                            Toast.makeText(Bingo.this, "이미 입력한 번호 입니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            playerClickNumber = player.nMatrix[i][j];
                            player.nMatrix[i][j] = -1;
                            matrix[i][j].setText("X");
                        }
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
                    player.nMatrix[i][j] = X;
                    matrix[i][j].setText("X");
                }
                if(computer.nMatrix[i][j] == computerClickNumber){
                    computer.nMatrix[i][j] = X;
                }
            }
        }
    }


}
