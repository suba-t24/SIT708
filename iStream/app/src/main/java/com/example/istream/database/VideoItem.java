package com.example.istream.database;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist",
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = ForeignKey.CASCADE))
public class VideoItem {
    @PrimaryKey(autoGenerate = true)
    public int videoId;
    public int userId;
    public String youtubeUrl;
}