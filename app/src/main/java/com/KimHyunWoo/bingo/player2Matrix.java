package com.KimHyunWoo.bingo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class player2Matrix extends AppCompatActivity {

    private final String TAG = "BINGO_LOG";
    private final int X = -1;

    TextView matrix[][] = new TextView[5][5];
    TextView state;

    setMatrix player2;

    Intent recvIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player2_matrix);

        int getID;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                getID = getResources().getIdentifier("m_" + i + "_" + j, "id", this.getPackageName());
                matrix[i][j] = (TextView) findViewById(getID);
            }
        }

    }

    //todo onRestart에서 intent 초기화

    @Override
    protected void onStart() {
        super.onStart();

        recvIntent = getIntent();
        player2 = (setMatrix) recvIntent.getSerializableExtra("matrix");

        //TextView로 이루어진 빙고판.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(player2.nMatrix[i][j] == X){
                    matrix[i][j].setText("X");
                }else{
                    matrix[i][j].setText(String.valueOf(player2.nMatrix[i][j]));
                }
            }
        }

    }
}
