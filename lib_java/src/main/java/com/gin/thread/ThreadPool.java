package com.gin.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by wang.lichen on 2017/7/5.
 */

public class ThreadPool {
    //    ThreadPoolExecutor
    private ThreadPoolExecutor mExecutor;            // 线程池
    private int mCorePoolSize;
    private int mMaximumPoolSize;
    private long mKeepAliveTime;

    public ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        this.mCorePoolSize = corePoolSize;
        this.mMaximumPoolSize = maximumPoolSize;
        this.mKeepAliveTime = keepAliveTime;
    }

    /**
     * 执行任务
     * @param task
     */
    public void execute(Runnable task) {

        initThreadPoolExecutor();
        // 执行线程
        mExecutor.execute(task);
    }

    public Future<?> submit(Runnable task) {
        initThreadPoolExecutor();
        return mExecutor.submit(task);
    }

    private synchronized void initThreadPoolExecutor() {
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
            TimeUnit unit = TimeUnit.MILLISECONDS;
            // BlockingQueue<Runnable> workQueue = new
            // ArrayBlockingQueue<Runnable>(10);// 阻塞队列
            BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();// 阻塞队列

            // BlockingQueue<Runnable> workQueue = new
            // PriorityBlockingQueue(initialCapacity, comparator);// 优先级队列

            // BlockingQueue<Runnable> workQueue = new
            // SynchronousQueue<Runnable>();//

            ThreadFactory threadFactory = Executors.defaultThreadFactory();
            // RejectedExecutionHandler handler = new
            // ThreadPoolExecutor.AbortPolicy();//如果出现错误，则直接抛出异常
            // RejectedExecutionHandler handler = new
            // ThreadPoolExecutor.CallerRunsPolicy();// 如果出现错误，直接执行加入的任务

            // RejectedExecutionHandler handler = new
            // ThreadPoolExecutor.DiscardOldestPolicy();//
            // 如果出现错误,移除第一个任务,执行加入的任务

            RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();// 如果出现错误，不做处理

            mExecutor = new ThreadPoolExecutor(mCorePoolSize,// 核心线程数 : 2
                    mMaximumPoolSize,// 最大线程数 : 4
                    mKeepAliveTime,// 保持的时间长度
                    unit,// keepAliveTime单位
                    workQueue,// 任务队列
                    threadFactory,// 线程工厂
                    handler);// 错误捕获器

        }
    }

    public void remove(Runnable task) {
        if (mExecutor != null) {
            mExecutor.remove(task);
        }
    }

}
