package com.bebeep.slidemenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MainLayout extends FrameLayout {

    private DoubleSlideMenu mySlideMenu;

    public MainLayout(@NonNull Context context) {
        super(context);
    }


    public MainLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public MainLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setMySlideMenu(DoubleSlideMenu mySlideMenu){
        this.mySlideMenu = mySlideMenu;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if(mySlideMenu !=null && mySlideMenu.getDragState() == DoubleSlideMenu.DragState.STATE_OPEN){
            //如果该侧滑面板是打开，则拦截消费触摸事件
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mySlideMenu !=null && mySlideMenu.getDragState() == DoubleSlideMenu.DragState.STATE_OPEN){
            if(event.getAction() == MotionEvent.ACTION_UP) mySlideMenu.close();//如果该侧滑面板是打开，则拦截消费触摸事件
            return true;
        }
        return super.onTouchEvent(event);
    }
}
