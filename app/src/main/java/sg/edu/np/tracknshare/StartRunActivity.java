package sg.edu.np.tracknshare;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.RunDBHandler;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.handlers.TrackingDBHandler;
import sg.edu.np.tracknshare.models.MyLatLng;
import sg.edu.np.tracknshare.models.Run;

//This class is the main activity where the following are done
//Step Counting - records steps per run and saves it to the local DB
//Timer - The timer runs when the user starts the run and stops when the user clicks stop run.

public class StartRunActivity extends AppCompatActivity{

    Handler handler = new Handler(Looper.getMainLooper());
    Date date1;
    Date date2;
    private SensorManager sensorManager = null;
    int seconds = 0;
    boolean running = false;
    int previousTotalSteps = 0;
    int totalSteps = 0;
    int currentSteps;
    private AlertDialog dialog;
    public boolean isMapEnabled(Context c){
        LocationManager manager = (LocationManager) getSystemService(c.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_run);
        Log.e("Started Main Activity", "Done");
        //requestPermissions();

        TrackingUtility tU = new TrackingUtility();
        Log.d("PERMS", "HasPerms1: "+tU.HasPerms(this));
        if (!tU.HasPerms(this)){
            createDialogForPermission();
        }

        AuthHandler auth = new AuthHandler(this);
        RunDBHandler runsDB = new RunDBHandler(this);
        TrackingDBHandler trackingDB = new TrackingDBHandler(this);

        loadData();
        Button startBtn = findViewById(R.id.startRun);

        if (!isMapEnabled(this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("GPS is turned off. Please enable GPS.")
                    .setCancelable(false)
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Button posButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                    posButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isMapEnabled(StartRunActivity.this)){
                                dialog.dismiss();
                            }
                        }
                    });
                    Button negButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                    negButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                }
            });
            dialog.show();
        }

        if (!tU.isMyServiceRunning(TrackingService.class, StartRunActivity.this)){
            startBtn.setText("Start");
        } else{
            startBtn.setText("Stop");
        }
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("STARRUN", "onClick: "+startBtn.getText().toString());
                if (!tU.isMyServiceRunning(TrackingService.class, StartRunActivity.this)){
                    startBtn.setText("Stop");
                    sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE);
                    running = true;
                    date1 = Calendar.getInstance().getTime();
                    runTimer();
                    stepCounter();
                    Toast.makeText(StartRunActivity.this, "Start Run!", Toast.LENGTH_SHORT).show();
                } else{
                    sendCommandToService(Constants.ACTION_STOP_SERVICE);
                    running = false;
                    date2 = Calendar.getInstance().getTime();
                    long diffInMs = date2.getTime() - date1.getTime();
                    long diffInS = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
                    SharedPreferences sharedPreferences = getSharedPreferences("steps", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("key2", diffInS);
                    editor.apply();
                    Toast.makeText(StartRunActivity.this, "Stopped Run!", Toast.LENGTH_SHORT).show();
                    saveData();

                    AlertDialog.Builder builder = new AlertDialog.Builder(StartRunActivity.this);
                    builder.setMessage("Are you sure you want to stop?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", null)
                            .setNegativeButton("No", null);
                    AlertDialog dialog = builder.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            Button posButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                            posButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(StartRunActivity.this, CreateRunActivity.class);
                                    startActivity(intent);
                                }
                            });
                            Button negButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                            negButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                    dialog.show();
//                    StorageHandler storageHandler = new StorageHandler();
//
//                    long id = storageHandler.GenerateId();
//                    Run r = new Run(auth.GetCurrentUser().getUid(), "",""+id,null,1,getDistance(),1,1,trackingDB.getAllPoints());
//                    runsDB.AddRun(r);
//
//                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
//
//                    ArrayList<MyLatLng> rList = trackingDB.getAllPoints();
//                    for (int i = 0; i < rList.size() - 1; i++) {
//                        LatLng latLng = new LatLng(rList.get(i).latitude, rList.get(i).longitude);// in this line put you lat and long
//                        builder.include(latLng);  //add latlng to builder
//                    }
//
//                    LatLngBounds bounds = builder.build();
//
//                    int padding = 0; // offset from edges of the map in pixels
//                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//
//                    MapsFragment.map.moveCamera(cu);
//
//                    GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
//                        Bitmap bitmap;
//
//                        @Override
//                        public void onSnapshotReady(Bitmap snapshot) {
//                            bitmap = snapshot;
//                            storageHandler.UploadRunImage(id, bitmap);
//                        }
//                    };
//
//                    MapsFragment.map.snapshot(callback);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TrackingUtility tU = new TrackingUtility();
        if (tU.HasPerms(StartRunActivity.this)){
            dialog.dismiss();
        } else if (tU.PermsDeniedPermemently(StartRunActivity.this)){
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }
    public void createDialogForPermission(){
        Log.d("PERMS", "CREAYE");
        TrackingUtility tU = new TrackingUtility();

        AlertDialog.Builder builder = new AlertDialog.Builder(StartRunActivity.this);
        builder.setTitle("Location permissions needed.")
                .setMessage("Click \"Accept\" to allow location permissions.")
                .setCancelable(false)
                .setPositiveButton("Accept", null)
                .setNegativeButton("Cancel", null);
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button posButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                posButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tU.requestPermission(StartRunActivity.this);
                    }
                });
                Button negButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                negButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        });
        dialog.show();
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
                    //Implement the setting the same time to the textview when the app is restarted..... :)
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
    public double getDistance(){
        TrackingDBHandler trackingDB = new TrackingDBHandler(this);
        ArrayList<MyLatLng> pointsList = trackingDB.getAllPoints();
        double totalDistance = 0;
        for (int i = 0; i < pointsList.size() - 1; i++) {
            MyLatLng src = pointsList.get(i);
            MyLatLng dest = pointsList.get(i + 1);
            // mMap is the Map Object
            totalDistance += convertToKm(src, dest);
        }
        return totalDistance;
    }
    public double convertToKm(MyLatLng p1, MyLatLng p2){
        // Uses havasine formula to get distance
        // https://cloud.google.com/blog/products/maps-platform/how-calculate-distances-map-maps-javascript-api

        int earthRadius = 6371;
        double rLat1 = p1.latitude * (Math.PI / 100);
        double rLat2 = p2.latitude * (Math.PI / 100);

        double diffLat = rLat1 - rLat2;
        double diffLng = (p1.longitude - p2.longitude) * (Math.PI / 100);
        double distance = 2 * earthRadius * Math.asin(Math.sqrt(Math.sin(diffLat / 2) * Math.sin(diffLat / 2) + Math.cos(rLat1) * Math.cos(rLat2) * Math.sin(diffLng / 2) * Math.sin(diffLng / 2)));

        return distance;
    }

}