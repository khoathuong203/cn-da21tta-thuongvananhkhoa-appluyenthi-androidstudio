package com.example.cn_apptracnghiem;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cn_apptracnghiem.model.cauhoi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class loadQues extends AppCompatActivity {

    private TextView tvTimer, tvQuestionNumber, tvQuestionContent;
    private CardView cardAnswer1, cardAnswer2, cardAnswer3, cardAnswer4;
    private Button btnShowQuestionList;

    private ArrayList<cauhoi> listCauhoi;
    private int currentQuestionIndex = 0;
    private float score = 0;
    private int[] userAnswers = new int[40]; // Lưu trữ câu trả lời của người dùng
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_ques);

        // Khởi tạo các thành phần giao diện
        initViews();
        Arrays.fill(userAnswers, -1);
        Button btnSubmitExam = findViewById(R.id.btn_submit_exam);
        Button btnPreviousQuestion = findViewById(R.id.btn_previous_question);
        btnPreviousQuestion.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--; // Giảm chỉ số câu hỏi
                showQuestion(); // Hiển thị câu hỏi
            } else {
                Toast.makeText(this, "Đây là câu hỏi đầu tiên!", Toast.LENGTH_SHORT).show();
            }
        });
        btnSubmitExam.setOnClickListener(v -> showSubmitConfirmationDialog());
        // Tải danh sách câu hỏi từ cơ sở dữ liệu
        Database db = new Database(this);
        listCauhoi = db.listQues();

        // Hiển thị câu hỏi đầu tiên
        showQuestion();

        // Bắt đầu bộ đếm thời gian 5 phút
        startExamTimer(2 * 60 * 1000);

        // Hiển thị danh sách câu hỏi
        btnShowQuestionList.setOnClickListener(v -> showQuestionList());
        Button btnNextQuestion = findViewById(R.id.btn_next_question);
        btnNextQuestion.setOnClickListener(v -> showNextQuestion());

    }
    private void showNextQuestion() {
        if (currentQuestionIndex < listCauhoi.size() - 1) {
            currentQuestionIndex++; // Tăng chỉ số câu hỏi hiện tại
            showQuestion(); // Hiển thị câu hỏi mới
        } else {
            Toast.makeText(this, "Đây là câu hỏi cuối cùng!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        tvTimer = findViewById(R.id.tv_timer);
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvQuestionContent = findViewById(R.id.tv_question_content);
        cardAnswer1 = findViewById(R.id.card_answer1);
        cardAnswer2 = findViewById(R.id.card_answer2);
        cardAnswer3 = findViewById(R.id.card_answer3);
        cardAnswer4 = findViewById(R.id.card_answer4);
        btnShowQuestionList = findViewById(R.id.btn_show_question_list);

        // Đặt sự kiện click cho các lựa chọn đáp án
        cardAnswer1.setOnClickListener(this::onAnswerSelected);
        cardAnswer2.setOnClickListener(this::onAnswerSelected);
        cardAnswer3.setOnClickListener(this::onAnswerSelected);
        cardAnswer4.setOnClickListener(this::onAnswerSelected);
    }

    private void showQuestion() {
        resetAnswerCards();

        if (currentQuestionIndex < listCauhoi.size()) {
            cauhoi question = listCauhoi.get(currentQuestionIndex);
            tvQuestionNumber.setText("Câu hỏi " + (currentQuestionIndex + 1) + "/40");
            tvQuestionContent.setText(question.getNoidung());

            ((TextView) cardAnswer1.findViewById(R.id.tv_answer1)).setText(question.getTuychon1());
            ((TextView) cardAnswer2.findViewById(R.id.tv_answer2)).setText(question.getTuychon2());
            ((TextView) cardAnswer3.findViewById(R.id.tv_answer3)).setText(question.getTuychon3());
            ((TextView) cardAnswer4.findViewById(R.id.tv_answer4)).setText(question.getTuychon4());

            // Nếu người dùng đã trả lời câu này, làm nổi bật câu trả lời đã chọn
            if (userAnswers[currentQuestionIndex] != -1) {
                highlightAnswer(userAnswers[currentQuestionIndex]);
            }
        } else {
            finishExam();
        }
    }

    private void startExamTimer(long totalTime) {
        timer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 1000 / 60;
                long seconds = millisUntilFinished / 1000 % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "Thời gian: %02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                finishExam();
            }
        }.start();
    }

    private void onAnswerSelected(View view) {
        resetAnswerCards();

        // Lưu câu trả lời của người dùng
        if (view == cardAnswer1) {
            userAnswers[currentQuestionIndex] = 1;
        } else if (view == cardAnswer2) {
            userAnswers[currentQuestionIndex] = 2;
        } else if (view == cardAnswer3) {
            userAnswers[currentQuestionIndex] = 3;
        } else if (view == cardAnswer4) {
            userAnswers[currentQuestionIndex] = 4;
        }

        view.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
    }
    private void showSubmitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có chắc chắn muốn nộp bài?");

        // Nút xác nhận
        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            finishExam(); // Gọi phương thức tính điểm và chuyển Activity
        });

        // Nút hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        // Hiển thị hộp thoại
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetAnswerCards() {
        cardAnswer1.setBackgroundColor(getResources().getColor(android.R.color.white));
        cardAnswer2.setBackgroundColor(getResources().getColor(android.R.color.white));
        cardAnswer3.setBackgroundColor(getResources().getColor(android.R.color.white));
        cardAnswer4.setBackgroundColor(getResources().getColor(android.R.color.white));
    }


    private void highlightAnswer(int answerIndex) {
        switch (answerIndex) {
            case 1:
                cardAnswer1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                break;
            case 2:
                cardAnswer2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                break;
            case 3:
                cardAnswer3.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                break;
            case 4:
                cardAnswer4.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                break;
        }
    }

    private void showQuestionList() {
        String[] questionNumbers = new String[40];
        for (int i = 0; i < 40; i++) {
            // Kiểm tra nếu người dùng đã chọn đáp án cho câu hỏi tương ứng
            if (userAnswers[i] != -1) {
                questionNumbers[i] = "Câu " + (i + 1) + " (Đã chọn)";
            } else {
                questionNumbers[i] = "Câu " + (i + 1);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Danh sách câu hỏi");
        builder.setItems(questionNumbers, (dialog, which) -> {
            currentQuestionIndex = which; // Cập nhật câu hỏi hiện tại
            showQuestion(); // Hiển thị câu hỏi được chọn
        });
        builder.show();
    }

    private void finishExam() {
        timer.cancel();

        // Tính điểm
        float score = 0.0f;
        for (int i = 0; i < listCauhoi.size(); i++) {
            cauhoi question = listCauhoi.get(i);
            if (userAnswers[i] != -1) {
                String selectedAnswer = getAnswerByIndex(i);
                if (selectedAnswer.equals(question.getDapan())) {
                    score += 0.25f; // Mỗi câu đúng +0.25 điểm
                }
            }
        }

        // Điều chỉnh điểm cuối cùng
        float fractionalPart = score - (int) score; // Lấy phần thập phân
        if (fractionalPart == 0.25f || fractionalPart == 0.75f) {
            score += 0.05f; // Cộng thêm 0.05 điểm nếu phần thập phân là 0.25 hoặc 0.75
        }

        // Chuyển đến Activity hiển thị kết quả
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("SCORE", score); // Truyền điểm dạng float
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        if (timer != null) {
            timer.cancel(); // Hủy đồng hồ đếm ngược
        }
        super.onBackPressed(); // Gọi hành vi mặc định để trở về Activity trước đó
    }



    private String getAnswerByIndex(int questionIndex) {
        switch (userAnswers[questionIndex]) {
            case 1:
                return listCauhoi.get(questionIndex).getTuychon1();
            case 2:
                return listCauhoi.get(questionIndex).getTuychon2();
            case 3:
                return listCauhoi.get(questionIndex).getTuychon3();
            case 4:
                return listCauhoi.get(questionIndex).getTuychon4();
            default:
                return "";
        }
    }
}
