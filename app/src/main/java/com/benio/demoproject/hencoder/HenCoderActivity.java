package com.benio.demoproject.hencoder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.benio.demoproject.R;
import com.benio.demoproject.hencoder.practice1.PracticeDraw1Fragment;
import com.benio.demoproject.hencoder.practice2.PracticeDraw2Fragment;
import com.benio.demoproject.hencoder.practice3.PracticeDraw3Fragment;
import com.benio.demoproject.hencoder.practice4.PracticeDraw4Fragment;

public class HenCoderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hen_coder);

        replaceFragment(PracticeDraw1Fragment.newInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_hen_coder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.action_1:
                fragment = PracticeDraw1Fragment.newInstance();
                break;
            case R.id.action_2:
                fragment = PracticeDraw2Fragment.newInstance();
                break;
            case R.id.action_3:
                fragment = PracticeDraw3Fragment.newInstance();
                break;
            case R.id.action_4:
                fragment = PracticeDraw4Fragment.newInstance();
                break;
        }

        boolean handled = fragment != null;
        if (handled) {
            replaceFragment(fragment);
        }

        return handled || super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
