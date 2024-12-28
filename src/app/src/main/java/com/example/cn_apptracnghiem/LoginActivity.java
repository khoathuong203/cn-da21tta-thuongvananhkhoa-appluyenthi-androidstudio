package com.example.cn_apptracnghiem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        etEmail.setFilters(new InputFilter[]{new InputFilterSpecial()});
        etPassword.setFilters(new InputFilter[]{new InputFilterSpecial()});
        // Khởi tạo Database
        db = new Database(this);

        // Xử lý sự kiện Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Hãy điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Lấy user_id từ phương thức loginUser
                    int userId = db.loginUser(username, password);

                    if (userId != -1) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("USER_ID", userId); // Lưu user_id
                        editor.apply();
                        // Lưu user_id vào SharedPreferences hoặc Intent để sử dụng ở Activity sau
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);// Chuyển user_id sang MenuActivity
                        startActivity(intent);
                        finish(); // Kết thúc LoginActivity
                    } else {
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Xử lý sự kiện Sign Up
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
