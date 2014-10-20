package com.michaelb.homework1.fragments;

/**
 * Created by michaelb on 10/20/14.
 */

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;

import com.michaelb.homework1.R;


public class ControlFragment extends Fragment {

    public interface OnButtonClickListener {
        public void onButtonClick(String btnTag);
    }

    OnButtonClickListener mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonClickListener");
        }
    }

    public void onSelectorButtonClick (View view) {
        //do nothing for now
        Button sender = (Button) view;
        String btnTag = sender.getTag().toString();
        mCallback.onButtonClick(btnTag);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.control_fragment, container, false);
        Button button = (Button) view.findViewById(R.id.gold_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onSelectorButtonClick(v);
            }
        });
        button = (Button) view.findViewById(R.id.silver_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onSelectorButtonClick(v);
            }
        });
        button = (Button) view.findViewById(R.id.uranium_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onSelectorButtonClick(v);
            }
        });

        return view;
    }
}
