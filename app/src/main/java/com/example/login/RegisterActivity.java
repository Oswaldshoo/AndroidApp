package com.example.login;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, usernameEditText, passwordEditText, emailEditText, phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserDbHelper dbHelper = new UserDbHelper(this);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            saveUserData(dbHelper);
            startLoginActivity();
        });
    }

    private void saveUserData(UserDbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_FIRST_NAME, firstNameEditText.getText().toString());
        values.put(UserContract.UserEntry.COLUMN_LAST_NAME, lastNameEditText.getText().toString());
        values.put(UserContract.UserEntry.COLUMN_USERNAME, usernameEditText.getText().toString());
        values.put(UserContract.UserEntry.COLUMN_PASSWORD, passwordEditText.getText().toString());
        values.put(UserContract.UserEntry.COLUMN_EMAIL, emailEditText.getText().toString());
        values.put(UserContract.UserEntry.COLUMN_PHONE, phoneEditText.getText().toString());

        long newRowId = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);

        // Optionally, you can check if the data was successfully inserted
        if (newRowId != -1) {
            // Data inserted successfully
            Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Error inserting data
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

