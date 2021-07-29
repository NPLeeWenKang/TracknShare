package sg.edu.np.tracknshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import sg.edu.np.tracknshare.handlers.AuthHandler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        AuthHandler auth = new AuthHandler(this);
        if (auth.isSignedIn()){
            Intent intent = new Intent(SplashActivity.this, BaseActivity.class);
            startActivity(intent);
            finish();
        }

        ConstraintLayout cL = findViewById(R.id.splash_button);
        cL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent;
                if (getSharedPreferences("isChecked", MODE_PRIVATE).getBoolean("firstTimeLogin",true)==true){
                    intent = new Intent(SplashActivity.this, SplashDescriptionActivity.class);
                }else{
                    intent = new Intent(SplashActivity.this, SplashLoginActivity.class);
                }
                SharedPreferences.Editor editor =  getSharedPreferences("isChecked", MODE_PRIVATE).edit();
                editor.putBoolean("firstTimeLogin",false);
                editor.apply();
                startActivity(intent);
                overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
            }
        });
    }
}