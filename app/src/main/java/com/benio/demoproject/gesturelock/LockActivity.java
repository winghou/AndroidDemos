package com.benio.demoproject.gesturelock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.benio.demoproject.R;

import java.util.List;

public class LockActivity extends AppCompatActivity {
    private LockPatternView mLockPatternView;
    private LockPatternView mIndicatorView;

    private static final String TAG = "xxxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        mIndicatorView = (LockPatternView) findViewById(R.id.indicator);
        // just as an indicator
        mIndicatorView.disableInput();
        mLockPatternView = (LockPatternView) findViewById(R.id.lock);
        mLockPatternView.setOnPatternListener(new LockPatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {
                Log.d(TAG, "onPatternStart() called");
            }

            @Override
            public void onPatternCleared() {
                Log.d(TAG, "onPatternCleared() called");
            }

            @Override
            public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
                Log.d(TAG, "onPatternCellAdded() called with: pattern = [" + LockPatternUtils.patternToString(pattern) + "]");
            }

            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                Log.d(TAG, "onPatternDetected() called with: pattern = [" + pattern.toString() + "," + LockPatternUtils.patternToString(pattern) + "]");
                mIndicatorView.setPattern(LockPatternView.DisplayMode.Correct, pattern);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_lock) {
            return true;
        } else if (id == R.id.action_unlock) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
