package com.estrouge.utopia.db;

/**
 * Created by longtran on 4/10/19.
 */

public abstract class Table {
    public static abstract class Cities {
        public static final String NAME = "cities";
        public static abstract class Column {
            public static final String ID = "id";
            public static final String CITY = "city";
            public static final String COUNTRY = "country";
            public static final String POPULATION = "population";
        }
    }

}
