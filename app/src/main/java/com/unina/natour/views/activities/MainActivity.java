package com.unina.natour.views.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.unina.natour.R;
import com.unina.natour.views.fragments.CommunityFragment;
import com.unina.natour.views.fragments.HomeFragment;
import com.unina.natour.views.fragments.PianificaFragment;
import com.unina.natour.views.fragments.ProfiloPersonaleFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();
        ProfiloPersonaleFragment profiloFragment = new ProfiloPersonaleFragment();
        PianificaFragment pianificaFragment = new PianificaFragment();
        CommunityFragment communityFragment = new CommunityFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.Main_navigationBar_menu);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Main_fragment_containter, homeFragment)
                .commit();

        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.bottomMenu_community);
        badgeDrawable.setVisible(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.bottomMenu_home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Main_fragment_containter,homeFragment)
                            .commit();

                    return true;
                }
                if (item.getItemId() == R.id.bottomMenu_profilo) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Main_fragment_containter,profiloFragment)
                            .commit();

                    return true;
                }
                if (item.getItemId() == R.id.bottomMenu_pianifica) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Main_fragment_containter,pianificaFragment)
                            .commit();

                    return true;
                }
                if (item.getItemId() == R.id.bottomMenu_community) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Main_fragment_containter,communityFragment)
                            .commit();

                    return true;
                }

                return false;
            }
        });


    }
}