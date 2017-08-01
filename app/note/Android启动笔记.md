# Android系统启动流程
* 当系统引导程序启动Linux内核，内核会记载各种数据结构，和驱动程序，加载完毕之后，Android系统开始启动并加载第一个用户级别的进程：init（system/core/init/Init.c）

* 查看Init.c代码，看main函数

		int main(int argc, char **argv)
		{
	   		 ...
			//执行Linux指令
		    mkdir("/dev", 0755);
		    mkdir("/proc", 0755);
		    mkdir("/sys", 0755);
	
	      	...
	    	//解析执行init.rc配置文件
	    	init_parse_config_file("/init.rc");
			...
		}

* 在init.rc中定义好的指令都会开始执行，其中执行了很多bin指令，启动系统服务

		//启动孵化器进程，此进程是Android系统启动关键服务的一个母进程
		service zygote /system/bin/app_process -Xzygote /system/bin --zygote --start-system-server
    	socket zygote stream 666
   	 	onrestart write /sys/android_power/request_state wake
    	onrestart write /sys/power/state on
    	onrestart restart media
    	onrestart restart netd

* 在app_process文件夹下找到app_main.cpp，查看main函数，发现以下代码

		int main(int argc, const char* const argv[])
		{
	   		...
			//启动一个系统服务：ZygoteInit
	        runtime.start("com.android.internal.os.ZygoteInit",startSystemServer);
			...
		}

* 在ZygoteInit.java中，查看main方法
```
		 public static void main(String argv[]) {
			...
			//加载Android系统需要的类
			preloadClasses();
			...
			if (argv[1].equals("true")) {
				//调用方法启动一个系统服务
                startSystemServer();
            }
			...
		}
```

* startSystemServer()方法的方法体

		String args[] = {
            "--setuid=1000",
            "--setgid=1000",
            "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,3001,3002,3003",
            "--capabilities=130104352,130104352",
            "--runtime-init",
            "--nice-name=system_server",
            "com.android.server.SystemServer",
        };

		...
		//分叉启动上面字符串数组定义的服务
		 pid = Zygote.forkSystemServer(
         parsedArgs.uid, parsedArgs.gid,
         parsedArgs.gids, debugFlags, null,
         parsedArgs.permittedCapabilities,
         parsedArgs.effectiveCapabilities);
* SystemServer服务被启动

		public static void main(String[] args) {
			...
			//加载动态链接库
			 System.loadLibrary("android_servers");
        	//执行链接库里的init1方法
			init1(args);
			...
		}

* 动态链接库文件和java类包名相同，找到com_android_server_SystemServer.cpp文件
* 在com_android_server_SystemServer.cpp文件中，找到了

		static JNINativeMethod gMethods[] = {
		    /* name, signature, funcPtr */
			//给init1方法映射一个指针，调用system_init方法
		    { "init1", "([Ljava/lang/String;)V", (void*) android_server_SystemServer_init1 },
		};

* android_server_SystemServer_init1方法体中调用了system_init，system_init没有方法体
* 在system_init.cpp文件中找到system_init方法，方法体中
		//执行了SystemServer.java的init2方法
		runtime->callStatic("com/android/server/SystemServer", "init2");

* 回到SystemServer.java，在init2的方法体中

		//启动一个服务线程
		Thread thr = new ServerThread();
        thr.start();

* 在ServerThread的run方法中
		
		//准备消息轮询器
		Looper.prepare();
		...
		//启动大量的系统服务并把其逐一添加至ServiceManager
		ServiceManager.addService(Context.WINDOW_SERVICE, wm);
		...
		//调用systemReady，准备创建第一个activity
		 ((ActivityManagerService)ActivityManagerNative.getDefault())
                .systemReady(new Runnable(){
				...
		}）；

* 在ActivityManagerService.java中，有systemReady方法，方法体里找到

		//检测任务栈中有没有activity，如果没有，创建Launcher
		mMainStack.resumeTopActivityLocked(null);
* 在ActivityStack.java中，方法resumeTopActivityLocked

		// Find the first activity that is not finishing.
        ActivityRecord next = topRunningActivityLocked(null);
        ...
        if (next == null) {
            // There are no more activities!  Let's just start up the
            // Launcher...
            if (mMainStack) {
                return mService.startHomeActivityLocked();
            }
        }
		...

---
#Handler消息机制
* Message类的obtain方法
	* 消息队列顺序的维护是使用单链表的形式来维护的
	* 把消息池里的第一条数据取出来，然后把第二条变成第一条

		 if (sPool != null) {
                Message m = sPool;
                sPool = m.next;
                m.next = null;
                sPoolSize--;
                return m;
            }

* 创建Handler对象时，在构造方法中会获取Looper和MessageQueue的对象

		public Handler() {
	        ...
			//拿到looper
	        mLooper = Looper.myLooper();
	        ...
			//拿到消息队列
	        mQueue = mLooper.mQueue;
	        mCallback = null;
    	}
	
* 查看myLooper方法体，发现Looper对象是通过ThreadLocal得到的，在查找ThreadLocal的set方法时发现
	* Looper是直接new出来的，并且在Looper的构造方法中，new出了消息队列对象

			sThreadLocal.set(new Looper());
	
			private Looper() {
		        mQueue = new MessageQueue();
		        mRun = true;
		        mThread = Thread.currentThread();
	   		}
	* sThreadLocal.set(new Looper())是在Looper.prepare方法中调用的

* prepare方法是在prepareMainLooper()方法中调用的

		public static final void prepareMainLooper() {
       		prepare();
			...
		}

* 在应用启动时，主线程要被启动，ActivityThread会被创建，在此类的main方法中

		public static final void main(String[] args) {
        	...
			//创建Looper和MessageQueue
        	Looper.prepareMainLooper();
			...
			//轮询器开始轮询
			Looper.loop();
			...
		}
* Looper.loop()方法中有一个死循环

		while (true) {
			//取出消息队列的消息，可能会阻塞
            Message msg = queue.next(); // might block
			...
			//解析消息，分发消息
			msg.target.dispatchMessage(msg);
			...
		}


* Linux的一个进程间通信机制：管道（pipe）。原理：在内存中有一个特殊的文件，这个文件有两个句柄（引用），一个是读取句柄，一个是写入句柄
* 主线程Looper从消息队列读取消息，当读完所有消息时，进入睡眠，主线程阻塞。子线程往消息队列发送消息，并且往管道文件写数据，主线程即被唤醒，从管道文件读取数据，主线程被唤醒只是为了读取消息，当消息读取完毕，再次睡眠

* Handler发送消息，sendMessage的所有重载，实际最终都调用了sendMessageAtTime

		public boolean sendMessageAtTime(Message msg, long uptimeMillis)
    	{
	       ...
			//把消息放到消息队列中
            sent = queue.enqueueMessage(msg, uptimeMillis);
		   ...
		}

* enqueueMessage把消息通过重新排序放入消息队列

		final boolean enqueueMessage(Message msg, long when) {
	        ...
	        final boolean needWake;
	        synchronized (this) {
	           ...
				//对消息的重新排序，通过判断消息队列里是否有消息以及消息的时间对比
	            msg.when = when;
	            
	            Message p = mMessages;
				//把放入消息队列的消息置为消息队列第一条消息
	            if (p == null || when == 0 || when < p.when) {
	                msg.next = p;
	                mMessages = msg;
	                needWake = mBlocked; // new head, might need to wake up
	            } else {
					//判断时间顺序，为刚放进来的消息寻找合适的位置
	                Message prev = null;
	                while (p != null && p.when <= when) {
	                    prev = p;
	                    p = p.next;
	                }
	                msg.next = prev.next;
	                prev.next = msg;
	                needWake = false; // still waiting on head, no need to wake up
	            }
	        }
			//唤醒主线程
	        if (needWake) {
	            nativeWake(mPtr);
	        }
	        return true;
	    }

* Looper.loop方法中，获取消息，然后分发消息

		//获取消息队列的消息
		 Message msg = queue.next(); // might block
         ...
		//分发消息，消息由哪个handler对象创建，则由它分发，并由它的handlerMessage处理  
         msg.target.dispatchMessage(msg);

* message对象的target属性，用于记录该消息由哪个Handler创建，在obtain方法中赋值

---
#AsyncTask机制
* onPreExeCute
* doInBackground
* onPostExecute

* AsyncTask的execute方法，启动异步任务的地方，方法体中

		public final AsyncTask<Params, Progress, Result> execute(Params... params) {
	        ...
	
	        mStatus = Status.RUNNING;
	
			//调用onPreExecute方法
	        onPreExecute();
	
			//把参数赋值给mWorker对象
	        mWorker.mParams = params;
			//线程池对象执行mFuture
	        sExecutor.execute(mFuture);
	
	        return this;
	    }

* mWorker是什么类型？，在AsyncTask的构造方法中

		mWorker = new WorkerRunnable<Params, Result>() {
            public Result call() throws Exception {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                return doInBackground(mParams);
            }
        };

* 然后把mWorker对象封装至FutureTask对象

		mFuture = new FutureTask<Result>(mWorker)

* 在FutureTask的构造中，又把mWorker封装给Sync对象

		public FutureTask(Callable<V> callable) {
        if (callable == null)
            throw new NullPointerException();
        	sync = new Sync(callable);
    	}
* 在Sync的构造方法中

		Sync(Callable<V> callable) {
			//这里的callable就是mWorker
            this.callable = callable;
        }

* 线程池执行mFuture对象，此对象是FutureTask的对象，而FutureTask实现了Runnable接口

		public final AsyncTask<Params, Progress, Result> execute(Params... params) {
        ...

		//线程池对象执行mFuture
        sExecutor.execute(mFuture);
		...
        
    }

* mFuture的run方法被调用了

		public void run() {
        	sync.innerRun();
    	}

* 在innerRun方法中，调用了callable的call方法，但是在sync被new出来的时候，在构造方法中就已经把mWorker赋值给了callable，所以实际上是调用mWorker的call方法

		void innerRun() {
            ...
				//调用mWorker的call()
                result = callable.call();
                
                set(result);
            ...
        }

* mWorker的call在mWorker被new出来时就已经重写了

		mWorker = new WorkerRunnable<Params, Result>() {
            public Result call() throws Exception {
               ...
				//在子线程中调用了doInBackground方法
                return doInBackground(mParams);
            }
        };

* call方法调用完毕后，得到doInBackground所返回的result

		void innerRun() {
            ...
                result = callable.call();
                //返回的result传入了set方法
                set(result);
            ...
        }

* set方法体

		protected void set(V v) {
       		 sync.innerSet(v);
    	}

* innerSet方法体

		 if (compareAndSetState(s, RAN)) {
                    result = v;
                    releaseShared(0);
					//关键的done方法
                    done();
                    return;
          }
* innerSet方法是属于FutureTask类的，那么done方法也是调用FutureTask类的，这个done方法定义的地方，在AsyncTask.java的构造方法里

		mFuture = new FutureTask<Result>(mWorker) {
			//此处重写done方法
            @Override
            protected void done() {
                

               //获取doInbackground方法返回的结果
                result = get();

				//创建一个消息
                message = sHandler.obtainMessage(MESSAGE_POST_RESULT,
                        new AsyncTaskResult<Result>(AsyncTask.this, result));
				//把这条消息发送给创建这个消息的Handler：target.sendMessage(this)
                message.sendToTarget();
            }
        };

* 然后sHandler的handlerMessage被触发

		public void handleMessage(Message msg) {
            AsyncTaskResult result = (AsyncTaskResult) msg.obj;
            switch (msg.what) {
                case MESSAGE_POST_RESULT:
                    //调用finish方法
                    result.mTask.finish(result.mData[0]);
                    break;
               
            }
        }

* finish的方法体

		private void finish(Result result) {
       	 if (isCancelled()) result = null;
			//调用onPostExecute方法，并传入结果
        	onPostExecute(result);
        	mStatus = Status.FINISHED;
    	}