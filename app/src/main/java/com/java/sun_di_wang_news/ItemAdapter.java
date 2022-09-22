package com.java.sun_di_wang_news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingChild;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private final ArrayList<NewsObject> items;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    View view;
    boolean isMainView = false;

    public ItemAdapter(Context context, ArrayList<NewsObject> items, boolean isMainView) {
        this.context = context;
        this.items = items;
        this.isMainView = isMainView;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        if (type == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news, null);
            ItemViewHolder result = new ItemViewHolder(view);
            result.setIsRecyclable(false);
            return result;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof ItemViewHolder) {
            final NewsObject item = items.get(holder.getAdapterPosition());
            boolean alreadyRead = item.alreadyRead;
            String title = item.title;
            String publisher = item.publisher;
            String date = item.publishTime;
            String urlToImage = item.imageLink;

            if(alreadyRead && isMainView) {
                ((ItemViewHolder) holder).editTitle.setTextColor(context.getColor(R.color.dark_grey));
            }

            if (title != null)
                ((ItemViewHolder) holder).editTitle.setText(title);
            if (publisher != null)
                ((ItemViewHolder) holder).editAuthor.setText(publisher);
            if (date != null)
                ((ItemViewHolder) holder).editDate.setText(date);
            if (urlToImage != null && !urlToImage.equals("")) {
                Picasso.get().load(urlToImage).memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(((ItemViewHolder) holder).editUrlImage);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Type type = new TypeToken<List<NewsObject>>() {
                    }.getType();

                    final NewsObject item = items.get(holder.getAdapterPosition());
                    item.alreadyRead = true;
                    if(isMainView)
                        ((ItemViewHolder) holder).editTitle.setTextColor(context.getColor(R.color.dark_grey));

                    SharedPreferences appSharedPrefs = PreferenceManager
                            .getDefaultSharedPreferences(context);
                    SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();

                    Gson gson = new Gson();
                    String json = appSharedPrefs.getString("history", "");
                    List<NewsObject> newsObjectList = new ArrayList<NewsObject>();
                    List<NewsObject> tempList = gson.fromJson(json, type);
                    if(tempList != null){
                        newsObjectList.addAll(tempList);
                    }
                    newsObjectList.removeIf(a -> a.newsID.equals(item.newsID)); // remove dupes

                    newsObjectList.add(item);

                    for (NewsObject a : newsObjectList) {
                        System.out.println(a.title + " " + a.publisher);
                    }

                    Gson newGson = new Gson();
                    String newJson = newGson.toJson(newsObjectList);
                    prefsEditor.putString("history", newJson);
                    prefsEditor.apply();

                    Intent myIntent = new Intent(v.getContext(), DisplayNewsActivity.class);
                    myIntent.putExtra(MainActivity.NEWS_TITLE, item.title);
                    myIntent.putExtra(MainActivity.NEWS_TIME, item.publishTime);
                    myIntent.putExtra(MainActivity.NEWS_AUTHOR, item.publisher);
                    myIntent.putExtra(MainActivity.NEWS_CONTENT, item.content);
                    myIntent.putExtra(MainActivity.NEWS_IMAGE_URL, item.imageLink);
                    myIntent.putExtra(MainActivity.NEWS_VIDEO_URL, item.videoLink);
                    myIntent.putExtra(MainActivity.NEWS_ID, item.newsID);
                    myIntent.putExtra(MainActivity.NEWS_ORIGIN, isMainView);
                    context.startActivity(myIntent);

                }
            });
        }

    }

    public void removeAt(int position) {
        if (items.get(position) != null) {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        }

    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView editTitle, editAuthor, editDate;
        ImageView editUrlImage;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            editTitle = itemView.findViewById(R.id.title1);
            editUrlImage = itemView.findViewById(R.id.image1);
            editAuthor = itemView.findViewById(R.id.author);
            editDate = itemView.findViewById(R.id.date);
        }
    }

}