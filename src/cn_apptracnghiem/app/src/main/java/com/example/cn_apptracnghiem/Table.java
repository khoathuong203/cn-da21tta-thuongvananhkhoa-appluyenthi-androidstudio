package com.example.cn_apptracnghiem;

import android.provider.BaseColumns;

public class Table {
    private Table(){

    }
    public static class QuestionsTable implements BaseColumns{
        public static final String TableName = "cauhoi"; // Tên bảng
        public static final String id = "id";
        public static final String noidung = "noidung";
        public static final String tuychon1 = "tuychon1";
        public static final String tuychon2 = "tuychon2";
        public static final String tuychon3 = "tuychon3";
        public static final String tuychon4 = "tuychon4";
        public static final String dapan = "dapan";
        public static final String phanloai = "phanloai";
    }
}
