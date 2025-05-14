package com.example.myapplication3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.buttonRegister);

        Database dbHelper = new Database(this);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (dbHelper.checkUser(username, password)) {
                // user_id’yi al
                int userId = dbHelper.getUserId(username, password);

                // SharedPreferences’e kaydet
                SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("user_id", userId);
                editor.apply();

                Toast.makeText(this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();

                // Ana sayfaya geçiş
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Kullanıcı adı veya şifre hatalı!", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, Register.class);
            startActivity(intent);
        });
    }
}