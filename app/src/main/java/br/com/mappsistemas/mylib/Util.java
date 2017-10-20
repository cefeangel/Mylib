package br.com.mappsistemas.mylib;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by DEV on 16/10/2017.
 */

public class Util {

    private static final String TAG = "OFF2";
    public static final String USERDATA = "configServiceOffLine";

    public static String getProperty(String key,Context context) {

        Properties properties = new Properties();
        try{

            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
            inputStream.close();

        }catch (IOException e){
            Log.e(TAG,"Util --  IOException " +e.getMessage());
        }


        return properties.getProperty(key);

    }

    public static void saveMap(String key, Map<String,String> inputMap, Context context){
        SharedPreferences pSharedPref = context.getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        if (pSharedPref != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(key).commit();
            editor.putString(key, jsonString);
            editor.commit();
        }
    }

    public static Map<String,String> loadMap(String key, Context context){
        Map<String,String> outputMap = new HashMap<String,String>();
        SharedPreferences pSharedPref = context.getSharedPreferences(USERDATA, Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                String jsonString = pSharedPref.getString(key, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String k = keysItr.next();
                    String v = (String) jsonObject.get(k);
                    outputMap.put(k,v);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
}
