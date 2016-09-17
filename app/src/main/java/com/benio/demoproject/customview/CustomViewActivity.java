package com.benio.demoproject.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.benio.demoproject.R;

import java.util.ArrayList;
import java.util.List;

public class CustomViewActivity extends AppCompatActivity {

    ViewGroup mViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

        mViewGroup = (ViewGroup) findViewById(R.id.container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_custom_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mViewGroup.removeAllViews();
        if (id == R.id.action_pie) {
            List<PieView.PieData> pieDatas = new ArrayList<>();
            pieDatas.add(new PieView.PieData("item 1", 120));
            pieDatas.add(new PieView.PieData("item 2", 220));
            pieDatas.add(new PieView.PieData("item 3", 320));
            pieDatas.add(new PieView.PieData("item 4", 420));
            pieDatas.add(new PieView.PieData("item 5", 160));
            PieView pieView = new PieView(this);
            pieView.setPieDatas(pieDatas);
            mViewGroup.addView(pieView);
            return true;
        } else if (id == R.id.action_custom1) {
            mViewGroup.addView(new CustomView1(this));
            return true;
        } else if (id == R.id.action_custom2) {
            mViewGroup.addView(new CustomView2(this));
            return true;
        } else if (id == R.id.action_custom3) {
            mViewGroup.addView(new CustomView3(this));
            return true;
        } else if (id == R.id.action_custom4) {
            mViewGroup.addView(new CustomView4(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
