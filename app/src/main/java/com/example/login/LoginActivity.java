package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private UserDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new UserDbHelper(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Retrieve user data from the database
        if (authenticateUser(username, password)) {
            // Authentication successful
            startHomeActivity(username);
        } else {
            // Authentication failed
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                UserContract.UserEntry.COLUMN_USERNAME,
                UserContract.UserEntry.COLUMN_PASSWORD
        };

        String selection = UserContract.UserEntry.COLUMN_USERNAME + " = ? AND " +
                UserContract.UserEntry.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isAuthenticated = cursor.moveToFirst();

        cursor.close();
        db.close();

        return isAuthenticated;
    }

    private void startHomeActivity(String username) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        finish();
    }
}
