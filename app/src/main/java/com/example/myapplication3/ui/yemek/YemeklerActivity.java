package com.example.myapplication3.ui.yemek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication3.R;
import com.example.myapplication3.adapter.YemekPagerAdapter;
import com.example.myapplication3.model.Yemek;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class YemeklerActivity extends AppCompatActivity {

    private static final String TAG = "YemeklerActivity";
    private static final String PREFS_NAME = "MyPrefs";
    private static final String ANONIM_USER_ID_KEY = "anonimUserId";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ProgressBar progressBar;
    private YemekPagerAdapter pagerAdapter;
    private List<Yemek> yemekListesi;
    private RatingBar ratingBar;
    private TextView averageRatingTextView;
    private EditText yorumEditText;
    private Button yorumGonderButton;
    private RecyclerView yorumlarRecyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference yorumlarReference;
    private DatabaseReference puanlarReference;
    private int currentYemekPosition = 0;
    private boolean kullaniciPuanVerdi = false;
    private String yemekTarih;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private YorumlarAdapter yorumlarAdapter;
    private float kullanicPuani = 0f;
    private Map<String, Float> kullaniciPuanlari = new HashMap<>(); // Kullanıcının verdiği puanları saklamak için, yemekTarih'ine göre saklanacak
    private String anonimUserId; // Cihaza özel anonim kullanıcı ID'si

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: YemeklerActivity oluşturuldu.");
        setContentView(R.layout.activity_yemekler);

        // UI öğelerini başlat
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        progressBar = findViewById(R.id.progressBar);
        ratingBar = findViewById(R.id.ratingBar);
        averageRatingTextView = findViewById(R.id.averageRatingTextView);
        yorumEditText = findViewById(R.id.commentEditText);
        yorumGonderButton = findViewById(R.id.addCommentButton);
        yorumlarRecyclerView = findViewById(R.id.commentsRecyclerView);

        // Firebase veritabanını başlat
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        puanlarReference = firebaseDatabase.getReference("puanlar");

        // Cihaza özel anonim kullanıcı ID'sini al veya oluştur
        anonimUserId = getAnonimUserId();

        // Manuel olarak yemek listesini oluştur
        yemekListesi = new ArrayList<>();
        yemekListesi.add(new Yemek("05 Mayıs 2025", "Ezogelin Çorbası, Şehriyeli Güveç, Etsiz Güveç, Aysberg Salata, Mozaik Pasta"));
        yemekListesi.add(new Yemek("06 Mayıs 2025", "Köylü Çorbası, Etli Taze Fasülye, Etsiz Taze Fasülye, Pirinç Pilavı, Cacık"));
        yemekListesi.add(new Yemek("07 Mayıs 2025", "Yayla Çorbası, Mantar Soslu Sahan Köfte, Mantar Kavurma, Soslu Makarna, Meyve"));
        yemekListesi.add(new Yemek("08 Mayıs 2025", "Mercimek Çorbası, Antep Usulü Patates, Etsiz Antep Usulü Patates, Bulgur Pilavı, Salata"));
        yemekListesi.add(new Yemek("09 Mayıs 2025", "Tarhana Çorbası, Tavuk Sote, Peynirli Makarna, Barbunya Pilaki, Sütlü İrmik Tatlısı"));
        yemekListesi.add(new Yemek("12 Mayıs 2025", "Peskütan Çorbası, Tas Kebabi, Etsiz Patates, Pirinç Pilavı, Meyve"));
        yemekListesi.add(new Yemek("13 Mayıs 2025", "Düğün Çorbası, Etli Nohut, Etsiz Nohut, Bulgur Pilavı, Turşu"));
        yemekListesi.add(new Yemek("14 Mayıs 2025", "Ezogelin Çorbası, Köri Soslu Kabak, Etsiz Kabak, Şehriyeli Pirinç Pilavı, Tiramisu"));
        yemekListesi.add(new Yemek("15 Mayıs 2025", "Mercimek Çorbası, Çiftlik Köfte, Etsiz Kuru Fasülye, Soslu Makarna, Salata"));
        yemekListesi.add(new Yemek("16 Mayıs 2025", "Domates Çorbası, Özbek Pilavı, Etsiz Bamya, Mercimek Piyazı, Yoğurt"));
        yemekListesi.add(new Yemek("19 Mayıs 2025", "Resmi Tatil"));
        yemekListesi.add(new Yemek("20 Mayıs 2025", "Mercimek Çorbası, Etli Bezelye, Etsiz Bezelye, Cevizli Erişte, Yoğurt"));
        yemekListesi.add(new Yemek("21 Mayıs 2025", "Ezogelin Çorbası, Çökertme Kebabı, Etsiz Ispanak, Salata, Meyve"));
        yemekListesi.add(new Yemek("22 Mayıs 2025", "Hanımağa Çorbası, Fırın Tavuk Baget / Sebze Garnitür, Mantar Sote, Pirinç Pilavı, Mozaik Pasta"));
        yemekListesi.add(new Yemek("23 Mayıs 2025", "Şehriye Çorbası, Beğendili Köfte, Etsiz Patlıcan, Bulgur Pilavı, Cacık"));
        yemekListesi.add(new Yemek("26 Mayıs 2025", "Mercimek Çorbası, Et Sote, Etsiz Kabak, Su Böreği, Ayran"));
        yemekListesi.add(new Yemek("27 Mayıs 2025", "Mantar Çorbası, Köri Soslu Tavuk, Barbunya Pilaki, Mercimekli Bulgur Pilavı, Salata"));
        yemekListesi.add(new Yemek("28 Mayıs 2025", "Domates Çorbası, Hasan Paşa Köfte, Etsiz Patates, Gökçesu Pilavı, Yoğurtlu Pancar"));
        yemekListesi.add(new Yemek("29 Mayıs 2025", "Alaca Çorbası, Şehriyeli Güveç, Şehriyeli Pilav, Domates Soslu Şakşuka, Triliçe"));
        yemekListesi.add(new Yemek("30 Mayıs 2025", "Ezogelin Çorbası, Etli Biber Dolma, Etsiz Biber Dolma, Peynirli Makarna, Meyve"));

        // ViewPager'ı ve TabLayout'u kur
        setupViewPager();
        progressBar.setVisibility(View.GONE);

        // Yorumlar RecyclerView'ini ayarla
        yorumlarRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        yorumlarAdapter = new YorumlarAdapter(this, new ArrayList<>());
        yorumlarRecyclerView.setAdapter(yorumlarAdapter);

        // İlk yüklemede ortalama puanı göster ve yorumları yükle
        yemekTarih = yemekListesi.get(0).getTarih();
        displayAverageRating(yemekTarih);
        loadYorumlar(yemekTarih);
        //loadKullaniciPuani(yemekTarih); // Kullanıcının puanını yükle  -- taşındı

        // ViewPager sayfa değiştirme listener'ı
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Bu metodun içinde bir şey yapmamıza gerek yok.
            }

            @Override
            public void onPageSelected(int position) {
                // Sayfa değiştiğinde ortalama puanı ve yorumları güncelle
                currentYemekPosition = position;
                yemekTarih = yemekListesi.get(position).getTarih();
                displayAverageRating(yemekTarih);
                loadYorumlar(yemekTarih);
                loadKullaniciPuani(yemekTarih); // Kullanıcının puanını yükle
                kullaniciPuanVerdi = false;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Bu metodun içinde bir şey yapmamıza gerek yok.
            }
        });

        // RatingBar listener'ı ekle
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    // Kullanıcı tarafından bir değişiklik yapıldıysa
                    yemekTarih = yemekListesi.get(currentYemekPosition).getTarih();
                    LocalDate yemekTarihi = LocalDate.parse(yemekTarih, DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("tr", "TR")));
                    LocalDate bugun = LocalDate.now();

                    if (yemekTarihi.isBefore(bugun) || yemekTarihi.isEqual(bugun)) {
                        // Geçmiş veya bugünse puanı kaydet veya güncelle
                        saveRating(yemekTarih, rating);
                        displayAverageRating(yemekTarih);
                        kullaniciPuanVerdi = true;
                        kullanicPuani = rating; // Kullanıcının verdiği puanı sakla
                        kullaniciPuanlari.put(yemekTarih, rating); // Kullanıcının puanını sakla
                    } else {
                        // Gelecek bir tarihse hata mesajı
                        Toast.makeText(YemeklerActivity.this, "Gelecek yemekler için puanlama yapılamaz.", Toast.LENGTH_SHORT).show();
                        ratingBar.setRating(kullaniciPuanVerdi ? kullanicPuani :  kullaniciPuanlari.getOrDefault(yemekTarih, 0f)); // Önceki puanı koru
                    }
                }
            }
        });

        // Yorum gönderme listener'ı
        yorumGonderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yorum = yorumEditText.getText().toString().trim();
                if (!yorum.isEmpty()) {
                    yemekTarih = yemekListesi.get(currentYemekPosition).getTarih();
                    yorumGonder(yemekTarih, yorum);
                    yorumEditText.getText().clear();
                } else {
                    Toast.makeText(YemeklerActivity.this, "Yorumunuzu girin.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        pagerAdapter = new YemekPagerAdapter(fragmentManager, yemekListesi);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Log.d(TAG, "setupViewPager: ViewPager ve TabLayout ayarlandı.");
    }

    private void saveRating(String yemekTarih, float rating) {
        puanlarReference = firebaseDatabase.getReference("puanlar");
        String userId = (currentUser != null) ? currentUser.getUid() : anonimUserId; // Anonim kullanıcı ID'sini kullan
        puanlarReference.child(yemekTarih).child(userId).setValue(rating);
        Log.d(TAG, "saveRating: Puan kaydedildi. Tarih: " + yemekTarih + ", Puan: " + rating + ", Kullanıcı: " + userId);
    }

    private void displayAverageRating(String yemekTarih) {
        puanlarReference = firebaseDatabase.getReference("puanlar");
        puanlarReference.child(yemekTarih).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float total = 0;
                int count = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // Her kullanıcı için olan puanı al
                    Float puanObj = snapshot.getValue(Float.class);
                    if (puanObj != null) {
                        total += puanObj;
                        count++;
                    }
                }
                float average = count > 0 ? total / count : 0;
                averageRatingTextView.setText("Ortalama Puan: " + String.format("%.1f", average));
                Log.d(TAG, "displayAverageRating: Ortalama Puan: " + average + ", Puan Sayısı: " + count + ", Tarih: " + yemekTarih);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "displayAverageRating: Veritabanı hatası: " + databaseError.getMessage());
                Toast.makeText(YemeklerActivity.this, "Puanlar alınamadı: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                averageRatingTextView.setText("Ortalama Puan: 0");
            }
        });
    }

    private void yorumGonder(String yemekTarih, String yorum) {
        yorumlarReference = firebaseDatabase.getReference("yorumlar");
        String userId = (currentUser != null) ? currentUser.getUid() : anonimUserId;  // Anonim kullanıcı ID'sini kullan
        Yorum yeniYorum = new Yorum(userId, yorum);
        yorumlarReference.child(yemekTarih).push().setValue(yeniYorum);
        loadYorumlar(yemekTarih);
    }

    private void loadYorumlar(String yemekTarih) {
        yorumlarReference = firebaseDatabase.getReference("yorumlar").child(yemekTarih);
        yorumlarReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Yorum> yorumlar = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Yorum yorumObj = snapshot.getValue(Yorum.class);
                    if (yorumObj != null) {
                        yorumlar.add(yorumObj);
                    }
                }
                yorumlarAdapter.setYorumlar(yorumlar);
                yorumlarAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "loadYorumlar: Yorumlar alınamadı: " + databaseError.getMessage());
                Toast.makeText(YemeklerActivity.this, "Yorumlar alınamadı: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadKullaniciPuani(String yemekTarih) {
        String userId = (currentUser != null) ? currentUser.getUid() : anonimUserId; // Anonim kullanıcı ID'sini kullan
        puanlarReference.child(yemekTarih).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float puanObj = dataSnapshot.getValue(Float.class);
                if (puanObj != null) {
                    kullanicPuani = puanObj;
                    kullaniciPuanlari.put(yemekTarih, kullanicPuani);
                    ratingBar.setRating(kullanicPuani);
                } else {
                    kullanicPuani = 0f;
                    kullaniciPuanlari.put(yemekTarih, 0f);
                    ratingBar.setRating(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "loadKullaniciPuani: Kullanıcı puanı alınamadı: " + databaseError.getMessage());
                Toast.makeText(YemeklerActivity.this, "Kullanıcı puanı alınamadı: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                ratingBar.setRating(0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private String getAnonimUserId() {
        android.content.SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String id = prefs.getString(ANONIM_USER_ID_KEY, null);
        if (id == null) {
            id = "anonim_" + generateRandomNumber();
            prefs.edit().putString(ANONIM_USER_ID_KEY, id).apply();
        }
        return id;
    }

    private String generateRandomNumber() {
        Random random = new Random();
        return String.valueOf(random.nextInt(1000000)); // Örnek olarak 6 haneli bir sayı
    }

    public static class Yorum {
        private String kullaniciId;
        private String yorum;

        public Yorum() {
            // Boş yapıcı metot Firebase için gerekli
        }

        public Yorum(String kullaniciId, String yorum) {
            this.kullaniciId = kullaniciId;
            this.yorum = yorum;
        }

        public String getKullaniciId() {
            return kullaniciId;
        }

        public String getYorum() {
            return yorum;
        }
    }

    // Yorumları göstermek için RecyclerView Adapter'ı
    private class YorumlarAdapter extends RecyclerView.Adapter<YorumlarAdapter.YorumViewHolder> {

        private List<Yorum> yorumlar;
        private android.content.Context context;

        public YorumlarAdapter(android.content.Context context, List<Yorum> yorumlar) {
            this.context = context;
            this.yorumlar = yorumlar;
        }

        public void setYorumlar(List<Yorum> yeniYorumlar) {
            this.yorumlar = yeniYorumlar;
        }

        @NonNull
        @Override
        public YorumViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            // Yorum öğesi için layout'u oluştur
            View view = android.view.LayoutInflater.from(parent.getContext()).inflate(R.layout.yorum_item, parent, false);
            return new YorumViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull YorumViewHolder holder, int position) {
            // Yorum verilerini ViewHolder'a bağla
            Yorum yorum = yorumlar.get(position);
            if (yorum.getKullaniciId().startsWith("anonim_")) {
                holder.kullaniciAdiTextView.setText("Anonim: ");
            } else {
                holder.kullaniciAdiTextView.setText("Kullanıcı: "); // veya farklı bir şey
            }
            holder.yorumTextView.setText(yorum.getYorum());
        }

        @Override
        public int getItemCount() {
            return yorumlar.size();
        }

        public class YorumViewHolder extends RecyclerView.ViewHolder {
            TextView kullaniciAdiTextView;
            TextView yorumTextView;

            public YorumViewHolder(@NonNull View itemView) {
                super(itemView);
                kullaniciAdiTextView = itemView.findViewById(R.id.kullaniciAdiTextView);
                yorumTextView = itemView.findViewById(R.id.yorumTextView);
            }
        }
    }
}

