package com.michaelb.homework3.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.michaelb.homework3.R;

/**
 * Created by michaelb on 11/3/14.
 */
public class DataListFragment extends Fragment {

    public interface ActivityListener {
        public void onListItemSelect(String btnTag);
    }

    private ListView entriesList;
    private String[] listItems;



    ActivityListener activityListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityListener = (ActivityListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnButtonClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.data_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        entriesList = (ListView) view.findViewById(R.id.list_items);
        listItems = getResources().getStringArray(R.array.sample_list_entries);
        entriesList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.fragment_list_item, listItems));
        OnItemClickListener listener = new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activityListener.onListItemSelect(parent.getItemAtPosition(position).toString());
            }
        };
        entriesList.setOnItemClickListener(listener);
    }
}
