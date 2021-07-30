package sg.edu.np.tracknshare.models;

import java.util.ArrayList;

public class Run {
    private String UserId;
    private String RunId;
    private long RunDate; //to be formatted with SimpleDateFormatter class
    private String ImageId;
    private long RunDuration; //timing calculated in milliseconds
    private double RunDistance;
    private int RunCalories;
    private double RunPace;
    private int RunSteps;
    private ArrayList<MyLatLng> points;

    public String getImageId() {
        return ImageId;
    }

    public void setImageId(String imageId) {
        ImageId = imageId;
    }

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

    public long getRunDate() {
        return RunDate;
    }

    public void setRunDate(long runDate) {
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

    public int getRunCalories() {
        return RunCalories;
    }

    public void setRunCalories(int runCalories) {
        RunCalories = runCalories;
    }

    public double getRunPace() {
        return RunPace;
    }

    public void setRunPace(double runPace) {
        RunPace = runPace;
    }

    public ArrayList<MyLatLng> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<MyLatLng> points) {
        this.points = points;
    }

    public int getRunSteps() {
        return RunSteps;
    }

    public void setRunSteps(int runSteps) {
        RunSteps = runSteps;
    }

    public Run(String userId, String runId, long runDate, String imageId, long runDuration, double runDistance, int runCalories, double runPace, int runSteps, ArrayList<MyLatLng> points) {
        UserId = userId;
        RunId = runId;
        RunDate = runDate;
        ImageId = imageId;
        RunDuration = runDuration;
        RunDistance = runDistance;
        RunCalories = runCalories;
        RunPace = runPace;
        RunSteps = runSteps;
        this.points = points;
    }

    public Run() { }
}
