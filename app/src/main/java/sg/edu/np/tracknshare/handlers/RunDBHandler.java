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
        Calendar calendar = Calendar.getInstance();
        long timeMilli = calendar.getTimeInMillis();
        r.setRunId(""+timeMilli);
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.child(""+timeMilli).setValue(r);
    }
}
