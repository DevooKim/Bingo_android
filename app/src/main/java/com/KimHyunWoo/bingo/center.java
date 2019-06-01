package com.KimHyunWoo.bingo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class center extends AppCompatActivity {

    private final String TAG = "BINGO_LOG";
    private final int X = -1;
    private final int REQUEST_TO_TURN1 = 100;
    private final int REQUEST_TO_TURN2 = 200;

    Button button;

    Intent player1Intent, player2Intent;

    private int recvNumber;
    private int turn;
    private int Bingo=0; //1: player1 Win, 2: player2 Win, 3: Draw

    setMatrix player1, player2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center);

        button = (Button)findViewById(R.id.button);
        player1Intent = new Intent(this, player1.class);
        player2Intent = new Intent(this, player2.class);
        player1 = new setMatrix();
        player2 = new setMatrix();



        //todo center Flags는 FLAG_ACTIVITY_REORDER_TO_FRONT
        //todo getIntent -> resume으로


    }

    @Override
    protected void onResume() {
        super.onResume();

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

        sendData();
    }

    private void recvData(){
        turn = getIntent().getIntExtra("turn", 0);
        Bingo = getIntent().getIntExtra("isBingo", 0);
        recvNumber = getIntent().getIntExtra("num", -2);
        Log.d(TAG,"center: "+turn+"/"+recvNumber);

        if(turn == 1){
            for(int i=0; i<5; i++){
                for(int j=0; j<5; j++){
                    if(player1.nMatrix[i][j]==recvNumber){
                        player1.nMatrix[i][j] = X;
                    }
                }
            }
        }else if(turn == 2){
            for(int i=0; i<5; i++){
                for(int j=0; j<5; j++){
                    if(player2.nMatrix[i][j]==recvNumber){
                        player2.nMatrix[i][j] = X;
                    }
                }
            }
        }
    }

    private void sendData(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1 -> 2
                if(turn == 1){
                    player2Intent.putExtra("matrix", player2.nMatrix);
                    player2Intent.putExtra("num", recvNumber);
                    startActivityForResult(player2Intent,REQUEST_TO_TURN2);

                }else if(turn == 2){
                    player1Intent.putExtra("matrix", player1.nMatrix);
                    player1Intent.putExtra("num", recvNumber);
                    startActivityForResult(player1Intent,REQUEST_TO_TURN1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch(requestCode){

                case REQUEST_TO_TURN2:
                    turn = getIntent().getIntExtra("turn", 0);
                    Bingo = getIntent().getIntExtra("isBingo", 0);
                    recvNumber = getIntent().getIntExtra("num", -2);
                    Log.d(TAG,"center: "+turn+"/"+recvNumber);
                    break;

                case REQUEST_TO_TURN1:
                    turn = getIntent().getIntExtra("turn", 0);
                    Bingo = getIntent().getIntExtra("isBingo", 0);
                    recvNumber = getIntent().getIntExtra("num", -2);
                    Log.d(TAG,"center: "+turn+"/"+recvNumber);
                    break;

            }
        }
    }
}
