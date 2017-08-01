#### AsyncTask
	
	new AsyncTask<String,String,String>(){
		// 运行在主线程中，做预备工作
		onPreExecute(){

		}
		// 运行在子线程中，做耗时操作
		String doingBackGround(String s){

		}
		// 运行在主线程中，耗时操作完成，更新UI
		onPostExecute(String s){
					
		}

	}.execute(String);
	
#### AsyncTask的execute方法

	public final AsyncTask<Params, Progress, Result> execute(Params... params) {
	        ...
	
	        mStatus = Status.RUNNING;
			// 在主线程中执行准备操作
	        onPreExecute();
			// 把params参数赋值给mWorker
	        mWorker.mParams = params;
			// 用线程池执行mFuture
	        sExecutor.execute(mFuture);

	// 在AsyncTask构造方法中创建了mWorker
	    mWorker = new WorkerRunnable<Params, Result>() {
            public Result call() throws Exception {
               ...
            }
        };

        mFuture = new FutureTask<Result>(mWorker) {
            @Override
            protected void done() {
                ...
            }
        };
    }
		// 把mWorker传递给FutureTask，callable指的就是mWorker
		public FutureTask(Callable<V> callable) {
		        if (callable == null)
		            throw new NullPointerException();
		        sync = new Sync(callable);
		    }
		// 把mWorker传递给Sync，callable指的是mWorker
		Sync(Callable<V> callable) {
		            this.callable = callable;
		        }

> #### 线程池执行FutureTask，就是执行FutureTask的run方法,代码如下：

		public void run() {
				// 转调
		        sync.innerRun();
		    }
      
		void innerRun() {
		            if (!compareAndSetState(READY, RUNNING))
		                return;
		
		            runner = Thread.currentThread();
		            if (getState() == RUNNING) { // recheck after setting thread
		                V result;
		                try {
							// 就是调用了mWorker.call方法
							// 把耗时操作得到的结果赋值给result
		                    result = callable.call();
		                } catch (Throwable ex) {
		                    setException(ex);
		                    return;
		                }
						// 转调了sync.innerSet(v);
		                set(result);
		            } else {
		                releaseShared(0); // cancel
		            }
		        }
	
		mWorker = new WorkerRunnable<Params, Result>() {
		            public Result call() throws Exception {
		                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
						// 执行耗时操作 在子线程中执行
		                return doInBackground(mParams);
		            }
		        };

		protected void set(V v) {
				// 转调
		        sync.innerSet(v);
		    }

		void innerSet(V v) {
		            for (;;) {
		                int s = getState();
		                if (s == RAN)
		                    return;
		                if (s == CANCELLED) {
		                    // aggressively release to set runner to null,
		                    // in case we are racing with a cancel request
		                    // that will try to interrupt runner
		                    releaseShared(0);
		                    return;
		                }
		                if (compareAndSetState(s, RAN)) {
		                    result = v;
		                    releaseShared(0);
							// 调用了FutureTask的抽象方法
		                    done();
		                    return;
		                }
		            }
		        }

		mFuture = new FutureTask<Result>(mWorker) {
            @Override
            protected void done() {
                Message message;
                Result result = null;

                try {
					// 转调了sync.innerGet()
                    result = get();
                } catch (InterruptedException e) {
                    android.util.Log.w(LOG_TAG, e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("An error occured while executing doInBackground()",
                            e.getCause());
                } catch (CancellationException e) {
                    message = sHandler.obtainMessage(MESSAGE_POST_CANCEL,
                            new AsyncTaskResult<Result>(AsyncTask.this, (Result[]) null));
                    message.sendToTarget();
                    return;
                } catch (Throwable t) {
                    throw new RuntimeException("An error occured while executing "
                            + "doInBackground()", t);
                }
				// 发送了一个Message
                message = sHandler.obtainMessage(MESSAGE_POST_RESULT,
                        new AsyncTaskResult<Result>(AsyncTask.this, result));
                message.sendToTarget();
            }
        };

		public V get() throws InterruptedException, ExecutionException {
				// 转调
		        return sync.innerGet();
		    }

		V innerGet() throws InterruptedException, ExecutionException {
		            acquireSharedInterruptibly(0);
		            if (getState() == CANCELLED)
		                throw new CancellationException();
		            if (exception != null)
		                throw new ExecutionException(exception);
					// 把之前doinBackground方法的结果返回
		            return result;
		        }
> #### 在AsyncTask的成员变量，创建了InternalHandler
    private static class InternalHandler extends Handler {
        @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
        @Override
        public void handleMessage(Message msg) {
            AsyncTaskResult result = (AsyncTaskResult) msg.obj;
            switch (msg.what) {
                case MESSAGE_POST_RESULT:
                    // There is only one result
					// 结束耗时操作完成后的消息
					// 调用了AsyncTask的finish方法传递的result.mData[0]就是之前
					// 耗时操作返回来的结果
                    result.mTask.finish(result.mData[0]);
                    break;
                case MESSAGE_POST_PROGRESS:
                    result.mTask.onProgressUpdate(result.mData);
                    break;
                case MESSAGE_POST_CANCEL:
                    result.mTask.onCancelled();
                    break;
            }
        }
    }

    private static class AsyncTaskResult<Data> {
        final AsyncTask mTask;
        final Data[] mData;
		// data 是返回的结果
        AsyncTaskResult(AsyncTask task, Data... data) {
            mTask = task;
            mData = data;
        }
    }

    private void finish(Result result) {
        if (isCancelled()) result = null;
		// 耗时操作完成，更新UI，执行在主线程
        onPostExecute(result);
        mStatus = Status.FINISHED;
    }