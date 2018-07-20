package com.progressdemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class AnimProgressView extends View {

    private static final String TAG = "AnimProgressView";

    protected Context mContext;
    private static final float MAX_DEFAULT = 100f;

    private static final float START_PROGRESS_DEFAULT = 0f;
    private static final float PROGRESS_DEFAULT = 0f;
    private static final int ANIM_DURATION_DEFAULT = 4 * 1000;
    private static final int PROGRESS_COLOR_DEFAULT = Color.RED;
    private static final int PROGRESS_BACKGROUND_COLOR_DEFAULT = Color.TRANSPARENT;
    private static final int STROKE_WIDTH_DEFAULT = 10;
    private static final int STROKE_COLOR_DEFAULT = Color.WHITE;

    protected static final float ANGLE_MAX = 360f;

    protected float progress;
    protected float max;
    protected float startProgress;
    protected int strokeWidth;

    protected int progressColor;
    protected int progressBackgroundColor;
    protected int strokeColor;
    protected int animDuration;


    private AnimatorListener animatorListener;
    private ObjectAnimator progressAnimation;
    protected boolean shouldShowStateText;

    public AnimProgressView(Context context) {
        this(context, null);
    }

    public AnimProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initView(attrs, defStyleAttr);

    }

    private void initView(AttributeSet attrs, int defStyleAttr) {

        TypedArray attributes = mContext.obtainStyledAttributes(attrs, R.styleable.AnimProgressView, defStyleAttr, 0);

        max = attributes.getFloat(R.styleable.AnimProgressView_progress_max, MAX_DEFAULT);
        startProgress = attributes.getFloat(R.styleable.AnimProgressView_start_progress, START_PROGRESS_DEFAULT);
        progress = attributes.getFloat(R.styleable.AnimProgressView_progress, PROGRESS_DEFAULT);
        strokeWidth = attributes.getDimensionPixelSize(R.styleable.AnimProgressView_apv_stroke_width, STROKE_WIDTH_DEFAULT);
        animDuration = attributes.getInt(R.styleable.AnimProgressView_anim_duration, ANIM_DURATION_DEFAULT);


        progressColor = attributes.getColor(R.styleable.AnimProgressView_progress_color, PROGRESS_COLOR_DEFAULT);
        progressBackgroundColor = attributes.getColor(R.styleable.AnimProgressView_progress_background_color, PROGRESS_BACKGROUND_COLOR_DEFAULT);
        strokeColor = attributes.getColor(R.styleable.AnimProgressView_apv_stroke_color, STROKE_COLOR_DEFAULT);
        attributes.recycle();

    }

    public void showFailStateText() {
        cancelAnim();
        this.shouldShowStateText = true;
        invalidate();
        this.progress = startProgress;
    }
    public synchronized float getProgress() {
        return progress;
    }

    public float getMax() {
        return max;
    }

    public int getAnimDuration() {
        return animDuration;
    }

    public AnimProgressView setAnimDuration(int animDuration) {
        if (animDuration < 0) {
            throw new IllegalArgumentException("animDuration must more than 0");
        }
        this.animDuration = animDuration;
        return this;
    }

    public float getStartProgress() {
        return startProgress;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public int getProgressBackgroundColor() {
        return progressBackgroundColor;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public AnimProgressView setProgress(float progress) {

        if (progress > max) {
            throw new IllegalArgumentException("progress must less than max progress value, max progress default values is 100");
        }

        if (progress < startProgress) {
            throw new IllegalArgumentException("progress must more than startProgress");
        }

        this.progress = progress;
        invalidate();
        return this;
    }

    private AnimProgressView resetProgress() {
        this.progress = startProgress;
        invalidate();
        return this;
    }

    private AnimProgressView fullProgress() {
        this.progress = max;
        invalidate();
        return this;
    }

    public AnimProgressView setMax(float max) {
        if (max < 0) {
            throw new IllegalArgumentException("max must more than 0");
        }
        this.max = max;
        return this;
    }

    public AnimProgressView setStartProgress(float startProgress) {

        if (startProgress < 0) {
            throw new IllegalArgumentException("startProgress must more than 0");
        }

        if (startProgress > max) {
            throw new IllegalArgumentException("startProgress must less than max progress value, max progress default values is 100");
        }
        this.startProgress = startProgress;
        return this;
    }

    public AnimProgressView setStrokeWidth(int strokeWidth) {
        if (strokeWidth < 0) {
            throw new IllegalArgumentException("strokeWidth must more than 0");
        }
        if (strokeWidth > Math.max(getWidth(), getHeight())) {
            throw new IllegalArgumentException("strokeWidth must less than min value of view width&height");
        }
        this.strokeWidth = strokeWidth;
        invalidate();
        return this;
    }

    public AnimProgressView setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        invalidate();
        return this;
    }

    public AnimProgressView setProgressBackgroundColor(int progressBackgroundColor) {
        this.progressBackgroundColor = progressBackgroundColor;
        invalidate();
        return this;
    }

    public AnimProgressView setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        invalidate();
        return this;
    }

    public void fullAnimProgress() {

        animProgress(max);
    }

    public void fullAnimProgress(Interpolator interpolator) {

        animProgress(max, interpolator);
    }


    public void fullAnimProgressInfinite() {

        animProgress(max, Animation.INFINITE, Animation.RESTART, new LinearInterpolator());
    }

    public void fullAnimProgressInfinite(Interpolator interpolator) {

        animProgress(max, Animation.INFINITE, Animation.RESTART, interpolator);
    }


    public void animProgress(float progress) {

        animProgress(progress, (int) startProgress, Animation.RESTART, new LinearInterpolator());
    }

    public void animProgress(float progress, Interpolator interpolator) {

        animProgress(progress, (int) startProgress, Animation.RESTART, interpolator);
    }


    private void animProgress(final float progress, int repeatCount, int repeatMode, Interpolator interpolator) {

        progressAnimation = ObjectAnimator.ofFloat(this, "progress", getProgress(), progress);

        progressAnimation.setDuration((long) (animDuration * (Math.abs(getProgress() - progress) / max)));// 动画执行时间

        progressAnimation.setInterpolator(interpolator);
        progressAnimation.setRepeatCount(repeatCount);
        progressAnimation.setRepeatMode(repeatMode);
        progressAnimation.start();
        progressAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

                if (animatorListener != null) {
                    animatorListener.onAnimationStart(animator, getProgress());
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (animatorListener != null) {
                    animatorListener.onAnimationEnd(animator, getProgress());
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

                Log.i(TAG, "onAnimationCancel: ");

                AnimProgressView.this.shouldShowStateText = false;
                if (animatorListener != null) {
                    animatorListener.onAnimationCancel(animator, getProgress());
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                if (animatorListener != null) {
                    animatorListener.onAnimationRepeat(animator, getProgress());
                }
            }
        });


    }

    public void addAnimatorListener(AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }


    private void cancelAnimAtFullProgress() {
        this.progress = max;
        if (progressAnimation != null && progressAnimation.isRunning()) {
            progressAnimation.cancel();
        }
    }

    public void cancelAnim() {
        resetProgress();
        if (progressAnimation != null && progressAnimation.isRunning()) {
            progressAnimation.cancel();
        }
    }

    public boolean isAnimRunning() {
        return progressAnimation != null && progressAnimation.isRunning();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isAnimPaused() {
        return progressAnimation != null && progressAnimation.isPaused();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pauseAnim() {
        if (progressAnimation != null && !progressAnimation.isPaused()) {
            progressAnimation.pause();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resumeAnim() {
        if (progressAnimation != null && progressAnimation.isPaused()) {
            progressAnimation.resume();
        }
    }

    public void reverseAnim() {
        if (progressAnimation != null) {
            progressAnimation.reverse();
        }
    }


    public interface AnimatorListener {
        void onAnimationStart(Animator animator, float progress);

        void onAnimationEnd(Animator animator, float progress);

        void onAnimationRepeat(Animator animator, float progress);

        void onAnimationCancel(Animator animator, float progress);
    }


}
