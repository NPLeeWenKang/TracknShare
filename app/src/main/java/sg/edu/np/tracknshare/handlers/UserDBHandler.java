package sg.edu.np.tracknshare.handlers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.SearchItemAdapter;
import sg.edu.np.tracknshare.models.User;

public class UserDBHandler {
    private final String dbUrl = "https://testapp-bc30f-default-rtdb.asia-southeast1.firebasedatabase.app";
    private FirebaseDatabase database = FirebaseDatabase.getInstance(dbUrl);
    Context context;
    public  UserDBHandler(Context c){
        context = c;
    }
    public void addUser(User u){
        // adds new user to db
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(u.getId()).setValue(u);
    }
    public void addLike(String userId, String postId){
        // adds which posts the user has liked
        // prevents users from liking the same post twice
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).child("likedId").child(postId).setValue(true);
    }
    public void removeLike(String userId, String postId){
        // removes which posts the user has liked
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).child("likedId").child(postId).setValue(null);
    }

    public void searchUser(String username, ArrayList<User> uList, SearchItemAdapter mAdapter){
        // search users based on their username
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
    public void getUserDetails(String id, Context context){
        // get other user's details
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        // sets the TextView and button for other user's profile page
                        DataSnapshot ds = task.getResult();
                        User u = ds.getValue(User.class);
                        u.setFriendsId(null);
                        TextView numPosts = ((Activity) context).findViewById(R.id.numPosts);
                        TextView numRuns = ((Activity) context).findViewById(R.id.numRuns);
                        TextView accountCreationDate = ((Activity) context).findViewById(R.id.accountCreationDate);
                        if (accountCreationDate != null && numPosts != null && numRuns != null){
                            // populates top details bar
                            ((AppCompatActivity) context).getSupportActionBar().setTitle(u.getUserName());
                            PostDBHandler postDBHandler = new PostDBHandler(context);
                            postDBHandler.getCount(u.getId(),numPosts);
                            RunDBHandler runDBHandler = new RunDBHandler(context);
                            runDBHandler.getCount(u.getId(),numRuns);
                            if (u.getAccountCreationDate() != null)
                            {
                                accountCreationDate.setText(u.getAccountCreationDate());
                            }
                        }
                        isFriends(id, context); // checks whether current user is friends with selected user
                    }
                }
                else {
                    Log.d("FIREBASE123", "Error getting data", task.getException());
                }
            }
        });
    }
    private void isFriends(String id, Context context){
        // checks if current user is friends with selected user
        AuthHandler authHandler = new AuthHandler(context);
        database.getReference("/user").child(authHandler.getCurrentUser().getUid()).child("friendsId").child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot ds = task.getResult();
                    if (ds.exists()){ // friends
                        TextView tv = ((Activity) context).findViewById(R.id.friends_btn);
                        tv.setText("Remove from Friends");
                    }else{ // not friends
                        TextView tv = ((Activity) context).findViewById(R.id.friends_btn);
                        tv.setText("Add to Friends");
                    }
                }
            }
        });


    }

    public void getMyDetails(String id, Context context){
        // gets current user's details
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
                            // populates top details bar
                            PostDBHandler postDBHandler = new PostDBHandler(context);
                            postDBHandler.getCount(u.getId(),numPosts);
                            RunDBHandler runDBHandler = new RunDBHandler(context);
                            runDBHandler.getCount(u.getId(),numRuns);
                            if (u.getAccountCreationDate() != null)
                            {
                                accountCreationDate.setText(u.getAccountCreationDate());
                            }
                        }
                    }
                }
                else {
                    Log.d("FIREBASE123", "Error getting data", task.getException());
                }
            }
        });
    }
    public void getUserDetailsIntoSettings(String id, Context context){
        // displays user details in settings
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        // populates EditText with current user details
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
                        }
                        StorageHandler storageHandler = new StorageHandler();
                        storageHandler.loadProfileImageToApp(u.getId(), context, imageView);
                    }
                }
                else {
                    Log.d("FIREBASE123", "Error getting data", task.getException());
                }
            }
        });
    }
    public void addToFriends(String userId, String friendId){
        // adds selected user to friends
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).child("friendsId").child(friendId).setValue(true);
    }
    public void removeFriends(String userId, String friendId){
        // removes selected user from friends
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).child("friendsId").child(friendId).setValue(null);
    }
    public void updateUserDetails(User u){
        // update user details
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(u.getId()).child("userName").setValue(u.getUserName());
        dbRef.child(u.getId()).child("mass").setValue(u.getMass());
        dbRef.child(u.getId()).child("height").setValue(u.getHeight());
    }
    // ---------------------------------
    public void getFriendsList(String id, ArrayList<User> uList, SearchItemAdapter mAdapter){
        // get and display all users in friends list
        uList.clear();
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        User u = ds.getValue(User.class);
                        if (u.getFriendsId() != null){
                            long length = u.getFriendsId().size();
                            long currentLength = 1;
                            ArrayList<String> sList = new ArrayList<>(u.getFriendsId().keySet());
                            for (int i = 0; i <= sList.size()-1;i++){
                                // get friends details
                                getFriend(sList.get(i), length, currentLength, uList, mAdapter);
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
    private void getFriend(String userId, long length, long currentLength, ArrayList<User> uList, SearchItemAdapter mAdapter){
        // get friends details
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        User u = ds.getValue(User.class);
                        uList.add(0, u);
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
