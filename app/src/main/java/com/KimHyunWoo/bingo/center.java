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
    Button button;

    Intent player1Intent, player2Intent;

    private int recvNumber;
    private int turn;
    private boolean Bingo;

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

        turn = getIntent().getIntExtra("turn", 0);
        Bingo = getIntent().getBooleanExtra("isBingo", false);
        recvNumber = getIntent().getIntExtra("num", -2);
        Log.d(TAG,"center: "+turn+"/"+recvNumber);

        //todo center Flags는 FLAG_ACTIVITY_REORDER_TO_FRONT
        //todo getIntent -> resume으로

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1 -> 2
                if(turn == 1){
                    player2Intent.putExtra("num", recvNumber);
                    player2Intent.putExtra("isBingo", Bingo);
                    startActivity(player2Intent);
                    finish();
                }else if(turn == 2){
                    player1Intent.putExtra("num", recvNumber);
                    startActivity(player1Intent);
                    finish();
                }
            }
        });
    }
}
