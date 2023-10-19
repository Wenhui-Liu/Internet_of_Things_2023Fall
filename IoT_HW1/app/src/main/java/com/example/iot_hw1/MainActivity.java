package com.example.iot_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button collect_button_main;
    private Button train_button_main;
    private Button deploy_button_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        collect_button_main = (Button) findViewById(R.id.collect_main_btn);
        train_button_main = (Button) findViewById(R.id.train_main_btn);
        deploy_button_main = (Button) findViewById(R.id.deploy_main_btn);

        collect_button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Collect.class);
                startActivity(intent);
            }
        } );
        train_button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Train.class);
                startActivity(intent);
            }
        } );
        deploy_button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Deploy.class);
                startActivity(intent);
            }
        } );

    }
}