package com.example.iot_pt_4_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Deploy_PT extends AppCompatActivity {

    private TextView DT_txt_D;
    private TextView RF_txt_D;
    private TextView NB_txt_D;
    private TextView MP_txt_D;
    private Button collect_button_d;
    private Button train_button_d;
    private Button deploy_button_d;

    private int predictedClass_DT;
    private int predictedClass_RF;
    private int predictedClass_NB;
    private int predictedClass_MP;
    private static final String[] activity_name = {"Walking", "Sitting", "Standing", "Lying"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy_pt);

        DT_txt_D = (TextView) findViewById(R.id.DP_DT_txt_empty);
        RF_txt_D = (TextView) findViewById(R.id.DP_RF_txt_empty);
        NB_txt_D = (TextView) findViewById(R.id.DP_NB_txt_empty);
        MP_txt_D = (TextView) findViewById(R.id.DP_MP_txt_empty);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            predictedClass_DT = extras.getInt("PT_DT_predictedClass_Deploy");
            predictedClass_RF = extras.getInt("PT_RF_predictedClass_Deploy");
            predictedClass_NB = extras.getInt("PT_NB_predictedClass_Deploy");
            predictedClass_MP = extras.getInt("PT_MP_predictedClass_Deploy");

            String DT_className = activity_name[predictedClass_DT];
            String RF_className = activity_name[predictedClass_RF];
            String NB_className = activity_name[predictedClass_NB];
            String MP_className = activity_name[predictedClass_MP];

            DT_txt_D.setText(DT_className);
            RF_txt_D.setText(RF_className);
            NB_txt_D.setText(NB_className);
            MP_txt_D.setText(MP_className);
        }



        collect_button_d = (Button) findViewById(R.id.collect_DP_btn);
        train_button_d = (Button) findViewById(R.id.train_DP_btn);
        deploy_button_d = (Button) findViewById(R.id.deploy_DP_btn);

        deploy_button_d.setBackgroundColor(Color.parseColor("#D3D3D3"));

        collect_button_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Deploy_PT.this, Collect_PT.class);
                startActivity(intent);
            }
        } );
        train_button_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Deploy_PT.this, Train_PT.class);
                startActivity(intent);
            }
        } );
        deploy_button_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(Deploy_PT.this, "Currently, The Deploy Interface", Toast.LENGTH_SHORT).show();
            }
        } );


    }
}