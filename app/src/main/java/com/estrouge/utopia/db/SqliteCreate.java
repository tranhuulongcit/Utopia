package com.estrouge.utopia.db;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by longtran on 4/9/19.
 */

public class SqliteCreate {
    //scheme
    private Schema schema;
    //table name
    private String tablename;
    //definitions column
    private ArrayList<String> definitions = new ArrayList<>();

    /**
     * construct Create
     * @param schema schema
     * @param tablename table name
     */
    public SqliteCreate(Schema schema, String tablename) {
        this.schema = schema;
        this.tablename = tablename;
    }

    /**
     * create column auto increment
     *
     * @param id column id
     * @return this
     */
    public SqliteCreate increments(String id) {
        createString(id);
        return this;
    }

    /**
     * add column
     *
     * @param name column name
     * @param type var type
     * @return this
     */
    public SqliteCreate add(String name, SQLiteType type) {
        createString(name, type.toString());
        return this;
    }

    /**
     * add column have the option
     *
     * @param name   column name
     * @param type   var type
     * @param option option
     * @return this
     */
    public SqliteCreate add(String name, SQLiteType type, String option) {
        createString(name, type.toString(), option.toUpperCase().trim());
        return this;
    }

    /**
     * create string
     *
     * @param parrams array parrams
     */
    private void createString(Object... parrams) {
        if (parrams.length > 0) {
            StringBuilder definition = new StringBuilder();
            if (parrams.length > 2) {
                definition.append(parrams[0]);
                definition.append(" ");
                definition.append(parrams[1]);
                definition.append(" ");
                definition.append(parrams[2]);
            } else if(parrams.length > 1){
                definition.append(parrams[0]);
                definition.append(" ");
                definition.append(parrams[1]);
                definition.append(" NULL");
            } else {
                definition.append(parrams[0]);
                definition.append(" ");
                definition.append("INTEGER");
                definition.append(" PRIMARY KEY AUTOINCREMENT");
            }
            definitions.add(definition.toString());
        }
    }

    /**
     * build string and execute sql
     *
     * @return is exec successfuly
     */
    public boolean build() {
        String column = TextUtils.join(", ", definitions);
        if (!column.contains("PRIMARY KEY")) {
            StringBuilder name = new StringBuilder();
            name.append("id");
            name.append(tablename);
            this.add(name.toString(), SQLiteType.INTEGER, "PRIMARY KEY AUTOINCREMENT");
        }
        // if the existing table does not create again
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s);", tablename, TextUtils.join(", ", definitions));
        return schema.execSQL(sql);
    }


}