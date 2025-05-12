package com.example.myapplication3.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication3.model.Yemek;
import com.example.myapplication3.ui.yemek.YemekGunuFragment;

import java.util.List;

public class YemekPagerAdapter extends FragmentPagerAdapter {

    private List<Yemek> yemekListesi;

    public YemekPagerAdapter(@NonNull FragmentManager fm, List<Yemek> yemekListesi) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.yemekListesi = yemekListesi;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return YemekGunuFragment.newInstance(yemekListesi.get(position));
    }

    @Override
    public int getCount() {
        return yemekListesi.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return yemekListesi.get(position).getTarih();
    }
}