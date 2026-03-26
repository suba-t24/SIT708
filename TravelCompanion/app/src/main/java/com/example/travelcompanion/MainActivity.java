package com.example.travelcompanion;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.ViewCompat;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCurrency;
    private Button btnFuel;
    private Button btnTemp;
    private Spinner spinnerFrom, spinnerTo;
    private EditText etValue;
    private TextView tvCategoryHeading, tvResult, tvHelper;

    private String currentCategory = "Currency";

    private final String[] currencyUnits = {"USD", "AUD", "EUR", "JPY", "GBP"};
    private final String[] fuelUnits = {"mpg", "km/L", "Gallon", "Liter", "Nautical Mile", "Kilometer"};
    private final String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCurrency = findViewById(R.id.btnCurrency);
        btnFuel = findViewById(R.id.btnFuel);
        btnTemp = findViewById(R.id.btnTemp);
        Button btnConvert = findViewById(R.id.btnConvert);

        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        etValue = findViewById(R.id.etValue);

        tvCategoryHeading = findViewById(R.id.tvCategoryHeading);
        tvResult = findViewById(R.id.tvResult);
        tvHelper = findViewById(R.id.tvHelper);

        ViewCompat.setBackgroundTintList(btnConvert, null);
        ViewCompat.setBackgroundTintList(btnCurrency, null);
        ViewCompat.setBackgroundTintList(btnFuel, null);
        ViewCompat.setBackgroundTintList(btnTemp, null);

        setCategory("Currency");

        btnCurrency.setOnClickListener(v -> setCategory("Currency"));
        btnFuel.setOnClickListener(v -> setCategory("Fuel"));
        btnTemp.setOnClickListener(v -> setCategory("Temp"));

        btnConvert.setOnClickListener(v -> convertValue());
    }

    @SuppressLint("SetTextI18n")
    private void setCategory(String category) {
        currentCategory = category;
        tvCategoryHeading.setText(category);

        etValue.setText("");
        tvResult.setText("Result will appear here");

        if (category.equals("Currency")) {
            setSpinnerData(currencyUnits);
            tvHelper.setText("Exchange rates are approximate and for reference only");
        } else if (category.equals("Fuel")) {
            setSpinnerData(fuelUnits);
            tvHelper.setText("Use fuel, volume, or distance conversions for travel planning");
        } else {
            setSpinnerData(temperatureUnits);
            tvHelper.setText("Convert between Celsius, Fahrenheit, and Kelvin");
        }

        updateTabStyles();
    }

    private void setSpinnerData(String[] units) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
    }

    private void updateTabStyles() {
        btnCurrency.setBackgroundResource(android.R.color.transparent);
        btnFuel.setBackgroundResource(android.R.color.transparent);
        btnTemp.setBackgroundResource(android.R.color.transparent);

        if (currentCategory.equals("Currency")) {
            btnCurrency.setBackgroundResource(R.drawable.tab_selected);
        } else if (currentCategory.equals("Fuel")) {
            btnFuel.setBackgroundResource(R.drawable.tab_selected);
        } else {
            btnTemp.setBackgroundResource(R.drawable.tab_selected);
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void convertValue() {
        String input = etValue.getText().toString().trim();

        if (input.isEmpty()) {
            etValue.setError("Please enter a value");
            return;
        }

        double value;
        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            etValue.setError("Please enter a valid number");
            return;
        }

        String fromUnit = spinnerFrom.getSelectedItem().toString();
        String toUnit = spinnerTo.getSelectedItem().toString();

        if (currentCategory.equals("Fuel") && value < 0) {
            etValue.setError("Negative values are not allowed for fuel or distance conversions");
            tvResult.setText("Result will appear here");
            return;
        }

        if (currentCategory.equals("Temp") && fromUnit.equals("Kelvin") && value < 0) {
            etValue.setError("Kelvin cannot be negative");
            tvResult.setText("Result will appear here");
            return;
        }

        if (fromUnit.equals(toUnit)) {
            tvResult.setText(String.format("%.2f %s", value, toUnit));
            Toast.makeText(this, "Same unit selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Double result = null;

        switch (currentCategory) {
            case "Currency":
                result = convertCurrency(value, fromUnit, toUnit);
                break;
            case "Fuel":
                result = convertFuelDistance(value, fromUnit, toUnit);
                break;
            case "Temp":
                result = convertTemperature(value, fromUnit, toUnit);
                break;
        }

        if (result != null) {
            tvResult.setText(String.format("%.2f %s", result, toUnit));
        } else {
            tvResult.setText("Conversion not supported");
        }
    }

    private Double convertCurrency(double value, String from, String to) {
        double usdValue;

        switch (from) {
            case "USD":
                usdValue = value;
                break;
            case "AUD":
                usdValue = value / 1.55;
                break;
            case "EUR":
                usdValue = value / 0.92;
                break;
            case "JPY":
                usdValue = value / 148.50;
                break;
            case "GBP":
                usdValue = value / 0.78;
                break;
            default:
                return null;
        }

        switch (to) {
            case "USD":
                return usdValue;
            case "AUD":
                return usdValue * 1.55;
            case "EUR":
                return usdValue * 0.92;
            case "JPY":
                return usdValue * 148.50;
            case "GBP":
                return usdValue * 0.78;
            default:
                return null;
        }
    }

    private Double convertFuelDistance(double value, String from, String to) {
        if (from.equals("mpg") && to.equals("km/L")) return value * 0.425;
        if (from.equals("km/L") && to.equals("mpg")) return value / 0.425;
        if (from.equals("Gallon") && to.equals("Liter")) return value * 3.785;
        if (from.equals("Liter") && to.equals("Gallon")) return value / 3.785;
        if (from.equals("Nautical Mile") && to.equals("Kilometer")) return value * 1.852;
        if (from.equals("Kilometer") && to.equals("Nautical Mile")) return value / 1.852;

        return null;
    }

    private Double convertTemperature(double value, String from, String to) {
        if (from.equals("Celsius") && to.equals("Fahrenheit")) return (value * 1.8) + 32;
        if (from.equals("Fahrenheit") && to.equals("Celsius")) return (value - 32) / 1.8;
        if (from.equals("Celsius") && to.equals("Kelvin")) return value + 273.15;
        if (from.equals("Kelvin") && to.equals("Celsius")) return value - 273.15;
        if (from.equals("Fahrenheit") && to.equals("Kelvin")) return ((value - 32) / 1.8) + 273.15;
        if (from.equals("Kelvin") && to.equals("Fahrenheit")) return ((value - 273.15) * 1.8) + 32;

        return null;
    }
}