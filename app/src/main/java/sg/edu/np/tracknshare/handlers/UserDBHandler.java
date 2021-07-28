package sg.edu.np.tracknshare.handlers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        Log.d("SAVEUSERDB", "AddUser: "+u.getAccountCreationDate());
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(u.getId()).setValue(u);
    }
    public void addLike(String userId, String postId){
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).child("likedId").child(postId).setValue(true);
    }
    public void removeLike(String userId, String postId){
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).child("likedId").child(postId).setValue(null);
    }

    public void SearchUser(String username, ArrayList<User> uList, SearchItemAdapter mAdapter){
        mAdapter.notifyItemRangeRemoved(0,uList.size());
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
                            if (u.getUserName() != username){
                                u.setFriendsId(null);
                                uList.add(u);
                            }
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
                        TextView numPosts = ((Activity) context).findViewById(R.id.numPosts);
                        TextView numRuns = ((Activity) context).findViewById(R.id.numRuns);
                        TextView accountCreationDate = ((Activity) context).findViewById(R.id.accountCreationDate);
                        if (accountCreationDate != null && numPosts != null && numRuns != null){
                            ((AppCompatActivity) context).getSupportActionBar().setTitle(u.getUserName());
                            PostDBHandler postDBHandler = new PostDBHandler(context);
                            postDBHandler.getCount(u.getId(),numPosts);
                            RunDBHandler runDBHandler = new RunDBHandler(context);
                            runDBHandler.getCount(u.getId(),numRuns);
                            if (u.getAccountCreationDate() != null)
                            {
                                accountCreationDate.setText(u.getAccountCreationDate());
                            }
                            Log.d("FIREBASE123", "Error getting data"+ u.getAccountCreationDate());
                        }
                        isFriends(id, context);
                    }
                }
                else {
                    Log.d("FIREBASE123", "Error getting data", task.getException());
                }
            }
        });
    }
    private void isFriends(String id, Context context){
        AuthHandler authHandler = new AuthHandler(context);
        database.getReference("/user").child(authHandler.GetCurrentUser().getUid()).child("friendsId").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot ds = task.getResult();
                    if (ds.exists()){
                        TextView tv = ((Activity) context).findViewById(R.id.friends_btn);
                        tv.setText("Remove from Friends");
                    }else{
                        TextView tv = ((Activity) context).findViewById(R.id.friends_btn);
                        tv.setText("Add to Friends");
                    }
                }
            }
        });


    }

    public void GetMyDetails(String id, Context context){
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
                        TextView numPosts = ((Activity) context).findViewById(R.id.numPosts);
                        TextView numRuns = ((Activity) context).findViewById(R.id.numRuns);
                        TextView accountCreationDate = ((Activity) context).findViewById(R.id.accountCreationDate);
                        if (accountCreationDate != null){
                            PostDBHandler postDBHandler = new PostDBHandler(context);
                            postDBHandler.getCount(u.getId(),numPosts);
                            RunDBHandler runDBHandler = new RunDBHandler(context);
                            runDBHandler.getCount(u.getId(),numRuns);
                            if (u.getAccountCreationDate() != null)
                            {
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
    public void GetMyStatistics(String id, Context context){
        Log.d("FIREBASE123", "GetUserDetails: "+id);
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        User u = ds.getValue(User.class);

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
                        EditText mass = ((Activity) context).findViewById(R.id.edit_mass);
                        EditText height = ((Activity) context).findViewById(R.id.edit_height);
                        ImageView imageView = ((Activity) context).findViewById(R.id.add_profile_image);
                        if (name != null){
                            name.setText(u.getUserName());
                            mass.setText(u.getMass());
                            height.setText(u.getHeight());
                            Log.d("FIREBASE123", "Error getting data"+ u.getUserName());
                        }
                        StorageHandler storageHandler = new StorageHandler();
                        storageHandler.LoadProfileImageToApp(u.getId(), context, imageView);
                    }
                }
                else {
                    Log.d("FIREBASE123", "Error getting data", task.getException());
                }
            }
        });
    }
    public void AddToFriends(String userId, String friendId){
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).child("friendsId").child(friendId).setValue(true);
    }
    public void RemoveFriends(String userId, String friendId){
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).child("friendsId").child(friendId).setValue(null);
    }
    public void UpdateUserDetails(User u){
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(u.getId()).child("userName").setValue(u.getUserName());
        dbRef.child(u.getId()).child("mass").setValue(u.getMass());
        dbRef.child(u.getId()).child("height").setValue(u.getHeight());
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
//                        ArrayList<String> temp = new ArrayList<>();
//                        for (DataSnapshot friendsDS :ds.child("friendsId").getChildren()){
//                            temp.add(friendsDS.getKey());
//                        }
//                        u.setFriendsId(temp);
                        Log.d("ADDFRIEND", "onComplete: "+ u.getFriendsId());
                        if (u.getFriendsId() != null){
                            long length = u.getFriendsId().size();
                            long currentLength = 1;
                            ArrayList<String> sList = new ArrayList<>(u.getFriendsId().keySet());
                            for (int i = 0; i <= sList.size()-1;i++){
                                Log.d("ADDFRIEND", "onComplete: "+u.getFriendsId().get(i));
                                GetFriend(sList.get(i), length, currentLength, uList, mAdapter);
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
