package com.KimHyunWoo.bingo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class player1Matrix extends AppCompatActivity {

    private final String TAG = "BINGO_LOG";
    private final int X = -1;

    TextView matrix[][] = new TextView[5][5];
    TextView state;

    setMatrix player1;
    int player1Number;
    int recvNumber = -2;

    Intent player2Intent;
    Intent recvIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_matrix);
        Log.d(TAG, "Bingo Start");

        state = (TextView)findViewById(R.id.state);
        recvIntent = getIntent();

        player2Intent = new Intent(player1Matrix.this, player2.class);
        player2Intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //스택에 호출하는 액티비티가 존재하는 경우 스택 최상단으로 호출
        // player1 <-> player2 전환
        //todo 중간 액티비티로 턴 연결
        //todo 중간 액티비티에서 핸들러 + 결과 + 최종 빙고판 보여주기
        //todo 빙고 완성시 중간액티비티에서 player1,2 종료
        //https://koreamin.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%ED%98%84%EC%9E%AC-%EC%95%A1%ED%8B%B0%EB%B9%84%ED%8B%B0-%EB%8B%A4%EB%A5%B8-%EC%95%A1%ED%8B%B0%EB%B9%84%ED%8B%B0-%EC%A2%85%EB%A3%8C%ED%95%98%EA%B8%B0


       //TextView 배열로 빙고판 구성
        int getID;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                getID = getResources().getIdentifier("m_" + i + "_" + j, "id", this.getPackageName());
                matrix[i][j] = (TextView) findViewById(getID);
            }
        }

        //빙고판 생성//
        //plyer1,2는 숫자배열로 구성되고 onRestart()를 시작하며 TextView로 보여진다.
        player1 = new setMatrix();

        //TextView로 이루어진 빙고판.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                    matrix[i][j].setText(String.valueOf(player1.nMatrix[i][j]));
            }
        }

    }

    //액티비티 A <-> B 전환하면서 반복실행
    @Override
    protected void onStart() {
        super.onStart();


//        recvNumber = recvIntent.getIntExtra("number",-2);
//        Log.d(TAG, "player1 recv: "+recvNumber);

        //TextView로 이루어진 빙고판.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(player1.nMatrix[i][j] == recvNumber){
                    matrix[i][j].setText("X");
                    //todo 최초변경 컬러변경 추가
                }
            }
        }

        myTurn();

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d(TAG,"player1 - onPause");
//        recvIntent.removeExtra("number");
//
//    }

    protected void myTurn(){
        Log.d(TAG,"player1 start");
        for(int i =0; i<5; i++){
            for(int j= 0; j<5; j++){
                matrix[i][j].setOnClickListener(new findIndexOnClickListener(i,j) {
                    @Override
                    public void onClick(View v) {
                        if (player1.nMatrix[i][j] == X) {
                            Toast.makeText(player1Matrix.this, "이미 입력한 번호 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            player1Number = player1.nMatrix[i][j];
                            player1.nMatrix[i][j] = X;
                            matrix[i][j].setText("X");
                            Log.d(TAG, "player1 Click: " + player1Number);

                            //intent//
                            player2Intent.putExtra("number", player1Number);
                            //startActivity(player2Intent);

                            startActivityForResult(player2Intent,100);
                        }
                    }
                });
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            if(resultCode == RESULT_OK){
                recvNumber = data.getIntExtra("number",-2);
                Log.d(TAG, "player1 recv: "+recvNumber);
            }
        }
    }
}
