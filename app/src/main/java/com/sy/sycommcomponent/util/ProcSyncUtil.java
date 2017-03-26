package com.sy.sycommcomponent.util;

import android.os.Environment;
import android.util.Log;

import com.sy.sycommcomponent.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileLock;

/**
 * Created by ASUS User on 2017/2/26.
 */
public class ProcSyncUtil {
    private final static String TAG = ProcSyncUtil.class.getSimpleName();
    public final static String LOCK_NAME = Environment.getExternalStorageDirectory().getPath() + File.separator + "lock";


    private String mStrFileName = null;
    FileOutputStream mFileOutputStream = null;
    FileLock mLock = null;
    public ProcSyncUtil(String strFileName){
       mStrFileName = strFileName;
    }

    public void lock(){
        try {
            mFileOutputStream = new FileOutputStream(mStrFileName);
        }catch(Exception e)
        {
            Log.d(TAG, "e : " + e.toString());
            return;
        }
        try {
            Log.d(TAG, "try lock");
            mLock = mFileOutputStream.getChannel().lock();
            Log.d(TAG, "obtain lock");
        }catch(Exception e){
            return;
        }

    }

    public void unlock(){
        if (mLock != null){
            try {
                mLock.release();
            }catch(Exception e){

            }

        }
        if (mFileOutputStream != null){
            try {
                mFileOutputStream.close();
            }catch(Exception e){

            }
        }
    }


}
