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
import androidx.navigation.Navigation;

import com.example.personaleventplannerapp.databinding.FragmentEditEventBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class EditEventFragment extends Fragment {

    private FragmentEditEventBinding binding;
    private EventDatabase database;
    private final Calendar selectedCalendar = Calendar.getInstance();
    private Event currentEvent;
    private int eventId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditEventBinding.inflate(inflater, container, false);
        database = EventDatabase.getInstance(requireContext());

        if (getArguments() != null) {
            eventId = getArguments().getInt("eventId", -1);
        }

        setupCategorySpinner();
        binding.etDateTimeEdit.setOnClickListener(v -> showDateTimePicker());
        binding.btnUpdateEvent.setOnClickListener(v -> updateEvent());

        ViewCompat.setBackgroundTintList(binding.btnUpdateEvent, null);

        loadEvent();

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
        binding.spinnerCategoryEdit.setAdapter(adapter);
    }

    private void loadEvent() {
        Executors.newSingleThreadExecutor().execute(() -> {
            currentEvent = database.eventDao().getEventById(eventId);

            requireActivity().runOnUiThread(() -> {
                if (currentEvent != null) {
                    binding.etTitleEdit.setText(currentEvent.getTitle());
                    binding.etLocationEdit.setText(currentEvent.getLocation());

                    String[] categories = {"Work", "Social", "Travel"};
                    for (int i = 0; i < categories.length; i++) {
                        if (categories[i].equals(currentEvent.getCategory())) {
                            binding.spinnerCategoryEdit.setSelection(i);
                            break;
                        }
                    }

                    selectedCalendar.setTimeInMillis(currentEvent.getDateTimeMillis());
                    SimpleDateFormat sdf =
                            new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
                    binding.etDateTimeEdit.setText(sdf.format(selectedCalendar.getTime()));
                }
            });
        });
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
                                binding.etDateTimeEdit.setText(sdf.format(selectedCalendar.getTime()));
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

    private void updateEvent() {
        String title = binding.etTitleEdit.getText().toString().trim();
        String category = binding.spinnerCategoryEdit.getSelectedItem().toString();
        String location = binding.etLocationEdit.getText().toString().trim();
        String dateText = binding.etDateTimeEdit.getText().toString().trim();

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

        if (currentEvent != null) {
            currentEvent.setTitle(title);
            currentEvent.setCategory(category);
            currentEvent.setLocation(location);
            currentEvent.setDateTimeMillis(selectedCalendar.getTimeInMillis());

            Executors.newSingleThreadExecutor().execute(() -> {
                database.eventDao().update(currentEvent);

                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(binding.getRoot(), "Event updated successfully", Snackbar.LENGTH_SHORT).show();
                    Navigation.findNavController(binding.getRoot()).navigateUp();
                });
            });
        }
    }
}