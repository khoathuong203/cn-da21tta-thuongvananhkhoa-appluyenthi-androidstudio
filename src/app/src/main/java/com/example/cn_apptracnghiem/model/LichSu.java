package com.example.cn_apptracnghiem.model;
public class LichSu {
    private int id_his;
    private int user_id;
    private String ngayThucHien;
    private int diem;

    public LichSu(int id_his, int user_id, String ngayThucHien, int diem) {
        this.id_his = id_his;
        this.user_id = user_id;
        this.ngayThucHien = ngayThucHien;
        this.diem = diem;
    }

    public int getId_his() {
        return id_his;
    }

    public void setId_his(int id_his) {
        this.id_his = id_his;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNgayThucHien() {
        return ngayThucHien;
    }

    public void setNgayThucHien(String ngayThucHien) {
        this.ngayThucHien = ngayThucHien;
    }

    public int getDiem() {
        return diem;
    }

    public void setDiem(int diem) {
        this.diem = diem;
    }
}
