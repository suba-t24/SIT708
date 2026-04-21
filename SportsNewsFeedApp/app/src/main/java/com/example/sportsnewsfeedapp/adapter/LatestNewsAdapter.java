package com.example.sportsnewsfeedapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeedapp.databinding.ItemLatestNewsBinding;
import com.example.sportsnewsfeedapp.model.NewsItem;

import java.util.List;

public class LatestNewsAdapter extends RecyclerView.Adapter<LatestNewsAdapter.NewsViewHolder> {

    public interface OnNewsClickListener {
        void onClick(NewsItem item);
    }

    private List<NewsItem> items;
    private final OnNewsClickListener listener;

    public LatestNewsAdapter(List<NewsItem> items, OnNewsClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void updateData(List<NewsItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLatestNewsBinding binding = ItemLatestNewsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem item = items.get(position);
        holder.binding.ivThumb.setImageResource(item.getImageResId());
        holder.binding.tvTitle.setText(item.getTitle());
        holder.binding.tvCategory.setText(item.getSportCategory());
        holder.binding.tvShortDescription.setText(item.getDescription());
        holder.binding.getRoot().setOnClickListener(v -> listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ItemLatestNewsBinding binding;

        public NewsViewHolder(ItemLatestNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}