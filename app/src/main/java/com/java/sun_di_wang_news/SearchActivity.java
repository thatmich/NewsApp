package com.java.sun_di_wang_news;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class SearchActivity extends AppCompatActivity {

    long DAY_IN_MS = 1000 * 60 * 60 * 24;

    EditText searchEditText;
    AlertDialog.Builder builder;
    int timeMode = 0;
    int tempTimeMode = timeMode;
    int categoryMode = 0;
    int tempCategoryMode = categoryMode;

    boolean fragmentExists = false;

    private void search(CharSequence searchTerm) {
        // TODO: Add more search parameters
        if (searchTerm.length() <= 0) {
            return;
        }
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Date endTime = Calendar.getInstance().getTime();
        Date startTime;
        CharSequence category;

        if (categoryMode == 0) {
            category = "";
        } else {
            try {
                category = getResources().getStringArray(R.array.category_array)[categoryMode];
            } catch (Exception e) {
                category = "";
            }

        }

        switch (timeMode) {
            case 1:
                startTime = new Date(endTime.getTime() - (1 * DAY_IN_MS));
                break;
            case 2:
                startTime = new Date(endTime.getTime() - (7 * DAY_IN_MS));
                break;
            case 3:
                startTime = new Date(endTime.getTime() - (30 * DAY_IN_MS));
                break;
            case 4:
                startTime = new Date(endTime.getTime() - (365 * DAY_IN_MS));
                break;
            default:
                startTime = new Date(1999, 01, 01);
        }
        endTime = new Date(endTime.getTime() + (1 * DAY_IN_MS));


        NewsFragment newsFragment = new NewsFragment(-1, startTime, endTime, searchTerm, category);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.search_fragment_container_view, newsFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        fragmentExists = true;
    }

    public void searchButtonHandler(View view) {
        if (searchEditText != null && searchEditText.getText().length() > 0) {
            search(searchEditText.getText());
        }
    }

    public void searchTimeHandler(View view) {
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button, pass through options

                timeMode = tempTimeMode;
                // change text of filter button
                Button b = (Button) view;
                try {
                    if(timeMode == 0){
                        b.setText(getResources().getString(R.string.time));
                    }
                    else {
                        b.setText(getResources().getStringArray(R.array.time_array)[timeMode]);
                    }

                } catch (Exception e) {
                    b.setText(getResources().getString(R.string.time));
                }
                if (searchEditText != null && searchEditText.getText().length() > 0) {
                    search(searchEditText.getText());
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setTitle(R.string.time_dialog_message)
                .setSingleChoiceItems(R.array.time_array, timeMode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tempTimeMode = i;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void searchCategoryHandler(View view) {
        builder.setTitle(R.string.category)
                .setSingleChoiceItems(R.array.category_array, categoryMode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tempCategoryMode = i;
                    }
                });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button, pass through options

                categoryMode = tempCategoryMode;
                Button b = (Button) view;
                try {
                    if(categoryMode == 0){
                        b.setText(getResources().getString(R.string.category));
                    }
                    else {
                        b.setText(getResources().getStringArray(R.array.category_array)[categoryMode]);
                    }

                } catch (Exception e) {
                    b.setText(getResources().getString(R.string.category));
                }

                if (searchEditText != null && searchEditText.getText().length() > 0) {
                    search(searchEditText.getText());
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.search);
        ImageButton searchButton = (ImageButton) findViewById(R.id.searchEnterButton);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        TextView.OnEditorActionListener searchListener =
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                            search(textView.getText());
                        }
                        return false;
                    }
                };
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    searchButton.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
                } else {
                    searchButton.setBackgroundColor(Color.parseColor("#D3D3D3"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        builder = new AlertDialog.Builder(SearchActivity.this);
    }
    // create menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
}
