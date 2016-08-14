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

import java.util.EventListenerProxy;

/**
 * Created by ASUS User on 2016/8/11.
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
        Log.d("", "width " + getWidth());
        Log.d("", "height " + getHeight());
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(1080 + 360, 360);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        Rect rect = new Rect();
        int top = getTop();
        int bottom = getBottom();
        int right = getRight();
        int left = getLeft();
        int perWitdh = getWidth()/4;

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
        paint.setColor(Color.YELLOW);
        rect.set(left, top, left + perWitdh, bottom);
        canvas.drawRect(rect, paint);
    }

    private float oldPotion_x = 0;


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP: {
                int moveStep = (int) (oldPotion_x - motionEvent.getX());
                scrollBySafety(moveStep, 0, true);
            }
                break;
            case MotionEvent.ACTION_DOWN: {
                oldPotion_x = motionEvent.getX();
            }
                break;
            case MotionEvent.ACTION_MOVE: {
                int moveStep = (int) (motionEvent.getX() - oldPotion_x);
                scrollBySafety(moveStep, 0, false);
            }
                break;
            default:
        }
        return true;
    }

    private int currX = getLeft();
    private int currY = getTop();
    private void scrollBySafety(int x, int y, boolean bRestore){
        int newX = currX + x;
        Log.d("", "currX : " + currX + ",newX : " + newX);
        if (newX >= -1 * getWidth()/4 && newX <= getWidth()/4) {
            scrollBy(x, y);
            currX = newX;
        }
        if (bRestore) {
            if (newX >= getWidth() / 4) {
                newX = getWidth() / 4;
                scrollTo(newX, currY);
                currX = newX;
            }

            if (newX <= -getWidth() / 4) {
                newX = -getWidth() / 4;
                scrollTo(newX, currY);
                currX = newX;
            }
        }
    }

}
