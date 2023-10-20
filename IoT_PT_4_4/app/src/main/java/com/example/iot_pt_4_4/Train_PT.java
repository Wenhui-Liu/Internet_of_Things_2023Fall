package com.example.iot_pt_4_4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Train_PT extends AppCompatActivity {

    private TextView DT_txt_T;
    private TextView RF_txt_T;
    private TextView NB_txt_T;
    private TextView MP_txt_T;

    private Button collect_button_t;
    private Button train_button_t;
    private Button deploy_button_t;

    private int predictedClass_DT;
    private double confidence_DT;
    private int predictedClass_RF;
    private double confidence_RF;
    private int predictedClass_NB;
    private double confidence_NB;
    private int predictedClass_MP;
    private double confidence_MP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_pt);

        DT_txt_T = (TextView) findViewById(R.id.TP_DT_txt_empty);
        RF_txt_T = (TextView) findViewById(R.id.TP_RF_txt_empty);
        NB_txt_T = (TextView) findViewById(R.id.TP_NB_txt_empty);
        MP_txt_T = (TextView) findViewById(R.id.TP_MP_txt_empty);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            predictedClass_DT = extras.getInt("PT_DT_predicted_class");
            confidence_DT = extras.getDouble("PT_DT_confidence");
            predictedClass_RF = extras.getInt("PT_RF_predicted_class");
            confidence_RF = extras.getDouble("PT_RF_confidence");
            predictedClass_NB = extras.getInt("PT_NB_predicted_class");
            confidence_NB = extras.getDouble("PT_NB_confidence");
            predictedClass_MP = extras.getInt("PT_MP_predicted_class");
            confidence_MP = extras.getDouble("PT_MP_confidence");

            String DT_format = String.format("%.2f", confidence_DT);
            String RF_format = String.format("%.2f", confidence_RF);
            String NB_format = String.format("%.2f", confidence_NB);
            String MP_format = String.format("%.2f", confidence_MP);

            DT_txt_T.setText(DT_format + "%");
            RF_txt_T.setText(RF_format + "%");
            NB_txt_T.setText(NB_format + "%");
            MP_txt_T.setText(MP_format + "%");
        }

        collect_button_t = (Button) findViewById(R.id.collect_TP_btn);
        train_button_t = (Button) findViewById(R.id.train_TP_btn);
        deploy_button_t = (Button) findViewById(R.id.deploy_TP_btn);

        train_button_t.setBackgroundColor(Color.parseColor("#D3D3D3"));

        collect_button_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Train_PT.this, Collect_PT.class);
                startActivity(intent);
            }
        } );
        train_button_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(Train_PT.this, "Currently, The Train Interface", Toast.LENGTH_SHORT).show();
            }
        } );
        deploy_button_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent PT_Deploy_intent = new Intent(Train_PT.this, Deploy_PT.class);
                PT_Deploy_intent.putExtra("PT_DT_predictedClass_Deploy", predictedClass_DT);
                PT_Deploy_intent.putExtra("PT_RF_predictedClass_Deploy", predictedClass_RF);
                PT_Deploy_intent.putExtra("PT_NB_predictedClass_Deploy", predictedClass_NB);
                PT_Deploy_intent.putExtra("PT_MP_predictedClass_Deploy", predictedClass_MP);
                startActivity(PT_Deploy_intent);
            }
        } );
    }
}