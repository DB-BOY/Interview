### Android 性能优化

性能调优方式
明白了何为性能问题之后，就能明白性能优化实际就是优化系统的响应时间，提高TPS。优化响应时间，提高TPS。方式不外乎这三大类：

1. 降低执行时间
    * 利用多线程并发或分布式
    * 缓存(包括对象缓存、IO 缓存、网络缓存等)
    * 数据结构和算法优化
    * 性能更优的底层接口调用，如JNI实现
    * 需求优化
    * 逻辑优化
2. 同步改异步，利用多线程
3. 提前或延迟操作

### 布局优化
   
   ``` layout_head.xml
   <?xml version="1.0" encoding="utf-8"?>
   <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:layout_width="match_parent"
       android:layout_height="match_parent" >
    
       <Button
           android:id="@+id/button"
           android:layout_width="match_parent"
           android:layout_height="@dimen/dp_40"
           android:layout_above="@+id/text"/>
    
       <TextView
           android:id="@+id/text"
           android:layout_width="match_parent"
           android:layout_height="@dimen/dp_40"
           android:layout_alignParentBottom="true"
           android:text="@string/app_name" />
   </RelativeLayout>
   
   ```
1. include
   
  ``` xml
  <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="match_parent" >
   
      <ListView
          android:id="@+id/simple_list_view"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:layout_marginBottom="20dp" />
   
      <include layout="@layout/layout_head.xml" />
   
  </RelativeLayout>
  
  ```
   include常用于将布局中的公共部分提取出来供其他layout共用，以实现布局模块化，这在布局编写方便提供了大大的便利。
      
2. viewstub

  ``` xml
  <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent" >
        
      <Button
               android:id="@+id/simple_list_view"
               android:layout_width="wrap_content"
               android:layout_height="wrap_content"
               android:layout_marginTop="20dp" />
               
        <ViewStub
                  android:id="@+id/error_layout"
                  android:layout_width="match_parent"
                  android:layout_height="match_parent"
                  android:layout="@layout/error" />
     
    </RelativeLayout>
        
  ```

   与include类似但是默认不可见，运势是延迟加载，对viewstub调用inflate()方法或者设置可见时，才会加载指定布局。然后添加到俯视图中。
   
3. merge

 在使用了include后可能导致布局嵌套过多，多余不必要的layout节点，从而导致解析变慢，不必要的节点和嵌套可通过hierarchy viewer查看。
 
  __merge标签可用于两种典型情况：__
  
   * 布局顶结点是FrameLayout且不需要设置background或padding等属性，可以用merge代替，因为Activity内容试图的parent view就是个FrameLayout，所以可以用merge消除只剩一个。
   
   * 某布局作为子布局被其他布局include时，使用merge当作该布局的顶节点，这样在被引入时顶结点会自动被忽略，而将其子节点全部合并到主布局中。
   
   
  对于 1)中使用include方式 layout_foot.xml的改造，可以将RelativeLayout合并，减少一层
   ``` 
   <?xml version="1.0" encoding="utf-8"?>
   <merge xmlns:android="http://schemas.android.com/apk/res/android"
       android:layout_width="match_parent"
       android:layout_height="match_parent" >
    
       <Button
           android:id="@+id/button"
           android:layout_width="match_parent"
           android:layout_height="@dimen/dp_40"
           android:layout_above="@+id/text"/>
    
       <TextView
           android:id="@+id/text"
           android:layout_width="match_parent"
           android:layout_height="@dimen/dp_40"
           android:layout_alignParentBottom="true"
           android:text="@string/app_name" />
   </merge>
   
   ```
   
4. 减少树层级

    xml中的控件是以树的形式加载，显示的时候都需要经历measure、layout、 draw的过程，层级越多会造成额外的measure、layout等工作，变得卡顿，
    findViewById的过程也是从树种查找节点的过程，层级越少，查找的效率越高。

5. 过度绘制


6. 总结

    * 去掉不必要的嵌套，尽量使用RelativeLayout
    * ListView中的LinearLayout不要使用layout_weight
    * 可复用布局抽取，使用include标签
    * 首次不可见的，使用viewstub标签
    * 用merge减少布局嵌套层级
    

7. 布局调优工具 hierarchy viewer
 Android studio 中 Tools->Android->Android Device Monitor 
 
### 内存优化



### 代码优化

##### 降低执行时间
   1. 缓存
       * 线程池
       * Handler中使用Message.obtain()，从消息池中获取消息对象
       * http缓存，请求头加入Cache-Control 设置缓存时间
       * 数据缓存，对频繁使用或者大量数据进行缓存(空间换时间)
       * IO缓存，使用BufferInputStream、BufferReader进行IO操作
       * 布局缓存，参考布局优化
       * ListView缓存
      
   2. 存储优化
       * StringBuilder代替String做字符拼接，初始化长度
       * ArrayList、LinkedList、HashMap等存储模型的选择（ArrayList、HashMap初始化容量的选择）
       * WeakReference SoftReference的选择
       * 常量使用final关键字
   
   3. 算法优化
       
      * 根据算法的时间负责度，选择合适的
      * 尽量减少使用递归
      
   4. 需求优化
       
       不和逻辑的，反人类的需求，怼产品
      
   5. 逻辑优化
      
      代码逻辑清晰，多思考
   
##### 同步改异步
> 关于Android的ANR触发条件，解决方法等
   * 多线程(工作线程、UI线程)
   * handler
   * rxava

##### 提前/延迟加载

* 首次调用耗时操作，放到初始化中进行，耗时提前
* 非主要显示的数据，延迟操作，充分使用各种延迟操作的方式，Handler、ScheduledExecutorService、View.postDelayed、AlarmManager等

### 网络优化

1. 直接使用IP，减少DNS解析
    
    首次解析域名需要花费几百毫秒的时间，直接使用IP访问，减少解析时间，防止域名劫持
    
    弊端：动态IP列表，IP不可用时转域名访问
    
2. 缓存图片


3. json数据，避免xml，html

    xml、html需要完整的结束标签，浪费流量，使用json格式的

4. httpTimeOut
5. gzip压缩
6. 减少请求次数，请求合并
7. 请求的Connection是否是keep-alive

    节省连接建立时间，http1.1默认开启
    
8. 请求头的cache-control 、 expires是否有缓存
9. 设置服务器接口响应时间
10. 重定向次数
11. 服务器多地部署


### 数据库优化

1. 建索引
    
   优点: 检索速度加快
   缺点: 创建维护消耗，索引占用物理空间，数据量越大，消耗空间越大

2. 使用事物

   原子级别的提交，要么都做要么都不做，提交失败，事物回滚，默认为每个插入、更新操作创建事务，但是如果显示的创建事务，会将同一个操作合并到一个事务中，一次性操作，性能得到提升。
    
3. 语句优化

   * 不使用select *，返回哪个字段，查那个 
   * 唯一条件放在前 
   * 尽量不适用distinct
   * 使用StringBuffer 代替String

4. 异步
    
    异步操作，避免anr
   
   
### [电量篇](http://hukai.me/android-performance-battery/)

### [渲染篇] (http://hukai.me/android-performance-render/)
 
### [运算篇] (http://hukai.me/android-performance-compute/)
 
### [内存篇] (http://hukai.me/android-performance-memory/)