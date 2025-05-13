package com.example.myapplication3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;


public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String TABLE_PLACES = "places";
    public static final String TABLE_FOODS = "foods";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE_PLACES = "CREATE TABLE " + TABLE_PLACES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "rating REAL, " +
                "imagePath TEXT," +
                "location TEXT)";
        db.execSQL(CREATE_TABLE_PLACES);

        String CREATE_TABLE_FOODS = "CREATE TABLE " + TABLE_FOODS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "rating REAL, " +
                "imagePath TEXT," +
                "location TEXT)";
        db.execSQL(CREATE_TABLE_FOODS);

        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // (Yükseltme yapılacaksa eski tabloyu silip yeniden oluştur)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Kullanıcı girişi kontrolü için eklendi.
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    private void insertInitialData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Atakule', 2.5, 'images/atakule.jpeg', 'Çankaya, Çankaya Cd., 06690 Çankaya/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Anıtkabir', 5.0, 'images/anitkabir.jpeg', 'Mebusevleri, Anıttepe, Anıtkabir, 06570 Çankaya/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Ankara Garı', 2.5, 'images/gar.jpeg', 'Doğanbey, Ankara Gar No:1, 06050 Altındağ/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Maltepe Camii', 3.5, 'images/maltepecamii.jpeg', 'Maltepe, Şht. Gönenç Cd. No:3, 06570 Çankaya/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Güvenpark', 3.2, 'images/guvenpark.jpeg', 'Kızılay Meydanı, 06421 Çankaya/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Kocatepe Camii', 2.5, 'images/kocatepe.jpeg', 'Kültür, Dr. Mediha Eldem Sk. No:67, 06420 Çankaya/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Kuğulu Park', 2.5, 'images/kugulu.jpeg', 'Çankaya Kuğulu, Park, 06690 Çankaya/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Tunalı Hilmi Caddesi', 2.5, 'images/tunalı.jpeg', 'Tunalı Hilmi Caddesi')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Saraçoğlu Mahallesi', 2.5, 'images/saracoglu.jpeg', 'Namık Kemal, Kumrular Cd. No:7 D:B, 06650 Çankaya/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Kurtuluş Savaşı Müzesi (I. TBMMM)', 2.5, 'images/I.TBMM.jpeg', 'Hacı Bayram, Cumhuriyet Cd. No: 2/1, 06050 Altındağ/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Cumhuriyet Müzesi (II.TBMM)', 2.5, 'images/II.TBMM.jpeg', 'Doğanbey Mahallesi, Cumhuriyet Cd. No:7, 06050 Altındağ/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Türkiye İş Bankası İktisadi Bağımsızlık Müzesi', 2.5, 'images/isbankasi.jpeg', 'Ulus, Çam Sk. No: 3, 06050 Altındağ/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Ziraat Bankası Müzesi', 2.5, 'images/ziraat.jpeg', 'Hacı Bayram, Atatürk Blv No: 8, 06050 Altındağ/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Etnografya Müzesi', 2.5, 'images/etnografya.jpeg', 'Hacettepe, Etnoğrafya Müzesi, 06230 Altındağ/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Resim Heykel Müzesi', 5.0, 'images/resimheykel.jpeg', 'Hacettepe, 06230 Altındağ/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('CSO Ada Ankara', 2.5, 'images/cso.jpeg', 'Talatpaşa Bulvarı, No: 38 Opera, 06330 Altındağ/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Sıhhiye Abdi İpekçi Parkı', 2.5, 'images/sihhiye.jpeg', 'Sağlık, 06420 Çankaya/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Söğütözü', 2.5, 'images/sogutozu.jpeg', 'Söğütözü')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Eymir Gölü', 2.5, 'images/eymir.jpeg', 'Oran, 06450 Çankaya/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Göksu Parkı', 5.0, 'images/goksu.jpeg', 'Göksu, 06820 Etimesgut/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Melike Hatun Camii', 2.5, 'images/melikehatun.jpeg', 'Hacı Bayram, Atatürk Blv No:19, 06050 Altındağ/Ankara')");
        db.execSQL("INSERT INTO places (title, rating, imagePath, location) VALUES ('Gazi Merkez Kampüs', 5.0, 'images/gazimerkez.jpeg', 'Emniyet, Gazi Ünv., 06560 Yenimahalle/Ankara')");

        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Penguen Kitap/Kafe', 5.0, 'images/penguen.jpeg', 'Gaziosmanpaşa, Arjantin Cd. No: 2, 06680 Çankaya/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Caribou Kafe Gar', 2.5, 'images/kafe.jpg', 'Ankara Tren Gari, Eti, Celal Bayar Blv., 06930 Çankaya/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Meşhur Gülçimen Aspava', 5.0, 'images/restaurant.jpg', 'Mustafa Kemal, Şehit Aybüke Yalçın Caddesi No:23, 06510 Çankaya/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('100 Burger', 5.0, 'images/restaurant.jpg', 'Emek, Veli Necdet Arığ Cd No:36A, 06490 Çankaya/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Colambia Gölbaşı', 5.0, 'images/kafe.jpg', 'Gaziosmanpaşa, Sahil Cd. No:22, 06830 Gölbaşı/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('ODTÜ Vişnelik', 5.0, 'images/restaurant.jpg', 'İşçi Blokları, 1538. Cd. No:58, 06530 Çankaya/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Hacettepe Yeşil Vadi', 5.0, 'images/restaurant.jpg', 'Gaziosmanpaşa, Kırlangıç Sk. No:7, 06700 Çankaya/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Peçenek Döner', 5.0, 'images/restaurant.jpg', 'Mustafa Kemal Mh. VIA GREEN İş Merkezi, 2079. Sk. 2C/8, 06530 Çankaya/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('HMBRGR Tunalı', 5.0, 'images/restaurant.jpg', 'Çankaya Barbaros İran Caddesi No:6 D:No:8A, 06700 Çankaya/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Timboo Cafe Atakule', 5.0, 'images/kafe.jpg', 'Çankaya Mahallesi, Çankaya Caddesi, No: 1B/29, Atakule, 06690 Çankaya/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Taş Bebek Kafe', 5.0, 'images/kafe.jpg', 'Kale, Doyuran Sk. No:3, 06250 Kale/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Kahve Bahane Hamamönü', 5.0, 'images/kafe.jpg', 'C, Hacettepe, Hamamönü Sk. No:106, 06230 Altındağ/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Kırk Fırın Kaşmir Center', 5.0, 'images/kafe.jpg', 'Göksu, 1. TBMM Cd. No:9, 06820 Etimesgut/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('AOÇ Park Center', 5.0, 'images/restaurant.jpg', 'Bahçekapı, 06797 Etimesgut/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('IF Sokak Söğütözü', 5.0, 'images/restaurant.jpg', 'Beştepe, Nergiz Sk. Via Tower İş Merkezi No:7/A Kat:21 D:50, 06560 Yenimahalle/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Caffe Di Toee Söğütözü', 5.0, 'images/kafe.jpg', 'Çukurambar, 1480. Sk. 2B Blok No: 12, 06510 Çankaya/Ankara')");
        db.execSQL("INSERT INTO foods (title, rating, imagePath, location) VALUES ('Unique Burgers', 5.0, 'images/restaurant.jpg', 'Bahçelievler, Prof. Muammer Aksoy Cd 7-A, 06450 Çankaya/Ankara')");

    }

    // TABLE_PLACES'tan verileri çeken metot
    public ArrayList<Place> getPlacesData() {
        ArrayList<Place> placesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_PLACES;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                double rating = cursor.getDouble(cursor.getColumnIndexOrThrow("rating"));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                String tableName = "places";

                Place place = new Place(id, title, rating, imagePath, location, tableName);
                placesList.add(place);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return placesList;
    }

    public ArrayList<Place> getFoodsData() {
        ArrayList<Place> foodsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_FOODS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                double rating = cursor.getDouble(cursor.getColumnIndexOrThrow("rating"));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("imagePath"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                String tableName = "foods";

                Place place = new Place(id, title, rating, imagePath, location, tableName);
                foodsList.add(place);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return foodsList;
    }

    public void UpdateRating(String title, double rating, String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("rating", rating);

        if(tableName.equals("places")) {
            db.update(TABLE_PLACES, values, "title = ?", new String[]{title});
        }
        else if(tableName.equals("foods")) {
            db.update(TABLE_FOODS, values, "title = ?", new String[]{title});
        }
    }

    public double getRating(String title, String tableName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT rating FROM " + tableName + " WHERE title = ?";
        Cursor cursor = db.rawQuery(query, new String[]{title});
        double rating = -1;

        if(cursor.moveToFirst()){
            rating = cursor.getDouble(cursor.getColumnIndexOrThrow("rating"));
        }
        cursor.close();
        db.close();

        return rating;
    }

}