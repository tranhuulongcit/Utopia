package com.estrouge.utopia;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.estrouge.utopia.db.ResultSet;
import com.estrouge.utopia.db.Schema;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {


    @Before
    public void initDatabase() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Schema.createDB(appContext, true);
    }

    @Test
    public void checkTotalRecord() throws Exception {
        ResultSet rs = Schema.open().query("SELECT Count(*) as total from cities");
        assertEquals(rs.get().getInt("total"), 272128);
    }

}
