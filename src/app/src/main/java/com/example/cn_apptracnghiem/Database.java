package com.example.cn_apptracnghiem;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import com.example.cn_apptracnghiem.model.LichSu;
import com.example.cn_apptracnghiem.model.cauhoi;

import java.util.ArrayList;

import javax.xml.transform.sax.SAXResult;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Question.db";
    private static final int VERSION = 1;

    private SQLiteDatabase db;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String QUESTION_TABLE = "CREATE TABLE "+ Table.QuestionsTable.TableName+"("+
                Table.QuestionsTable.id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Table.QuestionsTable.noidung+ " TEXT,"+
                Table.QuestionsTable.tuychon1+ " TEXT,"+
                Table.QuestionsTable.tuychon2+ " TEXT,"+
                Table.QuestionsTable.tuychon3+ " TEXT,"+
                Table.QuestionsTable.tuychon4+ " TEXT,"+
                Table.QuestionsTable.dapan+ " TEXT,"+
                Table.QuestionsTable.phanloai+" INTEGER"
                +")";
        db.execSQL(QUESTION_TABLE);
        final String USER_TABLE = "CREATE TABLE nguoidung(" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT)";
        db.execSQL(USER_TABLE);
        final String HISTORY_TABLE = "CREATE TABLE lichsu (" +
                "id_his INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Sửa tên cột thành id_his
                "user_id INTEGER, " +
                "ngaythuchien TEXT, " +
                "diem INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES nguoidung(user_id)" +
                ")";

        db.execSQL(HISTORY_TABLE);
        valueQues();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+Table.QuestionsTable.TableName);
        db.execSQL("DROP TABLE IF EXISTS nguoidung");
        db.execSQL("DROP TABLE IF EXISTS lichsu");
        onCreate(db);
    }

    private void insertQues(cauhoi ques){
        ContentValues values = new ContentValues();

        values.put(Table.QuestionsTable.noidung,ques.getNoidung());
        values.put(Table.QuestionsTable.tuychon1,ques.getTuychon1());
        values.put(Table.QuestionsTable.tuychon2,ques.getTuychon2());
        values.put(Table.QuestionsTable.tuychon3,ques.getTuychon3());
        values.put(Table.QuestionsTable.tuychon4,ques.getTuychon4());
        values.put(Table.QuestionsTable.dapan,ques.getDapan());
        values.put((Table.QuestionsTable.phanloai),ques.getPhanloai());

        db.insert(Table.QuestionsTable.TableName,null,values);
    }

    public ArrayList<LichSu> getUserHistory(int userId) {
        ArrayList<LichSu> historyList = new ArrayList<>();
        db = getReadableDatabase();

        // Câu truy vấn đúng
        String query = "SELECT * FROM lichsu WHERE user_id = ? ORDER BY ngaythuchien DESC";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (c.moveToFirst()) {
            do {
                // Kiểm tra cột trước khi truy xuất giá trị
                int colHisId = c.getColumnIndex("id_his");  // Đảm bảo đúng tên cột
                int colUserId = c.getColumnIndex("user_id");
                int colNgayThucHien = c.getColumnIndex("ngaythuchien");
                int colDiem = c.getColumnIndex("diem");

                // Kiểm tra nếu các cột hợp lệ (không phải -1)
                if (colHisId >= 0 && colUserId >= 0 && colNgayThucHien >= 0 && colDiem >= 0) {
                    LichSu lichSu = new LichSu(
                            c.getInt(colHisId),
                            c.getInt(colUserId),
                            c.getString(colNgayThucHien),
                            c.getInt(colDiem)
                    );
                    historyList.add(lichSu);
                }
            } while (c.moveToNext());
        }

        c.close();
        return historyList;
    }


    private void valueQues(){

        cauhoi c1 = new cauhoi("Hệ điều hành là:", "Phần mềm ứng dụng", "Phần mềm tiện ích", "Phần mềm hệ thống", "Phần mềm ứng dụng và tiện ích", "Phần mềm hệ thống", 1);
        cauhoi c2 = new cauhoi("Những thiết bị nào sau đây là thiết bị nhập:", "Loa, bàn phím, màn hình", "Webcam, bàn phím, micro", "Bàn phím, màn hình", "Màn hình, chuột, bàn phím", "Webcam, bàn phím, micro", 1);
        cauhoi c3 = new cauhoi("Bộ nhớ trong của máy tính gồm:", "RAM và ROM", "Đĩa cứng và đĩa quang", "Ram và đĩa cứng", "Thẻ nhớ và USB", "RAM và ROM", 1);
        cauhoi c4 = new cauhoi("Hệ đếm nhị phân sử dụng những chữ số nào?", "0 và 1", "0 đến 9", "a đến z", "1 và 2", "0 và 1", 1);
        cauhoi c5 = new cauhoi("Hãy cho biết các thiết bị nào sau đây được xem là thiết bị nhập?", "Chuột, bàn phím, máy quét", "Chuột, loa, màn hình", "Màn hình, loa, máy in", "Màn hình, bàn phím, máy in", "Chuột, bàn phím, máy quét", 1);
        cauhoi c6 = new cauhoi("Máy in là:", "Thiết bị nhập", "Thiết bị xuất", "Thiết bị lưu trữ", "Thiết bị xử lý", "Thiết bị xuất", 1);
        cauhoi c7 = new cauhoi("Các thiết bị nhập của một hệ thống máy tính:", "Chuột, bàn phím, máy quét, webcam.", "Chuột, bàn phím, màn hình.", "Màn hình, máy in, loa.", "Máy in.", "Chuột, bàn phím, máy quét, webcam.", 1);
        cauhoi c8 = new cauhoi("Để máy tính có thể làm việc được, hệ điều hành cần được nạp vào:", "RAM", "Bộ nhớ ngoài", "Chỉ nạp vào bộ nhớ trong khi chạy chương trình ứng dụng", "ROM", "RAM", 1);
        cauhoi c9 = new cauhoi("Cho biết thiết bị nào sau đây không là thiết bị xuất:", "Màn hình", "Loa", "Bàn phím", "Máy in", "Bàn phím", 1);
        cauhoi c10 = new cauhoi("Hệ đếm thập phân sử dụng những chữ số nào?", "Từ A đến Z", "Từ 0 đến 9", "Từ a đến z", "Từ 9 đến z", "Từ 0 đến 9", 1);
        cauhoi c11 = new cauhoi("Trong Windows 10, Recycle Bin là nơi:", "Để chứa những thông tin mới cần thiết", "Lưu trữ tập tin, thư mục sau khi bị xóa và ta có thể phục hồi", "Chứa dữ liệu của hệ thống", "Lưu trữ tập tin, thư mục sau khi bị xóa và ta không thể phục hồi", "Lưu trữ tập tin, thư mục sau khi bị xóa và ta có thể phục hồi", 2);
        cauhoi c12 = new cauhoi("Trong Windows 10, để sắp xếp các biểu tượng trên màn hình nền theo kích thước, ta nhấp phải chuột vào vùng trống trên màn hình nền, chọn Sort by, chọn:", "Name", "Size", "Type", "Modified", "Size", 2);
        cauhoi c13 = new cauhoi("Trong Windows 10 chương trình Calculator là chương trình được dùng để:", "Tạo văn bản", "Tính toán", "Vẽ hình", "Quản lý tập tin", "Tính toán", 2);
        cauhoi c14 = new cauhoi("Trong Windows 10, để sắp xếp các biểu tượng trên màn hình nền theo kiểu, ta nhấp phải chuột vào vùng trống trên màn hình nền, chọn Sort by, chọn:", "Name", "Size", "Type", "Modified", "Type", 2);
        cauhoi c15 = new cauhoi("Để tạo một tập tin có phần mở rộng .txt ta chọn chuột phải ở vùng trống sau đó:", "New -> Text Document", "New -> Folder", "New -> Shortcut", "New -> Bitmap Image", "New -> Text Document", 2);
        cauhoi c16 = new cauhoi("Trong Windows 10, tại cửa sổ hiện hành của ổ đĩa cứng (giả sử ổ đĩa D), để chọn các đối tượng rời rạc, trong quá trình chọn ta nhấn giữ phím:", "Alt", "Ctrl", "Shift", "Windows", "Ctrl", 2);
        cauhoi c17 = new cauhoi("Để tạo một thư mục mới, ta chọn chuột phải ở vùng trống sau đó:", "New -> Text Document", "New -> Folder", "New -> Shortcut", "New -> Bitmap Image", "New -> Folder", 2);
        cauhoi c18 = new cauhoi("Để lựa chọn các đối tượng không liên tục trong một cửa sổ, cùng với việc click chọn đối tượng thì ta phải nhấn và giữ phím nào sau đây?", "Ctrl", "Alt", "Shift", "Enter", "Ctrl", 2);
        cauhoi c19 = new cauhoi("Để chuyển đổi qua lại giữa các cửa sổ trên thanh tác vụ (Taskbar) ta sử dụng tổ hợp phím:", "Ctrl + Tab", "Alt + Tab", "Ctrl + Shift", "Alt + Shift", "Alt + Tab", 2);
        cauhoi c20 = new cauhoi("Trong Windows 10, để đóng cửa sổ chương trình ứng dụng hiện hành ta nhấn tổ hợp phím:", "Ctrl + F4", "Tab + F4", "Alt + F4", "Shift + F4", "Alt + F4", 2);
        cauhoi c21 = new cauhoi("Trong  MS Word 2013, tổ hợp phím nào cho phép chọn tất cả văn bản đang soạn thảo:", "Ctrl + F", "Alt + A", "Ctrl + A", "Alt + F", "Ctrl + A", 3);
        cauhoi c22 = new cauhoi("Phần mở rộng mặc định dùng trong Microsolf Word 2013:", ".xlsx", ".pptx", ".txt", ".docx", ".docx", 3);
        cauhoi c23 = new cauhoi("Trong  MS Word 2013, chức năng của tổ hợp phím Ctrl + S:", "Lưu file văn bản đang soạn thảo", "Xóa file văn bản đang soạn thảo", "Chèn kí hiệu đặc biệt", "Tạo file văn bản mới", "Lưu file văn bản đang soạn thảo", 3);
        cauhoi c24 = new cauhoi("Khi soạn thảo văn bản với MS Word 2013, tổ hợp phím Ctrl + A có ý nghĩa:", "Lưu bài", "Dán", "Xóa", "Chọn khối toàn văn bản", "Chọn khối toàn văn bản", 3);
        cauhoi c25 = new cauhoi("Hãy cho biết muốn tạo chữ nghệ thuật trong MS Word 2013, ta thực hiện:", "Thẻ Insert -> nhóm Picture -> WordArt", "Thẻ Insert -> nhóm Text -> WordArt", "Thẻ Home -> nhóm Paragraph -> Drop Cap", "Thẻ Page Layout -> nhóm Paragraph -> Drop Cap", "Thẻ Insert -> nhóm Text -> WordArt", 3);
        cauhoi c26 = new cauhoi("Trong MS Word 2013, để mở một tài liệu có sẵn trên máy ta thực hiện như sau:", "Chọn thẻ File -> Open", "Chọn thẻ Home -> Open", "Chọn thẻ Insert -> Open", "Chọn Office Button -> Open", "Chọn thẻ File -> Open", 3);
        cauhoi c27 = new cauhoi("Trong MS Word 2013, muốn chọn hướng giấy đứng, ta làm như sau:", "Page Layout -> Text Direction", "Page Layout -> Page Setup -> Trong thẻ Margins, phần Orientation chọn Portrait", "Page Layout -> Page Setup -> Trong thẻ Margins, phần Orientation chọn Landscape", "View -> trong Document Views chọn Outline", "Page Layout -> Page Setup -> Trong thẻ Margins, phần Orientation chọn Portrait", 3);
        cauhoi c28 = new cauhoi("Trong MS Word 2013, để hiển thị thanh thước ta chọn:", "Home -> Ruler", "Insert -> Ruler", "View -> Ruler", "Review -> Ruler", "View -> Ruler", 3);
        cauhoi c29 = new cauhoi("Muốn đánh chữ số tự động ở đầu dòng cho các đoạn văn bản thì chọn:", "Thẻ Home -> nhóm Font -> biểu tượng    (Numbering)", "Thẻ Home -> nhóm Paragraph -> biểu tượng   (Numbering)", "Thẻ Insert -> nhóm Text -> biểu tượng   (Numbering)", "Thẻ View -> nhóm Paragraph -> biểu tượng   (Numbering)", "Thẻ Home -> nhóm Paragraph -> biểu tượng   (Numbering)", 3);
        cauhoi c30 = new cauhoi("Khi soạn thảo văn bản với Microsoft Word, tổ hợp phím Ctrl + C có ý nghĩa:", "Cắt.", "Sao chép.", "Dán.", "Lấy lại thao tác trước.", "Sao chép.", 3);
        cauhoi c31 = new cauhoi("Sử dụng tổ hợp phím nào sau đây để lưu một văn bản trong MS Word 2013?", "Ctrl + L", "Alt + S", "Shift + S", "Ctrl + S", "Ctrl + S", 3);
        cauhoi c32 = new cauhoi("Khi soạn thảo văn bản với Microsoft Word, tổ hợp phím Ctrl + A có ý nghĩa:", "Cắt.", "Dán.", "Xóa.", "Chọn toàn bộ văn bản.", "Chọn toàn bộ văn bản.", 3);
        cauhoi c33 = new cauhoi("Khi soạn thảo văn bản với MS Word 2013, tổ hợp phím Ctrl + O có ý nghĩa:", "Lưu lại với tên mới", "Lưu tập tin", "Mở cửa sổ mới", "Mở tập tin có sẵn", "Mở tập tin có sẵn", 3);
        cauhoi c34 = new cauhoi("Khi soạn thảo văn bản với MS Word 2013, tổ hợp phím Ctrl + S có ý nghĩa:", "Lưu lại với tên mới", "Lưu tập tin", "Mở cửa sổ mới", "Mở tập tin có sẵn", "Lưu tập tin", 3);
        cauhoi c35 = new cauhoi("Khi soạn thảo văn bản với MS Word 2013, tổ hợp phím Ctrl + N có ý nghĩa:", "Lưu lại với tên mới", "Lưu tập tin", "Mở cửa sổ mới", "Mở tập tin có sẵn", "Mở cửa sổ mới", 3);
        cauhoi c36 = new cauhoi("Trong MS Word 2013, hãy cho biết tổ hợp phím nào sau đây cho phép ngay lập tức đưa con trỏ về cuối văn bản:", "Ctrl + End", "Alt + End", "Shift + End", "Ctrl + Alt + End", "Ctrl + End", 3);
        cauhoi c37 = new cauhoi("Trong MS Word 2013, để mở hộp thoại định dạng Tab ta thực hiện các thao tác sau:", "Chọn thẻ Home -> nhóm Font -> chọn nút hiển thị hộp thoại Font -> chọn nút Tabs", "Chọn thẻ Home -> nhóm Styles -> chọn nút hiển thị hộp thoại Styles -> chọn nút Tabs", "Chọn thẻ  Home -> nhóm Paragraph -> chọn nút hiển thị hộp thoại Paragraph -> chọn nút Tabs", "Chọn thẻ Home -> nhóm Clipboard -> chọn nút hiển thị hộp thoại Clipboard -> chọn nút Tabs", "Chọn thẻ  Home -> nhóm Paragraph -> chọn nút hiển thị hộp thoại Paragraph -> chọn nút Tabs", 3);
        cauhoi c38 = new cauhoi("Trong MS Word 2013 để chia cột báo, ta chọn:", "Format -> Columns", "Page layout -> Columns", "Format -> Tabs", "Page layout -> Size", "Page layout -> Columns", 3);
        cauhoi c39 = new cauhoi("Trong MS Word 2013, hãy cho biết tổ hợp phím nào sau đây được dùng để mở một tài liệu có sẵn:", "Ctrl + O", "Ctrl + N", "Ctrl + S", "Ctrl + P", "Ctrl + O", 3);
        cauhoi c40 = new cauhoi("Thao tác chọn Insert -> Text -> Drop Cap trong MS Word 2013 dùng để:", "Tạo chữ cái lớn đầu dòng", "Chia văn bản dạng cột báo", "Tạo một siêu liên kết", "Tạo chữ nghệ thuật", "Tạo chữ cái lớn đầu dòng", 3);
        cauhoi c41 = new cauhoi("Trong MS Excel 2013, tại ô A2 có giá trị là chuỗi “TIN HOC”. Tại ô B2 gõ công thức =RIGHT(A2,3) thì nhận được kết quả:", "HOC", "3", "TIN", "Tin hoc", "HOC", 4);
        cauhoi c42 = new cauhoi("Trong MS Excel 2013, tại ô A2 có giá trị là dãy kí tự 'Tin hoc van phong' . Tại ô B2 gõ vào công thức =LOWER(A2) thì nhận được kết quả:", "TIN HOC VAN PHONG", "tin hoc van phong", "Tin hoc van phong", "Tin Hoc Van Phong", "tin hoc van phong", 4);
        cauhoi c43 = new cauhoi("Trong MS Excel 2013, tại ô A2 có giá trị là  10 ; ô B2 có giá trị là 3. Tại ô C2 gõ công thức =MOD(A2,B2) thì nhận được kết quả :", "10", "3", "1", "#Value", "1", 4);
        cauhoi c44 = new cauhoi("Trong MS Excel 2013, địa chỉ nào là địa chỉ tuyệt đối:", "B$1:D$10", "$B1:$D10", "B$1$:D$10$", "$B$1:$D$10", "$B$1:$D$10", 4);
        cauhoi c45 = new cauhoi("Trong MS Excel 2013, tại ô A2 có giá trị  là 25. Tại ô B2 gõ vào công thức =SQRT(A2) thì nhận được kết quả:", "5", "0", "#VALUE!", "#NAME!", "5", 4);
        cauhoi c46 = new cauhoi("Trong MS Excel 2013, hàm nào sau đây có chức năng tính trung bình cộng?", "Sum", "DSum", "Average", "Max", "Average", 4);
        cauhoi c47 = new cauhoi("Cửa sổ của Excel thuộc  loại?", "Cửa sổ tập tin", "Cửa sổ ứng dụng", "Cửa sổ tư liệu", "Cửa sổ thư mục", "Cửa sổ ứng dụng", 4);
        cauhoi c48 = new cauhoi("Khi đang làm việc với MS Excel 2013, tổ hợp phím nào cho phép đưa con trỏ về ô đầu tiên (ô A1) của bảng tính?", "Ctrl + Home", "Alt + Home", "Shift + Home", "Shift + Ctrl + Home", "Ctrl + Home", 4);
        cauhoi c49 = new cauhoi("Giả sử ô A3 có giá trị là H5A6T, nếu tại ô A4 ta có công thức là =MID(A3,2,3) thì kết quả trả về là:", "A6", "H5A", "5A6", "A6T", "5A6", 4);
        cauhoi c50 = new cauhoi("Trong bảng tính Excel, tại ô A2 có sẵn giá trị số 25; Tại ô B2 gõ vào công thức =SQRT(A2) thì nhận được kết quả hiện:", "0", "5", "#VALUE!", "#NAME!", "5", 4);
        cauhoi c51 = new cauhoi("Trong Excel, tại ô A2 có giá trị là số 10; ô B2 có giá trị là số 3. Tại ô C2 gõ công thức =MOD(A2,B2) thì nhận được kết quả:", "10", "3", "#Value", "1", "1", 4);
        cauhoi c52 = new cauhoi("Trong bảng tính Excel, tại ô A2 có sẵn giá trị chuỗi TINHOC; Tại ô B2 gõ vào công thức =VALUE(A2) thì nhận được kết quả:", "#VALUE!", "Tinhoc", "TINHOC", "6", "#VALUE!", 4);
        cauhoi c53 = new cauhoi("Trong MS Excel 2013 hàm dùng để chuyển chuỗi ký tự chữ số sang dữ liệu kiểu số là:", "Value(chuỗi ký tự số)", "Right(chuỗi ký tự số, n)", "Mid(chuỗi ký tự số, m, n)", "Left(chuỗi ký tự số, n)", "Value(chuỗi ký tự số)", 4);
        cauhoi c54 = new cauhoi("Trong bảng tính Excel, tại ô A2 có sẵn giá trị chuỗi 2011; Tại ô B2 gõ vào công thức = VALUE(A2) thì nhận được kết quả:", "#NAME!", "#VALUE!", "Giá trị kiểu chuỗi 2011", "Giá trị kiểu số 2011", "Giá trị kiểu số 2011", 4);
        cauhoi c55 = new cauhoi("Trong bảng tính Excel, tại ô A2 gõ vào công thức =MAX(30,10,65,5) thì nhận được kết quả tại ô A2 là:", "30", "5", "65", "110", "65", 4);
        cauhoi c56 = new cauhoi("Trong Excel, tại ô A2 có giá trị là “Tinhoc”; Tại ô C2 gõ vào công thức =A2 thì nhận được kết quả tại ô C2:", "#Value", "TINHOC", "TinHoc", "Tinhoc", "Tinhoc", 4);
        cauhoi c57 = new cauhoi("Trong MS Excel 2013, hàm SUM dùng để?", "Tính tổng", "Tính trung bình", "Tính tổng có điều kiện", "Đếm số lượng ô", "Tính tổng", 4);
        cauhoi c58 = new cauhoi("Trong bảng tính Excel, hàm nào sau đây cho phép tính tổng các giá trị kiểu số thỏa mãn một điều kiện cho trước?", "SUM", "COUNTIF", "COUNT", "SUMIF", "SUMIF", 4);
        cauhoi c59 = new cauhoi("Trong bảng tính Excel, hàm nào dưới đây cho phép tính tổng các giá trị kiểu số?", "SUM", "COUNTIF", "COUNT", "SUMIF", "SUM", 4);
        cauhoi c60 = new cauhoi("Trong MS Excel 2013, hàm nào sau đây có chức năng tìm giá trị lớn nhất?", "Sum", "Min", "Average", "Max", "Max", 4);
        cauhoi c61 = new cauhoi("Trong MS PowerPoint 2013, để thay đổi thiết lập về Font:", "Chọn Font -> nhóm Format  Font -> nút Format", "Chọn Home -> nhóm Font -> nút Font", "Chọn Slide Show  -> nhóm Slide Show -> nút  Slide", "Chọn Review  -> nhóm Review -> nút Font", "Chọn Home -> nhóm Font -> nút Font", 5);
        cauhoi c62 = new cauhoi("Trong MS PowerPoint 2013, để chèn đối tượng Shape:", "Chọn Home -> nhóm Home -> Shapes", "Chọn Insert -> nhóm Illustrations -> Shapes", "Chọn Shapes -> nhóm Shapes -> Shapes", "Chọn Format -> nhóm Format -> Shapes", "Chọn Insert -> nhóm Illustrations -> Shapes", 5);
        cauhoi c63 = new cauhoi("Trong MS PowerPoint 2013, để thiết kế Slide Master ta thực hiện các thao tác sau:", "Chọn thẻ Home -> nhóm Master Views -> biểu tượng Slide Master", "Chọn thẻ Design -> nhóm Master Views -> biểu tượng Slide Master", "Chọn thẻ View -> nhóm Master Views -> biểu tượng Slide Master", "Chọn thẻ Review -> nhóm Master Views -> biểu tượng Slide Master", "Chọn thẻ View -> nhóm Master Views -> biểu tượng Slide Master", 5);
        cauhoi c64 = new cauhoi("Khi đang trình diễn trong Powerpoint, muốn kết thúc phiên trình diễn, ta thực hiện:", "Click phải chuột, rồi chọn Exit", "Click phải chuột, rồi chọn Return", "Click phải chuột, rồi chọn End Show", "Click phải chuột, rồi chọn Screen", "Click phải chuột, rồi chọn End Show", 5);
        cauhoi c65 = new cauhoi("Trong MS PowerPoint 2013, để ẩn/hiện Gridlines trên các slide ta thực hiện các thao tác sau:", "Chọn thẻ View -> nhóm Show -> đánh dấu chọn hoặc bỏ chọn trong ô checkbox Gridlines", "Chọn thẻ Review -> nhóm Show -> đánh dấu chọn hoặc bỏ chọn trong ô checkbox Gridlines", "Chọn thẻ Slide Show -> nhóm Show -> đánh dấu chọn hoặc bỏ chọn trong ô checkbox Gridlines", "Chọn thẻ Insert -> nhóm Show -> đánh dấu chọn hoặc bỏ chọn trong ô checkbox Gridlines", "Chọn thẻ View -> nhóm Show -> đánh dấu chọn hoặc bỏ chọn trong ô checkbox Gridlines", 5);
        cauhoi c66 = new cauhoi("Trong MS PowerPoint 2013, để tạo mới một Slide, ta chọn thẻ:", "Home -> Layout", "Home -> New Slide", "Home -> Reset", "Home -> Delete", "Home -> New Slide", 5);
        cauhoi c67 = new cauhoi("Hãy cho biết để trình chiếu các Slide trong MS PowerPoint 2013 ta sử dụng:", "Phím F2", "Phím F5", "Tổ hợp phím Ctrl + F5", "Tổ hợp phím Alt + F5", "Phím F5", 5);
        cauhoi c68 = new cauhoi("Trong MS PowerPoint 2013, để chèn các hình cơ bản (Shapes) vào slide ta thực hiện các thao tác sau:", "Chọn thẻ View -> nhóm Illustrations -> biểu tượng Shapes.", "Chọn thẻ Insert -> nhóm Illustrations -> biểu tượng Shapes.", "Chọn thẻ Design -> nhóm Illustrations -> biểu tượng Shapes.", "Chọn thẻ Home -> nhóm Illustrations -> biểu tượng Shapes.", "Chọn thẻ Insert -> nhóm Illustrations -> biểu tượng Shapes.", 5);
        cauhoi c69 = new cauhoi("Trong MS PowerPoint 2013, muốn tạo hiệu ứng cho chữ và hình, ta chọn thẻ:", "Home", "Design", "View", "Animations", "Animations", 5);
        cauhoi c70 = new cauhoi("Trong MS PowerPoint 2013, để đánh số thứ tự cho slide ta thực hiện các thao tác sau:", "Chọn thẻ Insert -> nhóm Text -> biểu tượng Slide Number", "Chọn thẻ Insert -> nhóm Illustrations -> biểu tượng Slide Number", "Chọn thẻ Insert -> nhóm Text -> biểu tượng Object", "Chọn thẻ Insert -> nhóm Text -> biểu tượng Date & Time", "Chọn thẻ Insert -> nhóm Text -> biểu tượng Slide Number", 5);
        cauhoi c71 = new cauhoi("Mạng LAN có ý nghĩa:", "Mạng cục bộ", "Mạng diện rộng", "Mạng toàn cầu", "Một ý nghĩa khác", "Mạng cục bộ", 6);
        cauhoi c72 = new cauhoi("Những phần mềm duyệt web:", "Internet Explorer, Firefox, MS - Windows", "Internet Explorer, Firefox, Opera", "Internet Explorer, Opera, Linux", "Internet Explorer, Opera, MS – Windows", "Internet Explorer, Firefox, Opera", 6);
        cauhoi c73 = new cauhoi("Trong mạng máy tính, thuật ngữ “Share” có ý nghĩa:", "Chọn lệnh in trong mạng cục bộ", "Nhãn hiệu của một thiết bị kết nối mạng", "Chia sẻ tài nguyên", "Một phần mềm hỗ trợ sử dụng mạng cục bộ", "Chia sẻ tài nguyên", 6);
        cauhoi c74 = new cauhoi("“Internet” có nghĩa là:", "Hệ thống máy tính", "Hệ thống mạng máy tính", "Hệ thống mạng máy tính trong một nước", "Hệ thống mạng máy tính toàn cầu", "Hệ thống mạng máy tính toàn cầu", 6);
        cauhoi c75 = new cauhoi("Website nào sau đây hỗ trợ tìm kiếm thông tin trên mạng:", "http://www.w3schools.com", "http://www.google.com", "http://www.vnexpress.net", "http://www.tvu.edu.vn", "http://www.google.com", 6);
        cauhoi c76 = new cauhoi("Từ viết tắt của World Wide Web là?", "HTTP", "FTP", "WWW", "HTTPS", "WWW", 6);
        cauhoi c77 = new cauhoi("Dưới góc độ địa lí, mạng máy tính được phân biệt thành:", "Mạng cục bộ, mạng diện rộng, mạng toàn cầu", "Mạng cục bộ, mạng diện rộng, mạng toàn cục", "Mạng cục bộ, mạng toàn cục, mạng toàn cầu", "Mạng diện rộng, mạng toàn cầu, mạng toàn cục", "Mạng cục bộ, mạng diện rộng, mạng toàn cầu", 6);
        cauhoi c78 = new cauhoi("Phần mềm nào sau đây là trình duyệt Web?", "Windows 10", "Microsoft Office Outlook", "Linux", "Mozilla Firefox", "Mozilla Firefox", 6);
        cauhoi c79 = new cauhoi("Chương trình nào sau đây có chức năng duyệt web?", "Microsoft Word", "Acrobat Reader", "Mozilla Firefox", "Notepad", "Mozilla Firefox", 6);
        cauhoi c80 = new cauhoi("Trong mạng máy tính, thuật ngữ LAN có ý nghĩa gì?", "Mạng cục bộ", "Mạng diện rộng", "Mạng toàn cầu", "Một ý nghĩa khác", "Mạng cục bộ", 6);

        insertQues(c1);
        insertQues(c2);
        insertQues(c3);
        insertQues(c4);
        insertQues(c5);
        insertQues(c6);
        insertQues(c7);
        insertQues(c8);
        insertQues(c9);
        insertQues(c10);
        insertQues(c11);
        insertQues(c12);
        insertQues(c13);
        insertQues(c14);
        insertQues(c15);
        insertQues(c16);
        insertQues(c17);
        insertQues(c18);
        insertQues(c19);
        insertQues(c20);
        insertQues(c21);
        insertQues(c22);
        insertQues(c23);
        insertQues(c24);
        insertQues(c25);
        insertQues(c26);
        insertQues(c27);
        insertQues(c28);
        insertQues(c29);
        insertQues(c30);
        insertQues(c31);
        insertQues(c32);
        insertQues(c33);
        insertQues(c34);
        insertQues(c35);
        insertQues(c36);
        insertQues(c37);
        insertQues(c38);
        insertQues(c39);
        insertQues(c40);
        insertQues(c41);
        insertQues(c42);
        insertQues(c43);
        insertQues(c44);
        insertQues(c45);
        insertQues(c46);
        insertQues(c47);
        insertQues(c48);
        insertQues(c49);
        insertQues(c50);
        insertQues(c51);
        insertQues(c52);
        insertQues(c53);
        insertQues(c54);
        insertQues(c55);
        insertQues(c56);
        insertQues(c57);
        insertQues(c58);
        insertQues(c59);
        insertQues(c60);
        insertQues(c61);
        insertQues(c62);
        insertQues(c63);
        insertQues(c64);
        insertQues(c65);
        insertQues(c66);
        insertQues(c67);
        insertQues(c68);
        insertQues(c69);
        insertQues(c70);
        insertQues(c71);
        insertQues(c72);
        insertQues(c73);
        insertQues(c74);
        insertQues(c75);
        insertQues(c76);
        insertQues(c77);
        insertQues(c78);
        insertQues(c79);
        insertQues(c80);
    }
    public ArrayList<cauhoi> listQues() {
        ArrayList<cauhoi> dsCauhoi = new ArrayList<>();

        db = getReadableDatabase();

        // Câu truy vấn SQL sử dụng UNION ALL để kết hợp các phân loại câu hỏi
        String query = "SELECT * FROM (SELECT * FROM cauhoi WHERE phanloai = 1 ORDER BY RANDOM() LIMIT 5) AS t1 " +
                "UNION ALL " +
                "SELECT * FROM (SELECT * FROM cauhoi WHERE phanloai = 2 ORDER BY RANDOM() LIMIT 5) AS t2 " +
                "UNION ALL " +
                "SELECT * FROM (SELECT * FROM cauhoi WHERE phanloai = 3 ORDER BY RANDOM() LIMIT 10) AS t3 " +
                "UNION ALL " +
                "SELECT * FROM (SELECT * FROM cauhoi WHERE phanloai = 4 ORDER BY RANDOM() LIMIT 10) AS t4 " +
                "UNION ALL " +
                "SELECT * FROM (SELECT * FROM cauhoi WHERE phanloai = 5 ORDER BY RANDOM() LIMIT 5) AS t5 " +
                "UNION ALL " +
                "SELECT * FROM (SELECT * FROM cauhoi WHERE phanloai = 6 ORDER BY RANDOM() LIMIT 5) AS t6 " +
                "ORDER BY phanloai;";

        // Thực hiện truy vấn
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                cauhoi ques = new cauhoi();

                // Kiểm tra và lấy giá trị từ Cursor
                int colId = c.getColumnIndex(Table.QuestionsTable.id);
                int colNoidung = c.getColumnIndex(Table.QuestionsTable.noidung);
                int colTuychon1 = c.getColumnIndex(Table.QuestionsTable.tuychon1);
                int colTuychon2 = c.getColumnIndex(Table.QuestionsTable.tuychon2);
                int colTuychon3 = c.getColumnIndex(Table.QuestionsTable.tuychon3);
                int colTuychon4 = c.getColumnIndex(Table.QuestionsTable.tuychon4);
                int colDapan = c.getColumnIndex(Table.QuestionsTable.dapan);
                int colPhanloai = c.getColumnIndex(Table.QuestionsTable.phanloai);

                // Chỉ lấy giá trị nếu cột hợp lệ (colIndex >= 0)
                if (colId >= 0) ques.setId(c.getInt(colId));
                if (colNoidung >= 0) ques.setNoidung(c.getString(colNoidung));
                if (colTuychon1 >= 0) ques.setTuychon1(c.getString(colTuychon1));
                if (colTuychon2 >= 0) ques.setTuychon2(c.getString(colTuychon2));
                if (colTuychon3 >= 0) ques.setTuychon3(c.getString(colTuychon3));
                if (colTuychon4 >= 0) ques.setTuychon4(c.getString(colTuychon4));
                if (colDapan >= 0) ques.setDapan(c.getString(colDapan));
                if (colPhanloai >= 0) ques.setPhanloai(c.getInt(colPhanloai));

                // Thêm câu hỏi vào danh sách
                dsCauhoi.add(ques);
            } while (c.moveToNext());
        }

        c.close();
        return dsCauhoi;
    }
    public boolean registerUser(String username, String password) {
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert("nguoidung", null, values);

        return result != -1; // Trả về true nếu đăng ký thành công
    }
    public int loginUser(String username, String password) {
        db = getReadableDatabase();

        // Truy vấn để lấy thông tin người dùng
        String query = "SELECT * FROM nguoidung WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor != null && cursor.moveToFirst()) {
            // Kiểm tra xem cột "user_id" có tồn tại không
            int userIdColumnIndex = cursor.getColumnIndex("user_id");
            if (userIdColumnIndex != -1) {
                // Lấy user_id nếu đăng nhập thành công
                int userId = cursor.getInt(userIdColumnIndex);
                cursor.close();
                return userId;  // Trả về user_id
            } else {
                // Cột "user_id" không tồn tại
                cursor.close();
                return -1;  // Trả về -1 nếu không tìm thấy cột "user_id"
            }
        }

        cursor.close();
        return -1;  // Trả về -1 nếu không tìm thấy người dùng
    }
    public void insertHistory(int userId, int score, String date) {
        db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("diem", score);
        values.put("ngaythuchien", date);

        db.insert("lichsu", null, values);  // Lưu vào bảng lịch sử
    }

    public boolean isUserExists(String username) {
        db = getReadableDatabase();
        String query = "SELECT * FROM nguoidung WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists; // Trả về true nếu tài khoản đã tồn tại
    }

}
