package com.example.myclinicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Clinic.db";

    public DatabaseHelper(Context context) {
        super(context, "Clinic.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table users(username TEXT primary key, password TEXT)");
        // Tabel dengan kolom time
        db.execSQL("create Table bookings(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, doctor TEXT, gender TEXT, date TEXT, time TEXT)");
        db.execSQL("insert into users(username, password) values('admin', '12345')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS bookings");
        onCreate(db);
    }

    // 1. REGISTER
    public Boolean insertUser(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = db.insert("users", null, contentValues);
        return result != -1;
    }

    // 2. LOGIN
    public Boolean checkLogin(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where username = ? and password = ?", new String[] {username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // 3. CREATE BOOKING
    public Boolean insertBooking(String name, String doctor, String gender, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("doctor", doctor);
        contentValues.put("gender", gender);
        contentValues.put("date", date);
        contentValues.put("time", time);
        long result = db.insert("bookings", null, contentValues);
        return result != -1;
    }

    // 4. READ BOOKING
    public Cursor getBookings(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from bookings order by id desc", null);
        return cursor;
    }

    // 5. DELETE BOOKING
    public boolean deleteBooking(String name, String doctor, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("bookings", "name=? and doctor=? and date=?", new String[]{name, doctor, date});
        return result != -1;
    }

    // 6. UPDATE PASSWORD
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);
        long result = db.update("users", contentValues, "username = ?", new String[]{username});
        return result != -1;
    }

    // --- FUNGSI BARU YANG DIBUTUHKAN UNTUK MEMPERBAIKI ERROR ---
    public boolean updateBookingDateTime(String name, String doctor, String oldDate, String newDate, String newTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", newDate);
        contentValues.put("time", newTime); // Update juga jam-nya

        // Update data berdasarkan nama, dokter, dan tanggal lama
        long result = db.update("bookings", contentValues, "name=? and doctor=? and date=?", new String[]{name, doctor, oldDate});
        return result != -1;
    }

    // ... kode fungsi lainnya ...

    // 8. CEK KETERSEDIAAN JADWAL (Mencegah Double Booking)
    public Boolean checkAppointmentExists(String doctor, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Query: Cari data booking di mana dokter, tanggal, DAN jam-nya sama
        Cursor cursor = db.rawQuery("Select * from bookings where doctor = ? and date = ? and time = ?", new String[]{doctor, date, time});
        boolean exists = cursor.getCount() > 0; // Jika > 0, berarti jadwal sudah ada
        cursor.close();
        return exists;
    }
}
