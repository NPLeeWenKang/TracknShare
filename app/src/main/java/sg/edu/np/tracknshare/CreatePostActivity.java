package sg.edu.np.tracknshare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.PostDBHandler;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.handlers.TrackingDBHandler;
import sg.edu.np.tracknshare.models.Post;


public class CreatePostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent receivingEnd = getIntent();
        String runId = receivingEnd.getStringExtra("runId");

        ImageView img = findViewById(R.id.create_post_img);

        Log.d("CREATEPOST", "onCreate: "+img.getWidth());

        StorageHandler storageDB = new StorageHandler();
        storageDB.LoadFileToApp(runId, this, img);

        ImageView settingsBtn = findViewById(R.id.post_run);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText captionText = findViewById(R.id.caption_editText);
                String caption = captionText.getText().toString();
                if(!caption.isEmpty()){
                    performPost(captionText.getText().toString());
                }
                else{ //empty caption validation
                    Toast.makeText(getApplicationContext(),"Caption cannot be left empty",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void performPost(String caption){
        StorageHandler storageHandler = new StorageHandler();
        AuthHandler auth = new AuthHandler(CreatePostActivity.this);
        TrackingDBHandler trackingDB = new TrackingDBHandler(CreatePostActivity.this);
        PostDBHandler PostsDB = new PostDBHandler(CreatePostActivity.this);

        Intent receivingEnd = getIntent();
        String runId = receivingEnd.getStringExtra("runId");

        long id = Calendar.getInstance().getTimeInMillis();
        Post p = new Post(auth.getCurrentUser().getUid(), ""+id, runId, id, 0, caption);
        PostsDB.AddPost(p);
        //set Success value to create Snackbar
        SharedPreferences.Editor sharedPref = getSharedPreferences("PostStatus",MODE_PRIVATE).edit();
        sharedPref.putBoolean("Success",true);
        sharedPref.apply();
        finish();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}