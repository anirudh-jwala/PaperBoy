package com.example.anirudh.paperboy;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;


import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final String GUARDIANS_REQUEST_URL = "http://content.guardianapis.com/search?api-key=a453017b-6b96-4c3f-834d-e9569f4f0444";
    private static final int NEWS_LOADER_ID = 1;

    private NewsAdapter mAdapter;
    private LinearLayout mEmptyStateLinear;
    private TextView mEmptyStateTextView;
    private ImageView mEmptyStateImageView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = findViewById(R.id.root);

        mEmptyStateLinear = findViewById(R.id.sub_root);
        mEmptyStateTextView = findViewById(R.id.no_data_text);
        mEmptyStateImageView = findViewById(R.id.no_data_image);

        newsListView.setEmptyView(mEmptyStateLinear);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.progression);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateImageView.setImageResource(R.drawable.wifi);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        //creating sharedPreferences object and get default values like page size and order by setting
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pageSize = sharedPreferences.getString(getString(R.string.settings_page_size_key), getString(R.string.settings_page_size_default));
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(GUARDIANS_REQUEST_URL);
        Uri.Builder builder = baseUri.buildUpon();

        builder.appendQueryParameter(getString(R.string.query_string_show_fields), getString(R.string.query_parameter_thumbnail));
        builder.appendQueryParameter(getString(R.string.query_string_order_by), orderBy);
        builder.appendQueryParameter(getString(R.string.query_string_show_tags), getString(R.string.query_parameter_contributor));
        builder.appendQueryParameter(getString(R.string.query_string_page_size), pageSize);

        return new NewsLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> theNews) {
        View loadingIndicator = findViewById(R.id.progression);
        loadingIndicator.setVisibility(View.GONE);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mEmptyStateTextView.setText(R.string.no_news);
            mEmptyStateImageView.setImageResource(R.drawable.newspaper);

        } else {
            mEmptyStateImageView.setImageResource(R.drawable.wifi);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        mAdapter.clear();
        //notify about the data changed
        mAdapter.notifyDataSetChanged();

        if (theNews != null && !theNews.isEmpty()) {
            mAdapter.addAll(theNews);
            //notify about the data changed
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
        //notify about the data changed
        mAdapter.notifyDataSetChanged();
    }
}