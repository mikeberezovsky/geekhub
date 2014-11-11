package com.michaelb.homework3.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelb.homework3.R;

/**
 * Created by michaelb on 11/3/14.
 */
public class DataContentFragment extends Fragment {
    public final static String ARG_TEXT = "text";
    private String currentText = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            currentText = savedInstanceState.getString(ARG_TEXT);
        }
        return inflater.inflate(R.layout.data_content_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            setTextviewText(args.getString(ARG_TEXT));
        } else {
            if (currentText != null) {
                setTextviewText(currentText);
            } else {
                setTextviewText("For now it is null");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_TEXT,currentText);
    }

    public void setTextviewText(String text) {
        View fragmentView = getView();
        TextView tv = (TextView) fragmentView.findViewById(R.id.list_text_view);
        tv.setText(text);
        currentText = text;
        getActivity().setTitle(currentText);
    }
}
