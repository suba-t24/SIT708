package com.example.istream.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.istream.R;
import com.example.istream.database.AppDatabase;
import com.example.istream.database.VideoItem;
import com.example.istream.ui.auth.LoginFragment;
import com.example.istream.ui.playlist.PlaylistFragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment {

    private WebView webViewYoutube;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etUrl = view.findViewById(R.id.et_youtube_url);
        Button btnPlay = view.findViewById(R.id.btn_play);
        Button btnAdd = view.findViewById(R.id.btn_add_playlist);
        Button btnPlaylist = view.findViewById(R.id.btn_my_playlist);
        Button btnLogout = view.findViewById(R.id.btn_logout);
        webViewYoutube = view.findViewById(R.id.youtube_player_view);

        setupWebView();

        // Load video if passed back from the Playlist fragment
        if (getArguments() != null && getArguments().containsKey("VIDEO_URL")) {
            String url = getArguments().getString("VIDEO_URL");
            String id = extractYTId(url);
            if (id != null) {
                loadYoutubeVideo(id);
            }
        }
//        else {
//            // Cue a default Google test video so the player isn't empty on launch
//            loadYoutubeVideo("M7lc1UVf-VE");
//        }

        btnPlay.setOnClickListener(v -> {
            String id = extractYTId(etUrl.getText().toString());
            if (id != null) {
                loadYoutubeVideo(id);
            } else {
                Toast.makeText(getContext(), "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            }
        });

        btnAdd.setOnClickListener(v -> {
            String url = etUrl.getText().toString();
            if (extractYTId(url) == null) {
                Toast.makeText(getContext(), "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
                return;
            }

            // Retrieve the secure session ID
            SharedPreferences prefs = requireActivity().getSharedPreferences("iStreamPrefs", Context.MODE_PRIVATE);
            int userId = prefs.getInt("CURRENT_USER_ID", -1);

            if (userId == -1) {
                Toast.makeText(getContext(), "Session error. Please log in again.", Toast.LENGTH_SHORT).show();
                return;
            }

            VideoItem item = new VideoItem();
            item.userId = userId;
            item.youtubeUrl = url;

            // Execute the Room database write strictly on a background thread
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                AppDatabase.getInstance(requireContext()).videoItemDao().insertVideo(item);

                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Added to Playlist", Toast.LENGTH_SHORT).show()
                );
            });
        });

        btnPlaylist.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PlaylistFragment())
                .addToBackStack(null).commit());

        btnLogout.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit());
    }

    private void setupWebView() {
        WebSettings webSettings = webViewYoutube.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        // 1. THE DESKTOP SPOOF: Trick YouTube into thinking this is a Windows PC, not an Android App.
        // This is the exact trick used by the Flutter packages to bypass Error 152.
        webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");

        webViewYoutube.setWebViewClient(new WebViewClient());
        webViewYoutube.setWebChromeClient(new WebChromeClient());
    }

    private String extractYTId(String ytUrl) {
        String vId = null;
        // Bulletproof regex to extract just the 11-character video ID
        Pattern pattern = Pattern.compile(".*(?:youtu\\.be/|v/|u/\\w/|embed/|watch\\?v=)([^#&?]{11}).*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }

    private void loadYoutubeVideo(String videoId) {
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<style>" +
                "html, body { margin:0; padding:0; width:100%; height:100%; background-color: #000; }" +
                "iframe { width: 100%; height: 100%; border: none; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "" +
                "<iframe " +
                "src=\"https://www.youtube-nocookie.com/embed/" + videoId + "?autoplay=1&playsinline=1\" " +
                "allow=\"autoplay; fullscreen\">" +
                "</iframe>" +
                "</body>" +
                "</html>";

        // Inject using the matching nocookie base URL
        webViewYoutube.loadDataWithBaseURL(
                "https://www.youtube-nocookie.com",
                html,
                "text/html",
                "UTF-8",
                null
        );
    }
}