package com.KimHyunWoo.bingo;

public abstract class customRunnable implements Runnable{
    int readBufferPosition;
    byte[] readBuffer;

    public customRunnable(int readBufferPosition, byte[] readBuffer){
        this.readBuffer = readBuffer;
        this.readBufferPosition = readBufferPosition;
    }
}
