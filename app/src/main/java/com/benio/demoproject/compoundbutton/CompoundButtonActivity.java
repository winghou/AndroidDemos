package com.benio.demoproject.compoundbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.benio.demoproject.R;

public class CompoundButtonActivity extends AppCompatActivity {

    private CompoundButtonGroup mSingleGroup;
    private CompoundButtonGroup mMultipleGroup;
    private CompoundButtonGroup mMultipleGroup2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compound_button);
        mSingleGroup = (CompoundButtonGroup) findViewById(R.id.cbg_single);
        mMultipleGroup = (CompoundButtonGroup) findViewById(R.id.cbg_multiple);
        mMultipleGroup2 = (CompoundButtonGroup) findViewById(R.id.cbg_multiple_2);
        mSingleGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged() called with: buttonView = [" + buttonView + "], isChecked = [" + isChecked + "]");
            }
        });
        mMultipleGroup2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged() called with: buttonView = [" + buttonView + "], isChecked = [" + isChecked + "]");
            }
        });
    }

    private static final String TAG = "xxx";

    public void clearCheck(View view) {
        mSingleGroup.clearCheck();
        mMultipleGroup.clearCheck();
        mMultipleGroup2.clearCheck();
    }

    public void toggle(View view) {
        mSingleGroup.toggle(R.id.rb_single_option1);
        mMultipleGroup.toggle(R.id.cb_multiple_option1);
        mMultipleGroup.toggle(R.id.cb_multiple_option2);
        mMultipleGroup2.toggle(R.id.cb_multiple2_option1);
        mMultipleGroup2.toggle(R.id.cb_multiple2_option2);
    }

    public void singleMode(View view) {
        mSingleGroup.setCheckMode(CompoundButtonGroup.CHECK_MODE_SINGLE);
        mMultipleGroup.setCheckMode(CompoundButtonGroup.CHECK_MODE_SINGLE);
        mMultipleGroup2.setCheckMode(CompoundButtonGroup.CHECK_MODE_SINGLE);
    }

    public void multipleMode(View view) {
        mSingleGroup.setCheckMode(CompoundButtonGroup.CHECK_MODE_MULTIPLE);
        mMultipleGroup.setCheckMode(CompoundButtonGroup.CHECK_MODE_MULTIPLE);
        mMultipleGroup2.setCheckMode(CompoundButtonGroup.CHECK_MODE_MULTIPLE);
    }
}
