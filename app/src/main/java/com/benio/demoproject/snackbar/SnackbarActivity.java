package com.benio.demoproject.snackbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.ASnackbar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.benio.demoproject.R;

public class SnackbarActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
        findViewById(R.id.btn_drawable).setOnClickListener(this);
        findViewById(R.id.btn_drawable_padding).setOnClickListener(this);
        findViewById(R.id.btn_text_gravity_left).setOnClickListener(this);
        findViewById(R.id.btn_text_gravity_center).setOnClickListener(this);
        findViewById(R.id.btn_text_gravity_right).setOnClickListener(this);
        findViewById(R.id.btn_padding).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "FakeToast")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == Menu.FIRST) {
            Intent intent = new Intent(SnackbarActivity.this, ToastActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                removeInsetEdge(v, aSnackbar);
                break;
            case R.id.btn_gravity_center:
                aSnackbar.setGravity(Gravity.CENTER);
                removeInsetEdge(v, aSnackbar);
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
            case R.id.btn_drawable:
                aSnackbar.setDrawableWithIntrinsicBound(android.R.drawable.ic_dialog_map)
                        .setTextGravity(Gravity.CENTER_VERTICAL);
                break;
            case R.id.btn_drawable_padding:
                int pad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                        getResources().getDisplayMetrics());
                aSnackbar.setDrawableWithIntrinsicBound(android.R.drawable.ic_dialog_map)
                        .setDrawablePadding(pad)
                        .setTextGravity(Gravity.CENTER_VERTICAL);
                break;
            case R.id.btn_text_gravity_left:
                aSnackbar.setTextGravity(Gravity.START);
                break;
            case R.id.btn_text_gravity_center:
                aSnackbar.setTextGravity(Gravity.CENTER);
                break;
            case R.id.btn_text_gravity_right:
                aSnackbar.setTextGravity(Gravity.END);
                break;
            case R.id.btn_padding:
                aSnackbar.setPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        8, getResources().getDisplayMetrics()));
                break;
        }
        aSnackbar.show();
    }

    private static void removeInsetEdge(View v, final ASnackbar snackbar) {
        ViewGroup viewGroup = findSuitableParent(v);
        if (viewGroup instanceof CoordinatorLayout) {
            viewGroup.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
                @Override
                public void onChildViewAdded(View parent, View child) {
                    if (child == snackbar.getView()) {
                        CoordinatorLayout.LayoutParams clp =
                                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                        clp.insetEdge = Gravity.NO_GRAVITY;
                    }
                }

                @Override
                public void onChildViewRemoved(View parent, View child) {
                }
            });
        }
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                // We've found a CoordinatorLayout, use it
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    // If we've hit the decor content view, then we didn't find a CoL in the
                    // hierarchy, so use it.
                    return (ViewGroup) view;
                } else {
                    // It's not the content view but we'll use it as our fallback
                    fallback = (ViewGroup) view;
                }
            }

            if (view != null) {
                // Else, we will loop and crawl up the view hierarchy and try to find a parent
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
        return fallback;
    }
}
