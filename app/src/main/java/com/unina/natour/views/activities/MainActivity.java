package com.unina.natour.views.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.unina.natour.R;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.PianificaItinerarioController;
import com.unina.natour.views.fragments.CommunityFragment;
import com.unina.natour.views.fragments.HomeFragment;
import com.unina.natour.views.fragments.PianificaItinerarioFragment;
import com.unina.natour.views.fragments.ProfiloPersonaleFragment;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends NaTourActivity {

    public final static long CODE_FRAGMENT_HOME = 0l;
    public final static long CODE_FRAGMENT_PROFILO = 1l;
    public final static long CODE_FRAGMENT_PIANIFICA = 2l;
    public final static long CODE_FRAGMENT_COMMUNITY = 3l;

    private MainController mainController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainController = new MainController(this);

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setNaTourActivity(this);

        ProfiloPersonaleFragment profiloFragment = new ProfiloPersonaleFragment();
        profiloFragment.setNaTourActivity(this);

        PianificaItinerarioController pianificaItinerarioController = new PianificaItinerarioController(this);
        PianificaItinerarioFragment pianificaFragment = PianificaItinerarioFragment.newInstance(pianificaItinerarioController);
        pianificaFragment.setNaTourActivity(this);

        CommunityFragment communityFragment = new CommunityFragment();
        communityFragment.setNaTourActivity(this);


        BottomNavigationView bottomNavigationView = findViewById(R.id.Main_navigationBar_menu);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Main_fragment_containter, homeFragment)
                .commit();

        mainController.setCurrentFragment(homeFragment);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.bottomMenu_home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Main_fragment_containter,homeFragment)
                            .commit();

                    mainController.setCurrentFragment(homeFragment);
                    return true;
                }
                if (item.getItemId() == R.id.bottomMenu_profilo) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Main_fragment_containter,profiloFragment)
                            .commit();

                    mainController.setCurrentFragment(profiloFragment);
                    return true;
                }
                if (item.getItemId() == R.id.bottomMenu_pianifica) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Main_fragment_containter,pianificaFragment)
                            .commit();

                    mainController.setCurrentFragment(pianificaFragment);
                    return true;
                }
                if (item.getItemId() == R.id.bottomMenu_community) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Main_fragment_containter,communityFragment)
                            .commit();

                    mainController.setCurrentFragment(communityFragment);
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        mainController.checkIfHasChatNotification();
        updateChatNotification();



        super.onResume();
    }

    public MainController getMainController() {
        return mainController;
    }

    public void updateChatNotification(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.Main_navigationBar_menu);
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.bottomMenu_community);
        if(mainController.hasChatNotification()){
            badgeDrawable.setBackgroundColor(Color.BLUE);
            badgeDrawable.setVisible(true);
        }
        else badgeDrawable.setVisible(false);
    }

    @Override
    public void onBackPressed() {
        mainController.back();
    }
}