package com.example.sportsnewsfeedapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsfeedapp.databinding.ItemRelatedStoryBinding;
import com.example.sportsnewsfeedapp.model.NewsItem;

import java.util.List;

public class RelatedStoriesAdapter extends RecyclerView.Adapter<RelatedStoriesAdapter.RelatedViewHolder> {

    private final List<NewsItem> items;

    public RelatedStoriesAdapter(List<NewsItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RelatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRelatedStoryBinding binding = ItemRelatedStoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RelatedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedViewHolder holder, int position) {
        NewsItem item = items.get(position);
        holder.binding.tvRelatedTitle.setText(item.getTitle());
        holder.binding.tvRelatedCategory.setText(item.getSportCategory());
        holder.binding.ivRelated.setImageResource(item.getImageResId());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RelatedViewHolder extends RecyclerView.ViewHolder {
        ItemRelatedStoryBinding binding;

        public RelatedViewHolder(ItemRelatedStoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}