package sg.edu.np.tracknshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import sg.edu.np.tracknshare.fragments.PostFragment;
import sg.edu.np.tracknshare.fragments.ProfileFragment;
import sg.edu.np.tracknshare.fragments.RunsFragment;
import sg.edu.np.tracknshare.fragments.SearchFragment;
import sg.edu.np.tracknshare.handlers.AuthHandler;

public class BaseActivity extends AppCompatActivity {
    public ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Initiating navigation drawer
        toggle = new ActionBarDrawerToggle(this, findViewById(R.id.drawLayout), R.string.open, R.string.close);
        DrawerLayout dL = findViewById(R.id.drawLayout);
        dL.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting initial fragment
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        Fragment postFrag = new PostFragment();
        Fragment searchFrag = new SearchFragment(this);
        Fragment runsFrag = new RunsFragment();
        Fragment profileFrag = new ProfileFragment();
        Fragment startRunFrag = new StartRunFragment();

        fTransaction.replace(R.id.flFragment, postFrag);
        fTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fTransaction.commit();
        NavigationView nav = findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Log.d("NAV", "onNavigationItemSelected: ");
                dL.closeDrawer(Gravity.LEFT);
                BottomNavigationView bottomNavigationView = (BaseActivity.this).findViewById(R.id.bottomNavigationView);
                if (item.getItemId() == R.id.homeNavItem){
                    bottomNavigationView.setSelectedItemId(R.id.homeNavItem);

                }else if (item.getItemId() == R.id.searchNavItem){
                    bottomNavigationView.setSelectedItemId(R.id.searchNavItem);

                }
                else if (item.getItemId() == R.id.runsNavItem){
                    bottomNavigationView.setSelectedItemId(R.id.runsNavItem);
                }
                else if (item.getItemId() == R.id.profileNavItem){
                    bottomNavigationView.setSelectedItemId(R.id.profileNavItem);
                }
                else if (item.getItemId() == R.id.signOut){
                    AuthHandler auth = new AuthHandler(BaseActivity.this);
                    auth.SignOut();
                    Log.d("NAV", "onNavigationItemSelected: ");
                    Intent intent = new Intent(BaseActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    BaseActivity.this.startActivity(intent);
                    ((Activity) BaseActivity.this).finish();
                }
                return true;
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bottomNavigationView.getMenu().setGroupCheckable(0, true,true);
                FragmentManager fManager = getSupportFragmentManager();
                FragmentTransaction fTransaction = fManager.beginTransaction();
                fTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
;                if (item.getItemId() == R.id.homeNavItem){
                    fTransaction.replace(R.id.flFragment, postFrag, "Post_Frag");
                    fTransaction.commit();
                }else if (item.getItemId() == R.id.searchNavItem){
                    fTransaction.replace(R.id.flFragment, searchFrag, "Search_Frag");
                    fTransaction.commit();
                }
                else if (item.getItemId() == R.id.runsNavItem){
                    fTransaction.replace(R.id.flFragment, runsFrag, "Runs_Frag");
                    fTransaction.commit();
                }
                else if (item.getItemId() == R.id.profileNavItem){
                    fTransaction.replace(R.id.flFragment, profileFrag, "Profile_Frag");
                    fTransaction.commit();
                }
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.getMenu().setGroupCheckable(0, false,false);
                FragmentManager fManager = getSupportFragmentManager();
                FragmentTransaction fTransaction = fManager.beginTransaction();
                fTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
                fTransaction.replace(R.id.flFragment, startRunFrag, "StartRun_Frag");
                fTransaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout dL = findViewById(R.id.drawLayout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        FragmentManager fManager = getSupportFragmentManager();

        if (dL.isDrawerOpen(Gravity.LEFT)){
            dL.closeDrawer(Gravity.LEFT);
        }else{
            if (R.id.homeNavItem != seletedItemId) {
                Log.d("BACKBUTTON", "onBackPressed1: "+(R.id.homeNavItem != seletedItemId));
                setHomeItem(BaseActivity.this);
            } else if (fManager.findFragmentByTag("StartRun_Frag") != null && fManager.findFragmentByTag("StartRun_Frag").isVisible())
            {
                Log.d("BACKBUTTON", "onBackPressed2: "+(fManager.findFragmentByTag("StartRun_Frag") != null && fManager.findFragmentByTag("StartRun_Frag").isVisible()));
                setHomeItem(BaseActivity.this);
            } else {
                Log.d("BACKBUTTON", "onBackPressed3: ");
                super.onBackPressed();
            }
//            super.onBackPressed();
//
//            if (fManager.findFragmentByTag("Search_Frag") != null && fManager.findFragmentByTag("Search_Frag").isVisible()) {
//                bottomNavigationView.setSelectedItemId(R.id.searchNavItem);
//            } else if(fManager.findFragmentByTag("StartRun_Frag") != null && fManager.findFragmentByTag("StartRun_Frag").isVisible()) {
//                // add your code here
//            } else if(fManager.findFragmentByTag("Runs_Frag") != null && fManager.findFragmentByTag("Runs_Frag").isVisible()) {
//                bottomNavigationView.setSelectedItemId(R.id.runsNavItem);
//            } else if(fManager.findFragmentByTag("Profile_Frag") != null && fManager.findFragmentByTag("Profile_Frag").isVisible()) {
//                bottomNavigationView.setSelectedItemId(R.id.profileNavItem);
//                // add your code here
//            }
        }

    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.homeNavItem);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}