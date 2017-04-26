package com.benio.demoproject.adapterlayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.benio.demoproject.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterLayoutActivity extends AppCompatActivity {
    private static final String TAG = "Adapter";
    private MyAdapter mAdapter;
    private ScrollView mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter_layout);
        mContainerView = (ScrollView) findViewById(R.id.scrollView);
        int size = 50;
        List<String> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(String.valueOf(i));
        }
        mAdapter = new MyAdapter(data);

        showLinear();
    }

    private void showLinear() {
        AdapterLinearLayout linearLayout = new AdapterLinearLayout(this);
        linearLayout.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayout.setAdapter(mAdapter);
        mContainerView.removeAllViews();
        mContainerView.addView(linearLayout);
    }

    private void showGrid() {
        AdapterGridLayout gridLayout = new AdapterGridLayout(this);
        gridLayout.setColumnCount(2);
        gridLayout.setAdapter(mAdapter);
        mContainerView.removeAllViews();
        mContainerView.addView(gridLayout);
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
        } else if (id == R.id.linear) {
            showLinear();
            return true;
        } else if (id == R.id.grid) {
            showGrid();
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
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_layout, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mTextView.setText(mData.get(position));
            viewHolder.itemView.setBackgroundColor(position % 2 == 0 ? Color.TRANSPARENT : Color.BLUE);
            return convertView;
        }
    }

    private static class ViewHolder {
        public final View itemView;
        TextView mTextView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            this.mTextView = (TextView) itemView.findViewById(R.id.tv_adapter_layout);
        }
    }
}
