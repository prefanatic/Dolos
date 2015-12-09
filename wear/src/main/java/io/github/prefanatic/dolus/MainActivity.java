package io.github.prefanatic.dolus;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private static final String TAG = "Dolus";
    private static final SimpleDateFormat AMBIENT_DATE_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    private BoxInsetLayout mContainerView;
    private TextView mTextView, mAccurateText, mVariable;
    private TextView mClockView;
    private SensorManager manager;

    private int accuracy;
    private int hrV;
    private long lastHeartRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor hrSensor = manager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        manager.registerListener(this, hrSensor, SensorManager.SENSOR_DELAY_NORMAL);

        mContainerView = (BoxInsetLayout) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.text);
        mAccurateText = (TextView) findViewById(R.id.accuracy);
        mClockView = (TextView) findViewById(R.id.clock);
        mVariable = (TextView) findViewById(R.id.hrv);

        lastHeartRate = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        manager.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        Log.d(TAG, "onSensorChanged() called with: " + "event = [" + event + "]");

        if (accuracy != SensorManager.SENSOR_STATUS_UNRELIABLE && accuracy != SensorManager.SENSOR_STATUS_NO_CONTACT) {
            hrV = (int) (System.currentTimeMillis() - lastHeartRate);
            lastHeartRate = System.currentTimeMillis();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(String.format("HR: %f", event.values[0]));
                mVariable.setText(String.format("Variability: %d", hrV));
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, final int accuracy) {
        this.accuracy = accuracy;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String msg = "Accurate";
                switch (accuracy) {
                    case SensorManager.SENSOR_STATUS_UNRELIABLE:
                        msg = "Unreliable";
                        break;
                    case SensorManager.SENSOR_STATUS_NO_CONTACT:
                        msg = "No Contact";
                        break;
                }

                mAccurateText.setText(msg);
            }
        });
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
            mTextView.setTextColor(getResources().getColor(android.R.color.white));
            mClockView.setVisibility(View.VISIBLE);

            mClockView.setText(AMBIENT_DATE_FORMAT.format(new Date()));
        } else {
            mContainerView.setBackground(null);
            mTextView.setTextColor(getResources().getColor(android.R.color.black));
            mClockView.setVisibility(View.GONE);
        }
    }
}
