package com.estrouge.utopia.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by longtran on 4/9/19.
 */

public final class PropertiesManager {
    //key path db
    public static String PATH = "est.db.path";
    //key db name
    public static String DB_NAME = "est.db.name";
    //properties file name
    static final String PATH_CONFIG = "app.properties";
    //instance properties
    static Properties properties = null;

    private PropertiesManager() {}

    /**
     * open file properties
     * @param context application context
     * @return properties
     */
    public static Properties open(Context context){
        if(properties == null) {
            properties = new Properties();
            try {
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open(PATH_CONFIG);
                properties.load(inputStream);

            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        }
        return properties;
    }
}

