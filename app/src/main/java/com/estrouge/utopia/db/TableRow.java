package com.estrouge.utopia.db;

import java.util.HashMap;

/**
 * Created by longtran on 4/9/19.
 */

public class TableRow {
    //map key and value
    private HashMap<String, Object> dataRow;
    //key array
    private String[] keys;

    /**
     * construct tableRow
     * @param keys
     * @param dataRow
     */
    public TableRow(String[] keys, HashMap<String, Object> dataRow) {
        this.keys = keys;
        this.dataRow = dataRow;
    }

    /**
     * Get the column whose data type is integer
     * @param column column name
     * @return column data
     */
    public int getInt(String column) {
        return Integer.parseInt(dataRow.get(column).toString());
    }

    /**
     * Get the column whose data type is real
     * @param column column name
     * @return column data
     */
    public double getReal(String column) {
        return Double.parseDouble(dataRow.get(column).toString());
    }

    /**
     * Get the column whose data type is String
     * @param column column name
     * @return column data
     */
    public String getText(String column) {
        return (String) dataRow.get(column);
    }

    /**
     * Get the column whose data type is boolean
     * @param column column name
     * @return column data
     */
    public boolean getBlob(String column) {
        return getInt(column) == 0 ? false : true;
    }

    /**
     * convert data row to string array
     * @return String array
     */
    public String[] toStringArray() {
        String[] arr = new String[dataRow.size()];
        for (int i = 0, len = keys.length; i < len; i++) {
            arr[i] = dataRow.get(keys[i]) == null ? "null" : dataRow.get(keys[i]).toString();
        }
        return arr;
    }

    /**
     * convert data row to string
     * @return string data
     */
    public String toString() {
        StringBuilder log = new StringBuilder();
        for (String key : keys) {
            log.append(dataRow.get(key));
            log.append("\t");
            log.append("|");
            log.append("\t");
        }
        return log.toString();
    }

}

