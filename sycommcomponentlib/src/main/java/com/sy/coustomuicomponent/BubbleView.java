package com.sy.coustomuicomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sy.sycommcomponent.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS User on 2016/8/23.
 */
public class BubbleView extends View{
    private final static String TAG = BubbleView.class.getSimpleName();
    private int mColor = Color.RED;
    private int mProgressColor = Color.BLACK;
    private int mProgressSize = 0;
    private int mBubbleRadiu = 20;
    private int mAngel = 0;
    private int mProgress = 0;
    private Handler mHandler = null;
    public BubbleView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        if (attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BubbleView, defStyleAttr, 0);
            mProgressColor = typedArray.getColor(R.styleable.BubbleView_progress_color, Color.BLACK);
            mProgressSize = (int)typedArray.getDimension(R.styleable.BubbleView_progress_size, 0);
            mBubbleRadiu = (int)typedArray.getDimension(R.styleable.BubbleView_bubble_radius, 0);
            mColor = typedArray.getColor(R.styleable.BubbleView_bubble_color, Color.RED);
            typedArray.recycle();
        }
        mHandler = new Handler(Looper.getMainLooper());
        initBubble();
        animation();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint paint = new Paint();
        paint.setColor(mColor);
        int small_r = mBubbleRadiu;
        int r0 = getWidth() < getHeight() ? getWidth()/2 : getHeight()/2;
        int x0 = getLeft() + getWidth()/2;
        int y0 = getTop() + getHeight()/2;
//        for (int corner = 0; corner < 360; corner += 30) {
//            int angel = corner + mAngel;
//            int x = x0 + (int)((r0 - small_r) * Math.cos(Math.PI * angel/180));
//            int y = y0 - (int)((r0 - small_r) * Math.sin(Math.PI * angel/180));
//            canvas.drawCircle(x, y, small_r, paint);
//        }
        for (Bubble bubble : mBubbleList) {
            int angel = bubble.getAngle();
            int x = x0 + (int)((r0 - small_r) * Math.cos(Math.PI * angel/180));
            int y = y0 - (int)((r0 - small_r) * Math.sin(Math.PI * angel/180));
            canvas.drawCircle(x, y, small_r, paint);
        }
        paint.setColor(mProgressColor);
        paint.setTextSize(mProgressSize);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        String strText = String.format("%3d%s", mProgress, "%");
        int baseline = y0 + (-fontMetrics.top) -(fontMetrics.bottom - fontMetrics.top)/2;
        canvas.drawText(strText, x0, baseline, paint);
        //canvas.rotate(mAngel);
    }

    private void animation(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAngel += 20;
                if (mAngel > 360){
                    mAngel = 0;
                }
                mProgress = 100 * mAngel/360;
                if (!bForwad) {
                    moveForwad(mBubbleList);
                }else{
                    moveBack(mBubbleList);
                }
                bForwad = !bForwad;
                invalidate();
                animation();
            }
        }, 500);
    }

    private static class Bubble{
        private int mAngle = 0;
        public int getAngle(){
            return  mAngle;
        }
        public void setAngle(int angle){
            mAngle = angle;
        }

    }

    private List<Bubble> mBubbleList = null;
    private int mBubbleCount = 6;
    private int mBaseAngle = 0;
    private int mPerAngle = 10;
    private boolean bForwad = false;

    private void initBubble(){
        mBubbleList = new ArrayList<Bubble>();
        for (int i = 0; i < mBubbleCount; i++){
            Bubble bubble = new Bubble();
            if (i == 0) {
                bubble.setAngle(mBaseAngle);
            }else{
                bubble.setAngle(mBubbleList.get(i-1).getAngle() + mPerAngle);
            }
            mBubbleList.add(bubble);
        }
    }

    private void moveForwad(List<Bubble> list){
        for (int i = 0; i < list.size(); i++) {
            if (i == 0){
                continue;
            }
            Bubble bubble = list.get(i);
            int angel = list.get(i - 1).getAngle() + i*mPerAngle;
            bubble.setAngle(angel);
        }
    }

    private void moveBack(List<Bubble> list){
        for (int i = list.size() - 1; i >= 0; i--) {
            if (i == list.size() - 1){
                continue;
            }
            Bubble bubble = list.get(i);
            int angel = list.get(i + 1).getAngle() - mPerAngle;
            bubble.setAngle(angel);
        }
    }

}
