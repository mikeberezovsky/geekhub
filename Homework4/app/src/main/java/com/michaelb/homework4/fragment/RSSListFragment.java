package com.michaelb.homework4.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.michaelb.homework4.R;
import com.michaelb.homework4.adapter.RSSListAdapter;
import com.michaelb.homework4.entities.HackerNewsRSSItem;

import java.util.List;

/**
 * Created by michaelb on 11/11/14.
 */
public class RSSListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rss_list_fragment, container, false);
    }

    public void initRSSList(List<HackerNewsRSSItem> items) {
        View fragmentView = getView();
        ListView lv = (ListView) fragmentView.findViewById(R.id.rss_list_view);
        lv.setAdapter(new RSSListAdapter(getActivity(), R.layout.rss_list_item, items.toArray(new HackerNewsRSSItem[items.size()])));
    }
}
