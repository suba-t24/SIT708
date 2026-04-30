package com.example.lostfoundapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "lost_found.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "adverts";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT, " +
                "name TEXT, " +
                "phone TEXT, " +
                "description TEXT, " +
                "date TEXT, " +
                "location TEXT, " +
                "category TEXT, " +
                "imageUri TEXT, " +
                "timestamp TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertAdvert(String type, String name, String phone, String description,
                                String date, String location, String category,
                                String imageUri, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("type", type);
        values.put("name", name);
        values.put("phone", phone);
        values.put("description", description);
        values.put("date", date);
        values.put("location", location);
        values.put("category", category);
        values.put("imageUri", imageUri);
        values.put("timestamp", timestamp);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public ArrayList<Advert> getAllAdverts() {
        ArrayList<Advert> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new Advert(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public ArrayList<Advert> getAdvertsByCategory(String category) {
        ArrayList<Advert> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE category=? ORDER BY id DESC",
                new String[]{category}
        );

        if (cursor.moveToFirst()) {
            do {
                list.add(new Advert(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public Advert getAdvertById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE id=?",
                new String[]{String.valueOf(id)}
        );

        Advert advert = null;

        if (cursor.moveToFirst()) {
            advert = new Advert(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9)
            );
        }

        cursor.close();
        return advert;
    }

    public boolean deleteAdvert(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}