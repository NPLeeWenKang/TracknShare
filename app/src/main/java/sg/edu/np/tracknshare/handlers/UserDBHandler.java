package sg.edu.np.tracknshare.handlers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
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
import sg.edu.np.tracknshare.adapters.PostsAdapter;
import sg.edu.np.tracknshare.adapters.SearchItemAdapter;
import sg.edu.np.tracknshare.fragments.SearchFragment;
import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.models.User;
import sg.edu.np.tracknshare.models.UserPostViewModel;

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
                        Log.d("KEYCODE", "onKey: UPDATE "+username);
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        mAdapter.notifyDataSetChanged();
                    }else{
                        mAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
    public void GetUserDetails(String id, Context context){
        Log.d("FIREBASE123", "GetUserDetails: "+id);
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        User u = ds.getValue(User.class);
                        u.setFriendsId(null);
                        TextView name = ((Activity) context).findViewById(R.id.profileName);
                        if (name != null){
                            name.setText(u.getUserName());
                            TextView accountCreationDate = ((Activity) context).findViewById(R.id.accountCreationDate);
                            if (accountCreationDate == null){
                                accountCreationDate.setText("No date recoreded.");
                            }else{
                                accountCreationDate.setText(u.getAccountCreationDate());
                            }
                            Log.d("FIREBASE123", "Error getting data"+ u.getUserName());
                        }
                    }
                }
                else {
                    Log.d("FIREBASE123", "Error getting data", task.getException());
                }
            }
        });
    }

    public void GetUserDetailsIntoSettings(String id, Context context){
        Log.d("FIREBASE123", "GetUserDetails: "+id);
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        User u = ds.getValue(User.class);
                        u.setFriendsId(null);
                        EditText name = ((Activity) context).findViewById(R.id.edit_username);
                        EditText description = ((Activity) context).findViewById(R.id.edit_description);
                        if (name != null && description != null){
                            name.setText(u.getUserName());
                            description.setText(u.getEmail());
                            Log.d("FIREBASE123", "Error getting data"+ u.getUserName());
                        }
                    }
                }
                else {
                    Log.d("FIREBASE123", "Error getting data", task.getException());
                }
            }
        });
    }
    public void UpdateUserDetails(User u){
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(u.getId()).setValue(u);
    }
    public void GetFriendsList(String id, ArrayList<User> uList, SearchItemAdapter mAdapter){
        uList.clear();
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("ADDFRIEND", "onComplete: ");
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        User u = ds.getValue(User.class);
                        Log.d("ADDFRIEND", "onComplete: "+ u.getFriendsId());
                        if (u.getFriendsId() != null){
                            long length = u.getFriendsId().size();
                            long currentLength = 1;
                            for (int i = 0; i <= u.getFriendsId().size()-1;i++){
                                Log.d("ADDFRIEND", "onComplete: "+u.getFriendsId().get(i));
                                GetFriend(u.getFriendsId().get(i), length, currentLength, uList, mAdapter);
                                currentLength++;
                            }
                        }
                    }
                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
    private void GetFriend(String userId, long length, long currentLength, ArrayList<User> uList, SearchItemAdapter mAdapter){
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        User u = ds.getValue(User.class);
                        uList.add(0, u);
                        Log.d("ADDFRIEND", "onComplete: "+ (length - currentLength));
                        if (length - currentLength == 0){
                            mAdapter.notifyDataSetChanged();
                        }

                    }

                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
}
