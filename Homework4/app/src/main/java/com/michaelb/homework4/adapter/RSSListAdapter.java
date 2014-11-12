package com.michaelb.homework4.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.michaelb.homework4.R;
import com.michaelb.homework4.entities.HackerNewsRSSItem;

import java.util.List;

/**
 * Created by michaelb on 11/12/14.
 */
public class RSSListAdapter extends ArrayAdapter<HackerNewsRSSItem> {

    private Context context;
    private int resourceLayoutId;
    HackerNewsRSSItem[] items;

    public RSSListAdapter(Context context, int resource, HackerNewsRSSItem[] objects) {
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
        HackerNewsRSSItem rssItem = items[position];
        TextView textViewItem = (TextView) convertView.findViewById(R.id.rss_list_item);
        textViewItem.setText(rssItem.getTitle());
        textViewItem.setTag(rssItem.getUrl());
        return textViewItem;
    }
}
