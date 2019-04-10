package com.estrouge.utopia.db;

import java.util.List;

/**
 * Created by longtran on 4/9/19.
 */

public class SqliteSelect {

    private static final String TAG = "FromSQLite";
    //table name
    private String tableName;
    //schema
    private Schema schema;
    //where clause
    private StringBuilder mWhere = new StringBuilder();
    //order clause
    private StringBuilder mOrderBy = new StringBuilder();
    //having clause
    private StringBuilder mHaving = new StringBuilder();
    //like clause
    private StringBuilder mLike = new StringBuilder();
    //group clause
    private String mGroupBy = "";

    /**
     * constrcut select
     * @param schema schema
     * @param tableName table name
     */
    public SqliteSelect(Schema schema, String tableName) {
        this.schema = schema;
        this.tableName = tableName;

    }

    /**
     * where (and) clause
     * @param column column name
     * @param arg value
     * @return this
     */
    public SqliteSelect where(String column, Object arg) {
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
     * where (or) clause
     * @param column column name
     * @param arg value
     * @return this
     */
    public SqliteSelect orWhere(String column, Object arg) {
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
     * select like clause
     * @param column column name
     * @param arg value
     * @return this
     */
    public SqliteSelect like(String column, String arg) {
        if (mLike.length() > 0)
            mLike.append(" OR ");
        mLike.append(column);
        mLike.append(" LIKE ");
        mLike.append("'");
        mLike.append(arg);
        mLike.append("'");
        return this;
    }

    /**
     * order by data clause
     * @param column column name
     * @param type type order
     * @return this
     */
    public SqliteSelect orderBy(String column, Schema.OrderByType type) {
        if (mOrderBy.length() > 0)
            mOrderBy.append(", ");
        mOrderBy.append(column);
        mOrderBy.append(" ");
        mOrderBy.append(type.toString());
        return this;
    }

    /**
     * group clause
     * @param groupBy group by column
     * @return this
     */
    public SqliteSelect groupBy(String groupBy) {
        mGroupBy = groupBy;
        return this;
    }

    /**
     * having clause
     * @param column colum name or function contains column names
     * @param operator operator
     * @param count condition
     * @return this
     */
    public SqliteSelect having(String column, char operator, int count) {
        mHaving.append(column);
        mHaving.append(operator);
        mHaving.append(count);
        return this;
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
     * Add clause order for query
     * @param sql string query
     */
    private void addOrderBy(final StringBuilder sql) {
        if (mOrderBy.length() != 0) {
            sql.append("ORDER BY ");
            sql.append(mOrderBy);
            sql.append(" ");
        }
    }

    /**
     * Add clause like for query
     * @param sql string query
     */
    private void addLike(final StringBuilder sql) {

        if (mLike.length() != 0) {
            sql.append("WHERE ");
            sql.append(mLike);
            sql.append(" ");
        }
    }

    /**
     * Add clause having for query
     * @param sql string query
     */
    private void addHaving(final StringBuilder sql) {
        if (mHaving.length() != 0) {
            sql.append("HAVING ");
            sql.append(mHaving);
            sql.append(" ");
        }

    }

    /**
     * Add clause group for query
     * @param sql string query
     */
    private void addGroupBy(final StringBuilder sql) {
        if (mGroupBy.length() != 0) {
            sql.append("GROUP BY ");
            sql.append(sql);
            sql.append(" ");
        }
    }

    /**
     * Counts the number of elements selected
     * @return number row
     */
    public int count() {
        return query().count();
    }

    /**
     * get first row
     * @return first row
     */
    public TableRow first() {
        return query().first();
    }

    /**
     * get row index
     * @param index index
     * @return table row
     */
    public TableRow get(int index) {
        return query().get(index);
    }

    /**
     * get list table row
     * @return list table row
     */
    public List<TableRow> getAll() {
        return query().getAll();
    }

    /**
     * build query and exec
     * @return ResultSet obj
     */
    public ResultSet query() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("SELECT * FROM %s ", tableName));
        addWhere(sql);
        addLike(sql);
        addGroupBy(sql);
        addHaving(sql);
        addOrderBy(sql);
        return schema.query(sql.toString());
    }

    /**
     * delete data
     * @return Successfully deleted or failed
     */
    public boolean delete() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("DELETE FROM %s ", tableName));
        addWhere(sql);
        return schema.execSQL(sql.toString());
    }
}
