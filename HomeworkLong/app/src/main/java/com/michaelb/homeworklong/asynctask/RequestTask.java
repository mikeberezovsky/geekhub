package com.michaelb.homeworklong.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.michaelb.homeworklong.entities.HackerNewsRSSItem;
import com.michaelb.homeworklong.fragment.RSSListFragment;

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
 * Created by michaelb on 11/25/14.
 */
public class RequestTask extends AsyncTask<String, String, List<HackerNewsRSSItem>> {

    private static final String CLASS_NAME = String.valueOf(RequestTask.class);
    private List<HackerNewsRSSItem> hackerNewsRSSItems = null;
    private RSSListFragment rssListFragment = null;

    public RequestTask(RSSListFragment rssListFragment) {
        this.rssListFragment = rssListFragment;
    }

    @Override
    protected void onPreExecute() {
        rssListFragment.setRssAsyncInProgress(true);
        Log.i(CLASS_NAME, "RequestTask in progress = true");
    }

    @Override
    protected List<HackerNewsRSSItem> doInBackground(String... uri) {
        Log.i(CLASS_NAME, "doInBackground started.");
        hackerNewsRSSItems = new ArrayList<HackerNewsRSSItem>();
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
            JSONObject rssObject = new JSONObject(responseString);
            JSONArray items = rssObject.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonItem = items.getJSONObject(i);
                HackerNewsRSSItem rssItem = new HackerNewsRSSItem();
                rssItem.setTitle(jsonItem.getString("title"));
                rssItem.setUrl(jsonItem.getString("url"));
                hackerNewsRSSItems.add(rssItem);
            }
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Error parsing JSON", e);
        }
        Log.i(CLASS_NAME, "doInBackground complete.");
        return hackerNewsRSSItems;
    }

    @Override
    protected void onPostExecute(List<HackerNewsRSSItem> result) {
        super.onPostExecute(result);
        if (result == null) {
            Log.i(CLASS_NAME, "onPostExecute result is null.");
        } else {
            Log.i(CLASS_NAME, "onPostExecute result is not null.");
        }
        Log.i(CLASS_NAME, "initRSSList will be called from task.");
        rssListFragment.initRSSList(result);
        rssListFragment.setRssAsyncInProgress(false);
        Log.i(CLASS_NAME, "RequestTask in progress = false");
    }
}
