package com.michaelb.homeworklong.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.michaelb.homeworklong.R;
import com.michaelb.homeworklong.constants.AppValues;
import com.michaelb.homeworklong.constants.BlogNewsServiceConstants;
import com.michaelb.homeworklong.fragment.RSSContentFragment;
import com.michaelb.homeworklong.fragment.RSSListFragment;

public class FreshNewsActivity extends Activity implements RSSListFragment.ActivityListener {
    private static final String CLASS_NAME = String.valueOf(FreshNewsActivity.class);
    private RSSListFragment rssListFragment = null;
    public void onListItemSelect(String itemURL) {
        RSSContentFragment dataContentFragment =
                (RSSContentFragment) getFragmentManager().findFragmentById(R.id.content_fragment);
        if (dataContentFragment != null && dataContentFragment.isVisible()) {
            dataContentFragment.setWebViewURL(itemURL);
        } else {
            RSSContentFragment contentFragment = new RSSContentFragment();
            Bundle args = new Bundle();
            args.putString(RSSContentFragment.ARG_URL ,itemURL);
            contentFragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,contentFragment).addToBackStack(null).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(CLASS_NAME, "onCreate called");
        if (savedInstanceState != null) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(BlogNewsServiceConstants.FRESH_NEWS_NOTIFICATION_ID);

        if (findViewById(R.id.fragment_container) != null) {
            RSSListFragment rssListFragment = new RSSListFragment();
            rssListFragment.setArguments(getIntent().getExtras());
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, rssListFragment)
                    .commit();
            this.rssListFragment = rssListFragment;
        } else {
            rssListFragment = (RSSListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fresh_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
