package com.example.cn_apptracnghiem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScoreActivity extends AppCompatActivity {
    private TextView tvFinalScore;
    private Button btnPlayAgain, btnBackMainMenu;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Khởi tạo Database
        db = new Database(this);

        // Ánh xạ các thành phần giao diện
        tvFinalScore = findViewById(R.id.tv_final_score);
        btnPlayAgain = findViewById(R.id.btn_play_again);
        btnBackMainMenu = findViewById(R.id.btn_back_main_menu);

        // Nhận điểm số và user_id từ Intent
        Intent intent = getIntent();
        float score = intent.getFloatExtra("SCORE", 0.0f); // Nhận score là float
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1); // Nhận user_id từ Intent

        tvFinalScore.setText(String.format(Locale.getDefault(), "Điểm: %.2f", score)); // Định dạng điểm

        // Lưu điểm vào bảng lịch sử
        if (userId != -1) {  // Kiểm tra nếu userId hợp lệ
            saveScoreToHistory(userId, score);
        }

        // Sự kiện nút "Chơi lại"
        btnPlayAgain.setOnClickListener(v -> {
            Intent replayIntent = new Intent(ScoreActivity.this, loadQues.class);
            replayIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(replayIntent);
            finish();
        });

        // Sự kiện nút "Trở về màn hình chính"
        btnBackMainMenu.setOnClickListener(v -> {
            Intent mainMenuIntent = new Intent(ScoreActivity.this, MenuActivity.class);
            mainMenuIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainMenuIntent);
            finish();
        });
    }

    // Hàm lưu điểm vào bảng lịch sử
    private void saveScoreToHistory(int userId, float score) {
        String date = getCurrentDate(); // Hàm lấy ngày hiện tại

        // Lưu điểm vào bảng lịch sử
        db.insertHistory(userId, score, date);
    }

    // Hàm lấy ngày hiện tại
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
}
