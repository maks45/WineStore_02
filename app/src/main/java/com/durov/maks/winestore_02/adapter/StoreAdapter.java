package com.durov.maks.winestore_02.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.durov.maks.winestore_02.R;
import com.durov.maks.winestore_02.model.Store;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Store> stores;
    private OnItemClicked onClick;
    private static final int PROGRESS_BAR_ITEM_VIEW = 1;
    private boolean isLoadData = false; //if true add progress bar to end of list


    public StoreAdapter(ArrayList<Store> stores){
        this.stores = stores;
        }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        View view;
        if(viewType == PROGRESS_BAR_ITEM_VIEW) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_with_progressbar, viewGroup, false);
            ProgressBarViewHolder progressBarViewHolder = new ProgressBarViewHolder(view);
            return progressBarViewHolder;
        }else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.store_item_view, viewGroup, false);
            StoreViewHolder viewHolder = new StoreViewHolder(view);
            return viewHolder;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i){
            if (viewHolder instanceof StoreViewHolder) {
                StoreViewHolder storeViewHolder = (StoreViewHolder) viewHolder;
                storeViewHolder.textViewStoreName.setText(stores.get(i).getName());
                storeViewHolder.textViewStoreNo.setText(String.valueOf(stores.get(i).getId()));
                storeViewHolder.textViewStoreAdress1.setText(stores.get(i).getAddressLine1());
                storeViewHolder.textViewStoreCity.setText(stores.get(i).getCity());
                storeViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClick.onItemClick(stores.get(i));
                    }
                });

            } else if (viewHolder instanceof ProgressBarViewHolder) {
                ProgressBarViewHolder progressBarViewHolder = (ProgressBarViewHolder) viewHolder;
            }
    }

    @Override
    public int getItemCount(){
        if(stores == null && isLoadData){
            return 1;
        }else if(stores.size()==0 && isLoadData){
            return 0;
        }else if(isLoadData) {
            return stores.size()+1;
        }
        return stores.size();
        }

    @Override
    public int getItemViewType(int position) {
        if (position == stores.size()) {
            return PROGRESS_BAR_ITEM_VIEW;
        }
        return super.getItemViewType(position);
    }

    public void setLoadData(boolean loadData){
        this.isLoadData = loadData;
    }

    public boolean isLoadData() {
        return isLoadData;
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewStoreName,textViewStoreNo,textViewStoreAdress1,textViewStoreCity;
        private LinearLayout rootView;
        public StoreViewHolder(View view) {
            super(view);
            rootView = (LinearLayout) view.findViewById(R.id.store_item_view_root_view);
            textViewStoreName = (TextView)view.findViewById(R.id.store_item_view_store_name);
            textViewStoreNo =(TextView) view.findViewById(R.id.store_item_view_store_no);
            textViewStoreAdress1 =(TextView) view.findViewById(R.id.store_item_view_store_adress_1);
            textViewStoreCity =(TextView) view.findViewById(R.id.store_item_view_store_city);
        }
    }
    public class ProgressBarViewHolder extends RecyclerView.ViewHolder{
        private ProgressBar progressBar;
        public ProgressBarViewHolder(@NonNull View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        }
    }

    public void setOnClick(OnItemClicked onClick){this.onClick=onClick;}
    public interface OnItemClicked {
        void onItemClick(Store store);
    }

}