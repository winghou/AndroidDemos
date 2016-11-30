package com.benio.demoproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.benio.demoproject.adapterlayout.AdapterLinearLayoutActivity;
import com.benio.demoproject.customview.CustomViewActivity;
import com.benio.demoproject.progress.ProgressActivity;
import com.benio.demoproject.span.SpanActivity;
import com.benio.demoproject.web.WebViewActivity;

public class MainActivity extends AppCompatActivity {

    DemoInfo[] mInfos = {
            new DemoInfo("CustomView", CustomViewActivity.class),
            new DemoInfo("ProgressView", ProgressActivity.class),
            new DemoInfo("AdapterLinearLayout", AdapterLinearLayoutActivity.class),
            new DemoInfo("Span", SpanActivity.class),
            new DemoInfo("WebView", WebViewActivity.class)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);
        final BaseAdapter adapter = new MyAdapter(mInfos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DemoInfo demoInfo = (DemoInfo) adapter.getItem(position);
                startActivity(new Intent(MainActivity.this, demoInfo.activityClass));
            }
        });
    }

    private static class DemoInfo {
        String title;
        Class<?> activityClass;

        public DemoInfo(String title, Class<?> activityClass) {
            this.title = title;
            this.activityClass = activityClass;
        }
    }

    private static class MyAdapter extends BaseAdapter {
        private DemoInfo[] mInfos;

        public MyAdapter(DemoInfo[] infos) {
            mInfos = infos;
        }

        @Override
        public int getCount() {
            return mInfos.length;
        }

        @Override
        public Object getItem(int position) {
            return mInfos[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;
            if (convertView == null) {
                textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
            } else {
                textView = (TextView) convertView;
            }

            textView.setText(mInfos[position].title);
            return textView;
        }
    }
}
