package com.KimHyunWoo.bingo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BingoSingle extends AppCompatActivity {

    TextView matrix[][] = new TextView[5][5];
    TextView state;


    EditText comTest;
    Button button;

    setMatrix player, computer;
    private int playerClickNumber= -1, computerClickNumber= -1;
    private final int X = -1;
    private final String TAG = "BingoLog";
    protected boolean firstTurn= true;
    private String turn = "player";

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

        /*테스트*/
        for(int i=0; i<5; i++){
            Log.d(TAG,"["+Integer.toString(computer.nMatrix[i][0])+"]"
                    +"["+Integer.toString(computer.nMatrix[i][1])+"]"
                    +"["+Integer.toString(computer.nMatrix[i][2])+"]"
                    +"["+Integer.toString(computer.nMatrix[i][3])+"]"
                    +"["+Integer.toString(computer.nMatrix[i][4])+"]");
        }

        comTest = (EditText)findViewById(R.id.com_test);

//        switch(turn){
//                case "player":
//                    clickCell();
//                    break;
//
//                case "computer":
//                    computerTurn();
//                    break;
//        }

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                computerTurn();
                //버튼클릭은 만들고 대기중 이기 때문에 ? => 랜덤은..?
            }
        });
    }



    protected void clickCell(){
        for(int i =0; i<5; i++){
            for(int j = 0; j<5; j++){
                matrix[i][j].setOnClickListener(new findIndexOnClickListener(i,j) {
                    @Override
                    public void onClick(View v) {
                        if(player.nMatrix[i][j] == -1){
                            Toast.makeText(BingoSingle.this, "이미 입력한 번호 입니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            playerClickNumber = player.nMatrix[i][j];
                            player.nMatrix[i][j] = -1;
                            matrix[i][j].setText("X");
                            turn = "computer";
                        }
                        //todo 숫자 눌렀을때 턴 넘기기

                    }
                });
            }
        }
    }


    private void computerTurn(){

        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                if(computer.nMatrix[i][j] == playerClickNumber){
                    computer.nMatrix[i][j] =-1;
                    break;
                }
            }
        }
        int a = computer.Turn();
        Toast.makeText(this,Integer.toString(a),Toast.LENGTH_LONG).show();

        computerClickNumber = computer.getComputerClickNumber();
        state.setText(Integer.toString(computerClickNumber));
        turn = "player";
    }

}
