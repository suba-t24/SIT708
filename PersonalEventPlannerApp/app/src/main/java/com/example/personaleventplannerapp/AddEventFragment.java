package com.example.personaleventplannerapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.example.personaleventplannerapp.databinding.FragmentAddEventBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddEventFragment extends Fragment {

    private FragmentAddEventBinding binding;
    private EventDatabase database;
    private final Calendar selectedCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container, false);
        database = EventDatabase.getInstance(requireContext());

        setupCategorySpinner();
        binding.etDateTime.setOnClickListener(v -> showDateTimePicker());
        binding.btnSaveEvent.setOnClickListener(v -> saveEvent());

        ViewCompat.setBackgroundTintList(binding.btnSaveEvent, null);

        return binding.getRoot();
    }

    private void setupCategorySpinner() {
        String[] categories = {"Work", "Social", "Travel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapter);
    }

    private void showDateTimePicker() {
        Calendar now = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, month);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            requireContext(),
                            (timeView, hourOfDay, minute) -> {
                                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedCalendar.set(Calendar.MINUTE, minute);
                                selectedCalendar.set(Calendar.SECOND, 0);

                                SimpleDateFormat sdf =
                                        new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
                                binding.etDateTime.setText(sdf.format(selectedCalendar.getTime()));
                            },
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false
                    );

                    timePickerDialog.show();
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void saveEvent() {
        String title = binding.etTitle.getText().toString().trim();
        String category = binding.spinnerCategory.getSelectedItem().toString();
        String location = binding.etLocation.getText().toString().trim();
        String dateText = binding.etDateTime.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Snackbar.make(binding.getRoot(), "Title cannot be empty", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(dateText)) {
            Snackbar.make(binding.getRoot(), "Date/Time cannot be empty", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (selectedCalendar.getTimeInMillis() < System.currentTimeMillis()) {
            Snackbar.make(binding.getRoot(), "Please select a future date/time", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event(title, category, location, selectedCalendar.getTimeInMillis());

        Executors.newSingleThreadExecutor().execute(() -> {
            database.eventDao().insert(event);

            requireActivity().runOnUiThread(() -> {
                Snackbar.make(binding.getRoot(), "Event added successfully", Snackbar.LENGTH_SHORT).show();
                clearFields();
            });
        });
    }

    private void clearFields() {
        binding.etTitle.setText("");
        binding.etLocation.setText("");
        binding.etDateTime.setText("");
        binding.spinnerCategory.setSelection(0);
    }
}