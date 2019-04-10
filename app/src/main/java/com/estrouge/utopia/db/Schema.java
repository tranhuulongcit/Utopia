package com.estrouge.utopia.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.estrouge.utopia.utils.PropertiesManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by longtran on 4/9/19.
 */

public class Schema {

    /**
     * order by type column
     */
    public enum OrderByType {
        DESC, ASC
    }
    //copy to path
    private static  String path = null;
    //db name
    private static String dbName = null;
    //schema instance
    private static Schema instance = null;
    //A helper class to manage database creation and version management.
    private static SQLiteOpenHelper helper;
    //application context
    private static Context context;
    //Exposes methods to manage a SQLite database.
    private SQLiteDatabase db;
    //message
    private StringBuilder message = new StringBuilder();

    /**
     * get instance singleton
     * @return instance Schema
     */
    public static synchronized Schema open() {
        if (instance == null) {
            instance = new Schema();
        }
        return instance;
    }

    /**
     * construct schema
     */
    private Schema() {
        this.db = SQLiteDatabase.openDatabase(Schema.path + Schema.dbName, null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    /**
     * create database
     * @param context application context
     * @param hasCopy has copy
     */
    public static synchronized void createDB(Context context, boolean hasCopy) {
        Schema.dbName = PropertiesManager.open(context).getProperty(PropertiesManager.DB_NAME);
        Schema.path = PropertiesManager.open(context).getProperty(PropertiesManager.PATH);

        if (Schema.dbName != null){
            Schema.context = context;
            Schema.helper = new SQLiteOpenHelper(context, Schema.dbName, null, 1) {
                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                }

                @Override
                public void onCreate(SQLiteDatabase db) {
                    Log.i( "onCreate", "db create");
                }
            };

            if (path != null) {
                if (hasCopy) {
                    try {
                        boolean dbExist = checkDB();
                        if (!dbExist) {
                            copyDB();
                        }
                    } catch (Exception e) {
                        throw new Error("copy error");
                    }
                }
            }else {
                Log.e("createDB", "path is null, please set path db in file app.properties");
            }
        } else {
            Log.e("createDB", "dbName is null, please set db name in file app.properties");
        }

    }

    /**
     * check db is exists
     * @return true/false
     */
    private static boolean checkDB() {
        final File file = new File(Schema.path, Schema.dbName);
        return file.exists();
    }

    /**
     * copy db to dir storage
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    private static void copyDB() throws IOException {
        helper.getReadableDatabase();
        InputStream is = context.getAssets().open(Schema.dbName);
        String outfilename = Schema.path + Schema.dbName;
        OutputStream os = new FileOutputStream(outfilename);
        byte[] buffer = new byte[10240];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        os.flush();
        os.close();
        is.close();
    }


    public void setDB(SQLiteDatabase db) {
        this.db = db;
    }

    public void createNew(String table) {
        // delete table
        execSQL("DELETE FROM " + table);
    }

    /**
     * create native query
     * @param sql query string
     * @return Result set obj
     */
    public ResultSet query(String sql) {
        if (db == null) {
            setMessage(500, "The database does not open");
            throw new Error("The database does not open");
        }
        Cursor cursor = db.rawQuery(sql, null);
        return new ResultSet(this, cursor);
    }

    /**
     * create native query
     * @param sql query string
     * @return cursor obj
     */
    public Cursor rawQuery(String sql) {
        return db.rawQuery(sql, null);
    }

    /**
     * exec sql query
     * @param sql string query
     * @return true exec is successfuly
     */
    public boolean execSQL(String sql) {
        try {
            db.execSQL(sql);
            setMessage(200, "Query executed successfully: " + sql);
            return true;
        } catch (SQLException ex) {
            setMessage(417, ex.getMessage());
            return false;
        }
    }

    /**
     * get message log
     * @return string message
     */
    public String getMessage() {
        return message.toString();
    }

    /**
     * set message log
     * @param code code log
     * @param msg message log
     */
    public void setMessage(int code, String msg) {
        message.append("[");
        message.append(code);
        message.append("]");
        message.append(" ");
        message.append(msg);
    }

    /**
     * select table
     * @param tableName table name
     * @return Select obj
     */
    public SqliteSelect from(String tableName) {
        return new SqliteSelect(this, tableName);
    }

    /**
     * insert table
     * @param tableName table name
     * @return Insert obj
     */
    public SqliteInsert insert(String tableName) {
        return new SqliteInsert(this, tableName);
    }

    /**
     * create table
     * @param tablename table name
     * @return Create obj
     */
    public SqliteCreate create(String tablename) {
        return new SqliteCreate(this, tablename);
    }

    /**
     * drop table
     * @param tableName table name
     */
    public void drop(String tableName) {
        execSQL("DROP TABLE " + tableName);
    }

    /**
     * update data table
     * @param tablename table name
     * @return Update obj
     */
    public SqliteUpdate update(String tablename) {
        return new SqliteUpdate(this, tablename);
    }


}