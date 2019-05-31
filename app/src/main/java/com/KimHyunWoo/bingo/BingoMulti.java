package com.KimHyunWoo.bingo;

import android.app.Activity;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class BingoMulti extends AppCompatActivity {

    TextView matrix[][] = new TextView[5][5];
    private final int maxRange = 51; //0~50
    int[][] nMatrix = new int[5][5];


    private final String TAG = "BingoLog";
    private final int X = -1;
    protected int playerClickNumber = -1, opponentClickNumber = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bingo_matrix);



        int getID;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                getID = getResources().getIdentifier("m_" + i + "_" + j, "id", this.getPackageName());
                matrix[i][j] = (TextView) findViewById(getID);
            }
        }

        set_nMatrix();

        //nMatrix: 보이지 않는 숫자배열의 빙고판
        //matrix: 텍스트뷰로 구성된 빙고판. nMatrix를 실제 보여준다.
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j].setText(String.valueOf(nMatrix[i][j]));
            }
        }

        clickCell();
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


    protected void clickCell() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j].setOnClickListener(new findIndexOnClickListener(i, j) {
                    @Override
                    public void onClick(View v) {
                        if (nMatrix[i][j] == -1) {
                            Toast.makeText(BingoMulti.this, "이미 입력한 번호 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            playerClickNumber = nMatrix[i][j];
                            nMatrix[i][j] = -1;
                            matrix[i][j].setText("X");
                        }
                        //todo 숫자 눌렀을때 턴 넘기기
                        //todo 턴 넘기고 클릭 막기

                    }
                });
            }
        }
    }

    


}

