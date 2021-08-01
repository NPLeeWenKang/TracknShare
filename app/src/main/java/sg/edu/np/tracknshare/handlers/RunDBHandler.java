package sg.edu.np.tracknshare.handlers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import sg.edu.np.tracknshare.fragments.MapsFragment;
import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.RunsAdapter;
import sg.edu.np.tracknshare.adapters.StatsAdapter;
import sg.edu.np.tracknshare.models.Bargraph;
import sg.edu.np.tracknshare.models.Run;
import sg.edu.np.tracknshare.models.Statistics;

public class RunDBHandler {
    private final String dbUrl = "https://testapp-bc30f-default-rtdb.asia-southeast1.firebasedatabase.app";
    private FirebaseDatabase database = FirebaseDatabase.getInstance(dbUrl);
    Context context;

    public RunDBHandler(Context c){
        context = c;
    }
    public void addRun(Run r){
        Log.d("GENERATEID", "AddRun: "+r.getImageId());
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.child(""+r.getRunId()).setValue(r);
    }
    public void getCount(String id, TextView numRun){
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.orderByChild("userId").equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        numRun.setText(""+task.getResult().getChildrenCount());
                    }

                }
            }
        });
    }
    public void getMyRunStatistics(String id,  ArrayList<Statistics> statisticsList, StatsAdapter adapter){
        // getting general statistics about a user's run
        statisticsList.clear();
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.orderByChild("userId").equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        int totalSteps = 0;
                        double totalDistance = 0;
                        int totalCalories = 0;
                        for (DataSnapshot ds : task.getResult().getChildren()){
                            Run r = ds.getValue(Run.class);
                            totalSteps += r.getRunSteps();
                            totalDistance += r.getRunDistance();
                            totalCalories += r.getRunCalories();
                        }
                        long numRuns = task.getResult().getChildrenCount();
                        int averageSteps = totalSteps / (int) numRuns;
                        double averageDistance = totalDistance / numRuns;
                        int averageCalories = totalCalories / (int) numRuns;
                        Log.d("STATISTICS", "onComplete: "+averageDistance+" "+averageCalories);
                        Statistics steps = new Statistics("Steps", averageSteps+"", totalSteps+"");
                        Statistics distance = new Statistics("Distance", String.format("%.2f", averageDistance)+" km", String.format("%.2f", totalDistance)+" km");
                        Statistics calories = new Statistics("Calories", averageCalories+" kcal", totalCalories+" kcal");

                        statisticsList.add(steps);
                        statisticsList.add(distance);
                        statisticsList.add(calories);
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
    public void getStepsForBarGraph(String id, Bargraph bargraph, BarChart barChart){
        // populates the bar graph with steps
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.orderByChild("userId").equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        int count = 0;
                        ArrayList<BarEntry> data = new ArrayList<>();
                        for (DataSnapshot ds : task.getResult().getChildren()){
                            Run r = ds.getValue(Run.class);
                            data.add(new BarEntry(count, r.getRunSteps()));
                            count++;
                        }
                        bargraph.setChart(data,barChart, Color.BLACK);
                    }
                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
    public void getCaloriesForBarGraph(String id, Bargraph bargraph, BarChart barChart){
        // populates the bar graph with calories
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.orderByChild("userId").equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        int count = 0;
                        ArrayList<BarEntry> data = new ArrayList<>();
                        for (DataSnapshot ds : task.getResult().getChildren()){
                            Run r = ds.getValue(Run.class);
                            data.add(new BarEntry(count, r.getRunCalories()));
                            count++;
                        }
                        bargraph.setChart(data,barChart, Color.BLACK);
                    }
                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
    public void getPacesForBarGraph(String id, Bargraph bargraph, BarChart barChart){
        // populates the bar graph with paces
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.orderByChild("userId").equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        int count = 0;
                        ArrayList<BarEntry> data = new ArrayList<>();
                        for (DataSnapshot ds : task.getResult().getChildren()){
                            Run r = ds.getValue(Run.class);
                            data.add(new BarEntry(count, (float) r.getRunPace()));
                            count++;
                        }
                        bargraph.setChart(data,barChart, Color.BLACK);
                    }
                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
    public void getRunPoints(String id, GoogleMap googleMap, Context context){
        // Sets up map and plots the run
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        Run r = ds.getValue(Run.class);
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

                        for (int i = 0; i <= r.getPoints().size() - 1; i++) {
                            LatLng latLng = new LatLng(r.getPoints().get(i).latitude, r.getPoints().get(i).longitude);// in this line put you lat and long
                            builder.include(latLng);  //add latlng to builder
                        }
                        LatLngBounds bounds = builder.build();

                        int padding = 150; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                        googleMap.moveCamera(cu);

                        MapsFragment.setPoints(googleMap, r.getPoints());
                    }
                    else{
                        ((Activity) context).finish();
                    }
                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                    ((Activity) context).finish();
                }
            }
        });
    }
    public void getRuns(String id, ArrayList<Run> rList, RunsAdapter mAdapter, Context context){
        // Get all the runs current user has
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.orderByChild("userId").equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ConstraintLayout progress = ((Activity) context).findViewById(R.id.progress_bar);
                if (progress != null){
                    // Remove progress bar
                    progress.setVisibility(View.INVISIBLE);
                }
                if (task.isSuccessful()) {
                    rList.clear();
                    if (task.getResult().exists()){
                        for (DataSnapshot ds : task.getResult().getChildren()){
                            Run r = ds.getValue(Run.class);
                            rList.add(0, r);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    else{
                        ConstraintLayout img = ((Activity) context).findViewById(R.id.invalid_runs);
                        if (img != null){
                            // Display "No Runs" message
                            img.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else {
                    ConstraintLayout img = ((Activity) context).findViewById(R.id.error);
                    if (img != null){
                        // Display error message
                        img.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
    }
}
