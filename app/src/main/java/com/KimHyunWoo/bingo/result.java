package com.KimHyunWoo.bingo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result extends AppCompatActivity {

    TextView result;
    Button home;
    Intent intent;

    int winner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        result = (TextView)findViewById(R.id.result);
        home = (Button)findViewById(R.id.home);

        intent = new Intent(this, MainActivity.class);
        winner = getIntent().getIntExtra("result",0);

        if(winner ==0){
            result.setText("Player1 Winner!!");
        }else if(winner == 1){
            result.setText("Player2 Winner!!");
        }else{
            result.setText("Draw!!");
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                finish();
            }
        });


    }


}
