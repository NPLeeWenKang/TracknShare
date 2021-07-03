package sg.edu.np.tracknshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.RunDBHandler;
import sg.edu.np.tracknshare.handlers.TrackingDBHandler;
import sg.edu.np.tracknshare.models.Run;

//This class is the main activity where the following are done
//Step Counting - records steps per run and saves it to the local DB
//Timer - The timer runs when the user starts the run and stops when the user clicks stop run.

public class StartRunActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private SensorManager sensorManager = null;
    int seconds = 0;
    boolean running = false;
    int previousTotalSteps = 0;
    int totalSteps = 0;
    int currentSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_run);
        Log.e("Started Main Activity", "Done");
        requestPermissions();

        AuthHandler auth = new AuthHandler(this);
        RunDBHandler runsDB = new RunDBHandler(this);
        TrackingDBHandler trackingDB = new TrackingDBHandler(this);

        loadData();
        Button startBtn = findViewById(R.id.startRun);
        Button stopBtn = findViewById(R.id.stopRun);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE);
                running = true;
                runTimer();
                stepCounter();
                Toast.makeText(StartRunActivity.this, "Start Run!", Toast.LENGTH_SHORT).show();
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommandToService(Constants.ACTION_STOP_SERVICE);
                running = false;
                Toast.makeText(StartRunActivity.this, "Stopped Run!", Toast.LENGTH_SHORT).show();
                saveData();

                Run r = new Run(auth.GetCurrentUser().getUid(), "1",null,1,1,1,1,trackingDB.getAllPoints());
                runsDB.AddRun(r);
            }
        });
    }
    private void requestPermissions(){
        TrackingUtility tU = new TrackingUtility();
        if(tU.HasPermissions(StartRunActivity.this)){
            return;
        }
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                    StartRunActivity.this,
                    "You have to accept permissions",
                    0,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACTIVITY_RECOGNITION
            );
        }else{
            EasyPermissions.requestPermissions(
                    StartRunActivity.this,
                    "You have to accept permissions",
                    0,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            );
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        TrackingUtility tU = new TrackingUtility();
        if (EasyPermissions.somePermissionPermanentlyDenied(StartRunActivity.this, perms)){
            new AppSettingsDialog.Builder(StartRunActivity.this).build().show();
        }else{
            requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,StartRunActivity.this);
    }
    //Send commands to Service class
    public void sendCommandToService(String action){
        Intent intent = new Intent(StartRunActivity.this, TrackingService.class);
        intent.setAction(action);
        StartRunActivity.this.startService(intent);
    }
    //Timer Codes
    private void runTimer(){
        TextView timer = findViewById(R.id.timer);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(),
                        "%d:%02d:%02d",
                        hours, minutes, secs);
                if(running){
                    seconds++;
                    timer.setText(time);
                    Log.e("STOPWATCH", ""+time);
                }
                else{
                    handler.removeCallbacks(this::run);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
    //StepCounter codes
    public void stepCounter(){
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(running){
                    totalSteps = (int) event.values[0];
                    currentSteps = totalSteps - previousTotalSteps;
                    TextView tv = findViewById(R.id.tv_stepCounter);
                    tv.setText(""+currentSteps + "steps");
                    Log.e("Running", "TRACKING STEPS" + currentSteps);
                }
                else{
                    Log.e("Running", "NOT TRACKING STEPS");
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor == null) {
            // This will give a toast message to the user if there is no sensor in the device
            Toast.makeText(StartRunActivity.this, "No sensor detected on this device", Toast.LENGTH_SHORT).show();
        } else {
            // Rate suitable for the user interface
            sensorManager.registerListener(sensorEventListener, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
        Toast.makeText(StartRunActivity.this, "Start Run!", Toast.LENGTH_SHORT).show();
    }
    //Saving and Loading StepCounter Data from sharedPreferences
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("steps", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("key1", previousTotalSteps);
        editor.apply();
    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("steps", Context.MODE_PRIVATE);
        int savedData = sharedPreferences.getInt("key1", 0);
        previousTotalSteps = savedData;
    }
    //Reset steps when the user longpress on the textview.
    public void resetSteps(View view) {
        TextView tv_stepsTaken = findViewById(R.id.tv_stepCounter);
        tv_stepsTaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartRunActivity.this, "Long tap to reset", Toast.LENGTH_SHORT).show();
            }
        });
        tv_stepsTaken.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                previousTotalSteps = totalSteps;
                tv_stepsTaken.setText(""+0 + "steps");
                saveData();
                return true;
            }
        });
    }

}