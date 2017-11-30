package com.sample.customviewdesign.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

import com.sample.customviewdesign.R;

// Alternative loading effect
public class InflateLoadingCircle extends View {

    private AnimatorSet mAnimatorSet;
    private ShapeDrawable mCircleDrawable;
    private int mCircleMax = 0;
    private int mCircleMin = 0;
    private int mStrokeThick = 0;
    private int mStrokeThin = 0;
    private int mStrokeColor = 0;
    private int mAnimDuration = 0;

    public InflateLoadingCircle(Context context) {
        this(context, null);
    }

    public InflateLoadingCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InflateLoadingCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.InflateLoadingCircle, 0, 0);
            mCircleMax = a.getDimensionPixelSize(R.styleable.InflateLoadingCircle_circleMax, 0);
            mCircleMin = a.getDimensionPixelSize(R.styleable.InflateLoadingCircle_circleMin, 0);
            mStrokeThick = a.getDimensionPixelSize(R.styleable.InflateLoadingCircle_strokeThick, 20);
            mStrokeThin = a.getDimensionPixelSize(R.styleable.InflateLoadingCircle_strokeThin, 10);
            mStrokeColor = a.getColor(R.styleable.InflateLoadingCircle_strokeColor, 0x0AED3D);
            mAnimDuration = a.getInt(R.styleable.InflateLoadingCircle_animDurationMs, 1000);
        } finally {
            a.recycle();
        }

        mCircleDrawable = new ShapeDrawable(new OvalShape());
        // If the color isn't set, the shape uses black as the default.
        mCircleDrawable.getPaint().setColor(mStrokeColor);
        mCircleDrawable.getPaint().setStyle(Paint.Style.STROKE);
        // If the bounds aren't set, the shape can't be drawn.
        //mCircleDrawable.setBounds(0, 0, width, height);
        invalidate();
    }

    public void startAnimation() {
        if (mAnimatorSet == null) {
            mAnimatorSet = new AnimatorSet();
        }
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(getStrokeAnimation(), getShrinkAnimation());
        mAnimatorSet.start();
    }

    public void stopAnmation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
    }

    private ValueAnimator getStrokeAnimation() {
        ValueAnimator animation = ValueAnimator.ofFloat(mStrokeThin, mStrokeThick);
        animation.setDuration(mAnimDuration);
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCircleDrawable.getPaint().setStrokeWidth((Float)animation.getAnimatedValue());
                invalidate();
            }
        });
        return animation;
    }

    private ValueAnimator getShrinkAnimation() {
        ValueAnimator animation = ValueAnimator.ofInt(mCircleMax, mCircleMin);
        animation.setDuration(mAnimDuration);
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int delta = (mCircleMax - (Integer) animation.getAnimatedValue()) / 2;
                mCircleDrawable.setBounds(
                        delta + mStrokeThick,
                        delta + mStrokeThick,
                        delta + (Integer) animation.getAnimatedValue(),
                        delta + (Integer) animation.getAnimatedValue());
                invalidate();
            }
        });
        return animation;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Get the width measurement
        int size = View.resolveSize(getDesiredSquareSize(), widthMeasureSpec);
        //MUST call this to store the measurements
        setMeasuredDimension(size, size);
    }

    private int getDesiredSquareSize() {
        // To ensure the widget layout has enough space for expandable circle
        return mCircleMax + mStrokeThick;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCircleDrawable.draw(canvas);
    }
}
