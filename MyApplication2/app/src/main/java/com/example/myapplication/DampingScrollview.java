package com.example.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;

/**
 * 我的页面滚动自定义视图
 * Created by Administrator on 2017/4/5 0005.
 */
public class DampingScrollview extends ScrollView {
    private ScrollViewListener scrollViewListener;
    private ImageView parallaxImageView;
    private int maxHeight;
    private int originH;

    public DampingScrollview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DampingScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DampingScrollview(Context context) {
        super(context);
    }
    int top;
    // 允许从外部传递视差效果的控件进来
    public void setParallaxImageView(ImageView parallaxImageView) {
        this.parallaxImageView = parallaxImageView;

        Drawable drawable = parallaxImageView.getDrawable();
        if(drawable!=null) {
            maxHeight = drawable.getIntrinsicHeight()+600;// 获取图片的最大高度

            originH = parallaxImageView.getHeight();// 获取图片的起始高度
        }
    }
    int newHeight;
    boolean isLayoutChange=false;
    @Override
    // 当列表被滑动到两端尽头的时候被调用
    // deltaY 两次滑动间的变化大小
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (deltaY < 0 && isTouchEvent) {
             newHeight = parallaxImageView.getHeight() + Math.abs(deltaY / 2);
            if (newHeight < maxHeight) {
                parallaxImageView.getLayoutParams().height = newHeight;
                parallaxImageView.requestLayout();
            }
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                // 将图片从当前高度弹回最初的高度
                final int currentH = parallaxImageView.getHeight();

                // 动态生成一个值的变化
                ValueAnimator animator = ValueAnimator.ofFloat(1);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator value) {
                        int newHeight = evaluate(value.getAnimatedFraction(), currentH, originH);
                        parallaxImageView.getLayoutParams().height = newHeight;
                        parallaxImageView.requestLayout();
                    }
                });
                animator.setDuration(200);
                animator.setInterpolator(new OvershootInterpolator(4));
                animator.start();
                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);// 一定要使用super来返回，因为列表的滚动要由ListView处理
    }

    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int)(startInt + fraction * (endValue - startInt));
    }
    public interface ScrollViewListener {
        void onScrollChanged(DampingScrollview scrollView, int x, int y, int oldx, int oldy);
    }
}
