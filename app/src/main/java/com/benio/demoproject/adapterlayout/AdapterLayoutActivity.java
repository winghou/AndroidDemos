package com.benio.demoproject.adapterlayout;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.benio.demoproject.R;
import com.benio.demoproject.common.ViewHolder;
import com.benio.demoproject.common.ViewHolderAdapter;

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
        int size = 500;
        List<String> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(String.valueOf(i));
        }
        mAdapter = new MyAdapter(data);
        showGrid();
    }

    private void showLinear() {
        AdapterLinearLayout linearLayout = new AdapterLinearLayout(this);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        linearLayout.setShowDividers(LinearLayoutCompat.SHOW_DIVIDER_MIDDLE);
        linearLayout.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayout.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position, long id) {
                Toast.makeText(AdapterLayoutActivity.this, "pos:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        linearLayout.setAdapter(mAdapter);
        mContainerView.removeAllViews();
        mContainerView.addView(linearLayout);
    }

    private void showGrid() {
        AdapterGridLayout gridLayout = new AdapterGridLayout(this);
        gridLayout.setColumnCount(3);
        gridLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        gridLayout.setAdapter(mAdapter);
        gridLayout.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position, long id) {
                Toast.makeText(AdapterLayoutActivity.this, "pos" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mContainerView.removeAllViews();
        mContainerView.addView(gridLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adapter_layout, menu);
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

    private static class MyAdapter extends ViewHolderAdapter {
        private static final int TYPE_TEXT = 0;
        private static final int TYPE_IMG = 1;

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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            if (viewType == TYPE_TEXT) {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_layout, parent, false);
            } else {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_layout_2, parent, false);
            }
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final int viewType = getItemViewType(position);
            if (viewType == TYPE_TEXT) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(mData.get(position));
            } else {
                ImageView imageView = (ImageView) holder.itemView;
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position % 2 == 0 ? TYPE_TEXT : TYPE_IMG;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }
}
