package com.example.lequangviet.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;


public class speedx extends MainActivity {
    MQTTHelper mqttHelper;
    private LineChart mchart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speedx);
//        mchart = (LineChart) findViewById(R.id.linechart);
//
////        mchart.setOnChartGestureListener(speedx.this);
////        mchart.setOnChartValueSelectedListener(speedx.this);
//        mchart.setDragEnabled(true);
//        mchart.setScaleEnabled(false);
//
//        ArrayList<Entry> yValues = new ArrayList<>();
//        yValues.add(new Entry(1,13f));
//        yValues.add(new Entry(2,23f));
//        yValues.add(new Entry(3,33f));
//        yValues.add(new Entry(4,43f));
//        yValues.add(new Entry(5,53f));
//
//
//        LineDataSet set1 = new LineDataSet(yValues,"Data Set 1");
//        set1.setFillAlpha(110);
//
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set1);
//        LineData data= new LineData(dataSets);
//        mchart.setData(data);

        startMQTT();
    }
    private void startMQTT(){
        mqttHelper = new MQTTHelper(getApplicationContext(), "1236");
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.d("Mqtt", "Kết nối thành công");
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("Mqtt", "Received: " + message.toString());
                int speed = Integer.parseInt(message.toString());


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }

        });
    }

};

