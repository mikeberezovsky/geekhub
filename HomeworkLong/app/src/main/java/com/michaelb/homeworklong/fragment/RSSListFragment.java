package com.michaelb.homeworklong.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import com.michaelb.homeworklong.constants.FeedURLs;
import com.michaelb.homeworklong.entities.HackerNewsRSSItem;
import com.michaelb.homeworklong.asynctask.RequestTask;

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
    private List<HackerNewsRSSItem> hackerNewsRSSItems = null;
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
                new RequestTask(this).execute(FeedURLs.hackernewsFeedURL);
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

    public void initRSSList(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Log.i(CLASS_NAME, "onViewCreated savedInstanceState is not null.");
            hackerNewsRSSItems = savedInstanceState.getParcelableArrayList(RSS_ITEMS_STORAGE);
        }
        if (hackerNewsRSSItems != null) {
            Log.i(CLASS_NAME, "onViewCreated hackerNewsRSSItems in bundle bundle are not empty.");
            initRSSList(hackerNewsRSSItems);
        } else {
            refreshRSSListData();
        }
    }

    public void initRSSList(List<HackerNewsRSSItem> items) {
        hackerNewsRSSItems = items;
        if (items == null) {
            Log.i(CLASS_NAME, "items parameter is null.");
        } else {
            Log.i(CLASS_NAME, "items parameter is not null.");
        }
        HackerNewsRSSItem[] rssListItemsArray = items.toArray(new HackerNewsRSSItem[items.size()]);
        if (getActivity() != null) {
            lv.setAdapter(
                    new RSSListAdapter(getActivity(), R.layout.rss_list_item, rssListItemsArray)
            );
        }

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HackerNewsRSSItem rssItem = (HackerNewsRSSItem) parent.getItemAtPosition(position);
                activityListener.onListItemSelect(rssItem.getUrl());
            }
        };
        lv.setOnItemClickListener(listener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(CLASS_NAME, "onSaveInstanceState is called.");
        if (hackerNewsRSSItems != null && !rssAsyncInProgress) {
            Log.i(CLASS_NAME, "onSaveInstanceState hackerNewsRSSItems is not null. Saving data");
            outState.putParcelableArrayList(RSS_ITEMS_STORAGE, (ArrayList<? extends android.os.Parcelable>) hackerNewsRSSItems);
        }
    }

    public void setHackerNewsRSSItems(List<HackerNewsRSSItem> hackerNewsRSSItems) {
        this.hackerNewsRSSItems = hackerNewsRSSItems;
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
