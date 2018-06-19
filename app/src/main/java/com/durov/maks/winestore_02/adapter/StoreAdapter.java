package com.durov.maks.winestore_02.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.durov.maks.winestore_02.R;
import com.durov.maks.winestore_02.model.Store;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private ArrayList<Store> stores;

    private OnItemClicked onClick;


    public StoreAdapter(ArrayList<Store> stores){
        this.stores = stores;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.store_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textViewStoreName.setText(stores.get(i).getName());
        viewHolder.textViewStoreNo.setText(String.valueOf(stores.get(i).getId()));
        viewHolder.textViewStoreAdress1.setText(stores.get(i).getAddressLine1());
        viewHolder.textViewStoreCity.setText(stores.get(i).getCity());
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(stores.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewStoreName,textViewStoreNo,textViewStoreAdress1,textViewStoreCity;
        private LinearLayout rootView;
        public ViewHolder(View view) {
            super(view);
            rootView = (LinearLayout) view.findViewById(R.id.store_item_view_root_view);
            textViewStoreName = (TextView)view.findViewById(R.id.store_item_view_store_name);
            textViewStoreNo =(TextView) view.findViewById(R.id.store_item_view_store_no);
            textViewStoreAdress1 =(TextView) view.findViewById(R.id.store_item_view_store_adress_1);
            textViewStoreCity =(TextView) view.findViewById(R.id.store_item_view_store_city);

        }
    }
    public void setOnClick(OnItemClicked onClick){this.onClick=onClick;}
    public interface OnItemClicked {
        void onItemClick(Store store);
    }
}
