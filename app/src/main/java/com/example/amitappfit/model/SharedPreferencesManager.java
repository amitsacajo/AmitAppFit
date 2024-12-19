package com.example.amitappfit.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "ClosetApp";
    private static final String KEY_ITEMS = "items";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // שמירת פריט חדש
    public void saveItem(String itemName) {
        String existingItems = sharedPreferences.getString(KEY_ITEMS, "");
        String updatedItems = existingItems + itemName + ",";
        sharedPreferences.edit().putString(KEY_ITEMS, updatedItems).apply();
    }

    // שליפת כל הפריטים
    public List<String> getItems() {
        String savedItems = sharedPreferences.getString(KEY_ITEMS, "");
        if (!savedItems.isEmpty()) {
            return new ArrayList<>(Arrays.asList(savedItems.split(",")));
        }
        return new ArrayList<>();
    }

    // מחיקת כל הפריטים
    public void clearItems() {
        sharedPreferences.edit().remove(KEY_ITEMS).apply();
    }
}
