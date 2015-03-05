package com.michaelb.homeworklong.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.michaelb.homeworklong.R;
import com.michaelb.homeworklong.activity.MainActivity;


/**
 * Created by michaelb on 11/11/14.
 */
public class RSSContentFragment extends Fragment {

    public final static String ARG_URL = "cur_url";
    private String currentURL = null;
    private boolean rssAsyncInProgress = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //MainActivity activity = (MainActivity) getActivity();
        //activity.displaySaveMenuItem();
        if (savedInstanceState != null) {
            currentURL = savedInstanceState.getString(ARG_URL);
        }
        return inflater.inflate(R.layout.rss_content_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            setWebViewURL(args.getString(ARG_URL));
        } else {
            if (currentURL != null) {
                setWebViewURL(currentURL);
            } else {
                setWebViewURL(null);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_URL,currentURL);
    }

    public void setWebViewURL(String url) {
        currentURL = url;
        View fragmentView = getView();
        WebView webView = (WebView) fragmentView.findViewById(R.id.rss_content_view);
        if (url != null) {
            webView.loadUrl(url);
        } else {
            webView.loadData(getResources().getString(R.string.click_item_to_view_content),"text/html", null);
        }
    }

    public String getCurrentURL() {
        return currentURL;
    }

    public void setRssAsyncInProgress(boolean rssAsyncInProgress) {
        this.rssAsyncInProgress = rssAsyncInProgress;
    }

    public void setWebViewContent(String content) {
        View fragmentView = getView();
        WebView webView = (WebView) fragmentView.findViewById(R.id.rss_content_view);
        webView.loadData(content,"text/html", null);
    }

    public boolean getRssAsyncInProgress() {
        return rssAsyncInProgress;
    }


}
