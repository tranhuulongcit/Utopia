package com.estrouge.utopia;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.estrouge.utopia.adapter.CityAdapter;
import com.estrouge.utopia.databinding.ActivityUtopiaBinding;
import com.estrouge.utopia.model.City;
import com.estrouge.utopia.presenter.CitiesPresenter;

import java.util.ArrayList;
import java.util.List;

public class UtopiaActivity extends AppCompatActivity implements CitiesPresenter.IView {

    private ActivityUtopiaBinding binding;
    private CityAdapter adapter;
    private List<City> cities;
    private CitiesPresenter presenter;

    /**
     * handle init data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_utopia);
        presenter = new CitiesPresenter(this);
        initView();
    }

    /**
     * init recycleview and handle callback load more
     */
    void initView() {
        cities = new ArrayList<>();
        adapter = new CityAdapter(binding.listItem, cities);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.listItem.getContext(),
                linearLayoutManager.getOrientation());
        binding.listItem.setLayoutManager(linearLayoutManager);
        binding.listItem.addItemDecoration(dividerItemDecoration);
        binding.listItem.setAdapter(adapter);
        adapter.setLoadMore(new CityAdapter.ILoadMore() {
            @Override
            public void onLoadMore() {
                binding.listItem.post(new Runnable() {
                    public void run() {
                        //show load more view
                        cities.add(null);
                        adapter.notifyItemInserted(cities.size()-1);
                    }
                });
                binding.listItem.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //hide loadmore view
                        cities.remove(cities.size()-1);
                        adapter.notifyItemRemoved(cities.size());
                        //fetch data from db
                        presenter.fetchData();
                    }
                }, 500);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null) {
            presenter.fetchData();
        }
    }

    /**
     * callback update data for view
     * @param cities
     */
    @Override
    public void onReceiveCities(List<City> cities) {
        if (cities != null) {
            int position = this.cities.size();
            for (City c : cities) {
                this.cities.add(c);
                adapter.notifyItemInserted(position++);
            }

        } else {
            Toast.makeText(this, getString(R.string.load_list_error), Toast.LENGTH_LONG).show();
        }
        adapter.setLoaded();
    }

    /**
     * show message if load done
     */
    @Override
    public void onNotifyDataLoadDone() {
        Toast.makeText(this, getString(R.string.load_list_done), Toast.LENGTH_LONG).show();
    }
}
