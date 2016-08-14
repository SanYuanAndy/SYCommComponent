package com.sy.coustomuicomponent.ScrollView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by ASUS User on 2016/8/11.
 * Note：
 * 两个概念：
 * 1、可视视图：真实呈现在用户眼前的可视区域。大小、位置固定。
 * 2、内存视图：真实视图的数据表现形式。大小、位置可以变化。
     **************
   --*------------*---
   - *            *  -
   - *            *  -
   - **************  -
   -------------------

    *号所围成的区域是可视视图。-号所围成的区域是内存视图。两者重叠的部分是，
 *  用户能够真正能够看到的内存视图的一部分。移动或者改变内存视图，用户就可以
 *  看到内存视图的其他部分或者全部内容。
 */
public class ScrollView extends View implements View.OnTouchListener{
    public ScrollView(Context context){
        super(context);
    }

    public ScrollView(Context context, AttributeSet attr){
        super(context, attr);
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("", "widthMeasureSpec " + widthMeasureSpec);
        Log.d("", "heightMeasureSpec " + heightMeasureSpec);
        Log.d("", "width " + getWidth());
        Log.d("", "height " + getHeight());
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(1080 + 360*2, 360);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        Rect rect = new Rect();
        int perWitdh = getWidth()/mChunkCount;
        int top = getTop();
        int bottom = getBottom();
        int left = getLeft() - perWitdh;

        paint.setColor(Color.RED);
        rect.set(left, top, left + perWitdh, bottom);
        canvas.drawRect(rect, paint);

        left += perWitdh;
        paint.setColor(Color.BLUE);
        rect.set(left, top, left + perWitdh, bottom);
        canvas.drawRect(rect, paint);

        left += perWitdh;
        paint.setColor(Color.BLACK);
        rect.set(left, top, left + perWitdh, bottom);
        canvas.drawRect(rect, paint);

        left += perWitdh;
        paint.setColor(Color.GRAY);
        rect.set(left, top, left + perWitdh, bottom);
        canvas.drawRect(rect, paint);

        left += perWitdh;
        paint.setColor(Color.YELLOW);
        rect.set(left, top, left + perWitdh, bottom);
        canvas.drawRect(rect, paint);
    }

    private int mChunkCount = 5;
    private float oldPotionX = 0;
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP: {
                int moveStep = (int) (motionEvent.getX() - oldPotionX);
                scrollBySafety(moveStep, 0, true);
            }
                break;
            case MotionEvent.ACTION_DOWN: {
                oldPotionX = motionEvent.getX();
            }
                break;
            case MotionEvent.ACTION_MOVE: {
                float currPotionX = motionEvent.getX();
                int moveStep = (int) (currPotionX - oldPotionX);
                scrollBySafety(moveStep, 0, false);
                oldPotionX = currPotionX;
            }
                break;
            default:
        }
        return true;
    }

    private int currX = 0;
    private int currY = 0;
    private void scrollBySafety(int x, int y, boolean bRestore){
        int newX = currX - x;
        if (newX >= -1 * getWidth()/mChunkCount * 2 && newX <= getWidth()/mChunkCount * 2) {
            scrollBy(-x, y);
            currX = newX;
        }
        if (bRestore) {
            if (newX >= getWidth() / mChunkCount) {
                newX = getWidth() / mChunkCount;
                scrollTo(newX, currY);
                currX = newX;
            }

            if (newX <= -getWidth() / mChunkCount) {
                newX = -getWidth() / mChunkCount;
                scrollTo(newX, currY);
                currX = newX;
            }
        }
    }

}
