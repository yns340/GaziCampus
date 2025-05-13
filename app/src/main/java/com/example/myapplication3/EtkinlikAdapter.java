package com.example.myapplication3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EtkinlikAdapter extends RecyclerView.Adapter<EtkinlikAdapter.EtkinlikViewHolder> {

    private final List<GenericEtkinlik> etkinlikListesi;
    private final EtkinlikClickListener listener;



    public EtkinlikAdapter(List<GenericEtkinlik> etkinlikListesi, EtkinlikClickListener listener) {
        this.etkinlikListesi = etkinlikListesi;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EtkinlikViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.etkinlik_item, parent, false);
        return new EtkinlikViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EtkinlikViewHolder holder, int position) {
        GenericEtkinlik etkinlik = etkinlikListesi.get(position);

        holder.etkinlikTitle.setText(etkinlik.getAd());

        try {
            InputStream is = holder.itemView.getContext().getAssets().open(etkinlik.getAfisAdresi());
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            holder.etkinlikImage.setImageBitmap(bitmap);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.detayButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEtkinlikClick(etkinlik.getId());

            }
        });
    }


    @Override
    public int getItemCount() {
        return etkinlikListesi.size();
    }

    static class EtkinlikViewHolder extends RecyclerView.ViewHolder {
        TextView etkinlikTitle;
        ImageView etkinlikImage;
        Button detayButton;

        public EtkinlikViewHolder(@NonNull View itemView) {
            super(itemView);
            etkinlikTitle = itemView.findViewById(R.id.etkinlikTitle);
            etkinlikImage = itemView.findViewById(R.id.etkinlikImage);
            detayButton = itemView.findViewById(R.id.detayButton);
        }
    }
}
