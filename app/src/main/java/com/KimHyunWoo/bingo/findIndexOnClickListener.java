package com.KimHyunWoo.bingo;

import android.view.View;

public abstract class findIndexOnClickListener implements View.OnClickListener{

    protected int i;
    protected int j;

    public findIndexOnClickListener(int i, int j){
        this.i=i;
        this.j=j;
    }
}
