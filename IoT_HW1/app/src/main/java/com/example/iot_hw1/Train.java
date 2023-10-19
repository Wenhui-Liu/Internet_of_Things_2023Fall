package com.example.iot_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Train extends AppCompatActivity {

    private TextView DT_txt_T;
    private TextView RF_txt_T;
    private TextView NB_txt_T;

    private Button collect_button_t;
    private Button train_button_t;
    private Button deploy_button_t;

    private int predictedClass_DT;
    private double confidence_DT;
    private int predictedClass_RF;
    private double confidence_RF;
    private int predictedClass_NB;
    private double confidence_NB;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        DT_txt_T = (TextView) findViewById(R.id.TP_DT_txt_empty);
        RF_txt_T = (TextView) findViewById(R.id.TP_RF_txt_empty);
        NB_txt_T = (TextView) findViewById(R.id.TP_NB_txt_empty);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            predictedClass_DT = extras.getInt("DT_predicted_class");
            confidence_DT = extras.getDouble("DT_confidence");
            predictedClass_RF = extras.getInt("RF_predicted_class");
            confidence_RF = extras.getDouble("RF_confidence");
            predictedClass_NB = extras.getInt("NB_predicted_class");
            confidence_NB = extras.getDouble("NB_confidence");

            String DT_format = String.format("%.2f", confidence_DT);
            String RF_format = String.format("%.2f", confidence_RF);
            String NB_format = String.format("%.2f", confidence_NB);

            DT_txt_T.setText(DT_format + "%");
            RF_txt_T.setText(RF_format + "%");
            NB_txt_T.setText(NB_format + "%");
        }

        collect_button_t = (Button) findViewById(R.id.collect_TP_btn);
        train_button_t = (Button) findViewById(R.id.train_TP_btn);
        deploy_button_t = (Button) findViewById(R.id.deploy_TP_btn);

        train_button_t.setBackgroundColor(Color.parseColor("#D3D3D3"));

        collect_button_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Train.this, Collect.class);
                startActivity(intent);
            }
        } );
        train_button_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(Train.this, "Currently, The Train Interface", Toast.LENGTH_SHORT).show();
            }
        } );
        deploy_button_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent Deploy_intent = new Intent(Train.this, Deploy.class);
                Deploy_intent.putExtra("DT_predictedClass_Deploy", predictedClass_DT);
                Deploy_intent.putExtra("RF_predictedClass_Deploy", predictedClass_RF);
                Deploy_intent.putExtra("NB_predictedClass_Deploy", predictedClass_NB);
                startActivity(Deploy_intent);
            }
        } );


//        if (getIntent() != null) {
//            double DT_confidence = getIntent().getDoubleExtra("DT_confidence", 0);
//            double RF_confidence = getIntent().getDoubleExtra("RF_confidence", 0);
//            double NB_confidence = getIntent().getDoubleExtra("NB_confidence", 0);
//            Log.d("DT_Result", "DT_ReceivedConfidence: " + DT_confidence);
//            Log.d("RF_Result", "RF_ReceivedConfidence: " + RF_confidence);
//            Log.d("NB_Result", "NB_ReceivedConfidence: " + NB_confidence);
//
//            DT_txt_T.setText(DT_confidence + "%");
//            RF_txt_T.setText(RF_confidence + "%");
//            NB_txt_T.setText(NB_confidence + "%");
//        }
    }
}