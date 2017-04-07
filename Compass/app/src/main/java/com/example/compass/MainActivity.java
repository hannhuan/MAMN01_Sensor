package com.example.compass;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private ImageView compass;
    private float compass_degree = 0f;
    private SensorManager sensor_manager_compass;
    private SensorManager sensor_manager_acclerometer;
    private TextView x_coordinator, y_coordinator, z_coordinator;
    long prev_time_acc = 0;
    private boolean if_north = true;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        compass = (ImageView) findViewById(R.id.compass_image);
        sensor_manager_compass = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor_manager_acclerometer = (SensorManager)getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //creates x, y, z coordinator
        x_coordinator = (TextView)findViewById(R.id.current_x);
        y_coordinator = (TextView)findViewById(R.id.current_y);
        z_coordinator = (TextView)findViewById(R.id.current_z);
    }

    public void onResume(){
        super.onResume();
        sensor_manager_compass.registerListener(this, sensor_manager_compass.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_GAME);
        sensor_manager_acclerometer.registerListener(this,sensor_manager_acclerometer.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPause(){
        super.onPause();
        sensor_manager_compass.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long current_time = System.currentTimeMillis();
            long delta_time = current_time - prev_time_acc;
            //The coordinates update each 0.5s.
            if(delta_time > 500) {
                //x_coordinator.setText("" + x);
                y_coordinator.setText("" + y);
                z_coordinator.setText("" + z);
                prev_time_acc = current_time;
            }

        }
        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            //the picture is 34f rotated to right
            float degree_round_z = event.values[0] + 34f;
            RotateAnimation ra = new RotateAnimation(compass_degree, -degree_round_z, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(200);
            ra.setFillAfter(true);
            compass.startAnimation(ra);
            compass_degree = -degree_round_z;

            //vibrates each time when compass is towards north.
            if ((event.values[0] >= 0 && event.values[0] <= 15) || (event.values[0] >= 345 && event.values[0] <= 360)) {
                if(if_north) {
                    vibrator.vibrate(200);
                    if_north = false;
                }
            }else {
                if_north = true;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
