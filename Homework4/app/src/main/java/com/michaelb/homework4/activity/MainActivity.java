package com.michaelb.homework4.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.michaelb.homework4.R;
import com.michaelb.homework4.entities.HackerNewsRSSItem;
import com.michaelb.homework4.fragment.RSSListFragment;

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


public class MainActivity extends Activity {
    private String rssFeedJSONString = null;
    private List<HackerNewsRSSItem> hackerNewsRSSItems = null;
    private List<String> hackerNewsRSSTitles = null;

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();
                    responseString = out.toString();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            rssFeedJSONString = responseString;
            try {
                JSONObject rssObject = new JSONObject(responseString);
                JSONArray items = rssObject.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject jsonItem = items.getJSONObject(i);
                    HackerNewsRSSItem rssItem = new HackerNewsRSSItem();
                    rssItem.setTitle(jsonItem.getString("title"));
                    rssItem.setUrl(jsonItem.getString("url"));
                    hackerNewsRSSItems.add(rssItem);
                    hackerNewsRSSTitles.add(jsonItem.getString("title"));
                }
            } catch (Exception e) {
                //do nothing
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            RSSListFragment rssListFragment = (RSSListFragment) getFragmentManager().findFragmentByTag(getResources().getString(R.string.rss_list_fragment_tag));
            if ( rssListFragment != null ) {
                //rssListFragment.setTextviewText(result);
                rssListFragment.initRSSList(hackerNewsRSSItems);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            RSSListFragment rssListFragment = new RSSListFragment();
            rssListFragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(R.id.fragment_container, rssListFragment, getResources().getString(R.string.rss_list_fragment_tag)).commit();
        }
        hackerNewsRSSItems = new ArrayList<HackerNewsRSSItem>();
        hackerNewsRSSTitles = new ArrayList<String>();
        new RequestTask().execute(getResources().getString(R.string.feed_src));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayStringResponse(String jsonString) {

    }

}