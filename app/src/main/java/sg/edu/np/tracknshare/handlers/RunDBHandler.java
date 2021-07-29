package sg.edu.np.tracknshare.handlers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.tracknshare.MapsFragment;
import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.RunsAdapter;
import sg.edu.np.tracknshare.adapters.SearchItemAdapter;
import sg.edu.np.tracknshare.fragments.SearchFragment;
import sg.edu.np.tracknshare.models.MyLatLng;
import sg.edu.np.tracknshare.models.Run;
import sg.edu.np.tracknshare.models.User;
import java.util.Calendar;

public class RunDBHandler {
    private final String dbUrl = "https://testapp-bc30f-default-rtdb.asia-southeast1.firebasedatabase.app";
    private FirebaseDatabase database = FirebaseDatabase.getInstance(dbUrl);
    Context context;

    public RunDBHandler(Context c){
        context = c;
    }
    public void AddRun(Run r){
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
//    public void GetMyRunStatistics(String id, Context context){
//        ArrayList<Run> rList = new ArrayList<>();
//        DatabaseReference dbRef = database.getReference("/runs");
//        dbRef.orderByChild("userId").equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                    if (task.getResult().exists()){
//                        int previousD
//                        for (DataSnapshot ds : task.getResult().getChildren()){
//                            Run r = ds.getValue(Run.class);
//                            rList.add(0, r);
//                        }
//                    }
//                }
//                else {
//                    Log.d("firebase", "Error getting data", task.getException());
//                    ConstraintLayout img = ((Activity) context).findViewById(R.id.error);
//                }
//            }
//        });
//    }
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
    public void GetRuns(String id, ArrayList<Run> rList, RunsAdapter mAdapter, Context context){
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
