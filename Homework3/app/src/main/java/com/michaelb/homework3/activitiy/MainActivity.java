package com.michaelb.homework3.activitiy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.michaelb.homework3.R;
import com.michaelb.homework3.fragment.DataContentFragment;
import com.michaelb.homework3.fragment.DataListFragment;


public class MainActivity extends Activity implements DataListFragment.ActivityListener {

    public void onListItemSelect(String itemText) {
        DataContentFragment dataContentFragment = (DataContentFragment) getFragmentManager().findFragmentById(R.id.content_fragment);
        if (dataContentFragment != null && dataContentFragment.isVisible()) {
                dataContentFragment.setTextviewText(itemText);
        } else {
            DataContentFragment contentFragment = new DataContentFragment();
            Bundle args = new Bundle();
            args.putString(DataContentFragment.ARG_TEXT ,itemText);
            contentFragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,contentFragment).addToBackStack(null).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            DataListFragment dataListFragment = new DataListFragment();
            dataListFragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(R.id.fragment_container,dataListFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
