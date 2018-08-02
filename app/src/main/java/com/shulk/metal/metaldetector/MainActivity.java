package com.shulk.metal.metaldetector;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import com.github.anastr.speedviewlib.TubeSpeedometer;
import net.mabboud.android_tone_player.OneTimeBuzzer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    public static DecimalFormat DECIMAL_FORMATTER;
    private ProgressBar prg;
    private TubeSpeedometer tubeSpeedometer;
    double magnitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // define decimal formatter
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("#.000", symbols);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        tubeSpeedometer = (TubeSpeedometer) findViewById(R.id.tubeSpeedometer);

        prg = (ProgressBar) findViewById(R.id.progressBar);
        //prg.setMax(100);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // get values for each axes X,Y,Z
            float magX = event.values[0];
            float magY = event.values[1];
            float magZ = event.values[2];
            magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));
            // set value on the screen
            prg.setProgress((int) magnitude);
            tubeSpeedometer.speedTo((int) magnitude);

            playTones();
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void playTones(){
        int sensorValue = (int) magnitude;

        if(sensorValue >= 90){

            OneTimeBuzzer buzzer = new OneTimeBuzzer();
            buzzer.setDuration(5);

            buzzer.play();
        }

    }
}
