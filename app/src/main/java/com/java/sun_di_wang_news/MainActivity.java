package com.java.sun_di_wang_news;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private NewsPageAdapter adapter;

    public static final String NEWS_TITLE = "com.example.app.NEWS_TITLE";
    public static final String NEWS_TIME = "com.example.app.NEWS_TIME";
    public static final String NEWS_AUTHOR = "com.example.app.NEWS_AUTHOR";
    public static final String NEWS_CONTENT = "com.example.app.NEWS_CONTENT";
    public static final String NEWS_IMAGE_URL = "com.example.app.NEWS_IMAGE_URL";
    public static final String NEWS_VIDEO_URL = "com.example.app.NEWS_VIDEO_URL";
    public static final String NEWS_ID = "com.example.app.NEWS_ID";
    public static final String NEWS_ORIGIN = "com.example.app.NEWS_ORIGIN";

    ArrayList<String> categories;
    ArrayList<String> adjustedCategories;
    boolean[] isChecked;
    AlertDialog.Builder builder;
    String[] allCategories;

    private CharSequence getTabText(int position) {
        if(position >= 1){
            return adjustedCategories.get(position-1);
        }
        else {
            return "推荐";
        }
    }

    private void addTab(int position) {
        try {
            TabLayout.Tab t = tabLayout.getTabAt(position);
            if(t != null && position != 0) {

                View view = t.getCustomView();
                view.animate().alpha(1f).setDuration(200);

                int currentWidth = view.getWidth();
                int newWidth = 100;
                ValueAnimator widthAnimator = ValueAnimator.ofInt(currentWidth, newWidth).setDuration(500);

                widthAnimator.addUpdateListener(animation2 -> {
                    Integer value = (Integer) animation2.getAnimatedValue();
                    view.getLayoutParams().width = value.intValue();
                    view.requestLayout();
                });

                AnimatorSet animationSet = new AnimatorSet();
                animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
                animationSet.play(widthAnimator);
                animationSet.start();


            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }


    private void deleteTab(int position) {
        try {
            position++;
            TabLayout.Tab t = tabLayout.getTabAt(position);
            if(t != null && position != 0) {
                System.out.println("Animation: delete");
                View view = t.getCustomView();
                TextView tab_name = (TextView) view.findViewById(R.id.tabTitle);
                tab_name.animate().alpha(0f).setDuration(200);
                tab_name.setText("");

                int currentWidth = view.getWidth();
                int newWidth = 0;
                ValueAnimator widthAnimator = ValueAnimator.ofInt(currentWidth, newWidth).setDuration(500);

                widthAnimator.addUpdateListener(animation1 -> {
                    Integer value = (Integer) animation1.getAnimatedValue();
                    view.getLayoutParams().width = value.intValue();
                    view.requestLayout();
                });

                AnimatorSet animationSet = new AnimatorSet();
                animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
                animationSet.play(widthAnimator);
                animationSet.start();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        allCategories = getResources().getStringArray(R.array.category_array);
        builder = new AlertDialog.Builder(MainActivity.this);
        categories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.category_array)));
        categories.remove(0);
        adjustedCategories = categories;
        isChecked = new boolean[(categories.size())];
        Arrays.fill(isChecked, true);

        try{
            super.onCreate(savedInstanceState);
            if(savedInstanceState == null){
                setContentView(R.layout.activity_main);

                viewPager = findViewById(R.id.viewPager);
                tabLayout = findViewById(R.id.tablayout);
                adapter = new NewsPageAdapter(MainActivity.this, categories);
                viewPager.setAdapter(adapter);

                new TabLayoutMediator(tabLayout, viewPager,
                        (tab, position) -> tab.setText(getTabText(position))
                ).attach();
                setupTabView(0);
                tabLayout.addOnTabSelectedListener(onTabSelectedListener);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // prepare action bar
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    // create menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            Intent favIntent = new Intent(this, FavoriteActivity.class);
            this.startActivity(favIntent);
        }
        else if (id == R.id.action_history) {
            Intent historyIntent = new Intent(this, HistoryActivity.class);
            this.startActivity(historyIntent);
        }
        else if (id == R.id.action_search) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            this.startActivity(searchIntent);
        }
        else if (id == R.id.action_set_categories) {
            searchCategoryHandler(item.getActionView());

        }
        return super.onOptionsItemSelected(item);
    }

    public void setupTabView(int selected){
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(R.layout.custom_tab);
            TextView tab_name = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tabTitle);
            tab_name.setText("" + getTabText(i));
        }
        setSelectedTab(selected);
    }

    public void setupTabViewWithInvisibles(int selected, ArrayList<Integer> invisiblePositions){
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(R.layout.custom_tab);
            TextView tab_name = (TextView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tabTitle);
            tab_name.setText("" + getTabText(i));
        }
        setSelectedTab(selected);
        for(int invisibleTab : invisiblePositions){
            setupInvisibleTab(invisibleTab);
        }
    }

    public void setupInvisibleTab(int position){
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        if(tab != null){
            View v = tab.getCustomView();
            if(v != null){
                TextView tab_name = (TextView) v.findViewById(R.id.tabTitle);
                tab_name.setTextColor(getResources().getColor(R.color.purple_700));
                v.setBackgroundResource(R.drawable.custom_tab_layout_bg);
                v.getLayoutParams().width = 0;
                v.setAlpha(0f);
                addTab(position);
            }
        }

    }

    public void setSelectedTab(int position){
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        if(tab != null){
            View v = tab.getCustomView();
            if(v != null){
                TextView tab_name = (TextView) v.findViewById(R.id.tabTitle);
                tab_name.setTextColor(getResources().getColor(R.color.white));
                v.setBackgroundResource(R.drawable.custom_selected_tab_bg);
            }
        }

    }

    public void setUnselectedTab(int position){
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        if(tab != null){
            View v = tab.getCustomView();
            if(v != null) {
                TextView tab_name = (TextView) v.findViewById(R.id.tabTitle);
                tab_name.setTextColor(getResources().getColor(R.color.purple_700));
                v.setBackgroundResource(R.drawable.custom_tab_layout_bg);
            }
        }
    }

    private final TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener(){
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int c = tab.getPosition();
            System.out.println("Selected " + c);
            setSelectedTab(c);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            int c = tab.getPosition();
            System.out.println("Unselected " + c);
            setUnselectedTab(c);
        }


        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    public void searchCategoryHandler(View view) {
        String[] categoriesWithoutAll = Arrays.copyOfRange(allCategories, 1, allCategories.length);
        ArrayList<String> newCategories = new ArrayList<>();

        boolean[] tempIsChecked = new boolean[isChecked.length];
        for (int i = 0; i < isChecked.length; i++) {
            tempIsChecked[i] = isChecked[i];
        }


        builder.setTitle(R.string.category)
                .setMultiChoiceItems(categoriesWithoutAll, tempIsChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int indexSelected, boolean checked) {
                        tempIsChecked[indexSelected] = checked;
                    }
                });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ArrayList<Integer> addedPosition = new ArrayList<>();
                // User clicked OK button, pass through options
                // update adapter
                for (int i = 0; i < isChecked.length; i++) {
                    if(tempIsChecked[i]){
                        newCategories.add(categoriesWithoutAll[i]);
                    }
                }
                adjustedCategories = newCategories;
                for(int i = 0; i < isChecked.length; i++) {
                    if(tempIsChecked[i] != isChecked[i] && !tempIsChecked[i]){ // if changed
                        deleteTab(i);
                    }
                    else if(tempIsChecked[i] != isChecked[i] && tempIsChecked[i]){ // if changed
                        addedPosition.add(i+1);
                    }
                    isChecked[i] = tempIsChecked[i];
                }
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        adapter.setCategories(newCategories);
                        adapter.notifyDataSetChanged();
                        setupTabViewWithInvisibles(0, addedPosition);
                    }
                }, 1000);

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}