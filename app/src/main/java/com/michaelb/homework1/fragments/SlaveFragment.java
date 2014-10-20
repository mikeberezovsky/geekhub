package com.michaelb.homework1.fragments;

/**
 * Created by michaelb on 10/20/14.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import com.michaelb.homework1.R;

public class SlaveFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.slave_fragment, container, false);
    }

    public void setViewText(String text) {
        TextView textView = (TextView) getView().findViewById(R.id.slave_text_view);
        textView.setText(text);
    }

}
