package com.example.personaleventplannerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.personaleventplannerapp.databinding.FragmentEventListBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class EventListFragment extends Fragment {

    private FragmentEventListBinding binding;
    private EventDatabase database;
    private final List<Event> eventList = new ArrayList<>();
    private EventAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEventListBinding.inflate(inflater, container, false);
        database = EventDatabase.getInstance(requireContext());

        setupRecyclerView();
        loadEvents();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents();
    }

    private void setupRecyclerView() {
        adapter = new EventAdapter(eventList, new EventAdapter.OnEventActionListener() {
            @Override
            public void onEdit(Event event) {
                Bundle bundle = new Bundle();
                bundle.putInt("eventId", event.getId());
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.editEventFragment, bundle);
            }

            @Override
            public void onDelete(Event event) {
                deleteEvent(event);
            }
        });

        binding.recyclerViewEvents.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewEvents.setAdapter(adapter);
    }

    private void loadEvents() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Event> events = database.eventDao().getAllEvents();

            requireActivity().runOnUiThread(() -> {
                eventList.clear();
                eventList.addAll(events);
                adapter.notifyDataSetChanged();

                binding.tvEmpty.setVisibility(eventList.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }

    private void deleteEvent(Event event) {
        Executors.newSingleThreadExecutor().execute(() -> {
            database.eventDao().delete(event);

            requireActivity().runOnUiThread(() -> {
                Snackbar.make(binding.getRoot(), "Event deleted successfully", Snackbar.LENGTH_SHORT).show();
                loadEvents();
            });
        });
    }
}