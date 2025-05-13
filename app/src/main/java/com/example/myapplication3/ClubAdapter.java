package com.example.myapplication3;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.KulupViewHolder> {

    private final List<GenericKulup> kulupListesi;
    private final OnKulupClickListener listener;

    public interface OnKulupClickListener {
        void onKulupClick(GenericKulup kulup);
    }

    public ClubAdapter(List<GenericKulup> kulupListesi, OnKulupClickListener listener) {
        this.kulupListesi = kulupListesi;
        this.listener = listener;
    }

    @NonNull
    @Override
    public KulupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.club_item, parent, false);
        return new KulupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KulupViewHolder holder, int position) {
        GenericKulup kulup = kulupListesi.get(position);
        holder.kulupAdiTextView.setText(kulup.getAd());

        holder.itemView.setOnClickListener(v -> listener.onKulupClick(kulup));
    }

    @Override
    public int getItemCount() {
        return kulupListesi.size();
    }

    static class KulupViewHolder extends RecyclerView.ViewHolder {
        TextView kulupAdiTextView;

        public KulupViewHolder(@NonNull View itemView) {
            super(itemView);
            kulupAdiTextView = itemView.findViewById(R.id.clubNameText); // club_item.xml içindeki TextView
        }
    }
}
