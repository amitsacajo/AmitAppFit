package com.example.amitappfit.screens;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amitappfit.R;
import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amitappfit.adapters.UserAdapter;
import com.example.amitappfit.model.User;
import com.example.amitappfit.services.DatabaseService;

import java.util.List;

public class UsersList extends AppCompatActivity {

    private static final String TAG = "UsersListActivity";
    private RecyclerView usersList;
    private UserAdapter userAdapter;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_users_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();

        usersList = findViewById(R.id.rv_users_list);
        usersList.setLayoutManager(new LinearLayoutManager(this));
        UserAdapter.OnUserClickListener onUserClickListener = user -> {
            // Handle user click
            Log.d(TAG, "User clicked: " + user);
            Intent intent = new Intent(this, AdminEditUser.class);
            intent.putExtra("USER_UID", user.getId());
            startActivity(intent);

        };
        UserAdapter.OnUserClickListener onLongUserClickListener = user -> {
            // Handle long user click
            Log.d(TAG, "User long clicked: " + user);
            // show popup to delete user
//            showDeleteUserDialog(user);

        };
        userAdapter = new UserAdapter(onUserClickListener, onLongUserClickListener);
        usersList.setAdapter(userAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        databaseService.getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                userAdapter.setUserList(users);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Failed to get users list", e);
            }
        });
    }

}