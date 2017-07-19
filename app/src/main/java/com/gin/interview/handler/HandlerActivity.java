package com.gin.interview.handler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gin.interview.R;

/**
 * Created by wang.lichen on 2017/7/13.
 */

public class HandlerActivity extends Activity {
    Handler childHandler;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        new Thread1().start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = childHandler.obtainMessage();
                msg.obj = " eeeee";
                childHandler.sendMessage(msg);
            }
        }).start();
        
    }

    class Thread1 extends Thread {
        @Override
        public void run() {
            super.run();
            Looper.prepare();

            childHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.i("---tag", "msg: " + msg.obj);
                }
            };
            Looper.loop();
        }
    }
    
    
}
