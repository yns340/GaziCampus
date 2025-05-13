package com.example.myapplication3;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "kulupEtkinlikDB.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_KULUP = "kulup_table";
    public static final String COLUMN_KULUP_ID = "id";
    public static final String COLUMN_KULUP_ADI = "kulup_adi";
    public static final String COLUMN_LOGO_ADRESI = "logo_adresi";
    public static final String COLUMN_ACIKLAMA = "aciklama";

    public static final String TABLE_ETKINLIK = "etkinlik_table";
    public static final String COLUMN_ETKINLIK_ID = "id";
    public static final String COLUMN_ETKINLIK_ADI = "etkinlik_adi";
    public static final String COLUMN_ETKINLIK_AFISI = "etkinlik_afisi";
    public static final String COLUMN_KULUP_ID_FK = "kulup_id";

    private final Context dbContext;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.dbContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_KULUP_TABLE = "CREATE TABLE " + TABLE_KULUP + " (" +
                COLUMN_KULUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_KULUP_ADI + " TEXT, " +
                COLUMN_LOGO_ADRESI + " TEXT, " +
                COLUMN_ACIKLAMA + " TEXT);";

        String CREATE_ETKINLIK_TABLE = "CREATE TABLE " + TABLE_ETKINLIK + " (" +
                COLUMN_ETKINLIK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ETKINLIK_ADI + " TEXT, " +
                COLUMN_ETKINLIK_AFISI + " TEXT, " +
                COLUMN_KULUP_ID_FK + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_KULUP_ID_FK + ") REFERENCES " +
                TABLE_KULUP + "(" + COLUMN_KULUP_ID + "));";

        db.execSQL(CREATE_KULUP_TABLE);
        db.execSQL(CREATE_ETKINLIK_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ETKINLIK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KULUP);
        onCreate(db);
    }

    public int kulupEkle(String kulupAdi, String logoAdresi, String aciklama) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KULUP_ADI, kulupAdi);
        values.put(COLUMN_LOGO_ADRESI, logoAdresi);
        values.put(COLUMN_ACIKLAMA, aciklama);
        long result = db.insert(TABLE_KULUP, null, values);
        db.close();
        return (int) result;
    }

    public int etkinlikEkle(String etkinlikAdi, String afisAdresi, int kulupId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ETKINLIK_ADI, etkinlikAdi);
        values.put(COLUMN_ETKINLIK_AFISI, afisAdresi);
        values.put(COLUMN_KULUP_ID_FK, kulupId);
        long result = db.insert(TABLE_ETKINLIK, null, values);
        db.close();
        return (int) result;
    }

    public List<GenericKulup> tumKulupleriGetir() {
        List<GenericKulup> kulupListesi = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_KULUP;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KULUP_ID));
                String ad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KULUP_ADI));
                String logo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOGO_ADRESI));
                String aciklama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACIKLAMA));

                // Logo bitmap yükle
                Bitmap logoBitmap = yukleBitmap("images/" + logo + ".png");

                List<GenericEtkinlik> genericEtkinlikler = etkinlikleriGetirByKulupId(id);
                List<Etkinlik> etkinlikler = new ArrayList<>(genericEtkinlikler);

                GenericKulup kulup = new GenericKulup(id, ad, aciklama, logoBitmap, etkinlikler);
                kulupListesi.add(kulup);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return kulupListesi;
    }

    public List<GenericEtkinlik> etkinlikleriGetirByKulupId(int kulupId) {
        List<GenericEtkinlik> etkinlikListesi = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ETKINLIK + " WHERE " + COLUMN_KULUP_ID_FK + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(kulupId)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ETKINLIK_ID));
                String ad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ETKINLIK_ADI));
                String afis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ETKINLIK_AFISI));

                GenericEtkinlik etkinlik = new GenericEtkinlik(id, ad, afis, kulupId);
                etkinlik.yukleAfisBitmap(dbContext);
                etkinlikListesi.add(etkinlik);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return etkinlikListesi;
    }

    public GenericEtkinlik getEtkinlikler(int etkinlikId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ETKINLIK + " WHERE " + COLUMN_ETKINLIK_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(etkinlikId)});
        GenericEtkinlik etkinlik = null;
        if (cursor.moveToFirst()) {
            String ad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ETKINLIK_ADI));
            String afis = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ETKINLIK_AFISI));
            int kulupId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KULUP_ID_FK));
            etkinlik = new GenericEtkinlik(etkinlikId, ad, afis, kulupId);
            etkinlik.yukleAfisBitmap(dbContext);
        }
        cursor.close();
        db.close();
        return etkinlik;
    }

    public boolean kulupVeEtkinlikSil(int kulupId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_ETKINLIK, COLUMN_KULUP_ID_FK + " = ?", new String[]{String.valueOf(kulupId)});
            int result = db.delete(TABLE_KULUP, COLUMN_KULUP_ID + " = ?", new String[]{String.valueOf(kulupId)});
            db.setTransactionSuccessful();
            return result > 0;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean etkinlikSil(int etkinlikId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ETKINLIK, COLUMN_ETKINLIK_ID + " = ?", new String[]{String.valueOf(etkinlikId)});
        db.close();
        return result > 0;
    }

    public GenericKulup getKulupById(int kulupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        GenericKulup kulup = null;
        String query = "SELECT * FROM " + TABLE_KULUP + " WHERE " + COLUMN_KULUP_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(kulupId)});
        if (cursor.moveToFirst()) {
            String ad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KULUP_ADI));
            String logo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOGO_ADRESI));
            String aciklama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACIKLAMA));
            Bitmap logoBitmap = yukleBitmap("images/" + logo + ".png");

            List<GenericEtkinlik> genericEtkinlikler = etkinlikleriGetirByKulupId(kulupId);
            List<Etkinlik> etkinlikler = new ArrayList<>(genericEtkinlikler);

            kulup = new GenericKulup(kulupId, ad, aciklama, logoBitmap, etkinlikler);
        }
        cursor.close();
        db.close();
        return kulup;
    }

    public GenericKulup getKulupByAdi(String kulupAdi) {
        SQLiteDatabase db = this.getReadableDatabase();
        GenericKulup kulup = null;
        String query = "SELECT * FROM " + TABLE_KULUP + " WHERE " + COLUMN_KULUP_ADI + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{kulupAdi});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KULUP_ID));
            String logo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOGO_ADRESI));
            String aciklama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACIKLAMA));
            Bitmap logoBitmap = yukleBitmap("images/" + logo + ".png");

            List<GenericEtkinlik> genericEtkinlikler = etkinlikleriGetirByKulupId(id);
            List<Etkinlik> etkinlikler = new ArrayList<>(genericEtkinlikler);

            kulup = new GenericKulup(id, kulupAdi, aciklama, logoBitmap, etkinlikler);
        }
        cursor.close();
        db.close();
        return kulup;
    }

    public Bitmap yukleBitmap(String assetYolu) {
        AssetManager assetManager = dbContext.getAssets();
        try (InputStream is = assetManager.open(assetYolu)) {
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
