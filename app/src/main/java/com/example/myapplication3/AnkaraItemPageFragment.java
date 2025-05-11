package com.example.myapplication3;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.RatingBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.IOException;
import java.io.InputStream;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AnkaraItemPageFragment extends Fragment {

    public AnkaraItemPageFragment() {
        // Boş yapıcı metod
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ankara_item_page, container, false);

        // Bundle'dan Place nesnesini al
        if (getArguments() != null) {
            Place place = (Place) getArguments().getSerializable("place");

            // Verileri kullan (örneğin, başlık ve puanı göstermek)
            TextView headTitleTextView = view.findViewById(R.id.headTextView);
            headTitleTextView.setText(place.getTitle());

            RatingBar ratingBar = view.findViewById(R.id.ratingBar);
            ratingBar.setRating((float) place.getRating());

            TextView ratingText = view.findViewById(R.id.ratingTextView);
            ratingText.setText(String.valueOf(place.getRating()));

            ImageView imageView = view.findViewById(R.id.imageView);
            if (place.getImagePath() != null) {
                try {
                    // assets/image/ dosyasındaki resim dosyasını yükle
                    InputStream inputStream = requireContext().getAssets().open(place.getImagePath());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    imageView.setImageResource(R.drawable.place_holder_image);
                }
            }

            TextView locationText = view.findViewById(R.id.locationTextView);
            locationText.setText(place.getLocation());

        }

        // ImageView'a tıklama dinleyici ekle
        ImageView konumImageView = view.findViewById(R.id.konum);

        konumImageView.setOnClickListener(v -> {
            if (getArguments() != null) {
                Place place = (Place) getArguments().getSerializable("place");
                openLocationInMaps(place.getLocation());
            }
            else
                Toast.makeText(requireContext(), "Konum yok", Toast.LENGTH_SHORT).show();

        });

        ImageButton homeButton = view.findViewById(R.id.HomeButton); // HomeButton

        homeButton.setOnClickListener(v -> {
            Fragment newFragment = new DuyuruFragment(); // DuyuruFragment'ını yükle
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, newFragment) // fragmentContainer ID'si
                    .addToBackStack(null) // Geri gelmek için stack'e ekle
                    .commit();

            BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.nav_anasayfa);
        });

        Button buttonRating = view.findViewById(R.id.buttonRating);

        buttonRating.setOnClickListener(v -> {
            Fragment newFragment = new AnkaraRatingFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, newFragment) // add kullanarak üzerine ekleme
                    .addToBackStack(null)
                    .commit();
        });


        return view;
    }

    // Konumu Google Maps'te açma
    private void openLocationInMaps(String address) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);  // Adresi URI formatında belirtiyoruz
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        // Google Maps yüklü mü kontrol et
        if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(requireContext(), "Google Maps uygulaması yüklü değil", Toast.LENGTH_SHORT).show();
        }
    }
}
