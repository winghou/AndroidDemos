package com.benio.demoproject.badge;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.benio.demoproject.R;

public class BadgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                badge();
            }
        });
    }

    private void badge() {
        TextView textView = (TextView) findViewById(R.id.text);
        Drawable[] drawables = textView.getCompoundDrawables();
        Drawable drawableTop = drawables[1];
        BadgeDrawable badgeDrawable = BadgeDrawable.wrap(drawableTop);
        badgeDrawable.setBadgeVisible(true);
        badgeDrawable.setCornerRadius(14);
        badgeDrawable.setColor(Color.RED);
        badgeDrawable.setTextColor(Color.WHITE);
        badgeDrawable.setTextSize(18);
        badgeDrawable.setText("99");
        textView.setCompoundDrawablesWithIntrinsicBounds(drawables[0], badgeDrawable, drawables[2], drawables[3]);
    }
}
