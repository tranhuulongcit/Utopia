package com.estrouge.utopia.db;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by longtran on 4/9/19.
 */

public class ResultSet {
    private static final String TAG = ResultSet.class.getSimpleName();
    //list row
    private List<TableRow> dataList = new ArrayList<>();
    //cursor
    private Cursor cursor;
    //row current
    private TableRow rowCurrent;
    //possition current
    private int position;
    //number of record
    private int count;
    //schema
    private Schema schema;
    //array column name
    private String[] headers;

    /**
     * construct ResultSet
     * @param schema schema
     * @param cursor cursor
     */
    public ResultSet(Schema schema, Cursor cursor) {
        this.schema = schema;
        this.cursor = cursor;
        this.position = 0;
        appendData();
    }

    /**
     * append data cursor for datalist
     */
    private void appendData() {
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            this.headers = cursor.getColumnNames();
            while (!cursor.isAfterLast()) {
                HashMap<String, Object> row = new HashMap<>();
                for (int i = 0, count = cursor.getColumnCount(); i < count; i++)
                    row.put(cursor.getColumnName(i), cursor.getString(i));
                dataList.add(cursor.getPosition(), new TableRow(headers, row));
                cursor.moveToNext();
            }
        }
        if (!dataList.isEmpty()) {
            this.rowCurrent = dataList.get(position);
            this.count = dataList.size();

        }
    }

    /**
     * next possition
     * @return last position
     */
    public boolean next() {
        schema.setMessage(000, "next");
        if (position < count) {
            this.rowCurrent = dataList.get(position);
            position++;
            return true;
        }
        return false;
    }

    /**
     * get row current
     * @return data row current
     */
    public TableRow get() {
        return rowCurrent;
    }

    /**
     * get row position
     * @param position position row
     * @return data row
     */
    public TableRow get(int position) {
        if (position < count)
            return dataList.get(position);
        schema.setMessage(404, "null");
        return null;
    }

    /**
     * get first row
     * @return data row
     */
    public TableRow first() {
        if (dataList.size() > 0)
            return dataList.get(0);
        schema.setMessage(404, "null");
        return null;
    }

    /**
     * count total row
     * @return number row of query
     */
    public int count() {
        schema.setMessage(000, "count " + count);
        return count;
    }

    /**
     * check data isexists
     * @return true/false
     */
    public boolean exist() {
        return count() > 0;
    }

    /**
     * get schema
     * @return schema
     */
    public Schema getSchema() {
        return schema;
    }

    /**
     * get all row of query
     * @return list row
     */
    public List<TableRow> getAll() {
        if (dataList != null) {
            schema.setMessage(200, "get all");
            return dataList;
        }
        schema.setMessage(200, "null");
        return null;
    }


}
