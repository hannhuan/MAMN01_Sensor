package com.example.compass;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        compass = (ImageView) findViewById(R.id.compass_image);
        sensor_manager_compass = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor_manager_acclerometer = (SensorManager)getSystemService(SENSOR_SERVICE);

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

            x_coordinator.setText(""+x);
            y_coordinator.setText(""+y);
            z_coordinator.setText(""+z);

        }
        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree_round_z = event.values[0];
            RotateAnimation ra = new RotateAnimation(compass_degree, -degree_round_z, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(200);
            ra.setFillAfter(true);
            compass.startAnimation(ra);
            compass_degree = -degree_round_z;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
