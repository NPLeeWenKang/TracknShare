package sg.edu.np.tracknshare.handlers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import sg.edu.np.tracknshare.adapters.ProfilePostsAdapter;
import sg.edu.np.tracknshare.adapters.RunsAdapter;
import sg.edu.np.tracknshare.adapters.SearchItemAdapter;
import sg.edu.np.tracknshare.fragments.SearchFragment;
import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.models.Run;
import sg.edu.np.tracknshare.models.User;
import sg.edu.np.tracknshare.models.UserPostViewModel;

import java.util.Calendar;

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
    public void GetPosts(ArrayList<UserPostViewModel> upList, PostsAdapter mAdapter){
        DatabaseReference dbRef = database.getReference("/posts");
        dbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
    public void GetUserPosts(ArrayList<UserPostViewModel> upList, ProfilePostsAdapter mAdapter, String id){
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
}
