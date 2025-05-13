package com.example.myapplication3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AnkaraRatingFragment extends Fragment {

    public AnkaraRatingFragment() {
        // Boş yapıcı metod
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ankara_rating, container, false);

        TextView ratingTextView = view.findViewById(R.id.ratingTextView);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        Button tamamlandiButton = view.findViewById(R.id.tamamlandiButton);

        ratingBar.setOnTouchListener((v, event) -> {
            float rawX = event.getX();
            float width = ratingBar.getWidth();
            float rating = (rawX / width) * ratingBar.getNumStars();
            rating = Math.max(0, Math.min(rating, ratingBar.getNumStars()));  // 0 ile max arasında tutmak için
            ratingBar.setRating(rating);
            ratingTextView.setText(String.format("%.1f", rating));
            return true;  // Olayı burada yakaladığımızı belirtmek için
        });


        tamamlandiButton.setOnClickListener(v -> {
            float ratingValue = ratingBar.getRating();
            Database db = new Database(requireContext());
            Place place = (Place) getArguments().getSerializable("place");

            if(place.getTableName() == "places"){
                db.UpdateRating(place.getTitle(), ratingValue, "places");
            }

            else if(place.getTableName() == "foods"){
                db.UpdateRating(place.getTitle(), ratingValue, "foods");
            }

            Toast.makeText(requireContext(), "Puanınız: " + ratingValue, Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
