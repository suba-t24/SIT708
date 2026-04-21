package com.example.sportsnewsfeedapp.model;

import java.io.Serializable;

public class NewsItem implements Serializable {
    private final int id;
    private final String title;
    private final String description;
    private final String sportCategory;
    private final int imageResId;
    private final boolean featured;

    public NewsItem(int id, String title, String description, String sportCategory, int imageResId, boolean featured) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.sportCategory = sportCategory;
        this.imageResId = imageResId;
        this.featured = featured;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSportCategory() {
        return sportCategory;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isFeatured() {
        return featured;
    }
}