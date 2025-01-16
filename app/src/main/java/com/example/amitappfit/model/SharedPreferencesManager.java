package com.example.amitappfit.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

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


    // מחיקת פריט
    public void deleteItem(String itemData) {
        List<String> items = getItems();
        if (items.remove(itemData)) {  // מנסה להסיר את הפריט
            saveItems(items);  // שומר את הרשימה המעודכנת
        }
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

    // שמירת רשימה של פריטים
    private void saveItems(List<String> items) {
        StringBuilder itemsBuilder = new StringBuilder();
        for (String item : items) {
            itemsBuilder.append(item).append(",");
        }
        sharedPreferences.edit().putString(KEY_ITEMS, itemsBuilder.toString()).apply();
    }

    // מחיקת כל הפריטים
    public void clearItems() {
        sharedPreferences.edit().remove(KEY_ITEMS).apply();
    }

    // עדכון פריט קיים
    public void updateItem(String oldItem, String newItem) {
        List<String> items = getItems();
        int index = items.indexOf(oldItem);
        if (index != -1) {
            items.set(index, newItem); // מחליף את הפריט הישן בחדש
        }
        saveItems(items); // שומר את הרשימה המעודכנת
    }

    // ----------------------
    // פונקציות ללוקים (Looks)
    // ----------------------

    // שמירת לוק חדש
    public void saveLook(String lookName, Look look) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // המרת אובייקט הלוק ל-JSON
        Gson gson = new Gson();
        String lookJson = gson.toJson(look);

        // שמירה תחת שם הלוק
        editor.putString(lookName, lookJson);
        editor.apply();
    }

    // שליפת כל הלוקים
    public List<Look> getSavedLooks() {
        List<Look> savedLooks = new ArrayList<>();
        Map<String, ?> allKeys = sharedPreferences.getAll();
        Gson gson = new Gson();

        // שליפת כל הלוקים
        for (String key : allKeys.keySet()) {
            if (key != null && !key.equals(KEY_ITEMS)) {
                String lookJson = sharedPreferences.getString(key, "");
                Look look = gson.fromJson(lookJson, Look.class);
                savedLooks.add(look);
            }
        }
        return savedLooks;
    }

    // מחיקת לוק לפי שם
    public void deleteLook(String lookName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(lookName);
        editor.apply();
    }
}
