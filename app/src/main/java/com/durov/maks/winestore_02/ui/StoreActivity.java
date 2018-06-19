package com.durov.maks.winestore_02.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.durov.maks.winestore_02.R;
import com.durov.maks.winestore_02.model.Store;

public class StoreActivity extends AppCompatActivity {

    private Store store;
    private TextView textViewStoreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        //init
        Intent intent = getIntent();
        store =(Store) intent.getSerializableExtra(MainActivity.EXTRA_STORE);
        textViewStoreName = findViewById(R.id.activity_store_store_name);



        //fill all information store
        fillStoreFields();
    }
    private void fillStoreFields(){
        this.textViewStoreName.setText(store.getName());
    }

}
