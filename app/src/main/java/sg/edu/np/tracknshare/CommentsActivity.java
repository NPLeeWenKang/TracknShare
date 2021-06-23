package sg.edu.np.tracknshare;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import sg.edu.np.tracknshare.adapters.CommentsAdapter;
import sg.edu.np.tracknshare.models.Comment;

public class CommentsActivity extends AppCompatActivity {
    ImageView UserImg;
    TextView Username;
    TextView PostDate;
    TextView PostCaption;
    TextView Likes;
    ImageView PostImg;
    EditText editTextComment;
    Button sendComment;
    ArrayList<Comment> comments = new ArrayList<Comment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Username = findViewById(R.id.user_name);
        PostDate = findViewById(R.id.workout_Date);
        Likes = findViewById(R.id.likes_);
        PostCaption = findViewById(R.id.post_Caption);
        editTextComment = findViewById(R.id.new_commentEditText);
        sendComment = findViewById(R.id.sendComment_button);
        Intent receivePost = getIntent();
        Username.setText(receivePost.getStringExtra("Username"));
        PostDate.setText(receivePost.getStringExtra("Postdate"));
        Likes.setText(""+receivePost.getIntExtra("PostLikes",0));
        PostCaption.setText(receivePost.getStringExtra("PostCaption"));
        try {
            comments = generateComments();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        RecyclerView comments_rv = findViewById(R.id.rv_comments);
        CommentsAdapter adapter = new CommentsAdapter(getApplicationContext(),comments);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        comments_rv.setAdapter(adapter);
        comments_rv.setLayoutManager(lm);

        editTextComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                sendComment.setClickable(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendComment.setClickable(true);
                Comment newComment = new Comment();
                newComment.setCommentText(s.toString());

                //date and time formatting
                String timeformat = "hh:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(timeformat); //set the formatting string
                Calendar today = Calendar.getInstance(); 
                String commentTime = sdf.format(today.getTime());

                newComment.setDate(commentTime);
                newComment.setUserName("User11");
                //action upon clicking 'send' button
                sendComment.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        comments.add(newComment);
                        adapter.notifyDataSetChanged();
                        editTextComment.getText().clear(); //to clear input text after successful comment
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private ArrayList<Comment> generateComments() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss") ;
        String date = sdf.format(calendar.getTime());
        ArrayList<Comment> commentList = new ArrayList<Comment>();
        Date now = sdf.parse(date);
        long ms = now.getTime()-sdf.parse("22-6-2021 19:06:02").getTime();//date difference in milliseconds
        int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(ms);
        int minutes = (int)TimeUnit.MILLISECONDS.toMinutes(ms);
        int hours = (int)TimeUnit.MILLISECONDS.toHours(ms);
        int days = (int)TimeUnit.MILLISECONDS.toDays(ms);
        int weeks = (days - days % 7)/7;
        int months = (weeks- weeks % 4)/7;
        for(int i = 1;i<=10;i++){

            Comment cmt = new Comment();
            cmt.setId(i);
            cmt.setUserName("User"+i);
            String timeSpan = "";
            if(seconds >= 1){
                timeSpan =seconds+" seconds ago";
            }
            else if(minutes >= 1){
                timeSpan = minutes+" minutes ago";
            }
            else if(hours >= 1){
                timeSpan = hours+" hours ago";
            }
            else if(days >=1){
                timeSpan = days+" days ago";
            }

            else if(weeks >= 1){
                timeSpan = weeks+" weeks ago";
            }

            else if (months >= 1){
                timeSpan = months + "seconds ago.";
            }

            Log.d("Hello",String.valueOf(seconds));
            cmt.setDate(date);
            cmt.setCommentText("This is comment #"+i);
            commentList.add(cmt);
        }
        return commentList;
    }
}