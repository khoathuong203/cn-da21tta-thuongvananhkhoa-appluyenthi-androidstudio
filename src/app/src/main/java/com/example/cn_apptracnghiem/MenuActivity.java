package com.example.cn_apptracnghiem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btnOntap, btnXemlichsu, btnDangxuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Ánh xạ các thành phần
        btnOntap = findViewById(R.id.btn_ontap);
        btnXemlichsu = findViewById(R.id.btn_xemlichsu);
        btnDangxuat = findViewById(R.id.btn_dangxuat);

        // Xử lý sự kiện nút Ôn tập
        btnOntap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class); // Mở Activity loadQues
                startActivity(intent);
            }
        });
/*
        // Xử lý sự kiện nút Xem lịch sử
        btnXemlichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, HistoryActivity.class); // Mở Activity lịch sử
                startActivity(intent);
            }
        });

        // Xử lý sự kiện nút Đăng xuất
        btnDangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class); // Mở màn hình đăng nhập
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Kết thúc Activity hiện tại
            }
        });
*/

    }
}
