package com.example.lequangviet.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.anastr.speedviewlib.SpeedView;
import com.github.mikephil.charting.charts.LineChart;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.ArrayList;

public class Newactivity extends AppCompatActivity {

    SpeedView speedwind;
    MQTTHelper mqttHelper;
    LineChart mchart ;
    int count=0;
    int flag = 0;
    float speed2;
    ArrayList<Entry> yValues = new ArrayList<>();
    LineDataSet set1 = new LineDataSet(yValues,"Speed wind");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newactivity);
        speedwind = (SpeedView) findViewById(R.id.speedView);
        mchart = (LineChart) findViewById(R.id.linechart);
        mchart.setDragEnabled(true);
        mchart.setScaleEnabled(true);

        set1.setFillAlpha(200);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(20f);
        set1.setValueTextColor(Color.YELLOW);

        startMQTT();

    }
    public void drwawchart(float speed2)
    {
        Entry tmp = new Entry(count,speed2);
        set1.addEntry(tmp);
        LineData data= new LineData(set1);
        count = count+1;
        mchart.setData(data);
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
                    int speed1 = Integer.parseInt(message.toString());
                    speedwind.speedTo(speed1,1000);
                Log.d("count","!");
                    speed2 = Float.parseFloat(message.toString());
                    if(flag==0)
                    {
                        drwawchart(speed2);
                    }
                    flag = flag + 1;
                    if(flag == 3)
                        flag = 0;


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }

        });
    }

};

