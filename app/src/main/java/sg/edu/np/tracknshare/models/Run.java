package sg.edu.np.tracknshare.models;

import java.sql.Time;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class Run {
    private String UserId;
    private String RunId;
    private String RunDate; //to be formatted with SimpleDateFormatter class
    private long RunDuration; //timing calculated in milliseconds
    private double RunDistance;
    private double RunCalories;
    private double RunPace;
    private ArrayList<LatLng> points;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getRunId() {
        return RunId;
    }

    public void setRunId(String runId) {
        RunId = runId;
    }

    public String getRunDate() {
        return RunDate;
    }

    public void setRunDate(String runDate) {
        RunDate = runDate;
    }

    public long getRunDuration() {
        return RunDuration;
    }

    public void setRunDuration(long runDuration) {
        RunDuration = runDuration;
    }

    public double getRunDistance() {
        return RunDistance;
    }

    public void setRunDistance(double runDistance) {
        RunDistance = runDistance;
    }

    public double getRunCalories() {
        return RunCalories;
    }

    public void setRunCalories(double runCalories) {
        RunCalories = runCalories;
    }

    public double getRunPace() {
        return RunPace;
    }

    public void setRunPace() {
        RunPace = (RunDuration/1000/60)/RunDistance; //pace is read as x minutes/km
        //(RunDuration/1000/60) --> milliseconds to minutes

    }

    public ArrayList<LatLng> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<LatLng> points) {
        this.points = points;
    }

    public Run(String userId, String runId, String runDate, long runDuration, double runDistance, double runCalories, double runPace, ArrayList<LatLng> points) {
        UserId = userId;
        RunId = runId;
        RunDate = runDate;
        RunDuration = runDuration;
        RunDistance = runDistance;
        RunCalories = runCalories;
        RunPace = runPace;
        this.points = points;
    }

    public Run() { }
}
