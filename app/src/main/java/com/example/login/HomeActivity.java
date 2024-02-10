package com.example.login;
// HomeActivity.java
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private TextView usernameTextView, emailTextView, phoneTextView, firstNameTextView, lastNameTextView;
    private UserDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new UserDbHelper(this);

        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameTextView);

        // Retrieve logged-in user's username from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String loggedInUsername = preferences.getString("username", "");

        // Retrieve user data from the database based on the logged-in username
        displayUserData(loggedInUsername);
    }

    private void displayUserData(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                UserContract.UserEntry.COLUMN_FIRST_NAME,
                UserContract.UserEntry.COLUMN_LAST_NAME,
                UserContract.UserEntry.COLUMN_EMAIL,
                UserContract.UserEntry.COLUMN_PHONE
        };

        String selection = UserContract.UserEntry.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_FIRST_NAME));
            @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_LAST_NAME));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EMAIL));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PHONE));

            // Display user information in TextViews
            usernameTextView.setText("Username: " + username);
            firstNameTextView.setText("First Name: " + firstName);
            lastNameTextView.setText("Last Name: " + lastName);
            emailTextView.setText("Email: " + email);
            phoneTextView.setText("Phone: " + phone);
        }

        cursor.close();
        db.close();
    }
}
