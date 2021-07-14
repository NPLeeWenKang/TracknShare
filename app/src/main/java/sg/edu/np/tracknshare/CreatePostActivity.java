package sg.edu.np.tracknshare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;

import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.PostDBHandler;
import sg.edu.np.tracknshare.handlers.RunDBHandler;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.handlers.TrackingDBHandler;
import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.models.Run;


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
                performPost(captionText.getText().toString());
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

        long id = storageHandler.GenerateId();
        Post p = new Post(auth.GetCurrentUser().getUid(), ""+id, runId, id, 0, caption);
        PostsDB.AddPost(p);
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