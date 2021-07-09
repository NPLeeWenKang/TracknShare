package sg.edu.np.tracknshare.handlers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.RunsAdapter;
import sg.edu.np.tracknshare.adapters.SearchItemAdapter;
import sg.edu.np.tracknshare.fragments.SearchFragment;
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
        Calendar calendar = Calendar.getInstance();
        long timeMilli = calendar.getTimeInMillis();
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.child(""+timeMilli).setValue(r);
    }
    public void GetRuns(String id, ArrayList<Run> rList, RunsAdapter mAdapter){
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.orderByChild("userId").equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    rList.clear();
                    if (task.getResult().exists()){
                        for (DataSnapshot ds : task.getResult().getChildren()){
                            Run r = ds.getValue(Run.class);
                            rList.add(r);
                        }
                        Log.d("KEYCODE", "onKey: UPDATE");
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        mAdapter.notifyDataSetChanged();
                    }

                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
}
