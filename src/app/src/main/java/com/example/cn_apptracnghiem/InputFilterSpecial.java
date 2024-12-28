package com.example.cn_apptracnghiem;
import android.text.InputFilter;
import android.text.Spanned;
public class InputFilterSpecial implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            // Loại bỏ khoảng trắng và dấu tiếng Việt
            if (Character.isWhitespace(c) || !isBasicLatinCharacter(c)) {
                return ""; // Loại bỏ ký tự không hợp lệ
            }
        }
        return null; // Chấp nhận chuỗi nhập
    }

    private boolean isBasicLatinCharacter(char c) {
        // Kiểm tra nếu ký tự thuộc Basic Latin Unicode block (không có dấu)
        return (c >= 0x0020 && c <= 0x007E) || c == '@' || c == '_';
    }
}