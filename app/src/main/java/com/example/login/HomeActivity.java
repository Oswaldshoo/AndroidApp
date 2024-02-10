package com.example.login;// HomeActivity.java
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

        // Retrieve logged-in user's username from intent
        String loggedInUsername = getIntent().getStringExtra("USERNAME");

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
            int firstNameIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_FIRST_NAME);
            int lastNameIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_LAST_NAME);
            int emailIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EMAIL);
            int phoneIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PHONE);

            // Check if column indexes are valid before retrieving data
            String firstName = (firstNameIndex != -1) ? cursor.getString(firstNameIndex) : "";
            String lastName = (lastNameIndex != -1) ? cursor.getString(lastNameIndex) : "";
            String email = (emailIndex != -1) ? cursor.getString(emailIndex) : "";
            String phone = (phoneIndex != -1) ? cursor.getString(phoneIndex) : "";

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
