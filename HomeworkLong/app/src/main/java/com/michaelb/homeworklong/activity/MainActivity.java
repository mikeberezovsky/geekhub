package com.michaelb.homeworklong.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.michaelb.homeworklong.R;
import com.michaelb.homeworklong.constants.AppValues;
import com.michaelb.homeworklong.constants.BlogNewsServiceConstants;
import com.michaelb.homeworklong.entities.WordpressBlogPost;
import com.michaelb.homeworklong.entities.WordpressBlogRSSItem;
import com.michaelb.homeworklong.fragment.RSSContentFragment;
import com.michaelb.homeworklong.fragment.RSSListFragment;
import com.michaelb.homeworklong.helper.BlogNewsDBHelper;
import com.michaelb.homeworklong.service.BlogNewsService;

public class MainActivity extends Activity implements RSSListFragment.ActivityListener {
    private static final String CLASS_NAME = String.valueOf(MainActivity.class);

    private RSSListFragment rssListFragment = null;

    private Menu menu = null;

    public void onListItemSelect(String itemURL) {
        RSSContentFragment dataContentFragment = findExistingContentFragment();
        if (dataContentFragment != null && dataContentFragment.isVisible()) {
            dataContentFragment.setWebViewURL(itemURL);
        } else {
            RSSContentFragment contentFragment = new RSSContentFragment();
            Bundle args = new Bundle();
            args.putString(RSSContentFragment.ARG_URL ,itemURL);
            contentFragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,contentFragment,AppValues.RSS_CONTENT_FRAGMENT_TAG)
                    .addToBackStack(null).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Log.i(CLASS_NAME, "onCreate called");
        //MenuItem item = (MenuItem) findViewById(R.id.action_save_article);
        //item.setVisible(false);
        //this.invalidateOptionsMenu();
        if (savedInstanceState != null) {
            return;
        }

        Intent resultIntent = new Intent(this, FreshNewsActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Application Started")
                .setContentText("Some notification text")
                .setContentIntent(resultPendingIntent);
        int notificationId = 001;


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
        //NotificationManager notificationManager =
        //        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //notificationManager.notify(notificationId, builder.build());
        Intent intent = new Intent(this, BlogNewsService.class);
        intent.setAction(BlogNewsServiceConstants.COMMAND_START);
        startService(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        //this.hideSaveMenuItem();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.action_refresh:
                Intent intent = new Intent(this, BlogNewsService.class);
                //stopService(intent);
                refreshRSSList();
                return true;
            case R.id.action_save_article:
                handleSaveArticleClick();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displaySaveMenuItem() {
        if ( this.menu != null ) {
            this.menu.findItem(R.id.action_save_article).setVisible(true);
            this.invalidateOptionsMenu();
        }
    }

    public void hideSaveMenuItem() {
        if ( this.menu != null ) {
            this.menu.findItem(R.id.action_save_article).setVisible(false);
            this.invalidateOptionsMenu();
        }
    }

    public void updateSaveItemMenu(boolean articleSaved) {
        if ( this.menu != null ) {
            MenuItem item = this.menu.findItem(R.id.action_save_article);
            if ( item != null ) {
                if ( articleSaved ) {
                    item.setIcon(R.drawable.ic_menu_remove);
                    item.setTitle(R.string.action_remove_article);
                } else {
                    item.setIcon(R.drawable.ic_menu_save);
                    item.setTitle(R.string.action_save_article);
                }
                this.invalidateOptionsMenu();
            }
        }
    }

    private boolean refreshRSSList() {
        //RSSListFragment rssListFragment = (RSSListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
        if (rssListFragment != null) {
            Log.i(CLASS_NAME, "refreshRSSList. RSS list fragment is not null");
            rssListFragment.refreshRSSListData();
        } else {
            Log.i(CLASS_NAME, "refreshRSSList. RSS list fragment is null");
        }
        return true;
    }

    private RSSContentFragment findExistingContentFragment() {
        RSSContentFragment dataContentFragment =
                (RSSContentFragment) getFragmentManager().findFragmentById(R.id.content_fragment);
        if ( dataContentFragment == null ) {
            dataContentFragment = (RSSContentFragment) getFragmentManager()
                    .findFragmentByTag(AppValues.RSS_CONTENT_FRAGMENT_TAG);
        }
        return dataContentFragment;
    }

    private void handleSaveArticleClick() {
        RSSContentFragment dataContentFragment = findExistingContentFragment();
        if (dataContentFragment != null && dataContentFragment.isVisible()) {
            String url = dataContentFragment.getCurrentURL();
            if ( url != null ) {
                WordpressBlogRSSItem rssItem = BlogNewsDBHelper.getInstance(getApplicationContext())
                        .findPostByURL(url);
                if (rssItem != null) {
                    if (rssItem.isPostSaved()) {

                    } else {
                        WordpressBlogPost blogPost = new WordpressBlogPost();
                        blogPost.setPostId(rssItem.getPostId());
                        blogPost.setTitle(rssItem.getTitle());
                        blogPost.setUrl(rssItem.getUrl());
                        WebView webView = (WebView) dataContentFragment.getView().findViewById(R.id.rss_content_view);
                        //blogPost.setPostHTML(webView.sa);
                    }

                }
            }
        }
    }

}
