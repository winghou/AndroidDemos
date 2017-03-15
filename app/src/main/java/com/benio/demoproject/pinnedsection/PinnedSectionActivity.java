package com.benio.demoproject.pinnedsection;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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

        public void add(CharSequence charSequence) {
            List<CharSequence> list = new ArrayList<>(1);
            list.add(charSequence);
            addAll(list);
        }

        public void addAll(List<CharSequence> list) {
            generateDataSet(list);
            notifyDataSetChanged();
        }

        private void generateDataSet(List<CharSequence> list) {
            List<CharSequence> sectionList = null;
            Object sectionKey = null;
            for (int i = 0, count = list.size(); i < count; i++) {
                CharSequence item = list.get(i);
                if (sectionList == null) {
                    sectionList = new ArrayList<>();
                }
                sectionList.add(item);

                // 判断前一项与后一项首字母是否相同，不同则分组
                sectionKey = item.charAt(0);
                if (i + 1 >= count) { // 最后一项，将剩余的添加
                    add(sectionKey, sectionList);
                } else if (!sectionKey.equals(list.get(i + 1).charAt(0))) {
                    // 根据首字母获取缓存中的section下标
                    add(sectionKey, sectionList);
                    // 另开分组
                    sectionList = null;
                }
            }
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
            Object data = getItem(section, 0);
            viewHolder.mTextView.setText(data.toString().substring(0, 1));
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
