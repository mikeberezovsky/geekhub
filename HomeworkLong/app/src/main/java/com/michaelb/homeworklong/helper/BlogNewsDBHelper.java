package com.michaelb.homeworklong.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.michaelb.homeworklong.entities.WordpressBlogRSSItem;
import com.michaelb.homeworklong.entities.WordpressBlogPost;
import com.michaelb.homeworklong.entities.WordpressBlogRSSItem.WordpressBlogRSSEntry;
import com.michaelb.homeworklong.entities.WordpressBlogPost.WordpressBlogPostEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelb on 1/15/15.
 */
public class BlogNewsDBHelper extends SQLiteOpenHelper {

    private static BlogNewsDBHelper dbHelperInstance;

    private static final String CLASS_NAME = String.valueOf(BlogNewsDBHelper.class);
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String CREATE_RSS_ENTRIES_TABLE =
            " CREATE TABLE " + WordpressBlogRSSEntry.TABLE_NAME + " ( " +
            WordpressBlogRSSEntry.COLUMN_ID + INT_TYPE + " PRIMARY KEY," +
            WordpressBlogRSSEntry.COLUMN_POST_ID + INT_TYPE + " UNIQUE," +
            WordpressBlogRSSEntry.COLUMN_POST_TITLE + TEXT_TYPE + COMMA_SEP +
            WordpressBlogRSSEntry.COLUMN_POST_URL + TEXT_TYPE + COMMA_SEP +
            WordpressBlogRSSEntry.COLUMN_POST_AUTHOR + TEXT_TYPE + COMMA_SEP +
            WordpressBlogRSSEntry.COLUMN_POST_DATE_PUBLISHED + INT_TYPE +
            " );";

    public static final String CREATE_BLOG_POST_TABLE =
            " CREATE TABLE " + WordpressBlogPostEntry.TABLE_NAME + " ( " +
                    WordpressBlogPostEntry.COLUMN_ID + INT_TYPE + " PRIMARY KEY," +
                    WordpressBlogPostEntry.COLUMN_POST_ID + INT_TYPE + " UNIQUE," +
                    WordpressBlogPostEntry.COLUMN_POST_TITLE + TEXT_TYPE + COMMA_SEP +
                    WordpressBlogPostEntry.COLUMN_POST_URL + TEXT_TYPE + COMMA_SEP +
                    WordpressBlogPostEntry.COLUMN_POST_HTML + TEXT_TYPE + COMMA_SEP +
                    WordpressBlogPostEntry.COLUMN_POST_GZIPPED + TEXT_TYPE +
                    " );";

    private String[] rssColumns = {
            WordpressBlogRSSEntry.COLUMN_ID,
            WordpressBlogRSSEntry.COLUMN_POST_ID,
            WordpressBlogRSSEntry.COLUMN_POST_TITLE,
            WordpressBlogRSSEntry.COLUMN_POST_URL,
            WordpressBlogRSSEntry.COLUMN_POST_AUTHOR,
            WordpressBlogRSSEntry.COLUMN_POST_DATE_PUBLISHED
    };


    private static final String DATABASE_NAME = "blog_news.db";
    private static final int DATABASE_VERSION = 1;

    public static BlogNewsDBHelper getInstance(Context context) {
        if (dbHelperInstance == null) {
            dbHelperInstance = new BlogNewsDBHelper(context.getApplicationContext());
        }
        return dbHelperInstance;
    }

    private BlogNewsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_RSS_ENTRIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(BlogNewsDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS " + WordpressBlogRSSEntry.TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS " + WordpressBlogPostEntry.TABLE_NAME);
        //onCreate(db);
    }

    public List<WordpressBlogRSSItem> getAllRSSItems() {
        List<WordpressBlogRSSItem> readItems = new ArrayList<WordpressBlogRSSItem>();
        SQLiteDatabase db = getReadableDatabase();

        String sortOrder = WordpressBlogRSSEntry.COLUMN_POST_DATE_PUBLISHED + " DESC";

        Cursor cursor = db.query(WordpressBlogRSSEntry.TABLE_NAME, rssColumns, null, null, null, null, sortOrder);
        cursor.moveToFirst();
        int i = 0;
        while (!cursor.isAfterLast()) {
            try {
                readItems.add(rssItemFromCursor(cursor));
                Log.i(CLASS_NAME, "getting saved record #"+Integer.toString(i++));
            } catch (Exception e) {
                Log.e(CLASS_NAME, "Error getting saved RSS news item.", e);
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return readItems;
    }

    public List<WordpressBlogRSSItem> getRSSItemsAfterTimestamp(long ts) {
        List<WordpressBlogRSSItem> readItems = new ArrayList<WordpressBlogRSSItem>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = WordpressBlogRSSEntry.COLUMN_POST_DATE_PUBLISHED + " > ?";
        String[] selectionArgs = { String.valueOf(ts) };
        String sortOrder = WordpressBlogRSSEntry.COLUMN_POST_DATE_PUBLISHED + " DESC";

        Cursor cursor = db.query(
                WordpressBlogRSSEntry.TABLE_NAME,
                rssColumns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.moveToFirst();
        int i = 0;
        while (!cursor.isAfterLast()) {
            try {
                readItems.add(rssItemFromCursor(cursor));
                Log.i(CLASS_NAME, "getting saved record #"+Integer.toString(i++));
            } catch (Exception e) {
                Log.e(CLASS_NAME, "Error getting saved RSS news item.", e);
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return readItems;
    }

    public void saveRSSItems(List<WordpressBlogRSSItem> rssItems) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (WordpressBlogRSSItem item : rssItems) {
                ContentValues values = new ContentValues();
                values.put(WordpressBlogRSSEntry.COLUMN_POST_ID, item.getPostId());
                values.put(WordpressBlogRSSEntry.COLUMN_POST_TITLE, item.getTitle());
                values.put(WordpressBlogRSSEntry.COLUMN_POST_URL, item.getUrl());
                values.put(WordpressBlogRSSEntry.COLUMN_POST_AUTHOR, item.getAuthor());
                values.put(WordpressBlogRSSEntry.COLUMN_POST_DATE_PUBLISHED, item.getDatePublishedTimestamp());
                db.insert(WordpressBlogRSSEntry.TABLE_NAME,null,values);
                Log.i(CLASS_NAME, "Post '" + item.getTitle() + "' added to DB");
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
    }

    public long getMostRecentRSSTimestamp() {
        long ts = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[] tableColumns = new String[] {"MAX("+WordpressBlogRSSEntry.COLUMN_POST_DATE_PUBLISHED+") AS newest"};
        String maxStatement = "newest=(SELECT MAX("+WordpressBlogRSSEntry.COLUMN_POST_DATE_PUBLISHED+") FROM "+ WordpressBlogRSSEntry.TABLE_NAME + ")";
        try {
            Cursor cursor = db.query(WordpressBlogRSSEntry.TABLE_NAME, tableColumns, null, null, null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                ts = cursor.getLong(cursor.getColumnIndex("newest"));
                Log.i(CLASS_NAME,"Max news timestamp: "+ Long.toString(ts));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(CLASS_NAME, "Error getting max timestamp for RSS news item.", e);
        }
        db.close();
        return ts;
    }

    private WordpressBlogRSSItem rssItemFromCursor(Cursor cursor) {
        WordpressBlogRSSItem rssItem = new WordpressBlogRSSItem();
        rssItem.setId(cursor.getLong(cursor.getColumnIndexOrThrow(WordpressBlogRSSEntry.COLUMN_ID)));
        rssItem.setPostId(cursor.getString(cursor.getColumnIndexOrThrow(WordpressBlogRSSEntry.COLUMN_POST_ID)));
        rssItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(WordpressBlogRSSEntry.COLUMN_POST_TITLE)));
        rssItem.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(WordpressBlogRSSEntry.COLUMN_POST_URL)));
        rssItem.setDatePublished(cursor.getLong(cursor.getColumnIndexOrThrow(WordpressBlogRSSEntry.COLUMN_POST_DATE_PUBLISHED)));
        rssItem.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(WordpressBlogRSSEntry.COLUMN_POST_AUTHOR)));
        Log.i(CLASS_NAME, "rssItemFromCursor for entry '"+rssItem.getTitle()+"' processed.");
        return rssItem;
    }

}
