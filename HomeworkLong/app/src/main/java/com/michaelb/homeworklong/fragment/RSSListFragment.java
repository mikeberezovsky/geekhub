package com.michaelb.homeworklong.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.michaelb.homeworklong.R;
import com.michaelb.homeworklong.adapter.RSSListAdapter;
import com.michaelb.homeworklong.adapter.WordpressBlogRSSListAdapter;
import com.michaelb.homeworklong.asynctask.BlogNewsListStoredTask;
import com.michaelb.homeworklong.asynctask.BlogNewsRequestTask;
import com.michaelb.homeworklong.constants.AppValues;
import com.michaelb.homeworklong.constants.BlogNewsServiceConstants;
import com.michaelb.homeworklong.constants.FeedURLs;
import com.michaelb.homeworklong.entities.HackerNewsRSSItem;
import com.michaelb.homeworklong.asynctask.RequestTask;
import com.michaelb.homeworklong.entities.WordpressBlogRSSItem;
import com.michaelb.homeworklong.service.BlogNewsService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelb on 11/11/14.
 */
public class RSSListFragment extends Fragment {

    private final static String RSS_ITEMS_STORAGE = "rss_items_bundle_key";
    private static final String CLASS_NAME = String.valueOf(RSSListFragment.class);
    private ListView lv = null;
    private ActivityListener activityListener;
    private List<WordpressBlogRSSItem> wordpressBlogRSSItems = null;
    private long mostFreshNewsTimestamp = 0;
    private boolean rssAsyncInProgress = false;

    public interface ActivityListener {
        public void onListItemSelect(String btnTag);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityListener = (ActivityListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ActivityListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rss_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv = (ListView) view.findViewById(R.id.rss_list_view);
        Log.i(CLASS_NAME, "onViewCreated trying to get hackerNewsRSSItems from bundle.");
        initRSSList(savedInstanceState);
    }

    public void refreshRSSListData() {
        if (isWifiConnected()) {
            if (!rssAsyncInProgress) {
                long freshNewsTimestamp = getActivity().getIntent().getLongExtra(AppValues.CUTOFF_NEWS_TIMESTAMP_KEY, 0);
                new BlogNewsRequestTask(this, freshNewsTimestamp).execute(FeedURLs.testblogFeedURL);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.connect_wifi_title)
                    .setMessage(R.string.connect_wifi_message)
                    .setCancelable(false)
                    .setNegativeButton(R.string.connect_wifi_ok_btn,
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void loadDBRSSListData(){
        if (!rssAsyncInProgress) {
            long freshNewsTimestamp = getActivity().getIntent().getLongExtra(AppValues.CUTOFF_NEWS_TIMESTAMP_KEY, 0);
            new BlogNewsListStoredTask(this, freshNewsTimestamp).execute(FeedURLs.testblogFeedURL);
        }
    }

    public void initRSSList(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Log.i(CLASS_NAME, "onViewCreated savedInstanceState is not null.");
            wordpressBlogRSSItems = savedInstanceState.getParcelableArrayList(RSS_ITEMS_STORAGE);
        }
        if (wordpressBlogRSSItems != null) {
            Log.i(CLASS_NAME, "onViewCreated hackerNewsRSSItems in bundle bundle are not empty.");
            initRSSList(wordpressBlogRSSItems);
        } else {
            loadDBRSSListData();
        }
    }

    public void initRSSList(List<WordpressBlogRSSItem> items) {
        wordpressBlogRSSItems = items;
        if (items == null) {
            Log.i(CLASS_NAME, "items parameter is null.");
        } else {
            Log.i(CLASS_NAME, "items parameter is not null.");
        }
        WordpressBlogRSSItem[] rssListItemsArray = items.toArray(new WordpressBlogRSSItem[items.size()]);
        if (getActivity() != null) {
            lv.setAdapter(
                    new WordpressBlogRSSListAdapter(getActivity(), R.layout.rss_list_item, rssListItemsArray)
            );
        }

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WordpressBlogRSSItem rssItem = (WordpressBlogRSSItem) parent.getItemAtPosition(position);
                activityListener.onListItemSelect(rssItem.getUrl());
            }
        };
        lv.setOnItemClickListener(listener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(CLASS_NAME, "onSaveInstanceState is called.");
        if (wordpressBlogRSSItems != null && !rssAsyncInProgress) {
            Log.i(CLASS_NAME, "onSaveInstanceState hackerNewsRSSItems is not null. Saving data");
            outState.putParcelableArrayList(RSS_ITEMS_STORAGE, (ArrayList<? extends android.os.Parcelable>) wordpressBlogRSSItems);
        }
    }

    public void setMostFreshNewsTimestamp(long timestamp) {
        mostFreshNewsTimestamp = timestamp;
        SharedPreferences sPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        long prefNewsTimestamp = sPref.getLong(AppValues.APP_STORAGE_KEY_TIMESTAMP, 0);
        if (timestamp > prefNewsTimestamp) {
            mostFreshNewsTimestamp = timestamp;
            SharedPreferences.Editor ed = sPref.edit();
            ed.putLong(AppValues.APP_STORAGE_KEY_TIMESTAMP, mostFreshNewsTimestamp);
            ed.commit();
        } else {
            mostFreshNewsTimestamp = prefNewsTimestamp;
        }
        Intent intent = new Intent(getActivity(), BlogNewsService.class);
        intent.setAction(BlogNewsServiceConstants.COMMAND_UPDATE_FRESH_TIMESTAMP);
        intent.putExtra(BlogNewsServiceConstants.EXTRA_KEY_TIMESTAMP, mostFreshNewsTimestamp);
        getActivity().startService(intent);
    }

    public void setRSSItems(List<WordpressBlogRSSItem> wordpressBlogRSSItems) {
        this.wordpressBlogRSSItems = wordpressBlogRSSItems;
    }

    public void setRssAsyncInProgress(boolean rssAsyncInProgress) {
        this.rssAsyncInProgress = rssAsyncInProgress;
    }

    public boolean getRssAsyncInProgress() {
        return rssAsyncInProgress;
    }

    private boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(getActivity().getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return ((netInfo != null) && netInfo.isConnected());
    }

}
