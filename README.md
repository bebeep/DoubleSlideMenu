最近手上不忙，想研究一下侧滑菜单，扒拉到一篇博客

https://blog.csdn.net/qq_30948129/article/details/52282451

瞬间感觉ViewDragHelper的强大。
于是想着能不能改造一波，弄成可以根据需要从左侧或者右侧滑出的效果？
在捣鼓了一阵之后，发现只要少许的改动即可实现需求，效果如下：


<img src="https://github.com/bebeep/DoubleSlideMenu/blob/master/screenshots/screenshots.gif" width="300"></img>


使用示例：

布局使用：
mainLayout要设置背景颜色或者填充满布局，不然在首次加载布局时会显示菜单布局。
切记，无论是左侧滑出还是右侧滑出，菜单布局一定要放在前面，如下所示:
```java
 <com.bebeep.slidemenu.DoubleSlideMenu
        android:id="@+id/mDoubleSlideMenu"
        android:layout_width="match_parent"
        android:layout_height="match_parent">


        <!--菜单-->
        <FrameLayout
            android:id="@+id/fl_menu"
            android:layout_width="200dp"
            android:layout_height="match_parent"
            android:background="@color/yellow">
            
	    ···

        </FrameLayout>

        <!--主界面-->
        <com.bebeep.slidemenu.MainLayout
            android:id="@+id/mMain"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@color/green">
            
	    ···

        </com.bebeep.slidemenu.MainLayout>
    </com.bebeep.slidemenu.DoubleSlideMenu>
```

可以使用的方法:

```java
//将主layout设置到Mainlayout中，用来处理触摸拦截事件；
mMainLayout.setMySlideMenu(mDoubleSlideMenu);

//设置左滑还是右滑，可选DoubleSlideMenu.DragState.LEFT或者DoubleSlideMenu.DragState.RIGHT
mDoubleSlideMenu.setMenuLocation(DoubleSlideMenu.DragState.LEFT);

//设置菜单栏在屏幕中的占比，默认0.6f,即占屏幕60%的宽度，不能小于0或者大于1
mDoubleSlideMenu.setOffsetX(0.6f);

//侧边菜单的滑动事件监听
binding.msmParent.setOnDragstateChangeListener(new DoubleSlideMenu.onDragStateChangeListener() {
       @Override
       public void onOpen() {
           Log.e("TAG","open");
       }

       @Override
       public void onClose() {
           Log.e("TAG","close");
       }

       @Override
       public void onDraging(float fraction) {
           //Log.e("TAG","Draging:"+fraction);
       }
});        
```
如何引入到项目中：

方法一（推荐）、直接在app的build.gradle中添加：
```java
dependencies {
     ···
     implementation 'com.bebeep.slidemenu:slidemenu:1.2'
}
```

方法二、jitpack大法：

第1步，先在工程的主build.gradle添加如下配置：

```Java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```      

第2步，在app的build.gradle中添加一下依赖：
```java
dependencies {
        implementation 'com.github.bebeep:DoubleSlideMenu:v1.0'
}
```
      
方法三、直接下载本项目，然后将依赖slidemenu引入自己的项目中。


      
