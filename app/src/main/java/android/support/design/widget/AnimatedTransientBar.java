package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.SimpleAnimationListener;
import android.view.animation.TranslateAnimation;

import static android.support.design.widget.AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR;

/**
 * Created by benio on 2018/7/12.
 */
public abstract class AnimatedTransientBar<B extends BaseTransientBottomBar<B>> extends HackyBaseTransientBottomBar<B> {
    private Animation mInAnimation;
    private Animation mOutAnimation;
    private boolean mAnimationEnabled = true;

    protected AnimatedTransientBar(@NonNull ViewGroup parent, @NonNull View content,
                                   @NonNull ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);
    }

    protected abstract int animateAt();

    @Override
    void animateViewIn() {
        final Animation anim = mInAnimation;
        if (anim != null) {
            anim.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewShown();
                }
            });
            mView.startAnimation(anim);
            return;
        }

        final int animateAt = animateAt();
        switch (animateAt & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                animateViewInAtTop();
                break;
            case Gravity.CENTER_VERTICAL:
                animateViewInAtCenter();
                break;
            case Gravity.BOTTOM:
            default:
                super.animateViewIn();
                break;
        }
    }

    private void animateViewInAtTop() {
        if (Build.VERSION.SDK_INT >= 12) {
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
            anim.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewShown();
                }
            });
            mView.startAnimation(anim);
        }
    }

    private void animateViewInAtCenter() {
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
            anim.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewShown();
                }
            });
            mView.startAnimation(anim);
        }
    }

    @Override
    void animateViewOut(final int event) {
        final Animation anim = mOutAnimation;
        if (anim != null) {
            anim.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewHidden(event);
                }
            });
            mView.startAnimation(anim);
            return;
        }

        final int animateAt = animateAt();
        switch (animateAt & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                animateViewOutAtTop(event);
                break;
            case Gravity.CENTER_VERTICAL:
                animateViewOutAtCenter(event);
                break;
            case Gravity.BOTTOM:
            default:
                super.animateViewOut(event);
                break;
        }
    }

    private void animateViewOutAtTop(final int event) {
        if (Build.VERSION.SDK_INT >= 12) {
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
            anim.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewHidden(event);
                }
            });
            mView.startAnimation(anim);
        }
    }

    private void animateViewOutAtCenter(final int event) {
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
            anim.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewHidden(event);
                }
            });
            mView.startAnimation(anim);
        }
    }

    @Override
    boolean shouldAnimate() {
        return super.shouldAnimate() && mAnimationEnabled;
    }

    @NonNull
    public B setAnimationEnabled(boolean animationEnabled) {
        mAnimationEnabled = animationEnabled;
        return (B) this;
    }

    public boolean isAnimationEnabled() {
        return mAnimationEnabled;
    }

    @NonNull
    public B setAnimation(@AnimRes int in, @AnimRes int out) {
        final Context context = getContext();
        return setAnimation(
                android.view.animation.AnimationUtils.loadAnimation(context, in),
                android.view.animation.AnimationUtils.loadAnimation(context, out)
        );
    }

    @NonNull
    public B setAnimation(Animation in, Animation out) {
        mAnimationEnabled = true;
        mInAnimation = in;
        mOutAnimation = out;
        return (B) this;
    }

    @NonNull
    public B setMargins(int margin) {
        return setMargins(margin, margin, margin, margin);
    }

    @NonNull
    public B setMargins(int left, int top, int right, int bottom) {
        final View view = mView;
        final ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) p;
            if (mlp.leftMargin != left || mlp.topMargin != top
                    || mlp.rightMargin != right || mlp.bottomMargin != bottom) {
                mlp.setMargins(left, top, right, bottom);
                view.setLayoutParams(mlp);
            }
        }
        return (B) this;
    }

    @NonNull
    public B setPadding(int pad) {
        return setPadding(pad, pad, pad, pad);
    }

    @NonNull
    public B setPadding(int left, int top, int right, int bottom) {
        mView.setPadding(left, top, right, bottom);
        return (B) this;
    }

    @NonNull
    public B setBackgroundColor(@ColorInt int color) {
        mView.setBackgroundColor(color);
        return (B) this;
    }

    @NonNull
    public B setBackgroundResource(@DrawableRes int resId) {
        mView.setBackgroundResource(resId);
        return (B) this;
    }

    @NonNull
    public B setBackground(Drawable drawable) {
        ViewCompat.setBackground(mView, drawable);
        return (B) this;
    }
}
