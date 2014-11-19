package com.michaelb.homeworklong.fragment;

import android.app.Activity;
import android.app.Fragment;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelb on 11/11/14.
 */
public class RSSListFragment extends Fragment {

    public interface ActivityListener {
        public void onListItemSelect(String btnTag);
    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
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
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
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
                //do nothing
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("MainActivity", "populateRSSListFragment will be called from task.");
            initRSSList(hackerNewsRSSItems);

        }
    }

    ActivityListener activityListener;
    private List<HackerNewsRSSItem> hackerNewsRSSItems = null;

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
        hackerNewsRSSItems = new ArrayList<HackerNewsRSSItem>();
        new RequestTask().execute(getResources().getString(R.string.feed_src));
    }

    public void initRSSList(List<HackerNewsRSSItem> items) {
        View fragmentView = getView();
        ListView lv = (ListView) fragmentView.findViewById(R.id.rss_list_view);
        lv.setAdapter(new RSSListAdapter(getActivity(), R.layout.rss_list_item, items.toArray(new HackerNewsRSSItem[items.size()])));
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HackerNewsRSSItem rssItem = (HackerNewsRSSItem) parent.getItemAtPosition(position);
                activityListener.onListItemSelect(rssItem.getUrl());
            }
        };
        lv.setOnItemClickListener(listener);
    }

}
