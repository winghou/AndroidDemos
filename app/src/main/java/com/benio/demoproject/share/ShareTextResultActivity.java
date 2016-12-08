package com.benio.demoproject.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.benio.demoproject.R;

public class ShareTextResultActivity extends AppCompatActivity {
    private static final String TAG = "ShareTextResultActivity";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_text);
        Log.d(TAG, "onCreate() this: " + toString() + ",taskId: " + getTaskId());
        mTextView = (TextView) findViewById(R.id.text);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
        Log.d(TAG, "onNewIntent: " + ",taskId: " + getTaskId());
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        }
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            mTextView.setText(sharedText);
        }
    }
}
