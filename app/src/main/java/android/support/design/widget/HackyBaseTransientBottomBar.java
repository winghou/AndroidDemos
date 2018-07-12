package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.R;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.lang.reflect.Field;

import static android.support.design.widget.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;

/**
 * Created by zhangzhibin on 2018/7/12.
 */
public class HackyBaseTransientBottomBar<B extends BaseTransientBottomBar<B>> extends BaseTransientBottomBar<B> {
    private static final Handler sHackyHandler;

    // On JB/KK versions of the platform sometimes View.setTranslationY does not
    // result in layout / draw pass, and CoordinatorLayout relies on a draw pass to
    // happen to sync vertical positioning of all its child views
    static final boolean USE_OFFSET_API = (Build.VERSION.SDK_INT >= 16)
            && (Build.VERSION.SDK_INT <= 19);

    static {
        sHackyHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case MSG_SHOW:
                        ((HackyBaseTransientBottomBar) message.obj).showView();
                        return true;
                    case MSG_DISMISS:
                        ((HackyBaseTransientBottomBar) message.obj).hackyHideView(message.arg1);
                        return true;
                }
                return false;
            }
        });

        try {
            Field field = BaseTransientBottomBar.class.getDeclaredField("sHandler");
            field.setAccessible(true);
            field.set(null, sHackyHandler);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    final ContentViewCallback mContentViewCallback;

    protected HackyBaseTransientBottomBar(@NonNull ViewGroup parent, @NonNull View content, @NonNull ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);
        mContentViewCallback = contentViewCallback;
    }

    /**
     * copy from {@link BaseTransientBottomBar#hideView(int)}
     *
     * @param event
     */
    private void hackyHideView(@BaseCallback.DismissEvent final int event) {
        if (shouldAnimate() && mView.getVisibility() == View.VISIBLE) {
            animateViewOut(event);
        } else {
            // If anims are disabled or the view isn't visible, just call back now
            onViewHidden(event);
        }
    }

    /**
     * copy from {@link BaseTransientBottomBar#animateViewOut(int)}
     *
     * @param event
     */
    void animateViewOut(final int event) {
        if (Build.VERSION.SDK_INT >= 12) {
            final ValueAnimator animator = new ValueAnimator();
            animator.setIntValues(0, mView.getHeight());
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
            final Animation anim = AnimationUtils.loadAnimation(mView.getContext(),
                    R.anim.design_snackbar_out);
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
}
