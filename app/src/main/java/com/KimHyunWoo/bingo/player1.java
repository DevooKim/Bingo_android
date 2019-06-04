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

    TextView matrix[][] = new TextView[5][5];
    TextView state;
    TextView who;

    private final String TAG = "BINGO_LOG";
    private final int X = -1;
    private long time= 0;

    int recvNumber;
    int clickNumber;
     setMatrix player;

     Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_matrix);

        player = (setMatrix) getIntent().getSerializableExtra("matrix");
        recvNumber = getIntent().getIntExtra("num",-2);

        intent = new Intent();
        Log.d(TAG,"player1 recv: " + recvNumber);

        state = (TextView)findViewById(R.id.state);
        state.setText("opponent Click: " +recvNumber);
        who = (TextView)findViewById(R.id.player);
        who.setText("player1");

        //TextView 배열로 빙고판 구성
        int getID;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                getID = getResources().getIdentifier("m_" + i + "_" + j, "id", this.getPackageName());
                matrix[i][j] = (TextView) findViewById(getID);
            }
        }

        //TextView로 이루어진 빙고판.
        dpMatrix();

        clickCell();
    }

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            Intent intent = new Intent(player1.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    protected void dpMatrix(){
        for(int i= 0; i<5; i++){
            for(int j= 0; j<5; j++){
                if(player.nMatrix[i][j]==X){
                    matrix[i][j].setText("X");
                    matrix[i][j].setTextColor(Color.parseColor("#ff0000"));
                }else{
                    matrix[i][j].setText(String.valueOf(player.nMatrix[i][j]));
                }
            }
        }
    }

    protected void clickCell(){
        for(int i= 0; i<5; i++){
            for(int j= 0; j<5; j++){
                matrix[i][j].setOnClickListener(new findIndexOnClickListener(i,j) {
                    @Override
                    public void onClick(View v) {
                        if(player.nMatrix[i][j] == X){
                            Toast.makeText(player1.this, "이미 입력한 번호 입니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            clickNumber = player.nMatrix[i][j];
                            Log.d(TAG,"p1 click: " + clickNumber);

                            intent.putExtra("turn",1);
                            intent.putExtra("num",clickNumber);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
            }
        }
    }

}
