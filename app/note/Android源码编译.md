#Android源码编译

### 在编译源码之前需要做一些准备操作, 详细步骤如下:

> #### 1. 安装JDK, google官方要求编译2.3源码需要JDK1.6. 

- 1). 下载JDK1.6, 下载地址:[http://download.oracle.com/otn/java/jdk/6u45-b06/jdk-6u45-linux-x64.bin](http://download.oracle.com/otn/java/jdk/6u45-b06/jdk-6u45-linux-x64.bin "下载JDK1.6")

- 2). 创建目录.

		sudo mkdir /usr/java

- 3). 把下载好的jdk-6u45-linux-x64.bin拷贝到上面创建的目录下.

		sudo cp /home/zhaokan/jdk-6u45-linux-x64.bin /usr/java

- 4). 添加可执行权限.

		sudo chmod 755 /usr/java/jdk-6u45-linux-x64.bin

- 5). 解压.

		cd /usr/java
		sudo ./jdk-6u45-linux-x64.bin

- 6). 配置环境变量.

		export JAVA_HOME=/usr/java/jdk1.6.0_45
		export PATH=$PATH:$JAVA_HOME/bin
		export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

- 7). 验证是否成功.

		zhaokan@zhaokan-VBox:~$ java -version
		java version "1.6.0_45"
		Java(TM) SE Runtime Environment (build 1.6.0_45-b06)
		Java HotSpot(TM) 64-Bit Server VM (build 20.45-b01, mixed mode)

> #### 2. 安装其他编译时依赖的软件. 
> #### 注意: ubuntu自带的源中速度比较慢, 有些软件找不到, 所以需要修改为国内的源, 修改源步骤如下:

* 1). 备份ubuntu自带的源.

		sudo cp /etc/apt/sources.list /etc/apt/sources.list.old

* 2). 修改源文件.

		sudo gedit /etc/apt/sources.list

* 3). 这时会弹出一个文本编辑框, 先删除所有内容, 然后把以下内容拷贝进去, 并保存.

		deb http://mirrors.163.com/ubuntu/ trusty main restricted universe multiverse
		deb http://mirrors.163.com/ubuntu/ trusty-security main restricted universe multiverse
		deb http://mirrors.163.com/ubuntu/ trusty-updates main restricted universe multiverse
		deb http://mirrors.163.com/ubuntu/ trusty-proposed main restricted universe multiverse
		deb http://mirrors.163.com/ubuntu/ trusty-backports main restricted universe multiverse
		deb-src http://mirrors.163.com/ubuntu/ trusty main restricted universe multiverse
		deb-src http://mirrors.163.com/ubuntu/ trusty-security main restricted universe multiverse
		deb-src http://mirrors.163.com/ubuntu/ trusty-updates main restricted universe multiverse
		deb-src http://mirrors.163.com/ubuntu/ trusty-proposed main restricted universe multiverse
		deb-src http://mirrors.163.com/ubuntu/ trusty-backports main restricted universe multiverse
		
		deb http://mirrors.sohu.com/ubuntu/ trusty main restricted universe multiverse
		deb http://mirrors.sohu.com/ubuntu/ trusty-security main restricted universe multiverse
		deb http://mirrors.sohu.com/ubuntu/ trusty-updates main restricted universe multiverse
		deb http://mirrors.sohu.com/ubuntu/ trusty-proposed main restricted universe multiverse
		deb http://mirrors.sohu.com/ubuntu/ trusty-backports main restricted universe multiverse
		deb-src http://mirrors.sohu.com/ubuntu/ trusty main restricted universe multiverse
		deb-src http://mirrors.sohu.com/ubuntu/ trusty-security main restricted universe multiverse
		deb-src http://mirrors.sohu.com/ubuntu/ trusty-updates main restricted universe multiverse
		deb-src http://mirrors.sohu.com/ubuntu/ trusty-proposed main restricted universe multiverse
		deb-src http://mirrors.sohu.com/ubuntu/ trusty-backports main restricted universe multiverse
		
		deb http://mirrors.oschina.net/ubuntu/ trusty main restricted universe multiverse
		deb http://mirrors.oschina.net/ubuntu/ trusty-backports main restricted universe multiverse
		deb http://mirrors.oschina.net/ubuntu/ trusty-proposed main restricted universe multiverse
		deb http://mirrors.oschina.net/ubuntu/ trusty-security main restricted universe multiverse
		deb http://mirrors.oschina.net/ubuntu/ trusty-updates main restricted universe multiverse
		deb-src http://mirrors.oschina.net/ubuntu/ trusty main restricted universe multiverse
		deb-src http://mirrors.oschina.net/ubuntu/ trusty-backports main restricted universe multiverse
		deb-src http://mirrors.oschina.net/ubuntu/ trusty-proposed main restricted universe multiverse
		deb-src http://mirrors.oschina.net/ubuntu/ trusty-security main restricted universe multiverse
		deb-src http://mirrors.oschina.net/ubuntu/ trusty-updates main restricted universe multiverse

- 4). 保存之后, 更新数据源.

		sudo apt-get update

- #### 执行完上面几步, 数据源就更新完成了, 下面就开始安装编译依赖的软件, 同样, 在终端中以行为单位依次输入以下命令: 

		sudo apt-get install gnupg
		sudo apt-get install flex
		sudo apt-get install bison
		sudo apt-get install gperf
		sudo apt-get install zip
		sudo apt-get install curl
		sudo apt-get install build-essential
		sudo apt-get install libesd0-dev
		sudo apt-get install libwxgtk2.8-dev
		sudo apt-get install libsdl-dev
		sudo apt-get install lsb-core
		sudo apt-get install lib32readline-gplv2-dev
		sudo apt-get install g++-multilib
		sudo apt-get install lib32z1-dev
		sudo apt-get install libswitch-perl

> #### 3. 开始编译, 在源码的目录下, 执行一下命令:

		make

		