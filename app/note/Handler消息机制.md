#### Handler消息机制

> #### Message 消息

	Message.obtain()

	Message msg = new Message()

> #### Handler 

	new Handler(){

		handlerMessage(Message msg){
			// 处理消息
		}
	}

> #### Handler的构造方法：

			public Handler() {
		      	...
				// 获取looper
		        mLooper = Looper.myLooper();
		        if (mLooper == null) {
		            throw new RuntimeException(
		                "Can't create handler inside thread that has not called Looper.prepare()");
		        }
		        mQueue = mLooper.mQueue;
		        mCallback = null;
		    }

> #### 主线程设置Looper，在ActivityThread类里面

		public static final void main(String[] args) {
		        ....
				// 1.主线程创建Looper 
		        Looper.prepareMainLooper();
		        if (sMainThreadHandler == null) {
		            sMainThreadHandler = new Handler();
		        }
		
		        ActivityThread thread = new ActivityThread();
		        thread.attach(false);
		
		        if (false) {
		            Looper.myLooper().setMessageLogging(new
		                    LogPrinter(Log.DEBUG, "ActivityThread"));
		        }
		
		        Looper.loop();
> #### Looper

			public static final void prepare() {
	        if (sThreadLocal.get() != null) {
	            throw new RuntimeException("Only one Looper may be created per thread");
	        }
			// 3、在主线程中设置Looper， new Looper()里面创建了一个MessageQueue
	        sThreadLocal.set(new Looper());
    
		    public static final void prepareMainLooper() {
		        // 2、调用prepare
				prepare();
		        setMainLooper(myLooper());
		        if (Process.supportsProcesses()) {
		            myLooper().mQueue.mQuitAllowed = false;
		        }
		    }

> #### 主线程调用Looper.loop()方法,主线程就会阻塞，是一个死循环，使用管道（Pipe），是Linux中的一种进程间通信方式，使用了特殊的文件，有两个文件描述符（一个是读取，一个是写入）

> #### 应用场景；主进程拿着读取描述符等待读取，没有内容时就阻塞，另一个进程拿写入描述符去写内容，唤醒主进程，主进程拿着读取描述符读取到内容，继续执行。

> #### Handler应用场景：Handler在主线程中创建，Looper会在死循环里等待取消息，1、没取到，就阻塞，2、一旦被子线程唤醒，取到消息，就把Message交给Handler处理。子线程用Handler去发送消息，拿写入描述符去写消息，唤醒主线程。



		 public static final void loop() {
		       	...
		        while (true) {
					// 取消息，如果没有消息，就阻塞
		            Message msg = queue.next(); // might block
		            ...

	                msg.target.dispatchMessage(msg);
	                ...
				}
		    }

> #### Handler发送消息代码

		public boolean sendMessageAtTime(Message msg, long uptimeMillis)
		{
		        ....
					// 把Message的target置为当前发送的Handler，以便Looper取到message后根据target把message分发给正确的Handler
					msg.target = this;
					// 往队列里面添加Message
		            sent = queue.enqueueMessage(msg, uptimeMillis);
		        ....
        }

> #### MessageQueue.enqueueMessage 代码

	final boolean enqueueMessage(Message msg, long when) {
        ...
            Message p = mMessages;
            if (p == null || when == 0 || when < p.when) {
				// 当前发送的message需要马上被处理调，needWake唤醒状态置true
                msg.next = p;
                mMessages = msg;
                needWake = mBlocked; // new head, might need to wake up
            } else {
				// 当前发送的message被排队到其他message的后面，needWake唤醒状态置false
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
		// 是否唤醒主线程
        if (needWake) {
            nativeWake(mPtr);
        }
        return true;

> #### Handler.dispatchMessage方法

		public void dispatchMessage(Message msg) {
		        if (msg.callback != null) {
		            handleCallback(msg);
		        } else {
		            if (mCallback != null) {
		                if (mCallback.handleMessage(msg)) {
		                    return;
		                }
		            }
					// 把Message交给Handler处理
		            handleMessage(msg);
		        }
		    }