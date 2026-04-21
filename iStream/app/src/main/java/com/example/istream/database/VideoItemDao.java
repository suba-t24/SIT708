package com.example.istream.database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface VideoItemDao {
    @Insert
    void insertVideo(VideoItem video);

    @Query("SELECT * FROM playlist WHERE userId = :userId")
    List<VideoItem> getPlaylistForUser(int userId);
}