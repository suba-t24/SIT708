package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdvertListActivity extends AppCompatActivity {

    LinearLayout advertContainer;
    Spinner spinnerFilter;
    DatabaseHelper db;

    String[] filterOptions = {"All", "Electronics", "Pets", "Wallets", "Documents", "Keys", "Bags", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_list);

        db = new DatabaseHelper(this);

        advertContainer = findViewById(R.id.advertContainer);
        spinnerFilter = findViewById(R.id.spinnerFilter);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                filterOptions
        );
        spinnerFilter.setAdapter(adapter);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadAdverts(filterOptions[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdverts(spinnerFilter.getSelectedItem().toString());
    }

    private void loadAdverts(String category) {
        advertContainer.removeAllViews();

        ArrayList<Advert> adverts;

        if (category.equals("All")) {
            adverts = db.getAllAdverts();
        } else {
            adverts = db.getAdvertsByCategory(category);
        }

        if (adverts.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No lost or found items available.");
            emptyText.setTextSize(16);
            emptyText.setPadding(20, 40, 20, 20);
            advertContainer.addView(emptyText);
            return;
        }

        for (Advert advert : adverts) {
            View itemView = getLayoutInflater().inflate(R.layout.item_advert, advertContainer, false);

            TextView txtTitle = itemView.findViewById(R.id.txtItemTitle);
            TextView txtCategory = itemView.findViewById(R.id.txtItemCategory);
            TextView txtTime = itemView.findViewById(R.id.txtItemTime);

            txtTitle.setText(advert.type + ": " + advert.name);
            txtCategory.setText("Category: " + advert.category);
            txtTime.setText("Posted: " + advert.timestamp);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(AdvertListActivity.this, AdvertDetailActivity.class);
                intent.putExtra("advert_id", advert.id);
                startActivity(intent);
            });

            advertContainer.addView(itemView);
        }
    }
}