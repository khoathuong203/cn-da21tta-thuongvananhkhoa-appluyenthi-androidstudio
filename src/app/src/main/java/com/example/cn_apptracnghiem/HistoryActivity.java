package com.example.cn_apptracnghiem;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cn_apptracnghiem.Database;
import com.example.cn_apptracnghiem.model.LichSu;
import java.util.ArrayList;
public class HistoryActivity extends AppCompatActivity {

    private LinearLayout historyLayout;
    private Database database;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyLayout = findViewById(R.id.historyLayout);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        int userId = sharedPreferences.getInt("USER_ID", -1);
        database = new Database(this);

        // Lấy danh sách lịch sử kiểm tra từ cơ sở dữ liệu
        ArrayList<LichSu> historyList = database.getUserHistory(userId);

        // Duyệt qua danh sách và tạo giao diện động
        for (LichSu lichSu : historyList) {
            // Tạo một CardView mới cho mỗi mục lịch sử
            CardView cardView = new CardView(this);
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setPadding(16, 16, 16, 16);

            // Thiết lập thông tin lịch sử vào TextView
            TextView historyDate = new TextView(this);
            historyDate.setText("Ngày thực hiện: " + lichSu.getNgayThucHien());
            historyDate.setTextSize(16);
            historyDate.setTextColor(Color.BLACK);
            cardLayout.addView(historyDate);

            TextView historyScore = new TextView(this);
            historyScore.setText("Điểm: " + lichSu.getDiem());
            historyScore.setTextSize(16);
            historyScore.setTextColor(Color.BLACK);
            cardLayout.addView(historyScore);

            // Thêm layout vào CardView
            cardView.addView(cardLayout);
            historyLayout.addView(cardView);
        }
    }
}

