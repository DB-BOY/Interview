### android启动

> #### 当引导程序启动Linux内核后，会加载各种驱动和数据结构，当有了驱动以后，开始启动Android系统同时会加载用户级别的第一个进程init（system\core\init.c）代码如下：

		int main(int argc, char **argv)
	{
	    
		// 创建文件夹 挂载
	    mount("tmpfs", "/dev", "tmpfs", 0, "mode=0755");
	    mkdir("/dev/pts", 0755);
	   
		// 打卡日志
	    log_init();
	    
	    INFO("reading config file\n");
		// 加载init.rc配置文件
	    init_parse_config_file("/init.rc");
	
	} 

> #### 加载init.rc文件，会启动一个Zygote进程，此进程是Android系统的一个母进程，用来启动Android的其他服务进程，代码：

	service zygote /system/bin/app_process -Xzygote /system/bin --zygote --start-system-server
    socket zygote stream 666
    onrestart write /sys/android_power/request_state wake
    onrestart write /sys/power/state on
    onrestart restart media
    onrestart restart netd

> #### 从c++代码调到java代码：

		int main(int argc, const char* const argv[])
	{
	    ...
		// Android运行时环境
	    AppRuntime runtime;
	    ...
	    // Next arg is startup classname or "--zygote"
	    if (i < argc) {
	        arg = argv[i++];
	        if (0 == strcmp("--zygote", arg)) {
	            bool startSystemServer = (i < argc) ? 
	                    strcmp(argv[i], "--start-system-server") == 0 : false;
	            setArgv0(argv0, "zygote");
	            set_process_name("zygote");
				// 启动java代码
	            runtime.start("com.android.internal.os.ZygoteInit",
	         ...
	
	}
> #### ZygoteInit.java 代码：

 public static void main(String argv[]) {
        try {
            VMRuntime.getRuntime().setMinimumHeapSize(5 * 1024 * 1024);

			// 加载Android依赖的类
            preloadClasses();
            //cacheRegisterMaps();
            preloadResources();
            ...

            if (argv[1].equals("true")) {
				// 启动系统服务
                startSystemServer();
            } else if (!argv[1].equals("false")) {
           ...
    }


	private static boolean startSystemServer()
         ...
            args = new String[] {
                "--setuid=1000",
                "--setgid=1000",
                "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,3001,3002,3003,3006",
                "--capabilities=130104352,130104352",
                "--rlimit=8,",
                "--runtime-init",
                "--nice-name=system_server",
                "com.android.server.SystemServer",
          ...

            /* Request to fork the system server process */
			// 母进程开始分叉服务 启动SystemServer
            pid = Zygote.forkSystemServer(
                    parsedArgs.uid, parsedArgs.gid,
                    parsedArgs.gids, debugFlags, rlimits,
                    parsedArgs.permittedCapabilities,
                    parsedArgs.effectiveCapabilities);
        ..
    }

> #### SystemServer.java 代码

	

    public static void main(String[] args) {
       	... 
		// 加载jni库
        System.loadLibrary("android_servers");
		// 调用native方法
        init1(args);
    }
	native public static void init1(String[] args);

> #### SystemServer 对应的c++代码 com_android_server_SystemServer.cpp 代码如下：
	
	// 类似java的抽象方法
	extern "C" int system_init();
	
	static void android_server_SystemServer_init1(JNIEnv* env, jobject clazz)
	{	
		// 转调
	    system_init();
	}
	
	/*
	 * JNI registration.
	 */
	static JNINativeMethod gMethods[] = {
	    /* name, signature, funcPtr */ 
		// 函数指针 把init1方法映射到android_server_SystemServer_init1
	    { "init1", "([Ljava/lang/String;)V", (void*) android_server_SystemServer_init1 },
	};

> #### system_init 的实现方法在System_init.cpp 代码如下：

	extern "C" status_t system_init()
	{
	    ...
		// 启动硬件的服务
	    if (strcmp(propBuf, "1") == 0) {
	        // Start the SurfaceFlinger
	        SurfaceFlinger::instantiate();
	    }
	
	    
	    
	    AndroidRuntime* runtime = AndroidRuntime::getRuntime();
	
	    LOGI("System server: starting Android services.\n");
		// 启动完硬件服务后，又回到Systemserver的init2方法
	    runtime->callStatic("com/android/server/SystemServer", "init2");
	    ...
	}

> #### SystemServer 的init2方法代码：

	public static final void init2() {
	        Slog.i(TAG, "Entered the Android system server!");
	        Thread thr = new ServerThread();
	        thr.setName("android.server.ServerThread");
	        thr.start();
	    }
> #### ServerThread的run方法：

public void run() {
        ...
		// 开启Android各种服务并且添加到ServiceManager去管理
        Slog.i(TAG, "Device Policy");
        devicePolicy = new DevicePolicyManagerService(context);
        ServiceManager.addService(Context.DEVICE_POLICY_SERVICE, ottle = 

        ...
        // We now tell the activity manager it is okay to run third party
        // code.  It will call back into us once it has gotten to the state
        // where third party code can really run (but before it has actually
        // started launching the initial applications), for us to complete our
        // initialization.
		// 各种服务开启后调用ActivityManagerService.systemReady
        ((ActivityManagerService)ActivityManagerNative.getDefault())
                .systemReady(new Runnable() {
            public void run() {
                Slog.i(TAG, "Making services ready");

> #### ActivityMangerService的systemReady的方法：

	public void systemReady(final Runnable goingCallback) {
	        ...
			// 打开第一个Activity
	            mMainStack.resumeTopActivityLocked(null);
	        }
	    }

> #### ActivityStack的resumeTopActivityLocked方法

	final boolean resumeTopActivityLocked(ActivityRecord prev) {
	        // Find the first activity that is not finishing.
			// 没有已经打开的Activity next为 null
	        ActivityRecord next = topRunningActivityLocked(null);
	
	        // Remember how we'll process this pause/resume situation, and ensure
	        // that the state is reset however we wind up proceeding.
	        final boolean userLeaving = mUserLeaving;
	        mUserLeaving = false;
	
	        if (next == null) {
	            // There are no more activities!  Let's just start up the
	            // Launcher...

	            if (mMainStack) {
					// 启动lucher应用的锁屏界面
	                return mService.startHomeActivityLocked();
	            }
	        }

> #### Android系统启动完成，打开了Luncher应用的Home界面。
	