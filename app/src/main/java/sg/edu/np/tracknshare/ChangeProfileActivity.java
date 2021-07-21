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

import com.google.firebase.database.core.view.Change;

import java.io.FileNotFoundException;
import java.io.IOException;

import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.handlers.UserDBHandler;
import sg.edu.np.tracknshare.models.User;

public class ChangeProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadData();

        StorageHandler storageHandler = new StorageHandler();
        AuthHandler auth = new AuthHandler(this);
        UserDBHandler userDBHandler = new UserDBHandler(this);

        ImageView imageView = findViewById(R.id.add_profile_image);
        ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            //Detects request codes
                            Uri selectedImage = result.getData().getData();
                            Bitmap bitmap = null;
                            try {
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
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                myActivityResultLauncher.launch(intent);
            }
        });
        TextView newName = findViewById(R.id.edit_username);
        ImageView settingsBtn = findViewById(R.id.save_profile);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SETTINGS", "onClick: Settings");
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
                Bitmap bitmap = bitmapDrawable.getBitmap();
                storageHandler.UploadProfileImage(""+auth.GetCurrentUser().getUid(), bitmap);
                User u = new User();
                u.setUserName(newName.getText().toString());
                u.setId(auth.GetCurrentUser().getUid());
                userDBHandler.UpdateUserDetails(u);

                finish();
                overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
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

        db.GetUserDetailsIntoSettings(auth.GetCurrentUser().getUid(), ChangeProfileActivity.this);
    }
}