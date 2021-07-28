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
import sg.edu.np.tracknshare.handlers.PostDBHandler;
import sg.edu.np.tracknshare.models.Comment;

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
        if (postId == null && runId == null){
            finish();
        }
        Log.d("COMMENTSACTI", "onCreate: "+postId);
        PostDBHandler postDBHandler = new PostDBHandler(this);
        postDBHandler.getPost(postId, this);

        ImageView mapImage = findViewById(R.id.post_Image);
        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentsActivity.this, FullMapActivity.class);
                intent.putExtra("mapType", "movable");
                intent.putExtra("id", runId);
                startActivity(intent);
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

}