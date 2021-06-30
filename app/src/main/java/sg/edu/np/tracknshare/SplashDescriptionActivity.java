package sg.edu.np.tracknshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import sg.edu.np.tracknshare.adapters.ViewPagerAdapter;

public class SplashDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_description);

        //getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ViewPager2 viewPager2 = findViewById(R.id.viewpager2);
        ViewPagerAdapter mAdapter =
                new ViewPagerAdapter(this, viewPager2);
        viewPager2.setAdapter(mAdapter);
    }

}
