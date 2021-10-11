package com.example.lequangviet.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.anastr.speedviewlib.SpeedView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    TextView txtTemp;
    TextView txtHumi;
    Button btn;
    ToggleButton btnLED;
    SpeedView speedwind;
    int waiting_period = 0;
    boolean sending_mess_again = false;

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTemp = findViewById(R.id.txtTemperature);
        txtHumi = findViewById(R.id.txtHumidity);
        btnLED = findViewById(R.id.btnLED);
        btn = findViewById(R.id.btnClickme);
        txtTemp.setText("Temperature");
        txtHumi.setText("Humidity");
        speedwind = (SpeedView) findViewById(R.id.speedView);


        btnLED.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    Log.d("mqtt", "Button is ON");
                    sendDataToMQTT("vietlenai/feeds/bbc-temp", "1");
                } else {
                    Log.d("mqtt", "Button if OFF");
                    sendDataToMQTT("vietlenai/feeds/bbc-temp", "0");
                }

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity2();
            }
        });
        startMQTT();
//        setupErrotControl();

    }

    public void activity2() {
        Intent intent = new Intent(this, Newactivity.class);
        startActivity(intent);
    }

    private void setupErrotControl() {
        Timer aTimer = new Timer();
        TimerTask aTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("mqtt", "Task is executed");
            }
        };
        aTimer.schedule(aTask, 5000, 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void sendDataToMQTT(String topic, String mess) {
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        byte[] b = mess.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        } catch (Exception e) {
        }
    }


    private void startMQTT() {
        mqttHelper = new MQTTHelper(getApplicationContext(), "1235");
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
                if (topic.contains("bbc-temp")) {
                    txtTemp.setText(message.toString() + "°C");
                }
                if (topic.contains("bbc-humidity")) {
                    txtHumi.setText(message.toString() + "%");
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }

        });
    }

}