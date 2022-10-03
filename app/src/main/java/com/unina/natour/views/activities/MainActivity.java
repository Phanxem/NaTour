package com.unina.natour.views.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.unina.natour.R;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.PianificaItinerarioController;
import com.unina.natour.views.fragments.CommunityFragment;
import com.unina.natour.views.fragments.HomeFragment;
import com.unina.natour.views.fragments.PianificaItinerarioFragment;
import com.unina.natour.views.fragments.ProfiloPersonaleFragment;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends NaTourActivity {

/*
    public void openAwsConfigurationFile(){
        Resources resources = getActivity().getResources();

        InputStream inputStream = resources.openRawResource(R.raw.awsconfiguration);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonElement jsonElement = JsonParser.parseReader(inputStreamReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
    }
    */



    MainController mainController;

    //ProfiloPersonaleController profiloPersonaleController;
    //PianificaItinerarioController pianificaItinerarioController;
    //home controller ecc...


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Thread.setDefaultUncaughtExceptionHandler(un);

        setContentView(R.layout.activity_main);

        mainController = new MainController(this);
  /*
        profiloPersonaleController = new ProfiloPersonaleController(this);
        pianificaItinerarioController = new PianificaItinerarioController(this);
*/



        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setNaTourActivity(this);

        //ProfiloPersonaleFragment profiloFragment = ProfiloPersonaleFragment.newInstance(profiloPersonaleController);
        ProfiloPersonaleFragment profiloFragment = new ProfiloPersonaleFragment();
        profiloFragment.setNaTourActivity(this);

        PianificaItinerarioController pianificaItinerarioController = new PianificaItinerarioController(this);
        PianificaItinerarioFragment pianificaFragment = PianificaItinerarioFragment.newInstance(pianificaItinerarioController);
        //PianificaItinerarioFragment pianificaFragment = new PianificaItinerarioFragment();
        pianificaFragment.setNaTourActivity(this);

        CommunityFragment communityFragment = new CommunityFragment();
        communityFragment.setNaTourActivity(this);





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