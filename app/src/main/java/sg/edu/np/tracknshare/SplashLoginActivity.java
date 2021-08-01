package sg.edu.np.tracknshare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.zip.Inflater;

import sg.edu.np.tracknshare.handlers.AuthHandler;

public class SplashLoginActivity extends AppCompatActivity {
    private boolean toggled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        AuthHandler auth = new AuthHandler(this);

        Button loginButton = findViewById(R.id.createBtn);
        EditText email = findViewById(R.id.emailCreate);
        EditText password = findViewById(R.id.passwordCreate);
        TextView errorText = findViewById(R.id.error_msg);
        TextView signUpText = findViewById(R.id.SignUp);
        TextView forgotPasswordText = findViewById(R.id.forgot_password);
        ImageView passwordImg = findViewById(R.id.passwordVis);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // signs in the user
                if (!email.getText().toString().equals("") && !password.getText().toString().equals("")){
                    auth.signInWithEmailPassword(email.getText().toString(),password.getText().toString(),errorText);
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // used to check if edit text should lose focus
                if (!hasFocus && !password.hasFocus()) {
                    hideKeyboard(v);
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // used to check if edit text should lose focus
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
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // displays dialog box to input account email to reset password
                View ediTextView = getLayoutInflater().inflate(R.layout.alert_edittext, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashLoginActivity.this);
                builder.setMessage("Request for to reset password?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No", null)
                        .setView(ediTextView);
                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button posButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        posButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EditText input = ediTextView.findViewById(R.id.edittextfield);
                                if (!input.getText().toString().equals("")){
                                    String emailAddress = input.getText().toString();
                                    auth.sendResetEmail(emailAddress); // sets the password reset email
                                    dialog.dismiss();
                                    Toast.makeText(SplashLoginActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                }else{
                                    dialog.dismiss();
                                    Toast.makeText(SplashLoginActivity.this, "Email not valid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Button negButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                        negButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });

        // toggles to display password
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