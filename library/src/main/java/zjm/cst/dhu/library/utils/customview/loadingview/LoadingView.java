package zjm.cst.dhu.library.utils.customview.loadingview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import zjm.cst.dhu.library.R;

/**
 * Created by zjm on 2017/4/21.
 */

public class LoadingView extends View {

    private float[] RADIUS = new float[4];
    private int[] ALPHA = new int[4];
    private float r;
    private int duration;
    private Paint paint;
    private PointInCircle pointInCircle1, pointInCircle2, pointInCircle3, pointInCircle4;

    public float[] getRADIUS(){
        return RADIUS;
    }

    private float getPointX(PointInCircle pointInCircle) {
        double degree = (pointInCircle.getD() / 180f) * Math.PI;
        float x = (float) (pointInCircle.getX() + pointInCircle.getR() * Math.cos(degree));
        return x;
    }

    private float getPointY(PointInCircle pointInCircle) {
        double degree = (pointInCircle.getD() / 180f) * Math.PI;
        float y = (float) (pointInCircle.getY() + pointInCircle.getR() * Math.sin(degree));
        return y;
    }

    private void drawCircle(Canvas canvas) {
        paint.setAlpha(ALPHA[0]);
        canvas.drawCircle(getPointX(pointInCircle1), getPointY(pointInCircle1), RADIUS[0], paint);
        paint.setAlpha(ALPHA[1]);
        canvas.drawCircle(getPointX(pointInCircle2), getPointY(pointInCircle2), RADIUS[1], paint);
        paint.setAlpha(ALPHA[2]);
        canvas.drawCircle(getPointX(pointInCircle3), getPointY(pointInCircle3), RADIUS[2], paint);
        paint.setAlpha(ALPHA[3]);
        canvas.drawCircle(getPointX(pointInCircle4), getPointY(pointInCircle4), RADIUS[3], paint);
    }


    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        RADIUS[0] = array.getDimension(R.styleable.LoadingView_radius_1, 40f);
        RADIUS[1] = array.getDimension(R.styleable.LoadingView_radius_2, 30f);
        RADIUS[2] = array.getDimension(R.styleable.LoadingView_radius_3, 20f);
        RADIUS[3] = array.getDimension(R.styleable.LoadingView_radius_4, 10f);
        ALPHA[0] = (int) (array.getFloat(R.styleable.LoadingView_alpha_1, 1f) * 255);
        ALPHA[1] = (int) (array.getFloat(R.styleable.LoadingView_alpha_2, 0.8f) * 255);
        ALPHA[2] = (int) (array.getFloat(R.styleable.LoadingView_alpha_3, 0.6f) * 255);
        ALPHA[3] = (int) (array.getFloat(R.styleable.LoadingView_alpha_4, 0.4f) * 255);
        duration = (int) (1 / array.getFloat(R.styleable.LoadingView_duration, 1f) * 1000);
        r = (float) ((RADIUS[0] + RADIUS[1]) / (Math.sin(Math.PI / 12) * 2));
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(array.getColor(R.styleable.LoadingView_loading_color, Color.BLUE));
        array.recycle();
    }

    private void startAnimation(PointInCircle pointInCircle, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        PointInCircle start = new PointInCircle(getWidth() / 2, getHeight() / 2, pointInCircle.getD(), r);
        PointInCircle middle = new PointInCircle(getWidth() / 2, getHeight() / 2, pointInCircle.getD() + 180f, r);
        PointInCircle end = new PointInCircle(getWidth() / 2, getHeight() / 2, pointInCircle.getD() + 360f, r);
        LoadingEvaluator loadingEvaluator = new LoadingEvaluator();
        LoadingInterpolator loadingInterpolator = new LoadingInterpolator();

        ValueAnimator anim1 = ValueAnimator.ofObject(loadingEvaluator, start, middle);
        anim1.addUpdateListener(animatorUpdateListener);
        ValueAnimator anim2 = ValueAnimator.ofObject(loadingEvaluator, middle, end);
        anim2.addUpdateListener(animatorUpdateListener);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(anim1).before(anim2);
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(loadingInterpolator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.start();
            }
        });
        animatorSet.start();
    }

    private void initAnimation() {
        ValueAnimator.AnimatorUpdateListener animatorUpdateListener1 = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointInCircle1 = (PointInCircle) animation.getAnimatedValue();
                invalidate();
            }
        };
        startAnimation(pointInCircle1, animatorUpdateListener1);
        ValueAnimator.AnimatorUpdateListener animatorUpdateListener2 = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointInCircle2 = (PointInCircle) animation.getAnimatedValue();
            }
        };
        startAnimation(pointInCircle2, animatorUpdateListener2);
        ValueAnimator.AnimatorUpdateListener animatorUpdateListener3 = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointInCircle3 = (PointInCircle) animation.getAnimatedValue();
            }
        };
        startAnimation(pointInCircle3, animatorUpdateListener3);
        ValueAnimator.AnimatorUpdateListener animatorUpdateListener4 = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointInCircle4 = (PointInCircle) animation.getAnimatedValue();
            }
        };
        startAnimation(pointInCircle4, animatorUpdateListener4);
    }

    private int getSize(int minSize, int measureSpec) {
        int res = minSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int defaultSize = MeasureSpec.getSize(measureSpec);
        switch (mode) {
            case MeasureSpec.AT_MOST:
                res = Math.max(defaultSize, minSize);
                break;
            case MeasureSpec.EXACTLY:
                res = defaultSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                res = minSize;
                break;
        }
        return res;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minCircumscribe = (int) ((RADIUS[0] + RADIUS[1]) / Math.sin(Math.PI / 12) + 2 * RADIUS[0]);
        int width = getSize(minCircumscribe, widthMeasureSpec);
        int height = getSize(minCircumscribe, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pointInCircle1 == null || pointInCircle2 == null || pointInCircle3 == null || pointInCircle4 == null) {
            pointInCircle1 = new PointInCircle(getWidth() / 2, getHeight() / 2, 0f, r);
            pointInCircle2 = new PointInCircle(getWidth() / 2, getHeight() / 2, -30f, r);
            pointInCircle3 = new PointInCircle(getWidth() / 2, getHeight() / 2, -60f, r);
            pointInCircle4 = new PointInCircle(getWidth() / 2, getHeight() / 2, -90f, r);
            drawCircle(canvas);
            initAnimation();
        } else {
            drawCircle(canvas);
        }
    }

}
