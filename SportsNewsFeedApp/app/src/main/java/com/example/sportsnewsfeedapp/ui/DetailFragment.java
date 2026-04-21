package com.example.sportsnewsfeedapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sportsnewsfeedapp.adapter.RelatedStoriesAdapter;
import com.example.sportsnewsfeedapp.data.DummyData;
import com.example.sportsnewsfeedapp.databinding.FragmentDetailBinding;
import com.example.sportsnewsfeedapp.model.NewsItem;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;
    private NewsItem newsItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            newsItem = (NewsItem) getArguments().getSerializable("news");
        }

        if (newsItem != null) {
            binding.ivDetail.setImageResource(newsItem.getImageResId());
            binding.tvTitle.setText(newsItem.getTitle());
            binding.tvDescription.setText(newsItem.getDescription());
            setupRelatedStories();
        }

        binding.btnBookmark.setOnClickListener(v -> saveBookmark());

        return binding.getRoot();
    }

    private void setupRelatedStories() {
        List<NewsItem> related = new ArrayList<>();
        for (NewsItem item : DummyData.getAllNews()) {
            if (item.getId() != newsItem.getId()
                    && item.getSportCategory().equals(newsItem.getSportCategory())) {
                related.add(item);
            }
        }

        RelatedStoriesAdapter adapter = new RelatedStoriesAdapter(related);
        binding.rvRelated.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRelated.setAdapter(adapter);
    }

    private void saveBookmark() {
        SharedPreferences prefs = requireContext().getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        prefs.edit()
                .putInt("image_" + newsItem.getId(), newsItem.getImageResId())
                .putString("title_" + newsItem.getId(), newsItem.getTitle())
                .putString("desc_" + newsItem.getId(), newsItem.getDescription())
                .putString("cat_" + newsItem.getId(), newsItem.getSportCategory())
                .apply();

        Snackbar.make(binding.getRoot(), "Story bookmarked", Snackbar.LENGTH_SHORT).show();
    }
}