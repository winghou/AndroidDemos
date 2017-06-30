package com.benio.demoproject.pinnedsection;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.benio.demoproject.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PinnedSectionActivity extends AppCompatActivity {
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned_section);

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.add(String.valueOf(mAdapter.getCount() - mAdapter.getSectionCount()));
            }
        });
        findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener() {
            char charKey = '0';

            @Override
            public void onClick(View v) {
                Object key = charKey;
                if (mAdapter.remove(key)) {
                    charKey++;
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
        ListView listView = (ListView) findViewById(R.id.list);
        mAdapter = new MyAdapter();
        mAdapter.addAll(generateData());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = mAdapter.getItem(position);
                Toast.makeText(PinnedSectionActivity.this, "section:" + mAdapter.isSectionHeader(position)
                        + "pos:" + position + ", obj:" + obj, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<CharSequence> generateData() {
        int size = 59;
        List<CharSequence> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(String.valueOf(i));
        }
        Collections.shuffle(data);
        return data;
    }

    private static class MyAdapter extends BaseSectionAdapter<CharSequence> {
        private KeyCreator<CharSequence> mKeyCreator = new KeyCreator<CharSequence>() {
            @Override
            public Object getKey(CharSequence item) {
                return item.charAt(0);
            }
        };

        public void add(CharSequence charSequence) {
            List<CharSequence> list = new ArrayList<>(1);
            list.add(charSequence);
            addAll(list);
            notifyDataSetChanged();
        }

        public void addAll(List<CharSequence> list) {
            addAll(list, mKeyCreator);
        }

        @Override
        public View getItemView(int section, int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pinned_section, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Object data = getItem(section, position);
            viewHolder.mTextView.setText(data.toString());
            return convertView;
        }

        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pinned_section, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Object data = getKey(section);
            viewHolder.mTextView.setText(data.toString());
            viewHolder.mTextView.setBackgroundColor(Color.GREEN);
            return convertView;
        }

        static class ViewHolder {
            TextView mTextView;

            public ViewHolder(View itemView) {
                mTextView = (TextView) itemView.findViewById(R.id.tv_pinned_section);
            }
        }
    }
}
