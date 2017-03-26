package com.sy.sycommcomponent.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sy.alldemo.R;
import com.sy.sycommcomponent.util.ProcSyncUtil;

public class ContextActivity extends AppCompatActivity {
    private TextView mContextTv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);
        mContextTv = (TextView) findViewById(R.id.content_tv);
        //String dir = getApplicationContext().getFilesDir();
        getBaseContext();
        mContextTv.setText(getApplicationContext().getFilesDir().getPath());
        ProcSyncUtil lock = new ProcSyncUtil(ProcSyncUtil.LOCK_NAME);
        lock.lock();
    }
}

