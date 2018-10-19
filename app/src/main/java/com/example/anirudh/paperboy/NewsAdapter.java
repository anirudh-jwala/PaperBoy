package com.example.anirudh.paperboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> theNews) {
        super(context, 0, theNews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleView = listItemView.findViewById(R.id.story_title);
        titleView.setText(currentNews.getTitle());

        TextView sectionView = listItemView.findViewById(R.id.section);
        String section = "Category: " + currentNews.getSection();
        sectionView.setText(section);

        TextView dateView = listItemView.findViewById(R.id.date);
        String fullDate = currentNews.getDate();
        String shortDate = fullDate.substring(0, 10);
        if (currentNews.getDate() != null) {
            dateView.setText(shortDate);
        } else {
            dateView.setVisibility(View.GONE);
        }
        TextView authorView = listItemView.findViewById(R.id.author);
        if (currentNews.getAuthor() != null) {
            String author = "By: " + currentNews.getAuthor();
            authorView.setText(author);
        } else {
            authorView.setVisibility(View.GONE);
        }

        return listItemView;
    }
}