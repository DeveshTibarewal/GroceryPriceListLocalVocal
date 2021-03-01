package com.dev.grocerypricelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    //UI Views
    private TextView titleTv;
    private ImageButton refreshBtn;
    private FrameLayout frameLayout;
    private BottomNavigationView navigationView;

    //Fragments
    private Fragment homeFragment, statsFragment;
    private Fragment activeFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init UI Views
        titleTv = findViewById(R.id.titleTv);
        refreshBtn = findViewById(R.id.refreshBtn);
        frameLayout = findViewById(R.id.frameLayout);
        navigationView = findViewById(R.id.navigationView);

        initFragments();

        //refresh Button Click, Refresh Records
        refreshBtn.setOnClickListener(v -> {
            homeFragment.onResume();
            statsFragment.onResume();
        });

        navigationView.setOnNavigationItemSelectedListener(this);
    }

    private void initFragments() {
        homeFragment = new HomeFragment();
        statsFragment = new StatsFragment();

        fragmentManager = getSupportFragmentManager();
        activeFragment = homeFragment;

        fragmentManager.beginTransaction()
                .add(R.id.frameLayout, homeFragment, "Home Fragment")
                .commit();
        fragmentManager.beginTransaction()
                .add(R.id.frameLayout, statsFragment, "Stats Fragment")
                .hide(statsFragment)
                .commit();
    }

    private void loadHomeFragment() {
        titleTv.setText("Home");
        fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
        activeFragment = homeFragment;
    }

    private void loadStatsFragment() {
        titleTv.setText("Grocery Price List");
        fragmentManager.beginTransaction().hide(activeFragment).show(statsFragment).commit();
        activeFragment = statsFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //handle bottom navigation item clicks
        switch (item.getItemId()) {
            case R.id.nav_home: {
                //load Home Data
                loadHomeFragment();
                return true;
            }
            case R.id.nav_stats: {
                //load Stats
                loadStatsFragment();
                return true;
            }
        }
        return false;
    }
}