package com.unina.natour.config;

import android.content.Context;
import android.content.res.Resources;

import com.amazonaws.regions.Region;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.unina.natour.R;

import java.io.InputStream;
import java.io.InputStreamReader;



public class AmplifyUtils {

    private Context context;

    public AmplifyUtils(Context context){
        this.context = context;
    }

    public String getPoolId(){
        Resources resources = context.getResources();

        InputStream inputStream = resources.openRawResource(R.raw.awsconfiguration);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonElement jsonElement = JsonParser.parseReader(inputStreamReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonObject jsonCognitoUserPool = jsonObject.get("CognitoUserPool").getAsJsonObject();
        JsonObject jsonDefault = jsonCognitoUserPool.get("Default").getAsJsonObject();
        String poolId = jsonDefault.get("PoolId").getAsString();

       return poolId;
    }

    public Region getRegion(){
        Resources resources = context.getResources();

        InputStream inputStream = resources.openRawResource(R.raw.awsconfiguration);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonElement jsonElement = JsonParser.parseReader(inputStreamReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonObject jsonCognitoUserPool = jsonObject.get("CognitoUserPool").getAsJsonObject();
        JsonObject jsonDefault = jsonCognitoUserPool.get("Default").getAsJsonObject();
        String stringRegion = jsonDefault.get("Region").getAsString();

        Region region = Region.getRegion(stringRegion);
        return region;
    }

}
