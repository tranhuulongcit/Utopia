package com.estrouge.utopia.db;

/**
 * Created by longtran on 4/9/19.
 */

public class SqliteUpdate {
    private static final String TAG = "UpdateSQLite";
    //schema
    private final Schema schema;
    //table name
    private final String tablename;
    //column update
    private StringBuilder mData = new StringBuilder();
    //update condition
    private StringBuilder mWhere = new StringBuilder();

    /**
     * construct Update
     * @param schema schema
     * @param tablename table name
     */
    public SqliteUpdate(Schema schema, String tablename) {
        this.schema = schema;
        this.tablename = tablename;
    }

    /**
     * set data column update
     * @param column column name
     * @param arg data update
     * @return this
     */
    public SqliteUpdate set(String column, Object arg) {
        if (mData.length() > 0)
            mData.append(", ");

        if (arg instanceof String)
            arg = "'" + arg + "'";
        if (arg instanceof Boolean)
            arg = arg.equals(true) ? 1 : 0;

        mData.append(column);
        mData.append(" = ");
        mData.append(arg);
        return this;
    }

    /**
     * update condition (or)
     * @param column column name
     * @param arg data update
     * @return this
     */
    public SqliteUpdate where(String column, Object arg) {
        if (mWhere.length() > 0)
            mWhere.append(" AND ");
        mWhere.append(column);
        mWhere.append(" = ");

        if (arg instanceof String)
            arg = "'" + arg + "'";

        if (arg instanceof Boolean)
            arg = arg.equals(true) ? 1 : 0;

        mWhere.append(arg);
        return this;
    }
    /**
     * update condition (and)
     * @param column column name
     * @param arg data update
     * @return this
     */
    public SqliteUpdate orWhere(String column, Object arg) {
        if (mWhere.length() > 0)
            mWhere.append(" OR ");
        mWhere.append(column);
        mWhere.append(" = ");

        if (arg instanceof String)
            arg = "'" + arg + "'";
        if (arg instanceof Boolean)
            arg = arg.equals(true) ? 1 : 0;

        mWhere.append(arg);
        return this;
    }

    /**
     * Add update for query string
     * @param sql string query
     */
    private void addSet(final StringBuilder sql) {
        if (mData.length() != 0) {
            sql.append("SET ");
            sql.append(mData);
            sql.append(" ");
        }
    }

    /**
     * Add condition for query
     * @param sql string query
     */
    private void addWhere(final StringBuilder sql) {
        if (mWhere.length() > 0) {
            sql.append("WHERE ");
            sql.append(mWhere);
            sql.append(" ");
        }
    }

    /**
     * build query and exec
     * @return true if update successfuly
     */
    public boolean execute() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("UPDATE %s ", tablename));
        addSet(sql);
        addWhere(sql);
        return schema.execSQL(sql.toString());
    }
}
