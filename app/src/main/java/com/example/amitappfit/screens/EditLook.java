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
import com.example.amitappfit.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class EditLook extends AppCompatActivity {
    private EditText etLookName;
    private Spinner spinnerTops, spinnerBottoms, spinnerShoes;
    private Button btnSaveLook, btnDeleteLook;
    private DatabaseService databaseService;
    private List<Item> allItems = new ArrayList<>();
    private Look currentLook;

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
        btnDeleteLook = findViewById(R.id.btnDeleteLook);

        // קבלת ה-ID של הלוק מתוך Intent
        Intent intent = getIntent();
        String lookId = intent.getStringExtra("LOOK_ID");

        if (lookId != null) {
            loadLookData(lookId);
        }

        btnSaveLook.setOnClickListener(v -> saveLookChanges());
        btnDeleteLook.setOnClickListener(v -> deleteLook());
    }

    private void loadLookData(String lookId) {
        databaseService.getLook(lookId, new DatabaseService.DatabaseCallback<Look>() {
            @Override
            public void onCompleted(Look look) {
                currentLook = look;
                etLookName.setText(look.getName());

                databaseService.getItemList(new DatabaseService.DatabaseCallback<List<Item>>() {
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

        SpinnerItemAdapter topsAdapter = new SpinnerItemAdapter(this, android.R.layout.simple_spinner_item, tops);
        SpinnerItemAdapter bottomsAdapter = new SpinnerItemAdapter(this, android.R.layout.simple_spinner_item, bottoms);
        SpinnerItemAdapter shoesAdapter = new SpinnerItemAdapter(this, android.R.layout.simple_spinner_item, shoes);

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
                startActivity(intent);


                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(EditLook.this, "Failed to update look", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteLook() {
        databaseService.deleteLook(currentLook.getId(), new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(getApplicationContext(), "Look deleted successfully!", Toast.LENGTH_SHORT).show();

                // מעבר לעמוד YourSavedLooks
                Intent intent = new Intent(EditLook.this, YourSavedLooks.class);
                startActivity(intent);

                // סיום המסך הנוכחי כדי שלא יישאר בערימה
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(EditLook.this, "Failed to delete look", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
