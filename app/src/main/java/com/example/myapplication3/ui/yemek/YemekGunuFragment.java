package com.example.myapplication3.ui.yemek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication3.R;
import com.example.myapplication3.model.Yemek;

public class YemekGunuFragment extends Fragment {

    private static final String ARG_YEMEK = "yemek";

    private Yemek yemek;
    private TextView tarihTextView;
    private TextView ogleTextView;

    public static YemekGunuFragment newInstance(Yemek yemek) {
        YemekGunuFragment fragment = new YemekGunuFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_YEMEK, yemek);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            yemek = (Yemek) getArguments().getSerializable(ARG_YEMEK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yemek_gunu, container, false);
        tarihTextView = view.findViewById(R.id.tarihTextView);
        ogleTextView = view.findViewById(R.id.ogleTextView);

        if (yemek != null) {
            tarihTextView.setText(yemek.getTarih());
            ogleTextView.setText("Öğle Yemeği: " + yemek.getOgleYemegi());
        }
        return view;
    }
}