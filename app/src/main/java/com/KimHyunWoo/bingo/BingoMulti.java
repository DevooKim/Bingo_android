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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class BingoMulti extends AppCompatActivity {

    TextView matrix[][] = new TextView[5][5];
    TextView state;

    private final int maxRange = 51; //0~50
    int[][] nMatrix = new int[5][5];
    String recvMessage;

    private ArrayAdapter<String> mListAdapter; //logListView에 사용

    private final String TAG = "BingoLog";
    private final int X = -1;
    protected int playerClickNumber = -1, opponentClickNumber = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bingo_matrix);

        state = (TextView)findViewById(R.id.state);

        //TextView 배열로 빙고판 구성
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

        //bluetooth//
        ListView logListView = (ListView) findViewById(R.id.logListView);
        mListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        logListView.setAdapter(mListAdapter);


        receiveData();

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
                            sendMessage(Integer.toString(playerClickNumber));
                        }
                        //todo 숫자 눌렀을때 턴 넘기기
                        //todo 턴 넘기고 클릭 막기
                    }
                });
            }
        }
    }


    //수신쓰레드 생성//
    private void receiveData(){
        int readBufferPosition = 0;
        byte[] readBuffer = new byte[1024];

        Thread thread = new Thread(new customRunnable(readBufferPosition, readBuffer) {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    int byteAvailable = 0;

                    try {
                        byteAvailable = BluetoothConnect.inputStream.available();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //데이터 수신//
                    try {
                        if (byteAvailable > 0) {
                            byte[] packetByte = new byte[byteAvailable];

                            BluetoothConnect.inputStream.read(packetByte);

                            for (int i = 0; i < byteAvailable; i++) {
                                byte b = packetByte[i];
                                if (b == '\n') {
                                    byte[] encodeByte = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodeByte, 0, encodeByte.length);
                                    recvMessage = new String(encodeByte, "UTF-8");
                                    mListAdapter.insert("상대편이 "+recvMessage+"를 선택했습니다.",0);
                                    opponentClickNumber = Integer.parseInt(recvMessage);
                                    readBufferPosition = 0;
                                    Log.d(TAG, "recvMessage: " + recvMessage);

                                }else{
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "disconnected", e);
                        e.printStackTrace();
                    }
                }

            }


        });

        //쓰레드 시작//
        thread.start();

    }

    void sendMessage(String msg){

        msg += "\n";
        try{
            BluetoothConnect.outputStram.write(msg.getBytes());
            BluetoothConnect.outputStram.flush();

        }catch (IOException e){
            Log.e(TAG, "Exception during send",e);
        }


    }

}

