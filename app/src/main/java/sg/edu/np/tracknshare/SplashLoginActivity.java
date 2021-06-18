package sg.edu.np.tracknshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import sg.edu.np.tracknshare.handlers.AuthHandler;

public class SplashLoginActivity extends AppCompatActivity {
    private boolean toggled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        AuthHandler auth = new AuthHandler(this);

        Button loginButton = findViewById(R.id.createBtn);
        EditText email = findViewById(R.id.emailCreate);
        EditText password = findViewById(R.id.passwordCreate);
        TextView errorText = findViewById(R.id.error_msg);
        TextView signUpText = findViewById(R.id.SignUp);
        ImageView passwordImg = findViewById(R.id.passwordVis);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("") && !password.getText().toString().equals("")){
                    auth.SignInWithEmailPassword(email.getText().toString(),password.getText().toString(),errorText);
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !password.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !email.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashLoginActivity.this, SplashCreateActivity.class);
                startActivity(intent);
            }
        });
        passwordImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggled){
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    passwordImg.setImageResource(R.drawable.invisible);
                } else {
                    password.setTransformationMethod(null);
                    passwordImg.setImageResource(R.drawable.visibility_button);
                }
                password.setSelection(password.getText().length());
                toggled = !toggled;

            }
        });
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}