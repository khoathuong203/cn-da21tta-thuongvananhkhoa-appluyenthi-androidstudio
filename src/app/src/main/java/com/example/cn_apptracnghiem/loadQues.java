package com.example.cn_apptracnghiem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cn_apptracnghiem.model.cauhoi;

import java.util.ArrayList;
import java.util.Locale;

public class loadQues extends AppCompatActivity {

    private TextView tv_Num, tv_Ques, tv_Coutdown, tv_Score;
    private Button btn_Next;
    private CardView card_answer1, card_answer2, card_answer3, card_answer4;
    private CountDownTimer timer;
    private ArrayList<cauhoi> listCauhoi;
    private int quesCout, quesSize, Score;
    private boolean kq;
    private cauhoi cauhientai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_ques);
        anhxa();
        Score = 0;
        tv_Score.setText("Điểm: " + Score);

        Database db = new Database(this);
        listCauhoi = db.listQues();
        quesSize = listCauhoi.size();
        showNextQues();

        btn_Next.setOnClickListener(v -> {
            if (!kq) {
                checkAnswer();
            } else {
                showNextQues();
            }
        });
    }

    private void anhxa() {
        tv_Ques = findViewById(R.id.tv_question_content);
        tv_Num = findViewById(R.id.tv_question_number);
        tv_Score = findViewById(R.id.tv_score);
        tv_Coutdown = findViewById(R.id.tv_timer);
        btn_Next = findViewById(R.id.btn_next_question);

        card_answer1 = findViewById(R.id.card_answer1);
        card_answer2 = findViewById(R.id.card_answer2);
        card_answer3 = findViewById(R.id.card_answer3);
        card_answer4 = findViewById(R.id.card_answer4);

        card_answer1.setOnClickListener(this::onAnswerClick);
        card_answer2.setOnClickListener(this::onAnswerClick);
        card_answer3.setOnClickListener(this::onAnswerClick);
        card_answer4.setOnClickListener(this::onAnswerClick);
    }

    private void showNextQues() {
        resetAnswerCards();

        if (quesCout < quesSize) {
            cauhientai = listCauhoi.get(quesCout);

            tv_Ques.setText(cauhientai.getNoidung());
            ((TextView) card_answer1.findViewById(R.id.tv_answer1)).setText(cauhientai.getTuychon1());
            ((TextView) card_answer2.findViewById(R.id.tv_answer2)).setText(cauhientai.getTuychon2());
            ((TextView) card_answer3.findViewById(R.id.tv_answer3)).setText(cauhientai.getTuychon3());
            ((TextView) card_answer4.findViewById(R.id.tv_answer4)).setText(cauhientai.getTuychon4());

            quesCout++;
            tv_Num.setText("Câu hỏi " + quesCout + "/40");
            kq = false;
            btn_Next.setText("Xác nhận");
            startCountdown();
        } else {
            finishQues();
        }
    }

    private void onAnswerClick(View view) {
        if (kq) return;

        resetAnswerCards();
        view.setBackgroundColor(Color.LTGRAY);
        view.setTag("selected");
    }

    private void checkAnswer() {
        // Nếu người dùng chưa chọn đáp án, chỉ hiển thị thông báo và không làm gì thêm
        View selectedCard = findSelectedCard();
        if (selectedCard == null) {
            Toast.makeText(this, "Bạn chưa chọn đáp án!", Toast.LENGTH_SHORT).show();
            return; // Dừng lại, không tiếp tục kiểm tra và cho phép người dùng chọn lại
        }

        // Nếu đã chọn đáp án, tiếp tục xử lý
        kq = true;
        timer.cancel();

        // Lấy câu trả lời từ TextView trong thẻ câu trả lời đã chọn
        String dapanSelected = "";
        if (selectedCard == card_answer1) {
            dapanSelected = ((TextView) selectedCard.findViewById(R.id.tv_answer1)).getText().toString();
        } else if (selectedCard == card_answer2) {
            dapanSelected = ((TextView) selectedCard.findViewById(R.id.tv_answer2)).getText().toString();
        } else if (selectedCard == card_answer3) {
            dapanSelected = ((TextView) selectedCard.findViewById(R.id.tv_answer3)).getText().toString();
        } else if (selectedCard == card_answer4) {
            dapanSelected = ((TextView) selectedCard.findViewById(R.id.tv_answer4)).getText().toString();
        }

        // Kiểm tra đáp án
        if (dapanSelected.equals(cauhientai.getDapan())) {
            Score += 10;
            tv_Score.setText("Điểm: " + Score);
            selectedCard.setBackgroundColor(Color.GREEN);
        } else {
            selectedCard.setBackgroundColor(Color.RED);
            showCorrectAnswer();
        }

        btn_Next.setText("Tiếp theo");
    }



    private void showCorrectAnswer() {

        if (cauhientai.getDapan().equals(((TextView) card_answer1.findViewById(R.id.tv_answer1)).getText().toString())) {
            card_answer1.setBackgroundColor(Color.GREEN);
        } else if (cauhientai.getDapan().equals(((TextView) card_answer2.findViewById(R.id.tv_answer2)).getText().toString())) {
            card_answer2.setBackgroundColor(Color.GREEN);
        } else if (cauhientai.getDapan().equals(((TextView) card_answer3.findViewById(R.id.tv_answer3)).getText().toString())) {
            card_answer3.setBackgroundColor(Color.GREEN);
        } else if (cauhientai.getDapan().equals(((TextView) card_answer4.findViewById(R.id.tv_answer4)).getText().toString())) {
            card_answer4.setBackgroundColor(Color.GREEN);
        }
    }

    private void resetAnswerCards() {
        card_answer1.setBackgroundColor(Color.WHITE);
        card_answer2.setBackgroundColor(Color.WHITE);
        card_answer3.setBackgroundColor(Color.WHITE);
        card_answer4.setBackgroundColor(Color.WHITE);
        card_answer1.setTag(null);
        card_answer2.setTag(null);
        card_answer3.setTag(null);
        card_answer4.setTag(null);
    }

    private View findSelectedCard() {
        if ("selected".equals(card_answer1.getTag())) return card_answer1;
        if ("selected".equals(card_answer2.getTag())) return card_answer2;
        if ("selected".equals(card_answer3.getTag())) return card_answer3;
        if ("selected".equals(card_answer4.getTag())) return card_answer4;
        return null;
    }

    private void startCountdown() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_Coutdown.setText(String.format(Locale.getDefault(), "%ds", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tv_Coutdown.setText("0s");
                kq = true;
                showCorrectAnswer();
                btn_Next.setText("Tiếp theo");
            }
        }.start();
    }

    private void finishQues() {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putExtra("SCORE", Score);
        startActivity(intent);
        finish();
    }
}
