package com.benio.demoproject.snackbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.ASnackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.benio.demoproject.R;

public class SnackbarActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackbar);

        findViewById(R.id.btn_duration_short).setOnClickListener(this);
        findViewById(R.id.btn_duration_long).setOnClickListener(this);
        findViewById(R.id.btn_duration_indefinite).setOnClickListener(this);
        findViewById(R.id.btn_duration_custom).setOnClickListener(this);
        findViewById(R.id.btn_text_color).setOnClickListener(this);
        findViewById(R.id.btn_action_color).setOnClickListener(this);
        findViewById(R.id.btn_gravity_top).setOnClickListener(this);
        findViewById(R.id.btn_gravity_center).setOnClickListener(this);
        findViewById(R.id.btn_gravity_bottom).setOnClickListener(this);
        findViewById(R.id.btn_bg_color).setOnClickListener(this);
        findViewById(R.id.btn_bg_radius).setOnClickListener(this);
        findViewById(R.id.btn_anim_slide).setOnClickListener(this);
        findViewById(R.id.btn_anim_fade).setOnClickListener(this);
        findViewById(R.id.btn_anim_no).setOnClickListener(this);
        findViewById(R.id.btn_margin).setOnClickListener(this);
        findViewById(R.id.btn_drawable_horizontal).setOnClickListener(this);
        findViewById(R.id.btn_drawable_vertical).setOnClickListener(this);
        findViewById(R.id.btn_drawable_padding).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ASnackbar aSnackbar = ASnackbar.make(v, "Hello", ASnackbar.LENGTH_LONG)
                .setAction("action", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SnackbarActivity.this, "Hello world~",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        switch (v.getId()) {
            case R.id.btn_duration_short:
                aSnackbar.setDuration(ASnackbar.LENGTH_SHORT);
                break;
            case R.id.btn_duration_long:
                aSnackbar.setDuration(ASnackbar.LENGTH_LONG);
                break;
            case R.id.btn_duration_indefinite:
                aSnackbar.setDuration(ASnackbar.LENGTH_INDEFINITE);
                break;
            case R.id.btn_duration_custom:
                aSnackbar.setDuration(5000);
                break;
            case R.id.btn_text_color:
                aSnackbar.setTextColor(Color.YELLOW);
                break;
            case R.id.btn_action_color:
                aSnackbar.setActionTextColor(Color.GREEN);
                break;
            case R.id.btn_gravity_top:
                aSnackbar.setGravity(Gravity.TOP);
                break;
            case R.id.btn_gravity_center:
                aSnackbar.setGravity(Gravity.CENTER);
                break;
            case R.id.btn_gravity_bottom:
                aSnackbar.setGravity(Gravity.BOTTOM);
                break;
            case R.id.btn_bg_color:
                aSnackbar.setBackgroundColor(Color.GRAY);
                break;
            case R.id.btn_bg_radius:
                aSnackbar.setBackgroundResource(R.drawable.snackbar_bg_with_corners_and_stroke);
                break;
            case R.id.btn_anim_slide:
                aSnackbar.setAnimation(AnimationUtils.makeInAnimation(this, true),
                        AnimationUtils.makeOutAnimation(this, true)
                );
                break;
            case R.id.btn_anim_fade:
                aSnackbar.setAnimation(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btn_anim_no:
                aSnackbar.setAnimationEnabled(false);
                break;
            case R.id.btn_margin:
                int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
                        getResources().getDisplayMetrics());
                aSnackbar.setMargins(margin, margin, margin, margin);
                break;
            case R.id.btn_drawable_horizontal:
                aSnackbar.setDrawablesWithIntrinsicBounds(R.mipmap.tab_bar_message_icon, 0,
                        R.mipmap.tab_bar_message_icon, 0);
                break;
            case R.id.btn_drawable_vertical:
                aSnackbar.setDrawablesWithIntrinsicBounds(0, R.mipmap.tab_bar_message_icon,
                        0, R.mipmap.tab_bar_message_icon);
                break;
            case R.id.btn_drawable_padding:
                int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                        getResources().getDisplayMetrics());
                aSnackbar.setDrawablesWithIntrinsicBounds(R.mipmap.tab_bar_message_icon, 0,
                        0, 0)
                        .setDrawablePadding(pad);
                break;
        }
        aSnackbar.show();
    }
}
