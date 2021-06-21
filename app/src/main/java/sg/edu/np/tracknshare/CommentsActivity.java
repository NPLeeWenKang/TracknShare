package sg.edu.np.tracknshare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CommentsActivity extends AppCompatActivity {
    ImageView UserImg;
    TextView Username;
    TextView PostDate;
    TextView PostCaption;
    TextView Likes;
    ImageView PostImg;

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

    }
}