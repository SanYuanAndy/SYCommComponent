package com.sy.base;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by ASUS User on 2016/7/18.
 */
public class BaseApplication extends Application{
    private HandlerThread mBackgroundThread = null;
    private Handler mBackgroundHandler = null;
    private Handler mUiHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        mBackgroundThread = new HandlerThread("backgroundThread");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        mUiHandler = new Handler(getMainLooper());
    }

    protected  void runOnBackground(Runnable oRun, long delay){
       if (mBackgroundHandler != null){
           mBackgroundHandler.postDelayed(oRun, delay);
       }
    }

    protected  void runOnUiThread(Runnable oRun, long delay){
        if (mUiHandler != null){
            mUiHandler.postDelayed(oRun, delay);
        }
    }

}
