package com.java.sun_di_wang_news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class NewsFragment extends Fragment {
    ArrayList<NewsObject> newsItems = new ArrayList<>();
    ItemAdapter adapter;
    int currentPage = 0;
    int previousPageSize = 0;
    boolean isLoading = false;
    boolean isInitiated = false;
    int pos;

    int defaultSize = 15;
    String searchStartDate;
    String searchEndDate;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    CharSequence searchWords;
    CharSequence searchCategory;
    ArrayList<String> activeCategories;

    ProgressBar bottomProgressBar;
    SwipeRefreshLayout swipeContainer;

    public NewsFragment() {
    }

    public NewsFragment(int i, ArrayList<String> activeCategories) {
        this.pos = i;
        this.activeCategories = activeCategories;
    }

    public NewsFragment(ArrayList<NewsObject> items) {
        this.newsItems = items;
        Collections.reverse(newsItems);
        this.pos = -2;
    }

    public NewsFragment(int i, Date startDate, Date endDate, CharSequence words, CharSequence category) {
        this.pos = i;

        if (startDate.before(new Date(2010, 1, 1))) {
            searchStartDate = "";
        } else {
            searchStartDate = df.format(startDate);
        }
        searchEndDate = df.format(endDate);
        searchWords = words;
        searchCategory = category;
    }

    public String getCurrentDate() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        String dateString = df.format(dt);
        return dateString;
    }

    public String getURL(boolean refresh, String formattedDate) {
        String url;
        String categoryUrl;
        URI uri;
        // get urls for diff categories
        if (pos == -1) { // search
            url = "/svc/news/queryNewsList?size=" + defaultSize + "&startDate=" + searchStartDate + "&endDate=" + searchEndDate + "&words=" + searchWords + "&categories=" + searchCategory;
        } else if (pos == 0) {
            url = "/svc/news/queryNewsList?size=" + defaultSize + "&startDate=&endDate=" + formattedDate + "&words=&categories=";
        } else {
            try {
                categoryUrl = activeCategories.get(pos - 1);
                url = "/svc/news/queryNewsList?size=" + defaultSize + "&startDate=&endDate=" + formattedDate + "&words=&categories=" + categoryUrl;
            } catch (Exception e) {
                e.printStackTrace();
                url = "/svc/news/queryNewsList?size=" + defaultSize + "&startDate=&endDate=" + formattedDate + "&words=&categories=";
            }

        }
        if (!refresh)
            url = url + "&page=" + (currentPage + 1);

        try {
            uri = new URI("https://" + "api2.newsminer.net" + url);
            url = uri.toASCIIString();
            System.out.println(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return url;
    }

    public boolean checkHistoryStatus(String newsID) {
        try {
            Type type = new TypeToken<ArrayList<NewsObject>>() {
            }.getType();

            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(getContext());

            Gson gson = new Gson();
            String json = appSharedPrefs.getString("history", "");
            if (gson.fromJson(json, type) != null) {
                ArrayList<NewsObject> newsObjectArrayList = new ArrayList<>();
                newsObjectArrayList = gson.fromJson(json, type);

                for (NewsObject obj : newsObjectArrayList) {
                    if (newsID.equals(obj.newsID)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addNewsObject(JsonObject obj) {
        String id = obj.get("newsID").getAsString();
        String imageLink = "";
        try {
            String imageLinksString = obj.get("image").getAsString();
            if (!imageLinksString.isEmpty()) {
                imageLinksString = imageLinksString.substring(1, imageLinksString.length() - 1);
                String[] imageLinkArr = imageLinksString.split(",");
                if (imageLinkArr.length >= 1) {
                    imageLink = imageLinkArr[0];
                } else {
                    imageLink = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String publishTime = obj.get("publishTime").getAsString();
        String language = obj.get("language").getAsString();
        String videoLink = obj.get("video").getAsString();
        String title = obj.get("title").getAsString();
        String content = obj.get("content").getAsString();
        String publisher = obj.get("publisher").getAsString();
        String category = obj.get("category").getAsString();

        // unused data
//        HashMap<String, Double> keywordScores = processDoubleData(
//                obj.get("keywords").getAsJsonArray(), "word", "score");
//        HashMap<String, Double> dateScores = processDoubleData(
//                obj.get("when").getAsJsonArray(), "word", "score");
//        HashMap<String, Integer> peopleScores = processIntegerData(
//                obj.get("persons").getAsJsonArray(), "mention", "count");
//        String crawlTime = obj.get("crawlTime").getAsString();
//        HashMap<String, Integer> orgScores = processIntegerData(
//                obj.get("organizations").getAsJsonArray(), "mention", "count");

        boolean alreadyRead = checkHistoryStatus(id);

        newsItems.add(new NewsObject(imageLink, publishTime,
                language, videoLink, title, content, id,
                publisher, category, alreadyRead));
    }


    public void loadNews(RecyclerView recyclerView, boolean refresh) {
        String formattedDate = getCurrentDate();
        String url = getURL(refresh, formattedDate);

//        url = "https://api2.newsminer.net/svc/news/queryNewsList?words=%E8%99%9A%E6%8B%9F%E7%8E%B0%E5%AE%9E&size=1&startDate=2021-09-01&endDate=2021-09-02&model=withUrl&websites=%E6%96%B0%E5%8D%8E%E7%BD%91,%E4%BA%BA%E6%B0%91%E7%BD%91";

        Ion.getDefault(requireContext()).getConscryptMiddleware().enable(false);
        Ion.with(this).load("GET", url).asJsonObject().setCallback((e, result) -> {
            try {
                int pageSize = 0;
                if (result != null && result.get("pageSize") != null) {
                    pageSize = result.get("pageSize").getAsInt();
                } else {
                    System.out.println(result.toString());
                }
                if (pageSize > 0) {
                    JsonArray array = result.get("data").getAsJsonArray();
                    currentPage = result.get("currentPage").getAsInt();
                    previousPageSize = result.get("total").getAsInt();
                    if (refresh && isInitiated) {
                        int tempSize = newsItems.size();
                        newsItems.clear();
                        adapter.notifyDataSetChanged();
                    }
                    for (JsonElement element : array) {
                        JsonObject obj = element.getAsJsonObject();
                        if (obj != null)
                            addNewsObject(obj);
                    }
                    if (!isInitiated) {
                        adapter = new ItemAdapter(getActivity(), newsItems, true);
                        recyclerView.setAdapter(adapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        isInitiated = true;
                    } else {
                        adapter.notifyItemRangeInserted((currentPage - 1) * 15, 15);
                    }
                    System.out.println("Success");
                } else {
                    System.out.println("FAILED to get elements");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            isLoading = false;
//            topProgressBar.setVisibility(View.GONE);
            bottomProgressBar.setVisibility(View.GONE);
            swipeContainer.setRefreshing(false);
        });
    }

    private void loadLocalNews(RecyclerView recyclerView) {
        try {
            adapter = new ItemAdapter(getActivity(), newsItems, false);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRefreshListener(RecyclerView recyclerView) {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(recyclerView, true);
            }

        });
    }

    private void initScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == newsItems.size() - 1) {
                        if (bottomProgressBar != null)
                            bottomProgressBar.setVisibility(View.VISIBLE);
                        final Handler h = new Handler();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                h.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadNews(recyclerView, false);
                                    }
                                }, 200); // 0.2 second delay (takes millis)
                            }
                        }).start();
                        isLoading = true;
                    }
                }
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xmlfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            final RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
            swipeContainer = getView().findViewById(R.id.swipeContainer);
            if (pos != -2) {
//                topProgressBar = getView().findViewById(R.id.progressBar);
                initRefreshListener(recyclerView);
                bottomProgressBar = getView().findViewById(R.id.progressBar2);
                initScrollListener(recyclerView);
                loadNews(recyclerView, false);
            } else {
                loadLocalNews(recyclerView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // unused functions for unused keyword data

    public HashMap<String, Double> processDoubleData(JsonArray jArray, String key, String value) {
        HashMap<String, Double> resultsList = new HashMap<>();
        for (int i = 0; i < jArray.size(); i++) {
            JsonObject a = jArray.get(i).getAsJsonObject();
            resultsList.put(a.get(key).getAsString(), a.get(value).getAsDouble());
        }
        return resultsList;
    }

    public HashMap<String, Integer> processIntegerData(JsonArray jArray, String key, String value) {
        HashMap<String, Integer> resultsList = new HashMap<>();
        for (int i = 0; i < jArray.size(); i++) {
            JsonObject a = jArray.get(i).getAsJsonObject();
            resultsList.put(a.get(key).getAsString(), a.get(value).getAsInt());
        }
        return resultsList;
    }
}