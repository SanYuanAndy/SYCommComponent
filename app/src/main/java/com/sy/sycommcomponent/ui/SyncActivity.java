package com.sy.sycommcomponent.ui;

import android.app.Activity;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sy.alldemo.R;
import com.sy.coustomuicomponent.SelfView;
import com.sy.sycommcomponent.MyApplication;
import com.sy.utils.AppInfoUtil;

import java.util.List;

public class SyncActivity extends Activity {
    List<AppInfoUtil.AppInfo> mAppList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_view);
        mAppList = AppInfoUtil.getAppInfoList_ext(MyApplication.getAppliction());
        SelfView view = (SelfView)findViewById(R.id.self_sv);
        for (int i = 0; i < 16 && i < mAppList.size(); ++i) {
            Log.d("", " i = " + i + "size = " + mAppList.size());
            if (i == 15){
                mAppList.get(i).mIconDrawable = this.getResources().getDrawable(R.drawable.dd);
            }
            view.addEntity(mAppList.get(i));
        }
    }
}
