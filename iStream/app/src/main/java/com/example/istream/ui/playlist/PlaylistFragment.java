package com.example.istream.ui.playlist;
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
import androidx.recyclerview.widget.RecyclerView;
import com.example.istream.R;
import com.example.istream.database.AppDatabase;
import com.example.istream.database.VideoItem;
import com.example.istream.ui.home.HomeFragment;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaylistFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences prefs = requireActivity().getSharedPreferences("iStreamPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("CURRENT_USER_ID", -1);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<VideoItem> playlist = AppDatabase.getInstance(requireContext()).videoItemDao().getPlaylistForUser(userId); // [cite: 58, 61]
            requireActivity().runOnUiThread(() -> {
                VideoAdapter adapter = new VideoAdapter(playlist, url -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("VIDEO_URL", url);
                    HomeFragment homeFrag = new HomeFragment();
                    homeFrag.setArguments(bundle);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, homeFrag)
                            .commit(); // [cite: 59]
                });
                recyclerView.setAdapter(adapter);
            });
        });

        return view;
    }
}