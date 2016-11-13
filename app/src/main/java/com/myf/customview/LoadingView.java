package com.myf.customview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2016/11/13.
 */
public class LoadingView extends View implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private static final int STOKE_WIDTH = 10;
    private static final float MIN_SWEEP_ANGLE = 30;
    private ValueAnimator rotateAnim;
    private Paint redPaint;
    private Paint yellowPaint;
    private Paint bluePaint;
    private Paint greenPaint;
    private int mRadius;
    private RectF mRedRectF, mYellowRecF, mBuleRectF, mGreenRectF;
    private float mCurrentGlobalAngle;
    private boolean mRunning;
    private boolean mModeAppearing = true;
    private float mCurrentGlobalAngleOffset;
    private float mCurrentSweepAngle;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setColor(Color.RED);
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setStrokeWidth(STOKE_WIDTH);

        yellowPaint = new Paint();
        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setStyle(Paint.Style.STROKE);
        yellowPaint.setStrokeWidth(STOKE_WIDTH);

        bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);
        bluePaint.setStyle(Paint.Style.STROKE);
        bluePaint.setStrokeWidth(STOKE_WIDTH);

        greenPaint = new Paint();
        greenPaint.setColor(Color.GREEN);
        greenPaint.setStyle(Paint.Style.STROKE);
        greenPaint.setStrokeWidth(STOKE_WIDTH);

        rotateAnim = ValueAnimator.ofFloat(0, 1f);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.setDuration(1000);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.addUpdateListener(this);
        rotateAnim.addListener(this);

        mRedRectF = new RectF();
        mYellowRecF = new RectF();
        mBuleRectF = new RectF();
        mGreenRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width > height)
            width = height;
        else
            height = width;
        Log.d("myf", "onMeasure: width-->" + width);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = w / 2;
        float x, y;
        x = mRadius;
        y = mRadius;
        mRedRectF.set(x - mRadius + STOKE_WIDTH / 2, y - mRadius + STOKE_WIDTH / 2, x + mRadius - STOKE_WIDTH / 2, y + mRadius - STOKE_WIDTH / 2);
        mYellowRecF.set(x - mRadius + STOKE_WIDTH / 2 + STOKE_WIDTH, y - mRadius + STOKE_WIDTH / 2 + STOKE_WIDTH, x + mRadius - STOKE_WIDTH / 2 - STOKE_WIDTH, y + mRadius - STOKE_WIDTH / 2 - STOKE_WIDTH);
        mBuleRectF.set(x - mRadius + STOKE_WIDTH / 2 + STOKE_WIDTH * 2, y - mRadius + STOKE_WIDTH / 2 + STOKE_WIDTH * 2, x + mRadius - STOKE_WIDTH / 2 - STOKE_WIDTH * 2, y + mRadius - STOKE_WIDTH / 2 - STOKE_WIDTH * 2);
        mGreenRectF.set(x - mRadius + STOKE_WIDTH / 2 + STOKE_WIDTH * 3, y - mRadius + STOKE_WIDTH / 2 + STOKE_WIDTH * 3, x + mRadius - STOKE_WIDTH / 2 - STOKE_WIDTH * 3, y + mRadius - STOKE_WIDTH / 2 - STOKE_WIDTH * 3);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startAnim();
        } else {
            stopAnim();
        }
    }

    private void startAnim() {
        if (isRunning()) return;
        mRunning = true;
        rotateAnim.start();
    }

    private void stopAnim() {
        if (!isRunning()) return;
        mRunning = false;
        rotateAnim.cancel();
    }

    private boolean isRunning() {
        return mRunning;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc(canvas, redPaint,mRedRectF,180,0,true);
        drawArc(canvas, yellowPaint,mYellowRecF,90,90,false);
        drawArc(canvas, bluePaint,mBuleRectF,30,180,true);
        drawArc(canvas, greenPaint,mGreenRectF,90,270,false);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float value = (float) animation.getAnimatedValue();
        mCurrentGlobalAngle = value * 360;
        mCurrentSweepAngle = value * 150;
        postInvalidate();
    }

    private void drawArc(Canvas canvas, Paint paint,RectF rectF,int angle,int offset,boolean bool) {
        float startAngle = mCurrentGlobalAngle*(bool?1:-1);
        float sweepAngle = mCurrentSweepAngle;
        if (mModeAppearing) {
        } else {
            sweepAngle = 150-sweepAngle;
        }
        canvas.drawArc(rectF, startAngle+offset, angle+sweepAngle, false, paint);
    }

    private void toggleAppearingMode() {
        mModeAppearing = !mModeAppearing;
    }

    @Override
    protected void onAttachedToWindow() {
        startAnim();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnim();
        super.onDetachedFromWindow();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        toggleAppearingMode();
    }


}
