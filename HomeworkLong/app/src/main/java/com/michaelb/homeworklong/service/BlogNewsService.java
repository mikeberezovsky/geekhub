package com.michaelb.homeworklong.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.michaelb.homeworklong.R;
import com.michaelb.homeworklong.activity.FreshNewsActivity;
import com.michaelb.homeworklong.asynctask.BlogNewsRequestTask;
import com.michaelb.homeworklong.constants.AppValues;
import com.michaelb.homeworklong.constants.BlogNewsServiceConstants;
import com.michaelb.homeworklong.constants.FeedURLs;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by michaelb on 1/11/15.
 */
public class BlogNewsService extends Service {

    private final IBinder localBinder = new LocalBinder();

    private long mostFreshNewsTimestamp = 0;

    private Context appContext = null;

    private NotificationManager notificationManager = null;

    private Timer newsCheckTimer = null;

    public class LocalBinder extends Binder {
        BlogNewsService getService() {
            return BlogNewsService.this;
        }
    }

    class ReadNewsTimerTask extends TimerTask {
        public void run() {
            Intent intent = new Intent(appContext, BlogNewsService.class);
            intent.setAction(BlogNewsServiceConstants.COMMAND_GET_NEWS);
            intent.putExtra(BlogNewsServiceConstants.EXTRA_KEY_AFTER_TIMESTAMP, mostFreshNewsTimestamp);
            appContext.startService(intent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public void onCreate() {
        appContext = getApplicationContext();
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        newsCheckTimer = new Timer(true);
        newsCheckTimer.scheduleAtFixedRate(new ReadNewsTimerTask(),
                BlogNewsServiceConstants.NEWS_CHECK_INTERVAL,
                BlogNewsServiceConstants.NEWS_CHECK_INTERVAL);

        // Display a notification about us starting. We put an icon in the status bar.
        showNotification();
        //showFreshNewsNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("BlogNewsService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        String serviceCommand = intent.getAction();
        if ( BlogNewsServiceConstants.COMMAND_START.equals(serviceCommand) ) {
            Log.i("BlogNewsService", "Received command START");
        } else if (BlogNewsServiceConstants.COMMAND_UPDATE_FRESH_TIMESTAMP.equals(serviceCommand)) {
            Log.i("BlogNewsService", "Received command UPDATE_FRESH_TIMESTAMP");
            long freshTimestamp = intent.getLongExtra(BlogNewsServiceConstants.EXTRA_KEY_TIMESTAMP, 0);
            if (freshTimestamp > mostFreshNewsTimestamp) {
                mostFreshNewsTimestamp = freshTimestamp;
                Log.i("BlogNewsService", "timestamp set to:"+ new Date(freshTimestamp));
            } else {
                Log.i("BlogNewsService", "timestamp is not updated because it is same or older then already existing");
            }
        } else if ( BlogNewsServiceConstants.COMMAND_GET_NEWS.equals(serviceCommand) ) {
            Log.i("BlogNewsService", "Received command GET_NEWS");
            if (isWifiConnected()) {
                new BlogNewsRequestTask(appContext, mostFreshNewsTimestamp).execute(FeedURLs.testblogFeedURL);
            } else {
                Log.i("BlogNewsService", "GET_NEWS not refreshing because of internet problems");
            }
        } else if (BlogNewsServiceConstants.COMMAND_NOTIFY_FRESH_NEWS.equals(serviceCommand)) {
            int newsCount = intent.getIntExtra(BlogNewsServiceConstants.EXTRA_KEY_NEWS_COUNT,0);
            if (newsCount > 0) {
                showFreshNewsNotification(newsCount);
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        notificationManager.cancel(BlogNewsServiceConstants.START_SERVICE_NOTIFICATION_ID);
        if (newsCheckTimer != null) {
            newsCheckTimer.cancel();
            newsCheckTimer.purge();
        }
        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    private void showFreshNewsNotification(int newsCount) {
        Intent resultIntent = new Intent(this, FreshNewsActivity.class);
        resultIntent.putExtra(AppValues.CUTOFF_NEWS_TIMESTAMP_KEY, mostFreshNewsTimestamp);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.fresh_news_notification_title))
                .setContentText(getString(R.string.fresh_news_notification_text, newsCount))
                .setContentIntent(resultPendingIntent);
        notificationManager.notify(BlogNewsServiceConstants.FRESH_NEWS_NOTIFICATION_ID, builder.build());
    }

    private void showNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.local_service_started_title))
                .setContentText(getString(R.string.local_service_started));
        notificationManager.notify(BlogNewsServiceConstants.START_SERVICE_NOTIFICATION_ID, builder.build());
    }

    private boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return ((netInfo != null) && netInfo.isConnected());
    }

}
