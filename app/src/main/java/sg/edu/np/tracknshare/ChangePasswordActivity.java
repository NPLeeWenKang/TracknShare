package sg.edu.np.tracknshare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import sg.edu.np.tracknshare.handlers.AuthHandler;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Change Password");
        actionBar.setDisplayHomeAsUpEnabled(true);

        EditText password1 = findViewById(R.id.reset_password);
        EditText password2 = findViewById(R.id.reset_createpassword);
        ImageView save = findViewById(R.id.save_profile);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UPDATEPASSWORD", "onClick: ");
                String pwd1 = password1.getText().toString();
                String pwd2 = password2.getText().toString();
                Log.d("UPDATEPASSWORD", "onClick: "+Boolean.toString((!pwd1.equals("") && !pwd2.equals("")) && pwd1.equals(pwd2)));
                if ((!pwd1.equals("") && !pwd2.equals("")) && pwd1.equals(pwd2) && pwd1.length() >= 8){
                    AuthHandler auth = new AuthHandler(ChangePasswordActivity.this);
                    auth.UpdatePassword(password1.getText().toString());
                    finish();
                    overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
                } else{
                    Toast.makeText(ChangePasswordActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }

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