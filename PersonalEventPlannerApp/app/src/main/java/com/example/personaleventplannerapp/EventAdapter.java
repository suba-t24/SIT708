package com.example.personaleventplannerapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personaleventplannerapp.databinding.ItemEventBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public interface OnEventActionListener {
        void onEdit(Event event);
        void onDelete(Event event);
    }

    private final List<Event> eventList;
    private final OnEventActionListener listener;
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());

    public EventAdapter(List<Event> eventList, OnEventActionListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEventBinding binding = ItemEventBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new EventViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.binding.tvTitle.setText(event.getTitle());
        holder.binding.tvCategory.setText("Category: " + event.getCategory());
        holder.binding.tvLocation.setText("Location: " + event.getLocation());
        holder.binding.tvDateTime.setText("Date: " + dateFormat.format(new Date(event.getDateTimeMillis())));

        ViewCompat.setBackgroundTintList(holder.binding.btnEdit, null);
        ViewCompat.setBackgroundTintList(holder.binding.btnDelete, null);

        holder.binding.btnEdit.setOnClickListener(v -> listener.onEdit(event));
        holder.binding.btnDelete.setOnClickListener(v -> listener.onDelete(event));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        ItemEventBinding binding;

        public EventViewHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}