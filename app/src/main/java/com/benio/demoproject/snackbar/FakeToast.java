package com.benio.demoproject.snackbar;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.AnimatedTransientBar;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by benio on 2018/7/14.
 */
public class FakeToast extends AnimatedTransientBar<FakeToast> {
    /**
     * Show the Toast indefinitely. This means that the Toast will be displayed from the time
     * that is {@link #show() shown} until either it is dismissed, or another Toast is shown.
     *
     * @see #setDuration
     */
    public static final int LENGTH_INDEFINITE = BaseTransientBottomBar.LENGTH_INDEFINITE;

    /**
     * Show the Toast for a short period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_SHORT = BaseTransientBottomBar.LENGTH_SHORT;

    /**
     * Show the Toast for a long period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_LONG = BaseTransientBottomBar.LENGTH_LONG;

    /**
     * Callback class for {@link FakeToast} instances.
     * <p>
     * Note: this class is here to provide backwards-compatible way for apps written before
     * the existence of the base {@link BaseTransientBottomBar} class.
     *
     * @see BaseTransientBottomBar#addCallback(BaseCallback)
     */
    public static class Callback extends BaseCallback<FakeToast> {
        /**
         * Indicates that the FakeToast was dismissed via a swipe.
         */
        public static final int DISMISS_EVENT_SWIPE = BaseCallback.DISMISS_EVENT_SWIPE;
        /**
         * Indicates that the FakeToast was dismissed via an action click.
         */
        public static final int DISMISS_EVENT_ACTION = BaseCallback.DISMISS_EVENT_ACTION;
        /**
         * Indicates that the FakeToast was dismissed via a timeout.
         */
        public static final int DISMISS_EVENT_TIMEOUT = BaseCallback.DISMISS_EVENT_TIMEOUT;
        /**
         * Indicates that the FakeToast was dismissed via a call to {@link #dismiss()}.
         */
        public static final int DISMISS_EVENT_MANUAL = BaseCallback.DISMISS_EVENT_MANUAL;
        /**
         * Indicates that the FakeToast was dismissed from a new FakeToast being shown.
         */
        public static final int DISMISS_EVENT_CONSECUTIVE = BaseCallback.DISMISS_EVENT_CONSECUTIVE;

        @Override
        public void onShown(FakeToast toast) {
            // Stub implementation to make API check happy.
        }

        @Override
        public void onDismissed(FakeToast toast, @DismissEvent int event) {
            // Stub implementation to make API check happy.
        }
    }

    private TextView mMessageView;
    private int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;

    private FakeToast(@NonNull ViewGroup parent, @NonNull View content,
                      @NonNull ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);
        setAnimation(android.R.anim.fade_in, android.R.anim.fade_out);

        final View view = getView();
        final ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (lp instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) lp).gravity = mGravity;
            final Resources resources = view.getResources();
            final int yOffset = resources.getDimensionPixelSize(resources.
                    getIdentifier("toast_y_offset", "dimen", "android"));
            ((ViewGroup.MarginLayoutParams) lp).bottomMargin += yOffset;
        }
        view.setLayoutParams(lp);
    }

    public static FakeToast make(@NonNull View view, @NonNull CharSequence text,
                                 @Duration int duration) {
        final ViewGroup parent = findSuitableParent(view);
        if (parent == null) {
            throw new IllegalArgumentException("No suitable parent found from the given view. "
                    + "Please provide a valid view.");
        }

        // Inflate the android toast layout
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final int toastLayoutRes = parent.getResources().getIdentifier("transient_notification",
                "layout", "android");
        final View content = inflater.inflate(toastLayoutRes, parent, false);
        final ContentViewCallback callback = new ContentViewCallback() {
            @Override
            public void animateContentIn(int delay, int duration) {
            }

            @Override
            public void animateContentOut(int delay, int duration) {
            }
        };
        final FakeToast toast = new FakeToast(parent, content, callback);
        toast.mMessageView = (TextView) content.findViewById(android.R.id.message);
        toast.mMessageView.setText(text);
        toast.setDuration(duration);

        // 1.Get toast content view background and set it into SnackbarLayout
        // 2.Remove toast content view background
        // 3.Remove SnackbarLayout padding and elevation
        final View containerView = toast.getView();
        ViewCompat.setBackground(containerView, content.getBackground());
        ViewCompat.setBackground(content, null);
        containerView.setPadding(0, 0, 0, 0);
        ViewCompat.setElevation(containerView, 0);
        return toast;
    }

    @NonNull
    public static FakeToast make(@NonNull View view, @StringRes int resId, @Duration int duration) {
        return make(view, view.getResources().getText(resId), duration);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            //It's a toast, so we should disable features in CoordinatorLayout
            /*if (view instanceof CoordinatorLayout) {
                // We've found a CoordinatorLayout, use it
                return (ViewGroup) view;
            } else */
            if (view instanceof FrameLayout) {
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

    @Override
    protected int animateAt() {
        return mGravity;
    }

    public int getGravity() {
        return mGravity;
    }

    @NonNull
    public FakeToast setGravity(int gravity) {
        final View view = getView();
        final ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p instanceof FrameLayout.LayoutParams
                && ((FrameLayout.LayoutParams) p).gravity != gravity) {
            ((FrameLayout.LayoutParams) p).gravity = gravity;
            // If we change gravity, reset bottomMargin.
            // Because we change bottomMargin in constructor.
            ((FrameLayout.LayoutParams) p).bottomMargin = 0;
            view.setLayoutParams(p);
            mGravity = gravity;
        }
        return this;
    }

    /**
     * Update the text in this {@link FakeToast}.
     *
     * @param message The new text for this {@link BaseTransientBottomBar}.
     */
    @NonNull
    public FakeToast setText(@NonNull CharSequence message) {
        final TextView tv = mMessageView;
        tv.setText(message);
        return this;
    }

    /**
     * Update the text in this {@link FakeToast}.
     *
     * @param resId The new text for this {@link BaseTransientBottomBar}.
     */
    @NonNull
    public FakeToast setText(@StringRes int resId) {
        return setText(getContext().getText(resId));
    }

    @NonNull
    public FakeToast setTextColor(@ColorInt int color) {
        final TextView tv = mMessageView;
        tv.setTextColor(color);
        return this;
    }

    @NonNull
    public FakeToast setTextColor(ColorStateList colors) {
        final TextView tv = mMessageView;
        tv.setTextColor(colors);
        return this;
    }
}