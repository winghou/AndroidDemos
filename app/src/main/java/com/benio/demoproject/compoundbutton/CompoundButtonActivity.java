package com.benio.demoproject.compoundbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

import com.benio.demoproject.R;

public class CompoundButtonActivity extends AppCompatActivity {
    private static final String TAG = "CompoundButtonActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compound_button);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        RadioGroup radioGroup2 = (RadioGroup) findViewById(R.id.radio_group_2);
        RadioGroup radioGroup3 = (RadioGroup) findViewById(R.id.radio_group_3);
        RadioGroup radioGroup4 = (RadioGroup) findViewById(R.id.radio_group_4);

        new RadioGroupHelper().addItems("房", "票", "餐")
                .setCheckedItem(1)
                .attachTo(radioGroup);

        new RadioGroupHelper().addItems("房", "票")
                .addItem("餐")
                .setCheckedItem(2)
                .setRadioLayoutResource(R.layout.item_radio_group)
                .attachTo(radioGroup2);

        new TabGroupHelper().addItems("房", "票", "餐")
                .attachTo(radioGroup3);

        new TabGroupHelper().addItems("房", "票", "餐")
                .setCheckedItem(0)
                .setRadioLayoutResource(R.layout.item_tab_group)
                .attachTo(radioGroup4);

        CompoundButtonGroup compoundButtonGroup = (CompoundButtonGroup) findViewById(R.id.compound_group);
        compoundButtonGroup.setOnCheckedChangeListener(new CompoundButtonGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButtonGroup group, int checkedId, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged() called with: checkedId = [" + checkedId + "], isChecked = [" + isChecked + "]");
            }
        });
    }
}
