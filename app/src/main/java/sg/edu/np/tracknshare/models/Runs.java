package sg.edu.np.tracknshare.models;

import java.sql.Time;

public class Runs {
    private int RunId;
    private String RunDate; //to be formatted with SimpleDateFormatter class
    private long RunDuration; //timing calculated in milliseconds
    private double RunDistance;
    private double RunCalories;
    private double RunPace;

    public int getRunId() {
        return RunId;
    }

    public void setRunId(int runId) {
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
}
