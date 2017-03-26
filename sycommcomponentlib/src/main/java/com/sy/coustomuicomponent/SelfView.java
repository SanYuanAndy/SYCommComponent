package com.sy.coustomuicomponent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sy.utils.AppInfoUtil;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ASUS User on 2017/3/14.
 */
public class SelfView extends View implements View.OnTouchListener{
    private final static String TAG = SelfView.class.getSimpleName();
    public SelfView(Context context){
        this(context, null);
    }

    public SelfView(Context context, AttributeSet attr){
        super(context, attr);//必须显示调用，因为基类中没有默认的构造方法
        setOnTouchListener(this);
        initBgEntity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int nWidth = MeasureSpec.getSize(widthMeasureSpec);
        int nHeight = MeasureSpec.getSize(heightMeasureSpec);
        String strMeasureInfo = String.format("onMeasure w = %d, h = %d", nWidth, nHeight);
        Log.d(TAG, strMeasureInfo);
        mEntityFadingX = nWidth/15;
        mEntityFadingY = nWidth/12;
        if (nWidth > nHeight){
            mRowCnt = mLineCnt*nWidth/nHeight;
        }else{
            mLineCnt = mRowCnt * nHeight/nWidth;
        }
        mEntityWidth = (nWidth - (mRowCnt + 1)*mEntityFadingX)/mRowCnt;
        mEntityHeight = mEntityWidth;
        mEntityHeight = mEntityWidth > mEntityHeight ? mEntityHeight : mEntityWidth;
        onMeasureEntity(bgEntities);
        onMeasureEntity(realEntities);
        setMeasuredDimension(nWidth, nHeight);//多次回调onMeasure才能确定view最终的大小。在构造方法中获取宽高是错误的做法
}

    @Override
    protected void onDraw(Canvas canvas) {
        //Log.d(TAG, "onDraw");
        canvas.drawColor(0x77dddddd);
        drawEntity(canvas, bgEntities);
        drawEntity(canvas, realEntities);
        if (mSelectedEntity != null){
            mSelectedEntity.draw(canvas);
        }
        super.onDraw(canvas);//实际绘制过程中也是使用的左上方顶点的相对位置。当绘制的内容超出View的大小时，之后scroll view才能显示出来
        //调用scroll后，绘制的基准变了，所以绘制的内容也跟着sroll了。但是，被绘制的内容与View的左上方的顶点的相对位置没有变。
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        String strLayoutInfo = String.format("onLayout left = %d, top = %d, right = %d, bottom = %d", left, top, right, bottom);
        Log.d(TAG, strLayoutInfo);
        super.onLayout(changed, left, top, right, bottom);//与父布局左上方顶点的相对距离
    }

    public void addEntity(AppInfoUtil.AppInfo appInfo){
        for(int i = 0; i < realEntities.length; ++i){
            Entity entity = realEntities[i];
            int line = i / mRowCnt;
            int row = i % mRowCnt;
            if (entity == null){
                entity = new Entity(this);
                entity.i = row;
                entity.j = line;
                entity.appInfo = appInfo;
                realEntities[i] = entity;
                return;
            }
        }
    }

    private void onMeasureEntity(Entity[] oEntities){
        for(int i = 0; i < oEntities.length; ++i){
            Entity entity = oEntities[i];
            int line = i / mRowCnt;
            int row = i % mRowCnt;
            if (entity != null){
                entity.x = mEntityFadingX + (mEntityFadingX + mEntityWidth)*row;
                entity.y = mEntityFadingY + (mEntityFadingY + mEntityHeight)*line;
                entity.w = mEntityWidth;
                entity.h = mEntityHeight;
            }
        }

    }

    private void initBgEntity(){
        for(int i = 0; i < bgEntities.length; ++i){
            bgEntities[i] = new Entity(this, true);
            int line = i / mRowCnt;
            int row = i % mRowCnt;
            bgEntities[i].i = row;
            bgEntities[i].j = line;
        }
    }


    private void drawEntity(Canvas canvas, Entity[] oEntities){
        for(int i = 0; i < oEntities.length; ++i){
            Entity entity = oEntities[i];
            int line = i / mRowCnt;
            int row = i % mRowCnt;
            if (entity != null){
                entity.draw(canvas);
            }
        }
    }

    private Entity findEntity(int x, int y){
        for(int i = 0; i < realEntities.length; ++i){
            Entity entity = realEntities[i];
            if (entity != null){
                if ((x > entity.x && x <entity.x + entity.w)
                        && (y > entity.y && y <entity.y + entity.h)){
                    return entity;
                }
            }
        }
        return null;
    }

    private Entity findNearBgEntity(Entity entity, Entity[] oEntities){
       int i = 0, j = 0;
        //int x = mEntityFading + (mEntityFading + mEntityWidth)*i;
        //int y = mEntityFading + (mEntityFading + mEntityHeight)*j;
        i = (entity.x - mEntityFadingX)/(mEntityFadingX + mEntityWidth);
        j = (entity.y - mEntityFadingY)/(mEntityFadingY + mEntityHeight);
        Log.d(TAG, " i , j " + i + " , " + j);
        int[] e = new int[4];
        e[0] = i + j*mRowCnt;
        e[1] = e[0] + 1;
        e[2] = e[0] + mRowCnt;
        e[3] = e[0] + mRowCnt + 1;
        Entity minEntity = null;
        int centerX = entity.x + entity.w/2;
        int centerY = entity.y + entity.h/2;
        int minDistance = Integer.MAX_VALUE;
        for (int k = 0; k < e.length; ++k){
            if (e[k] > realEntities.length -1 || realEntities[e[k]] != null){
                Log.d(TAG, "e[k] = " + e[k]);
                continue;
            }
            Entity tempEntity = oEntities[e[k]];
            int tempCenterX = tempEntity.x + tempEntity.w/2;
            int tempCenterY = tempEntity.y + tempEntity.h/2;
            int tempDistance = (tempCenterX - centerX)*(tempCenterX - centerX) + (tempCenterY - centerY)*(tempCenterY - centerY);
            if (tempDistance < minDistance){
                minDistance = tempDistance;
                minEntity = tempEntity;
            }

        }
        //四周没有可用的空间
        if (minEntity == null){
            int z = e[3] + 1;
            for(; z < realEntities.length; ++z){
                if (realEntities[z] == null){
                    for(int w = z; w > e[3]; w--){
                        realEntities[w] = realEntities[w - 1];
                        realEntities[w].i = oEntities[w].i;
                        realEntities[w].j = oEntities[w].j;
                        int x = mEntityFadingX + (mEntityFadingX + mEntityWidth)* realEntities[w].i;
                        int y = mEntityFadingY + (mEntityFadingY + mEntityHeight)* realEntities[w].j;
                        realEntities[w].move(x, y, 500);
                        realEntities[w - 1] = null;
                    }
                    minEntity = oEntities[e[3]];
                    break;
                }
            }
        }

        return minEntity;
    }

    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private Set<SelfTimerTask> mTimerTaskQueue = new HashSet<SelfTimerTask>();
    public static interface SelfTimerTask{
        public boolean onTime();
    }

    public void addTimeTask(SelfTimerTask oTask){
        synchronized (mTimerTaskQueue) {
            mTimerTaskQueue.add(oTask);
            //
            if (mTimer == null) {
                mTimer = new Timer();
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (mTimerTaskQueue) {
                            if (mTimerTaskQueue != null) {
                                Iterator<SelfTimerTask> iterator = mTimerTaskQueue.iterator();
                                while (iterator.hasNext()) {
                                    SelfTimerTask task = iterator.next();
                                    if (!task.onTime()){
                                        iterator.remove();
                                        //mTimerTaskQueue.remove(task);错误的做法，会报异常
                                    }
                                }
                            }
                        }
                    }
                };
                mTimer.scheduleAtFixedRate(mTimerTask, 0, 50);
            }
        }
    }

    public void removeTimeTask(SelfTimerTask oTask){
        synchronized (mTimerTaskQueue) {
            if (mTimerTaskQueue != null) {
                mTimerTaskQueue.remove(oTask);
            }
            if (mTimerTaskQueue.isEmpty()) {
                mTimerTask.cancel();
                mTimer.cancel();
                mTimer = null;
                mTimerTask = null;
            }
        }
    }



    private final int DEFAULT_ROW_CNT = 4;
    private int mRowCnt = DEFAULT_ROW_CNT;
    private int mLineCnt = DEFAULT_ROW_CNT;
    private int mEntityFadingX = 0;
    private int mEntityFadingY = 0;
    private int mEntityWidth = 0;
    private int mEntityHeight = 0;

    public Entity[] realEntities = new Entity[28];
    public Entity[] bgEntities = new Entity[28];
    public static class Entity{
        private static int COUNT = 0;
        private int id = ++COUNT;
        boolean bg = false;
        int w;
        int h;
        int x;
        int y;
        int i;
        int j;
        AppInfoUtil.AppInfo appInfo;
        private View mParent = null;
        public Entity(View view){
            this(view, false);
        }

        public  Entity(View view, boolean bBg){
            bg = bBg;
            mParent = view;
        }

        private boolean mPressed = false;
        public void setLongPressed(boolean bPressed){
            mPressed = bPressed;
        }

        public void draw(Canvas canvas){
            int alpha = 0xbb;
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(3);
            String strLayoutInfo = String.format("left = %d, top = %d, right = %d, bottom = %d", x, y, w, h);
            //Log.d(TAG, strLayoutInfo);
            int w0 = 0;
            if (bg){
                w0 = 15;
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(1);
                return;
            }
            if (mPressed){
                paint.setStrokeWidth(5);
                paint.setColor(Color.BLUE);
                //paint.setColor(0x77ff0000);
                paint.setAlpha(alpha);
            }
            RectF rectF = new RectF(x - w0, y - w0, x + w + w0, y + h + w0);
            canvas.drawRoundRect(rectF, 30, 30, paint);
            paint.setTextSize(60);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int x0 = x + w/2;
            int y0 = y + h/2 - (fontMetrics.bottom + fontMetrics.top)/2;
            if (appInfo != null){
                paint.setColor(Color.BLACK);
                paint.setTextSize(25);
                paint.setStrokeWidth(1);
                paint.setStyle(Paint.Style.FILL);
                fontMetrics = paint.getFontMetricsInt();
                int labelX = x + w/2;
                int labelY = y + h + ((SelfView)mParent).mEntityFadingY/4 - (fontMetrics.bottom + fontMetrics.top)/2;
                String strLabel = appInfo.mLabel;
                if (strLabel.length() > 6){
                    strLabel = strLabel.substring(0, 6) + "...";
                }
                canvas.drawText(strLabel, labelX, labelY, paint);

                paint.setStyle(Paint.Style.STROKE);
                Drawable drawable = appInfo.mIconDrawable;
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                /***********
                //int bitmapX = 0;
                //int bitmapY = 0;
                //bitmapX = x + w/2 - bitmap.getWidth()/2;
                //bitmapY = y + h/2 - bitmap.getHeight()/2;
                //canvas.drawBitmap(bitmap, bitmapX, bitmapY, paint);
                /*******
                //Rect dstRest = new Rect(x, y, x + w, y + h);
                //Rect srcRest = new Rect(0,0,w, h);
                //canvas.drawBitmap(bitmap, srcRest, dstRest, paint);
                ********/
                /************/
               if (mPressed){
                    paint.setAlpha(0x77);
                }
                Matrix matrix = new Matrix();
                float scale = 0.8f;
                float scaleX = 1.0f*w/bitmap.getWidth()*scale;
                float scaleY = 1.0f*h/bitmap.getHeight()*scale;
                float bitmapX = 0;
                float bitmapY = 0;
                bitmapX = x + w/2 - scaleX*bitmap.getWidth()/2;
                bitmapY = y + h/2 - scaleY*bitmap.getHeight()/2;
                matrix.postScale(scaleX, scaleY);//需要小数点的整形除法，必须先将一个类型转成浮点型
                matrix.postTranslate(bitmapX, bitmapY);
                canvas.drawBitmap(bitmap, matrix, paint);

                BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                paint.setShader(bitmapShader);
                canvas.drawCircle(x + w/2, y + h/2, 40, paint);
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(3);
                rectF = new RectF(x, y, x + w, y + h);
                canvas.drawRoundRect(rectF, 30, 30, paint);
            }
        }

        public void move(int x0, int y0){
            x = x0;
            y = y0;
        }

        public void move(final int x0, final int y0, int nMillSecond) {
            final int stepCnt = nMillSecond/50;
            final int stepX = (x0 - x)/stepCnt;
            final int stepY = (y0 - y)/stepCnt;
            ((SelfView)mParent).addTimeTask(new SelfTimerTask() {
                int step = 0;
                @Override
                public boolean onTime(){
                    boolean bRet = true;
                    if(step < stepCnt) {
                        x += stepX;
                        y += stepY;
                        ++step;
                        if (step >= stepCnt) {
                            //((SelfView)mParent).removeTimeTask(this);在同一线程中操作了set
                            x = x0;
                            y = y0;
                            bRet = false;
                        }
                        mParent.postInvalidate();
                    }else{
                        bRet = false;
                    }
                    return bRet;
                }
            });
        }
    }

    private Runnable check = new Runnable() {
        @Override
        public void run() {
            if (mPressed){
                Log.d(TAG, String.format("long pressed x = %d, y = %d", mPressedX, mPressedY));
                mSelectedEntity = findEntity(mPressedX, mPressedY);
                if (mSelectedEntity != null) {
                    mSelectedEntity.setLongPressed(true);
                    realEntities[mSelectedEntity.i + mSelectedEntity.j * mRowCnt] = null;
                    invalidate(new Rect(mSelectedEntity.x, mSelectedEntity.y, mSelectedEntity.x + mSelectedEntity.w, mSelectedEntity.y + mSelectedEntity.h));
                    //SelfView.this.scrollBy(-15, -15);
                    //SelfView.this.layout(SelfView.this.getLeft() - 10, SelfView.this.getTop() -10, SelfView.this.getRight() - 10, SelfView.this.getBottom() - 10);
                }
            }
        }
    };

    private Runnable steadyCheck = new Runnable() {
        @Override
        public void run() {
            if (mPressed && mSelectedEntity != null){
                Log.d(TAG, "long wait");
                Entity emptyEntity = findNearBgEntity(mSelectedEntity, bgEntities);
                //invalidate(new Rect(mSelectedEntity.x, mSelectedEntity.y, mSelectedEntity.x + mSelectedEntity.w, mSelectedEntity.y + mSelectedEntity.h));
            }
        }
    };

    private boolean mPressed = false;
    private int mPressedX = 0;
    private int mPressedY = 0;
    private Entity mSelectedEntity = null;
    private long mLastMoveTime = SystemClock.elapsedRealtime();
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //Log.d(TAG, "action : " + motionEvent.getAction());
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_UP: {
                mPressed = false;
                this.removeCallbacks(check);
                this.removeCallbacks(steadyCheck);
                scrollTo(0,0);
                if (mSelectedEntity != null){
                    Entity emptyEntity = findNearBgEntity(mSelectedEntity, bgEntities);
                    if (emptyEntity != null){
                        realEntities[emptyEntity.i + emptyEntity.j * mRowCnt] = mSelectedEntity;
                        mSelectedEntity.move(emptyEntity.x, emptyEntity.y);
                        //realEntities[mSelectedEntity.i + mSelectedEntity.j * mRowCnt] = null;
                        mSelectedEntity.i = emptyEntity.i;
                        mSelectedEntity.j = emptyEntity.j;
                    }else{
                        mSelectedEntity.x = mEntityFadingX + (mEntityFadingX + mEntityWidth)*mSelectedEntity.i;
                        mSelectedEntity.y = mEntityFadingY + (mEntityFadingY + mEntityHeight)*mSelectedEntity.j;
                        //mSelectedEntity.move(emptyEntity.x, emptyEntity.y);
                    }

                    mSelectedEntity.setLongPressed(false);
                    mSelectedEntity = null;
                    invalidate();
                }
            }
            break;
            case MotionEvent.ACTION_DOWN: {
                mPressed = true;
                this.removeCallbacks(check);
                this.postDelayed(check, 500);
                mPressedX = (int)motionEvent.getX();//获取的是view的左上方顶点的相对位置。即使view被scroll过，基准不变
                mPressedY = (int)motionEvent.getY();

            }
            break;
            case MotionEvent.ACTION_MOVE: {
                int x = (int)motionEvent.getX();
                int y = (int)motionEvent.getY();
                long tNow = SystemClock.elapsedRealtime();
                if (tNow - mLastMoveTime < 100){
                    break;
                }
                mLastMoveTime = tNow;
                if (mSelectedEntity == null){
                    int nCurrScrollX = getScrollX();

                    int lastScorllX = mPressedX - x + nCurrScrollX;
                    if (lastScorllX < -mEntityWidth) {
                        lastScorllX = -mEntityWidth - nCurrScrollX;
                    }else if (lastScorllX > mEntityWidth){
                        lastScorllX = mEntityWidth - nCurrScrollX;
                    }else {

                    }
                    Log.d(TAG, "scrollX " + nCurrScrollX + " last " + lastScorllX);
                    scrollBy(lastScorllX, 0);
                    postInvalidate();
                    break;
                }
                this.removeCallbacks(check);
                this.removeCallbacks(steadyCheck);
                this.postDelayed(steadyCheck, 200);

                if (mSelectedEntity != null && mPressed){
                    mSelectedEntity.move(x, y);
                    invalidate(new Rect(mSelectedEntity.x, mSelectedEntity.y, mSelectedEntity.x + mSelectedEntity.w, mSelectedEntity.y + mSelectedEntity.h));
                }
            }
            break;
            default:
        }
        return true;
    }
}
