package com.example.amitappfit.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "ClosetApp";
    private static final String KEY_ITEMS = "items";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ----------------------
    // פונקציות לפריטים (Items)
    // ----------------------

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

    // ----------------------
    // פונקציות ללוקים (Looks)
    // ----------------------

    // שמירת לוק חדש
    public void saveLook(String lookName, String top, String bottom, String shoes) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(lookName + "_top", top);
        editor.putString(lookName + "_bottom", bottom);
        editor.putString(lookName + "_shoes", shoes);
        editor.apply();
    }

    // שליפת כל הלוקים
    public List<Look> getSavedLooks() {
        List<Look> savedLooks = new ArrayList<>();
        Map<String, ?> allKeys = sharedPreferences.getAll();
        for (String key : allKeys.keySet()) {
            if (key.contains("_top")) {
                String lookName = key.split("_")[0];
                String top = sharedPreferences.getString(lookName + "_top", "");
                String bottom = sharedPreferences.getString(lookName + "_bottom", "");
                String shoes = sharedPreferences.getString(lookName + "_shoes", "");
                savedLooks.add(new Look(lookName, top, bottom, shoes));
            }
        }
        return savedLooks;
    }

    // מחיקת לוק לפי שם
    public void deleteLook(String lookName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(lookName + "_top");
        editor.remove(lookName + "_bottom");
        editor.remove(lookName + "_shoes");
        editor.apply();
    }
}
