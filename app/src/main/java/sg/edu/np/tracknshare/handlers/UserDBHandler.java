package sg.edu.np.tracknshare.handlers;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.tracknshare.adapters.SearchItemAdapter;
import sg.edu.np.tracknshare.fragments.SearchFragment;
import sg.edu.np.tracknshare.models.User;

public class UserDBHandler {
    private final String dbUrl = "https://testapp-bc30f-default-rtdb.asia-southeast1.firebasedatabase.app";
    private FirebaseDatabase database = FirebaseDatabase.getInstance(dbUrl);
    Context context;
    public  UserDBHandler(Context c){
        context = c;
    }
    public void AddUser(User u){
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(u.getId()).setValue(u);
    }
    public void SearchUser(String username, ArrayList<User> uList, SearchItemAdapter mAdapter){
        uList.clear();
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.orderByChild("userName").startAt(username).endAt(username+"\uf8ff").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    uList.clear();
                    if (task.getResult().exists()){
                        for (DataSnapshot ds : task.getResult().getChildren()){
                            User u = ds.getValue(User.class);
                            u.setFriendsId(null);
                            uList.add(u);
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
//    public User GetUser(String id){
//
//    }
}
