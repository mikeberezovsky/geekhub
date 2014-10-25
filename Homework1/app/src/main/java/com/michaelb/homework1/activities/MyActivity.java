package com.michaelb.homework1.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.michaelb.homework1.R;
import com.michaelb.homework1.fragments.ControlFragment;
import com.michaelb.homework1.fragments.SlaveFragment;


public class MyActivity extends Activity implements ControlFragment.OnButtonClickListener {

    public void onButtonClick(String btnTag) {
        SlaveFragment slaveFragment = (SlaveFragment) getFragmentManager().findFragmentById(R.id.slave_fragment);
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(btnTag+"_button_data", "string", packageName);
        String buttonDecText = getResources().getString(resId);
        slaveFragment.setViewText(buttonDecText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
