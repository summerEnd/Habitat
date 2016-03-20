package com.sumauto.common.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class ActivityDismissUtils {

    public static SwipeDismiss getSwipeDismiss(Activity activity) {
        return new SwipeDismiss(activity);
    }

    public static class SwipeDismiss {
        private float closeFactor = 0.6f;
        private float touchFactor = 0.1f;
        float x      = 0;
        float startX = 0;
        Activity activity;
        private       boolean handleSlide = false;
        private       Point   outSize     = new Point();
        private final int     MAX_ALPHA   = 120;

        public SwipeDismiss(Activity activity) {
            this.activity = activity;
            activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);
        }

        /**
         * @param closeFactor 关闭页面的临界点，取值范围0-1
         */
        public void setCloseFactor(float closeFactor) {
            this.closeFactor = closeFactor;
        }

        /**
         * @param touchFactor 处理触摸事件起点，取值范围0-1
         */
        public void setTouchFactor(float touchFactor) {
            this.touchFactor = touchFactor;
        }

        /**
         * 处理触摸事件
         * @param ev
         */
        public void process(MotionEvent ev) {
            final View contentView = getContentView();
            float eventX = ev.getX();
            float currentPosition = eventX / contentView.getWidth();//当前位置 用0-1数值表示

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    this.x = eventX;
                    this.startX = eventX;
                    if (currentPosition < touchFactor) {
                        handleSlide = true;
                    }

                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (handleSlide && eventX > startX) {
                        float a = (outSize.x - x) / outSize.x;//1-0
                        int alpha = (int) (a * MAX_ALPHA);
                        contentView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
                        contentView.scrollBy((int) (this.x - eventX), 0);
                        this.x = eventX;
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    if (handleSlide) {
                        if (eventX-startX==0)return;
                        float fromXValue = x / contentView.getWidth();
                        final float toXValue;
                        if (currentPosition > closeFactor) {
                            toXValue = 1;
                        }
                        else {
                            toXValue = 0;
                        }
                        finishWithAnimation(fromXValue, toXValue);
                    }
                    handleSlide = false;
                    break;
                }
            }
        }



        private void finishWithAnimation(float from, final float to) {
            TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, from, Animation.RELATIVE_TO_SELF, to, 0, 0, 0, 0);
            animation.setDuration(400);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (to == 1) {
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            View contentView = getContentView();
            contentView.startAnimation(animation);
            contentView.setScrollX(0);
        }
        private View getContentView() {return ((ViewGroup) activity.getWindow().getDecorView()).getChildAt(0);}
    }
}
