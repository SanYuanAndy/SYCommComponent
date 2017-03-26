package com.sy.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ASUS User on 2017/3/26.
 */
public class AppInfoUtil {
    public static class AppInfo{
        public String mLabel;
        public String mPackgeName;
        public Drawable mIconDrawable;
    }

    public static List<AppInfo> getAppInfoList(Context context){
        List<AppInfo> appInfoList = new ArrayList<AppInfo>();
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(intent, pm.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfoList){
            AppInfo appInfo = new AppInfo();
            appInfo.mLabel = (String)resolveInfo.loadLabel(pm);
            appInfo.mPackgeName = resolveInfo.resolvePackageName;
            appInfo.mIconDrawable = resolveInfo.loadIcon(pm);
            if (isRepeated(appInfoList, appInfo)){
                //continue;
            }
            appInfoList.add(appInfo);
            Log.d("", "label : " + appInfo.mLabel);

        }
        return appInfoList;

    }

    public static List<AppInfo> getAppInfoList_ext(Context context){
        List<AppInfo> appInfoList = new ArrayList<AppInfo>();
        PackageManager pm = context.getPackageManager();;
        List<PackageInfo> resolveInfoList = pm.getInstalledPackages(0);
        for (PackageInfo PackageInfo : resolveInfoList){
            AppInfo appInfo = new AppInfo();
            appInfo.mLabel = (String)PackageInfo.applicationInfo.loadLabel(pm);
            appInfo.mPackgeName = PackageInfo.packageName;
            appInfo.mIconDrawable = PackageInfo.applicationInfo.loadIcon(pm);
            if (isRepeated(appInfoList, appInfo)){
                continue;
            }
            appInfoList.add(appInfo);
            Log.d("", "label : " + appInfo.mLabel);

        }
        return appInfoList;

    }

    private static boolean isRepeated(List<AppInfo> list, AppInfo oAppInfo){
        boolean bRet = false;
        for (AppInfo appInfo:list){
            Log.d("", "label : " + appInfo.mLabel);
            if (appInfo.mLabel == oAppInfo.mLabel || appInfo.mPackgeName == oAppInfo.mPackgeName){
                bRet = true;
                return bRet;
            }
        }
        return bRet;
    }



}
