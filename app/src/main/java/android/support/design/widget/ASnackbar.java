package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.R;
import android.support.design.internal.SnackbarContentLayout;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import static android.support.design.widget.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;

/**
 * 与{@link Snackbar}一致。提供更多的自定义API
 * https://github.com/AndreiD/TSnackBar
 * https://github.com/HuanHaiLiuXin/SnackbarUtils/blob/master/MSnackBar/app/src/main/java/com/jet/msnackbar/SnackbarUtils.java
 * Created by zhangzhibin on 2018/6/30.
 */
public final class ASnackbar extends HackyBaseTransientBottomBar<ASnackbar> {
    /**
     * Show the Snackbar indefinitely. This means that the Snackbar will be displayed from the time
     * that is {@link #show() shown} until either it is dismissed, or another Snackbar is shown.
     *
     * @see #setDuration
     */
    public static final int LENGTH_INDEFINITE = BaseTransientBottomBar.LENGTH_INDEFINITE;

    /**
     * Show the Snackbar for a short period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_SHORT = BaseTransientBottomBar.LENGTH_SHORT;

    /**
     * Show the Snackbar for a long period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_LONG = BaseTransientBottomBar.LENGTH_LONG;

    /**
     * Callback class for {@link Snackbar} instances.
     * <p>
     * Note: this class is here to provide backwards-compatible way for apps written before
     * the existence of the base {@link BaseTransientBottomBar} class.
     *
     * @see BaseTransientBottomBar#addCallback(BaseCallback)
     */
    public static class Callback extends BaseCallback<ASnackbar> {
        /**
         * Indicates that the Snackbar was dismissed via a swipe.
         */
        public static final int DISMISS_EVENT_SWIPE = BaseCallback.DISMISS_EVENT_SWIPE;
        /**
         * Indicates that the Snackbar was dismissed via an action click.
         */
        public static final int DISMISS_EVENT_ACTION = BaseCallback.DISMISS_EVENT_ACTION;
        /**
         * Indicates that the Snackbar was dismissed via a timeout.
         */
        public static final int DISMISS_EVENT_TIMEOUT = BaseCallback.DISMISS_EVENT_TIMEOUT;
        /**
         * Indicates that the Snackbar was dismissed via a call to {@link #dismiss()}.
         */
        public static final int DISMISS_EVENT_MANUAL = BaseCallback.DISMISS_EVENT_MANUAL;
        /**
         * Indicates that the Snackbar was dismissed from a new Snackbar being shown.
         */
        public static final int DISMISS_EVENT_CONSECUTIVE = BaseCallback.DISMISS_EVENT_CONSECUTIVE;

        @Override
        public void onShown(ASnackbar sb) {
            // Stub implementation to make API check happy.
        }

        @Override
        public void onDismissed(ASnackbar transientBottomBar, @DismissEvent int event) {
            // Stub implementation to make API check happy.
        }
    }

    private int mGravity = Gravity.BOTTOM;

    private ASnackbar(@NonNull ViewGroup parent, @NonNull View content, @NonNull ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);
    }

    /**
     * Make a Snackbar to display a message
     * <p>
     * <p>Snackbar will try and find a parent view to hold Snackbar's view from the value given
     * to {@code view}. Snackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     * <p>
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Snackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *
     * @param view     The view to find a parent from.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or {@link
     *                 #LENGTH_LONG}
     */
    @NonNull
    public static ASnackbar make(@NonNull View view, @NonNull CharSequence text,
                                 @Duration int duration) {
        final ViewGroup parent = findSuitableParent(view);
        if (parent == null) {
            throw new IllegalArgumentException("No suitable parent found from the given view. "
                    + "Please provide a valid view.");
        }

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SnackbarContentLayout content =
                (SnackbarContentLayout) inflater.inflate(
                        R.layout.design_layout_snackbar_include, parent, false);
        final ASnackbar snackbar = new ASnackbar(parent, content, content);
        snackbar.setText(text);
        snackbar.setDuration(duration);
        return snackbar;
    }

    /**
     * Make a Snackbar to display a message.
     * <p>
     * <p>Snackbar will try and find a parent view to hold Snackbar's view from the value given
     * to {@code view}. Snackbar will walk up the view tree trying to find a suitable parent,
     * which is defined as a {@link CoordinatorLayout} or the window decor's content view,
     * whichever comes first.
     * <p>
     * <p>Having a {@link CoordinatorLayout} in your view hierarchy allows Snackbar to enable
     * certain features, such as swipe-to-dismiss and automatically moving of widgets like
     * {@link FloatingActionButton}.
     *
     * @param view     The view to find a parent from.
     * @param resId    The resource id of the string resource to use. Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or {@link
     *                 #LENGTH_LONG}
     */
    @NonNull
    public static ASnackbar make(@NonNull View view, @StringRes int resId, @Duration int duration) {
        return make(view, view.getResources().getText(resId), duration);
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

    @Override
    void animateViewIn() {
        switch (mGravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                animateViewInFromTop();
                break;
            case Gravity.CENTER_VERTICAL:
                animateViewInFromCenter();
                break;
            case Gravity.BOTTOM:
            default:
                super.animateViewIn();
                break;
        }
    }

    private void animateViewInFromTop() {
        if (Build.VERSION.SDK_INT >= 99) {
            final int viewHeight = -mView.getHeight();
            if (USE_OFFSET_API) {
                ViewCompat.offsetTopAndBottom(mView, viewHeight);
            } else {
                mView.setTranslationY(viewHeight);
            }
            final ValueAnimator animator = new ValueAnimator();
            animator.setIntValues(viewHeight, 0);
            animator.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            animator.setDuration(ANIMATION_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mContentViewCallback.animateContentIn(
                            ANIMATION_DURATION - ANIMATION_FADE_DURATION,
                            ANIMATION_FADE_DURATION);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    onViewShown();
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private int mPreviousAnimatedIntValue = viewHeight;

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    int currentAnimatedIntValue = (int) animator.getAnimatedValue();
                    if (USE_OFFSET_API) {
                        ViewCompat.offsetTopAndBottom(mView,
                                currentAnimatedIntValue - mPreviousAnimatedIntValue);
                    } else {
                        mView.setTranslationY(currentAnimatedIntValue);
                    }
                    mPreviousAnimatedIntValue = currentAnimatedIntValue;
                }
            });
            animator.start();
        } else {
            final Animation anim = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f
            );
            anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewShown();
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mView.startAnimation(anim);
        }
    }

    private void animateViewInFromCenter() {
        if (Build.VERSION.SDK_INT >= 12) {
            mView.setAlpha(1f);
            final ValueAnimator animator = new ValueAnimator();
            animator.setFloatValues(0f, 1f);
            animator.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            animator.setDuration(ANIMATION_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mContentViewCallback.animateContentIn(
                            ANIMATION_DURATION - ANIMATION_FADE_DURATION,
                            ANIMATION_FADE_DURATION);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    onViewShown();
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    float currentAnimatedValue = (float) animator.getAnimatedValue();
                    mView.setAlpha(currentAnimatedValue);
                }
            });
            animator.start();
        } else {
            final Animation anim = new AlphaAnimation(0f, 1f);
            anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewShown();
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mView.startAnimation(anim);
        }
    }

    @Override
    void animateViewOut(int event) {
        switch (mGravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                animateViewOutFromTop(event);
                break;
            case Gravity.CENTER_VERTICAL:
                animateViewOutFromCenter(event);
                break;
            case Gravity.BOTTOM:
            default:
                super.animateViewOut(event);
                break;
        }
    }

    private void animateViewOutFromTop(final int event) {
        if (Build.VERSION.SDK_INT >= 99) {
            final ValueAnimator animator = new ValueAnimator();
            animator.setIntValues(0, -mView.getHeight());
            animator.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            animator.setDuration(ANIMATION_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mContentViewCallback.animateContentOut(0, ANIMATION_FADE_DURATION);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    onViewHidden(event);
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private int mPreviousAnimatedIntValue = 0;

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    int currentAnimatedIntValue = (int) animator.getAnimatedValue();
                    if (USE_OFFSET_API) {
                        ViewCompat.offsetTopAndBottom(mView,
                                currentAnimatedIntValue - mPreviousAnimatedIntValue);
                    } else {
                        mView.setTranslationY(currentAnimatedIntValue);
                    }
                    mPreviousAnimatedIntValue = currentAnimatedIntValue;
                }
            });
            animator.start();
        } else {
            final TranslateAnimation anim = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f
            );
            anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewHidden(event);
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mView.startAnimation(anim);
        }
    }

    private void animateViewOutFromCenter(final int event) {
        if (Build.VERSION.SDK_INT >= 12) {
            final ValueAnimator animator = new ValueAnimator();
            animator.setFloatValues(1f, 0f);
            animator.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            animator.setDuration(ANIMATION_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mContentViewCallback.animateContentOut(0, ANIMATION_FADE_DURATION);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    onViewHidden(event);
                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    float currentAnimatedValue = (float) animator.getAnimatedValue();
                    mView.setAlpha(currentAnimatedValue);
                }
            });
            animator.start();
        } else {
            final Animation anim = new AlphaAnimation(1f, 0f);
            anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewHidden(event);
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mView.startAnimation(anim);
        }
    }

    /**
     * Update the text in this {@link Snackbar}.
     *
     * @param message The new text for this {@link BaseTransientBottomBar}.
     */
    @NonNull
    public ASnackbar setText(@NonNull CharSequence message) {
        final SnackbarContentLayout contentLayout = (SnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setText(message);
        return this;
    }

    /**
     * Update the text in this {@link Snackbar}.
     *
     * @param resId The new text for this {@link BaseTransientBottomBar}.
     */
    @NonNull
    public ASnackbar setText(@StringRes int resId) {
        return setText(getContext().getText(resId));
    }

    /**
     * Set the action to be displayed in this {@link BaseTransientBottomBar}.
     *
     * @param resId    String resource to display for the action
     * @param listener callback to be invoked when the action is clicked
     */
    @NonNull
    public ASnackbar setAction(@StringRes int resId, View.OnClickListener listener) {
        return setAction(getContext().getText(resId), listener);
    }

    /**
     * Set the action to be displayed in this {@link BaseTransientBottomBar}.
     *
     * @param text     Text to display for the action
     * @param listener callback to be invoked when the action is clicked
     */
    @NonNull
    public ASnackbar setAction(CharSequence text, final View.OnClickListener listener) {
        final SnackbarContentLayout contentLayout = (SnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getActionView();

        if (TextUtils.isEmpty(text) || listener == null) {
            tv.setVisibility(View.GONE);
            tv.setOnClickListener(null);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                    // Now dismiss the Snackbar
                    dispatchDismiss(BaseCallback.DISMISS_EVENT_ACTION);
                }
            });
        }
        return this;
    }

    /**
     * Sets the text color of the action specified in
     * {@link #setAction(CharSequence, View.OnClickListener)}.
     */
    @NonNull
    public ASnackbar setActionTextColor(ColorStateList colors) {
        final SnackbarContentLayout contentLayout = (SnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getActionView();
        tv.setTextColor(colors);
        return this;
    }

    /**
     * Sets the text color of the action specified in
     * {@link #setAction(CharSequence, View.OnClickListener)}.
     */
    @NonNull
    public ASnackbar setActionTextColor(@ColorInt int color) {
        final SnackbarContentLayout contentLayout = (SnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getActionView();
        tv.setTextColor(color);
        return this;
    }

    @NonNull
    public ASnackbar setTextColor(@ColorInt int color) {
        final SnackbarContentLayout contentLayout = (SnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setTextColor(color);
        return this;
    }

    @NonNull
    public ASnackbar setTextColor(ColorStateList colors) {
        final SnackbarContentLayout contentLayout = (SnackbarContentLayout) mView.getChildAt(0);
        final TextView tv = contentLayout.getMessageView();
        tv.setTextColor(colors);
        return this;
    }

    @NonNull
    public ASnackbar setGravity(int gravity) {
        View view = mView;
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) p).gravity = gravity;
            view.setLayoutParams(p);
            mGravity = gravity;
        } else if (p instanceof CoordinatorLayout.LayoutParams) {
            ((CoordinatorLayout.LayoutParams) p).gravity = gravity;
            view.setLayoutParams(p);
            mGravity = gravity;
        }
        return this;
    }

    @NonNull
    public ASnackbar setMargins(int left, int top, int right, int bottom) {
        View view = mView;
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) p).setMargins(left, top, right, bottom);
            view.setLayoutParams(p);
        }
        return this;
    }

    @NonNull
    public ASnackbar setBackgroundColor(@ColorInt int color) {
        mView.setBackgroundColor(color);
        return this;
    }

    @NonNull
    public ASnackbar setBackgroundResource(@DrawableRes int resId) {
        mView.setBackgroundResource(resId);
        return this;
    }

    @NonNull
    public ASnackbar setBackground(Drawable drawable) {
        ViewCompat.setBackground(mView, drawable);
        return this;
    }
}
