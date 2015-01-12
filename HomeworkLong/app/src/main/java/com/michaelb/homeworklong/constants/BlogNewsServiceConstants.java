package com.michaelb.homeworklong.constants;

/**
 * Created by michaelb on 1/11/15.
 */
public class BlogNewsServiceConstants {
    public static final long NEWS_CHECK_INTERVAL = 60000;//60 000 millisecomds = 60 seconds
    public static final int START_SERVICE_NOTIFICATION_ID = 001;
    public static final int FRESH_NEWS_NOTIFICATION_ID = 002;
    public static final String COMMAND_START = "com.michaelb.homeworklong.constants.start";
    public static final String COMMAND_GET_NEWS = "com.michaelb.homeworklong.constants.get_news";
    public static final String COMMAND_NOTIFY_FRESH_NEWS = "com.michaelb.homeworklong.constants.notify_fresh_news";
    public static final String COMMAND_UPDATE_FRESH_TIMESTAMP = "com.michaelb.homeworklong.constants.update_timestamp";
    public static final String EXTRA_KEY_TIMESTAMP = "com.michaelb.homeworklong.constants.timestampKey";
    public static final String EXTRA_KEY_AFTER_TIMESTAMP = "com.michaelb.homeworklong.constants.afterTimestampKey";
    public static final String EXTRA_KEY_NEWS_COUNT = "com.michaelb.homeworklong.constants.newsCountKey";
}
