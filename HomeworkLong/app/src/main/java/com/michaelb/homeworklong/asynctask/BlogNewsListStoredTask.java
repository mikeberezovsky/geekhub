package com.michaelb.homeworklong.asynctask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.michaelb.homeworklong.constants.BlogNewsServiceConstants;
import com.michaelb.homeworklong.entities.WordpressBlogRSSItem;
import com.michaelb.homeworklong.fragment.RSSListFragment;
import com.michaelb.homeworklong.helper.BlogNewsDBHelper;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by michaelb on 11/25/14.
 */
public class BlogNewsListStoredTask extends AsyncTask<String, String, List<WordpressBlogRSSItem>> {

    private static final String CLASS_NAME = String.valueOf(BlogNewsListStoredTask.class);
    private DateFormat dafeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private List<WordpressBlogRSSItem> wordpressBlogRSSItems = null;
    private RSSListFragment rssListFragment = null;
    private Context serviceContext = null;
    private BlogNewsDBHelper dbHelper = null;
    private long mostFreshNewsTimestamp = 0;
    private long mostFreshNewsTimestampLimit = 0;

    public BlogNewsListStoredTask(RSSListFragment rssListFragment) {
        this.rssListFragment = rssListFragment;
        dbHelper = BlogNewsDBHelper.getInstance(rssListFragment.getActivity().getApplicationContext());
    }

    public BlogNewsListStoredTask(Context context, long displayNewsAfter) {
        mostFreshNewsTimestampLimit = displayNewsAfter;
        serviceContext = context;
        dbHelper = BlogNewsDBHelper.getInstance(context);
    }

    public BlogNewsListStoredTask(RSSListFragment rssListFragment, long displayNewsAfter) {
        this.rssListFragment = rssListFragment;
        this.mostFreshNewsTimestampLimit = displayNewsAfter;
        dbHelper = BlogNewsDBHelper.getInstance(rssListFragment.getActivity().getApplicationContext());
    }

    @Override
    protected void onPreExecute() {
        if (rssListFragment != null) {
            // if it is called within fragment and not a sevice
            rssListFragment.setRssAsyncInProgress(true);
            Log.i(CLASS_NAME, "RequestTask in progress = true");
        } else {
            Log.i(CLASS_NAME, "RequestTask in progress by service = true");
        }
    }

    @Override
    protected List<WordpressBlogRSSItem> doInBackground(String... uri) {
        Log.i(CLASS_NAME, "doInBackground started.");
        wordpressBlogRSSItems = new ArrayList<WordpressBlogRSSItem>();
        if (dbHelper != null) {
            if (mostFreshNewsTimestampLimit == 0) {
                wordpressBlogRSSItems = dbHelper.getAllRSSItems();
            } else {
                wordpressBlogRSSItems = dbHelper.getRSSItemsAfterTimestamp(mostFreshNewsTimestampLimit);
            }
        }
        Log.i(CLASS_NAME, "doInBackground complete.");
        return wordpressBlogRSSItems;
    }

    @Override
    protected void onPostExecute(List<WordpressBlogRSSItem> result) {
        super.onPostExecute(result);
        if (result == null) {
            Log.i(CLASS_NAME, "onPostExecute result is null.");
        } else {
            Log.i(CLASS_NAME, "onPostExecute result is not null.");
        }
        Log.i(CLASS_NAME, "initRSSList will be called from task.");
        if (rssListFragment != null) {
            rssListFragment.initRSSList(result);
            // set most fresh news timestamp only in case the task called from RSSListFragment
            rssListFragment.setMostFreshNewsTimestamp(mostFreshNewsTimestamp);
            rssListFragment.setRssAsyncInProgress(false);
            if (result == null) {
                Log.i(CLASS_NAME, "News counted: 0");
            } else {
                Log.i(CLASS_NAME, "News counted: " + result.size());
            }
            Log.i(CLASS_NAME, "RequestTask in progress = false");
            //run refresh from internet
            if (this.mostFreshNewsTimestampLimit == 0) {
                //if this is default RSS list and no the fresh RSS list
                rssListFragment.refreshRSSListData();
            }
        }
        /* not needed cause getting a list of stored RSS news will be called from fragment
        else {
            Log.i(CLASS_NAME, "News pull by service complete.");
            if (result == null) {
                Log.i(CLASS_NAME, "News counted: 0");
            } else {
                Log.i(CLASS_NAME, "News counted: " + result.size());
            }
            if (result != null && result.size() > 0) {
                Intent intent = new Intent(serviceContext, BlogNewsService.class);
                intent.setAction(BlogNewsServiceConstants.COMMAND_NOTIFY_FRESH_NEWS);
                intent.putExtra(BlogNewsServiceConstants.EXTRA_KEY_NEWS_COUNT, result.size());
                serviceContext.startService(intent);
            }
        }
        */
    }
}
