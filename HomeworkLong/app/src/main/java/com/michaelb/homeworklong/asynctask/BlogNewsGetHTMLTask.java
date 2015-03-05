package com.michaelb.homeworklong.asynctask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.michaelb.homeworklong.constants.BlogNewsServiceConstants;
import com.michaelb.homeworklong.entities.WordpressBlogRSSItem;
import com.michaelb.homeworklong.fragment.RSSContentFragment;
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
public class BlogNewsGetHTMLTask extends AsyncTask<String, String, String> {

    private static final String CLASS_NAME = String.valueOf(BlogNewsGetHTMLTask.class);
    private RSSContentFragment rssContentFragment = null;

    public BlogNewsGetHTMLTask(RSSContentFragment rssContentFragment) {
        this.rssContentFragment = rssContentFragment;
    }

    @Override
    protected void onPreExecute() {
        if (rssContentFragment != null) {
            // if it is called within fragment and not a sevice
            rssContentFragment.setRssAsyncInProgress(true);
            Log.i(CLASS_NAME, "RequestTask in progress = true");
        } else {
            Log.i(CLASS_NAME, "RequestTask in progress by service = true");
        }
    }

    @Override
    protected String doInBackground(String... uri) {
        Log.i(CLASS_NAME, "doInBackground started.");
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
            Log.e(CLASS_NAME, "Error getting HTML", e);
        } catch (IOException e) {
            Log.e(CLASS_NAME, "Error getting HTML", e);
        }

        Log.i(CLASS_NAME, "doInBackground complete.");
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result == null) {
            Log.i(CLASS_NAME, "onPostExecute result is null.");
        } else {
            Log.i(CLASS_NAME, "onPostExecute result is not null.");
        }
        Log.i(CLASS_NAME, "initRSSList will be called from task.");
        if (rssContentFragment != null) {
            rssContentFragment.setWebViewContent(result);
            rssContentFragment.setRssAsyncInProgress(false);
            if (result == null) {
                Log.i(CLASS_NAME, "No HTML received");
            }
            Log.i(CLASS_NAME, "RequestTask in progress = false");
        }
    }
}
