package com.estrouge.utopia.presenter;

import com.estrouge.utopia.db.ResultSet;
import com.estrouge.utopia.db.Schema;
import com.estrouge.utopia.db.Table;
import com.estrouge.utopia.model.City;
import com.estrouge.utopia.services.UtopiaService;

import java.util.List;

/**
 * Created by longtran on 4/9/19.
 */

public class UtopiaPresenter implements UtopiaService.UtopiaServiceImpl {

    private UtopiaService service;
    private IView view;
    private int page;


    public UtopiaPresenter(IView view) {
        this.view = view;
        page = 0;
        service = new UtopiaService(this);
    }

    /**
     * handle call to model
     */
    public void fetchData() {
        service.getCities(++page);
    }

    /**
     * listener event callback
     * @param cities list cities receive form model
     */
    @Override
    public void receiveData(List<City> cities) {
        if (cities == null) {
            page = page - 1;
        }
        view.onReceiveCities(cities);
    }

    /**
     * listener from model if load done
     */
    @Override
    public void isloadDone() {
        view.onNotifyDataLoadDone();
    }

    /**
     * inteface callback data from presenter to view
     */
    public interface IView {
        void onReceiveCities(List<City> cities);
        void onNotifyDataLoadDone();
    }
}
