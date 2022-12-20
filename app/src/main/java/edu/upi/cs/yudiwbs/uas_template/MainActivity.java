package edu.upi.cs.yudiwbs.uas_template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private BottomNavigationView bnv;

    private SensorManager sm;
    private Sensor senAccel;

    private TextView tvhasil;
    String TAG = "debug_yudi";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvhasil = findViewById(R.id.tvhasil);

        //cek apakah sensor tersedia
        sm = (SensorManager)    getSystemService(getApplicationContext().SENSOR_SERVICE);
        senAccel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (senAccel != null){
            Log.d(TAG,"Sukses, device punya sensor accelerometer!");
        }
        else {
            // gagal, tidak ada sensor accelerometer.
            Log.d(TAG,"Tidak ada sensor accelerometer!");
        }


        bnv = findViewById(R.id.bnv);
        bnv.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.itmSatu:
                                Toast.makeText(getApplicationContext(), "Satu", Toast.LENGTH_SHORT).show();
                                if (savedInstanceState == null) {
                                    getSupportFragmentManager().beginTransaction()
                                            .setReorderingAllowed(true)
                                            .replace(R.id.fragmentContainerView, FragmentSatu.class, null)
                                            .commit();
                                }
                                break;
                            case R.id.itmDua:
                                Toast.makeText(getApplicationContext(), "Dua", Toast.LENGTH_SHORT).show();
                                if (savedInstanceState == null) {
                                    getSupportFragmentManager().beginTransaction()
                                            .setReorderingAllowed(true)
                                            .replace(R.id.fragmentContainerView, FragmentDua.class, null)
                                            .commit();
                                }
                                break;

                        }

                        return true;
                    }
                });

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double ax=0,ay=0,az=0;
        boolean isTabrakan = false;
        // menangkap perubahan nilai sensor
        if (sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            ax=sensorEvent.values[0];
            ay=sensorEvent.values[1];
            az=sensorEvent.values[2];
        }

        if  (az<=8) {
            isTabrakan = true;
        }
        if (isTabrakan) {
            Log.d(TAG, "TABRAKAN!!");
            tvhasil.setText("TABRAKAN!!");
        } else{
            long timestamp = System.currentTimeMillis();
            // Menampilkan log dari accelerometer beserta timestamp
            String msg = "X: " + ax + ", Y: " + ay + ", Z: " + az + ", Timestamp: " + timestamp;
            Log.d("Position", msg);
            tvhasil.setText(msg);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, senAccel, SensorManager.SENSOR_DELAY_NORMAL);
    }


}