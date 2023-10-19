package com.example.iot_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.AssetManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.classifiers.meta.FilteredClassifier;
import static weka.core.SerializationHelper.read;

public class Collect extends AppCompatActivity implements SensorEventListener {

    private Spinner spinner_c;
    private Button collect_button_c;
    private Button train_button_c;
    private Button deploy_button_c;
    private Button start_button_c;
    private Button end_button_c;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private List<Float> magnitudeList = new ArrayList<>();
    private Handler handler = new Handler();
    private boolean isCollecting = false;
    private float min;
    private float max;
    private float mean;
    private float var;
    private float std;
    private float zcr;
    private Classifier cls_DT = null;
    private Classifier cls_RF = null;
    private Classifier cls_NB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);


        spinner_c = (Spinner) findViewById(R.id.spinner_CP);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.CP_label_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_c.setAdapter(adapter);

        //collect, train, deploy button functions
        collect_button_c = (Button) findViewById(R.id.collect_CP_btn);
        train_button_c = (Button) findViewById(R.id.train_CP_btn);
        deploy_button_c = (Button) findViewById(R.id.deploy_CP_btn);

        collect_button_c.setBackgroundColor(Color.parseColor("#D3D3D3"));

        collect_button_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(Collect.this, "Currently, The Collect Interface", Toast.LENGTH_SHORT).show();
            }
        } );
        train_button_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                Intent intent = new Intent(Collect.this, Train.class);
//                startActivity(intent);

                double[] results_DT = classification(cls_DT, min, max, std, zcr, mean, var);
                int predictedClass_DT = (int) results_DT[0];
                double confidence_DT = results_DT[1];

                double[] results_RF = classification(cls_RF, min, max, std, zcr, mean, var);
                int predictedClass_RF = (int) results_RF[0];
                double confidence_RF = results_RF[1];

                double[] results_NB = classification(cls_NB, min, max, std, zcr, mean, var);
                int predictedClass_NB = (int) results_NB[0];
                double confidence_NB = results_NB[1];

                Intent Train_intent = new Intent(Collect.this, Train.class);
                Train_intent.putExtra("DT_confidence", confidence_DT);
                Train_intent.putExtra("DT_predicted_class", predictedClass_DT);
                Train_intent.putExtra("RF_confidence", confidence_RF);
                Train_intent.putExtra("RF_predicted_class", predictedClass_RF);
                Train_intent.putExtra("NB_confidence", confidence_NB);
                Train_intent.putExtra("NB_predicted_class", predictedClass_NB);
                startActivity(Train_intent);
            }
        } );
        deploy_button_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Collect.this, Deploy.class);
                startActivity(intent);
            }
        } );



        //start, end button functions, and sensor collect data
        start_button_c = (Button) findViewById(R.id.start_CP_btn);
        end_button_c = (Button) findViewById(R.id.end_CP_btn);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(Collect.this,accelerometer,sensorManager.SENSOR_DELAY_NORMAL);

        start_button_c.setBackgroundColor(Color.parseColor("#ADD8E6"));
        end_button_c.setBackgroundColor(Color.parseColor("#ADD8E6"));
        try {
            cls_DT = (Classifier) read(getAssets().open("DecisionTree.model"));
            cls_RF = (Classifier) read(getAssets().open("RandomForest.model"));
            cls_NB = (Classifier) read(getAssets().open("NaiveBayes.model"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        start_button_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCollecting) {
                    sensorManager.registerListener(Collect.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                    handler.postDelayed(saveDataRunnable, 2000);
                    isCollecting = true;
                    start_button_c.setBackgroundColor(Color.parseColor("#D3D3D3"));
                    end_button_c.setBackgroundColor(Color.parseColor("#ADD8E6"));
                }
            }
        });
        end_button_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollecting) {
                    sensorManager.unregisterListener(Collect.this);
                    handler.removeCallbacks(saveDataRunnable);
                    isCollecting = false;
                    start_button_c.setBackgroundColor(Color.parseColor("#ADD8E6"));
                    end_button_c.setBackgroundColor(Color.parseColor("#D3D3D3"));

                    magnitudeList.clear();
                }
            }
        });
    }





    private Runnable saveDataRunnable = new Runnable() {
        @Override
        public void run() {
            collectData();
            handler.postDelayed(this, 2000);
        }
    };

    //sensor get magnitudes
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        Log.d("SensorData", "X: " + x + ", Y: " + y + ", Z: " + z);
        float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
        if (isCollecting) {
            Log.d("Magnitude","magnitudes:"+ magnitude);
            magnitudeList.add(magnitude);
            Log.d("AddMagnitude","success");
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }




    public double [] classification(Classifier cls, double Min, double Max, double StdDev, double ZeroCrossingRate, double Mean, double Variance) {
        double [] predicted_class =new double[2];
        predicted_class[0]=0.0;
        predicted_class[1]=0.0;

        Log.d("ClassificationData", "Min: " + Min + ", Max: " + Max + ", StdDev: " + StdDev + ", ZeroCrossingRate: " + ZeroCrossingRate + ", Mean: " + Mean + ", Variance: " + Variance);

        try {

            ArrayList<Attribute> attributes = new ArrayList<>();

            attributes.add(new Attribute("Min",0));
            attributes.add(new Attribute("Max",1));
            attributes.add(new Attribute("StdDev",2));
            attributes.add(new Attribute("ZeroCrossingRate",3));
            attributes.add(new Attribute("Mean",4));
            attributes.add(new Attribute("Variance",5));

            attributes.add(new Attribute("Class", Arrays.asList("0","1","2","3","4","5"),6));

            Instance instance = new SparseInstance(6);
            instance.setValue(attributes.get(0), Min);
            instance.setValue(attributes.get(1), Max);
            instance.setValue(attributes.get(2), StdDev);
            instance.setValue(attributes.get(3), ZeroCrossingRate);
            instance.setValue(attributes.get(4), Mean);
            instance.setValue(attributes.get(5), Variance);


            Instances datasetConfiguration;
            datasetConfiguration = new Instances("temp_data-holder", attributes, 0);

            datasetConfiguration.setClassIndex(6);
            instance.setDataset(datasetConfiguration);


            double[] distribution;
            distribution = cls.distributionForInstance(instance);
            Log.d("DistributionData", "Distribution Array: " + Arrays.toString(distribution));
            predicted_class[0] = cls.classifyInstance(instance);
            int maxIndex = 0;
            for (int i = 1; i < distribution.length; i++) {
                if (distribution[i] > distribution[maxIndex]) {
                    maxIndex = i;
                }
            }
            predicted_class[1] = distribution[maxIndex] * 100;

        }catch (NumberFormatException e){
            e.printStackTrace();
            Toast.makeText(Collect.this, "Error: " + String.valueOf(e) , Toast.LENGTH_LONG).show();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return predicted_class;
    }




    private void collectData() {
        min = calculateMin(magnitudeList);
        max = calculateMax(magnitudeList);
        mean = calculateMean(magnitudeList);
        var = calculateVariance(magnitudeList, mean);
        std = (float) Math.sqrt(var);
        zcr = calculateZeroCrossingRate(magnitudeList);
        Log.d("CollectData","Min: " + min + ", Max: " + max + ", StdDev: " + std + ", ZeroCrossingRate: " + zcr + ", Mean: " + mean + ", Variance: " + var);

        magnitudeList.clear();
    }





    //calculate Min, Max, Mean, Std, Var, ZeroCrossing
    private float calculateMin(List<Float> values) {
        if (values.isEmpty()) return 0;
        return Collections.min(values);
    }

    private float calculateMax(List<Float> values) {
        if (values.isEmpty()) return 0;
        return Collections.max(values);
    }

    private float calculateMean(List<Float> values) {
        if (values.isEmpty()) return 0;
        float sum = 0;
        for (float value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    private float calculateVariance(List<Float> values, float mean) {
        if (values.isEmpty()) return 0;
        float variance = 0;
        for (float value : values) {
            variance += (value - mean) * (value - mean);
        }
        return variance / values.size();
    }

    private float calculateStandardDeviation(List<Float> values) {
        if (values.isEmpty()) return 0;
        float mean = calculateMean(values);
        float variance = calculateVariance(values, mean);
        return (float) Math.sqrt(variance);
    }

    private float calculateZeroCrossingRate(List<Float> values) {
        if (values.isEmpty()) return 0;
        int zeroCrossings = 0;
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i - 1) * values.get(i) < 0) {
                zeroCrossings++;
            }
        }
        return (float) zeroCrossings / values.size();
    }
}