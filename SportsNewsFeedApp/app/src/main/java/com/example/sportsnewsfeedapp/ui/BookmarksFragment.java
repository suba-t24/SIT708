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

import com.example.sportsnewsfeedapp.adapter.LatestNewsAdapter;
import com.example.sportsnewsfeedapp.data.DummyData;
import com.example.sportsnewsfeedapp.databinding.FragmentBookmarksBinding;
import com.example.sportsnewsfeedapp.model.NewsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookmarksFragment extends Fragment {

    private FragmentBookmarksBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBookmarksBinding.inflate(inflater, container, false);

        SharedPreferences prefs = requireContext().getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();

        List<NewsItem> bookmarked = new ArrayList<>();
        for (NewsItem item : DummyData.getAllNews()) {
            if (allEntries.containsKey("title_" + item.getId())) {
                bookmarked.add(item);
            }
        }

        LatestNewsAdapter adapter = new LatestNewsAdapter(bookmarked, newsItem -> {});
        binding.rvBookmarks.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvBookmarks.setAdapter(adapter);

        binding.tvEmpty.setVisibility(bookmarked.isEmpty() ? View.VISIBLE : View.GONE);

        return binding.getRoot();
    }
}