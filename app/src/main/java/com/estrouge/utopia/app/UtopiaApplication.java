package com.estrouge.utopia.app;

import android.app.Application;

import com.estrouge.utopia.db.Schema;

/**
 * Created by longtran on 4/9/19.
 */

public class UtopiaApplication extends Application {

    //variable init singleton application
    static UtopiaApplication mInstance;

    /**
     * get instance application
     *
     * @return instance application
     */
    public static synchronized UtopiaApplication getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initDatabase();
    }

    /**
     * Copy database to local if database not exists
     */
    private void initDatabase() {
        Schema.createDB(getApplicationContext(), true);
    }
}
