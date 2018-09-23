package com.example.anirudh.paperboy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> theNews){
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

        String fullDate = currentNews.getDate();
        String shortDate = fullDate.substring(0,10);

        TextView dateView = listItemView.findViewById(R.id.date);
        dateView.setText(shortDate);

        String author = "By: " + currentNews.getAuthor();
        TextView authorView = listItemView.findViewById(R.id.author);
        authorView.setText(author);

        return listItemView;
    }
}