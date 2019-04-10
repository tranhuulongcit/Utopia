package com.estrouge.utopia.services;

import android.util.Log;

import com.estrouge.utopia.db.ResultSet;
import com.estrouge.utopia.db.Schema;
import com.estrouge.utopia.db.Table;
import com.estrouge.utopia.db.TableRow;
import com.estrouge.utopia.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longtran on 4/9/19.
 */

public class CitiesService {

    private CitiesServiceImpl calback;
    private int totalPage = 0;

    public CitiesService(CitiesServiceImpl calback) {
        this.calback = calback;
        calcTotalPage();
    }


    /**
     * calculate total page
     */
    private void calcTotalPage() {
        ResultSet rs = Schema.open().query("SELECT Count(*) as total from " + Table.Cities.NAME);
        if (rs != null && rs.get() != null) {
            totalPage = Math.round(rs.get().getInt("total") / 20);
        }
    }

    /**
     * handle fetch data from db
     * @param page current page
     */
    public void getCities(int page){
        if (page > totalPage) {
           calback.isloadDone();
        }

        try {
            int offset = (page - 1) * 20;
            List<TableRow> tableRows = Schema.open().query("SELECT * FROM " + Table.Cities.NAME + " LIMIT 20 OFFSET " + offset).getAll();
            List<City> cities = new ArrayList<>();
            City city = null;
            for (TableRow tr : tableRows) {
                city = new City();
                city.setId(tr.getText(Table.Cities.Column.ID));
                city.setCity(tr.getText(Table.Cities.Column.CITY));
                city.setCountry(tr.getText(Table.Cities.Column.COUNTRY));
                city.setPopulation(tr.getInt(Table.Cities.Column.POPULATION));
                cities.add(city);
            }
            calback.receiveData(cities);
        } catch (Exception ex) {
            Log.e("getCities", "cannot fetch data from database!");
            calback.receiveData(null);
        }

    }

    /**
     * inteface callback data from model to prisenter
     */
    public interface CitiesServiceImpl {
        void receiveData(List<City> cities);
        void isloadDone();
    }


}
