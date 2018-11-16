最近手上不忙，想研究一下侧滑菜单，扒拉到一篇博客（https://blog.csdn.net/qq_30948129/article/details/52282451）
瞬间感觉ViewDragHelper的强大。
于是想着能不能改造一波，弄成可以根据需要从左侧或者右侧滑出的效果？
在捣鼓了一阵之后，发现只要少许的改动即可实现需求，效果如下：

![Image text](https://github.com/bebeep/DoubleSlideMenu/blob/master/screenshots/screenshots.gif)

使用方法：
方法1、jitpack大法：
第一步，先在工程的主build.gradle添加如下配置：

```Java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```      
第二步，在app的build.gradle中添加一下依赖：
```java
dependencies {
        implementation 'com.github.bebeep:DoubleSlideMenu:Tag'
}
```
      
方法2、直接下载本项目，然后将依赖slidemenu引入自己的项目中。
      
