package sg.edu.np.tracknshare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.RunDBHandler;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.handlers.TrackingDBHandler;
import sg.edu.np.tracknshare.models.MyLatLng;
import sg.edu.np.tracknshare.models.Run;

public class CreateRunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_run);

        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView dateText = findViewById(R.id.create_run_date);
        TextView timeText = findViewById(R.id.create_run_time);
        TextView distanceText = findViewById(R.id.create_run_distance);
        TextView paceText = findViewById(R.id.create_run_pace);
        TextView caloriesText = findViewById(R.id.create_run_calories);
        TextView stepsText = findViewById(R.id.create_run_steps);

        StorageHandler storageHandler = new StorageHandler();
        TrackingDBHandler trackingDB = new TrackingDBHandler(CreateRunActivity.this);
        AuthHandler auth = new AuthHandler(CreateRunActivity.this);

        Calendar calendar = Calendar.getInstance();
        long timeMilli = calendar.getTimeInMillis();
        Run r = new Run(auth.getCurrentUser().getUid(), ""+timeMilli,timeMilli, ""+timeMilli,getTimeInS(),getDistance(),getCalories(),getPace(), getStep(), trackingDB.getAllPoints());

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy h:ma");

        dateText.setText(dateFormat.format(timeMilli));
        timeText.setText("" + getTimeInS() + " seconds");
        distanceText.setText(String.format("%.4f", getDistance()) + "km");
        paceText.setText("" + String.format("%.2f", getPace()) + " m/s");
        caloriesText.setText(""+getCalories());
        stepsText.setText(""+r.getRunSteps());

        ImageView settingsBtn = findViewById(R.id.save_profile);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSave(r);
                finish();
            }
        });
        TextView mapClickable = findViewById(R.id.mapClickable);
        mapClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateRunActivity.this, FullMapActivity.class);
                intent.putExtra("mapType", "movable");
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                displayBackConfirmation();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        displayBackConfirmation();
    }
    public void displayBackConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Data will be lost. Are you sure?")
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
    public void performSave(Run r){
        StorageHandler storageHandler = new StorageHandler();
        AuthHandler auth = new AuthHandler(CreateRunActivity.this);
        TrackingDBHandler trackingDB = new TrackingDBHandler(CreateRunActivity.this);
        RunDBHandler runsDB = new RunDBHandler(CreateRunActivity.this);

        runsDB.addRun(r);

        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;
                storageHandler.uploadRunImage(""+r.getRunId(), bitmap);
            }
        };

        MapsFragment.map.snapshot(callback);
    }

    //Gets the distance from the local database

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

    //Method for converting the distance to Km

    public double convertToKm(MyLatLng p1, MyLatLng p2){
        // Uses Havasine formula to get distance
        // https://cloud.google.com/blog/products/maps-platform/how-calculate-distances-map-maps-javascript-api

        int earthRadius = 6371;
        double rLat1 = p1.latitude * (Math.PI / 100);
        double rLat2 = p2.latitude * (Math.PI / 100);

        double diffLat = rLat1 - rLat2;
        double diffLng = (p1.longitude - p2.longitude) * (Math.PI / 100);
        double distance = 2 * earthRadius * Math.asin(Math.sqrt(Math.sin(diffLat / 2) * Math.sin(diffLat / 2) + Math.cos(rLat1) * Math.cos(rLat2) * Math.sin(diffLng / 2) * Math.sin(diffLng / 2)));

        return distance;
    }

    //Gets the time between the user clicks on the start button and stop button to display the total time taken up by the user.

    public long getTimeInS(){
        SharedPreferences sharedPreferences = getSharedPreferences("tracking", Context.MODE_PRIVATE);
        long initialTime = sharedPreferences.getLong("initialTime", 0);
        long finalTime = sharedPreferences.getLong("finalTime", 0);
        long diffTime = (finalTime - initialTime);
        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffTime);
        return diffInSec;
    }

    //Gets the speed / pace from the distance and time

    public double getPace(){
        double distance = getDistance() * 1000;
        long time = getTimeInS();
        double speed = distance / time; //simple formula to calculate pace...
        return speed;
    }

    //Method for calculating the calories

    public int getCalories(){
        //The CONSTANT is derived from this website
        //https://www.healthline.com/health/calories-burned-walking#::text=Calories%20burned%20per%20mile,Daniel%20V.
        int calories = (int) Math.rint((getDistance() / 1.609) * 84.85);
        return calories;
    }


    public int getStep(){
        Intent intent = getIntent();
        return intent.getIntExtra("stepsOfRun", 0);
    }
}