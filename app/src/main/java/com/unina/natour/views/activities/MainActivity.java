package com.unina.natour.views.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.unina.natour.R;
import com.unina.natour.controllers.ImportaFileGPXController;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.PianificaItinerarioController;
import com.unina.natour.controllers.ProfiloPersonaleController;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.fragments.CommunityFragment;
import com.unina.natour.views.fragments.HomeFragment;
import com.unina.natour.views.fragments.PianificaItinerarioFragment;
import com.unina.natour.views.fragments.ProfiloPersonaleFragment;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {



    MainController mainController;

    ProfiloPersonaleController profiloPersonaleController;
    PianificaItinerarioController pianificaItinerarioController;
    //home controller ecc...


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setSupportFragmentManager(getSupportFragmentManager());

        mainController = new MainController(this, messageDialog);

        profiloPersonaleController = new ProfiloPersonaleController(this, messageDialog);
        pianificaItinerarioController = new PianificaItinerarioController(this, messageDialog);


        HomeFragment homeFragment = new HomeFragment();
        ProfiloPersonaleFragment profiloFragment = ProfiloPersonaleFragment.newInstance(profiloPersonaleController);
        PianificaItinerarioFragment pianificaFragment = PianificaItinerarioFragment.newInstance(pianificaItinerarioController);
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