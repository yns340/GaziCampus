package com.example.myapplication3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DersTakip.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tablonun oluşturulması
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE dersler (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "dersAdi TEXT, " +
                "saat TEXT, " +
                "aciklama TEXT, " +
                "gun TEXT, " +
                "tamamlandi INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TABLE);
    }

    // Veritabanı sürümü güncellenirse tabloyu yeniden oluşturur
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS dersler");
        onCreate(db);
    }

    // Yeni ders ekleme
    public void addDers(Ders ders) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("dersAdi", ders.getDersAdi());
        values.put("saat", ders.getSaat());
        values.put("aciklama", ders.getAciklama());
        values.put("gun", ders.getGun());
        values.put("tamamlandi", ders.isTamamlandi() ? 1 : 0);

        db.insert("dersler", null, values);
        db.close();
    }

    // Tüm dersleri listeleme
    public List<Ders> getAllDersler() {
        List<Ders> dersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM dersler", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String dersAdi = cursor.getString(cursor.getColumnIndexOrThrow("dersAdi"));
                String saat = cursor.getString(cursor.getColumnIndexOrThrow("saat"));
                String aciklama = cursor.getString(cursor.getColumnIndexOrThrow("aciklama"));
                String gun = cursor.getString(cursor.getColumnIndexOrThrow("gun"));
                boolean tamamlandi = cursor.getInt(cursor.getColumnIndexOrThrow("tamamlandi")) == 1;

                Ders ders = new Ders(id, dersAdi, saat, aciklama, gun, tamamlandi);
                dersList.add(ders);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return dersList;
    }

    public List<Ders> getDerslerByGun(String gun) {
        List<Ders> dersList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM dersler WHERE gun = ?", new String[]{gun});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String dersAdi = cursor.getString(cursor.getColumnIndexOrThrow("dersAdi"));
                String saat = cursor.getString(cursor.getColumnIndexOrThrow("saat"));
                String aciklama = cursor.getString(cursor.getColumnIndexOrThrow("aciklama"));
                boolean tamamlandi = cursor.getInt(cursor.getColumnIndexOrThrow("tamamlandi")) == 1;

                Ders ders = new Ders(id, dersAdi, saat, aciklama, gun, tamamlandi);
                dersList.add(ders);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return dersList;
    }
    public void updateDers(Ders ders) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("dersAdi", ders.getDersAdi());
        values.put("saat", ders.getSaat());
        values.put("aciklama", ders.getAciklama());
        values.put("gun", ders.getGun());
        values.put("tamamlandi", ders.isTamamlandi() ? 1 : 0); // boolean için 1 veya 0

        db.update("Dersler", values, "id = ?", new String[]{String.valueOf(ders.getId())});
        db.close();
    }
    public void deleteDers(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("dersler", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}