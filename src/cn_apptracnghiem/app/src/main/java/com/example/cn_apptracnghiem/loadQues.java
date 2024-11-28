package com.example.cn_apptracnghiem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cn_apptracnghiem.model.cauhoi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class loadQues extends AppCompatActivity {

    private TextView tv_Num, tv_Ques, getTv_Num, tv_Coutdown, tv_Score;
    private RadioButton rb_answer1, rb_answer2, rb_answer3, rb_answer4;
    private RadioGroup radioGroup;
    private Button btn_Next;
    private CountDownTimer timer;
    private ArrayList<cauhoi> listCauhoi;
    private long timeleftInMillis;
    private int quesCout;
    private int quesSize;
    private int Score;
    private boolean kq;
    private int count = 0;
    private cauhoi cauhientai;
    private android.os.CountDownTimer CountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_ques);
        anhxa();
        Score = 0; // Điểm ban đầu là 0
        tv_Score.setText("Điểm: " + Score);

        Database db = new Database(this);
        listCauhoi = db.listQues();
        quesSize = listCauhoi.size();
        showNextQues();

        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!kq){
                    if(rb_answer1.isChecked() || rb_answer2.isChecked() || rb_answer3.isChecked() || rb_answer4.isChecked()){
                        checkAnswer();
                    }
                    else {
                        Toast.makeText(loadQues.this,"Hãy chọn đáp án",Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    showNextQues();
                }
            }
        });
    }

    private void showNextQues() {
        rb_answer1.setTextColor(Color.BLACK);
        rb_answer2.setTextColor(Color.BLACK);
        rb_answer3.setTextColor(Color.BLACK);
        rb_answer4.setTextColor(Color.BLACK);

        radioGroup.clearCheck();

        if(quesCout < quesSize){
            cauhientai = listCauhoi.get(quesCout);

            tv_Ques.setText(cauhientai.getNoidung());
            rb_answer1.setText(cauhientai.getTuychon1());
            rb_answer2.setText(cauhientai.getTuychon2());
            rb_answer3.setText(cauhientai.getTuychon3());
            rb_answer4.setText(cauhientai.getTuychon4());

            quesCout++;
            tv_Num.setText("Câu hỏi "+quesCout+"/40");
            kq = false;
            btn_Next.setText("Xác nhận");
            timeleftInMillis = 60000;
            startCountdown();
        }
        else {
            finishQues();
        }
    }

    private void startCountdown() {
        CountDownTimer = new CountDownTimer(timeleftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleftInMillis = millisUntilFinished;
                updateCountText();
            }

            @Override
            public void onFinish() {
                timeleftInMillis = 0;
                updateCountText(); // Cập nhật đồng hồ thành 00:00
                if (!kq) { // Nếu người dùng chưa trả lời
                    showSolution();
                }
            }
        }.start();
    }

    private void checkAnswer() {
        kq = true; // Đánh dấu câu hỏi đã được trả lời
        CountDownTimer.cancel(); // Dừng đồng hồ

        RadioButton radSelected = findViewById(radioGroup.getCheckedRadioButtonId());
        if (radSelected == null) { // Nếu chưa chọn đáp án
            Toast.makeText(this, "Bạn chưa chọn đáp án!", Toast.LENGTH_SHORT).show();
            showSolution(); // Hiển thị đáp án đúng
            return;
        }

        String dapanSelected = radSelected.getText().toString();
        if (dapanSelected.equals(cauhientai.getDapan())) {
            Score += 10; // Nếu đúng thì cộng điểm
            tv_Score.setText("Điểm: " + Score);
        }

        showSolution();
    }

    private void updateCountText() {
        int minute = (int) (timeleftInMillis / 1000) / 60;
        int second = (int) (timeleftInMillis / 1000) % 60;

        String timeFormat = String.format(Locale.getDefault(), "%02d:%02d", minute, second);
        tv_Coutdown.setText(timeFormat);

        if (timeleftInMillis < 10000) {
            tv_Coutdown.setTextColor(Color.RED);
        } else {
            tv_Coutdown.setTextColor(Color.BLACK);
        }
    }

    private void showSolution() {
        rb_answer1.setTextColor(Color.RED);
        rb_answer2.setTextColor(Color.RED);
        rb_answer3.setTextColor(Color.RED);
        rb_answer4.setTextColor(Color.RED);

        // Đánh dấu đáp án đúng bằng màu xanh lá
        if (cauhientai.getDapan().equals(rb_answer1.getText().toString())) {
            rb_answer1.setTextColor(Color.GREEN);
            tv_Ques.setText("Đáp án đúng là A");
        } else if (cauhientai.getDapan().equals(rb_answer2.getText().toString())) {
            rb_answer2.setTextColor(Color.GREEN);
            tv_Ques.setText("Đáp án đúng là B");
        } else if (cauhientai.getDapan().equals(rb_answer3.getText().toString())) {
            rb_answer3.setTextColor(Color.GREEN);
            tv_Ques.setText("Đáp án đúng là C");
        } else if (cauhientai.getDapan().equals(rb_answer4.getText().toString())) {
            rb_answer4.setTextColor(Color.GREEN);
            tv_Ques.setText("Đáp án đúng là D");
        }

        // Cập nhật nút sang "Tiếp theo" hoặc "Hoàn thành"
        if (quesCout < quesSize) {
            btn_Next.setText("Tiếp theo");
        } else {
            btn_Next.setText("Hoàn thành");
        }
        CountDownTimer.cancel();

    }


    private void finishQues() {
        Intent result = new Intent();
        result.putExtra("Score", Score);
        setResult(RESULT_OK,result);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        count++;
        if (count>=1){
            finishQues();
        }
        count=0;
    }

    private void anhxa(){
        tv_Ques = findViewById(R.id.tv_question_content);
        tv_Num = findViewById(R.id.tv_question_number);
        tv_Score = findViewById(R.id.tv_score);
        tv_Coutdown = findViewById(R.id.tv_timer);
        rb_answer1 = findViewById(R.id.rb_answer1);
        rb_answer2 = findViewById(R.id.rb_answer2);
        rb_answer3 = findViewById(R.id.rb_answer3);
        rb_answer4 = findViewById(R.id.rb_answer4);
        radioGroup = findViewById(R.id.radioGroup_answers);
        btn_Next = findViewById(R.id.btn_next_question);
    }
}