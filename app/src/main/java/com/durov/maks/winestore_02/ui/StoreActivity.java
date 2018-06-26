package com.durov.maks.winestore_02.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.durov.maks.winestore_02.R;
import com.durov.maks.winestore_02.model.Store;

public class StoreActivity extends AppCompatActivity {

    private Store store;
    private TextView textViewStoreName,textViewStoreCity,textViewStoreAddress;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        //init
        Intent intent = getIntent();
        store =(Store) intent.getSerializableExtra(MainActivity.EXTRA_STORE);
        recyclerView = findViewById(R.id.activity_store_recyclerview);
        textViewStoreName = findViewById(R.id.activity_store_store_name);
        textViewStoreCity = findViewById(R.id.activity_store_store_city);
        textViewStoreAddress = findViewById(R.id.activity_store_adress);



        //fill all information store
        fillStoreFields();
    }
    private void fillStoreFields(){
        textViewStoreName.setText(store.getName());
        textViewStoreCity.setText(store.getCity());
        textViewStoreAddress.setText(store.getFullAdress());
    }
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.store_activity_button_show_on_map:
                startMapActivity();
        }
    }
    private void startMapActivity(){
        /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:geo:0,0?q="+String.valueOf(store.getLatitude())
                +","+String.valueOf(store.getLongitude())+"("+store.getName()+")"));
        startActivity(intent);*/
        Uri gmmIntentUri = Uri.parse("google.maps:"+String.valueOf(store.getLatitude())+","+String.valueOf(store.getLongitude()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

}
