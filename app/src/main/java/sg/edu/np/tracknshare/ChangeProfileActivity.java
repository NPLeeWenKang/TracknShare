package sg.edu.np.tracknshare;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.handlers.UserDBHandler;
import sg.edu.np.tracknshare.models.User;

public class ChangeProfileActivity extends AppCompatActivity {
    private boolean imageIsChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadData(); // load current user's data into the Image and Text views

        StorageHandler storageHandler = new StorageHandler();
        AuthHandler auth = new AuthHandler(this);
        UserDBHandler userDBHandler = new UserDBHandler(this);

        // when user has chosen an image from gallery
        ImageView imageView = findViewById(R.id.add_profile_image);
        ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            //Detects request codes
                            imageIsChanged = true;
                            Uri selectedImage = result.getData().getData();
                            Bitmap bitmap = null;
                            try {
                                // displays chosen image on ImageView
                                bitmap = MediaStore.Images.Media.getBitmap(ChangeProfileActivity.this.getContentResolver(), selectedImage);
                                imageView.setImageBitmap(bitmap);
                                Log.d("UPLOADFILE", "onActivityResult: Success");
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Log.d("UPLOADFILE", "onActivityResult: Fail");
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d("UPLOADFILE", "onActivityResult: Fail");
                            }
                        }
                    }
                });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // opens the user's image gallery
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");
                long maxVideoSize = 1500000;
                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, maxVideoSize);
                myActivityResultLauncher.launch(intent);
            }
        });
        TextView newName = findViewById(R.id.edit_username);
        TextView newMass = findViewById(R.id.edit_mass);
        TextView newHeight = findViewById(R.id.edit_height);
        ImageView saveBtn = findViewById(R.id.save_profile);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SETTINGS", "onClick: Settings");
                if (imageIsChanged){
                    // save new profile image into firebase storage
                    BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    storageHandler.uploadProfileImage(""+auth.getCurrentUser().getUid(), bitmap);
                }
                User u = new User();
                u.setUserName(newName.getText().toString());
                u.setId(auth.getCurrentUser().getUid());
                u.setMass(newMass.getText().toString());
                u.setHeight(newHeight.getText().toString());
                userDBHandler.updateUserDetails(u); // inserts new data into database

                finish();
                overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
    }
    public void loadData(){
        AuthHandler auth = new AuthHandler(ChangeProfileActivity.this);
        UserDBHandler db = new UserDBHandler(ChangeProfileActivity.this);

        db.getUserDetailsIntoSettings(auth.getCurrentUser().getUid(), ChangeProfileActivity.this);
    }
}