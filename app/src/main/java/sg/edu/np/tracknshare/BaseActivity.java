package sg.edu.np.tracknshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import sg.edu.np.tracknshare.fragments.PostFragment;
import sg.edu.np.tracknshare.fragments.ProfileFragment;
import sg.edu.np.tracknshare.fragments.RunsFragment;
import sg.edu.np.tracknshare.fragments.SearchFragment;
import sg.edu.np.tracknshare.handlers.AuthHandler;

public class BaseActivity extends AppCompatActivity{
    public ActionBarDrawerToggle toggle;

    public void setAppBar(boolean isSearch){
        // checks whether to display search app bar, or normal app bar
        Toolbar toolBar = null;
        if (isSearch){
            Log.d("APPBAR", "setAppBar: true");
            findViewById(R.id.includeBase).setVisibility(View.GONE);
            findViewById(R.id.includeSearch).setVisibility(View.VISIBLE);
            toolBar = findViewById(R.id.toolBarSearch);
        }else{
            Log.d("APPBAR", "setAppBar: true");
            findViewById(R.id.includeBase).setVisibility(View.VISIBLE);
            findViewById(R.id.includeSearch).setVisibility(View.GONE);
            toolBar = findViewById(R.id.toolBarBase);
        }
        setSupportActionBar(toolBar);
        toggle = new ActionBarDrawerToggle(this, findViewById(R.id.drawLayout), R.string.open, R.string.close);
        DrawerLayout dL = findViewById(R.id.drawLayout);
        dL.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        TrackingUtility tU = new TrackingUtility();
        if (tU.isMyServiceRunning(TrackingService.class, this)){
            // checks if running is in progress
            Intent intent = new Intent(this, StartRunActivity.class);
            startActivity(intent);
        }

        setAppBar(false);

        DrawerLayout dL = findViewById(R.id.drawLayout);

        // Setting initial fragment
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        Fragment postFrag = new PostFragment();
        Fragment searchFrag = new SearchFragment(this);
        Fragment runsFrag = new RunsFragment();
        Fragment profileFrag = new ProfileFragment();

        fTransaction.replace(R.id.flFragment, postFrag);
        fTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        fTransaction.commit();

        // sets drawer listener
        NavigationView nav = findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                dL.closeDrawer(Gravity.LEFT);
                BottomNavigationView bottomNavigationView = (BaseActivity.this).findViewById(R.id.bottomNavigationView);
                if(item.getItemId() == R.id.settingsNavItem){
                    Intent intent = new Intent(BaseActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.signOut){
                    AuthHandler auth = new AuthHandler(BaseActivity.this);
                    auth.signOut();
                    Log.d("NAV", "onNavigationItemSelected: ");
                    Intent intent = new Intent(BaseActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    BaseActivity.this.startActivity(intent);
                    ((Activity) BaseActivity.this).finish();
                }
                return true;
            }
        });

        // sets listeners for the botton navigation button
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fManager = getSupportFragmentManager();
                FragmentTransaction fTransaction = fManager.beginTransaction();
                fTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);
;                if (item.getItemId() == R.id.homeNavItem){ // home page
                    setAppBar(false);
                    fTransaction.replace(R.id.flFragment, postFrag, "Post_Frag");
                    fTransaction.commit();
                }else if (item.getItemId() == R.id.searchNavItem){ // search page
                    setAppBar(true);
                    fTransaction.replace(R.id.flFragment, searchFrag, "Search_Frag");
                    fTransaction.commit();
                }
                else if (item.getItemId() == R.id.runsNavItem){ // runs page
                    setAppBar(false);
                    fTransaction.replace(R.id.flFragment, runsFrag, "Runs_Frag");
                    fTransaction.commit();
                }
                else if (item.getItemId() == R.id.profileNavItem){ // profile page
                    setAppBar(false);
                    fTransaction.replace(R.id.flFragment, profileFrag, "Profile_Frag");
                    fTransaction.commit();
                }
                return true;
            }
        });

        // floating action button leads to start run page
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BaseActivity.this, StartRunActivity.class));
            }
        });
    }

    // when back button is pressed, app checks which fragment is displayed
    // if fragment is not PostFragment, change fragment to PostFragment.
    // But if it is PostFragment, then perform a normal back press.
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
            } else {
                Log.d("BACKBUTTON", "onBackPressed3: ");
                super.onBackPressed();
            }
        }

    }

    // sets the fragment to the PostFragment
    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.homeNavItem);
    }

    // listener for the top left burger button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ExampleBottomSheetDialog bottomSheet = new ExampleBottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}