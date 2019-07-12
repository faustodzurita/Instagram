package com.faustodzurita.instagram;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.faustodzurita.instagram.fragments.ComposeFragment;
import com.faustodzurita.instagram.fragments.ProfileFragment;
import com.faustodzurita.instagram.fragments.TimelineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private FrameLayout fragmentFrame;
    private BottomNavigationView bottomNavigationButtonGroup;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager = getSupportFragmentManager();

        fragmentFrame = findViewById(R.id.activity_home_fragment_frame);
        bottomNavigationButtonGroup = findViewById(R.id.activity_home_bottom_navigation);

        bottomNavigationButtonGroup.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;

                switch (menuItem.getItemId()) {
                    case R.id.action_timeline:
                        fragment = new TimelineFragment();
                        break;
                    case R.id.action_compose:
                        fragment = new ComposeFragment();
                        break;
                    default:
                        fragment = new ProfileFragment();
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.activity_home_fragment_frame, fragment).commit();

                return true;
            }
        });

        bottomNavigationButtonGroup.setSelectedItemId(R.id.action_timeline);
    }
}
