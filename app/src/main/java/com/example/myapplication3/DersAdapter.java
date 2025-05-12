package com.example.myapplication3;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DersAdapter extends RecyclerView.Adapter<DersAdapter.DersViewHolder> {

    private List<Ders> dersList;
    private MyDatabaseHelper dbHelper;

    public DersAdapter(List<Ders> dersList) {
        this.dersList = dersList;
    }

    @NonNull
    @Override
    public DersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_item, parent, false);
        dbHelper = new MyDatabaseHelper(parent.getContext());
        return new DersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DersViewHolder holder, int position) {
        Ders ders = dersList.get(position);
        holder.tvDersAdi.setText(ders.getDersAdi());
        holder.tvSaat.setText(ders.getSaat());
        holder.tvAciklama.setText(ders.getAciklama());
        holder.tvGun.setText(ders.getGun());

        holder.imageViewTick.setVisibility(ders.isTamamlandi() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            boolean yeniDurum = !ders.isTamamlandi();
            ders.setTamamlandi(yeniDurum);
            dbHelper.updateDers(ders);
            notifyItemChanged(holder.getAdapterPosition());
        });

        // Silme işlemi
        holder.imageViewSil.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Dersi Sil")
                    .setMessage("Bu dersi silmek istediğinizden emin misiniz?")
                    .setPositiveButton("Evet", (dialog, which) -> {
                        dbHelper.deleteDers(ders.getId());
                        dersList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    })
                    .setNegativeButton("Hayır", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return dersList.size();
    }

    public void setDersList(List<Ders> dersList) {
        this.dersList = dersList;
        notifyDataSetChanged();
    }

    public static class DersViewHolder extends RecyclerView.ViewHolder {

        TextView tvDersAdi, tvSaat, tvAciklama, tvGun;
        ImageView imageViewTick, imageViewSil;

        public DersViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDersAdi = itemView.findViewById(R.id.tvDersAdi);
            tvSaat = itemView.findViewById(R.id.tvSaat);
            tvAciklama = itemView.findViewById(R.id.tvAciklama);
            tvGun = itemView.findViewById(R.id.tvGun);
            imageViewTick = itemView.findViewById(R.id.imageViewTick);
            imageViewSil = itemView.findViewById(R.id.imageViewSil);
        }
    }
}