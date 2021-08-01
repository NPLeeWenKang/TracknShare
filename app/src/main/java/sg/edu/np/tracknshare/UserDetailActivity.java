package sg.edu.np.tracknshare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.edu.np.tracknshare.adapters.ProfilePostsAdapter;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.PostDBHandler;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.handlers.UserDBHandler;
import sg.edu.np.tracknshare.models.UserPostViewModel;

public class UserDetailActivity extends AppCompatActivity {
    private ArrayList<UserPostViewModel> upList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent receivingEnd = getIntent();
        String id = receivingEnd.getStringExtra("id");

        UserDBHandler db = new UserDBHandler(this);
        AuthHandler auth = new AuthHandler(this);
        db.getUserDetails(id, this);

        StorageHandler storageHandler = new StorageHandler();
        ImageView imageView = findViewById(R.id.avatarIMG);
        storageHandler.loadProfileImageToApp(id, this, imageView);

        RecyclerView recyclerView = findViewById(R.id.rv_profile_post);
        ProfilePostsAdapter mAdapter = new ProfilePostsAdapter(this, upList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        PostDBHandler postDBHandler = new PostDBHandler(this);
        postDBHandler.getUserPosts(upList, mAdapter, id);

        Button friends_btn = findViewById(R.id.friends_btn);
        friends_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (friends_btn.getText().toString().equals("Remove from Friends")){
                    Toast.makeText(UserDetailActivity.this, "From from Friends", Toast.LENGTH_SHORT).show();
                    db.removeFriends(auth.getCurrentUser().getUid(), id);
                    friends_btn.setText("Add to Friends");
                }else{
                    Toast.makeText(UserDetailActivity.this, "Added to Friends", Toast.LENGTH_SHORT).show();
                    db.addToFriends(auth.getCurrentUser().getUid(), id);
                    friends_btn.setText("Remove from Friends");
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
    }
}