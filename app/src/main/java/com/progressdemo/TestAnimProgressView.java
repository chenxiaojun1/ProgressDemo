package com.progressdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class TestAnimProgressView extends AnimProgressView {

    private static final String TAG = "ArcAnimProgressView";
    private Paint progressPaint;
    private RectF progressRectF;
    private static final int INNER_BACKGROUND_COLOR_DEFAULT = Color.TRANSPARENT;
    private static final int PROGRESS_WIDTH_DEFAULT = 10;

    private static final int ARC_STROKE = 0;
    private static final int ARC_FILL = 1;

    private RectF progressBackGroundRectF;
    private int style;

    private int progressWidth;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private Paint strokePaint;

    public TestAnimProgressView(Context context) {
        this(context, null);
    }

    public TestAnimProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestAnimProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(attrs, defStyleAttr);
    }

    public int getStyle() {
        return style;
    }


    public int getProgressWidth() {
        return progressWidth;
    }

    public TestAnimProgressView setStyle(int style) {
        this.style = style;
        return this;
    }


    public TestAnimProgressView setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
        return this;
    }

    private void initView(AttributeSet attrs, int defStyleAttr) {

        TypedArray attributes = mContext.obtainStyledAttributes(attrs, R.styleable.ArcAnimProgressView, defStyleAttr, 0);
        style = attributes.getInt(R.styleable.ArcAnimProgressView_apv_style, ARC_FILL);

        progressWidth = attributes.getDimensionPixelSize(R.styleable.ArcAnimProgressView_progress_width, PROGRESS_WIDTH_DEFAULT);

        attributes.recycle();

        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);

        progressRectF = new RectF();
        progressBackGroundRectF = new RectF();

        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.setDrawFilter(paintFlagsDrawFilter);

        float centerX = (float) (getWidth() >> 1);
        float centerY = (float) (getHeight() >> 1);

        float startX1 = centerX - (getWidth()/2f);//开始坐标
        float startY1 = centerY - (getHeight()/4f);

        float startX2 = centerX - (getWidth()/2f)+(getHeight()/4f);
        float startY2 = centerY - (getHeight()/2f);

        float startX3 = centerX + (getWidth()/2f)-(getHeight()/4f);
        float startY3 = centerY - (getHeight()/2f);

        float startX4 = centerX + (getWidth()/2f);
        float startY4 = centerY - (getHeight()/4f);

        float startX5 = centerX + (getWidth()/2f);
        float startY5 = centerY + (getHeight()/4f);

        float startX6 = centerX + (getWidth()/2f) - (getHeight()/4f);
        float startY6 = centerY + (getHeight()/4f)+(getHeight()/4f);

        float startX7 = centerX - (getWidth()/2f) + (getHeight()/4f);
        float startY7 = centerY + (getHeight()/4f)+(getHeight()/4f);

        float startX8 = centerX - (getWidth()/2f) ;
        float startY8 = centerY + (getHeight()/4f);

        //draw outer
        if (strokeWidth > 0) {
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(strokeWidth);
            strokePaint.setColor(strokeColor);
            Path path = new Path();
            path.moveTo(startX1,startY1);
            path.lineTo(startX2,startY2);
            path.lineTo(startX3,startY3);
            path.lineTo(startX4,startY4);
            path.lineTo(startX5,startY5);
            path.lineTo(startX6,startY6);
            path.lineTo(startX7,startY7);
            path.lineTo(startX8,startY8);
            path.close();
            canvas.drawPath(path, strokePaint);
        }
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.FILL);

        float v = progress - startProgress;//

        int number = (int) (v * 0.2);// 总进度100。 每5个进度加一个矩形  矩形长半个高度，宽4分一高度。间隔4分1高度
        int a = (int) (startProgress* 0.2);//已经画出矩形个数
        for(int i = 0; i< number;i++){
            progressRectF.set(centerX - (getWidth()/2f) +(a*(getHeight()/2f))+(getHeight()/4f)+ (i*((getHeight()/2f))),
                    centerY - (getHeight()/4f), centerX - (getWidth()/2f) +(a*(getHeight()/2f))+(getHeight()/2f)+ (i*((getHeight()/2f))), centerY + (getHeight()/4f));
            canvas.drawRect(progressRectF,progressPaint);
        }
    }
}
