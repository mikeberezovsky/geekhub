package com.michaelb.homeworklong.asynctask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.michaelb.homeworklong.constants.BlogNewsServiceConstants;
import com.michaelb.homeworklong.entities.HackerNewsRSSItem;
import com.michaelb.homeworklong.entities.WordpressBlogRSSItem;
import com.michaelb.homeworklong.fragment.RSSListFragment;
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
public class BlogNewsRequestTask extends AsyncTask<String, String, List<WordpressBlogRSSItem>> {

    private static final String CLASS_NAME = String.valueOf(BlogNewsRequestTask.class);
    private DateFormat dafeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private List<WordpressBlogRSSItem> wordpressBlogRSSItems = null;
    private RSSListFragment rssListFragment = null;
    private Context serviceContext = null;
    private long mostFreshNewsTimestamp = 0;
    private long mostFreshNewsTimestampLimit = 0;

    public BlogNewsRequestTask(RSSListFragment rssListFragment) {
        this.rssListFragment = rssListFragment;
    }

    public BlogNewsRequestTask(Context context, long displayNewsAfter) {
        mostFreshNewsTimestampLimit = displayNewsAfter;
        serviceContext = context;
    }

    public BlogNewsRequestTask(RSSListFragment rssListFragment, long displayNewsAfter) {
        this.rssListFragment = rssListFragment;
        this.mostFreshNewsTimestampLimit = displayNewsAfter;
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
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            Log.e(CLASS_NAME, "Error getting feed", e);
        } catch (IOException e) {
            Log.e(CLASS_NAME, "Error getting feed", e);
        }
        try {
            JSONArray items = new JSONArray(responseString);
            Date postDate = null;
            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonItem = items.getJSONObject(i);
                postDate = dafeFormat.parse(jsonItem.getString("date"));
                if (postDate.getTime() > mostFreshNewsTimestampLimit) {
                    WordpressBlogRSSItem rssItem = new WordpressBlogRSSItem();
                    rssItem.setTitle(jsonItem.getString("title"));
                    rssItem.setUrl(jsonItem.getString("permalink"));
                    rssItem.setDatePublished(postDate);
                    wordpressBlogRSSItems.add(rssItem);
                    if (rssItem.getDatePublishedTimestamp() > mostFreshNewsTimestamp) {
                        mostFreshNewsTimestamp = rssItem.getDatePublishedTimestamp();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Error parsing JSON", e);
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
        } else {
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
    }
}
