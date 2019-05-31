package com.KimHyunWoo.bingo;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;

public class BluetoothConnect extends AppCompatActivity{

    private static final int REQUEST_ENABLE_BT = 100;
    private static final String TAG = "BINGO";

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private Set<BluetoothDevice> devices;
    private BluetoothSocket bluetoothSocket;
    public static InputStream inputStream;
    public static OutputStream outputStram;

    private String pairedDeviceName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_connect);

        Button button = (Button)findViewById(R.id.connect);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkBluetooth();
            }
        });
    }

    protected void checkBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 디바이스 입니다.", Toast.LENGTH_LONG).show();
            finish();
        }else{
            if(bluetoothAdapter.isEnabled()){
                pairingDevice();
            }else{
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case REQUEST_ENABLE_BT:
                if(requestCode == RESULT_OK){
                    pairingDevice();
                }else if(requestCode == RESULT_CANCELED){
                    Toast.makeText(getApplicationContext(), "블루투스를 활성화 하지 않아 종료합니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    public void pairingDevice(){
        devices = bluetoothAdapter.getBondedDevices();
        final BluetoothDevice[] pairedDevices = devices.toArray(new BluetoothDevice[0]);

        if(pairedDevices.length == 0){
            showQuitDialog("페어링된 디바이스가 없습니다.");
            return;
        }

        String[] items;
        items = new String[pairedDevices.length];
        for(int i = 0; i<pairedDevices.length; i++){
            items[i] = pairedDevices[i].getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select device");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                ConnectTask connectTask = new ConnectTask(pairedDevices[which]);
                connectTask.execute();
                //todo task실행  매개변수: pairedDevices[which]
            }
       });
        builder.create().show();
    }

    public void showQuitDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    public void createSocket(BluetoothDevice mBluetoothDevice){
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try{
            bluetoothSocket = mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();

            inputStream = bluetoothSocket.getInputStream();
            outputStram = bluetoothSocket.getOutputStream();

            Log.d(TAG, "create Socket " + pairedDeviceName);
        }catch (IOException e){
            Log.e(TAG, "Socket not connected");
        }

    }
    private class ConnectTask extends AsyncTask<Void, Void, Void>{

        private BluetoothSocket mBluetoothSocket = null;
        private BluetoothDevice mBluetoothDevice = null;

        ConnectTask(BluetoothDevice bluetoothDevice){
            mBluetoothDevice = bluetoothDevice;
            pairedDeviceName = bluetoothDevice.getName();
        }
        @Override
        protected Void doInBackground(Void... params){
            createSocket(mBluetoothDevice);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
                try{
                    mBluetoothSocket.close();
                    Log.d(TAG,"close socket");
                }catch(IOException e){
                    Log.e(TAG,"unable close socket");
                }
        }
    }

    //todo Multi에서 송수신쓰레드를 만들어 메시지 송수신 수행. 혹은 새로운 클래스를 이용(기존방법).

}

