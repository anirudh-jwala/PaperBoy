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
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);
        TextView titleView = listItemView.findViewById(R.id.title_textview);
        titleView.setText(currentNews.getTitle());
        TextView sectionView = listItemView.findViewById(R.id.section_textview);
        String section = "Section: " + currentNews.getSection();
        sectionView.setText(section);
        String fullDate = currentNews.getDate();
        String shortDate = fullDate.substring(0,10);
        TextView dateView = listItemView.findViewById(R.id.date_textview);
        dateView.setText(shortDate);
        String author = "By: " + currentNews.getAuthor();
        TextView authorView = listItemView.findViewById(R.id.author_textview);
        authorView.setText(author);
        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}