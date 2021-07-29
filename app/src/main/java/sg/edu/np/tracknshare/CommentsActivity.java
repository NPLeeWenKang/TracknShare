package sg.edu.np.tracknshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import sg.edu.np.tracknshare.adapters.CommentsAdapter;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.PostDBHandler;
import sg.edu.np.tracknshare.handlers.UserDBHandler;
import sg.edu.np.tracknshare.models.Comment;
import sg.edu.np.tracknshare.viewholders.PostViewHolder;

public class CommentsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent receivedIntent = getIntent();
        String postId = receivedIntent.getStringExtra("postId");
        String runId = receivedIntent.getStringExtra("runId");
        String userId = receivedIntent.getStringExtra("userId");
        if (postId == null && runId == null){
            finish();
        }
        Log.d("COMMENTSACTI", "onCreate: "+postId);
        PostDBHandler postDBHandler = new PostDBHandler(this);
        postDBHandler.getPost(postId, this);

        ImageView mapImage = findViewById(R.id.post_Image);
        ImageView userImage = findViewById(R.id.avatarIMG);
        TextView username = findViewById(R.id.username);
        TextView date = findViewById(R.id.workoutDate);

        ImageView likeIcon = findViewById(R.id.likesImg);
        TextView isLiked = findViewById(R.id.isLiked);
        TextView likes = findViewById(R.id.likes);

        likeIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (isLiked.getText().toString().equals("0"))
                {
                    addOneLike(likes, isLiked, likeIcon, postId);
                }else{
                    removeOneLike(likes, isLiked, likeIcon,postId);
                }
            }


        });

        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentsActivity.this, FullMapActivity.class);
                intent.putExtra("mapType", "movable");
                intent.putExtra("id", runId);
                startActivity(intent);
            }
        });
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentsActivity.this, UserDetailActivity.class);
                intent.putExtra("id", ""+userId);
                startActivity(intent);
                overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
            }
        });
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentsActivity.this, UserDetailActivity.class);
                intent.putExtra("id", ""+userId);
                startActivity(intent);
                overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentsActivity.this, UserDetailActivity.class);
                intent.putExtra("id", ""+userId);
                startActivity(intent);
                overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
            }
        });
    }
    public void addOneLike(TextView likes, TextView isLiked, ImageView likeIcon, String postId){
        likes.setText(""+(Integer.parseInt(likes.getText().toString())+1));
        likeIcon.setColorFilter(getResources().getColor(R.color.red)); // Add tint color
        PostDBHandler postDBHandler = new PostDBHandler(CommentsActivity.this);
        postDBHandler.addLike(postId);
        UserDBHandler userDBHandler = new UserDBHandler(CommentsActivity.this);
        AuthHandler authHandler = new AuthHandler(CommentsActivity.this);
        userDBHandler.addLike(authHandler.getCurrentUser().getUid(), postId);
        isLiked.setText("1");
    }
    public void removeOneLike(TextView likes, TextView isLiked, ImageView likeIcon, String postId){
        likes.setText(""+(Integer.parseInt(likes.getText().toString())-1));
        likeIcon.setColorFilter(null); // remove tint color
        PostDBHandler postDBHandler = new PostDBHandler(CommentsActivity.this);
        postDBHandler.removeLike(postId);
        UserDBHandler userDBHandler = new UserDBHandler(CommentsActivity.this);
        AuthHandler authHandler = new AuthHandler(CommentsActivity.this);
        userDBHandler.removeLike(authHandler.getCurrentUser().getUid(), postId);
        isLiked.setText("0");
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

}