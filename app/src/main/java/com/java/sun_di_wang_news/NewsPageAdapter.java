package com.java.sun_di_wang_news;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

// TODO: switch to view pager2
public class NewsPageAdapter extends FragmentStateAdapter {
    ArrayList<String> categories;
    public NewsPageAdapter(FragmentActivity fa, ArrayList<String> categories) {
        super(fa);
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new NewsFragment(position, categories);
    }

    @Override
    public int getItemCount() {
        return categories.size()+1;
    }

    public void remove(int position) {
        System.out.println(categories.get(position-1));
        categories.remove(position-1);
        notifyItemRemoved(position-1);
    }

    public void insert(int position, String insertedCategory) {
        categories.add(position-1, insertedCategory);
        notifyItemInserted(position-1);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setCategories(ArrayList<String> categories){
        this.categories = categories;
    }


}
