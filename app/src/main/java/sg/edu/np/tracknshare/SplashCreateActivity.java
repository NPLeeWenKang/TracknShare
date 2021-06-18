package sg.edu.np.tracknshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import sg.edu.np.tracknshare.handlers.AuthHandler;

public class SplashCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_create);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Account Creation");
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.drab)));

        AuthHandler auth = new AuthHandler(this);

        Button button = findViewById(R.id.createBtn);
        EditText email = findViewById(R.id.emailCreate);
        EditText username = findViewById(R.id.usernameCreate);
        EditText password = findViewById(R.id.passwordCreate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.CreateEmailPasswordAccount(email.getText().toString(),password.getText().toString(),username.getText().toString());
            }
        });


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