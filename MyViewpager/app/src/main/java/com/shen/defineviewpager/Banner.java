package com.shen.defineviewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pp on 2018/4/22.
 */

public class Banner extends ViewGroup {

    //子view的个数
    private int childCount;
    //子view的高度
    private int childHeight;
    //子view的宽度
    private int childWidth;

    //移动前的横坐标
    private int x;
    //当前图片的索引位置
    private int index = 0;

    //是否自动轮播，true表示自动轮播，false表示不轮播
    private boolean isAuto = true;

    //判断是否为单击事件
    private boolean isClick = false;

    private Timer timer = new Timer();
    private TimerTask task;

    public static int TIME_INTERVAL = 3000;

    private Scroller scroller;

    //点击事件
    private ImageBannerListener listener;
    //通知当前的图片索引
    private ImageBannerSelect imageBannerSelect;

    private boolean isFirstOpen = true;

    //当为true时，表示index正在增长
    private boolean hasAddIndex = true;

    public interface ImageBannerListener{
        void OnClickListener(int pos);
    }

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScroller();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();
        if(childCount ==0){
            setMeasuredDimension(0, 0);
            return;
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        View childView = getChildAt(0);
        childWidth = childView.getMeasuredWidth();
        childHeight = childView.getMeasuredHeight();
        setMeasuredDimension(childWidth * childCount, childHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){
            int leftMargin = 0;
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                view.layout(leftMargin +childWidth * i, 0, leftMargin + childWidth * (i+1), childHeight);
            }
        }
    }

    private Handler autoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if(isFirstOpen){
                        isFirstOpen = false;
                        return;
                    }
                    int preX = index * childWidth;
                    carouselOrder();
                    int currentX = index * childWidth - preX;
                    scroller.startScroll(preX, 0, currentX, 0);
                    invalidate();
                    imageBannerSelect.selectImage(index);
                    break;
                case 1:
                    break;
            }

        }
    };

    private void carouselOrder(){
        if(hasAddIndex){
            if(index >=childCount-1 || ++index >= childCount-1){
                Log.e("++index", index +"");
                hasAddIndex = false;
            }
        } else {
            if(index <=0 ||--index <= 0){
                Log.e("--index", index +"");
                hasAddIndex = true;
            }
        }
    }


    /**
     * 初始化滑动
     */
    private void initScroller(){
        scroller = new Scroller(getContext());
        task = new TimerTask() {
            @Override
            public void run() {
                Log.e("isAuto", isAuto +"");
                if(isAuto){
                    autoHandler.sendEmptyMessage(0);
                }
            }
        };
        timer.schedule(task, 100, TIME_INTERVAL);
    }

    /**
     * onDispatchEvent、onInterceptTouchEvent、onTouchEvent
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isClick = true;
                isAuto = false;
                if(!scroller.isFinished()){
                    scroller.abortAnimation();
                }
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                isClick = false;
                int moveX = (int) event.getX();
                int distance = moveX - x;
//                if((index == 0 && distance > 0) || (index == childCount-1 && distance < 0)){
//                    x =0;
//                } else{
                    scrollBy(-distance, 0);
                    x = moveX;
//                }
                break;
            case MotionEvent.ACTION_UP:
                isAuto = true;
                int scrollx = getScrollX();
                index = (scrollx + childWidth/2)/childWidth;
                if(index < 0){
                    index = 0;
                }else if(index> childCount - 1){
                    index = childCount - 1;
                }
                if(isClick){
                    //单击事件
                    listener.OnClickListener(index);
                }else{
                    int dx = index * childWidth - scrollx;
                    scroller.startScroll(scrollx, 0, dx, 0);
                    postInvalidate();
                    imageBannerSelect.selectImage(index);
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            //是否滑动完毕
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
        }
    }

    //事件的拦截，该方法的返回值为true，自定义的容器会处理此次拦截事件
    //返回false时，点击事件会继续传递下去
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public interface ImageBannerSelect{
        void selectImage(int pos);
    }

    public ImageBannerListener getListener() {
        return listener;
    }

    public void setListener(ImageBannerListener listener) {
        this.listener = listener;
    }

    public ImageBannerSelect getImageBannerSelect() {
        return imageBannerSelect;
    }

    public void setImageBannerSelect(ImageBannerSelect imageBannerSelect) {
        this.imageBannerSelect = imageBannerSelect;
    }
}
