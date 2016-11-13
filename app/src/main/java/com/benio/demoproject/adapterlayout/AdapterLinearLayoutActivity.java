package com.benio.demoproject.adapterlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.benio.demoproject.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterLinearLayoutActivity extends AppCompatActivity {
    private static final String TAG = "Adapter";
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_linear_layout);

        int size = 50;
        List<String> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(String.valueOf(i));
        }

        AdapterLinearLayout linearLayout = (AdapterLinearLayout) findViewById(R.id.list);
        mAdapter = new MyAdapter(data);
        linearLayout.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adapter_linearlayout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            mAdapter.add("new value");
            mAdapter.notifyDataSetChanged();
            return true;
        } else if (id == R.id.del) {
            mAdapter.delete(0);
            mAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class MyAdapter extends BaseAdapter {

        private List<String> mData;

        public MyAdapter(List<String> data) {
            mData = data;
        }

        @Override
        public int getCount() {
            return mData != null ? mData.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public void add(String elem) {
            mData.add(0, elem);
        }

        public void delete(int position) {
            mData.remove(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView: " + position);
            TextView textView = new TextView(parent.getContext());
            textView.setTextSize(20);
            textView.setPadding(10, 10, 10, 10);
            textView.setText(mData.get(position));
            return textView;
        }
    }
}
