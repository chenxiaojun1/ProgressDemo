package com.progressdemo;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private TestAnimProgressView aapv;

    private int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aapv = findViewById(R.id.aapv);

        Button button = findViewById(R.id.bt);

        Button button1 = findViewById(R.id.bt1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i+=5;
                aapv.animProgress(i);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i-=5;
                aapv.animProgress(i);
            }
        });
    }
}
