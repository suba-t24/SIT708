package com.example.llmchatbotapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chat_history.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MESSAGES = "messages";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_MESSAGES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "message TEXT, " +
                "sender TEXT, " +
                "timestamp TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }

    public void insertMessage(String username, String message, String sender, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("message", message);
        values.put("sender", sender);
        values.put("timestamp", timestamp);

        db.insert(TABLE_MESSAGES, null, values);
        db.close();
    }

    public ArrayList<ChatMessage> getMessagesForUser(String username) {
        ArrayList<ChatMessage> messages = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_MESSAGES + " WHERE username=? ORDER BY id ASC",
                new String[]{username}
        );

        if (cursor.moveToFirst()) {
            do {
                messages.add(new ChatMessage(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return messages;
    }
}