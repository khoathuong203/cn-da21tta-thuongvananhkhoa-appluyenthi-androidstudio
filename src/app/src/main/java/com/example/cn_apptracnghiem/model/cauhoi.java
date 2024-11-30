package com.example.cn_apptracnghiem.model;

public class cauhoi {
    private int id, phanloai;
    private String noidung, tuychon1, tuychon2, tuychon3, tuychon4, dapan;


    public cauhoi() {
    }

    public cauhoi(String noidung, String tuychon1, String tuychon2, String tuychon3, String tuychon4, String dapan,int phanloai) {
        this.noidung = noidung;
        this.tuychon1 = tuychon1;
        this.tuychon2 = tuychon2;
        this.tuychon3 = tuychon3;
        this.tuychon4 = tuychon4;
        this.dapan = dapan;
        this.phanloai = phanloai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhanloai() {
        return phanloai;
    }

    public void setPhanloai(int phanloai) {
        this.phanloai = phanloai;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getTuychon1() {
        return tuychon1;
    }

    public void setTuychon1(String tuychon1) {
        this.tuychon1 = tuychon1;
    }

    public String getTuychon2() {
        return tuychon2;
    }

    public void setTuychon2(String tuychon2) {
        this.tuychon2 = tuychon2;
    }

    public String getTuychon3() {
        return tuychon3;
    }

    public void setTuychon3(String tuychon3) {
        this.tuychon3 = tuychon3;
    }

    public String getTuychon4() {
        return tuychon4;
    }

    public void setTuychon4(String tuychon4) {
        this.tuychon4 = tuychon4;
    }

    public String getDapan() {
        return dapan;
    }

    public void setDapan(String dapan) {
        this.dapan = dapan;
    }
}
