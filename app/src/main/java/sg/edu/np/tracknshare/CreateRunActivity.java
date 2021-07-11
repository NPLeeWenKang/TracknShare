package sg.edu.np.tracknshare;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

        StorageHandler storageHandler = new StorageHandler();
        TrackingDBHandler trackingDB = new TrackingDBHandler(CreateRunActivity.this);
        AuthHandler auth = new AuthHandler(CreateRunActivity.this);

        Calendar calendar = Calendar.getInstance();
        long timeMilli = calendar.getTimeInMillis();
        Run r = new Run(auth.GetCurrentUser().getUid(), ""+timeMilli,""+timeMilli,null,1,getDistance(),1,1,trackingDB.getAllPoints());

        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy h:ma");
        System.out.println(dateFormat.format(timeMilli));

        dateText.setText(dateFormat.format(timeMilli));
        timeText.setText("NULL");
        distanceText.setText(String.format("%.4f", getDistance()));
        paceText.setText("NULL");
        caloriesText.setText("NULL");

        ImageView settingsBtn = findViewById(R.id.save_profile);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSave();
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
        super.onBackPressed();
        overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
    }
    public void displayBackConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Data will be lost. Are you sure?")
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
    public void performSave(){
        StorageHandler storageHandler = new StorageHandler();
        AuthHandler auth = new AuthHandler(CreateRunActivity.this);
        TrackingDBHandler trackingDB = new TrackingDBHandler(CreateRunActivity.this);
        RunDBHandler runsDB = new RunDBHandler(CreateRunActivity.this);

        long id = storageHandler.GenerateId();
        Run r = new Run(auth.GetCurrentUser().getUid(), ""+id,""+id,null,1,getDistance(),1,1,trackingDB.getAllPoints());
        runsDB.AddRun(r);

        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;
                storageHandler.UploadRunImage(id, bitmap);
            }
        };

        MapsFragment.map.snapshot(callback);
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