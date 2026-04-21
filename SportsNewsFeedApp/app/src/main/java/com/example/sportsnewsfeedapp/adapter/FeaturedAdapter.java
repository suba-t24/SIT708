package com.example.sportsnewsfeedapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeedapp.databinding.ItemFeaturedMatchBinding;
import com.example.sportsnewsfeedapp.model.NewsItem;

import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    public interface OnFeaturedClickListener {
        void onClick(NewsItem item);
    }

    private List<NewsItem> items;
    private final OnFeaturedClickListener listener;

    public FeaturedAdapter(List<NewsItem> items, OnFeaturedClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void updateData(List<NewsItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeaturedMatchBinding binding = ItemFeaturedMatchBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FeaturedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {
        NewsItem item = items.get(position);
        holder.binding.ivFeatured.setImageResource(item.getImageResId());
        holder.binding.tvFeaturedTitle.setText(item.getTitle());
        holder.binding.tvFeaturedCategory.setText(item.getSportCategory());
        holder.binding.getRoot().setOnClickListener(v -> listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class FeaturedViewHolder extends RecyclerView.ViewHolder {
        ItemFeaturedMatchBinding binding;

        public FeaturedViewHolder(ItemFeaturedMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}