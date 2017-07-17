### AsyncTask###
>对thread 、handler的封装
>
>内部有两个线程池，默认SERIAL_EXECUTOR 串行线程池， THREAD_POOL_EXECUTOR 并行线程池

1. AsyncTask实际上是对java.util.concurrent包里Executor，Callable，FutureTask以及Handler的一个综合应用，属于简化开发人员流程的一个工具类。


	new BitmapTask().execute(ivPic,url);//从这启动AsyncTask

**AsyncTask和线程池的封装**
		
		class BitmapTask extends AsyncTask<Void,Void,Void>{
		
		 *第一个泛型:参数类型
		 *第二个:跟新进度
		 *第三个:OnPostExecute返回结果
		
		*后台运行,在子线程中运行
				doInBackground(ImageView ivPic,String url)
		*更新进度,在主线程中运行
				onProgressUpdate()
		*耗时方法结束后,执行该方法,主线程
				onPostExecute()
		*在主线程调用,做一些预处理的事情,例如初始化进度圈等.
				onPreExecute()
		}

1 原理 内部实现

 * 每次调用AsynTask的execute的方法都新开启一个线程么?
 
 * execute方法只能调用一次(调用多次会发生异常)

 * AsyncTask中遇到的Bug


 Future 接口

 FutureTask 

 callable 接口

   基于线程池和handler的机制,在线程池执行,回调handler的方法

**AsnycTask的构造方法**


	public AsyncTask(){
		mWorker = new WorkerRunnable<Params,Result>(){
			public Result call() throws Exception{
				//设置task为invoked；
				mTaskInvoked.set(true);
				//指定线程的优先等级,把此进程设置为后台的一个进程
				Process.setThreadPrioryty(Process.THREAD_PRIORYTY_BACKgROUND);
				//调用doInBackground()方法 , 并将结果发布到UI线程--->由此可以看出为什么doInBackground()会在后台进行
				return postResult(doInBackground(mParams));
			}
		};

		//FutureTask
		//mFuture表示一个可取消的异步操作，这里指的就是mWorker；
		mFuture = new FutureTask<Result>(mWorker){
			@Override
			protected void done(){
				try{
					postResultIfNotInvoked(get());
				} catch (){
					android.util.Log.w(LOG_TAG,e);
					}
			}
		}
	}

	Status 是一个枚举类型
	// Indicates that the task has not been executed yet.(说明此任务还没有执行)
	Status.PENDING 
	//Indicates that the task is running.(说明此任务正在运行)
	Status.RUNNING 
	//Indicates that {@link AsyncTask#onPostExecute} has finished.(说明此任务已经结束)
	Status.FINISHED 

	mWorker是一个抽象类实现了Callable<Result>接口，还有个Params类型的数组字段mParams；

	


execute()方法中会调用另外一个executeOnExecutor()方法

	executeOnExecutor(){
		if(mStatus != Status.PENDING){
			switch(mStatus){
				case RUNNING:
				throw new IllegalStateException()...
				case FINISHED:
				throw new ...

			}
		}
		//讲mStatus 设置为Status.RUNNING;
		mStatus = Status.RUNNING;
		//在主线程调用,做一些预处理的事情,例如初始化进度圈等.
		onPreExecute();
		//传参(下载链接等),执行任务   ---->从这就解释了为什么方法只能调用一次.
		mWorker.mParams = params;
		exec.execute(mFuture);
	
		return this;
	}
  -------------------^^^^^^真正的异步操作也就是在此段代码中执行^^^^^^-------------------

进入execute看内部实现
	
	public final AsyncTask<Params,Progress,Result> execute(Params... params){
		return executeOnExecutor(sDefaultExecutor,params);
	}
execute 是 sDefaultExecutor的一个实例 sDefaultExecutor表示默认的Executor，即串行执行的；

	private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR;

<  volatile是一个类型修饰符（type specifier）。
它是被设计用来修饰被不同线程访问和修改的变量。如果没有volatile，基本上会导致这样的结果：要么无法编写多线程程序，要么编译器失去大量优化的机会。>

	private static class SerialExectuor implements Executor{
		final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
		Runnable mActive;

		public synchronized void execute(final Runnable r){
			mTask.offer(new Runnalbe(){
				public void run(){
					try{
						r.run();
					}finnaly{
						scheduleNext();
					}
				}
			});
			if(mActive == null){
				scheduleNext();
			}
		}

		protected synchronized void scheduleNext(){
			if((mActive = mTasks.poll()) != null){
				THREAD_POOL_EXECUTOR.execute(mActive);
			}	
		}
	}
	//Executor that can be used to execute tasks in parallel. 可以平行分配的执行任务(多线程处理)
	public static final Executor THREAD_POOL_EXECUTOR 
			= new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE,
					TimeUnit.SECONDS,sPoolWorkQueue,sThreadFactory);

	// 当前可用线程  老师讲过
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	//CORE_POOL_SIZE的取值，为cpu的数目加1，这样做可以刚好保持cpu忙碌，最大限度的提高cpu利用率。
	private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

--------------------------------------

	 private void postResultIfNotInvoked(Result result) {
        final boolean wasTaskInvoked = mTaskInvoked.get();
        if (!wasTaskInvoked) {
            postResult(result);
        }
    }


	private Rusult postResult(Result result){
		Message message = sHandler.obtainMessage(MESSAGE_POST_RESUTL,
							new AsyncTaskResult<Result>(this,result));
		message.sendToTarget();
		return result;
	}

	 protected final void publishProgress(Progress... values) {
        if (!isCancelled()) {
            sHandler.obtainMessage(MESSAGE_POST_PROGRESS,
                    new AsyncTaskResult<Progress>(this, values)).sendToTarget();
        }
    }


前者只在task没被invoked的时候post，postResult的具体实现是obtain一个Message（将result包装在Message中），发送给sHandler处理；
 
publishProgress可以将后台线程的进度周期性的汇报给UI线程，可以用来更新UI显示，其实现也都是发送Message到sHandler，但是是在任务
 
没被取消的前提下。


----------------------------


*内部的InternalHandler对象*

----------------------------------------------------------------------------


			private void finish(Result result) {
			        if (isCancelled()) {
			            onCancelled(result);
			        } else {
			            onPostExecute(result);
			        }
			        mStatus = Status.FINISHED;
			    }
			 
			    private static class InternalHandler extends Handler {
			        @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
			        @Override
			        public void handleMessage(Message msg) {
			            AsyncTaskResult result = (AsyncTaskResult) msg.obj;
			            switch (msg.what) {
			                case MESSAGE_POST_RESULT:
			                    // There is only one result
			                    result.mTask.finish(result.mData[0]);
			                    break;
			                case MESSAGE_POST_PROGRESS:
			                    result.mTask.onProgressUpdate(result.mData);
			                    break;
			            }
			        }
			    }
			 
			    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
			        Params[] mParams;
			    }
			 
			    @SuppressWarnings({"RawUseOfParameterizedType"})
			    private static class AsyncTaskResult<Data> {
			        final AsyncTask mTask;
			        final Data[] mData;
			 
			        AsyncTaskResult(AsyncTask task, Data... data) {
			            mTask = task;
			            mData = data;
			        }
			    }
			}


可以看出finish方法是UI线程在收到MESSAGE_POST_RESULT消息的时候调用，如果取消了则调用onCancelled(result)，否则调用
 
onPostExecute(result)；最后都设置了task的状态为FINISHED。同样的，onProgressUpdate是在收到MESSAGE_POST_PROGRESS
 
被调用的。最后的2个类都是持有数据的类，WorkerRunnable<Params, Result>是一个Callable<Result>并且持有后台task运行需要用到
 
的参数列表，后台线程post Message的时候会将结果包装成一个AsyncTaskResult发送出去。

----------------------------------------------------

2 使用过程中,为什么会产生相关异常信息,并能解决这些异常

3 了解各个android版本当中,AsyncTask实现机制的不同

	2.3 之前的版本
		
核心线程就是5,最大能开128线程
 弊端:如果已经提交了5个任务,现在要提交第六个任务的话.第六个任务就需要的等了,
2.3之前的版本是没法解决这个问题
	

	2.3 之后的版本  

在3.0-4.0几个版本的策略是:
构建一个SerialExecutor对象内部有ArrayDeque队列,
   所有的提交的任务都是往这个队列当中添加的,也就是只有前一个任务只想能够完成后,下一个任务才会执行.
 弊端:所有的任务都是在单线程上执行
 解决方案 :指定一个线程池对象去处理任务.
	executeOnExecutor(Executors.newFixedThreadPool(10),url);//固定10个线程

	

4 解决AsyncTask相关的面试

#### new 多个AsyncTask 对象会在多个线程上执行么?

	2.3之前的版本会直接开启5个线程去启动AsyncTask,如果启动多个任务的情况下,会把多余任务放在等待队列当中,而当等待队列也堆满了的时候,将会启动128个线程数,128个线程是最大的线程数,128个线程也满了的话 会抛出异常

	2.3之后的版本 AsyncTask执行的顺序是一个一个往线程池中提交,无论启动多少个任务也不会抛出异常,只有一个任务执行之后才会执行第二个任务

####AsyncTask的任务执行是单线程还是多线程?

		Android1.6以后更新让AsyncTask获得了并行的能力，但是为了避免因为并行引起的大部分应用程序错误自3.0开始让所有AsyncTask运行在一个单独的线程中，也就是说所有的AsyncTask是串行的
		如果你真的想要使AsyncTask并行，可以使用：
		executeOnExecutor(java.util.concurrent.Executor, Object[])
		参数传入一个线程池即可，如：
		ExecutorService pool = Executors.newCachedThreadPool();
			
####如何破解AsyncTask的单线程问题

	前提 : AsyncTask做大规模的并发任务的时候

	1:采用线程池
	2:版本区分,2.3之前用线程池,2.3以后用AsyncTask处理(但是都是单线程处理)

#####什么时候用AsyncTask,什么时候用Thread问题

	AsyncTask的好处
		简单灵活,减少线程上的交互.不用考虑更新UI的问题
		坏处:版本的不同,可能会导致一些问题,在有些版本上单线程运行的.
	什么时候用Thread?
	1.党有大量的瞬时任务的时候,就应该考虑使用线程池了.(节省了线程的创建和销毁)
