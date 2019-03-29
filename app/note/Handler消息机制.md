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
		    


#### Looper分发处理消息
	* Message对象中有一个Handler类型的成员变量target，这个target是记录此消息对象是由谁创建的
	* 多个Handler给同一个消息队列发送消息，最终消息由谁处理，取决于消息由谁发送

### handler的作用:

1. 主要用于在非UI线程更新UI

2. 可以通过handler主线程中发送延迟消息

### handler源码分析

* 在子线程创建Handler对象,通过handler发送Message到MessageQueue , looper不断循环 发现有消息就通过handler的handleMessage方法处理消息 返回给主线程更新数据

### 首先消息对象是怎么创建的? 
是Message.obtain(this)消息自己创建自己,无论带参还是无参的obtain()方法最后还是是由无参的obtain()方法来创建的.
   
```
			private Message next;
			public static Message obtain(){
				synchronized(){
					if(sPool != null){
						Message m  = sPool;
						sPool = m.next;
						m.next = null;
						sPoolSize--;
						return m;
					}
				}
				return new Message();
			}
```
  sPool是消息池中的第一个消息, 当消息池中有3条消息a b c 时, sPool 指向 a, 就要拿走a, m指向 a,
  当a被拿走的时候,sPool = b , m.next = a 就为null. 然后返回 a . sPool池大小减一.

### Handler的创建
在new Handler()的时候,一般都用无参的Handler的构造方法,在构造方法中就会获取到一个Looper, 和一个 Message Queue;

```   
		mLooper = Looper.myLooper();
		myLooper()中
		return((Looper)sThreadLocal.get());
```
	
Looper从sThreadLocal中get出来
   sThreadLocal.set(new Looper());
  在new Looper的同时调用构造方法

```
	private Looper(){
		mQueue = new MessageQueue();
	}
```
  同时new 出一个 MessageQueue();
  在new 出 Handler的时候 是获取一个已经创建好的Looper.任何应用启动的时候ActivityThread主线程都会开启.在应用启动的时候在main方法里,就会调用prepareMainLooper(),然后创建looper和 MessageQueue;

  Looper.loop(); Looper开始循环检测MessageQueue中有没有message . loop()中 是一个死循环.为什么在主线程死循环为什么不会在主线程发生堵塞,因为这个循环就是主线程.UI的刷新,Activity中的生命方法都是在这个loop()中进行的.主线程真正是运行在此方法中.

  死循环中  第一个进行的方法就是取出第一条消息,但是有可能主线程堵塞,进程间通信的机制就是管道pipe,管道的原理是内存中有一个特殊的文件,特殊文件有两个句柄,一个句柄负责读取,另一个负责写入, 主线程从特殊文件中读取数据,子线程拿着写入句柄往特殊文件中写入数据,就完成了一次数据交互.如果特殊文件里没有数据的话,主线程会进入睡眠,当子线程往特殊文件中写入数据的时候,主线程就会苏醒,然后读取数据.

  如果消息队列没有消息的时候,主线程就会堵塞,进入睡眠的时候,子线程发送消息的时候往管道文件中写入了一个w字符,唤醒主线程.

  所谓发消息就是把消息放到消息队列中
  无论是SendEmptyMessage()还是SendMessageDelay()最终都是调用 sendMessageAtTime()方法,调用了消息队列的enqueueMessage(msg,uptimeMillis)方法.插入msg,一个参数为插入的msg,另一个为插入的时间.
  mMessages---->为消息队列中的第一条消息
  当 p == null时,将msg变为第一条消息,mMessages = msg;
  当 when == 0 时, 

		msg.next = a ;
		mMessages = msg ;
  MessageQueue中出消息的顺序是由when决定的,处理完msg在messagequeue中是什么位置的时候,在 经由Looper分发处理消息.
 		    
