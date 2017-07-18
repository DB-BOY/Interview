### 自定义view与viewgroup的区别
1. onDraw(Canvas canvas) 
>View类中用于重绘的方法，这个方法是所有View、ViewGroup及其派生类都具有的方法,也是Android UI绘制最重要的方法。开发者可重载该方法，并在重载的方法内部基于参数canvas绘制自己的各种图形、图像效果。
2. onLayout()
> 重载该类可以在布局发生改变时作定制处理，这在实现一些特效时非常有用。View中的onLayout不是必须重写的，ViewGroup中的onLayout()是抽象的，自定义ViewGroup必须重写。
3. dispatchDraw()
>ViewGroup类及其派生类具有的方法，控制子View绘制分发，重载该方法可改变子View的绘制，进而实现一些复杂的视效，典型的例子可参见Launcher模块Workspace的dispatchDraw重载。
4. drawChild()
>ViewGroup类及其派生类具有的方法，直接控制绘制某局具体的子view，重载该方法可控制具体某个具体子View。

### View方法执行过程
>三次measure,两次layout和一次draw 
http://blog.csdn.net/u012422440/article/details/52972825

Android视图树的根节点是DecorView，而它是FrameLayout的子类，所以就会让其子视图绘制两次，所以onMeasure函数会先被调用两次。

* onResume(Activity)
* onPostResume(Activity)
* onAttachedToWindow(View)
* onMeasure(View)
* onMeasure(View)
* onLayout(View)
* onSizeChanged(View)
* onMeasure(View)
* onLayout(View)
* onDraw(View)
* dispatchDraw()

### 事件分发
1. 