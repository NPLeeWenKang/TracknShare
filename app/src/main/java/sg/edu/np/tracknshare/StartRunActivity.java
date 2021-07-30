package sg.edu.np.tracknshare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import sg.edu.np.tracknshare.handlers.TrackingDBHandler;

//This class is the main activity where the following are done
//Step Counting - records steps per run and saves it to the SharedPreferences
//Timer - The timer runs when the user starts the run and stops when the user clicks stop run.

public class StartRunActivity extends AppCompatActivity{

    Handler handler = new Handler(Looper.getMainLooper());
    private SensorManager sensorManager = null;
    long seconds = 0;
    boolean running = false;
    int initialSteps;
    int totalSteps;
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

        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    /*Most of the function in this code is to be run when the users device is not awake / active. So we are using Foreground Service to track
    the data */

    @Override
    protected void onResume() {
        super.onResume();

        TrackingUtility tU = new TrackingUtility();
        if (!tU.HasPerms(this)){
            createDialogForPermission();
        }

        TrackingDBHandler trackingDB = new TrackingDBHandler(this);

        Button startBtn = findViewById(R.id.startRun);
        TextView timer = findViewById(R.id.timer);

        //Checks to see whether GPS is enabled or not and prompts the user with a dialog option

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

        /*Checks whether service is running in the Background and if not gets the UI ready to start a service and if yes continues with the Timer
        Step Counter*/

        if (!tU.isMyServiceRunning(TrackingService.class, StartRunActivity.this)){
            // No run in progress
            startBtn.setText("Start");
            timer.setText("0:00:00");
            TextView steps = findViewById(R.id.steps);
            steps.setText("0 steps");
            seconds = 0;
        } else{
            // Run is in progress
            startBtn.setText("Stop");
            SharedPreferences sharedPreferences = getSharedPreferences("tracking", Context.MODE_PRIVATE);
            long initialTime = sharedPreferences.getLong("initialTime", 0);
            long diffTime = (Calendar.getInstance().getTimeInMillis() - initialTime);
            long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffTime);
            long hours = diffInSec / 3600;
            long minutes = (diffInSec % 3600) / 60;
            long secs = diffInSec % 60;
            String time = String.format(Locale.getDefault(),
                    "%d:%02d:%02d",
                    hours, minutes, secs);
            timer.setText(time);

            seconds = diffInSec;
            running = true;

            SharedPreferences stepsSharedPref = getSharedPreferences("steps", Context.MODE_PRIVATE);
            initialSteps = stepsSharedPref.getInt("initialSteps",0);
            stepCounter();
            runTimer();
        }

       /* User clicks on the "Start" button and the program starts the Service and starts tracking the Step Counter and the Timer*/

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("STARRUN", "onClick: "+startBtn.getText().toString());
                if (!tU.isMyServiceRunning(TrackingService.class, StartRunActivity.this)){
                    startBtn.setText("Stop");
                    sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE);
                    running = true;
                    long initialMS = Calendar.getInstance().getTimeInMillis();
                    SharedPreferences sharedPreferences = getSharedPreferences("tracking", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("initialTime", initialMS);
                    editor.apply();

                    stepCounter();
                    runTimer();
                    Toast.makeText(StartRunActivity.this, "Start Run!", Toast.LENGTH_SHORT).show();
                } else{
                    Log.d("GETCOUNT", "onClick: "+trackingDB.getCount());
                    if (trackingDB.getCount() > 2){
                        AlertDialog.Builder builder = new AlertDialog.Builder(StartRunActivity.this);
                        builder.setMessage("Are you sure you want to stop?")
                                .setCancelable(true)
                                .setPositiveButton("Yes", null)
                                .setNegativeButton("No", null);
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                Button posButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                                //Stops the service, step tracker and the timer when positive button is clicked

                                posButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();

                                        handler.removeCallbacksAndMessages(null);

                                        sendCommandToService(Constants.ACTION_STOP_SERVICE);
                                        running = false;

                                        long finalMS = Calendar.getInstance().getTimeInMillis();
                                        SharedPreferences sharedPreferences = getSharedPreferences("tracking", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putLong("finalTime", finalMS);
                                        editor.apply();

                                        SharedPreferences stepsSharedPref = getSharedPreferences("steps", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor stepEditor = stepsSharedPref.edit();
                                        stepEditor.clear();
                                        stepEditor.apply();

                                        long initialTime = sharedPreferences.getLong("initialTime", 0);
                                        long finalTime = sharedPreferences.getLong("finalTime", 0);
                                        long diffTime = (finalTime - initialTime);
                                        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffTime);

                                        Log.e("FINAL", "onClick: " +diffInSec);
                                        Toast.makeText(StartRunActivity.this, "Stopped Run!", Toast.LENGTH_SHORT).show();

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
                    }else{

                        //To save the run and draw the route we need at least 2 LatLng. So if there's no enough LatLng this code block is triggered

                        AlertDialog.Builder builder = new AlertDialog.Builder(StartRunActivity.this);
                        builder.setMessage("Not enough points saved.")
                                .setCancelable(true)
                                .setPositiveButton("Back", null);
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                Button posButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                                posButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        dialog.show();
                    }

                }

            }
        });
    }

    //These blocks are for permissions handling

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TrackingUtility tU = new TrackingUtility();
        if (tU.HasPerms(StartRunActivity.this)){
            dialog.dismiss();
            // refresh page
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
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
                        Log.d("PERMS", "onClick: ");
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
                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                long secs = seconds % 60;
                String time = String.format(Locale.getDefault(),
                        "%d:%02d:%02d",
                        hours, minutes, secs);
                if(running){
                    seconds++;
                    timer.setText(time);
                    Log.e("STOPWATCH", ""+time);
                    //Implement the setting the same time to the textview when the app is restarted..... :)
                }
                Log.e("STOPWATCH", ""+time);
                handler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
    @Override
    public void onBackPressed() {
        TrackingUtility tU = new TrackingUtility();
        if (tU.isMyServiceRunning(TrackingService.class, StartRunActivity.this)){
            displayBackConfirmation();
        } else{
            super.onBackPressed();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        TrackingUtility tU = new TrackingUtility();
        switch (item.getItemId()) {
            case android.R.id.home:
                if (tU.isMyServiceRunning(TrackingService.class, StartRunActivity.this)){
                    displayBackConfirmation();
                }else{
                    finish();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayBackConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Run is still in progress. Are you sure?")
                .setCancelable(true)
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
                        sendCommandToService(Constants.ACTION_STOP_SERVICE);
                        running = false;
                        handler.removeCallbacksAndMessages(null);
                        SharedPreferences stepsSharedPref = getSharedPreferences("steps", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = stepsSharedPref.edit();
                        editor.clear();
                        editor.apply();

                        dialog.dismiss();
                        finish();
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
    }

    //StepCounter codes. The stepCounter uses the STEP_COUNTER sensor to capture users steps while walking

    public void stepCounter(){
        TextView steps = findViewById(R.id.steps);
        SensorEventListener sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(running){
                    SharedPreferences stepsSharedPref = getSharedPreferences("steps", Context.MODE_PRIVATE);
                    if (stepsSharedPref.getInt("initialSteps", -1) == -1){
                        Log.d("STEPSLOL", "onSensorChanged: SET");
                        initialSteps = (int) event.values[0];

                        SharedPreferences.Editor editor = stepsSharedPref.edit();
                        editor.putInt("initialSteps", initialSteps);
                        editor.apply();
                    }

                    totalSteps = (int) event.values[0];
                    currentSteps = totalSteps - initialSteps;
                    steps.setText(""+currentSteps + " steps");

                    Log.d("STEPSLOL", "onSensorChanged: "+totalSteps+" "+ initialSteps);
                    if (totalSteps != 0){
                        SharedPreferences.Editor editor = stepsSharedPref.edit();
                        editor.putInt("totalSteps", totalSteps);
                        editor.apply();
                    }

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor == null) {
            // This will give a toast message to the user if there is no sensor in the device
            Toast.makeText(StartRunActivity.this, "No sensor detected on this device", Toast.LENGTH_SHORT).show();
        } else {
            // Rate suitable for the user interface
            sensorManager.registerListener(sensorEventListener, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
}