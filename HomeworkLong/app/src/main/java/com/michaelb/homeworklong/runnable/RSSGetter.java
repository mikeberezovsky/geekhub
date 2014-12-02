package com.michaelb.homeworklong.runnable;

import android.os.Handler;
import android.util.Log;

import com.michaelb.homeworklong.entities.HackerNewsRSSItem;

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
import java.util.List;

/**
 * Created by michaelb on 11/30/14.
 */
public class RSSGetter implements Runnable {
    private static final String CLASS_NAME = String.valueOf(RSSGetter.class);
    private Handler uiHandler = null;
    private String feedURL = null;
    private List<HackerNewsRSSItem> hackerNewsRSSItems = null;

    public RSSGetter(Handler handler, String rssURL) {
        uiHandler = handler;
        feedURL = rssURL;
    }

    public List<HackerNewsRSSItem> getHackerNewsRSSItems() {
        return hackerNewsRSSItems;
    }

    @Override
    public void run() {

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        if (feedURL != null) {
            try {
                response = httpclient.execute(new HttpGet(feedURL));
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
        if (hackerNewsRSSItems != null) {
            uiHandler.post(this);
        }

    }
}
