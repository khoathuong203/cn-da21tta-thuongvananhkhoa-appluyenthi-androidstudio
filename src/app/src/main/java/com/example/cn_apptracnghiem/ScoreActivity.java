package com.example.cn_apptracnghiem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private TextView tvFinalScore;
    private Button btnPlayAgain, btnBackMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Ánh xạ các thành phần giao diện
        tvFinalScore = findViewById(R.id.tv_final_score);
        btnPlayAgain = findViewById(R.id.btn_play_again);
        btnBackMainMenu = findViewById(R.id.btn_back_main_menu);

        // Nhận điểm số từ Intent
        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);
        tvFinalScore.setText("Điểm: " + score);

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
}
