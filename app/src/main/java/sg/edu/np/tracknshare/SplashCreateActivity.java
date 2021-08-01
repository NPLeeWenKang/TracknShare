package sg.edu.np.tracknshare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.models.User;

public class SplashCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_create);

        // Create a TracknShare account

        Toolbar toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Account Creation");
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.drab)));

        AuthHandler auth = new AuthHandler(this);

        Button button = findViewById(R.id.createBtn);
        EditText email = findViewById(R.id.emailCreate);
        EditText username = findViewById(R.id.usernameCreate);
        EditText password = findViewById(R.id.passwordCreate);
        EditText mass = findViewById(R.id.massCreate);
        EditText height = findViewById(R.id.heightCreate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailVal = email.getText().toString();
                String passwordVal = password.getText().toString();
                String usernameVal = username.getText().toString();
                String massVal = mass.getText().toString();
                String heightVal = height.getText().toString();
                if (!emailVal.equals("") && !passwordVal.equals("") && !usernameVal.equals("") && !massVal.equals("") && !heightVal.equals("")){
                    User u = new User();
                    u.setEmail(emailVal);
                    u.setUserName(usernameVal);
                    u.setMass(massVal);
                    u.setHeight(heightVal);
                    auth.createEmailPasswordAccount(passwordVal, u);
                }

            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // used to check if edit text should lose focus
                if (!email.hasFocus() && !password.hasFocus() && !username.hasFocus() && !mass.hasFocus() && !height.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // used to check if edit text should lose focus
                if (!email.hasFocus() && !password.hasFocus() && !username.hasFocus() && !mass.hasFocus() && !height.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // used to check if edit text should lose focus
                if (!email.hasFocus() && !password.hasFocus() && !username.hasFocus() && !mass.hasFocus() && !height.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });
        mass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // used to check if edit text should lose focus
                if (!email.hasFocus() && !password.hasFocus() && !username.hasFocus() && !mass.hasFocus() && !height.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });
        height.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // used to check if edit text should lose focus
                if (!email.hasFocus() && !password.hasFocus() && !username.hasFocus() && !mass.hasFocus() && !height.hasFocus()) {
                    hideKeyboard(v);
                }
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
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}