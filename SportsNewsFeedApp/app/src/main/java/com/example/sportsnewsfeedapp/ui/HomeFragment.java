package com.example.sportsnewsfeedapp.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sportsnewsfeedapp.R;
import com.example.sportsnewsfeedapp.adapter.FeaturedAdapter;
import com.example.sportsnewsfeedapp.adapter.LatestNewsAdapter;
import com.example.sportsnewsfeedapp.data.DummyData;
import com.example.sportsnewsfeedapp.databinding.FragmentHomeBinding;
import com.example.sportsnewsfeedapp.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final List<NewsItem> allNews = DummyData.getAllNews();

    private LatestNewsAdapter latestNewsAdapter;
    private FeaturedAdapter featuredAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setupFeaturedRecycler();
        setupLatestRecycler();
        setupSearch();

        return binding.getRoot();
    }

    private void setupFeaturedRecycler() {
        List<NewsItem> featured = new ArrayList<>();
        for (NewsItem item : allNews) {
            if (item.isFeatured()) {
                featured.add(item);
            }
        }

        featuredAdapter = new FeaturedAdapter(featured, this::openDetail);
        binding.rvFeatured.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvFeatured.setAdapter(featuredAdapter);
    }

    private void setupLatestRecycler() {
        latestNewsAdapter = new LatestNewsAdapter(new ArrayList<>(allNews), this::openDetail);
        binding.rvLatest.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvLatest.setAdapter(latestNewsAdapter);
    }

    private void setupSearch() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim().toLowerCase();

                List<NewsItem> filteredLatest = new ArrayList<>();
                List<NewsItem> filteredFeatured = new ArrayList<>();

                if (query.isEmpty()) {
                    for (NewsItem item : allNews) {
                        filteredLatest.add(item);
                        if (item.isFeatured()) {
                            filteredFeatured.add(item);
                        }
                    }
                } else {
                    for (NewsItem item : allNews) {
                        boolean matches = item.getSportCategory().toLowerCase().contains(query)
                                || item.getTitle().toLowerCase().contains(query);

                        if (matches) {
                            filteredLatest.add(item);
                            if (item.isFeatured()) {
                                filteredFeatured.add(item);
                            }
                        }
                    }
                }

                latestNewsAdapter.updateData(filteredLatest);

                featuredAdapter.updateData(filteredFeatured);

                binding.tvFeaturedSection.setVisibility(filteredFeatured.isEmpty() ? View.GONE : View.VISIBLE);
                binding.rvFeatured.setVisibility(filteredFeatured.isEmpty() ? View.GONE : View.VISIBLE);

                binding.tvLatestSection.setVisibility(filteredLatest.isEmpty() ? View.GONE : View.VISIBLE);
                binding.rvLatest.setVisibility(filteredLatest.isEmpty() ? View.GONE : View.VISIBLE);

                binding.tvEmptySearch.setVisibility(
                        filteredLatest.isEmpty() && filteredFeatured.isEmpty() ? View.VISIBLE : View.GONE
                );
            }
        });
    }

    private void openDetail(NewsItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("news", item);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.detailFragment, bundle);
    }
}