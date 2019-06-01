package com.KimHyunWoo.bingo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class player1 extends AppCompatActivity {
    private final String TAG = "BINGO_LOG";
    private final int X = -1;
    private final int turn = 1;
    Intent intent;

    TextView matrix[][] = new TextView[5][5];
    TextView state;

    setMatrix player;

    int clickNumber;
    int recvNumber;
    boolean Bingo = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player1_matrix);
        Log.d(TAG,"player1 create");
        intent = new Intent(this, center.class);

        //TextView 배열로 빙고판 구성
        int getID;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                getID = getResources().getIdentifier("m_" + i + "_" + j, "id", this.getPackageName());
                matrix[i][j] = (TextView) findViewById(getID);
            }
        }

        player = new setMatrix();

        //TextView로 이루어진 빙고판.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j].setText(String.valueOf(player.nMatrix[i][j]));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        recvTurn();
        myTurn();

    }

    @Override
    protected void onPause() {
        super.onPause();
        getIntent().getExtras().clear();
    }

    protected void recvTurn(){
        recvNumber = getIntent().getIntExtra("num", -2);
        Log.d(TAG,"player1-recv: " + recvNumber);

        for(int i= 0; i<5; i++){
            for(int j= 0; j<5; j++){
                matrix[i][j].setTextColor(Color.parseColor("#000000"));
                if(player.nMatrix[i][j] == recvNumber){
                    player.nMatrix[i][j] = X;
                    matrix[i][j].setText("X");
                    matrix[i][j].setTextColor(Color.parseColor("#FF0000"));
                }
            }
        }
    }

    protected void myTurn(){
        Log.d(TAG,"player1-Turn");
        for(int i =0; i<5; i++){
            for(int j= 0; j<5; j++){
                matrix[i][j].setOnClickListener(new findIndexOnClickListener(i,j) {
                    @Override
                    public void onClick(View v) {
                        if (player.nMatrix[i][j] == X) {
                            Toast.makeText(player1.this, "이미 입력한 번호 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            clickNumber = player.nMatrix[i][j];
                            player.nMatrix[i][j] = X;
                            matrix[i][j].setText("X");
                            Log.d(TAG, "player1-Click: " + clickNumber);

                            Bingo = isBingo();

                            intent.putExtra("turn",turn);
                            intent.putExtra("inBingo",Bingo);
                            intent.putExtra("num",clickNumber);

                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    protected boolean isBingo(){

        return false;
    }
}
