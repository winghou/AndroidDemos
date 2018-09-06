package com.benio.demoproject.snackbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.benio.demoproject.R;

public class ToastActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);

        findViewById(R.id.btn_duration_short).setOnClickListener(this);
        findViewById(R.id.btn_duration_long).setOnClickListener(this);
        findViewById(R.id.btn_duration_custom).setOnClickListener(this);
        findViewById(R.id.btn_duration_indefinite).setOnClickListener(this);
        findViewById(R.id.btn_gravity_top).setOnClickListener(this);
        findViewById(R.id.btn_gravity_center).setOnClickListener(this);
        findViewById(R.id.btn_gravity_bottom).setOnClickListener(this);
        findViewById(R.id.btn_bg_color).setOnClickListener(this);
        findViewById(R.id.btn_bg_radius).setOnClickListener(this);
        findViewById(R.id.btn_anim_slide).setOnClickListener(this);
        findViewById(R.id.btn_anim_fade).setOnClickListener(this);
        findViewById(R.id.btn_anim_no).setOnClickListener(this);
        findViewById(R.id.btn_margin).setOnClickListener(this);
        findViewById(R.id.btn_text_color).setOnClickListener(this);
        findViewById(R.id.btn_padding).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FakeToast toast = FakeToast.make(v, "Hello, Toast", FakeToast.LENGTH_SHORT);
        switch (v.getId()) {
            case R.id.btn_duration_short:
                toast.setDuration(FakeToast.LENGTH_SHORT);
                break;
            case R.id.btn_duration_long:
                toast.setDuration(FakeToast.LENGTH_LONG);
                break;
            case R.id.btn_duration_custom:
                toast.setDuration(5000);
                break;
            case R.id.btn_duration_indefinite:
                toast.setDuration(FakeToast.LENGTH_INDEFINITE);
                break;
            case R.id.btn_text_color:
                toast.setTextColor(Color.YELLOW);
                break;
            case R.id.btn_gravity_top:
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                break;
            case R.id.btn_gravity_center:
                toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                break;
            case R.id.btn_gravity_bottom:
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                break;
            case R.id.btn_bg_color:
                toast.setBackgroundColor(Color.BLUE);
                break;
            case R.id.btn_bg_radius:
                toast.setBackgroundResource(R.drawable.snackbar_bg_with_corners_and_stroke);
                break;
            case R.id.btn_anim_slide:
                toast.setAnimation(AnimationUtils.makeInAnimation(this, true),
                        AnimationUtils.makeOutAnimation(this, true)
                );
                break;
            case R.id.btn_anim_fade:
                toast.setAnimation(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btn_anim_no:
                toast.setAnimationEnabled(false);
                break;
            case R.id.btn_margin:
                int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
                        getResources().getDisplayMetrics());
                // 先设置gravity再设置margin，因为setGravity方法会将margin清零
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .setMargins(margin, margin, margin, margin);
                break;
            case R.id.btn_padding:
                int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
                        getResources().getDisplayMetrics());
                toast.setPadding(pad, pad, pad, pad);
                break;
        }
        toast.show();
    }
}
