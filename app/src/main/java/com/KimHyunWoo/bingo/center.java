package com.KimHyunWoo.bingo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class center extends AppCompatActivity {

    private final String TAG = "BINGO_LOG";
    private final int X = -1;
    private final int REQUEST_TO_TURN1 = 100;
    private final int REQUEST_TO_TURN2 = 200;

    Button change, result;

    Intent player1Intent, player2Intent, resultIntent;

    TextView state;

    boolean isInit = true;
    private int recvNumber=-2;
    private int turn=1;
    private boolean p1Bingo=false, p2Bingo=false;

    setMatrix player1, player2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center);

        state = (TextView)findViewById(R.id.state);

        change = (Button)findViewById(R.id.button);
        result = (Button)findViewById(R.id.result);
        player1Intent = new Intent(this, player1.class);
        player2Intent = new Intent(this, player2.class);
        player1 = new setMatrix();
        player2 = new setMatrix();
        Log.d(TAG,"Setting OK..");

        resultIntent = new Intent(this, result.class);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInit){
                    Log.d(TAG,"Bingo Start!!");
                    change.setText("change Turn");
                    player1Intent.putExtra("matrix", player1);
                    isInit = false;
                    startActivityForResult(player1Intent,REQUEST_TO_TURN2);
                }else{
                    sendData();
                }
            }
        });

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(resultIntent);
                finish();
            }
        });
    }

private void markMatrix(){
    for(int i=0; i<5; i++){
        for(int j=0; j<5; j++){
            if(player1.nMatrix[i][j]==recvNumber){
                player1.nMatrix[i][j] = X;
            }
            if(player2.nMatrix[i][j]==recvNumber){
                player2.nMatrix[i][j] = X;
            }
        }
    }
}

    private void recvData(Intent data){
        turn = data.getIntExtra("turn", 0);
        recvNumber = data.getIntExtra("num", -2);
        if(turn == 1){ state.setText("player1 -> player2");}
        else{ state.setText("player2 -> player1");}

        for(int i=0; i<5; i++) {
            for (int j = 0; j < 5; j++) {
                if (player1.nMatrix[i][j] == recvNumber) {
                    player1.nMatrix[i][j] = X;
                    break;
                }
            }
        }
            for(int i=0; i<5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (player2.nMatrix[i][j] == recvNumber) {
                        player2.nMatrix[i][j] = X;
                        break;
                    }
                }
            }
    }

    private void sendData(){
                // 1 -> 2
                if(turn == 1){
                    Log.d(TAG,"Turn Change From 1 to 2");

                    player2Intent.putExtra("matrix", player2);
                    player2Intent.putExtra("num", recvNumber);
                    startActivityForResult(player2Intent,REQUEST_TO_TURN2);

                }else if(turn == 2){
                    Log.d(TAG,"Turn Change From 2 to 1");
                    state.setText("player1 -> player2");
                    player1Intent.putExtra("matrix", player1);
                    player1Intent.putExtra("num", recvNumber);
                    startActivityForResult(player1Intent,REQUEST_TO_TURN2);
                }
    }

    protected boolean bingoCount(setMatrix player){
        int dia1=0, dia2=0, wid=0, col=0;
        int count=0;

        //좌상 -> 우하 대각선 체크
        for(int i=0; i<5; i++){
            if(player.nMatrix[i][i] == X){ dia1++; }
        }
        if(dia1 == 5) { count++;}

        //좌하 -> 우상 대각선 체크
        for(int i=0; i<5; i++){
            if(player.nMatrix[4-i][i] == X){ dia2++; }
        }
        if(dia2 == 5) { count++;}

        //가로 체크
        for(int i=0; i<5; i++){
            for(int n : player.nMatrix[i]){
                if(n == X){ wid++; }
            }
            if(wid == 5) { count++;}
            wid=0;
        }

        //세로 체크
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if(player.nMatrix[j][i] == X){ col++; }
            }
            if(col == 5){ count ++;}
            col=0;
        }

        Log.d(TAG,"Bingo Count p1 and p2: " + count);
        return (count >= 5)? true:false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQUEST_TO_TURN2:
                    Log.d(TAG, "return center");
                    recvData(data);
                    markMatrix();
                    Log.d(TAG, "center recv: " + recvNumber);

                    p1Bingo = bingoCount(player1);
                    p2Bingo = bingoCount(player2);
                    endGame();
                    break;
            }
        }
    }

    protected void endGame(){
        if(turn == 2) {
            if (p1Bingo == true) {
                //p1 Win
                resultIntent.putExtra("result", 0);
                result.setVisibility(View.VISIBLE);
                result.setClickable(true);
                change.setVisibility(View.INVISIBLE);
                change.setClickable(false);

            } else if (p2Bingo == true) {
                //p2 Win
                resultIntent.putExtra("result", 1);
                result.setVisibility(View.VISIBLE);
                result.setClickable(true);
                change.setVisibility(View.INVISIBLE);
                change.setClickable(false);
            } else if (p1Bingo == true && p2Bingo == true) {
                //Draw
                resultIntent.putExtra("result", 2);
                result.setVisibility(View.VISIBLE);
                result.setClickable(true);
                change.setVisibility(View.INVISIBLE);
                change.setClickable(false);
            }
        }
    }

}
