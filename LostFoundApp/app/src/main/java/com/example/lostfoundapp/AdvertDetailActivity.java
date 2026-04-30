package com.example.lostfoundapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AdvertDetailActivity extends AppCompatActivity {

    TextView txtTitle, txtName, txtPhone, txtDescription, txtDate, txtLocation, txtCategory, txtTimestamp;
    ImageView detailImage;
    Button btnRemove;

    DatabaseHelper db;
    int advertId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_detail);

        db = new DatabaseHelper(this);

        advertId = getIntent().getIntExtra("advert_id", -1);

        txtTitle = findViewById(R.id.txtTitle);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtDescription = findViewById(R.id.txtDescription);
        txtDate = findViewById(R.id.txtDate);
        txtLocation = findViewById(R.id.txtLocation);
        txtCategory = findViewById(R.id.txtCategory);
        txtTimestamp = findViewById(R.id.txtTimestamp);
        detailImage = findViewById(R.id.detailImage);
        btnRemove = findViewById(R.id.btnRemove);

        loadAdvert();

        btnRemove.setOnClickListener(v -> {
            boolean deleted = db.deleteAdvert(advertId);

            if (deleted) {
                Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to remove advert", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAdvert() {
        Advert advert = db.getAdvertById(advertId);

        if (advert == null) {
            Toast.makeText(this, "Advert not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtTitle.setText(advert.type + " Item Details");
        txtName.setText("Name: " + advert.name);
        txtPhone.setText("Phone: " + advert.phone);
        txtDescription.setText("Description: " + advert.description);
        txtDate.setText("Lost/Found Date: " + advert.date);
        txtLocation.setText("Location: " + advert.location);
        txtCategory.setText("Category: " + advert.category);
        txtTimestamp.setText("Posted On: " + advert.timestamp);

        try {
            detailImage.setImageURI(Uri.parse(advert.imageUri));
        } catch (Exception e) {
            detailImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }
}