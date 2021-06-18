package sg.edu.np.tracknshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.tracknshare.fragments.LoginFragment;
import sg.edu.np.tracknshare.fragments.PostFragment;
import sg.edu.np.tracknshare.fragments.ProfileFragment;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        Fragment postFrag = new PostFragment();
        Fragment loginFrag = new LoginFragment(BaseActivity.this);
        Fragment profileFrag = new ProfileFragment();

        fTransaction.replace(R.id.flFragment, postFrag);
        fTransaction.addToBackStack(null);
        fTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fTransaction.commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fManager = getSupportFragmentManager();
                FragmentTransaction fTransaction = fManager.beginTransaction();
                fTransaction.addToBackStack(null);
                fTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                fTransaction.replace(R.id.flFragment, loginFrag);
                fTransaction.commit();
                Log.d("NAVBAR", "onClick: ");
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fManager = getSupportFragmentManager();
                FragmentTransaction fTransaction = fManager.beginTransaction();
                fTransaction.addToBackStack(null);
                fTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                if (item.toString().equals("home")){
                    fTransaction.replace(R.id.flFragment, postFrag);
                    fTransaction.commit();
                }else if (item.toString().equals("profile")){
                    fTransaction.replace(R.id.flFragment, profileFrag);
                    fTransaction.commit();
                }
                return true;
            }
        });
    }
}