package com.sy.sycommcomponent;

import com.sy.coustomuicomponent.BaseApplication;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ASUS User on 2016/7/18.
 */
public class MyApplication extends BaseApplication {
    private static MyApplication sMyApplication = null;
    @Override
    public void onCreate() {
        super.onCreate();
        sMyApplication = this;
    }

    public void runOnBackground(Runnable oRun, long delay) {
        super.runOnBackground(oRun, delay);
    }


    public void runOnUiThread(Runnable oRun, long delay) {
        super.runOnUiThread(oRun, delay);
    }

    public static MyApplication getAppliction(){
        return sMyApplication;
    }

    public void showMsg(final String msg){
        Runnable oRun = new Runnable() {
            @Override
            public void run() {
                if (!displayComponentList.isEmpty()){
                    displayComponentList.get(displayComponentList.size() - 1).showMsg(msg);
                }
            }
        };
        runOnUiThread(oRun, 0);
    }

    private List<DisplayComponent> displayComponentList = new LinkedList<DisplayComponent>();
    public void addDisplayComponent(DisplayComponent displayComponent){
        if (!displayComponentList.contains(displayComponent)){
            displayComponentList.add(displayComponent);
        }
    }

    public void removeDisplayComponent(DisplayComponent displayComponent){
        displayComponentList.remove(displayComponent);
    }

    public static interface DisplayComponent{
        public void showMsg(String msg);
    };

}
