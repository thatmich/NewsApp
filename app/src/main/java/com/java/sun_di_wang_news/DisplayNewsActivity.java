package com.java.sun_di_wang_news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DisplayNewsActivity extends AppCompatActivity {
    String id;
    boolean isMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_news);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        id = intent.getStringExtra(MainActivity.NEWS_ID);
        String title = intent.getStringExtra(MainActivity.NEWS_TITLE);
        String publisher = intent.getStringExtra(MainActivity.NEWS_AUTHOR);
        String publishTime = intent.getStringExtra(MainActivity.NEWS_TIME);
        String content = intent.getStringExtra(MainActivity.NEWS_CONTENT);
        String imageLink = intent.getStringExtra(MainActivity.NEWS_IMAGE_URL);
        String videoLink = intent.getStringExtra(MainActivity.NEWS_VIDEO_URL);
        isMain = intent.getExtras().getBoolean(MainActivity.NEWS_ORIGIN);

        TextView titleView = findViewById(R.id.title_inside);
        TextView authorView = findViewById(R.id.author_inside);
        TextView dateView = findViewById(R.id.date_inside);
        TextView contentView = findViewById(R.id.content_inside);
        ImageView imageView = findViewById(R.id.image_inside);
        VideoView videoView = findViewById(R.id.video_inside);

        titleView.setText(title);
        authorView.setText(publisher);
        dateView.setText(publishTime);
        contentView.setText(content);
        try {
            if (!imageLink.isEmpty()) {
                Picasso.get().load(imageLink).into(imageView);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        if (!videoLink.isEmpty()) {
            try {
                videoView.setVisibility(View.VISIBLE);
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                mediaController.setMediaPlayer(videoView);
                Uri video = Uri.parse(videoLink);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(video);
            } catch (Exception e) {
                e.printStackTrace();
            }
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                    float screenRatio = videoView.getWidth() / (float)
                            videoView.getHeight();
                    float scaleX = videoRatio / screenRatio;
                    if (scaleX >= 1f) {
                        videoView.setScaleX(scaleX);
                    } else {
                        videoView.setScaleY(1f / scaleX);
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuButtonID = item.getItemId();
        if (menuButtonID == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (menuButtonID == R.id.action_set_favorite) {
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

            try {
                // Retrieve values
                Set<String> set = appSharedPrefs.getStringSet("favorite", null);
                // Set the values
                Set<String> newSet = new HashSet<String>();
                if (set != null) {
                    newSet.addAll(set);
                }
                if (newSet.contains(id)) {
                    // remove
                    newSet.remove(id);

                    item.setIcon(R.drawable.favorite_outline);

                } else {
                    newSet.add(id);
                    item.setIcon(R.drawable.favorite);
                }

                System.out.println(newSet.toString());

                prefsEditor.putStringSet("favorite", newSet);
                prefsEditor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }


            // else if already is favorite (unfavorite)
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // create menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.menu_display, menu);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        MenuItem item = menu.findItem(R.id.action_set_favorite);
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());

        try {
            // Retrieve values
            Set<String> set = appSharedPrefs.getStringSet("favorite", null);
            // Set the values
            Set<String> newSet = new HashSet<String>();
            if (set != null) {
                newSet.addAll(set);
            }
            if (newSet.contains(id)) {
                item.setIcon(R.drawable.favorite);

            } else {
                item.setIcon(R.drawable.favorite_outline);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }
}