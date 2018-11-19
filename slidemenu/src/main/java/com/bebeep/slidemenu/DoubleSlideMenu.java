package com.bebeep.slidemenu;

import android.animation.FloatEvaluator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

public class DoubleSlideMenu extends FrameLayout {

    private View slideMenu;//菜单栏栏对象
    private View mainMenu;//主界面对象
    private FloatEvaluator floatEvaluator; //浮点数计算器
    private ViewDragHelper viewDragHelper;

    private int width; //控件宽度
    private float dragRange; //拖拽范围
    private float offsetX = 0.6f; //菜单栏的占比，默认为占屏幕的60%

    private DragState menulocation = DragState.LEFT; //滑动菜单栏的位置（控制从左侧滑出还是从右侧滑出）
    private DragState currentState = DragState.STATE_CLOSE;
    private onDragStateChangeListener listener;



    public interface onDragStateChangeListener{
        //菜单栏打开
        void onOpen();
        //菜单栏关闭
        void onClose();
        //菜单栏正在拖拽
        void onDraging(float fraction);
    }


    public DragState getDragState() {
        return currentState;
    }

    public enum DragState{
        STATE_OPEN,
        STATE_CLOSE,
        LEFT,
        RIGHT
    }



    public DoubleSlideMenu(@NonNull Context context) {
        super(context);
        init();
    }


    public DoubleSlideMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public DoubleSlideMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        viewDragHelper = ViewDragHelper.create(this,callback);
        floatEvaluator = new FloatEvaluator();
    }

    /********对外提供的方法********/
    //设置滑动监听
    public void setOnDragstateChangeListener(onDragStateChangeListener listener){
        this.listener = listener;
    }

    //设置菜单栏所在位置
    public void setMenuLocation(DragState dragState){
        menulocation = dragState;
    }

    //设置菜单栏的偏移量 0-1f
    public void setOffsetX(float offsetX){
        if(offsetX<=0 || offsetX >=1) return;
        this.offsetX = offsetX;
    }

    /******** end ********/

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //检测控件异常
        if(getChildCount() !=2){
            throw new IllegalArgumentException("仅能放置两个view");
        }
        slideMenu = getChildAt(0);
        mainMenu = getChildAt(1);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //让viewDragHlper来判断是否拦截
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }





    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        dragRange = width * offsetX;
    }


    private void setSlideMenuLayout(){
        if(menulocation == DragState.LEFT){
            slideMenu.layout((int) (mainMenu.getLeft() - dragRange),0, mainMenu.getLeft(), mainMenu.getMeasuredHeight());
        }else if(menulocation == DragState.RIGHT){
            slideMenu.layout(mainMenu.getRight(),0, (int) (mainMenu.getRight() + dragRange), mainMenu.getMeasuredHeight());
        }
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            return view == slideMenu || view == mainMenu;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return (int) dragRange;
        }


        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if(child == mainMenu){
                if(menulocation == DragState.LEFT){
                    if(left <0) left =0;
                    if(left > dragRange) left = (int) dragRange;
                }else if(menulocation == DragState.RIGHT){
                    if(left>0) left = 0;
                    if(left< - dragRange) left = (int) -dragRange;
                }
            }
            return left;
        }


        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            if(changedView == slideMenu){
                slideMenu.layout(0,0,mainMenu.getMeasuredWidth(), mainMenu.getMeasuredHeight());
                int newleft = mainMenu.getLeft() + dx;
                if(newleft <0) newleft =0;
                if(newleft >dragRange) newleft = (int) dragRange;
            }

            setSlideMenuLayout();

            //计算滑动百分比
            float fraction = mainMenu.getLeft() / dragRange;
            //执行伴随动画
            excuteAnim(fraction);


            //根据百分比值来确定侧滑菜单是打开还是关闭
            if(menulocation == DragState.LEFT){
                ViewHelper.setAlpha(mainMenu,floatEvaluator.evaluate(fraction,1f,0.3f));
                if(fraction ==0 && currentState!= DragState.STATE_CLOSE){
                    currentState = DragState.STATE_CLOSE;
                    if(listener!=null)listener.onClose();
                }else if(fraction==1 && currentState!= DragState.STATE_OPEN){
                    currentState = DragState.STATE_OPEN;
                    if(listener!=null)listener.onOpen();
                }
            }else if(menulocation == DragState.RIGHT){
                ViewHelper.setAlpha(mainMenu,floatEvaluator.evaluate(-fraction,1f,0.3f));
                if(fraction == 0 && currentState!= DragState.STATE_CLOSE){
                    currentState = DragState.STATE_CLOSE;
                    if(listener!=null)listener.onClose();
                }else if(fraction== -1 && currentState!= DragState.STATE_OPEN){
                    currentState = DragState.STATE_OPEN;
                    if(listener!=null)listener.onOpen();
                }
            }

            //当listener不为空的时候将滑动的百分比回调出去
            if(listener!=null){
                listener.onDraging(fraction);
            }
        }





        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if(menulocation == DragState.LEFT){
                if(mainMenu.getLeft() < dragRange /2) close(); //在拖拽范围的左边->关闭
                else open(); //在拖拽范围的右边->打开
                if(xvel > 200 && currentState!= DragState.STATE_OPEN){
                    open();
                }else if(xvel< -200 && currentState != DragState.STATE_CLOSE){
                    close();
                }
            }else if(menulocation == DragState.RIGHT){
                if(mainMenu.getRight() > mainMenu.getMeasuredWidth() - dragRange /2) close(); //在拖拽范围的右边->关闭
                else open(); //在拖拽范围的左边->打开
                if(xvel < -200 && currentState!= DragState.STATE_OPEN){
                    open();
                }else if(xvel > 200 && currentState != DragState.STATE_CLOSE){
                    close();
                }
            }

        }


    };


    public void open(){
        if(menulocation == DragState.LEFT)viewDragHelper.smoothSlideViewTo(mainMenu, (int) dragRange,mainMenu.getTop());
        else if(menulocation == DragState.RIGHT) viewDragHelper.smoothSlideViewTo(mainMenu, (int) -dragRange,mainMenu.getTop());
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void close(){
        viewDragHelper.smoothSlideViewTo(mainMenu,0,mainMenu.getTop());
        ViewCompat.postInvalidateOnAnimation(this);
    }


    @Override
    public void computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    private void excuteAnim(float fraction){
        //移动侧边栏
//        ViewHelper.setTranslationX(slideMenu,intEvaluator.evaluate(fraction,-slideMenu.getMeasuredWidth()/2,0));

        //放大侧边栏
//        ViewHelper.setScaleX(slideMenu,floatEvaluator.evaluate(fraction,0.5f,1f));
//        ViewHelper.setScaleY(slideMenu,floatEvaluator.evaluate(fraction,0.5f,1f));


        //给侧边栏背景添加黑色遮罩效果
//        getBackground().setColorFilter((Integer) ColorUtil.evaluateColor(fraction, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }



}
