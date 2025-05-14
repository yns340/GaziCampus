package com.example.myapplication3;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    Button createAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);


            EditText usernameEditText = findViewById(R.id.editTextNewUsername);
            EditText passwordEditText = findViewById(R.id.editTextNewPassword);
            Button createAccountButton = findViewById(R.id.createAccount);

            Database dbHelper = new Database(this);

            createAccountButton.setOnClickListener(view -> {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(Database.COLUMN_USERNAME, username);
                    values.put(Database.COLUMN_PASSWORD, password);

                    long result = db.insert(Database.TABLE_NAME, null, values);
                    if (result != -1) {
                        Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                        finish(); // Giriş ekranına dön
                    } else {
                        Toast.makeText(this, "Kayıt başarısız!", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
            });
            return insets;
        });
    }
}