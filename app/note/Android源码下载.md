# Android源码下载
> Android源码下载支持的系统目前只有Ubuntu和Mac OS两种操作系统, 本次以Ubuntu系统为例.
> 
> 官方网站: http://source.android.com/source/downloading.html

1. 下载Git(版本控制工具).  调出命令行: ctrl + alt + T

		sudo apt-get install git

2. 安装curl(上传和下载数据的工具).

		sudo apt-get install curl

3. 安装repo(一个基于git的版本库管理工具, 这里用于自动批量下载android整个项目.).

		// 创建目录
		mkdir bin

		// 下载repo脚本到本地bin文件夹下
		curl http://android.git.kernel.org/repo >~/bin/repo
		// 如果上面下载失败, 采用下面这种方式
		curl "http://php.webtutor.pl/en/wp-content/uploads/2011/09/repo" >~/bin/repo

		// 给所有用户追加可执行的权限
		chmod a+x ~/bin/repo

		// 临时把repo添加到环境变量中, 方便后面执行.
		// 注意: 每次重启ubuntu之后此环境变量失效, 重新配置就可以了.
		export PATH=~/bin:$PATH

4. 创建文件夹, 用于存放下载的Android源码.
		
		// 创建目录
		mkdir android_source 

		// 修改权限
		chmod 777 android_source

		cd android_source

5. 初始化库.

		// 需要先配置git的用户信息
		git config --global user.email "zhaokan226@sina.com"
		git config --global user.name "zhaokan"

		repo init -u https://android.googlesource.com/platform/manifest -b android-2.3_r1
		
		// 如果上面初始化失败, 用下面的代码
		repo init -u git://codeaurora.org/platform/manifest.git -b gingerbread
		// 或
		repo init -u git://android.git.kernel.org/platform/manifest.git -b  gingerbread

	######当屏幕出现以下信息表示成功初始化

		repo initialized in /home/liyindong/android_source

6. 开始同步下载.

		repo sync

	**注意: 下载过程中, 因为网络问题, 可能会中断下载. 当中断下载时, 继续使用repo sync命令继续下载.**