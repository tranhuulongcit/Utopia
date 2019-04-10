package com.estrouge.utopia.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.estrouge.utopia.R;
import com.estrouge.utopia.databinding.ItemCityBinding;
import com.estrouge.utopia.databinding.ItemLoadingBinding;
import com.estrouge.utopia.model.City;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by longtran on 4/9/19.
 */

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    ILoadMore loadMore;
    boolean isLoading;
    int visibleThreshold=5;
    int lastVisibleItem,totalItemCount;
    private List<City> cities;

    /**
     * construct CityAdapter
     * @param rc recycle view
     * @param cities list cities
     */
    public CityAdapter(RecyclerView rc, List<City> cities) {
        this.cities = cities;
        rc.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = recyclerView.getLayoutManager().getItemCount();
                lastVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                if(!isLoading && totalItemCount <= (lastVisibleItem+visibleThreshold))
                {
                    if(loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }

            }
        });
    }

    /**
     * Used to assign an interface to an element of the RecyclerView
     * @param parent view parent
     * @param viewType view type
     * @return viewholder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            ItemCityBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_city, parent, false);
            return new ItemViewHolder(binding);
        } else if(viewType == VIEW_TYPE_LOADING) {
            ItemLoadingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_loading, parent, false);
            return new LoadingViewHolder(binding);
        }
        return null;
    }

    /**
     * set load more event callback listener
     * @param loadMore event callback
     */
    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }


    public void setLoaded() {
        isLoading = false;
    }

    /**
     * Used to assign data from listData to viewHolder
     * @param holder viewholder
     * @param position possition of object data
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  ItemViewHolder)
        {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.setViewHolder(cities.get(position));
        }
        else if(holder instanceof LoadingViewHolder)
        {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.showProgress();
        }
    }

    /**
     * Returns the number of elements
     * @return number element
     */
    @Override
    public int getItemCount() {
        return cities.size();
    }

    /**
     * using handle get view type
     * @param position item position
     * @return view type
     */
    @Override
    public int getItemViewType(int position) {
        return cities.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    /**
     * This class controls the view better, avoiding the findViewById many times
     */
    public class LoadingViewHolder extends RecyclerView.ViewHolder
    {
        private ItemLoadingBinding loadingBinding;

        /**
         * construct ViewHolder
         * @param binding layout binding object
         */
        public LoadingViewHolder(ItemLoadingBinding binding) {
            super(binding.getRoot());
            this.loadingBinding = binding;
        }

        /**
         * show progress
         */
        public void showProgress() {
            loadingBinding.loadMore.setIndeterminate(true);
        }
    }

    /**
     * This class controls the view better, avoiding the findViewById many times
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ObservableField<String> city = new ObservableField<>();
        public ObservableField<String> country = new ObservableField<>();
        public ObservableField<String> population = new ObservableField<>();
        private DecimalFormat df = null;
        private ItemCityBinding binding;

        /**
         * construct ViewHolder
         * @param binding layout binding object
         */
        public ItemViewHolder(ItemCityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            df = new DecimalFormat("###,###,###");
        }

        /**
         * set View Holder
         * @param city obj contains the data to be sent
         */
        public void setViewHolder(City city) {
            if (binding.getViewHolder() == null) {
                binding.setViewHolder(this);
            }
            this.city.set(city.getCity());
            this.country.set(city.getCountry());
            this.population.set(df.format(city.getPopulation()));
        }
    }

    /**
     * inteface listener event load more
     */
    public interface ILoadMore {
        void onLoadMore();
    }
}
