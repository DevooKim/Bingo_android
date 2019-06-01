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

public class player1Matrix extends AppCompatActivity {

    private final String TAG = "BINGO_LOG";
    private final int REQUEST_CHANGE_TURN = 100;


    TextView matrix[][] = new TextView[5][5];
    TextView state;

    private final int maxRange = 26; //0~50
    private final int X = -1;
    private int playerClickNumber = -1, opponentClickNumber = -1;
    int[][] nMatrix = new int[5][5];
    
    Intent player2Intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player1_matrix);

        state = (TextView)findViewById(R.id.state);
        player2Intent = new Intent(player1Matrix.this,player2Matrix.class);
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
                            Toast.makeText(player1Matrix.this, "이미 입력한 번호 입니다.", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.d(TAG, i +"/"+ j);
                            playerClickNumber = nMatrix[i][j];
                            nMatrix[i][j] = -1;
                            matrix[i][j].setText("X");
                            Log.d(TAG,"player1-Clcik: " + playerClickNumber);
                            //getIntent().getExtras().clear();
//                            player2Intent.getExtras().clear();
                            player2Intent.putExtra("number", playerClickNumber);
                            //startActivityForResult(player2Intent, REQUEST_CHANGE_TURN);
                            player2Intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                            startActivity(player2Intent);
                        }

                        
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Player1 - onActivityResult");
        switch(requestCode){
            case REQUEST_CHANGE_TURN:
                if(requestCode == RESULT_OK){
                    data.getIntExtra("number", opponentClickNumber);
                    state.setText("player2: " + opponentClickNumber);
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

    //player2 -> player1
    @Override
    protected void onStart() {
        super.onStart();
        playerTrun();
        Log.d(TAG,"player1 - onStart");
        recvTurn();

    }

    //턴이 돌아왔을 때 상대가 클릭한 숫자가 내 빙고판에 있을경우 체크
    private void recvTurn(){
        Intent intent = getIntent();
        opponentClickNumber=intent.getIntExtra("number1",0);
        state.setText("player2: " + opponentClickNumber);
        Log.d(TAG,"player1 - recv: "+ opponentClickNumber);


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
}
