package com.java.sun_di_wang_news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FavoriteActivity extends AppCompatActivity {

    boolean fragmentExists = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.favorite);
        createFavoriteList();


    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    // create menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                if(fragmentExists) {
                    fragmentExists = false;
                    onBackPressed();
                }
                return true;
            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }
    }

    public void createFavoriteList() {
        Type type = new TypeToken<List<NewsObject>>() {
        }.getType();

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        Set<String> set = appSharedPrefs.getStringSet("favorite", null);
        String historyJson = appSharedPrefs.getString("history", "");
        List<NewsObject> allNewsObjectList = gson.fromJson(historyJson, type);
        ArrayList<NewsObject> favoriteList;

        if (allNewsObjectList != null) {
            favoriteList = new ArrayList<>();
            for (NewsObject a : allNewsObjectList) {
                for (String tempID : set) {
                    if (a.newsID.equals(tempID)) {
                        favoriteList.add(a);
                    }
                }
            }

            NewsFragment newsFragment = new NewsFragment(favoriteList);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.favorite_fragment_container_view, newsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            fragmentExists = true;
        }
    }
}
