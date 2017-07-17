package com.gin.interview.handler;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gin.interview.R;

/**
 * Created by wang.lichen on 2017/7/16.
 */

public class AsynTaskActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);


    }
    
    
    
    private class MyAsy extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }
    }
}
