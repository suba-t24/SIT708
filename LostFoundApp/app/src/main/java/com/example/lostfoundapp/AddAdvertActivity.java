package com.example.lostfoundapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddAdvertActivity extends AppCompatActivity {

    RadioGroup radioGroupType;
    EditText edtName, edtPhone, edtDescription, edtDate, edtLocation;
    Spinner spinnerCategory;
    ImageView imagePreview;
    Button btnChooseImage, btnSave;

    Uri selectedImageUri = null;
    DatabaseHelper db;

    String[] categories = {"Electronics", "Pets", "Wallets", "Documents", "Keys", "Bags", "Others"};

    ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    imagePreview.setImageURI(selectedImageUri);

                    final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    try {
                        getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
                    } catch (Exception ignored) {}
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advert);

        db = new DatabaseHelper(this);

        radioGroupType = findViewById(R.id.radioGroupType);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtDescription = findViewById(R.id.edtDescription);
        edtDate = findViewById(R.id.edtDate);
        edtLocation = findViewById(R.id.edtLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imagePreview = findViewById(R.id.imagePreview);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        edtDate.setOnClickListener(v -> showDatePicker());

        btnChooseImage.setOnClickListener(v -> openImagePicker());

        btnSave.setOnClickListener(v -> saveAdvert());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    edtDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        imagePickerLauncher.launch(intent);
    }

    private void saveAdvert() {
        int selectedTypeId = radioGroupType.getCheckedRadioButtonId();

        if (selectedTypeId == -1) {
            Toast.makeText(this, "Please select Lost or Found", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadio = findViewById(selectedTypeId);
        String type = selectedRadio.getText().toString();

        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String date = edtDate.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty()
                || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String timestamp = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
                .format(new Date());

        boolean inserted = db.insertAdvert(
                type,
                name,
                phone,
                description,
                date,
                location,
                category,
                selectedImageUri.toString(),
                timestamp
        );

        if (inserted) {
            Toast.makeText(this, "Advert saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save advert", Toast.LENGTH_SHORT).show();
        }
    }
}