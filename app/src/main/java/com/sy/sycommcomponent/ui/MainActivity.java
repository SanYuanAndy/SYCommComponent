package com.sy.sycommcomponent.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sy.alldemo.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView mDemosLv = null;
    private DemoDataAdapter mDataAdapter = null;
    private List<String> mData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        mDemosLv = (ListView) findViewById(R.id.test_demos_lv);
        mData = getActivitys();
        mData.remove(MainActivity.class.getName());
        mDataAdapter = new DemoDataAdapter(this, mData);
        mDemosLv.setAdapter(mDataAdapter);
        mDemosLv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       startActivity((String)adapterView.getAdapter().getItem(i));
    }

    private class DemoDataAdapter extends BaseAdapter{
        private Context mContext = null;
        private List<String> mData = null;

        public DemoDataAdapter(Context context, List<String> data){
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                LinearLayout linearLayout = new LinearLayout(mContext);
                LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER);
                //linearLayout.setLayoutParams(lineLayoutParams);

                TextView textView = new TextView(mContext);
                lineLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lineLayoutParams.setMargins(0, 20, 0, 20);
                linearLayout.setGravity(Gravity.CENTER);
                textView.setLayoutParams(lineLayoutParams);
                textView.setTextSize(20);
                linearLayout.addView(textView);
                convertView = linearLayout;

                viewHolder = new ViewHolder();
                viewHolder.textView = textView;
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String strDisplay = (String)getItem(i);
            String[] strTemp = strDisplay.split("\\.");
            viewHolder.textView.setText(strTemp[strTemp.length - 1]);
            return convertView;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return mData.get(i);
        }

    }

    private static class ViewHolder{
       TextView textView;
    }

    private List<String> getActivitys(){
        List<String> list = new ArrayList<String>();
        try {
            PackageManager pm = this.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activityInfo = packageInfo.activities;
            for (ActivityInfo info : activityInfo){
                list.add(info.name);
            }
        }catch(Exception e){
            Log.e(TAG, e.toString());
        }
        return list;
    }

    private void startActivity(String strClassName){
        try {
            Intent intent = new Intent();
            intent.setClassName(this, strClassName);
            this.startActivity(intent);
        }catch (Exception e){

        }
    }


}
