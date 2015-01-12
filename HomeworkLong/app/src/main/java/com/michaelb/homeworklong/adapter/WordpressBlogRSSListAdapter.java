package com.michaelb.homeworklong.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.michaelb.homeworklong.R;
import com.michaelb.homeworklong.entities.HackerNewsRSSItem;
import com.michaelb.homeworklong.entities.WordpressBlogRSSItem;

/**
 * Created by michaelb on 11/12/14.
 */
public class WordpressBlogRSSListAdapter extends ArrayAdapter<WordpressBlogRSSItem> {

    private Context context;
    private int resourceLayoutId;
    private WordpressBlogRSSItem[] items;

    public WordpressBlogRSSListAdapter(Context context, int resource, WordpressBlogRSSItem[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.resourceLayoutId = resource;
        this.items = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resourceLayoutId, parent, false);
        }
        WordpressBlogRSSItem rssItem = items[position];
        TextView textViewItem = (TextView) convertView.findViewById(R.id.rss_list_item);
        textViewItem.setText(rssItem.getTitle());
        //textViewItem.setTag(rssItem.getUrl());
        return textViewItem;
    }
}
