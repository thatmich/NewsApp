package com.java.sun_di_wang_news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {
    ArrayList<NewsObject> newsObjectList;
    SharedPreferences appSharedPrefs;

    boolean fragmentExists = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.history);

        Type type = new TypeToken<ArrayList<NewsObject>>() {
        }.getType();

        appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        String json = appSharedPrefs.getString("history", "");
        newsObjectList = new ArrayList<>();
        try {
            if(gson.fromJson(json, type) != null) {
                ArrayList<NewsObject> temp = new ArrayList<>();
                temp = gson.fromJson(json, type);
                newsObjectList.addAll(temp);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if(newsObjectList != null){
            NewsFragment newsFragment = new NewsFragment(newsObjectList); // is it news fragment???
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.history_fragment_container_view, newsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            fragmentExists = true;
        }

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
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println(item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                if(fragmentExists) {
                    fragmentExists = false;
                    onBackPressed();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
