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

import java.util.Random;

public class player2Matrix extends AppCompatActivity {

    private final String TAG = "BINGO_LOG";
    private final int REQUEST_CHANGE_TURN = 100;


    TextView matrix[][] = new TextView[5][5];
    TextView state;

    private final int maxRange = 51; //0~50
    private final int X = -1;
    private int playerClickNumber = -1, opponentClickNumber = -1;
    int[][] nMatrix = new int[5][5];

    Intent player2Intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player2_matrix);

        state = (TextView)findViewById(R.id.state);
        Log.d(TAG,"player2 - onCreate");


        //TextView 배열로 빙고판 구성
        int getID;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                getID = getResources().getIdentifier("m_" + i + "_" + j, "id", this.getPackageName());
                matrix[i][j] = (TextView) findViewById(getID);
            }
        }

        //nMatrix: 보이지 않는 숫자배열의 빙고판
        //matrix: 텍스트뷰로 구성된 빙고판. nMatrix를 실제 보여준다.
        set_nMatrix();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j].setText(String.valueOf(nMatrix[i][j]));
            }
        }

        playerTrun();

    }

    private void set_nMatrix() {
        int num;
        Random r = new Random();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                num = r.nextInt(maxRange);
                if (duplicationCheck(nMatrix, num)) {
                    nMatrix[i][j] = num;
                } else {
                    j--;
                }
            }
        }
    }

    //빙고판 중복 검사
    protected boolean duplicationCheck(int nMatrix[][], int checkNum) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (nMatrix[i][j] == checkNum) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void playerTrun(){
        for(int i =0; i<5; i++){
            for(int j= 0; j<5; j++){
                matrix[i][j].setOnClickListener(new findIndexOnClickListener(i,j) {
                    @Override
                    public void onClick(View v) {
                        if(nMatrix[i][j] == -1){
                            Toast.makeText(player2Matrix.this, "이미 입력한 번호 입니다.", Toast.LENGTH_SHORT).show();
                        }else {
                            playerClickNumber = nMatrix[i][j];
                            nMatrix[i][j] = -1;
                            matrix[i][j].setText("X");
                            Intent intent = new Intent(player2Matrix.this, player1Matrix.class);
                            intent.putExtra("number", playerClickNumber);
                            //startActivity(intent);
                            //setResult(RESULT_OK, intent);
                            //finish();
                            startActivityForResult(intent, REQUEST_CHANGE_TURN);
                        }


                    }
                });
            }
        }
    }

    //player1 -> player2
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(TAG,"player2 - onResume");
//        recvTurn();
//
//    }

    //턴이 돌아왔을 때 상대가 클릭한 숫자가 내 빙고판에 있을경우 체크
    private void recvTurn(){
        Intent intent = getIntent();
        intent.getIntExtra("number", opponentClickNumber);
        state.setText("player1: " + opponentClickNumber);

        for(int i= 0; i<5; i++){
            for(int j= 0; j<5; j++){
                matrix[i][j].setTextColor(Color.parseColor("#000000"));
                if(nMatrix[i][j] == opponentClickNumber){
                    matrix[i][j].setText("X");
                    matrix[i][j].setTextColor(Color.parseColor("#FF0000"));
                    nMatrix[i][j] = X;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Player2 - onActivityResult");
        switch(requestCode){
            case REQUEST_CHANGE_TURN:
                if(requestCode == RESULT_OK){
                    data.getIntExtra("number", opponentClickNumber);
                    state.setText("player1: " + opponentClickNumber);
                    //턴이 돌아왔을 때 상대가 클릭한 숫자가 내 빙고판에 있을경우 체크
                    for(int i= 0; i<5; i++){
                        for(int j= 0; j<5; j++){
                            matrix[i][j].setTextColor(Color.parseColor("#000000"));
                            if(nMatrix[i][j] ==opponentClickNumber){
                                matrix[i][j].setText("X");
                                matrix[i][j].setTextColor(Color.parseColor("#FF0000"));
                                nMatrix[i][j] = X;
                            }
                        }
                    }

                }
                break;
        }
    }

}
