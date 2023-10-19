package com.example.iot_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Deploy extends AppCompatActivity {

    private TextView DT_txt_D;
    private TextView RF_txt_D;
    private TextView NB_txt_D;
    private Button collect_button_d;
    private Button train_button_d;
    private Button deploy_button_d;

    private int predictedClass_DT;
    private int predictedClass_RF;
    private int predictedClass_NB;
    private static final String[] activity_name = {"Walking", "Jogging", "Upstairs", "Downstairs", "Sitting", "Standing"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy);

        DT_txt_D = (TextView) findViewById(R.id.DP_DT_txt_empty);
        RF_txt_D = (TextView) findViewById(R.id.DP_RF_txt_empty);
        NB_txt_D = (TextView) findViewById(R.id.DP_NB_txt_empty);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            predictedClass_DT = extras.getInt("DT_predictedClass_Deploy");
            predictedClass_RF = extras.getInt("RF_predictedClass_Deploy");
            predictedClass_NB = extras.getInt("NB_predictedClass_Deploy");

            String DT_className = activity_name[predictedClass_DT];
            String RF_className = activity_name[predictedClass_RF];
            String NB_className = activity_name[predictedClass_NB];

            DT_txt_D.setText(DT_className);
            RF_txt_D.setText(RF_className);
            NB_txt_D.setText(NB_className);
        }



        collect_button_d = (Button) findViewById(R.id.collect_DP_btn);
        train_button_d = (Button) findViewById(R.id.train_DP_btn);
        deploy_button_d = (Button) findViewById(R.id.deploy_DP_btn);

        deploy_button_d.setBackgroundColor(Color.parseColor("#D3D3D3"));

        collect_button_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Deploy.this, Collect.class);
                startActivity(intent);
            }
        } );
        train_button_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Deploy.this, Train.class);
                startActivity(intent);
            }
        } );
        deploy_button_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(Deploy.this, "Currently, The Deploy Interface", Toast.LENGTH_SHORT).show();
            }
        } );


    }
}