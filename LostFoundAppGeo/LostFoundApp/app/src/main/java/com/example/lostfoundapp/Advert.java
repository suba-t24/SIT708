package com.example.lostfoundapp;

public class Advert {
    int id;
    String type, name, phone, description, date, location, category, imageUri, timestamp;
    double latitude, longitude;

    public Advert(int id, String type, String name, String phone, String description,
                  String date, String location, String category, String imageUri,
                  String timestamp, double latitude, double longitude) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.category = category;
        this.imageUri = imageUri;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}