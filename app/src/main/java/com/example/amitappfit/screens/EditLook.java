package com.example.amitappfit.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amitappfit.R;
import com.example.amitappfit.adapters.SpinnerItemAdapter;
import com.example.amitappfit.model.Item;
import com.example.amitappfit.model.Look;
import com.example.amitappfit.services.AuthenticationService;
import com.example.amitappfit.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EditLook extends AppCompatActivity {
    private EditText etLookName;
    private Spinner spinnerTops, spinnerBottoms, spinnerShoes;
    private Button btnSaveLook;
    private DatabaseService databaseService;
    private List<Item> allItems = new ArrayList<>();
    private Look currentLook;
    String lookId, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_look);

        databaseService = DatabaseService.getInstance();

        etLookName = findViewById(R.id.etLookName);
        spinnerTops = findViewById(R.id.spinnerTops);
        spinnerBottoms = findViewById(R.id.spinnerBottoms);
        spinnerShoes = findViewById(R.id.spinnerShoes);
        btnSaveLook = findViewById(R.id.btnSaveLook);

        // קבלת ה-ID של הלוק מתוך Intent
        Intent intent = getIntent();
        lookId = intent.getStringExtra("LOOK_ID");
        userId = intent.getStringExtra("LOOK_USER_ID");

        if (lookId == null || userId == null) {
            finish();
            return;
        }

        loadLookData();

        btnSaveLook.setOnClickListener(v -> saveLookChanges());

    }

    private void loadLookData() {
        databaseService.getLook(userId, lookId, new DatabaseService.DatabaseCallback<Look>() {
            @Override
            public void onCompleted(Look look) {
                currentLook = look;
                etLookName.setText(look.getName());

                databaseService.getItemList(look.getUserId(), new DatabaseService.DatabaseCallback<List<Item>>() {
                    @Override
                    public void onCompleted(List<Item> items) {
                        allItems = items;
                        setupSpinners();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Toast.makeText(EditLook.this, "Failed to load items", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(EditLook.this, "Failed to load look", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinners() {
        List<Item> tops = filterItemsByCategory("Tops");
        List<Item> bottoms = filterItemsByCategory("Bottoms");
        List<Item> shoes = filterItemsByCategory("Shoes");

        SpinnerItemAdapter topsAdapter = new SpinnerItemAdapter(this, tops);
        SpinnerItemAdapter bottomsAdapter = new SpinnerItemAdapter(this, bottoms);
        SpinnerItemAdapter shoesAdapter = new SpinnerItemAdapter(this,  shoes);

        spinnerTops.setAdapter(topsAdapter);
        spinnerBottoms.setAdapter(bottomsAdapter);
        spinnerShoes.setAdapter(shoesAdapter);

        setSpinnerSelection(spinnerTops, tops, currentLook.getTop());
        setSpinnerSelection(spinnerBottoms, bottoms, currentLook.getBottom());
        setSpinnerSelection(spinnerShoes, shoes, currentLook.getShoes());
    }

    private void setSpinnerSelection(Spinner spinner, List<Item> items, Item selectedItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(selectedItem.getId())) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private List<Item> filterItemsByCategory(String category) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : allItems) {
            if (item.getCategory().equals(category)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    private void saveLookChanges() {
        if (currentLook == null) return;

        String lookName = etLookName.getText().toString();
        if (lookName.isEmpty()) {
            Toast.makeText(this, "Please enter a name for your look", Toast.LENGTH_SHORT).show();
            return;
        }

        Item top = (Item) spinnerTops.getSelectedItem();
        Item bottom = (Item) spinnerBottoms.getSelectedItem();
        Item shoes = (Item) spinnerShoes.getSelectedItem();

        currentLook.setName(lookName);
        currentLook.setTop(top);
        currentLook.setBottom(bottom);
        currentLook.setShoes(shoes);

        databaseService.updateLook(currentLook, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(getApplicationContext(), "Look updated successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditLook.this, YourSavedLooks.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("USER_UID", userId);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(EditLook.this, "Failed to update look", Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    protected void onResume() {
        super.onResume();


    }
}
