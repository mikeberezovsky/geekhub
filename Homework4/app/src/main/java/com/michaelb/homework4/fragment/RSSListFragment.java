package com.michaelb.homework4.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelb.homework4.R;

/**
 * Created by michaelb on 11/11/14.
 */
public class RSSListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rss_list_fragment, container, false);
    }

    public void setTextviewText(String text) {
        View fragmentView = getView();
        TextView tv = (TextView) fragmentView.findViewById(R.id.list_text_view);
        tv.setText(text);
    }
}
