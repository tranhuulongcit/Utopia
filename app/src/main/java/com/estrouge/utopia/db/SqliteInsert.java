package com.estrouge.utopia.db;

/**
 * Created by longtran on 4/9/19.
 */
public class SqliteInsert {

    private static final String TAG = "InsertSQLite";
    //schema
    private Schema schema;
    //table name
    private String tableName;
    //column need updating
    private StringBuilder mInsertHeader = new StringBuilder();
    //data update
    private StringBuilder mInsertData = new StringBuilder();

    /**
     * construct Insert
     * @param schema schema
     * @param tableName table name
     */
    public SqliteInsert(Schema schema, String tableName) {
        this.schema = schema;
        this.tableName = tableName;
    }

    /**
     * set key and value column
     * @param column column name
     * @param arg value
     * @return this
     */
    public SqliteInsert set(String column, Object arg) {
        // bind
        if (mInsertHeader.length() > 0)
            mInsertHeader.append(", ");
        mInsertHeader.append(column);

        if (mInsertData.length() > 0)
            mInsertData.append(", ");

        if (arg instanceof String)
            arg = "'" + arg + "'";

        if (arg instanceof Boolean)
            arg = arg.equals(true) ? 1 : 0;

        mInsertData.append(arg);
        return this;
    }

    /**
     * add column.
     * @param sql string append.
     */
    private void addHeader(StringBuilder sql) {
        sql.append("( ");
        sql.append(mInsertHeader);
        sql.append(" )");
    }

    /**
     * add data.
     * @param sql string append.
     */
    private void addData(StringBuilder sql) {
        sql.append("( ");
        sql.append(mInsertData);
        sql.append(" )");
    }

    /**
     * build string and exec query.
     * @return true if exec successfuly.
     */
    public boolean save() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("INSERT INTO %s ", tableName));
        addHeader(sql);
        sql.append(" VALUES ");
        addData(sql);
        sql.append(";");
        return schema.execSQL(sql.toString());
    }


}
