package sg.edu.np.tracknshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.tracknshare.fragments.LoginFragment;
import sg.edu.np.tracknshare.fragments.PostFragment;
import sg.edu.np.tracknshare.fragments.ProfileFragment;
import sg.edu.np.tracknshare.fragments.SearchFragment;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        Fragment postFrag = new PostFragment();
        Fragment searchFrag = new SearchFragment(this);
        Fragment profileFrag = new ProfileFragment();

        fTransaction.replace(R.id.flFragment, postFrag);
        fTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fTransaction.commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fManager = getSupportFragmentManager();
                FragmentTransaction fTransaction = fManager.beginTransaction();
                fTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
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
                fTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                if (item.toString().equals("home")){
                    fTransaction.replace(R.id.flFragment, postFrag);
                    fTransaction.commit();
                }else if (item.toString().equals("search")){
                    fTransaction.replace(R.id.flFragment, searchFrag);
                    fTransaction.commit();
                }
                else if (item.toString().equals("runs")){
                    fTransaction.replace(R.id.flFragment, profileFrag);
                    fTransaction.commit();
                }
                else if (item.toString().equals("profile")){
                    fTransaction.replace(R.id.flFragment, profileFrag);
                    fTransaction.commit();
                }
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.home != seletedItemId) {
            setHomeItem(BaseActivity.this);
        } else {
            super.onBackPressed();
        }
    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
}