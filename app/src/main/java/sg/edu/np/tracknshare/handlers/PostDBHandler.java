package sg.edu.np.tracknshare.handlers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.PostsAdapter;
import sg.edu.np.tracknshare.adapters.ProfilePostsAdapter;
import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.models.Run;
import sg.edu.np.tracknshare.models.User;
import sg.edu.np.tracknshare.models.UserPostViewModel;

import java.util.Collections;
import java.util.Iterator;

public class PostDBHandler {
    private final String dbUrl = "https://testapp-bc30f-default-rtdb.asia-southeast1.firebasedatabase.app";
    private FirebaseDatabase database = FirebaseDatabase.getInstance(dbUrl);
    Context context;

    public PostDBHandler(Context c){
        context = c;
    }
    public void AddPost(Post p){
        DatabaseReference dbRef = database.getReference("/posts");
        dbRef.child(""+p.getPostId()).setValue(p);
    }
    public void getCount(String id, TextView numPost){
        // Gets the number of runs for a specific user id
        DatabaseReference dbRef = database.getReference("/posts");
        dbRef.orderByChild("userId").equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        numPost.setText(""+task.getResult().getChildrenCount());
                    }

                }
            }
        });
    }
    public void removeLike(String postId){
        // Removes a like from selected post
        DatabaseReference dbRef = database.getReference("/posts");
        dbRef.child(postId).child("likes").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentValue - 1);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(
                    DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                Log.d("LIKES", "onComplete: ");
            }
        });
    }
    public void addLike(String postId){
        // Adds a like from selected post
        DatabaseReference dbRef = database.getReference("/posts");
        dbRef.child(postId).child("likes").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentValue + 1);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(
                    DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                Log.d("LIKES", "onComplete: ");
            }
        });
    }
    public void isLiked(ImageView imageView, TextView textView, String postId){
        // Checks if user has liked selected post
        AuthHandler authHandler = new AuthHandler(context);
        database.getReference("/user").child(authHandler.getCurrentUser().getUid()).child("likedId").child(postId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot ds = task.getResult();
                    if (ds.exists()){
                        textView.setText("1");
                        imageView.setColorFilter(((Activity) context).getResources().getColor(R.color.red)); // Add tint color
                    }else{
                        textView.setText("0");
                        imageView.setColorFilter(null); // Remove tint color
                    }
                }
            }
        });
    }
    // -------------------------------------------
    public void GetFriendsPost(ArrayList<UserPostViewModel> upList, PostsAdapter mAdapter, String userId){
        // Gets friends' post and refreshed the recycler view adaptor
        upList.clear();
        DatabaseReference dbRef = database.getReference("/user");

        // Get current user's friends
        dbRef.child(userId).child("friendsId").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ConstraintLayout progress = ((Activity) context).findViewById(R.id.progress_bar);
                if (progress != null){
                    // Remove progress bar
                    progress.setVisibility(View.INVISIBLE);
                }
                if (task.isSuccessful()) {
                    upList.clear();
                    if (task.getResult().exists()){
                        ArrayList<String> friends = new ArrayList<>();
                        for (DataSnapshot ds : task.getResult().getChildren()){
                            // Add friend to arraylist
                            friends.add(ds.getKey());
                        }
                        long totalFriends = friends.size();
                        long currentFiendCount = 1;
                        for (String friendId : friends){
                            GetFriends(upList, mAdapter, friendId, totalFriends, currentFiendCount);
                            currentFiendCount++;
                        }
                    }

                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                    ConstraintLayout img = ((Activity) context).findViewById(R.id.error);
                    if (img != null){
                        // Display error message
                        img.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
    }
    private void GetFriends(ArrayList<UserPostViewModel> upList, PostsAdapter mAdapter, String friendId, long totalFriends, long currentFriendCount){
        // Gets
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(friendId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ConstraintLayout progress = ((Activity) context).findViewById(R.id.progress_bar);
                if (progress != null){
                    progress.setVisibility(View.INVISIBLE);
                }
                if (task.isSuccessful()) {
                    DataSnapshot ds = task.getResult();
                    User u = ds.getValue(User.class);
                    getPosts(upList, mAdapter, u, totalFriends, currentFriendCount);
                }
            }
        });
    }
    private void getPosts(ArrayList<UserPostViewModel> upList, PostsAdapter mAdapter, User u, long totalFriends, long currentFriendCount){
        // Get friends' posts
        DatabaseReference dbRef = database.getReference("/posts");
        dbRef.orderByChild("userId").equalTo(u.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ConstraintLayout progress = ((Activity) context).findViewById(R.id.progress_bar);
                if (progress != null){
                    progress.setVisibility(View.INVISIBLE);
                }
                if (task.isSuccessful()) {
                    DataSnapshot ds = task.getResult();
                    for(DataSnapshot smallDS : ds.getChildren()){
                        Post p = smallDS.getValue(Post.class);
                        UserPostViewModel uPVM = new UserPostViewModel(p,u);
                        upList.add(0, uPVM);
                    }
                    if (totalFriends == currentFriendCount){
                        ArrayList<UserPostViewModel> temp = new ArrayList<>(upList);
                        ArrayList<String> sortedPostId = new ArrayList<>();
                        upList.clear();

                        // Sorts the posts based on time
                        for (UserPostViewModel upOBJ : temp){
                            // Places post ids in a list
                            sortedPostId.add(upOBJ.getPost().getPostId());
                        }
                        Collections.sort(sortedPostId); // sorts then in ascending order
                        Collections.reverse(sortedPostId); // reverse lists so that list is in descending order
                        for (String postId : sortedPostId){
                            Iterator<UserPostViewModel> iter = temp.iterator();
                            while (iter.hasNext()){
                                UserPostViewModel upOBJ = iter.next();
                                if (upOBJ.getPost().getPostId().equals(postId)){
                                    // Adds sorted posts into the list
                                    upList.add(upOBJ);
                                    iter.remove();
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    // -------------------------------------------
    public void getGeneralPosts(ArrayList<UserPostViewModel> upList, PostsAdapter mAdapter){
        // Gets all posts
        DatabaseReference dbRef = database.getReference("/posts");
        dbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ConstraintLayout progress = ((Activity) context).findViewById(R.id.progress_bar);
                if (progress != null){
                    progress.setVisibility(View.INVISIBLE);
                }
                if (task.isSuccessful()) {
                    upList.clear();
                    if (task.getResult().exists()){
                        long length = task.getResult().getChildrenCount();
                        long currentLength = 1;
                        for (DataSnapshot ds : task.getResult().getChildren()){
                            Post p = ds.getValue(Post.class);
                            GetUser(p.getUserId(), p, length,currentLength, upList, mAdapter);
                            currentLength++;
                        }
                    }
                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                    ConstraintLayout img = ((Activity) context).findViewById(R.id.error);
                    if (img != null){
                        img.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
    }
    public void GetUserPosts(ArrayList<UserPostViewModel> upList, ProfilePostsAdapter mAdapter, String id){
        // Gets post from a specific user
        DatabaseReference dbRef = database.getReference("/posts");
        dbRef.orderByChild("userId").equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    upList.clear();
                    if (task.getResult().exists()){
                        long length = task.getResult().getChildrenCount();
                        long currentLength = 1;
                        for (DataSnapshot ds : task.getResult().getChildren()){
                            Post p = ds.getValue(Post.class);
                            GetUser(p.getUserId(), p, length,currentLength, upList, mAdapter);
                            currentLength++;
                        }
                    }

                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
    private void GetUser(String userId, Post p, long length, long currentLength, ArrayList<UserPostViewModel> upList, RecyclerView.Adapter mAdapter){
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        User u = ds.getValue(User.class);
                        UserPostViewModel uPVM = new UserPostViewModel(p,u);
                        upList.add(0, uPVM);
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
    // -------------------------------------------
    public void getPost(String postId, Context context){
        // Get a post for Comments Activity
        DatabaseReference dbRef = database.getReference("/posts");
        dbRef.child(postId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        Post p = ds.getValue(Post.class);

                        TextView postDate = ((Activity) context).findViewById(R.id.workoutDate);
                        TextView likes = ((Activity) context).findViewById(R.id.likes);
                        TextView postCaption = ((Activity) context).findViewById(R.id.postCaption);
                        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy h:mma");

                        if (postDate  != null && likes != null && postCaption != null){
                            postDate.setText(dateFormat.format(p.getPostDate()));
                            likes.setText(""+p.getLikes());
                            postCaption.setText(""+p.getCaption());
                        }

                        getRun(p.getRunId(), context);
                        getUser(p.getUserId(), context);
                    }

                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
    private void getRun(String runId, Context c){
        // Gets the information for a run
        DatabaseReference dbRef = database.getReference("/runs");
        dbRef.child(runId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        Run r = ds.getValue(Run.class);
                        ImageView runImage = ((Activity) context).findViewById(R.id.post_Image);
                        StorageHandler storageHandler = new StorageHandler();
                        if (runImage != null){
                            storageHandler.LoadFileToApp(r.getImageId(), context, runImage);
                        }
                        TextView distance = ((Activity) context).findViewById(R.id.run_distance_post);
                        TextView pace = ((Activity) context).findViewById(R.id.run_pace_post);
                        TextView timing = ((Activity) context).findViewById(R.id.run_timing_post);
                        TextView calories = ((Activity) context).findViewById(R.id.run_calories_post);

                        if (distance  != null && pace != null && timing != null && calories != null){
                            distance.setText("" + String.format("%.4f", r.getRunDistance()));
                            pace.setText("" + String.format("%.2f", r.getRunPace()) + " m/s");
                            timing.setText(""+r.getRunDuration());
                            calories.setText(""+r.getRunCalories());
                        }
                    }
                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
    private void getUser(String userId, Context context){
        // Gets the information for a user
        DatabaseReference dbRef = database.getReference("/user");
        dbRef.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()){
                        DataSnapshot ds = task.getResult();
                        User u = ds.getValue(User.class);
                        ImageView avatarImage = ((Activity) context).findViewById(R.id.avatarIMG);
                        TextView name = ((Activity) context).findViewById(R.id.username);
                        StorageHandler storageHandler = new StorageHandler();

                        if (avatarImage != null && name != null){
                            storageHandler.LoadProfileImageToApp(u.getId(), context, avatarImage);
                            name.setText(u.getUserName());
                        }
                    }
                }
                else {
                    Log.d("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
    // -------------------------------------------
}
