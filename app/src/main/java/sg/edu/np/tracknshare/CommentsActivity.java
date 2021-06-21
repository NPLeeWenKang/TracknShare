package sg.edu.np.tracknshare;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sg.edu.np.tracknshare.adapters.CommentsAdapter;
import sg.edu.np.tracknshare.models.Comment;

public class CommentsActivity extends AppCompatActivity {
    ImageView UserImg;
    TextView Username;
    TextView PostDate;
    TextView PostCaption;
    TextView Likes;
    ImageView PostImg;
    ArrayList<Comment> comments = new ArrayList<Comment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Intent receivePost = getIntent();
        Username = findViewById(R.id.user_name);
        PostDate = findViewById(R.id.workout_Date);
        Likes = findViewById(R.id.likes_);
        PostCaption = findViewById(R.id.post_Caption);

        Username.setText(receivePost.getStringExtra("Username"));
        PostDate.setText(receivePost.getStringExtra("Postdate"));
        Likes.setText(""+receivePost.getIntExtra("PostLikes",0));
        PostCaption.setText(receivePost.getStringExtra("PostCaption"));
        comments = generateComments();
        RecyclerView comments_rv = findViewById(R.id.rv_comments);
        CommentsAdapter adapter = new CommentsAdapter(getApplicationContext(),comments);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        comments_rv.setAdapter(adapter);
        comments_rv.setLayoutManager(lm);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Comment> generateComments() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy") ;
        String date = sdf.format(calendar.getTime());
        ArrayList<Comment> commentList = new ArrayList<Comment>();
        for(int i = 1;i<=10;i++){

            Comment cmt = new Comment();
            cmt.setId(i);
            cmt.setUserName("User"+i);
            cmt.setDate(date);
            cmt.setCommentText("This is comment #"+i);
            commentList.add(cmt);
        }
        return commentList;
    }
}