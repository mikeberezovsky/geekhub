package com.michaelb.homeworklong.activity;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.michaelb.homeworklong.R;
import com.michaelb.homeworklong.fragment.RSSContentFragment;
import com.michaelb.homeworklong.fragment.RSSListFragment;

public class MainActivity extends Activity implements RSSListFragment.ActivityListener {
    private static final String CLASS_NAME = String.valueOf(MainActivity.class);
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
        if (!isWifiConnected()) {
            Log.i(CLASS_NAME, "onCreate called. No wifi connected");
            setTitle(getResources().getString(R.string.connect_wifi));
        } else {
            setContentView(R.layout.activity_main);
            getActionBar().setDisplayHomeAsUpEnabled(true);
            Log.i(CLASS_NAME, "onCreate called");
            if (savedInstanceState != null) {
                return;
            }
            if (findViewById(R.id.fragment_container) != null) {
                RSSListFragment rssListFragment = new RSSListFragment();
                rssListFragment.setArguments(getIntent().getExtras());
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, rssListFragment)
                        .commit();
            }
        }
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

    private boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return ((netInfo != null) && netInfo.isConnected());
    }

}
